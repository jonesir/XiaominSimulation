package de.jonesir.server;

import de.jonesir.beans.Packet;
import de.jonesir.client.ClientLauncher;

public class BufferEmptier extends Thread {

	public void run() {
		synchronized (Server.SHARED_BUFFER) {
			log("BufferEmptier is Running ... " );
			while (true) {
				log("waiting for shared buffer to have data");
				log("Server.SHARED_BUFFER.size() = " + Server.SHARED_BUFFER.size());
				if (Server.SHARED_BUFFER.size() != 0) {
					
					log("main logic of buffer emptier");
					System.exit(0);
					String[] tempBuffer = new String[4];
					
					int index = 0;
					while (true) {
						tempBuffer[index++] = Server.SHARED_BUFFER.getFirst();
						int ID = Integer.parseInt(Server.SHARED_BUFFER.getFirst()) % ClientLauncher.linkCount;
						int bufferDataID;
						for (String data : Server.SHARED_BUFFER) {
							bufferDataID = Integer.parseInt(data.substring(Packet.lengthWithouIdentifier)) % ClientLauncher.linkCount;
							if (ID == bufferDataID) {
								tempBuffer[index++] = data;
							}
							if (index == 4) {
								log("Complete Data Block/Packet found!!!");
								for (String temp : tempBuffer) {
									log("ID = " + Integer.parseInt(temp.substring(Packet.lengthWithouIdentifier)));
								}

								Server.ORDERED_BUFFER.add(tempBuffer);
								log("Data added to ordered buffer");
								
								for (String temp : tempBuffer) {
									Server.SHARED_BUFFER.remove(temp);
									log("data removed from shared buffer");
								}
								break;
							}

						}
						index = 0;
					}
				}
			}
		}
	}
	
	public void log(String logString){
		System.out.println("BufferEmptier ::: " + logString);
	}
}
