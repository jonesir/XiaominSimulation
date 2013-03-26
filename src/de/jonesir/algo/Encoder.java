package de.jonesir.algo;

import java.util.ArrayList;

import de.jonesir.beans.Block;
import de.jonesir.beans.Generation;
import de.jonesir.beans.Packet;
import de.jonesir.client.ClientLauncher;

public class Encoder {

    private static final int GALOIS_FIELD_SIZE = 8;
    private static final int GALOIS_FIELD_MATRIX_SIZE = 4;

    private static final GaloisField[][] galoisField = GaloisField.generateGaloisFieldMatrix(GALOIS_FIELD_MATRIX_SIZE, GALOIS_FIELD_SIZE);

    private static final int BYTE_LENGTH = 8;

    public static void main(String[] args) {
	System.out.println(new String[] { "8781", "asdf", "38klsdf-" }.toString());
    }

    /**
     * @param encodeString
     * @return
     */
    public static String[] encode(String[] encodeString) {

	int length = encodeString[0].length() / BYTE_LENGTH;
	String[][] matrix = new String[ClientLauncher.linkCount][length];

	for (int i = 0; i < length; i++) {
	    for (int j = 0; j < matrix.length; j++) {
		matrix[j][i] = encodeString[j].substring(i * BYTE_LENGTH, (i + 1) * BYTE_LENGTH);
	    }
	}

	// the output array of string
	String[] output = new String[ClientLauncher.linkCount];

	// row of galois field , or link number
	for (int rowOfGaloisField = 0; rowOfGaloisField < ClientLauncher.linkCount; rowOfGaloisField++) {
	    // length of bytes that needs to be encoded, or how many times of * and + need to be carried out
	    String ysOfOneLink = "";
	    for (int dataByteIndex = 0; dataByteIndex < length; dataByteIndex++) {
		// number of multiply
		String[] yValues = new String[ClientLauncher.linkCount];
		for (int galoisColumn = 0; galoisColumn < ClientLauncher.linkCount; galoisColumn++) {
		    yValues[galoisColumn] = computeYValue(galoisField[rowOfGaloisField][galoisColumn], matrix[galoisColumn][dataByteIndex]);
		}

		ysOfOneLink += computeYWithAddtion(yValues);
	    }
	    output[rowOfGaloisField] = ysOfOneLink.toString();
	}
	return output;
    }

    /**
     * @param yValues
     * @return
     */
    private static String computeYWithAddtion(String[] yValues) {
	String yValue = "";
	// iterate through each position of the y string
	for (int i = 0; i < yValues[0].length(); i++){
	    int temp = 0 ;
	    // check the same position of each y string and use xor to get the answer
	    for (String y : yValues) {
		temp += (y.charAt(i)=='0')?0:1;
	    }
	    yValue += temp%2;
	}
	return yValue;
    }

    /**
     * @param gValue
     * @param xValue
     * @return yValue of one column
     */
    private static String computeYValue(GaloisField gValue, String xValue) {
	String yValue = "";
	String[] gStrings = gValue.gf;
	for (String g : gStrings) {
	    int temp = 0;
	    for (int i = 0; i < g.length(); i++) {
		temp += (g.charAt(i) == xValue.charAt(i)) ? 0 : 1;
	    }
	    yValue += temp % 2;
	}
	return yValue;
    }

    private static void cout(String coutString) {
	System.out.println(coutString);
    }
}
