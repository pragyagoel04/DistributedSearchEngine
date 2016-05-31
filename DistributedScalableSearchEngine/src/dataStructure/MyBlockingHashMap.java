package dataStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class MyBlockingHashMap {
	private HashMap<String, HashSet<String>> map= new HashMap<String, HashSet<String>>();
	
	public synchronized void add(String word, String doc){
		if(map.containsKey(word))
			map.get(word).add(doc);
		else{
			HashSet<String> hashSet=new HashSet<String>();
			hashSet.add(doc);
			map.put(word, hashSet);
		}
	}
	
	public synchronized ArrayList<String> remove(String word){
		if(map.containsKey(word))
			return new ArrayList<String>(map.get(word));
		else
			return new ArrayList<String>();
	}
	
}
