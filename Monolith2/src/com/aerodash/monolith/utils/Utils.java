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
	
	public static void arrayPush(Object[] obj, Object toPush){
		
		for (int i = 0; i < obj.length - 1; i++){
			obj[i] = obj[i + 1];
		}
		obj[obj.length - 1] = toPush;
	}
	
	public static boolean allItemsSame(Object[] objs){

		if (objs[0] == null) return false;
		for (int i = 0; i < objs.length - 1; i++){
			if (objs[i] != objs[i + 1]) return false;
		}
		return true;
	}
	
	public static void clearArray(Object[] objs){
		for (Object o : objs){
			o = null;
		}
	}

}
