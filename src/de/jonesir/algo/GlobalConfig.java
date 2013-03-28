package de.jonesir.algo;

public class GlobalConfig {

	public static final int port1 = 4189, port2 = 4190, port3 = 4191, port4 = 4192, terminatorPort = 4193;

	// shared buffer size
	public static final int MAX_SHARED_BUFFER_SIZE = 164;

	// decide whether to be tranfered data is encoded or not
	public static final boolean dataIsEncoded = true;

	// number of total blocks need
	public static final int blockCount = 1000;

	// specify delay value for each link
	public static int delay1 = 500;
	public static int delay2 = 300;
	public static int delay3 = 400;
	public static int delay4 = 600;

	// specify sending tempo for each link
	public static int tempo1 = 100;
	public static int tempo2 = 100;
	public static int tempo3 = 100;
	public static int tempo4 = 100;

}
