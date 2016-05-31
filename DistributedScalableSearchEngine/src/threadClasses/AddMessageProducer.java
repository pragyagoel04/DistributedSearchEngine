package threadClasses;

import java.io.File;
import java.util.Scanner;

import pastry.NodeFactory;
import pastry.SearchApp;
import rice.pastry.routing.SendOptions;
import dataStructure.MyBlockingStringQueue;

public class AddMessageProducer implements Runnable {
	
	MyBlockingStringQueue queue;
	SearchApp searchApp;
	
	public AddMessageProducer(MyBlockingStringQueue queue, SearchApp searchApp){
		this.queue=queue;
		this.searchApp=searchApp;

	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try{
			String doc=queue.remove();
			Scanner sc=new Scanner(new File(doc));
			while(sc.hasNext()){
				String word=sc.next();
				searchApp.sendAddMessage(NodeFactory.getIdFromString(word), word, doc);
			}
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
