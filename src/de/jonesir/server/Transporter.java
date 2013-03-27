package de.jonesir.server;

/**
 * @author Yuesheng Zhong
 *
 */
public class Transporter implements Runnable{

    @Override
    public synchronized void run() {
	String temp = "";
	while(true){
	    try {
		temp = Server.TEMP_QUEUE.take();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    if(Server.SHARED_BUFFER.size()>=Server.bufferSize){
		log("Lost : " +Server.packetLost++);
	    }else{
		Server.SHARED_BUFFER.add(temp);
	    }
	}
    }

    private void log(String logString){
	System.out.println("Transporter - " + logString);
    }
}
