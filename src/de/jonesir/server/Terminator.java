package de.jonesir.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import de.jonesir.algo.GlobalConfig;
import de.jonesir.algo.Logger;
import de.jonesir.client.ClientLauncher;

public class Terminator implements Runnable {

	private volatile boolean stop = false;

	@SuppressWarnings("resource")
	@Override
	public synchronized void run() {
		log("Terminator is running ... ");
			try {
				ServerSocket ss = new ServerSocket(ClientLauncher.ports[4]);
				Socket s = ss.accept();

				BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String readString;
				while (!stop) {
					readString = reader.readLine();
					//log("readString = " + readString);
					if (readString != null) {
						if(readString.contains(":")){// next simulation with new configuration
							int confNumber = Integer.parseInt(readString.split(":")[1]);
							resetServer(true, confNumber);
							log("Simulation " + (confNumber-1) + " terminates");
							log("Simulation with new configuration: ");
							log("Total Packets: " + GlobalConfig.params.get(confNumber)[0] + ", Buffer Size: " + GlobalConfig.params.get(confNumber)[1] + ", Tempo: " + GlobalConfig.params.get(confNumber)[2] + ", Encoded Flag: " + GlobalConfig.params.get(confNumber)[3] + ", Packet Size: " + GlobalConfig.params.get(confNumber)[4]);
							log("begins ... ");
						}else if (readString.equals(GlobalConfig.NEXT_ROUND)) {// next round of simulation with the same configuration
							log(GlobalConfig.NEXT_ROUND);
							resetServer(false, 0);
						}else if (readString.equals(GlobalConfig.SIMU_TERMINATE)) { // stop the complete simulation
							// wait for 3 seconds and stop
							Thread.sleep(5000);
							log(" Complete Simulation Terminates");
							Logger.logAverage();
							ServerProcesser.stop = true;// stop the threads waiting for data
							BufferEmptier.stop = true; // stop the buffer emptier
							this.stop = true;
						}
						// set the time stamp of ending processing the SHARED_BUFFER
						GlobalConfig.end = System.nanoTime();
						// log the result
						Logger.logResult();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	private void resetServer(boolean confChanged, int confNumber){
		if(confChanged){
			int[] temp = GlobalConfig.params.get(confNumber);
			GlobalConfig.refreshParams(temp[0], temp[1], temp[2], temp[3], temp[4]);
		}
		
		synchronized (Server.lock) {
			Server.SHARED_BUFFER.clear();
		}
		GlobalConfig.begin = 0;
		GlobalConfig.end = 0;
		synchronized (ServerProcesser.lock1) {
			Server.NUMBER_OF_LOST_PACKETS = 0;
		}
	}
	
	
	private void log(String logString){
//		System.out.println("Terminator ::: " + logString);
		Logger.terminatorLog("Terminator ::: " + logString);
	}
}
