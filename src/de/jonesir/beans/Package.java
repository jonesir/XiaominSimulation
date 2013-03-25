package de.jonesir.beans;

import java.util.ArrayList;

public class Package {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    for(int i = 0; i<40 ; i++)
	    System.out.println("" + generatePackageID());
	}

	private ArrayList<Block> blocks = null;
	private long ID;
	private static final int IDLength = 8;
	public static final int size = 1;
	public static int length = Package.size * Block.length + IDLength;
	private static long IDGen = 0;
	private static final int linkCount = 4;

	public Package() {
		this.blocks = new ArrayList<Block>();
		this.ID = generatePackageID();
	}

	public void addBlock(Block block) {
		this.blocks.add(block);
	}

	public ArrayList<Block> getBlocks() {
		return this.blocks;
	}

	public long getID() {
		return this.ID;
	}

	public String toBinaryString() {
		String binaryString = "";
		// concatenate blocks
		for (Block block : this.blocks) {
			binaryString += block.toBinaryString().substring(0,Block.length);
		}
		
		// appending package id
		binaryString += formatBinaryString(Long.toBinaryString(this.ID),
				Package.IDLength, "package.toBinaryString");
		
		return binaryString;
	}

	public static String formatBinaryString(String binaryString, int length, String id) {
		if (binaryString.length() == length)
			return binaryString;
		else if (binaryString.length() < length) {
			int difference = length - binaryString.length();
			for (int i = 0; i < difference; i++) {
				binaryString = "0" + binaryString;
			}
		} else if (binaryString.length() > length) {
			System.out.println("Wrong binary format, it is too long! ==> ID: " + id + "..." + binaryString + " has the length " + binaryString.length() + " allowed length : " + length);
			System.exit(0);
		}

		return binaryString;
	}

	private static long generatePackageID() {
	    if((IDGen/linkCount)>=(2<<(IDLength-1))){
		IDGen=0;
	    }
	    return IDGen++/linkCount;
	}
}
