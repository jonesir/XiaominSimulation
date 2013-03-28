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
		Thread bufferEmptier = new Thread(new BufferEmptier());
		
		// four thread which generate server socket listening on different ports for different links
		// these links put incoming data directly into the shared buffer
		Thread sp1 = new Thread(new ServerProcesser(port1));
		Thread sp2 = new Thread(new ServerProcesser(port2));
		Thread sp3 = new Thread(new ServerProcesser(port3));
		Thread sp4 = new Thread(new ServerProcesser(port4));
		
		// create a list of threads
		ArrayList<Thread> threads = new ArrayList<Thread>();
		threads.add(bufferEmptier);
		threads.add(sp1);
		threads.add(sp2);
		threads.add(sp3);
		threads.add(sp4);
		
		// start threads
		for(Thread t : threads){
			t.start();
		}
		
		// start the terminator thread
		new Thread(new Terminator(threads)).start();
	}
}
