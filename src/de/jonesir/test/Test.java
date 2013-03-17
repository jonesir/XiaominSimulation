package de.jonesir.test;

import de.jonesir.algo.GaloisField;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GaloisField gf = GaloisField.getInstance();
		log(""+gf.add(2,3));
	}
	
	public static void log(String logString){
		System.out.println(logString);
	}
} 
