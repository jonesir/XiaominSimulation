package de.jonesir.client;

import java.util.concurrent.LinkedBlockingQueue;

import de.jonesir.beans.Block;
import de.jonesir.server.Server;
import de.jonesir.beans.Packet;

/**
 * Client for generating and putting data into corresponding link
 * 
 * @author Yuesheng Zhong
 * 
 */
public class ClientLauncher {

	public static final int port1 = 4189, port2 = 4190, port3 = 4191, port4 = 4192; // 4
																					// port
																					// numbers
																					// stands
																					// for
																					// 4
																					// corresponding
																					// links
																					// destination

	private static final boolean dataIsEncoded = true; // decide whether to be tranfered data is encoded or not
	public static final int linkCount = 4; // number of links through which
											// data will be sent
	public static final int blockCount = 100; // number of total blocks need
												// to be generated and sent
	// initialize 4 linked blocking queue as 4 links
	public static LinkedBlockingQueue<String> buffer1 = new LinkedBlockingQueue<String>();
	public static LinkedBlockingQueue<String> buffer2 = new LinkedBlockingQueue<String>();
	public static LinkedBlockingQueue<String> buffer3 = new LinkedBlockingQueue<String>();
	public static LinkedBlockingQueue<String> buffer4 = new LinkedBlockingQueue<String>();

	public static void main(String[] args) {
		// start the sender thread to send traffic once data is available in each queue
		// the 4 threads are each responsible for 1 link
		// get block data from each one and send it to corresponding destination
		new TrafficGenerator(Server.port1, 1).start();
		new TrafficGenerator(Server.port2, 2).start();
		new TrafficGenerator(Server.port3, 3).start();
		new TrafficGenerator(Server.port4, 4).start();

		// generate data and put them into each queue
		for (int i = 0; i < ClientLauncher.blockCount; i++) {
			/* Scenario without coding */
			if (!ClientLauncher.dataIsEncoded) {
				// generate a block
				Block dataBlock = new Block(0, null);

				// get the id of the block and assign the block to corresponding link
				// block will be assigned to certain buffer according to its ID
				// modulo 4
				switch ((int) dataBlock.getBlockID() % ClientLauncher.linkCount) {
				case 0:
					// System.out.println("switch 0");
					ClientLauncher.buffer1.add(dataBlock.toBinaryString());
//					System.out.println("Buffer Size 1 : " + ClientLauncher.buffer1.size());
					break;
				case 1:
					// System.out.println("switch 1");
					ClientLauncher.buffer2.add(dataBlock.toBinaryString());
//					System.out.println("Buffer Size 2 : " + ClientLauncher.buffer2.size());
					break;
				case 2:
					// System.out.println("switch 2");
					ClientLauncher.buffer3.add(dataBlock.toBinaryString());
//					System.out.println("Buffer Size 3 : " + ClientLauncher.buffer3.size());
					break;
				case 3:
					// System.out.println("switch 3");
					ClientLauncher.buffer4.add(dataBlock.toBinaryString());
//					System.out.println("Buffer Size 4 : " + ClientLauncher.buffer4.size());
					break;
				default:
					break;
				}
				/* Scenario with coding */
			} else {
				Packet dataPacket = new Packet();
				for(int j = 0 ; j < Packet.size ; j++){
					dataPacket.addBlock(new Block(0, dataPacket));
				}
				
				// get the id of the block and assign the block to corresponding link
				// block will be assigned to certain buffer according to its ID
				// modulo 4
				switch ((int) dataPacket.getPacketID() % ClientLauncher.linkCount) {
				case 0:
//					 System.out.println("switch 0");
					ClientLauncher.buffer1.add(dataPacket.toBinaryString());
//					System.out.println("Buffer Size 1 : " + ClientLauncher.buffer1.size());
					break;
				case 1:
//					 System.out.println("switch 1");
					ClientLauncher.buffer2.add(dataPacket.toBinaryString());
//					System.out.println("Buffer Size 2 : " + ClientLauncher.buffer2.size());
					break;
				case 2:
//					 System.out.println("switch 2");
					ClientLauncher.buffer3.add(dataPacket.toBinaryString());
//					System.out.println("Buffer Size 3 : " + ClientLauncher.buffer3.size());
					break;
				case 3:
//					 System.out.println("switch 3");
					ClientLauncher.buffer4.add(dataPacket.toBinaryString());
//					System.out.println("Buffer Size 4 : " + ClientLauncher.buffer4.size());
					break;
				default:
					break;
				}
			}

			

		}
		System.out.println("Simulation Finished!");
	}
}
