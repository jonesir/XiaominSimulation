package de.jonesir.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import de.jonesir.algo.GlobalConfig;

/**
 * Server that accept data from client through 4 different links and process them accordingly
 * 
 * @author Yuesheng Zhong
 * 
 */
public class Server {

	// each port stands for a link or command port
	public static final int[] ports = { 4189, 4190, 4191, 4192, 4193 };

	// shared buffer to store incoming data in time order
	public static LinkedList<String> SHARED_BUFFER = new LinkedList<String>();
	
	public static Thread[] threads = new Thread[GlobalConfig.links_amount];

	public static Thread bufferEmptier = new Thread(new BufferEmptier());
	// Counter to recorde the number of lost packets, this is important variable for logging the simulation result
	public static int NUMBER_OF_LOST_PACKETS = 0;

	// Synchronized lock object
	public static int[] lock = new int[0];

	public static void resetParams() {
		synchronized (Server.lock) {
			Server.SHARED_BUFFER.clear();
		}
		GlobalConfig.begin = 0;
		GlobalConfig.end = 0;
		synchronized (ServerProcesser.lock1) {
			Server.NUMBER_OF_LOST_PACKETS = 0;
		}
	}

	public static void main(String[] args) {
		// init work
		init();

		// buffer emptier remove the data from shared buffer if complete generation has been received
		Thread bufferEmptier = new Thread(new BufferEmptier());

		// create for each link a thread to read data from port and insert it into shared buffer
		for(int i = 0 ; i < GlobalConfig.links_amount ; i++){
			threads[i] = new Thread(new ServerProcesser(Server.ports[i]));
		}

		// start port listening thread
		for (int i = 0 ; i < threads.length ; i++) {
			threads[i].start();
		}

		// start the emptier thread
		bufferEmptier.start();
		
		// start the terminator thread
		new Thread(new Terminator()).start();
	}

	private static void init() {
		// prepare the possible parameters list
		GlobalConfig.init();
		setFirstSimuParameter();
	}

	private static void setFirstSimuParameter() {
		int[] temp = GlobalConfig.params.get(0);
		GlobalConfig.refreshParams(temp[0], temp[1], temp[2], temp[3], temp[4]);
	}
	
}
