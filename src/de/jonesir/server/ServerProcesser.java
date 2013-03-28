package de.jonesir.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import de.jonesir.algo.GlobalConfig;
import de.jonesir.algo.Logger;

/**
 * Process the incoming data through a specific link
 * 
 * @author Yuesheng Zhong
 * 
 */
public class ServerProcesser implements Runnable {

    int port; // port number that this thread listen on

    public ServerProcesser(int port) {
	this.port = port;
    }

    @SuppressWarnings("resource")
    @Override
    public synchronized void run() {
	try {
	    // socket socket on a given port
	    ServerSocket ss = new ServerSocket(port);
	    System.out.println("Listening on port : " + port);

	    // Connection with the corresponding client established
	    Socket s = ss.accept();
	    System.out.println("data coming : " + port);

	    // process the incoming data
	    try {
		// initialize the buffered reader on socket input stream
		BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String incomingString = "";
		// read the data from the connection and do the process work
		while ((!Thread.currentThread().isInterrupted()) && (incomingString = reader.readLine()) != null) {
		    log("insertIntoBuffer");

		    synchronized (Server.lock) {
			// put the incoming string into the shared buffer if it is not full
			if (Server.SHARED_BUFFER.size() < GlobalConfig.MAX_SHARED_BUFFER_SIZE) {
			    Server.SHARED_BUFFER.add(incomingString);
			}
			// otherwise increment the packet lost variable
			else {
			    synchronized (this) {
				System.out.println("Lost : " + Server.NUMBER_OF_LOST_PACKETS++);
			    }
			}
		    }
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void log(String logString) {
	// System.out.println("ServerProcesser - " + port + " : " + logString);
    }
}
