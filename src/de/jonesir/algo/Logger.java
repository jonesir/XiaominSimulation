package de.jonesir.algo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

	public static String result_encode = "result_encode.txt";
	public static String result_multipath = "result_multipath.txt";
	@SuppressWarnings("resource")
	public static void logResult() {
		String writerString = "";
		if (GlobalConfig.encoded) {
			writerString += "Encoding : YES \n";
		} else {
			writerString += "Encoding : NO \n";
		}
		writerString += "Delay Setting in miliseconds : " + GlobalConfig.delay1 + ", " + GlobalConfig.delay2 + ", " + GlobalConfig.delay3 + ", " + GlobalConfig.delay4 + "\n";
		writerString += "Sending Tempo Setting in nanoseconds : " + GlobalConfig.tempoN1 + "\n";
		writerString += "Total Packets Sent : " + GlobalConfig.amount_of_packets + "\n";
		writerString += "Buffer Size in Byte: " + GlobalConfig.MAX_SHARED_BUFFER_SIZE*(Packet.length/Encoder.BYTE_LENGTH) + "\n";
		writerString += "Packets Lost       : " + Server.NUMBER_OF_LOST_PACKETS + "\n";
		writerString += "Packet Lost Ratio  : " + 100 * ((double) Server.NUMBER_OF_LOST_PACKETS) / GlobalConfig.amount_of_packets + "%\n";
		writerString += "Total Time Used(ns): " + (GlobalConfig.end-GlobalConfig.begin) + "\n";
		writerString += "Throughput         : " + (double)(GlobalConfig.amount_of_packets-Server.NUMBER_OF_LOST_PACKETS)/((GlobalConfig.end-GlobalConfig.begin)/(1000000000)) + " Packets per Second\n";
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

	public static void main(String[] args){
	    System.out.println(1.0e9);
	}
	// public static void generateGaloisFieldMatrix(int matrixSize, int elementSize) {
	// int gfSize = 2 << (elementSize - 1);
	// String[] gfStrings = new String[gfSize];
	// for (int i = 0; i < gfSize; i++) {
	// gfStrings[i] = JonesirGaloisField.formatBinaryString(Integer.toBinaryString(i), elementSize);
	// }
	//
	// try {
	// @SuppressWarnings("resource")
	// BufferedWriter writer = new BufferedWriter(new FileWriter("galois_field.txt"));
	// String writerString = "";
	// ArrayList<Integer> usedIndex = new ArrayList<Integer>();
	// Random random = new Random();
	//
	// int index;
	// for (int i = 0; i < matrixSize * elementSize; i++) {
	// for (int j = 0; j < matrixSize; j++) {
	// index = random.nextInt(gfSize);
	// while (usedIndex.contains(index)) {
	// index = random.nextInt(gfSize);
	// }
	// usedIndex.add(index);
	// writerString += gfStrings[index] + "\t";
	// if(j==(matrixSize-1))
	// writerString +="\n";
	// }
	// if((i+1)%8 ==0){
	// writerString+="\n";
	// }
	//
	// }
	// System.out.println(writerString);
	// writer.write(writerString);
	// writer.flush();
	// writer.close();
	//
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public static void main(String[] args) {
	// Logger.generateGaloisFieldMatrix(4, 8);
	// }

}
