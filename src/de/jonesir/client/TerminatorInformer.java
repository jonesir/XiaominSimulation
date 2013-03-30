package de.jonesir.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import de.jonesir.algo.Logger;

public class TerminatorInformer implements Runnable {

	public volatile boolean stop = false;
	
	public TerminatorInformer(){
		
	}

	@SuppressWarnings("resource")
	@Override
	public void run() {
		LinkedBlockingQueue<String> buffer = ClientLauncher.terminationBuffer;

		try {
			Socket s = new Socket(TrafficGenerator.address, ClientLauncher.ports[4]);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			String dataString;
			while (!stop) {
				dataString = buffer.take();
				log("dataString = " + dataString);
				writer.write(dataString+"\n");
				writer.flush();
			}
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void log(String logString){
		Logger.terminatorInformerLog(" TerminatorInformer ::: " + logString);
	}
}
