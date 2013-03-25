package de.jonesir.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import de.jonesir.server.Server;

public class TrafficGenerator extends Thread {

    String address = "127.0.0.1";
    int port, bufferNumber;

    public TrafficGenerator(int port, int bufferNumber) {
	this.port = port;
	this.bufferNumber = bufferNumber;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @SuppressWarnings("resource")
    public void run() {
	try {
	    // create sender to specific host and port
	    Socket s = new Socket(address, port);
	    BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
	    LinkedBlockingQueue<String> buffer = null;
	    switch (bufferNumber) {
	    case 1:
		buffer = ClientLauncher.buffer1;
		break;
	    case 2:
		buffer = ClientLauncher.buffer2;
		break;
	    case 3:
		buffer = ClientLauncher.buffer3;
		break;
	    case 4:
		buffer = ClientLauncher.buffer4;
		break;
	    default:
		break;
	    }
	    String dataString;

	    while (true) {
		dataString = buffer.take();
		System.out.println("retrieved dataString : " + dataString);
//		sender.write(dataString);
//		sender.flush();
	    }

	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }
}
