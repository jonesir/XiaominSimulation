package de.jonesir.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.jonesir.algo.Encoder;
import de.jonesir.algo.GlobalConfig;

import de.jonesir.beans.Packet;
import de.jonesir.client.ClientLauncher;

/**
 * @author Yuesheng Zhong
 * 
 */
public class BufferEmptier implements Runnable {
    public BufferEmptier() {

    }

    @Override
    public void run() {
	log("BufferEmptier is Running ... ");

//	while (true) {
	while (!Thread.currentThread().isInterrupted()) {
	    synchronized (Server.lock) {
		System.out.println("====================================================");
		log("Server.SHARED_BUFFER.size() === " + Server.SHARED_BUFFER.size());
		if (Server.SHARED_BUFFER.size() != 0) {
		    if (GlobalConfig.shouldBegin) {
			GlobalConfig.begin = System.nanoTime();
			GlobalConfig.shouldBegin = false;
		    }
		    if (GlobalConfig.encoded) {
			// prepare a list of array to store possible candidates
			// iterate through the buffer
			for (int i = 0; i < Server.SHARED_BUFFER.size(); i++) {
			    // if (candidates.size() == 10)
			    // break;
			    // extract the id part out of packet and transfer it to decimal value
			    String tempString = Server.SHARED_BUFFER.get(i);
			    String idString = tempString.substring(tempString.length() - Encoder.BYTE_LENGTH);
			    int tempID = Integer.parseInt(idString, 2);
			    log("Packet_ID :" + tempID);

			    // four packets share the same keyID
			    int keyID = tempID / ClientLauncher.linkCount;
			    // check if the id is already in the map
			    // if yes, means this element belong to the same generation and should be inserted into the map value for the decoding later
			    if (candidates.containsKey(keyID)) {
				// log("if inside first for loop");
				// get the corresponding array list and insert the element into this list
				ArrayList<String> existingGeneration = candidates.get(tempID / ClientLauncher.linkCount);
				if (existingGeneration.size() != 4) {
				    existingGeneration.add(tempString);
				}
			    }
			    // if not, means this element belongs to no generation in the map yet, a new array list for generation with this id should be created
			    else {
				// log("else inside first for loop");
				ArrayList<String> newGeneration = new ArrayList<String>();
				newGeneration.add(tempString);
				candidates.put(keyID, newGeneration);
			    }
			}

			/*
			 * after the iteration, if in the map existing array list has 4 element, it means the reception of the generation with corresponding id is complete, and this should be decoded and delete from BOTH the MAP and the BUFFER, if the list doesn't have 4 element, it means the reception of the generation with corresponding id is not complete, delete it from the map directly
			 */

			for (int generationID : candidates.keySet()) {
			    log("( " + generationID + " , " + candidates.get(generationID).size() + " )");
			    // size of generation ==4 means it is complete, it should be 1, decoded, 2, delete from the SHARED_BUFFER
			    if (candidates.get(generationID).size() == 4) {
				/* 1. FIND THE COMPLETE GENERATION IN THE MAP */

				/* 2. DECODE THE COMPLETE GENERATION */
				prepareForDecoding(candidates.get(generationID));

				/* 3. REMOVE EACH PACKET OF THE GENERATION FROM THE SHARED_BUFFER */
				for (String packetString : candidates.get(generationID)) {
				    log("Server.SHARED_BUFFER.size() before remove = " + Server.SHARED_BUFFER.size());
				    Server.SHARED_BUFFER.remove(packetString);
				    log("Server.SHARED_BUFFER.size() after remove= " + Server.SHARED_BUFFER.size());
				}
			    }

			}
			/* After each round of iteration through the SHARED_BUFFER, clear the candidate map */
			candidates.clear();

		    } else { // no encoding
			String tempString;
			String idString;
			for (int i = 0; i < Server.SHARED_BUFFER.size(); i++) {
			    tempString = Server.SHARED_BUFFER.get(i);
			    idString = tempString.substring(tempString.length() - Encoder.BYTE_LENGTH);
			    // find the smallest element
			    if (Integer.parseInt(idString, 2) == minID) {
				// log("element with ID : " + minID + " found!");
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
				// increment the ID
				minID++;
			    }
			}
		    }
		}
	    }
	}
    }

    private static int minID = 1;
    private static HashMap<Integer, ArrayList<String>> candidates = new HashMap<Integer, ArrayList<String>>();

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
