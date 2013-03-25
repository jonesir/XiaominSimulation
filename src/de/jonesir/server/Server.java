package de.jonesir.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int port1 = 4189, port2 = 4190, port3 = 4191, port4 = 4192;
    public static void main(String[] args) {

	// for thread with generate server socket listening on different ports
	new ServerProcesser(port1).start();
	new ServerProcesser(port2).start();
	new ServerProcesser(port3).start();
	new ServerProcesser(port4).start();
    }
}
