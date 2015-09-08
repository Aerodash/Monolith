package com.aerodash.monolith.exception;

public class CostException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public CostException(){
		super("You haven't enough resources !");
	}

}
