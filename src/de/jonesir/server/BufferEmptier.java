package de.jonesir.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.jonesir.algo.Encoder;

import de.jonesir.beans.Packet;
import de.jonesir.client.ClientLauncher;

/**
 * @author Yuesheng Zhong
 * 
 */
public class BufferEmptier implements Runnable {
    private static int minID = 1;
    private static HashMap<Integer, ArrayList<String>> candidates = new HashMap<Integer, ArrayList<String>>();

    public synchronized void run() {
	// log("BufferEmptier is Running ... ");
	while (true) {
	    // synchronized (Server.SHARED_BUFFER) {
	    // log("waiting for shared buffer to have data");
	    // log("Server.SHARED_BUFFER.size() = " + Server.SHARED_BUFFER.size());

	    if (Server.SHARED_BUFFER.size() != 0) {
		if (ClientLauncher.dataIsEncoded) {
		    // prepare a list of array to store possible candidates
		    // iterate through the buffer
		    for (int i = 0; i < Server.SHARED_BUFFER.size(); i++) {
			// extract the id part out of packet and transfer it to decimal value
			String tempString = Server.SHARED_BUFFER.get(i);
			String idString = tempString.substring(tempString.length() - Encoder.BYTE_LENGTH);
			int tempID = Integer.parseInt(idString, 2);

			// check if the id is already in the map
			// if yes, means this element belong to the same generation and should be inserted into the map value for the decoding later
			if (candidates.containsKey(tempID / ClientLauncher.linkCount)) {
			    // get the corresponding array list and insert the element into this list
			    candidates.get(tempID / ClientLauncher.linkCount).add(tempString);
			}
			// if not, means this element belongs to no generation in the map yet, a new array list for generation with this id should be created
			else {
			    ArrayList<String> tempList = new ArrayList<String>();
			    tempList.add(tempString);
			    candidates.put(tempID / ClientLauncher.linkCount, tempList);
			}
		    }

		    /*
		     * after the iteration, if in the map existing array list has 4 element, it means the reception of the generation with corresponding id is complete, and this should be decoded and delete from BOTH the MAP and the BUFFER, if the list doesn't have 4 element, it means the reception of the generation with corresponding id is not complete, delete it from the map directly
		     */

		    for (Integer id : candidates.keySet()) {
			// if the array list has 4 elements
			ArrayList<String> tempList = candidates.get(id);
			if (tempList.size() == 4) {
			    // sent to decoder for decoding
			    prepareForDecoding(tempList);
			    // delete each element out of the array list from the BUFFER
			    for (String packetString : tempList)
				Server.SHARED_BUFFER.remove(packetString);
			}

			// clear all the entries in the map for the next round
			candidates.clear();
		    }
		} else { // no encoding
		    String tempString;
		    String idString;
		    for (int i = 0; i < Server.SHARED_BUFFER.size(); i++) {
			tempString = Server.SHARED_BUFFER.get(i);
			idString = tempString.substring(Server.SHARED_BUFFER.size() - Encoder.BYTE_LENGTH);
			// find the smallest element
			if (Integer.parseInt(idString, 2) == minID) {
			    log("element with ID : " + minID + " found!");
			    // swap the element with the smallest ID to the first position
			    if (i != 0) {
				log("switch to the first position");
				String tmp = Server.SHARED_BUFFER.getFirst();
				Server.SHARED_BUFFER.set(0, Server.SHARED_BUFFER.get(i));
				Server.SHARED_BUFFER.set(i, tmp);
			    }
			    log("remove the first element");
			    // remove the first element
			    Server.SHARED_BUFFER.removeFirst();
			}
		    }
		}
	    }
	}
    }

    private String[] prepareForDecoding(ArrayList<String> candidate) {
	String[] generation = new String[ClientLauncher.linkCount];
	for (int i = 0; i < generation.length; i++) {
	    generation[i] = candidate.get(i);
	}

	return Encoder.decode_apache(generation);
    }

    public void log(String logString) {
	System.out.println("BufferEmptier ::: " + logString);
    }
}
