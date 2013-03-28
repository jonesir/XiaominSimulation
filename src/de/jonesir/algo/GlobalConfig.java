package de.jonesir.algo;

import java.util.ArrayList;

public class GlobalConfig {

    public static int[] AMOUNT_OF_PACKETS = { 200000 };
    public static int[] MAX_SHARED_BUFFER_SIZES = { 1000, 2000, 3000, 4000, 5000 };
    public static int[] TEMPOS = { 1000, 5000, 2500, 10000 };
    public static int[] ENCODE = { 0, 1 };

    public static int paramCombinationCounter = 0;

    // generate combination of parameters with each set for each round of simulation
    // and store all the combination in an array list
    public static ArrayList<int[]> params = new ArrayList<int[]>();

    public static void generateParams() {
	for (int i = 0; i < AMOUNT_OF_PACKETS.length; i++) {// get packets number of a simulation
	    for (int j = 0; j < MAX_SHARED_BUFFER_SIZES.length; j++) {// get buffer size
		for (int k = 0; k < TEMPOS.length; k++) { // get sending tempo
		    for (int m = 0; m < ENCODE.length; m++) { // get encode flag
			int[] temp = { AMOUNT_OF_PACKETS[i], MAX_SHARED_BUFFER_SIZES[j], TEMPOS[k], ENCODE[m] };
			params.add(temp);
		    }
		}
	    }
	}
    }

    public static void main(String[] args) {
	generateParams();
	for (int[] param : params)
	    System.out.println(param[0]+", "+ param[1]+", "+param[2]+", "+param[3]);
    }

    public static void refreshParams(int numberOfPackets, int maxBufferSize, int tempo, int encode) {
	GlobalConfig.amount_of_packets = numberOfPackets;
	GlobalConfig.MAX_SHARED_BUFFER_SIZE = maxBufferSize;
	GlobalConfig.tempoN1 = tempo;
	GlobalConfig.tempoN2 = tempo;
	GlobalConfig.tempoN3 = tempo;
	GlobalConfig.tempoN4 = tempo;
	GlobalConfig.encoded = (encode == 0) ? false : true;
    }

    public static final int port1 = 4189, port2 = 4190, port3 = 4191, port4 = 4192, terminatorPort = 4193;

    // number of total blocks need
    public static int amount_of_packets = 200000;

    // shared buffer size
    public static int MAX_SHARED_BUFFER_SIZE = 5000;// 1000 - 5000 packets

    // specify sending tempo in nanosecond
    public static int tempoN1 = 10000;// 10000,5000,2500,1000
    public static int tempoN2 = 10000;
    public static int tempoN3 = 10000;
    public static int tempoN4 = 10000;

    // decide whether to be tranfered data is encoded or not
    public static boolean encoded = true;

    // specify delay value for each link
    public static int delay1 = 500;
    public static int delay2 = 300;
    public static int delay3 = 400;
    public static int delay4 = 600;

    // specify sending tempo for each link
    public static int tempo1 = 0;
    public static int tempo2 = 0;
    public static int tempo3 = 0;
    public static int tempo4 = 0;

    // time stamp for throughput in nano seconds
    public static long begin = 0;
    public static long end = 0;

    // flag to begin to set time stamp in BufferEmptier
    public static boolean shouldBegin = true;

}
