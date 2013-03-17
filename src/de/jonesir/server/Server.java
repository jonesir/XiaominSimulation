package de.jonesir.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args){
		try {
			ServerSocket ss = new ServerSocket(4189);
			Socket s = ss.accept();
			
			ServerProcesser processer = new ServerProcesser();
			processer.process(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
