package pastry;

import java.util.ArrayList;

import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

@SuppressWarnings("serial")
public class ChordMessage implements Message{
	public static final int DEFAULT_MESSAGE_PRIORITY=1;
	public enum Type{
		ADD	, SEARCH, SEARCHRESPONSE
		
	}
	//can't remove priority because it is needed by Message interface
	int priority;
	NodeHandle from;
	String message;
	Type type;
	String word;
	String doc;
	ArrayList<String> originalSearch;
	ArrayList<String> docList;
	
	//ADD
	public ChordMessage(NodeHandle localNodeHandle, Type type, String word, String doc){
		this.word=word;
		this.from=localNodeHandle;
		this.type=type;
		this.doc=doc;
	}

	//SEARCH
	public ChordMessage(NodeHandle localNodeHandle, Type type, String word, ArrayList<String> originalSearch){
		this.word=word;
		this.from=localNodeHandle;
		this.type=type;
		this.originalSearch=originalSearch;
		
	}
	
	//SEARCH RESPONSE
	public ChordMessage(NodeHandle localNodeHandle, Type type, String word, ArrayList<String> docList, ArrayList<String> originalSearch ){
		this.word=word;
		this.from=localNodeHandle;
		this.type=type;
		this.docList=docList;
		this.originalSearch=originalSearch;

	}
	@Override
	public int getPriority(){
		return priority;
	}


}
