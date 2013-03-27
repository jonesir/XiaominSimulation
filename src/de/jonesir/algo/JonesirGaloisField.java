package de.jonesir.algo;

import java.util.ArrayList;
import java.util.Random;

public class JonesirGaloisField {
	public String[] gf;

	public JonesirGaloisField(String[] gf) {
		this.gf = gf;
	}

	public static int[][] generateGaloisFieldMatrix(int matrixSize, int elementSize) {
		int gfSize = 2 << (elementSize - 1);
		String[] gfStrings = new String[gfSize];
		for (int i = 0; i < gfSize; i++) {
			gfStrings[i] = formatBinaryString(Integer.toBinaryString(i), elementSize);
		}

		JonesirGaloisField[][] matrix = new JonesirGaloisField[matrixSize][matrixSize];
		ArrayList<Integer> usedIndex = new ArrayList<Integer>();
		Random random = new Random();
		int index;
		for (int i = 0; i < matrixSize; i++) {
			for (int j = 0; j < matrixSize; j++) {
				String[] tempGF = new String[elementSize];
				for (int k = 0; k < elementSize; k++) {
					index = random.nextInt(gfSize);
					while (usedIndex.contains(index)) {
						index = random.nextInt(gfSize);
					}
					tempGF[k] = gfStrings[index];
					usedIndex.add(index);
				}

				matrix[i][j] = new JonesirGaloisField(tempGF);
			}
		}

		return null;
	}

	public static void main(String[] args) {
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

	public String toString() {
		String s = "";
		for (String ss : this.gf) {
			s += ss + "\n";
		}
		return s;
	}
}
