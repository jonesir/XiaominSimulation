package de.jonesir.algo.standalone;
import java.net.*;
//import Pkt_ID;
/**
 * Class Coef_Elt
 * 
 */
public class Coef_Elt extends Object {
  // Fields
  // 
  public int coef_;
  public Pkt_ID id_;
	public final static int coef_size = 13;


  // Methods
  // Constructors
  // Empty Constructor
  public Coef_Elt ( ) {
	id_ = new Pkt_ID();
 }
  // Accessor Methods
  /**
   * Get the value of coef_
   * 
   * @return the value of coef_
   */
  public int getCoef_ (  ) {
    return coef_;
  }
  /**
   * Set the value of coef_
   * 
   * 
   */
  public void setCoef_ ( int value  ) {
    coef_ = value;
  }
  /**
   * Get the value of id_
   * 
   * @return the value of id_
   */
  private Pkt_ID getId_ (  ) {
    return id_;
  }
  /**
   * Set the value of id_
   * 
   * 
   */
  private void setID_ ( Pkt_ID value  ) {
    id_ = value;
  }
  // Operations

//	public Coef_Elt clone() {
  	public Object clone() {
		Coef_Elt coef_elt = new Coef_Elt();
		
		coef_elt.coef_ = coef_;
		coef_elt.id_ = (Pkt_ID) id_.clone();

		return coef_elt;
	}


	public byte[] getBytes() {

		byte[] b = new byte[coef_size];
		int iter = 0;

		b[0] = (byte) coef_;
		iter++;
		
		for(iter = 1; iter < 9; iter++) {
			b[iter] = (byte) (id_.time_>>((iter-1)*8));
		}
		
		byte[] b_addr = id_.saddr.getAddress();

		for(int i = 0; i < 4; i++) {
			b[iter++] = b_addr[i];
		}
		
		return b;

	}

	public static Coef_Elt getCoef_Elt(byte[] b, int offset) {

		Coef_Elt coef_elt = new Coef_Elt();
//		int iter = 0;
		int iter = offset, i;
		coef_elt.coef_ = b[iter] >= 0 ? b[iter] : b[iter] + 256;
		
		coef_elt.id_.time_ = 0;
		
//		for(iter = 1; iter < 9; iter++) {
		for(i = 1; i < 9; i++) {
//			b[iter] = (byte) (id_.time_>>((iter-1)*8));
			iter++;
			long ll = (long) b[iter];
			if(ll < 0) {
				ll = ll + 256;
//				coef_elt.id_.time_ = (coef_elt.id_.time_<<((iter-1)*8)) + ((long) b[iter]) + 127;
				coef_elt.id_.time_ = coef_elt.id_.time_ + (ll<<((i-1)*8));
			} else {
//				coef_elt.id_.time_ = (coef_elt.id_.time_<<((iter-1)*8)) + ((long) b[iter]);
				coef_elt.id_.time_ = coef_elt.id_.time_ + (ll<<((i-1)*8));
			}
		}

		StringBuffer buf = new StringBuffer();
		//Long L;
		byte[] B = new byte[4];
		for(i = 0; i < 4; i++) {
			
			B[i]=b[++iter];
//			buf.append(st.charAt(i));
//			buf.append(L.toString());
//			buf.append('.');
		}
//		buf.deleteCharAt(buf.length() - 1);

		try{
//			coef_elt.id_.saddr = InetAddress.getByName(str.substring(iter, str.length() - 1));
			coef_elt.id_.saddr = InetAddress.getByAddress(B);
		}
		catch(UnknownHostException e) {
			System.err.println("getCoef_Elt: " + e);
		}
		return coef_elt;

	
	}


}

