package dataStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyBlockingSearchResponseHashMap {
	
	private HashMap<ArrayList<String>, ArrayList<StringArrayListPair>> map= new HashMap<ArrayList<String>, ArrayList<StringArrayListPair>>();
	
	public synchronized void add(ArrayList<String> originalSearch, StringArrayListPair pair){
		if(map.containsKey(originalSearch))
			map.get(originalSearch).add(pair);
		else{
			ArrayList<StringArrayListPair> al = new ArrayList<>();
			al.add(pair);
			map.put(originalSearch, al);
		}
			
	}
	
	public synchronized boolean containsAll(ArrayList<String> originalSearch){
		if(map.containsKey(originalSearch)){
			return originalSearch.size()==map.get(originalSearch).size();
		}
		else
			return false;
	}
	
	public synchronized ArrayList<String> intersectDoclist(ArrayList<String> originalSearch){
		ArrayList<StringArrayListPair> al=map.get(originalSearch);
		HashSet<String> hs= new HashSet<String>();
		HashSet<String> hs1;
		for(int i=0;i<al.size();i++){
			if(i==0){
				hs.addAll(al.get(0).docList);
			}
			else{
				hs1 = new HashSet<>(hs);
				HashSet<String> hs2= new HashSet<String>();
				hs2.addAll(al.get(i).docList);
				for(String s : hs1){
					if(!hs2.contains(s))
						hs.remove(s);
				}
			}
		}
		map.remove(originalSearch);
		return new ArrayList<String>(hs);
	}
}
