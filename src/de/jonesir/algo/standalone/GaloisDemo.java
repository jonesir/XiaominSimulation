package de.jonesir.algo.standalone;
public class GaloisDemo {

	public static void main(String[] args) throws GaloisException {

		final int c1[] = {2, 2, 1, 0, 1, 2};
		final int c2[] = {1, 0, 2};

		System.out.println("");
		System.out.println("");

		GaloisField field = new GaloisField(3);

		System.out.println("Field : "+field);

		System.out.println("");

		GaloisPolynomial p1 = new GaloisPolynomial(c1,'a',field);
		System.out.println("P1 : "+p1);

		GaloisPolynomial p2 = new GaloisPolynomial(c2,'a',field);
		System.out.println("P2 : "+p2);

		System.out.println("");

		System.out.println("P1+P2 = "+p1.sum(p2));

		System.out.println("P1*P2 = "+p1.product(p2));

		try {
			GaloisPolynomial[] res = p1.divide(p2);
			System.out.println("P1/P2 = "+res[0]+" Rm: "+res[1]);
		} catch (GaloisException e) {
			System.out.println(e);
		}
		
	}
}
