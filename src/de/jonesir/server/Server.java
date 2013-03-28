package de.jonesir.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Server that accept data from client through 4 different links and process them accordingly
 * 
 * @author Yuesheng Zhong
 * 
 */
public class Server {

	// four ports stand for 4 links
	public static final int port1 = 4189, port2 = 4190, port3 = 4191, port4 = 4192;

	// shared buffer to store incoming data in time order
	public static LinkedList<String> SHARED_BUFFER = new LinkedList<String>();

	// shared buffer size
	public static final int MAX_SHARED_BUFFER_SIZE = 164;

	// Counter to recorde the number of lost packets, this is important variable for logging the simulation result
	public static int NUMBER_OF_LOST_PACKETS = 0;
	
	// Synchronized lock object
	public static int[] lock = new int[0];

	public static void main(String[] args) {

		// buffer emptier remove the data from shared buffer if complete generation has been received
		new Thread(new BufferEmptier()).start();

		// four thread which generate server socket listening on different ports for different links
		// these links put incoming data directly into the shared buffer
		new Thread(new ServerProcesser(port1)).start();
		new Thread(new ServerProcesser(port2)).start();
		new Thread(new ServerProcesser(port3)).start();
		new Thread(new ServerProcesser(port4)).start();
	}
}
