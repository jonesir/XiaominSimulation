package de.jonesir.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Process the incoming data through a specific link
 * 
 * @author Yuesheng Zhong
 * 
 */
public class ServerProcesser implements Runnable {

	int port; // port number that this thread listen on
	private static int dataCount1 = 0, dataCount2 = 0, dataCount3 = 0, dataCount4 = 0;

	public ServerProcesser(int port) {
		this.port = port;
	}

	@SuppressWarnings("resource")
	@Override
	public void run() {
		try {
			// socket socket listening on a given port
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Listening on port : " + port);

			// data coming in
			Socket s = ss.accept();
			System.out.println("data coming : " + port);
			// process the incoming data
			process(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Process the incoming data
	 * 
	 * @param s
	 *            socket for this method to get input stream
	 */
	private void process(Socket s) {
		try {
			// initialize the buffered reader on socket input stream
			BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String incomingString = "";
			// read the data and do the process work from specific link according to port number
			while ((incomingString = reader.readLine()) != null) {
				// put the incoming string into the shared buffer , which is of size 64 blocks
				 insertIntoBuffer(incomingString);
//				switch (port) {
//				case Server.port1:
//					System.out.println(port + " : '" + (dataCount1++) + "'  " + incomingString + " --- size : " + incomingString.length());
//					break;
//				case Server.port2:
//					System.out.println(port + " : '" + (dataCount2++) + "'  " + "\t" + incomingString + " --- size : " + incomingString.length());
//					break;
//				case Server.port3:
//					System.out.println(port + " : '" + (dataCount3++) + "'  " + "\t\t" + incomingString + " --- size : " + incomingString.length());
//					break;
//				case Server.port4:
//					System.out.println(port + " : '" + (dataCount4++) + "'  " + "\t\t\t" + incomingString + " --- size : " + incomingString.length());
//					break;
//				default:
//					break;
//				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * put the incoming data into shared buffer
	 * 
	 * @param dataString
	 */
	private void insertIntoBuffer(String dataString) {
		synchronized (Server.SHARED_BUFFER) {
			log("insertIntoBuffer");
			if (Server.SHARED_BUFFER.size() <= Server.bufferSize){
				log("insertIntoBuffer : if");
				
				Server.SHARED_BUFFER.add(dataString);
			} else {
				log("insertIntoBuffer : else");
				Server.packetLost++;
				System.out.println("number of packet lost : " + Server.packetLost);
			}
		}
	}
	
	private void log(String logString){
		System.out.println("ServerProcesser - " + port + " : " + logString);
	}
}
