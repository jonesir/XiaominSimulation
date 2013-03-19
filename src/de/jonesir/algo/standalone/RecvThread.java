package de.jonesir.algo.standalone;
import java.net.*;
import java.io.*;

class RecvThread extends Thread {

//	DatagramSocket ds;
	MulticastSocket recv_ds;
	DatagramPacket d;
	int maxPktLength;
	NetCodAdapter adapter;

//	public RecvThread(DatagramSocket ds, int maxPktLength, NetCodAdapter adapter) {
//	public RecvThread(DatagramSocket ds, int maxPktLength, Adapter adapter) {
	public RecvThread(int maxPktLength, Adapter adapter, InetAddress group,int port) throws IOException {
		this.recv_ds = new MulticastSocket(port);

		this.maxPktLength = maxPktLength;
		this.adapter = (NetCodAdapter) adapter;
		recv_ds.joinGroup(group);
		recv_ds.setLoopbackMode(false);
	}

//	public void run() throws SocketException {
	public void run() {

		byte[] b = new byte[maxPktLength];

		d = new DatagramPacket(b, b.length);
		
		try{
			while(true) {
//				if(ds == null) {
//					System.out.println("RecvThread: run: ds == null");
//				}
//				synchronized(ds) {
					recv_ds.receive(d);
					
//					if(d == null) {
//						System.out.println("RecvThread: run: d == null");
//					}
//					if(adapter == null) {
//						System.out.println("RecvThread: run: adapter == null");
//					}


					adapter.recv(d);
//				}
			}
		}
		catch(SocketException e) {
			System.err.println("recvThread: run: " + e);
		}
		catch(IOException ev) {
			System.err.println("recvThread: run: " + ev);
		}
	}



}
