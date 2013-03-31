package de.jonesir.beans;

import java.util.ArrayList;

public class Generation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	private ArrayList<Packet> packets = null;
	private long ID;
	private static final int IDLength = 16;
	public static final int size = 4;
	public static int length = Packet.getPacketSize() * Packet.length;
	private static long IDGen = 0;

	public Generation() {
		this.packets = new ArrayList<Packet>();
		this.ID = generateGenerationID();
	}

	public void addPacket(Packet packet) {
		this.packets.add(packet);
	}

	public ArrayList<Packet> getPackets() {
		return this.packets;
	}

	public long getID() {
		return this.ID;
	}

	public String toBinaryString() {
		String binaryString = "";
		for (Packet packet : this.packets) {
			binaryString += packet.toBinaryString();
		}
		binaryString += formatBinaryString(Long.toBinaryString(this.ID),
				IDLength);
		
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

	private long generateGenerationID() {
		return IDGen++;
	}
}
