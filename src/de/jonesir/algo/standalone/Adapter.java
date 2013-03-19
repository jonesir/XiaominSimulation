package de.jonesir.algo.standalone;
import java.net.*;



interface Adapter {

//	public DatagramPacket toDatagram();

	public void run();
	public void send();
	public void recv(DatagramPacket d);


}
