package pastry;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import rice.pastry.routing.SendOptions;
import threadClasses.AddMessageConsumer;
import threadClasses.AddMessageProducer;
import dataStructure.MyBlockingHashMap;
import dataStructure.MyBlockingSearchResponseHashMap;
import dataStructure.MyBlockingStringPairQueue;
import dataStructure.MyBlockingStringQueue;

public class Main {

	/*
	 * 0. local port
	 * 1. bootstrapIp
	 * 2. bootstrapPort
	 * 3. directory path
	 * .
	 * .
	 * .
	 * .
	 * n. directory path
	 */
	public static void main(String[] args) throws InterruptedException {

		if(args.length<4)
			throw new IllegalArgumentException();
		int localPort= Integer.parseInt(args[0]);
		InetSocketAddress boot= new InetSocketAddress(args[1], Integer.parseInt(args[2]));

		NodeFactory nodeFactory= new NodeFactory(localPort, boot);

		MyBlockingStringPairQueue q= new MyBlockingStringPairQueue(1000);
		MyBlockingHashMap map=new MyBlockingHashMap();
		MyBlockingSearchResponseHashMap searchResponseMap= new MyBlockingSearchResponseHashMap();
		MyBlockingStringQueue queue=new MyBlockingStringQueue(1000);
		SearchApp searchApp =new SearchApp(nodeFactory, q, map, searchResponseMap);

		//Solving for file path
		Queue<String> directories=new LinkedList<String>();
		for(int i=3;i<args.length;i++)
			directories.add(args[i]);

		while(!directories.isEmpty()){
			String t=directories.remove();	
			File folder= new File(t);
			if(!folder.exists()){
				continue;
			}
			File[] listOfFile=folder.listFiles();
			for(File f: listOfFile){
				if(f.isDirectory())
					directories.add(f.getPath());
				if(f.isFile())
					queue.add(f.getPath());
			}
		}


		//Thread pool for producer threads
		Thread[] producers= new Thread[10];
		for(Thread producer: producers){
			producer=new Thread(new AddMessageProducer(queue, searchApp));
			producer.start();
		}


		// Thread pool for consumer threads
		Thread[] consumers=new Thread[10];
		for(Thread consumer: consumers){
			consumer= new Thread(new AddMessageConsumer(q, map));
			consumer.start();
		}

		while(true){
			System.out.println("Whats on your mind...");
			Scanner sc= new Scanner(System.in);
			String search=sc.nextLine();
			searchApp.sendSearchMessage(search);
		}
	}
}
