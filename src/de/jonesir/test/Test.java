package de.jonesir.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String cp = "";
		String[] commandWithPipe = new String[] {"/bin/sh","-c","find /usr/local/java/jdk1.7.0_13/jre/lib/ -name '*.jar' | tr '\\n' ':'"};
		try {
			Process pr = Runtime.getRuntime().exec(commandWithPipe);
			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			cp = input.readLine();
			cp = cp.substring(0,cp.length()-1);
			System.out.println(cp);
			input.close();
			pr.getInputStream().close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
//		for(int i = 0 ; i < 3 ; i++){
//			try {
//				Process p1  = Runtime.getRuntime().exec("java -cp \"" + cp + "\" /home/jonesir/workspace/eclipse/Simu/bin/de/jonesir/server/Server");
//				Thread.sleep(500);
//				Process p2 = Runtime.getRuntime().exec("java -cp \"" + cp + "\" /home/jonesir/workspace/eclipse/Simu/bin/de/jonesir/client/ClientLauncher");
//				System.out.println("exec");
//				Thread.sleep(20000);
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	public static void log(String logString){
		System.out.println(logString);
	}
} 
