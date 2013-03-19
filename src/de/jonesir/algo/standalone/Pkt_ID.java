/**
 * Class Pkt_ID

 * 
 */
package de.jonesir.algo.standalone;
import java.net.*;


public class Pkt_ID {
  // Fields
  // 
  public long time_;
  // 
//  public int saddr;
	public InetAddress saddr;
  // Methods
  // Constructors
  // Empty Constructor
  public Pkt_ID ( ) { }
  // Accessor Methods
  /**
   * Get the value of time_
   * 
   * @return the value of time_
   */
  public long getTime_ (  ) {
    return time_;
  }
  /**
   * Set the value of time_
   * 
   * 
   */
  public void setTime_ ( long value  ) {
    time_ = value;
  }
  /**
   * Get the value of saddr
   * 
   * @return the value of saddr
   */
//  public int getSaddr (  ) {
//    return saddr;
//  }
  /**
   * Set the value of saddr
   * 
   * 
   */
//  public void setSaddr ( int value  ) {
//    saddr = value;
//  }

//	public Pkt_ID clone() {
  	public Object clone() {
		Pkt_ID id = new Pkt_ID();

		id.time_ = time_;
//		id.saddr = saddr;
		try{
			String str=saddr.getHostAddress();
			id.saddr = InetAddress.getByName(str);
			return id;
		}
		catch(UnknownHostException e) {
			System.err.println(e);
			return null;
		}
	}

	public boolean compare(Pkt_ID id) {
		if( (id.time_ == time_) & (saddr.equals(id.saddr)) ) {
			return true;
		}

		return false;
	}

  // Operations
}

