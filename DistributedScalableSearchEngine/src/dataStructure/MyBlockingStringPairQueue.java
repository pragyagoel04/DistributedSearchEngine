package dataStructure;

import java.util.LinkedList;
import java.util.Queue;


public class MyBlockingStringPairQueue {

	private int limit;
	Queue<StringPair> q= new LinkedList<StringPair>();

	public MyBlockingStringPairQueue(int limit){
		this.limit=limit;
	}

	public synchronized void add(StringPair pair) throws InterruptedException{
		while(isFull()){
			wait();
		}
		q.add(pair);
		notifyAll();
	}

	public synchronized StringPair remove() throws InterruptedException{
		while(isEmpty()){
			wait();
		}
		StringPair p=q.remove();
		notifyAll();
		return p;
	}

	private boolean isEmpty() {
		// TODO Auto-generated method stub
		if(q.size()==0)
			return true;
		else
			return false;
	}

	private boolean isFull() {
		// TODO Auto-generated method stub
		if(q.size()==limit){
			return true;
		}
		else
			return false;

	}

}
