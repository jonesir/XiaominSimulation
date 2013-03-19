package de.jonesir.algo.standalone;
//import GaloisException;
//import GF.*;
public class decodingDemo {

	public double[][] B;

	public static void main(String[] args) throws GaloisException {

		DecodingBuf buf = new DecodingBuf();
		
		NCdatagram g1 = new NCdatagram();
		NCdatagram g2 = new NCdatagram();
		
		int L = 2;
		int C1 = 2;	// taille de la matrice de coef
		int C2 = 6;	// taille de toute la matrice (coef + données)
		int i,j,k;

		decodingDemo gauss = new decodingDemo();

		g1.Buf.append("alaa");
		g2.Buf.append("baha");

		System.out.println(g1.Buf.charAt(0));
		System.out.println(g2.Buf);
		i = g1.Buf.charAt(0);
		System.out.println(i);

		int[][] A = new int[2][6];

		int[][] coef = {{1,2},{5,7}};
		GaloisField base = new GaloisField(2);
		ExtendedGaloisField ext = new ExtendedGaloisField(base,'a',8);

		for(i=0; i<2; i++) {
			for(j=0; j<2; j++) {
				A[i][j] = coef[i][j];
			}
		}

		for(i=0 ; i<2; i++) {
			for(j=2 ; j<6; j++) {
				A[i][j] = ext.sum(ext.product(coef[i][0],(int) g1.Buf.charAt(j-2)),ext.product(coef[i][1],(int) g2.Buf.charAt(j-2)));
			}
		}

		boolean innovative = gauss.solveA(A,L,C1,C2);

		for(i = 0 ; i< L ; i++) { 
			for(k = 0 ; k < C2 ; k++) { 
				System.out.println( ((char) gauss.B[i][k]));
			}
		}
		if(!innovative){		
			System.out.println("Matrix not inversible ");
		}

	}


//---------------------------------------------------------------------------------------
	public boolean solveA(int[][] A, int L, int C1, int C2) throws GaloisException {

		double[] tmp;
		int k,i,l;
		double pivot, tmpd;
		B = new double[L][C2];

		GaloisField base = new GaloisField(2);
		ExtendedGaloisField ext = new ExtendedGaloisField(base,'a',8);
 
		for(i = 0 ; i< L ; i++) { 
			for(k = 0 ; k < C2 ; k++) { 
				B[i][k] = A[i][k];
			}
		}

		// boucle d'iterations: # d'iter = # des lignes
		for(k = 0; k < L ; k++) { 

			// first step of the kth iteration
			// we need to insure that the pivot is not zero
			if(B[k][k] == 0) {
				boolean b = false;
				for(int n = k+1 ; n < L ; n++){
					if(B[n][k] != 0) {
						tmp = B[n];
						B[n] = B[k];
						B[k] = tmp;
						b = true;
						break;
					}
					
				}
				if(!b) {
					for(int n = k+1 ; n < C1 ; n++){
						if(B[k][n] != 0) {
/**							for(l = 0; l < L; l++){
								tmpd = B[l][k];
								B[l][k] = B[l][n];
								B[l][n] = tmpd;
								
							}
*/
							Permut_col( k, n, L);
							b = true;
							break;
						}
					}
				}

				if(!b) return false;
			}			

			double piv = B[k][k];
			for(int cp = k; cp < C2 ; cp++) {
//				B[k][cp] = B[k][cp] / piv;
				B[k][cp] = (double) ext.divide((int) B[k][cp], (int) piv);
			}
			
			for(i = 0; i < L ; i++) {  // boucle des lignes
				
				
				if(i != k) {
					pivot = B[i][k];
					for(int c = k; c < C2 ; c++) { //boucle des colonnes
//						B[i][c] = B[i][c] - pivot*B[k][c];
						B[i][c] = (double) ext.minus((int) B[i][c], (int) ext.product((int) pivot, (int) B[k][c]));
					}
										
				}
			}
		}
		return true;
	}

//-----------------------------------------------------------------------------
	public void Permut_col(int col1, int col2, int L) {
		
		double tmpd;

		for(int l = 0; l < L; l++){
			tmpd = B[l][col1];
			B[l][col1] = B[l][col2];
			B[l][col2] = tmpd;
		}
	}

}
