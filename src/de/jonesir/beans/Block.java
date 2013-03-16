package de.jonesir.beans;

public class Block {

	// basic general information of blocks
	public static final int length = 66;
	public static final int IDLength = 16;
	public static final int contentLength = 50;
	private static long IDGen = 0;
	public static int routeNr = 4;
	// ----------------------------------------------
	private long blockContent;
	private long ID;
	private long routeID;
	private Package belongingPackage;

	public static void main(String[] args) {

	}

	public Block(long blockContent, long blockID, Package belongingPackage) {
		this.blockContent = blockContent;
		this.ID = generateBlockID();
		this.routeID = this.ID % Block.routeNr;
		this.belongingPackage = belongingPackage;
	}

	public long getBlockID() {
		return this.ID;
	}

	public Package getBelongingPackage() {
		return this.belongingPackage;
	}

	public long getRouteID() {
		return this.routeID;
	}

	public String toBinaryString() {
		return Package.formatBinaryString(Long.toBinaryString(this.blockContent), contentLength) + Package.formatBinaryString(Long.toBinaryString(this.ID), IDLength);
	}

	public long generateBlockID() {
		return IDGen++;
	}
}
