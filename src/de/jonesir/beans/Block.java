package de.jonesir.beans;

public class Block {

	// basic general information of blocks
	public static final int length = 66;
	public static final int IDLength = 6;
	private static long IDGen = 0;
	public static int routeNr = 4;
	// ----------------------------------------------
	private long blockContent;
	private long ID;
	private long routeID;
	private Package belongingPackage;

	public static void main(String[] args) {
	    
	    System.out.println("" + (2<<IDLength));
	}

	public Block(long blockContent, Package belongingPackage) {
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
		return Package.formatBinaryString(Long.toBinaryString(this.blockContent), length, "content") + Package.formatBinaryString(Long.toBinaryString(ID), IDLength, "id");
	}

	public long generateBlockID() {
	    if(IDGen>=(2<<(IDLength-1))){
		IDGen=0;
	    }
	    return IDGen++;
	}
}
