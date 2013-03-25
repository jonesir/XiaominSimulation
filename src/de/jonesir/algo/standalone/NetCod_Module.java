package de.jonesir.algo.standalone;
import java.util.*;
import java.net.*;
import java.lang.*;
//import cern.jet.random.*;


/**
* Class NetCod_Module
* @author Alaeddine
* 
*/
class NetCod_Module {

	//defined by the constructor:
	int MDU;	// max data unit
	int MCLU;
	int HCL;	// Hard Coefficient Limit
	int port;
	int fileLength;
	long time;
	int recv_cntr = 0;
	
	//defined internally:
	DecodingBuf ddingBuf;
	DecodedBuf  ddedBuf;
	Vector	map_list;
	int rank;
	NCdatagram freshData = null;
	short fresh_send_ctr = 0;
//	Uniform rndUnif_coef;
	Random rnd;
	GaloisField base;
	ExtendedGaloisField GF;

	//defined externally:
	InetAddress source, destination;
	short fresh_fwd_factor;

	//linking need:
	MyApplication application;
	ReadThread readthread;




//--------------------------------------------------------------------
/**
* 
*/
//	public NetCod_Module(int mdu, int mclu, int port, String dest) throws GaloisException, UnknownHostException {
	public NetCod_Module(int mdu, int mclu, int HCL, int port) throws GaloisException, UnknownHostException {
		ddingBuf = new DecodingBuf();
		ddedBuf = new DecodedBuf();
		base = new GaloisField(2);
		GF = new ExtendedGaloisField(base,'a',8);
		map_list = new Vector();
		rank = 0;
		MDU = mdu;
		MCLU = mclu;
		this.HCL = HCL;
		this.port = port;
//		destination = InetAddress.getByName(dest);
		rnd = new Random(System.currentTimeMillis());
//		rndUnif_coef = new Uniform(0,255,(int) System.currentTimeMillis());
	}


//--------------------------------------------------------------------
/**
* 
*/
/**	public NetCod_Module() throws GaloisException {

		ddingBuf = new DecodingBuf();
		ddedBuf = new DecodedBuf();
		base = new GaloisField(2);
		GF = new ExtendedGaloisField(base,'a',8);
		map_list = new Vector();
		rank = 0;
//		MDU = mdu;
	}
*/

//--------------------------------------------------------------------	
	public synchronized int[] getIndex() {

//		int[] index = {5,6,7};
		int[] index;
		int l = (ddingBuf.size() + ddedBuf.size());

		if( l <= MCLU ) {
			index = new int[l];

			for(int i = 0; i < l; i++) {
				index[i] = i;
			}

			return index;			
		}

//		Uniform rndUnif_idx = new Uniform(0,MCLU-1,(int) System.currentTimeMillis());
		index = new int[MCLU];

		for(int i = 0; i < MCLU; i++) {
			index[i] = rnd.nextInt(l);
		}

		return index;
	}


//--------------------------------------------------------------------	
	public synchronized int[] getCoefs(int len) {

//		int[] coefs = {1,15,27};
		int[] coefs = new int[len];

		for(int i = 0; i < len; i++) {
			coefs[i] = rnd.nextInt(256);
		}
		
		return coefs;
	}


//--------------------------------------------------------------------	
/**
* blbla
* @param int[]
* @return NCdatagram 
*/
//	public NCdatagram encode(int[] coefs) throws GaloisException {
	public synchronized NCdatagram encode() throws GaloisException {
//		int[] index = {5,6,7};
		int[] index = getIndex();
		int[] coefs = getCoefs(index.length);
		int i,j,k;
		int L1 = ddedBuf.size();
		int L2 = ddingBuf.size();
		Coef_Elt coeftp;
//		int[][] coefs = {{1,2,3},{4,5,6},{7,8,10}};

		NCdatagram g, g2, g1 = new NCdatagram();

		for(i = 0; i < MDU; i++) {
			g1.Buf.append((char) 0);
		}

		for(i = 0; i < index.length; i++) {
			g = g1;
			if(L1 > 0) {
				if(index[i] > (L1 - 1)) {
					//g2 = NC_product(coefs[i] , (NCdatagram) ddingBuf.elementAt(index[i]%L1));
					g2 = NC_product(coefs[i] , (NCdatagram) ddingBuf.elementAt(index[i]-L1));
					g1 = NC_sum(g1,g2);
				} else {
					g2 = NC_product(coefs[i] , (NCdatagram) ddedBuf.elementAt(index[i]));
					g1 = NC_sum(g1,g2);				

				}
			} else {
				g2 = NC_product(coefs[i] , (NCdatagram) ddingBuf.elementAt(index[i]));
				g1 = NC_sum(g1,g2);				
			}
			
			if(g == g1) {
				break;
			}
		}

		return g1;
	
	}


//--------------------------------------------------------------------	
	public synchronized NCdatagram NC_product(int coef, NCdatagram g) throws GaloisException {

		int i,j,k;
		Coef_Elt coeftp;
		NCdatagram gc = (NCdatagram) g.clone();

		// handling the coefs_list
		for(i = 0; i < gc.coefs_list.size(); i++) {
			coeftp = (Coef_Elt) gc.coefs_list.elementAt(i);
			k = GF.product(coeftp.coef_ , coef);
			coeftp.coef_ = k;
//			g.coefs_list.set(i,tmp);
		}

		// handling the data in the StringBuffer
//		System.out.println("NC_product " + gc.Buf.length());
		for(i = 0; i < gc.Buf.length() ; i++) {
			gc.Buf.setCharAt(i,(char) GF.product((int) gc.Buf.charAt(i), coef));				
			// GF8
		}		
		
		return gc;

	}


//--------------------------------------------------------------------	
	public synchronized NCdatagram NC_sum(NCdatagram g1, NCdatagram g2) throws GaloisException {

	// assuming that the coef in all coef_lists are similarly ordered
	// and thus all of them have the same length
	// assuming that 2 coef. elts. in g2 can not have the Pkt_ID.

		int i,j;
		NCdatagram g = new NCdatagram();
		Coef_Elt coef1, coef2 = new Coef_Elt(), coef;
		Vector list = new Vector();	
		boolean found = false;

		for(i = 0; i < g2.coefs_list.size(); i++) {
			coef = (Coef_Elt) g2.coefs_list.elementAt(i);
			list.add(coef.clone());

		}

		for(i = 0; i < g1.coefs_list.size(); i++) {
			coef1 = (Coef_Elt) g1.coefs_list.elementAt(i);
			for(j = 0; j < list.size(); j++) {
				coef2 = (Coef_Elt) list.elementAt(j);

				if(coef2.id_.compare(coef1.id_)) {
					coef2 = (Coef_Elt) list.remove(j);
					found = true;
					break;
				}
			}
			
			if(found) {
				coef = new Coef_Elt();
				coef.id_ = (Pkt_ID) coef1.id_.clone();
				coef.coef_ = GF.sum(coef1.coef_, coef2.coef_);
				g.coefs_list.add(coef);
				found = false;
			} else {
				g.coefs_list.add(coef1);
			}

		}



		for(i = 0; i < list.size() ; i++) {
			g.coefs_list.add((Coef_Elt) list.elementAt(i));
		}

		if(g.coefs_list.size() > HCL) {
			return g1;
		}

/**
		for(i = 0; i < g1.coefs_list.size(); i++) {
			coef1 = g1.coefs_list.elementAt(i);
			coef2 = g2.coefs_list.elementAt(i);
	
			coef = new Coef_Elt();
			coef.id_ = coef1.id_.clone();
			coef.coef_ = GF.sum(coef1.coef_, coef2.coef_);
			g.coefs_list.add(coef);
		}
*/

//		System.out.println(g1.Buf.length());
		for(i = 0; i < g1.Buf.length() ; i++) {
			g.Buf.append((char) GF.sum((int) g1.Buf.charAt(i), (int) g2.Buf.charAt(i)));				

		}	
		
		return g;

	}

//--------------------------------------------------------------------	
	public NCdatagram NC_minus(NCdatagram g, NCdatagram g1, int coef) throws Exception {
		
		int i;

		if(g.Buf.length() != g1.Buf.length()) {
			throw new Exception();
		}
		
		for(i = 0; i < g.Buf.length(); i++) {
			g.Buf.setCharAt(i, (char) GF.minus( (int) g.Buf.charAt(i) , GF.product(coef, (int) g1.Buf.charAt(i))));
		}

		return g;
	}


//--------------------------------------------------------------------	
	public synchronized NCdatagram reduce(NCdatagram g) throws GaloisException, Exception {
	
		int i,j;
		boolean found = false;
		NCdatagram g1;
		Coef_Elt coef_elt, coef_elt1;
		Vector list = new Vector();

		for(i = 0; i < g.coefs_list.size(); i++) {

			coef_elt = (Coef_Elt) g.coefs_list.elementAt(i);
			
			for(j = 0; j < ddedBuf.size(); j++) {

				g1 = (NCdatagram) ddedBuf.elementAt(j);
	
				if(g1.coefs_list.size() != 1) {
					throw new Exception();
				}

				coef_elt1 = (Coef_Elt) g1.coefs_list.elementAt(0);

				if(coef_elt1.coef_ != 1) {
					throw new Exception();
				}

				if(coef_elt1.id_.compare(coef_elt.id_)) {
					found = true;
					g = NC_minus(g,g1,coef_elt.coef_);
					break;
				}
			}

			if(!found) {
				list.add(coef_elt);
			}

			found = false;
		}

		g.coefs_list = list;

		return g;					
			

	}


//--------------------------------------------------------------------	
//	public synchronized void insert(NCdatagram g) throws Exception {
	public synchronized void insert(NCdatagram g) {
	
		try{
			g = reduce(g);
		}
		catch(Exception ev) {
			System.err.println("NetCode_Module: insert: reduce " + ev);
		}
// new modification
		if(g.coefs_list.size() == 0) {
			return;
		}

/**		if(g.coefs_list.size() == 1) {
			ddedBuf.add(g);
			deliverToApp(g);
			return;
		}
*/
// end of new modification
		try{
			update_map_list(g.coefs_list);
		}
		catch(Exception e1) {
			System.err.println("NetCode_Module: insert: update_map_list " + e1);
		}
		try{
			g = reorder_coef(g);
		}
		catch(Exception e2) {
			System.err.println("NetCode_Module: insert: reorder_coef " + e2);
		}
		ddingBuf.add(g);
		try{
			decode();
		}
		catch(GaloisException e) {
			System.err.println("NetCode_Module: insert: decode" + e);
		}
		catch(Exception e3) {
			System.err.println("NetCode_Module: insert: decode " + e3);
		}
	}


//--------------------------------------------------------------------	
	public synchronized void update_map_list(Vector v) throws Exception {

		int i,j;
		boolean found = false;
		Coef_Elt coef_elt1,coef_elt2;
		Pkt_ID id;

		for(i = 0; i < v.size(); i++) {
			coef_elt1 = (Coef_Elt) v.elementAt(i);
			
			for(j = 0; j < map_list.size(); j++) {
				id = (Pkt_ID) map_list.elementAt(j);
				
				if(coef_elt1.id_.compare(id)) {
					found = true;
					break;
				}

			}
		
			if(!found) {
				id = (Pkt_ID) coef_elt1.id_.clone();
				map_list.add(id);
			}

			found = false;
		}

	}


//--------------------------------------------------------------------	
	public synchronized NCdatagram reorder_coef(NCdatagram g) throws Exception {
		
		int i,j;
		Vector list = new Vector();
		Coef_Elt coef_elt;
		Pkt_ID id;
		boolean found = false;


		for(i = 0; i < map_list.size(); i++) {
			id = (Pkt_ID) map_list.elementAt(i);

			for(j = 0; j < g.coefs_list.size(); j++) {
				coef_elt = (Coef_Elt) g.coefs_list.elementAt(j);

				if(id.compare(coef_elt.id_)) {
					found = true;
					list.add(coef_elt.clone());
					break;
				}
			
			}

			if(!found) {
				coef_elt = new Coef_Elt();
				coef_elt.coef_ = 0;

//				id = new Pkt_ID();
//				id.saddr = -1;
//				id.time_ = -1;
				coef_elt.id_ = (Pkt_ID) id.clone();
				list.add(coef_elt);
			}

			found = false;

		}

//		if(list.size() != g.coefs_list.size()) {
		if(list.size() != map_list.size()) {
			throw new Exception();
		}

		g = (NCdatagram) g.clone();
		g.coefs_list = list;

		return g;


	}


//--------------------------------------------------------------------	
	public synchronized int[][] VecToArr() throws GaloisException, Exception {
		//GF8
		int i,j,k,jj;
		int[][] A;
		NCdatagram g;
		Coef_Elt coef_elt;

		A = new int[ddingBuf.size()][map_list.size() + MDU];

		for(i = 0; i < ddingBuf.size(); i++) {
			g = (NCdatagram) ddingBuf.elementAt(i);

//			if(g.coefs_list.size() != map_list.size()) {
//				throw new Exception();
//			}

			for(j = 0; j < g.coefs_list.size(); j++) {
				coef_elt = (Coef_Elt) g.coefs_list.elementAt(j);
				A[i][j] = coef_elt.coef_;
			}
			
			for( jj = j; jj < map_list.size(); jj++) {
				A[i][jj] = 0;
			}

			for(k = 0; k < MDU; k++) {
				A[i][jj+k] = (int) g.Buf.charAt(k);
			}

		}

		return A;
	}


//--------------------------------------------------------------------	
	public synchronized int[][] GausElim(int[][] A, int L, int C1, int C2) throws GaloisException, Exception {

		// C1: taille de la matrice de coef
		// C2: taille de toute la matrice (coef + données)

		int[] tmp;
		int k,i,l;
		int pivot, tmpd;
		int[][] B = new int[L][C2];

		rank = 0;

//		GaloisField base = new GaloisField(2);
//		ExtendedGaloisField ext = new ExtendedGaloisField(base,'a',8);
 
		for(i = 0 ; i< L ; i++) { 
			for(k = 0 ; k < C2 ; k++) { 
				B[i][k] = A[i][k];
			}
		}

		// boucle d'iterations: # d'iter = # des lignes
		for(k = 0; k < L ; k++) { 

			// first step of the kth iteration
			// we need to insure that the pivot is not zero
			if(B[k][k] == 0) {
				boolean b = false;
				for(int n = k+1 ; n < L ; n++){
					if(B[n][k] != 0) {
						tmp = B[n];
						B[n] = B[k];
						B[k] = tmp;
						b = true;
						break;
					}
					
				}
				if(!b) {
					for(int n = k+1 ; n < C1 ; n++){
						if(B[k][n] != 0) {
/**							for(l = 0; l < L; l++){
								tmpd = B[l][k];
								B[l][k] = B[l][n];
								B[l][n] = tmpd;
								
							}
*/
							Permut_col(B, k, n, L);
							b = true;
							break;
						}
					}
				}

//				if(!b) return false;
				if(!b) {return B;}
			}			

			rank++;

			int piv = B[k][k];
			for(int cp = k; cp < C2 ; cp++) {
//				B[k][cp] = B[k][cp] / piv;
				B[k][cp] = (int) GF.divide((int) B[k][cp], (int) piv);
			}
			
			for(i = 0; i < L ; i++) {  // boucle des lignes
				
				
				if(i != k) {
					pivot = B[i][k];
					for(int c = k; c < C2 ; c++) { //boucle des colonnes
//						B[i][c] = B[i][c] - pivot*B[k][c];
						B[i][c] = (int) GF.minus((int) B[i][c], (int) GF.product((int) pivot, (int) B[k][c]));
					}
										
				}
			}
		}
		return B;
	}


//--------------------------------------------------------------------	
	public synchronized void Permut_col(int[][] B, int col1, int col2, int L) {
		
		int tmpd;
		Pkt_ID id;
		id = (Pkt_ID) map_list.elementAt(col1);
		map_list.set(col1, map_list.elementAt(col2));
		map_list.set(col2, id);

		for(int l = 0; l < L; l++){
			tmpd = B[l][col1];
			B[l][col1] = B[l][col2];
			B[l][col2] = tmpd;
		}
	}


//--------------------------------------------------------------------	
	public synchronized int[][] reduceMatrix(int[][] B) throws GaloisException, Exception {
		// this fuction eliminate the unnessary lignes in the decoding
		// matrix after the Gaussian Elimination

		int[][] A = new int[rank][];
		int i;

		for(i = 0; i < rank; i++) {
			A[i] = B[i];
		}

		return A;

	}


//--------------------------------------------------------------------	
	public synchronized void ArrToVec(int[][] A, int C2, int gen) throws GaloisException, Exception {

		int i,j,k;
		NCdatagram g;
		Coef_Elt coef_elt;
		Pkt_ID id;
		

		ddingBuf.clear();
		

		for(i = 0; i < rank; i++) {
			g = new NCdatagram();

			for(j = 0; j < map_list.size(); j++) {
				coef_elt = new Coef_Elt();
				coef_elt.coef_ = A[i][j];
				id = (Pkt_ID) map_list.elementAt(j);
				coef_elt.id_ = (Pkt_ID) id.clone();
			
				g.coefs_list.add(coef_elt);
			}

			for(k = 0; k < MDU; k++) {
				g.Buf.append((char) A[i][k+j]);
			}
			
			g.gen_ = gen;
			g.list_extract = true;

			ddingBuf.add(g);
		}
		


	}


//--------------------------------------------------------------------	
	public synchronized void decodable() throws GaloisException, Exception {

		int i,j,k = 0,nbCoef;
		NCdatagram g;
		Coef_Elt coef_elt;
		Pkt_ID id;
		

		if(rank < map_list.size()) {
			i = 0;
			while(i < ddingBuf.size()) {
				g = (NCdatagram) ddingBuf.elementAt(i);
				nbCoef = 0;
	
				for(j = 0; j < g.coefs_list.size(); j++) {
					coef_elt = (Coef_Elt) g.coefs_list.elementAt(j);

					if(coef_elt.coef_ != 0) {
						nbCoef++;
						k = j;
					}
				}
			
				if(nbCoef == 1) {
					coef_elt = (Coef_Elt) g.coefs_list.elementAt(k);
					coef_elt = (Coef_Elt) coef_elt.clone();
					if(coef_elt.coef_ != 1) {
						throw new Exception();
					}
	
					g.coefs_list.clear();
					g.coefs_list.add(coef_elt);
					g = (NCdatagram) ddingBuf.remove(k);
					ddedBuf.add(g);
					deliverToApp(g);
					if(ddedBuf.size() == fileLength) {
						long duration = System.currentTimeMillis() - time;
						duration = duration/1000;
						System.out.println("file transfert time in (s) = " + duration);
						System.out.println("needed pkt Number is = " + recv_cntr);
					}

					map_list.remove(k);
					
					for(j = 0; j < ddingBuf.size(); j++) {
						g = (NCdatagram) ddingBuf.elementAt(j);
						g.coefs_list.remove(k);
					}	
				
					rank--;
				} else {
					i++;
				}
		
			}

		} else {
			i = 0;
			while(ddingBuf.size() != 0) {
				g = (NCdatagram) ddingBuf.elementAt(0);
				coef_elt = (Coef_Elt) g.coefs_list.elementAt(i);
				coef_elt = (Coef_Elt) coef_elt.clone();

				if(coef_elt.coef_ != 1) {
					throw new Exception();
				}
	
				g.coefs_list.clear();
				g.coefs_list.add(coef_elt);
				g = (NCdatagram) ddingBuf.remove(0);
				ddedBuf.add(g);
				deliverToApp(g);
				
				if(ddedBuf.size() == fileLength) {
					long duration = System.currentTimeMillis() - time;
					duration = duration/1000;
					System.out.println("file transfert time in (s) = " + duration);
					System.out.println("needed pkt Number is = " + recv_cntr);
				}
				
				
				i++;
			}
			map_list.clear();
			rank = 0;
		}

	}


//--------------------------------------------------------------------	
//	public void deliverToApp(NCdatagram g) throws Exception {
	public synchronized void deliverToApp(NCdatagram g) {

		application.writeData(g);


		// the name of this function is to change later

	}


//--------------------------------------------------------------------	
	public synchronized void decode() throws GaloisException, Exception {
		
		int L, C1, C2;
		int[][] A = VecToArr();
		L = ddingBuf.size();
		C1 = map_list.size();
		C2 = C1 + MDU;
		A = GausElim(A, L, C1, C2);
		A = reduceMatrix(A);
		ArrToVec(A, C2, 0);
		decodable();
	}


//--------------------------------------------------------------------	
	public NCdatagram send() {

		NCdatagram g;
		if(freshData != null) {

			if((--fresh_send_ctr) != 0) {
				return freshData;
			} else {
				g = freshData;
				freshData = null;

				try{
//					insert(g);
					ddedBuf.add(g);
				}
				catch(Exception ev) {
					System.err.println("NetCod_Module: send: " + ev);
				}

//				try{
					readthread.interrupt();
//				}
//				catch(InterruptedException e) {
//					System.out.println("InterruptedException");
//				}
				return g;
			}
		}

		if( (ddingBuf.size() + ddedBuf.size()) == 0) {
			return null;
		}

		try{
//			System.out.println("netcode: encode: " + fresh_send_ctr);
			g = encode();
			return g;
		}
		catch(GaloisException e) {
			System.err.println("NetCod_Module: send: " + e);
		}

		return null;
	}


//--------------------------------------------------------------------	
// debug
	public NCdatagram send(NCdatagram g) {

		return g;
	}


//--------------------------------------------------------------------	
	public void recv(NCdatagram g) {

		recv_cntr++;
		try{
			insert(g);
		}
		catch(Exception ev) {
			System.err.println("NetCod_Module: recv: " + ev);
		}


/**		System.out.println("g.Buf = " + g.Buf);	// debug
		Coef_Elt coef_elt = (Coef_Elt) g.coefs_list.elementAt(0);
		System.out.println("coef_elt.coef_ = " + coef_elt.coef_);
		System.out.println("coef_elt.id_.time_ = " + coef_elt.id_.time_);
		System.out.println("coef_elt.id_.saddr = " + coef_elt.id_.saddr);
*/		
	}


//--------------------------------------------------------------------
	public void sendToTransport(Payload p) {

		freshData = new NCdatagram();
		freshData.Buf = p.Buf;
		
		Coef_Elt coef_elt = new Coef_Elt();
		coef_elt.coef_ = 1;
		coef_elt.id_.time_ = System.currentTimeMillis();
		coef_elt.id_.saddr = source;
		freshData.coefs_list.add(coef_elt);
		
		fresh_send_ctr = fresh_fwd_factor;

	}


}
