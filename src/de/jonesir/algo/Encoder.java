package de.jonesir.algo;

import java.util.ArrayList;

import de.jonesir.algo.standalone.GaloisField;
import de.jonesir.algo.standalone.GaloisPolynomial;
import de.jonesir.beans.Block;
import de.jonesir.beans.Generation;
import de.jonesir.beans.Packet;

public class Encoder {

	private static final int byteLength = 8;
	public static void main(String[] args) {
		GaloisField gf = new GaloisField(256, 2);
		GaloisPolynomial gp = new GaloisPolynomial(new int[]{0,1}, 'x', gf);
	}
	
	public static void encode(Generation generation){
		GaloisField gf = new GaloisField(256, 2);
		GaloisPolynomial gp = new GaloisPolynomial(new int[]{0,1}, 'x', gf);
		// iterate through all packets in generation
		ArrayList<Packet> packets = generation.getPackets();
		// prepare 2-dim array to store generation information used for encoding
		int rows = packets.size(); 			// rows equal to packet number in a generation
		int columns = packets.get(0).length / byteLength; 	// columns equal to packet length in byte
		String[][] generationMatrix = new String[rows][columns];
		for(int i = 0 ; i < packets.size() ; i++){
			Packet packet = packets.get(i);
//			ArrayList<Block> blocks = packet.getBlocks();
//			// iterate through all blocks in packets
//			for(int j = 0 ; j < blocks.size() ; j++){
//				Block block = blocks.get(j);
//			}
			
			String packetString = packet.toBinaryString();
			int column = 0;
			for(int j = 0 ; j < packetString.length() ; j+=byteLength){
				generationMatrix[i][column] = packetString.substring(j, j+byteLength);
				column++;
			}
		}
		
		
	}
	
	private static void cout(String coutString){
		System.out.println(coutString);
	}
}
