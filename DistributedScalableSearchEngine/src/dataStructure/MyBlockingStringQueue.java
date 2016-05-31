package dataStructure;

import java.util.LinkedList;
import java.util.Queue;

public class MyBlockingStringQueue {

	private int limit;
	Queue<String> q=new LinkedList<String>();

	public MyBlockingStringQueue(int limit){
		this.limit=limit;

	}

	public synchronized void add(String s) throws InterruptedException{
		while(isFull())
			wait();
		q.add(s);
		notifyAll();
	}

	public synchronized String remove() throws InterruptedException{
		while(isEmpty())
			wait();
		String s=q.remove();
		notifyAll();
		return s;
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
		if(q.size()==limit)
			return true;
		else
			return false;
	}
}
