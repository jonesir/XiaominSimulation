package de.jonesir.algo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import de.jonesir.client.ClientLauncher;
import de.jonesir.server.Server;

/**
 * 
 * class used to log results of simulation
 * 
 * @author Yuesheng Zhong
 * 
 */
public class Logger {
    
    public static void logResult(){
	String writerString = "";
	writerString += "Total Packets Sent : " + ClientLauncher.blockCount + "\n";
	writerString += "Packets Lost       : " + Server.packetLost + "\n";
	writerString += "Packet Lost Ratio  : " + 100*((double)Server.packetLost)/ClientLauncher.blockCount + "%\n";
	writerString += "-----------------------------\n\n";
	    try {
		@SuppressWarnings("resource")
		BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt",true));
		writer.append(writerString);
		writer.flush();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	
    }
    
    public static void generateGaloisFieldMatrix(int matrixSize, int elementSize) {
	int gfSize = 2 << (elementSize - 1);
	String[] gfStrings = new String[gfSize];
	for (int i = 0; i < gfSize; i++) {
	    gfStrings[i] = JonesirGaloisField.formatBinaryString(Integer.toBinaryString(i), elementSize);
	}

	try {
	    @SuppressWarnings("resource")
	    BufferedWriter writer = new BufferedWriter(new FileWriter("galois_field.txt"));
	    String writerString = "";
	    ArrayList<Integer> usedIndex = new ArrayList<Integer>();
	    Random random = new Random();
	    
	    int index;
	    for (int i = 0; i < matrixSize * elementSize; i++) {
		for (int j = 0; j < matrixSize; j++) {
		    index = random.nextInt(gfSize);
		    while (usedIndex.contains(index)) {
			index = random.nextInt(gfSize);
		    }
		    usedIndex.add(index);
		    writerString += gfStrings[index] + "\t";
		    if(j==(matrixSize-1))
			writerString +="\n";
		}
		if((i+1)%8 ==0){
		    writerString+="\n";
		}

	    }
	   System.out.println(writerString);
	    writer.write(writerString);
	    writer.flush();
	    writer.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	Logger.generateGaloisFieldMatrix(4, 8);
    }

}
