package de.jonesir.algo;

import java.util.LinkedList;

import de.jonesir.client.ClientLauncher;

/**
 * @author Yuesheng Zhong
 *
 */
public class Encoder {

	private static final int[][] GALOIS_FIELD = {{198,79,203,136}, {125,130,165,90},{112,131,97,240},{114,209,207,224}};
	private static final int[][] INVERSE_GALOIS_FIELD = {{213,164,107,249},{146,34,216,159},{139,80,66,139},{112,95,229,56}};

	public static final int BYTE_LENGTH = 8;

	public static void main(String[] args) {
//		String resultGF = "";
//		String resultIGF = "";
//		for(int i = 0 ; i < 4 ; i++){
//			for(int j = 0 ; j < 4 ; j++){
//				resultGF += GALOIS_FIELD[i][j] + "\t";
//				resultIGF += INVERSE_GALOIS_FIELD[i][j] + "\t";
//				if(j==3){
//					resultGF +="\n";
//					resultIGF += "\n";
//				}
//			}
//		}
//		System.out.println("Result of GF : \n" + resultGF );
//		System.out.println("Result of Inverse GF : \n" + resultIGF );
	  System.out.println(""+13/4);
	  System.out.println(""+12/4);
	  System.out.println(""+14/4);
	  System.out.println(""+15/4);
	}

