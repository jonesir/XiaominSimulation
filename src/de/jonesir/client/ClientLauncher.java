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

/**
 * Client for generating and putting data into corresponding link
 * 
 * @author Yuesheng Zhong
 * 
 */
public class ClientLauncher {

	public static final int port1 = 4189, port2 = 4190, port3 = 4191, port4 = 4192, terminatorPort = 4193; // 4
	// port
	// numbers
	// stands
	// for
	// 4
	// corresponding
	// links
	// destination

	public static final boolean dataIsEncoded = true; // decide whether to be tranfered data is encoded or not
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

	public static void main(String[] args) {
		// start the sender thread to send traffic once data is available in
		// each queue
		// the 4 threads are each responsible for 1 link
		// get block data from each one and send it to corresponding destination
		new TrafficGenerator(Server.port1, 1).start();
		new TrafficGenerator(Server.port2, 2).start();
		new TrafficGenerator(Server.port3, 3).start();
		new TrafficGenerator(Server.port4, 4).start();

		String[] packetUnit = new String[linkCount];
		int packetUnitIndex = 0;
		// generate data and put them into each queue
		for (int i = 0; i < ClientLauncher.blockCount; i++) {
			/* Scenario without coding */
			if (!ClientLauncher.dataIsEncoded) {
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

				//A generation of Packet has been collected, it is sent to be encoded and then sent to server
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
		// try {
		// Socket terminator = new Socket(TrafficGenerator.address,
		// ClientLauncher.terminatorPort);
		// BufferedWriter writer = new BufferedWriter(new
		// OutputStreamWriter(terminator.getOutputStream()));
		// writer.write("terminate");
		// writer.flush();
		// writer.close();
		// } catch (UnknownHostException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		System.out.println("Client Stops Sending !");
	}

	public static void log(String logString) {
		// System.out.println("ClientLauncher - " + logString);
	}
}
