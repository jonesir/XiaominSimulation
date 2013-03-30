package de.jonesir.algo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import de.jonesir.beans.Packet;
import de.jonesir.client.TrafficGenerator;
import de.jonesir.server.Server;

/**
 * 
 * class used to log results of simulation
 * 
 * @author Yuesheng Zhong
 * 
 */
public class Logger {

	private static String result_encode = "results/A_packet_has_" + GlobalConfig.packet_size + "_blocks_" + GlobalConfig.mapping.get(GlobalConfig.tempoNs[0]) + "_" + GlobalConfig.MAX_SHARED_BUFFER_SIZE + "_GF8_Encode.txt";
	private static String result_multipath = "results/A_packet_has_" + GlobalConfig.packet_size + "_blocks_" + GlobalConfig.mapping.get(GlobalConfig.tempoNs[0]) + "_" + GlobalConfig.MAX_SHARED_BUFFER_SIZE + "_GF8_Multipath.txt";

	private static String lostRatioKey = "Packet Lost Ratio  : ";
	private static String throughputKey = "Throughput         : ";
	private static String pps = " Packets per Second";
	
	@SuppressWarnings("resource")
	public static void logResult() {
		String writerString = "";
		if (GlobalConfig.encoded) {
			writerString += "Encode \n";
		} else {
			writerString += "Multipath \n";
		}
		writerString += "Delay Setting in miliseconds : ";
		for(int i = 0 ; i < GlobalConfig.links_amount ; i++){
			writerString += GlobalConfig.delays[i];
			if(i!=GlobalConfig.links_amount-1){
				writerString+= ", ";
			}
		}
		writerString += "\n";
		writerString += "Sending Tempo Setting in nanoseconds : " + GlobalConfig.tempoNs[0] + "\n";
		writerString += "Total Packets Sent : " + GlobalConfig.packets_sent_in_one_simulation + "\n";
		writerString += "Buffer Size in Byte: " + GlobalConfig.MAX_SHARED_BUFFER_SIZE * (Packet.length / Encoder.BYTE_LENGTH) + "\n";
		writerString += "Packets Lost       : " + Server.NUMBER_OF_LOST_PACKETS + "\n";
		writerString += lostRatioKey + 100 * ((double) Server.NUMBER_OF_LOST_PACKETS) / GlobalConfig.packets_sent_in_one_simulation + "%\n";
		writerString += "Total Time Used(ns): " + (GlobalConfig.end - GlobalConfig.begin) + "\n";
		writerString += throughputKey + (double) (GlobalConfig.packets_sent_in_one_simulation - Server.NUMBER_OF_LOST_PACKETS) / ((GlobalConfig.end - GlobalConfig.begin) / (1000000000)) + pps + "\n";
		writerString += "-----------------------------\n\n";
		try {
			BufferedWriter writer;
			if (GlobalConfig.encoded)
				writer = new BufferedWriter(new FileWriter(result_encode, true));
			else
				writer = new BufferedWriter(new FileWriter(result_multipath, true));
			writer.append(writerString);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("resource")
	public static void logAverage(){
		log(" Calculating final average result out of log files ... ");
		
		String fileName = "average-result.txt";
		String resultsFolder = "results";
		
		String writerString = "";
		File results = new File(resultsFolder);
		File[] resultFiles = results.listFiles();
		
		ArrayList<String> lostRatios = new ArrayList<String>(); // save all lost ratio results without %
		ArrayList<String> throughputs = new ArrayList<String>(); // save all throughput results
		
		for(File resultFile : resultFiles){
			// write file name
			writerString += resultFile.getName() + "\t";
			
			// collect all interested values
			try {
				BufferedReader reader = new BufferedReader(new FileReader(resultFile));
				String readString;
				while((readString=reader.readLine())!=null){
					// add lost ratio value into lost ratio array
					if(readString.contains(lostRatioKey)){
						String[] temp = readString.split(":");
						String lostRatioRaw = temp[1].trim();
						lostRatios.add(lostRatioRaw.substring(0, lostRatioRaw.length()-1));
					}
					// add throughput value into throughput array
					if(readString.contains(throughputKey)){
						String[] temp = readString.split(":");
						String throughputRaw = temp[1];
						throughputs.add(throughputRaw.substring(0,throughputRaw.length()-pps.length()).trim());
					}
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// calculate average lost ratio
			double totalLostRatio = 0;
			for(String lostRatio : lostRatios){
				totalLostRatio += Double.parseDouble(lostRatio)/100; // in decimal number
			}
			
			writerString += "Average Lost Ratio : " + totalLostRatio/lostRatios.size() + "\t";
			
			// calculate average throughput
			double totalThroughput = 0;
			for(String throughput : throughputs){
				totalThroughput += Double.parseDouble(throughput);
			}
			
			writerString += "Average Throughput : " + totalThroughput/throughputs.size() + "\n";
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
			writer.write(writerString);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	private static void log(String logString){
		System.out.println(" Logger :::: " + logString);
	}
	
	public static void main(String[] args) {
		GlobalConfig.init();
		System.out.println(GlobalConfig.mapping.get(GlobalConfig.tempoNs[0]));
	}

}