	/**
	 * @param encodeString
	 * @return
	 */
	public static String[] encode(String[] encodeString) {

		int length = encodeString[0].length() / BYTE_LENGTH;
		// 2 dimensional int array to store decimal version of the binary value
		int[][] matrix = new int[ClientLauncher.linkCount][length];

		// cut the incoming to be encoded binary value into Bytes and transfer each Byte to decimal value in GF(2^8)
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[j][i] = Integer.parseInt(encodeString[j].substring(i * BYTE_LENGTH, (i + 1) * BYTE_LENGTH));
			}
		}

		// the output array of string in form of binary value that can directly be sent out to each link
		String[] output = new String[ClientLauncher.linkCount];

		// row of galois field , or link number, in this case 4
		for (int rowOfGaloisField = 0; rowOfGaloisField < ClientLauncher.linkCount; rowOfGaloisField++) {
			// length of bytes that needs to be encoded, or how many times of * and + need to be carried out
			String ysOfOneLink = "";
			for (int dataByteIndex = 0; dataByteIndex < length; dataByteIndex++) {
				// number of multiply, in this case 4
				int byteEncodeResult = 0;
				for (int galoisColumn = 0; galoisColumn < ClientLauncher.linkCount; galoisColumn++) {
					byteEncodeResult = GALOIS_FIELD[rowOfGaloisField][galoisColumn] * matrix[galoisColumn][dataByteIndex];
				}
				// after multiply of each row and column, store the result as binary string
				ysOfOneLink += UniversalFunctions.formatBinaryString(Integer.toBinaryString(byteEncodeResult%(2<<7)), BYTE_LENGTH);
			}
			// store complete binary string of encoded value for each link
			output[rowOfGaloisField] = ysOfOneLink;
		}

		return output;
	}
	
	/**
	 * @param encodeString
	 * @return
	 */
	public static String[] encode_apache(String[] encodeString) {

		int length = encodeString[0].length() / BYTE_LENGTH;
		// 2 dimensional int array to store decimal version of the binary value
		int[][] matrix = new int[ClientLauncher.linkCount][length];
		
		// Save only identifier of each packet so that only content will be encoded
		String[] identifiers = new String[ClientLauncher.linkCount];
		for(int i = 0 ; i < ClientLauncher.linkCount ; i++){
			identifiers[i] = encodeString[i].substring((length-1)*BYTE_LENGTH);
		}
		// cut the incoming to be encoded binary value into Bytes and transfer each Byte to decimal value in GF(2^8)
		for (int i = 0; i < length-1; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[j][i] = Integer.parseInt(encodeString[j].substring(i * BYTE_LENGTH, (i + 1) * BYTE_LENGTH));
			}
		}

		// the output array of string in form of binary value that can directly be sent out to each link
		String[] output = new String[ClientLauncher.linkCount];
		
		// init GaloisField class
		GaloisField gf = GaloisField.getInstance();
		
		// row of galois field , or link number, in this case 4
		for (int rowOfGaloisField = 0; rowOfGaloisField < ClientLauncher.linkCount; rowOfGaloisField++) {
			// length of bytes that needs to be encoded, or how many times of * and + need to be carried out
			String ysOfOneLink = "";
			for (int dataByteIndex = 0; dataByteIndex < length; dataByteIndex++) {
				// number of multiply, in this case 4
				int byteEncodeResult = 0;
				for (int galoisColumn = 0; galoisColumn < ClientLauncher.linkCount; galoisColumn++) {
					byteEncodeResult = gf.add(byteEncodeResult, gf.multiply(GALOIS_FIELD[rowOfGaloisField][galoisColumn],matrix[galoisColumn][dataByteIndex]));
				}
				// after multiply of each row and column, store the result as binary string
				ysOfOneLink += UniversalFunctions.formatBinaryString(Integer.toBinaryString(byteEncodeResult), BYTE_LENGTH);
			}
			// store complete binary string of encoded value AND append corresponding identifier to the end for each link
			output[rowOfGaloisField] = ysOfOneLink + identifiers[rowOfGaloisField];
		}

		return output;
	}

	/**
	 * @param encodeString
	 * @return
	 */
	public static String[] decode_apache(String[] encodeString) {

		int length = encodeString[0].length() / BYTE_LENGTH;
		// 2 dimensional int array to store decimal version of the binary value
		int[][] matrix = new int[ClientLauncher.linkCount][length];
		
		// Save only identifier of each packet so that only content will be encoded
		String[] identifiers = new String[ClientLauncher.linkCount];
		for(int i = 0 ; i < ClientLauncher.linkCount ; i++){
			identifiers[i] = encodeString[i].substring((length-1)*BYTE_LENGTH);
		}
		// cut the incoming to be encoded binary value into Bytes and transfer each Byte to decimal value in GF(2^8)
		for (int i = 0; i < length-1; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[j][i] = Integer.parseInt(encodeString[j].substring(i * BYTE_LENGTH, (i + 1) * BYTE_LENGTH));
			}
		}

		// the output array of string in form of binary value that can directly be sent out to each link
		String[] output = new String[ClientLauncher.linkCount];
		
		// init GaloisField class
		GaloisField gf = GaloisField.getInstance();
		
		// row of galois field , or link number, in this case 4
		for (int rowOfGaloisField = 0; rowOfGaloisField < ClientLauncher.linkCount; rowOfGaloisField++) {
			// length of bytes that needs to be encoded, or how many times of * and + need to be carried out
			String ysOfOneLink = "";
			for (int dataByteIndex = 0; dataByteIndex < length; dataByteIndex++) {
				// number of multiply, in this case 4
				int byteEncodeResult = 0;
				for (int galoisColumn = 0; galoisColumn < ClientLauncher.linkCount; galoisColumn++) {
					byteEncodeResult = gf.add(byteEncodeResult, gf.multiply(INVERSE_GALOIS_FIELD[rowOfGaloisField][galoisColumn],matrix[galoisColumn][dataByteIndex]));
				}
				// after multiply of each row and column, store the result as binary string
				ysOfOneLink += UniversalFunctions.formatBinaryString(Integer.toBinaryString(byteEncodeResult), BYTE_LENGTH);
			}
			// store complete binary string of encoded value AND append corresponding identifier to the end for each link
			output[rowOfGaloisField] = ysOfOneLink + identifiers[rowOfGaloisField];
		}

		return output;
	}
	
	private static void cout(String coutString) {
		System.out.println("Encoder ::: " + coutString);
	}
}
