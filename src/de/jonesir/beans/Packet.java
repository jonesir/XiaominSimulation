package de.jonesir.beans;

import java.util.ArrayList;

import de.jonesir.algo.UniversalFunctions;

/**
 * 
 * @author Yuesheng Zhong
 *
 */
public class Packet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	private ArrayList<Block> blocks = null;
	private long ID;
	private static final int IDLength = 8;
	public static final int size = 1;
	public static final int lengthWithouIdentifier = Packet.size * Block.lengthWithoutIdentifier;
	public static final int length = Packet.lengthWithouIdentifier + Packet.IDLength;
	private static long IDGen = 0;
	private Generation generation = null;

	public Packet() {
		this.blocks = new ArrayList<Block>();
		this.ID = generatePackageID();
	}

	public void addBlock(Block block) {
		this.blocks.add(block);
	}

	public ArrayList<Block> getBlocks() {
		return this.blocks;
	}

	public void setGeneration(Generation generation) {
		this.generation = generation;
	}

	public Generation getGeneration() {
		return this.generation;
	}

	public long getPacketID() {
		return this.ID;
	}

	public String toBinaryString() {
		String binaryString = "";
		for (Block block : this.blocks) {
			binaryString += block.toBinaryStringWithoutIdentifier();
		}
		binaryString += UniversalFunctions.formatBinaryString(Long.toBinaryString(this.ID), Packet.IDLength);

		return binaryString;
	}

	public long generatePackageID() {
//		System.out.println("IDGen = " + IDGen);
		if (IDGen >= (2 << (IDLength - 1))) {
			IDGen = 0;
		}
		return IDGen++;
	}
}
