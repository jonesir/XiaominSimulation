package de.jonesir.algo.standalone;
import java.io.*;



class ReadThread extends Thread {

	MyApplication app;
	

//--------------------------------------------------------------------
	public ReadThread(MyApplication app) {

		this.app = app;

	}


//--------------------------------------------------------------------
	public void run() {
		
		DataInputStream in = new DataInputStream(System.in);
		String str;
		byte[] b;

		try{
			while(true) {
//				synchronized(System.out) {
					System.out.println("ready...");
					str = in.readLine();
					app.readData(str);
					System.out.println("sending...");
					
					try{
						sleep(Long.MAX_VALUE);
					}
					catch(InterruptedException e) {
					}
//				}
			}

		}
		catch(IOException e) {
			System.err.println(e);
		}

	}


}
