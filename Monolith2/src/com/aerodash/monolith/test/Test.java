package com.aerodash.monolith.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Test {

	public static void main(String[] args) {
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "One");
		map.put(2, "Two");
		
		Iterator<String> it = map.values().iterator();
		while(it.hasNext()){
			if (it.next().equals("Two")){
				it.remove();
			}
		}
		
	}
	
	public static ArrayList removeDuplicates(ArrayList input){
		ArrayList<Object> res = new ArrayList<Object>();
		
		while(input.size() > 0){
			res.add(input.get(0));
			
			ArrayList elt = new ArrayList();
			elt.add(input.get(0));
			input.removeAll(elt);
		}
		
		return res;
	}

}
