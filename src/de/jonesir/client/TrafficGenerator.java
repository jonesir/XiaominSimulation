package de.jonesir.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import de.jonesir.algo.GlobalConfig;
import de.jonesir.algo.Logger;
import de.jonesir.server.Server;

/**
 * Class serves as traffic sender for one link It takes Block out of a buffer assigned to this class and send it to the server side listening on a specific port
 * 
 * @author Yuesheng Zhong
 * 
 */
public class TrafficGenerator implements Runnable {

	public static String address = "127.0.0.1"; // host address of the server
	int port, bufferNumber; // port number for the instance of this class to send data and the buffer number it should take data from

	public static volatile boolean stop = false;

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
	@SuppressWarnings({ "resource", "unchecked" })
	public void run() {
		// get buffer that this instance need to take Block data from according to bufferNumber property
		LinkedBlockingQueue<String> buffer = null;
		buffer = (LinkedBlockingQueue<String>) ClientLauncher.buffers[bufferNumber];

		String dataString;
		try {
			// create sender to specific host and port
			Socket s = new Socket(address, port);
			BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			// DELAY
			for(int i = 0 ; i < GlobalConfig.links_amount ; i++){
				if(port==ClientLauncher.ports[i]){
					Thread.sleep(GlobalConfig.delays[i]);
				}
			}

			while (!stop) {
				dataString = buffer.take();

				// setting data sending tempo
				for(int i = 0 ; i < GlobalConfig.links_amount ; i++){
					if(port == ClientLauncher.ports[i]){
						Thread.sleep(GlobalConfig.tempos[i],GlobalConfig.tempoNs[i]);
					}
				}

				// after this point, data will be sent to the server side
				sender.write(dataString + "\n");
				sender.flush();
			}
			
			sender.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	private void log(String logString){
		Logger.trafficGeneratorLog("TrafficGenerator ::: " + logString);
	}
}
