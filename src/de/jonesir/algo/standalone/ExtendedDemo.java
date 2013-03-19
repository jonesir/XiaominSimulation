package de.jonesir.algo.standalone;
import java.io.*;
import java.util.Vector;

public class ExtendedDemo {

	public static final int[] IRRCOEFS = {2, 1, 1};

	public static void main(String[] args) throws GaloisException {
		rundemo1();
		readStringFromStream(System.in);
		rundemo2();		
		readStringFromStream(System.in);
		rundemo3();		
		readStringFromStream(System.in);
		rundemo4();		
		readStringFromStream(System.in);
		rundemo5();		
	}

	public static void rundemo5() {
		int ctr;
		System.out.println("");
		System.out.println("");
	}

	public static void rundemo4() throws GaloisException {
		int ctr;
		System.out.println("");
		System.out.println("");
		GaloisField base = new GaloisField(3);
		try {
			ExtendedGaloisField ext = new ExtendedGaloisField(base,'a',2); 
			System.out.println("Base: GF(3), "+ext.toString());	
			System.out.println("Cyclotomics modulo 8, using GF(3)");
			System.out.println("---------------------------------");
			Vector res = base.getAllCyclotomics(8);
			for (ctr = 0; ctr < res.size(); ctr++) {
				int[] mycyclo = (int[])res.elementAt(ctr);
				String mystr = "{";
				for (int tel = 0; tel < mycyclo.length; tel++) {
					if (tel != 0) mystr += ", " + mycyclo[tel];
					else mystr += String.valueOf(mycyclo[tel]);
				}
				mystr += "}";
				System.out.println(mystr);
				if (mycyclo.length > 0) {
					GaloisPolynomial minpoly = ext.getMinimalPolynomial(mycyclo[0]+1,'x');
					System.out.println("           "+minpoly);
				}
			}
		} catch (GaloisException e) {
			System.out.println(e);
		}
	}

	public static void rundemo3() throws GaloisException {
		int ctr;
		System.out.println("");
		System.out.println("");
		GaloisField base = new GaloisField(2);
		System.out.println("Base : "+base);
		System.out.println("Create Extended fields GF(2^k). Show ModuloPoly and Primitive Element");
		System.out.println("---------------------------------------------------------------------");
		try {
			for (ctr = 2; ctr < 8; ctr++) {
				ExtendedGaloisField field = 
					new ExtendedGaloisField(base,'a',ctr);
				System.out.println(field+" "+field.getModuloPoly()+", "+field.getAlfaPower(1));
			}
		} catch (GaloisException e) {
			System.out.println(e);
		}
	}

	public static void rundemo2() throws GaloisException {
		int ctr;
		System.out.println("");
		System.out.println("");
		GaloisField base = new GaloisField(3);
		System.out.println("Base : "+base);
		System.out.println("Create Extended fields isomorph with GF(27) using irreducible polynomials");
		System.out.println("-------------------------------------------------------------------------");
		Vector irrpolys = GaloisPolynomial.irrMonomials(3,'a',base);
		try {
			for (ctr = 0; ctr < irrpolys.size(); ctr++) {
				GaloisPolynomial irrpoly = (GaloisPolynomial)irrpolys.elementAt(ctr);
				ExtendedGaloisField field = new ExtendedGaloisField(irrpoly);
				System.out.print("Polynomial: "+irrpoly);
				System.out.println(" Alfa: "+field.getAlfaPower(1));
			}
		} catch (GaloisException e) {
			System.out.println(e);
		}
	}

	public static void rundemo1() throws GaloisException {
		int ctr;
		System.out.println("");
		System.out.println("");
		GaloisField base = new GaloisField(3);
		System.out.println("Base : "+base);
		GaloisPolynomial irrpoly = new GaloisPolynomial(IRRCOEFS,'a',base);
		System.out.println("Primitive Polynomial : "+irrpoly);
		try {
			ExtendedGaloisField field = new ExtendedGaloisField(irrpoly);
			System.out.println("Field : "+field);
			for (ctr = 0; ctr < field.getCardinality(); ctr++) {
				field.setDisplayMode(GaloisField.ALFAPOWER);
				System.out.print(field.valueString(ctr));
				field.setDisplayMode(GaloisField.POLYNOMIAL);	
				System.out.println(" = "+field.valueString(ctr));
			}		
		} catch (GaloisException e) {
			System.out.println(e);
		}
	}

	public static String readStringFromStream(InputStream stream) {
		int ch = 0;
		boolean busy = true;
		StringBuffer outs = new StringBuffer(20);
		while (ch != -1 && ch != 13 && ch != 10 && busy) {
			try {
				ch = stream.read();
			} catch (IOException e) {
				busy = false;
			}
			if (ch != -1 && ch != 13 && ch != 10 && busy) outs.append((char)ch);
		}
		return outs.toString();
	}
}
