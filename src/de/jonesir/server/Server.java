package de.jonesir.server;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Server that accept data from client through 4 different links and process them accordingly
 * 
 * @author Yuesheng Zhong
 * 
 */
public class Server {

	// four ports stand for 4 links
	public static final int port1 = 4189, port2 = 4190, port3 = 4191, port4 = 4192;
	// shared buffer to store incoming data in an timely order
	public static LinkedList<String> SHARED_BUFFER = new LinkedList<String>();
	public static ArrayList<String[]> ORDERED_BUFFER = new ArrayList<String[]>();
	// shared buffer size
	public static final int bufferSize = 64;
	public static int packetLost = 0;

	public static void main(String[] args) {
		// four thread which generate server socket listening on different ports for different links
		new ServerProcesser(port1).start();
		new ServerProcesser(port2).start();
		new ServerProcesser(port3).start();
		new ServerProcesser(port4).start();
		// buffer emptier remove the data from shared buffer if complete data has been received
		new BufferEmptier().start();
	}
}
