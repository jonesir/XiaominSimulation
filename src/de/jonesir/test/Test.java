package de.jonesir.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

    	public static final int[] AMOUNT_OF_PACKETS = {200000};
    	public static final int[] MAX_BUFFER_SIZE = {1000,2000,3000,4000,5000};
    	public static final int[] TEMPO = {1000,2500,5000,10000};
    	public static final int[] ENCODE = {0,1};
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    for(int i = 0 ; i < AMOUNT_OF_PACKETS.length ; i++){
		for(int j = 0 ; j < MAX_BUFFER_SIZE.length ; j++){
		    for(int k = 0 ; k < TEMPO.length ; k++){
			for(int m = 0 ; m < ENCODE.length ; m++){
			    try {
				String[] command1 = {"/bin/sh","-c","java -jar /home/jonesir/Download/Simulation/Server.jar " + AMOUNT_OF_PACKETS[i] + " " + MAX_BUFFER_SIZE[j] + " " + TEMPO[k] + " " + ENCODE[m]};
				Runtime.getRuntime().exec(command1);
				Thread.sleep(1000);
				String[] command2 = {"/bin/sh","-c","java -jar /home/jonesir/Download/Simulation/ClientLauncher.jar " + AMOUNT_OF_PACKETS[i] + " " + MAX_BUFFER_SIZE[j] + " " + TEMPO[k] + " " + ENCODE[m]};
				Runtime.getRuntime().exec(command2);
				Thread.sleep(25000);
				System.out.println("Round Finishes");
			    } catch (IOException e) {
				e.printStackTrace();
			    } catch (InterruptedException e) {
				e.printStackTrace();
			    }
			}
		    }
		}
	    }
	    System.out.println("Simulation Terminates!!!");
//	    try {
//		String[] commands = {"/bin/sh","-c","java -jar /home/jonesir/Download/Simulation/Server.jar " + AMOUNT_OF_PACKETS[0] + " " + MAX_BUFFER_SIZE[0] + " " + TEMPO[1] + " " + ENCODE[1]};
//		Process p = Runtime.getRuntime().exec(commands);
//		Thread.sleep(500);
//		String[] command2 = {"/bin/sh","-c","java -jar /home/jonesir/Download/Simulation/ClientLauncher.jar " + AMOUNT_OF_PACKETS[0] + " " + MAX_BUFFER_SIZE[0] + " " + TEMPO[1] + " " + ENCODE[1]};
//		Runtime.getRuntime().exec(command2);
//		
//		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//		String readString;
//		while((readString=reader.readLine())!=null){
//		    System.out.println(readString);
//		}
//	    } catch (IOException e) {
//		e.printStackTrace();
//	    } catch (InterruptedException e) {
//		e.printStackTrace();
//	    }
	    
//		String cp = "";
//		String[] commandWithPipe = new String[] {"/bin/sh","-c","find /usr/local/java/jdk1.7.0_13/jre/lib/ -name '*.jar' | tr '\\n' ':'"};
//		try {
//			Process pr = Runtime.getRuntime().exec(commandWithPipe);
//			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
//			cp = input.readLine();
//			cp = cp.substring(0,cp.length()-1);
//			System.out.println(cp);
//			input.close();
//			pr.getInputStream().close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
	    
	    
		
		
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
