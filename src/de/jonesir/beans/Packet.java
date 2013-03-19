package de.jonesir.beans;

import java.util.ArrayList;

public class Packet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	private ArrayList<Block> blocks = null;
	private long ID;
	private static final int IDLength = 16;
	public static final int size = 4;
	public static int length = Packet.size * Block.length + IDLength;
	private static long IDGen = 0;
	private Generation generation = null;

	public Packet() {
		this.blocks = new ArrayList<Block>();
	}
	
	public void addBlock(Block block) {
		this.blocks.add(block);
	}

	public ArrayList<Block> getBlocks() {
		return this.blocks;
	}

	public void setGeneration(Generation generation){
		this.generation = generation;
	}
	
	public Generation getGeneration(){
		return this.generation;
	}
	
	public long getID() {
		return this.ID;
	}

	public String toBinaryString() {
		String binaryString = "";
		for (Block block : this.blocks) {
			binaryString += block.toBinaryString();
		}
		binaryString += formatBinaryString(Long.toBinaryString(this.ID),
				this.IDLength);
		
		return binaryString;
	}

	public static String formatBinaryString(String binaryString, int length) {
		if (binaryString.length() == length)
			return binaryString;
		else if (binaryString.length() < length) {
			int difference = length - binaryString.length();
			for (int i = 0; i < difference; i++) {
				binaryString = "0" + binaryString;
			}
		} else if (binaryString.length() > length) {
			System.out.println("Wrong binary format, it is too long!");
			System.exit(0);
		}

		return binaryString;
	}

	public long generatePackageID() {
		return IDGen++;
	}
}
