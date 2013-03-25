package de.jonesir.algo.standalone;
//import StringBuffer;
/**
 * Class Payload
 * 
 */

import java.lang.*;

public class Payload extends Object implements Cloneable {
  	// Fields
  	// 
  	public StringBuffer Buf;
  	// Methods
  	// Constructors
  	// Empty Constructor
  	public Payload ( ) { 
		
		Buf = new StringBuffer();
	}


  	public StringBuffer getBuff (  ) {
    		return Buf;
  	}
  	public void setBuff ( StringBuffer value  ) {
    		Buf = value;
  	}

  // Operations
}

