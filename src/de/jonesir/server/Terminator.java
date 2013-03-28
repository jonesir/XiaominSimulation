package de.jonesir.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import de.jonesir.algo.Logger;
import de.jonesir.client.ClientLauncher;

public class Terminator implements Runnable {

    @Override
    public void run() {
	System.out.println("Terminator is running ... ");
	try {
	    ServerSocket ss = new ServerSocket(ClientLauncher.terminatorPort);
	    Socket s = ss.accept();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    while (true)
		if (reader.readLine().equals("terminate")) {
//		    System.out.println("Simulation terminates!!!");
//		    Logger.logResult();
		}

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
