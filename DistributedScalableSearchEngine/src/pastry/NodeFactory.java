package pastry;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import rice.environment.Environment;
import rice.p2p.commonapi.Node;
import rice.pastry.Id;
import rice.pastry.NodeHandle;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;

public class NodeFactory {
	int myPort;
	Environment env;
	NodeHandle bootHandle;
	NodeIdFactory nodeIdFactory;
	SocketPastryNodeFactory pastryNodeFactory;

	public NodeFactory(int myPort, InetSocketAddress bootAddress){
		this(myPort);
		this.bootHandle= pastryNodeFactory.getNodeHandle(bootAddress);
	}

	public NodeFactory(int myPort) {
		// TODO Auto-generated constructor stub
		this.myPort=myPort;
		this.env=new Environment();
		this.nodeIdFactory= new RandomNodeIdFactory(env);
		try{
			this.pastryNodeFactory= new SocketPastryNodeFactory(nodeIdFactory, myPort, env);
		} catch(IOException e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}

	public Node getMyNode(){
		try{
			if(bootHandle==null){
				InetAddress localhost = InetAddress.getLocalHost();
				InetSocketAddress bootAddress= new InetSocketAddress(localhost, myPort);
				this.bootHandle=pastryNodeFactory.getNodeHandle(bootAddress);

			}
			PastryNode node= pastryNodeFactory.newNode(bootHandle);
			//start the Node
			synchronized (node){
				while(!node.isReady()&& !node.joinFailed()){
					node.wait(500);
					if(node.joinFailed()){
						throw new IOException("Could join the factory ring. Reason: "+node.joinFailedReason());
					}
				}
			}
			return node;
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}

	}

	public static Id getIdFromString(String key){
		MessageDigest md=null;
		try{
			md= MessageDigest.getInstance("SHA");			
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		byte[] content=key.getBytes();
		md.update(content);
		byte[] shaDigest= md.digest();
		Id keyId=Id.build(shaDigest);
		return keyId;
	}
}
