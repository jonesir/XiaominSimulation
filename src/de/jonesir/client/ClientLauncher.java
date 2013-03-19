package de.jonesir.client;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import de.jonesir.beans.Block;
import de.jonesir.beans.Packet;

public class ClientLauncher {
	
	private static final long blockCount = 10000;
	
	public static void main(String[] args){
		
		ArrayList<Packet> packageList = new ArrayList<Packet>();
		
		Packet p = null;
		Block b = null;
		
		for(int i = 0 ; i < blockCount ; i++){
			if(i % Packet.size == 0){
				if(p!=null)
					packageList.add(p);
				p = new Packet();
			}
			b = new Block(123, p);
			p.addBlock(b);
		}
		
		send(packageList);
	}
	
	public static void encoding(ArrayList<Packet> packageList){
		
	}
	
	private static void send(ArrayList<Packet> packageList){
		
		try {
			Socket s = new Socket("127.0.0.1", 4189);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
