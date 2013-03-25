package de.jonesir.algo.standalone;
//import Packet;
//import Vector;
/**
 * Class NCdatagram
 * 
 */

import java.lang.*;
import java.util.*;

public class NCdatagram extends Payload {
  	// Fields
  	// 

  	public long gen_;
//  	private int reduced_coef_list;
  	private boolean decoded;
  	public Vector coefs_list;
	public boolean list_extract; // to note wheither the coef_list is extracted from the received payload


  	// Methods
  	// Constructors
  	// Empty Constructor
  	public NCdatagram ( ) {
		coefs_list = new Vector();
		list_extract = false;
	}

	public void extract(int MDU) {
/**	cette fct sert a extraire les coefficients du payload
*/
	if(!list_extract){
		list_extract = true;

		// a remplir apres
	}
	
	}


  	private long getGen_ (  ) {
    		return gen_;
  	}
  	private void setGen_ ( long value  ) {
   		gen_ = value;
  	}


  	private boolean getDecoded (  ) {
    		return decoded;
  	}
  	private void setDecoded ( boolean value  ) {
    		decoded = value;
  	}


  	public Vector getCoefs_list (  ) {
    		return coefs_list;
  	}
  	public void setCoefs_list ( Vector value  ) {
   		coefs_list = value;
  	}


//	public NCdatagram clone() {
  	public Object clone() {		
		int i;
		Coef_Elt coef;

		NCdatagram g = new NCdatagram();
		g.gen_ = gen_;
		g.decoded = decoded;
		g.list_extract = list_extract;
		
		for(i = 0; i < coefs_list.size() ; i++) {
			coef = (Coef_Elt) coefs_list.elementAt(i);
			g.coefs_list.add(coef.clone());
		}
		
		for(i = 0; i < Buf.length(); i++) {
			g.Buf.append(Buf.charAt(i));
		}
		
		return g;
	}

  	// Operations
  	/**
   	* 
   	* @return long  
   	*/
//  	public long generation ( ) {
    
//  	}
  	/**
   	* 
   	* @return NCdatagram*  
   	*/
//  	public NCdatagram clone ( ) {
    
//  	}
}

