package de.jonesir.algo.standalone;
import java.net.*;
//import java.lang.Throwable;
//import java.lang.Exception;
import java.io.*;


class SocketInterface {

	//defined by the constructor:
	int maxPktLength;
	int port;

	//defined internally:
//	DatagramSocket ds;
	MulticastSocket send_ds;
	InetAddress group;
	
	//linking need:
	Adapter adapter;


//--------------------------------------------------------------------
	public SocketInterface(int port, int maxPktLength, InetAddress group) throws IOException {
		this.port = port;
//		ds = new DatagramSocket(port);
		send_ds=new MulticastSocket();
//		this.adapter = adapter;
		this.maxPktLength = maxPktLength;
		this.group=group;
	}


//--------------------------------------------------------------------
	public synchronized void sendToPhy(DatagramPacket g) throws SocketException {

		if(g == null) {
			System.out.println("sInt: sendToPhy: g == null");
		}
		if(send_ds == null) {
			System.out.println("sInt: sendToPhy: d == null");
		}

		try{
			send_ds.send(g);
		}
		catch(IOException e) {
			System.err.println("Socket Interface: snedToPhy: " + e);
		}

	}


//--------------------------------------------------------------------
//	public synchronized void recvFromPhy() {
	public  void recvFromPhy() throws IOException {
		RecvThread recvThrd = new RecvThread(maxPktLength, adapter, group,port);
		recvThrd.start();
	}


}
