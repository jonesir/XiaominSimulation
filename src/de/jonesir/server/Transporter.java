package de.jonesir.server;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.jonesir.algo.Encoder;
import de.jonesir.client.ClientLauncher;

/**
 * @author Yuesheng Zhong
 *
 */
public class Transporter implements Runnable{
    public static boolean stop = false;
    @Override
    public synchronized void run() {
	log("is running...");
	String temp = "";
	
	
//	while(true){
//	    try {
//		temp = Server.TEMP_QUEUE.take();
//	    } catch (InterruptedException e) {
//		e.printStackTrace();
//	    }
//	    log("SHARED_BUFFER_SIZE : " + Server.SHARED_BUFFER.size());
//	    if(Server.SHARED_BUFFER.size()>=Server.MAX_SHARED_BUFFER_SIZE){
////		log("Lost : " +Server.packetLost++);
//	    }else{
////		log("id before added to shared buffer:" + Integer.parseInt(temp.substring(64), 2));
//		Server.SHARED_BUFFER.add(temp);
//	    }
//	}
//    }
    }

    private void log(String logString){
	System.out.println("Transporter - " + logString);
    }
}
//
//







