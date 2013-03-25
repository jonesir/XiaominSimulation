package de.jonesir.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerProcesser extends Thread {

    int port;
    String[] buffer = new String[64]; // 64 byte length of buffer

    public ServerProcesser(int port) {
	this.port = port;
    }

    @Override
    public void run() {
	try {
	    ServerSocket ss = new ServerSocket(port);
	    System.out.println("Listening on port : " + port);
	    while (true) {
		process(ss.accept());
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * @param s
     */
    private void process(Socket s) {
	try {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    String incomingString;
	    while ((incomingString = reader.readLine()) != null) {
//		if (incomingString.length() % 8 == 0)
		    switch (port) {
		    case Server.port1:
			System.out.println(incomingString);
			break;
		    case Server.port2:
			System.out.println("\t" + incomingString);
			break;
		    case Server.port3:
			System.out.println("\t\t" + incomingString);
			break;
		    case Server.port4:
			System.out.println("\t\t\t" + incomingString);
			break;
		    default:
			break;
		    }
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
