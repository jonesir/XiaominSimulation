package de.jonesir.beans;

import java.util.ArrayList;

public class Package {

    /**
     * @param args
     */
    public static void main(String[] args) {
    }

    private ArrayList<Block> blocks = null;
    private long ID;
    private static final int IDLength = 16;
    public static final int size = 4;
    public static int length = Package.size * Block.length + IDLength;
    private static long IDGen = 0;
    
    public Package(){
	this.blocks = new ArrayList<Block>();
	this.ID = generatePackageID();
    }
    
    public void addBlock(Block block){
	this.blocks.add(block);
    }
    
    public ArrayList<Block> getBlocks(){
	return this.blocks;
    }
    
    public long getID(){
	return this.ID;
    }
    
    public String toBinaryString(){
	String binaryString = "";
	for(Block block : this.blocks){
	    binaryString+=block.toBinaryString();
	}
	String idBinary = Long.toBinaryString(this.ID);
	if(idBinary.length()==IDLength)
	    binaryString+=idBinary;
	
	binaryString += formatBinaryString(Long.toBinaryString(this.ID), this.IDLength);
	return binaryString;
    }
    
    public static String formatBinaryString(String binaryString, int length){
	if(binaryString.length() == length)
	    return binaryString;
	else if(binaryString.length()<length){
	    int difference = length - binaryString.length();
	    for(int i = 0 ; i< difference ; i++){
		binaryString = "0" + binaryString;
	    }
	}else if (binaryString.length() > length){
	    System.out.println("Wrong binary format, it is too long!");
	    System.exit(0);
	}
	
	return binaryString;
    }
    
    private long generatePackageID(){
	return IDGen++;
    }
}
