package de.jonesir.algo;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalConfig {

	/** ====================================================================================== */
	// how many times should the simulation run with a certain configuration
	public static int rounds_of_simulation_per_configuration = 10;

	// number of links
	public static int links_amount = 4;

	// number of total blocks need
	public static int packets_sent_in_one_simulation = 20000;

	// shared buffer size
	public static int MAX_SHARED_BUFFER_SIZE = 1000;// 1000 - 5000 packets

	// specify sending tempo in nanosecond
	public static int[] tempoNs = {1000,1000,1000,1000};// 1000(4MB), 2500(1.6MB), 5000(800KB), 10000(400KB)

	// decide whether to be tranfered data is encoded or not
	public static boolean encoded = true;

	// define packet size
	public static int packet_size = 1;

	/** ====================================================================================== */

	// possible parameter values
	public static int[] AMOUNT_OF_PACKETS = { 20000 };
	public static int[] MAX_SHARED_BUFFER_SIZES = {1000, 2000}; //{ 1000, 2000, 3000, 4000, 5000 };
	public static int[] TEMPOS = {1000, 5000};//{ 1000, 5000, 2500, 10000 };
	public static int[] ENCODE = { 0, 1 };
	public static int[] PACKET_SIZES = { 1 };

	public static int paramCombinationCounter = 0;

	// generate combination of parameters with each set for each round of simulation
	// and store all the combination in an array list
	public static ArrayList<int[]> params = new ArrayList<int[]>();

	public static void init() {
		// make a list of configuration parameters
		for (int i = 0; i < AMOUNT_OF_PACKETS.length; i++) {// get packets number of a simulation
			for (int j = 0; j < MAX_SHARED_BUFFER_SIZES.length; j++) {// get buffer size
				for (int k = 0; k < TEMPOS.length; k++) { // get sending tempo
					for (int m = 0; m < ENCODE.length; m++) { // get encode flag
						for (int n = 0; n < PACKET_SIZES.length; n++) {
							int[] temp = { AMOUNT_OF_PACKETS[i], MAX_SHARED_BUFFER_SIZES[j], TEMPOS[k], ENCODE[m], PACKET_SIZES[n] };
							params.add(temp);
						}
					}
				}
			}
		}

		// trivial
		mapping.put(1000, "4MB");
		mapping.put(2500, "1.6MB");
		mapping.put(5000, "800KB");
		mapping.put(10000, "400KB");

	}

	public static HashMap<Integer, String> mapping = new HashMap<Integer, String>();

	// *********************************************************************************************************
	public static void main(String[] args) {
		init();
		for (int[] param : params)
			System.out.println(param[0] + ", " + param[1] + ", " + param[2] + ", " + param[3] + ", " + param[4]);
	}

	public static void refreshParams(int numberOfPackets, int maxBufferSize, int tempo, int encode, int packetSize) {
		GlobalConfig.packets_sent_in_one_simulation = numberOfPackets;
		GlobalConfig.MAX_SHARED_BUFFER_SIZE = maxBufferSize;
		for(int i = 0 ; i<GlobalConfig.tempoNs.length ; i++){
			GlobalConfig.tempoNs[i] = tempo;
		}
		GlobalConfig.encoded = (encode == 0) ? false : true;
		GlobalConfig.packet_size = packetSize;
	}

	public static final String NEXT_ROUND = "NEXT_ROUND";
	public static final String NEXT_CONFIG = "NEXT_CONFIG";
	public static final String SIMU_TERMINATE = "SIMU_TERMINATE";
	
	// specify delay value for each link
	public static int[] delays = {500,300,400,600};
	public static int differentialDelay = 100;

	// specify sending tempo for each link
	public static int[] tempos = {0,0,0,0};

	// time stamp for throughput in nano seconds
	public static long begin = 0;
	public static long end = 0;

	// flag to begin to set time stamp in BufferEmptier
	public static boolean shouldBegin = true;

}
