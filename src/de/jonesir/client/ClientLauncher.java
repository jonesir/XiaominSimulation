package de.jonesir.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
	// each port stands for a link or command port
	public static final int[] ports = { 4189, 4190, 4191, 4192, 4193 };
	public static final Random random = new Random();// generate random number within range

	@SuppressWarnings("rawtypes")
	public static LinkedBlockingQueue[] buffers = new LinkedBlockingQueue[GlobalConfig.links_amount];
	public static LinkedBlockingQueue<String> terminationBuffer = new LinkedBlockingQueue<String>();

	private static String[] packetUnit = new String[GlobalConfig.links_amount];
	private static int packetUnitIndex = 0;
	
	@SuppressWarnings("unchecked")
	private static void init() {
		// prepare the possible parameters combination list
		GlobalConfig.init();// after this, the parameter list has entries with all combinations

		// generate buffers
		for (int i = 0; i < GlobalConfig.links_amount; i++) {
			buffers[i] = new LinkedBlockingQueue<String>();
		}

		// generate thread for each link(buffer)
		Thread[] threads = new Thread[GlobalConfig.links_amount];
		for (int i = 0; i < GlobalConfig.links_amount; i++) {
			Thread tmp = new Thread(new TrafficGenerator(Server.ports[i], i));
			threads[i] = new Thread(new TrafficGenerator(Server.ports[i], i));
			tmp.start();
		}
		
		// generate terminator informer thread
		new Thread(new TerminatorInformer()).start();
	}

	@SuppressWarnings({ "resource", "unchecked" })
	public static void main(String[] args) {
		// init work
		init();

		// iterate through all the parameter combinations
		for (int confNumber = 0; confNumber < GlobalConfig.params.size(); confNumber++) {

			// initialized parameters for each simulation under certain configurations
			GlobalConfig.refreshParams(GlobalConfig.params.get(confNumber)[0], GlobalConfig.params.get(confNumber)[1], GlobalConfig.params.get(confNumber)[2], GlobalConfig.params.get(confNumber)[3], GlobalConfig.params.get(confNumber)[4]);
			System.out.println("Simulation with following configuration: ");
			System.out.println("Total Packets: " + GlobalConfig.params.get(confNumber)[0] + ", Buffer Size: " + GlobalConfig.params.get(confNumber)[1] + ", Tempo: " + GlobalConfig.params.get(confNumber)[2] + ", Encoded Flag: " + GlobalConfig.params.get(confNumber)[3] + ", Packet Size: " + GlobalConfig.params.get(confNumber)[4]);
			System.out.println("begins ... ");
			for (int simuCount = 0; simuCount < GlobalConfig.rounds_of_simulation_per_configuration; simuCount++) {
				System.out.println("Simluation Round " + (simuCount + 1) + " begins");
				// generate data and put them into each queue
				for (int i = 0; i < GlobalConfig.packets_sent_in_one_simulation; i++) {
					/* Scenario with coding */
					if (GlobalConfig.encoded) {
						sentPackets(i);
						/* Scenario without coding */
					} else {
						sentBlocks();
					}
				}

				try {
					Thread.sleep(500);
					// tell the server to do corresponding reset work for the next round of simulation with the same configuration
					informServerOfNextRound();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				System.out.println("Simluation Round " + (simuCount + 1) + " finishes");

				// reset all resources for the new round of simulation
				reset();

				// after 1 seconds, begin another simulation with the same configuration
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			try {
				Thread.sleep(500);
				// tell the server to do corresponding reset work for the simulation with new configuration
				informServerOfNextConfig(confNumber);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Simulation with following configuration: ");
			System.out.println("Total Packets: " + GlobalConfig.params.get(confNumber)[0] + ", Buffer Size: " + GlobalConfig.params.get(confNumber)[1] + ", Tempo: " + GlobalConfig.params.get(confNumber)[2] + ", Encoded Flag: " + GlobalConfig.params.get(confNumber)[3] + ", Packet Size: " + GlobalConfig.params.get(confNumber)[4]);
			System.out.println("finishes ... ");
			System.out.println("==================================================");

			// after 1 seconds, begin another simulation with new configuration
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// tell the server to terminate the simulation completely and do the final logging work
		informServerOfSimuTermination();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// terminate client threads
		terminateClientThreads();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("------============ ALL SIMULATIONS FINISHES ============-----------");

		System.exit(0);
	}

	@SuppressWarnings("unchecked")
	private static void sentBlocks() {
		// generate a block
		Block dataBlock = new Block(0, null);

		// send the block through corresponding link
		int bufferNumber = (int) dataBlock.getBlockID() % GlobalConfig.links_amount;
		((LinkedBlockingQueue<String>) buffers[bufferNumber]).add(dataBlock.toBinaryString());
	}

	@SuppressWarnings("unchecked")
	private static void sentPackets(int p) {
		// generate Packet
		Packet dataPacket = new Packet();
		for (int j = 0; j < Packet.size; j++) {
			dataPacket.addBlock(new Block(0, dataPacket));
		}
		// put packet's binary string into array, which will
		packetUnit[packetUnitIndex++] = dataPacket.toBinaryString();

		// A generation of Packet has been collected, it is sent to be encoded and then sent to server
		if ((p + 1) % 4 == 0) {
			// encode the generation of packets
			String[] encodedPacket = Encoder.encode_apache(packetUnit);

			// place each of the packet in the generation into different Linked Blocking Queue
			for (int i = 0; i < GlobalConfig.links_amount; i++) {
				((LinkedBlockingQueue<String>) buffers[i]).add(encodedPacket[i]);
			}

			// reset
			packetUnitIndex = 0;
		}

	}

	private static void informServerOfNextRound() {
		ClientLauncher.terminationBuffer.add(GlobalConfig.NEXT_ROUND);
	}

	private static void informServerOfNextConfig(int configNumber) {
		ClientLauncher.terminationBuffer.add(GlobalConfig.NEXT_CONFIG + ":" + configNumber);
	}

	private static void informServerOfSimuTermination() {
		ClientLauncher.terminationBuffer.add(GlobalConfig.SIMU_TERMINATE);
	}
	
	private static void terminateClientThreads(){
		TrafficGenerator.stop = true;
	}

	@SuppressWarnings("unchecked")
	public static void reset() {
		Block.resetID();
		Packet.resetID();
		for (LinkedBlockingQueue<String> buffer : ClientLauncher.buffers) {
			buffer.clear();
		}
	}

	public static void log(String logString) {
		// System.out.println("ClientLauncher - " + logString);
	}
}
