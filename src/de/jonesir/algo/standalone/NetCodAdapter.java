package de.jonesir.algo.standalone;
import java.net.*;

class NetCodAdapter implements Adapter {

//	final int coef_size = 18;
	int coef_size = Coef_Elt.coef_size;
	int MDU;

	//defined externally:
	long rate;

	//linking need:
	NetCod_Module netCod;
	SocketInterface sInt;

//--------------------------------------------------------------------
//	public NetCodAdapter(NetCod_Module netCod, int MDU) {
	public NetCodAdapter(int MDU) {
//		this.netCod = netCod;
		this.MDU = MDU;
//		this.sInt = sInt;

	}


//--------------------------------------------------------------------
	public synchronized DatagramPacket toDatagram(NCdatagram g, int port, InetAddress destination) {

		// we assume that the number of coefs does not exceed 255.
		int i,j;
		byte[] b_coef;
		DatagramPacket d;
		Coef_Elt coef_elt;
		byte[] b;
		int b_iter = 0;
		
		int s = g.coefs_list.size();
		b = new byte[s*coef_size + MDU + 1];

		b[b_iter++] = (byte) s; // il faut faire attention a la transf inverse ou s peut etre negatif

		for(i = 0; i < s; i++) {
			coef_elt = (Coef_Elt) g.coefs_list.elementAt(i);
			b_coef = coef_elt.getBytes();
			
			for(j = 0; j < b_coef.length; j++) {
				b[b_iter++] = b_coef[j];
			}	
			
		}

		for(i = 0; i < MDU; i++) {
			b[b_iter++] = (byte) g.Buf.charAt(i);
		}
		
		d = new DatagramPacket(b, b.length, destination, port);
		return d;

	}


//--------------------------------------------------------------------
	public synchronized NCdatagram toNCdatagram(DatagramPacket d) {
	
		NCdatagram g = new NCdatagram();
		int b_iter = 0;
		int i,k;
		StringBuffer Buf = new StringBuffer();
		String str;
		Coef_Elt coef_elt;

		byte[] b = d.getData();
		int s = b[b_iter++];
		if(s < 0) {
			s = s + 256;
		}
		int coef_cnt = s*coef_size + 1;
		
//		str = new String(b);
//		Buf.append(str.substring(1,coef_cnt));
//		for(i = coef_cnt; i < b.length; i++) {
//			Buf.append((char) b[i]);
//		}
//		Buf.append(str);
		for(i = 0; i < s; i++) {
//			coef_elt = getCoef_Elt(Buf.substring(b_iter, b_iter + coef_size));
			coef_elt = Coef_Elt.getCoef_Elt(b, b_iter);
			b_iter = b_iter + coef_size;
			g.coefs_list.add(coef_elt);
		}

//		g.Buf.append(Buf.substring(b_iter, Buf.length() -1));
//		g.Buf.append(Buf.substring(b_iter, b_iter + MDU));

		for(i = 0; i < MDU; i++) {
			k = b[b_iter++];
			if(k < 0) {
				k = k + 256;
			}
			g.Buf.append((char) k);

//			g.Buf.append((char) b[b_iter++]);
		}

		return g;

	}


//--------------------------------------------------------------------
	public synchronized void send() {

		NCdatagram g = netCod.send();
		
		if(g != null) {
			DatagramPacket d = toDatagram(g, netCod.port, netCod.destination);
			try{
				sInt.sendToPhy(d);
			}
			catch(SocketException e) {
				System.err.println("NetCodAdapter: send: " + e);
			}
		}

	}


//--------------------------------------------------------------------
// used to debug
	public synchronized void send(NCdatagram g) {

//		NCdatagram g = netCod.send();
		
		if(g != null) {
			DatagramPacket d = toDatagram(g, netCod.port, netCod.destination);
			try{
				sInt.sendToPhy(d);
			}
			catch(SocketException e) {
				System.err.println("NetCodAdapter: send debug: " + e);
			}
		}

	}


//--------------------------------------------------------------------
	public synchronized void recv(DatagramPacket d) {

		NCdatagram g = toNCdatagram(d);
		netCod.recv(g);

	}


//--------------------------------------------------------------------
	public synchronized void run() {
//		System.out.println("adapter: run ");
		ManageSendThread ms = new ManageSendThread(this, rate);
		ms.start();

	}

}
