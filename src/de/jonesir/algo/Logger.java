package de.jonesir.algo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import de.jonesir.client.ClientLauncher;
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
		if (ClientLauncher.dataIsEncoded) {
			writerString += "Encoding : YES \n";
		} else {
			writerString += "Encoding : NO \n";
		}
		writerString += "Delay Setting in miliseconds : " + TrafficGenerator.delay1 + ", " + TrafficGenerator.delay2 + ", " + TrafficGenerator.delay3 + ", " + TrafficGenerator.delay4 + "\n";
		writerString += "Sending Tempo Setting in miliseconds : " + TrafficGenerator.tempo1 + ", " + TrafficGenerator.tempo2 + ", " + TrafficGenerator.tempo3 + ", " + TrafficGenerator.tempo4 + "\n";
		writerString += "Total Packets Sent : " + ClientLauncher.blockCount + "\n";
		writerString += "Packets Lost       : " + Server.NUMBER_OF_LOST_PACKETS + "\n";
		writerString += "Packet Lost Ratio  : " + 100 * ((double) Server.NUMBER_OF_LOST_PACKETS) / ClientLauncher.blockCount + "%\n";
		writerString += "-----------------------------\n\n";
		try {
			BufferedWriter writer;
			if (ClientLauncher.dataIsEncoded)
				writer = new BufferedWriter(new FileWriter(result_encode, true));
			else
				writer = new BufferedWriter(new FileWriter(result_multipath, true));
			writer.append(writerString);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
