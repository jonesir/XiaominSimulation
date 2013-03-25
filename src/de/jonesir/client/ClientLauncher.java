package de.jonesir.client;

import java.util.concurrent.LinkedBlockingQueue;

import de.jonesir.beans.Block;
import de.jonesir.server.Server;

public class ClientLauncher {

    private static final long blockCount = 10000;
    public static LinkedBlockingQueue<String> buffer1 = new LinkedBlockingQueue<String>();
    public static LinkedBlockingQueue<String> buffer2 = new LinkedBlockingQueue<String>();
    public static LinkedBlockingQueue<String> buffer3 = new LinkedBlockingQueue<String>();
    public static LinkedBlockingQueue<String> buffer4 = new LinkedBlockingQueue<String>();

    public static void main(String[] args) {
	// start the sender thread to send traffic once data is available in each queue
	new TrafficGenerator(Server.port1, 1).start();
	new TrafficGenerator(Server.port2, 2).start();
	new TrafficGenerator(Server.port3, 3).start();
	new TrafficGenerator(Server.port4, 4).start();

	int blockID;
	// generate data and put them into each queue
	for (int i = 0; i < ClientLauncher.blockCount; i++) {
	    // Scenario without coding
	    Block dataBlock = new Block(0, null);
	    blockID = (int)dataBlock.getBlockID();
	    
	    switch (blockID % 4) {
	    case 0:
		System.out.println("switch 0");
		ClientLauncher.buffer1.add(dataBlock.toBinaryString());
		System.out.println("Buffer Size : " + ClientLauncher.buffer1.size());
		break;
	    case 1:
		System.out.println("switch 1");
		ClientLauncher.buffer2.add(dataBlock.toBinaryString());
		System.out.println("Buffer Size : " + ClientLauncher.buffer2.size());
		break;
	    case 2:
		System.out.println("switch 2");
		ClientLauncher.buffer3.add(dataBlock.toBinaryString());
		System.out.println("Buffer Size : " + ClientLauncher.buffer3.size());
		break;
	    case 3:
		System.out.println("switch 3");
		ClientLauncher.buffer4.add(dataBlock.toBinaryString());
		System.out.println("Buffer Size : " + ClientLauncher.buffer4.size());
		break;
	    default:
		break;
	    }

	    // Scenario with coding
	}
	System.out.println("Simulation Finished!");
    }
}
