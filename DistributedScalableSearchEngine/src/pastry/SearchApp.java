package pastry;


import java.util.ArrayList;

import dataStructure.MyBlockingHashMap;
import dataStructure.MyBlockingStringPairQueue;
import dataStructure.MyBlockingSearchResponseHashMap;
import dataStructure.StringArrayListPair;
import dataStructure.StringPair;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;

public class SearchApp implements Application{

	NodeFactory nodeFactory;
	Node node;
	Endpoint endpoint;
	MyBlockingStringPairQueue q;
	MyBlockingHashMap map;
	MyBlockingSearchResponseHashMap searchResponseMap;

	public SearchApp(NodeFactory nodeFactory, MyBlockingStringPairQueue q, MyBlockingHashMap map, MyBlockingSearchResponseHashMap searchResponseMap) {
		// TODO Auto-generated constructor stub
		this.nodeFactory=nodeFactory;
		this.node=nodeFactory.getMyNode();
		this.endpoint= node.buildEndpoint(this, "Search App");
		this.endpoint.register();
		this.q=q;
		this.map=map;
		this.searchResponseMap= searchResponseMap;
	}
		
	
	public void sendAddMessage(Id toSendTo, String word, String doc){
		ChordMessage m=new ChordMessage(node.getLocalNodeHandle(), ChordMessage.Type.ADD, word, doc);
		endpoint.route(toSendTo, m, null);
	}
	public void sendSearchMessage(String originalSearch){
		ArrayList<String> words=new ArrayList<String>();
		for(String s:originalSearch.split(" ")){
			words.add(s);
		}
		for(String w:words){
			sendSearchMessage(NodeFactory.getIdFromString(w), w, words);
		}
		
	}
	public void sendSearchMessage(Id toSendTo, String word, ArrayList<String> originalSearch){
		ChordMessage m=new ChordMessage(node.getLocalNodeHandle(), ChordMessage.Type.SEARCH, word, originalSearch);
		endpoint.route(toSendTo, m, null);
	}

	public void sendSearchResponseMessage(Id toSendTo, String word, ArrayList<String> docList, ArrayList<String> originalSearch){
		ChordMessage m=new ChordMessage(node.getLocalNodeHandle(), ChordMessage.Type.SEARCHRESPONSE, word, docList, originalSearch);
		endpoint.route(toSendTo, m, null);
	}

	@Override
	public void deliver(Id destinationId, Message msg){
		ChordMessage m=(ChordMessage) msg;
		switch(m.type){
		case ADD:
			try{
				q.add(new StringPair(m.word,m.doc));
			}catch(Exception e){
				e.printStackTrace();
			}
			break;

			//System.out.println(node.getId()+"Received ping to ID "+destinationId+" from node"+m.from.getId()+" and sending PONG back");
			//ChordMessage reply= new ChordMessage(node.getLocalNodeHandle(), "PONG", ChordMessage.Type.PONG);
			//endpoint.route(null, reply, m.from);
		case SEARCH:
			ArrayList<String> docList = map.remove(m.word);
			sendSearchResponseMessage(m.from.getId(), m.word, docList, m.originalSearch);
			break;
		case SEARCHRESPONSE:
			searchResponseMap.add(m.originalSearch, new StringArrayListPair(m.word, m.docList));
			if(searchResponseMap.containsAll(m.originalSearch)){
				System.out.println(searchResponseMap.intersectDoclist(m.originalSearch));
			}
			break;
		default:
			System.out.println(node.getId()+" Received Invalid Message Type "+m.type+" from "+m.from.getId());
		}
	}

	public boolean forward(RouteMessage arg0){
		return true;
	}

	public void update(NodeHandle arg0, boolean arg1){

	}

}
