package de.jonesir.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import de.jonesir.beans.Block;
import de.jonesir.server.Server;
import de.jonesir.beans.Packet;
import de.jonesir.algo.Encoder;
import de.jonesir.algo.GlobalConfig;

/**
 * Client for generating and putting data into corresponding link
 * 
 * @author Yuesheng Zhong
 * 
 */
public class ClientLauncher {
    // different ports for data link and command sending
    public static final int port1 = 4189, port2 = 4190, port3 = 4191, port4 = 4192, terminatorPort = 4193; // 4

    public static final boolean encoded = true; // decide whether to be tranfered data is encoded or not
    public static final int linkCount = 4; // number of links through which data will be sent
    public static final int blockCount = 1000; // number of total blocks need
    public static final Random random = new Random();// generate random number within range
    public static final int bits = 65;// bits number in binary
    // to be generated and sent
    // initialize 4 linked blocking queue as 4 links
    public static LinkedBlockingQueue<String> buffer1 = new LinkedBlockingQueue<String>();
    public static LinkedBlockingQueue<String> buffer2 = new LinkedBlockingQueue<String>();
    public static LinkedBlockingQueue<String> buffer3 = new LinkedBlockingQueue<String>();
    public static LinkedBlockingQueue<String> buffer4 = new LinkedBlockingQueue<String>();

    @SuppressWarnings("resource")
    public static void main(String[] args) {
	// start the sender thread to send traffic once data is available in
	// each queue
	// the 4 threads are each responsible for 1 link
	// get block data from each one and send it to corresponding destination
	new TrafficGenerator(Server.port1, 1).start();
	new TrafficGenerator(Server.port2, 2).start();
	new TrafficGenerator(Server.port3, 3).start();
	new TrafficGenerator(Server.port4, 4).start();

	// prepare the possible parameters combination list
	GlobalConfig.generateParams();// after this, the parameter list has entries with all combinations
	int simulation_counter = 1;

	// iterate through all the combinations
	for (int[] params : GlobalConfig.params) {

	    // initialized parameters for each round of simulation
	    GlobalConfig.refreshParams(params[0], params[1], params[2], params[3]);
	    System.out.println("Total Packets: " + params[0] + ", Buffer Size: " + params[1] + ", Tempo: " + params[2] + ", Encoded Flag: " + params[3]);

	    String[] packetUnit = new String[linkCount];
	    int packetUnitIndex = 0;
	    // generate data and put them into each queue
	    for (int i = 0; i < GlobalConfig.amount_of_packets; i++) {
		/* Scenario without coding */
		if (!GlobalConfig.encoded) {
		    // generate a block
		    Block dataBlock = new Block(0, null);

		    // get the id of the block and assign the block to corresponding
		    // link
		    // block will be assigned to certain buffer according to its ID
		    // modulo 4
		    switch ((int) dataBlock.getBlockID() % ClientLauncher.linkCount) {
		    case 0:
			// System.out.println("switch 0");
			ClientLauncher.buffer1.add(dataBlock.toBinaryString());
			// System.out.println("Buffer Size 1 : " +
			// ClientLauncher.buffer1.size());
			break;
		    case 1:
			// System.out.println("switch 1");
			ClientLauncher.buffer2.add(dataBlock.toBinaryString());
			// System.out.println("Buffer Size 2 : " +
			// ClientLauncher.buffer2.size());
			break;
		    case 2:
			// System.out.println("switch 2");
			ClientLauncher.buffer3.add(dataBlock.toBinaryString());
			// System.out.println("Buffer Size 3 : " +
			// ClientLauncher.buffer3.size());
			break;
		    case 3:
			// System.out.println("switch 3");
			ClientLauncher.buffer4.add(dataBlock.toBinaryString());
			// System.out.println("Buffer Size 4 : " +
			// ClientLauncher.buffer4.size());
			break;
		    default:
			break;
		    }
		    /* Scenario with coding */
		} else {
		    // generate Packet
		    Packet dataPacket = new Packet();
		    for (int j = 0; j < Packet.size; j++) {
			dataPacket.addBlock(new Block(0, dataPacket));
		    }
		    // put packet's binary string into array, which will
		    packetUnit[packetUnitIndex++] = dataPacket.toBinaryString();

		    // A generation of Packet has been collected, it is sent to be encoded and then sent to server
		    if ((i + 1) % 4 == 0) {
			// encode the generation of packets
			String[] encodedPacket = Encoder.encode_apache(packetUnit);

			// place each of the packet in the generation into different Linked Blocking Queue
			ClientLauncher.buffer1.add(encodedPacket[0]);
			ClientLauncher.buffer2.add(encodedPacket[1]);
			ClientLauncher.buffer3.add(encodedPacket[2]);
			ClientLauncher.buffer4.add(encodedPacket[3]);

			// reset
			packetUnitIndex = 0;
		    }
		}

	    }

	    // after half second, send the termination command to terminate this round of simulation and log the result
	    try {
		Thread.sleep(500);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }

	    // send termination command to Terminator.java on the server side
	    try {
		Socket terminator = new Socket(TrafficGenerator.address, GlobalConfig.terminatorPort);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(terminator.getOutputStream()));
		writer.write("terminate");
		writer.flush();
	    } catch (UnknownHostException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    System.out.println("Simluation Round " + (simulation_counter++) + " finishes");

	    // after 5 seconds, begin another simulation with new combination of parameters
	    try {
		Thread.sleep(5000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    // reset all resources for the new round of simulation
	    reset();
	}

	// close connections to stop client threads
	TrafficGenerator.closeConnection = true;

	try {
	    Thread.sleep(500);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	ClientLauncher.buffer1.add("completeTerminate");
	ClientLauncher.buffer2.add("completeTerminate");
	ClientLauncher.buffer3.add("completeTerminate");
	ClientLauncher.buffer4.add("completeTerminate");

	// send termination command to Terminator.java on the server side
	try {
	    Socket terminator = new Socket(TrafficGenerator.address, GlobalConfig.terminatorPort);
	    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(terminator.getOutputStream()));
	    writer.write("completeTerminate");
	    writer.flush();
	    // After five second of all the simulations, close command connection
	    Thread.sleep(5000);
	    writer.close();
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	System.out.println("Client Stops Sending !");

	System.exit(0);
    }

    public static void reset() {
	Block.resetID();
	Packet.resetID();
	ClientLauncher.buffer1.clear();
	ClientLauncher.buffer2.clear();
	ClientLauncher.buffer3.clear();
	ClientLauncher.buffer4.clear();
    }

    public static void log(String logString) {
	// System.out.println("ClientLauncher - " + logString);
    }
}
