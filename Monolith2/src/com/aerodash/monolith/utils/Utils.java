package com.aerodash.monolith.utils;

import java.util.ArrayList;

public class Utils {

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
