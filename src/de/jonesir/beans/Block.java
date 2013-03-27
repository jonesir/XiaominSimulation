package de.jonesir.beans;

import de.jonesir.algo.UniversalFunctions;
import de.jonesir.client.ClientLauncher;

/**
 * @author Yuesheng Zhong
 *
 */
public class Block {

	// basic general information of blocks
	public static final int lengthWithoutIdentifier = 64;
	public static final int IDLength = 8;
	public static final int length = Block.lengthWithoutIdentifier + Block.IDLength;
	private static long IDGen = 0;
	public static int routeNr = 4;
	// ----------------------------------------------
	private long blockContent;
	private long ID;
	private long routeID;
	private Packet belongingPackage;

	public static void main(String[] args) {
	    
	    System.out.println("" + ClientLauncher.random.nextInt(2<<7));
	}

	public Block(long blockContent, Packet belongingPackage) {
		this.blockContent = blockContent;
		this.ID = generateBlockID();
		this.routeID = this.ID % Block.routeNr;
		this.belongingPackage = belongingPackage;
	}

	public long getBlockID() {
		return this.ID;
	}

	public Packet getBelongingPackage() {
		return this.belongingPackage;
	}

	public long getRouteID() {
		return this.routeID;
	}

	public String toBinaryString() {
		return UniversalFunctions.formatBinaryString(Long.toBinaryString(this.blockContent), lengthWithoutIdentifier) + UniversalFunctions.formatBinaryString(Long.toBinaryString(ID), IDLength);
	}
	
	public String toBinaryStringWithoutIdentifier(){
		return UniversalFunctions.formatBinaryString(Long.toBinaryString(this.blockContent), lengthWithoutIdentifier);
	}

	public long generateBlockID() {
	    if(IDGen>=(2<<(IDLength-1))){
		IDGen=0;
	    }
	    return IDGen++;
	}
}
