package de.jonesir.algo.standalone;
import java.lang.*;


class ManageSendThread extends Thread {

//	NetCodAdapter adapter;
	
	//defined by the constructor:
	Adapter adapter;
	long rate;


//--------------------------------------------------------------------
//	public ManageSendThread(NetCodAdapter adapter) {
	public ManageSendThread(Adapter adapter, long rate) {
		this.adapter = adapter;
		this.rate = rate;
	}


//--------------------------------------------------------------------
	public void run() {

		try{
			while(true) {
//				sleep(50);
//				System.out.println("ManageSendThread: run: " + rate);
				sleep(rate);
				adapter.send();
			}
		}
		catch(InterruptedException e) {
			System.err.println("ManageSendThread: run: " + e);
		}
	}

}
