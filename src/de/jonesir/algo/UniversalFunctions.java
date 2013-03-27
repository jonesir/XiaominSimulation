package de.jonesir.algo;

public class UniversalFunctions {
	
	public static String formatBinaryString(String binaryString, int length){
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
}
