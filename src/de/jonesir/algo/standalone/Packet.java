package de.jonesir.algo.standalone;
//import Payload;
//import Pkt_Id;
/**
 * Class Packet
 * 
 */

import java.lang.*;

public class Packet extends Payload implements Cloneable {
  
  	public int mac_saddr_;
  	public int mac_daddr_;
  	public int next_hop_;
  	public Pkt_ID uid_;
  	public int saddr_;
  	public int daddr_;


  	// Methods
  	// Constructors
  	// Empty Constructor
  	public Packet ( ) { }
  	// Accessor Methods
  	/**
   	* Get the value of next_hop_
   	* 
   	* @return the value of next_hop_
   	*/
  	public int getNext_hop_ (  ) {
    		return next_hop_;
  	}
  	public void setNext_hop_ ( int value  ) {
    		next_hop_ = value;
  	}


  	public Pkt_ID getUid_ (  ) {
    		return uid_;
  	}
  	public void setUid_ ( Pkt_ID value  ) {
    		uid_ = value;
  	}
  

  	public int getSaddr_ (  ) {
    		return saddr_;
  	}
	public void setSaddr_ ( int value  ) {
    		saddr_ = value;
  	}


  	public int getDaddr_ (  ) {
    		return daddr_;
  	}
  	public void setDaddr_ ( int value  ) {
    		daddr_ = value;
  	}


  	public int getMac_saddr_ (  ) {
    		return mac_saddr_;
  	}
  	public void setMac_saddr_ ( int value  ) {
    		mac_saddr_ = value;
  	}


  	public int getMac_daddr_ (  ) {
    		return mac_daddr_;
  	}
  	public void setMac_daddr_ ( int value  ) {
    		mac_daddr_ = value;
  	}
  	// Operations
  	/**
   	* 
   	* @return Packet  
   	*/
//  	public Packet clone ( ) {
    		
//  	}
  	/**
   	* 
   	* @return   
   	*/
  	public  Pkt_ID uid ( ) {
		return uid_;
    
  	}
}

