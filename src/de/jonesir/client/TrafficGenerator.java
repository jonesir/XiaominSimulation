package de.jonesir.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import de.jonesir.server.Server;

/**
 * Class serves as traffic sender for one link It takes Block out of a buffer assigned to this class and send it to the server side listening on a specific port
 * 
 * @author Yuesheng Zhong
 * 
 */
public class TrafficGenerator extends Thread {

	String address = "127.0.0.1"; // host address of the server
	int port, bufferNumber; // port number for the instance of this class to send data and the buffer number it should take data from

	/**
	 * @param port
	 * @param bufferNumber
	 */
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

		// get buffer that this instance need to take Block data from according to bufferNumber property
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
		try {
			// create sender to specific host and port
			Socket s = new Socket(address, port);
			BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			
			// the loop to take Block from buffer and send to corresponding server and port
			while (true) {
				dataString = buffer.take();
				System.out.println("buffer '" + bufferNumber + "',  retrieved dataString : " + dataString);
//				sender.write(dataString+"\n");
//				sender.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
		}
	}
}
