package threadClasses;

import dataStructure.MyBlockingHashMap;
import dataStructure.MyBlockingStringPairQueue;
import dataStructure.StringPair;

public class AddMessageConsumer implements Runnable{
	
	MyBlockingStringPairQueue queue;
	MyBlockingHashMap hm;
	
	public AddMessageConsumer(MyBlockingStringPairQueue queue, MyBlockingHashMap hm) {
		// TODO Auto-generated constructor stub
		this.queue=queue;
		this.hm=hm;
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try{
			StringPair sp=queue.remove();
			hm.add(sp.word, sp.doc);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}
