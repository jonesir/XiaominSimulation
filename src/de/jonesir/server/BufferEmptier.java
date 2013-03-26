package de.jonesir.server;

import java.util.Iterator;

import de.jonesir.beans.Packet;
import de.jonesir.client.ClientLauncher;

public class BufferEmptier extends Thread {

    public synchronized void run() {
	log("BufferEmptier is Running ... ");
	while (true) {
	    log("waiting for shared buffer to have data");
	    log("Server.SHARED_BUFFER.size() = " + Server.SHARED_BUFFER.size());
	    if (Server.SHARED_BUFFER.size() != 0) {

		log("main logic of buffer emptier");
		String[] tempBuffer = new String[4];

		int index = 0;
		while (true) {
		    tempBuffer[index++] = Server.SHARED_BUFFER.getFirst();
		    int ID = Integer.parseInt(Server.SHARED_BUFFER.getFirst(), 2) % ClientLauncher.linkCount;
		    int bufferDataID;
		    for (String data : Server.SHARED_BUFFER) {
			bufferDataID = Integer.parseInt(data.substring(Packet.lengthWithouIdentifier), 2) % ClientLauncher.linkCount;
			if (ID == bufferDataID) {
			    tempBuffer[index++] = data;
			}
			if (index == 4) {
			    log("Complete Data Block/Packet found!!!");
			    for (String temp : tempBuffer) {
				log("ID = " + Integer.parseInt(temp.substring(Packet.lengthWithouIdentifier), 2));
			    }

			    Server.ORDERED_BUFFER.add(tempBuffer);
			    log("Data added to ordered buffer");
			    Iterator<String> share_buffer_it = Server.SHARED_BUFFER.iterator();
			    for (String temp : tempBuffer) {
				while (share_buffer_it.hasNext())
				    if (temp.equals(share_buffer_it.next()))
					share_buffer_it.remove();
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

    public void log(String logString) {
	System.out.println("BufferEmptier ::: " + logString);
    }
}
