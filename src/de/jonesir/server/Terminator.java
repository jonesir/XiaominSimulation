package de.jonesir.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import de.jonesir.algo.GlobalConfig;
import de.jonesir.algo.Logger;
import de.jonesir.client.ClientLauncher;

public class Terminator implements Runnable {

    private ArrayList<Thread> threads;

    public Terminator(ArrayList<Thread> threads) {
	this.threads = threads;
    }

    @Override
    public void run() {
	System.out.println("Terminator is running ... ");
	try {
	    ServerSocket ss = new ServerSocket(ClientLauncher.terminatorPort);
	    Socket s = ss.accept();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    String readString;
	    while (true) {
		readString = reader.readLine();
		if (readString.equals("terminate")) {
		    System.out.println("Simulation " + GlobalConfig.paramCombinationCounter + " terminates");

		    // set the time stamp of ending processing the SHARED_BUFFER
		    GlobalConfig.end = System.nanoTime();
		    // log the result
		    Logger.logResult();

		    // refresh parameters and reset variables
		    Server.refreshParams();
		    Server.resetParams();
		}
		if (readString.equals("completeTerminate")) {
		    for (Thread t : threads) {
			t.stop();
		    }
		    Thread.sleep(3000);
		    System.exit(0);
		}

	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

}
