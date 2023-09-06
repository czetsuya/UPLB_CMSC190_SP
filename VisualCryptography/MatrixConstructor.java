/***********************************************************/
// What	  : Special Project (CMSC 190(2))					//
// School : University of the Philippines Los Baños, Laguna //
// Sem/Yr : 1st Semester (2005-06)							//
// Title  : Visual Encryptor java 1.4.2_06					//
// Author : Edward P. Legaspi								//
// Email  : Laizzez_faire07@yahoo.com						//
// Cell # : 0918-394-4272									//
/***********************************************************/

/***********************************************************/
// The MatrixConstructor Class								//
// Used to initialize the matrix c0 and c1 used 			//
// for encrypting the shares.								//
// (2,2), (2,n), (n, n) threshold schemes 					//
// Construction of Matrix c0 to encrypt white pixels		//
// Construction of Matrix c1 to encrypt black pixels		//
/***********************************************************/

// The package where the base classes are stored
package visualcryptography.matrix;

/**
 * MatrixConstructor is used to create the matrix c0 and c1 used for encrypting.
 * @author Edward P. Legaspi
 * @since 6.25.05
 **/

public class MatrixConstructor {
  //the black and white pixel respectively
  /**
   * An integer representation of a white pixel.
   **/
  private final int whitePixel = 0;
  /**
   * An integer representation of a black pixel.
   **/
  private final int blackPixel = 1;
	
  /**
   * Constructors
   **/
  
  /**
   * Constructs the zero-one {@link visualcryptography.matrix.Matrix} c0 for 2x2 Visual Cryptography.
   * Matrix looks like 1 0 0 1
   *				   1 0 0 1
   **/
  public Matrix gen2x2WhiteBasis() {
  	Matrix temp = new Matrix(2, 4);
  	temp.setElement(0, 0, blackPixel);
  	temp.setElement(0, 1, whitePixel);
  	temp.setElement(0, 2, whitePixel);
  	temp.setElement(0, 3, blackPixel);
  	temp.setElement(1, 0, blackPixel);
  	temp.setElement(1, 1, whitePixel);
  	temp.setElement(1, 2, whitePixel);
  	temp.setElement(1, 3, blackPixel);
	return temp;
  }
  
  /**
   * Constructs the zero-one {@link visualcryptography.matrix.Matrix} c1 for 2x2 Visual Cryptography
   * Matrix looks like 1 0 1 0 
   *				   0 1 0 1
   **/
  public Matrix gen2x2BlackBasis() {
    Matrix temp = new Matrix(2, 4);
    temp.setElement(0, 0, blackPixel);
	temp.setElement(0, 1, whitePixel);
	temp.setElement(0, 2, blackPixel);
	temp.setElement(0, 3, whitePixel);
	temp.setElement(1, 0, whitePixel);
	temp.setElement(1, 1, blackPixel);
	temp.setElement(1, 2, whitePixel);
	temp.setElement(1, 3, blackPixel);
	return temp;
  }
  
  /**
   * Generates the basis matrix c0 for encrypting the white pixel for the 2xn VCS.
   * Returns a zero-one {@link visualcryptography.matrix.Matrix} that has a (n - 1)Combination(n / 2) columns whose weights are zero
   * and (n - 1)Combination((n / 2) - 1) columns that have weights of n.   
   **/
  public Matrix gen2xNWhiteBasis(int nShares) {	
	Matrix temp = new Matrix(nShares, pixelExpansion2xN(nShares));
	int whiteCols = nCr(nShares - 1, nShares / 2);	
	int blackCols = nCr(nShares - 1, (nShares / 2) - 1);	
	
	//sets the columns whose weights are 0
	for(int row = 0; row < nShares; row++)
	  for(int col = 0; col < whiteCols; col++)
	    temp.setElement(row, col, whitePixel);
	//sets the columns whose weights are 1
	for(int row = 0; row < nShares; row++)
	  for(int col = whiteCols; col < (whiteCols + blackCols); col++)
		temp.setElement(row, col, blackPixel);	
	return temp;	
  } //end gen2xNWhiteBasis
  
  
  /**
   * Generates the basis matrix c1 for encrypting the black pixel for the 2xn VCS.
   * Returns a zero-one {@link visualcryptography.matrix.Matrix} of column weight (n / 2);
   * that has a column nCombination(n / 2).
   **/
  public Matrix gen2xNBlackBasis(int nShares) {
  	Matrix matrix = new Matrix(nShares, pixelExpansion2xN(nShares));
  	Matrix temp = new Matrix(XRaiseToY(2, nShares), nShares);
    boolean flag = false;
    int j;
    for(int i = nShares - 1; i >= 0; i--) {
      for(j = 0; j < Math.pow(2, nShares);)
        if(flag == false) {          
          for(int k = 0; k < (Math.pow(2, i + 1)) / 2; k++)
            temp.setElement(j++, (nShares - i) - 1, whitePixel);
          flag = true;          
        } else if(flag == true) {          
          for(int k = 0; k < (Math.pow(2, i + 1)) / 2; k++) {            
            temp.setElement(j++, (nShares - i) - 1, blackPixel);
          }
          flag = false;
        } //end else
    } //end for-loop    
    int col = 0;
    for(int i = 0; i < temp.numRows(); i++) {
      if(getRowWeight(temp, i) == (int)(nShares / 2)) {
        for(int k = 0; k < temp.numCols(); k++)
          matrix.setElement(nShares - k - 1, col, temp.getElement(i, k));
        col++;
      }
    } //end for-loop
    return matrix;
  } //end gen2xNBlackBasis
  
  /**
   * Generates the basis matrix c0 for encrypting the white pixel for the nxn VCS.
   * Creates a zero-one {@link visualcryptography.matrix.Matrix} whose column weights are even.
   **/
  public Matrix genNxNWhiteBasis(int nShares) {  	
	Matrix matrix = new Matrix(nShares, XRaiseToY(2, (nShares - 1)));
  	Matrix temp = new Matrix(XRaiseToY(2, nShares), nShares);
    boolean flag = false;
    int j;
    for(int i = nShares - 1; i >= 0; i--) {
      for(j = 0; j < Math.pow(2, nShares);)
        if(flag == false) {          
          for(int k = 0; k < (Math.pow(2, i + 1)) / 2; k++)
            temp.setElement(j++, (nShares - i) - 1, whitePixel);
          flag = true;          
        } else if(flag == true) {          
          for(int k = 0; k < (Math.pow(2, i + 1)) / 2; k++) {            
            temp.setElement(j++, (nShares - i) - 1, blackPixel);
          }
          flag = false;
        } //end else
    } //end for-loop    
    int col = 0;
    for(int i = 0; i < temp.numRows(); i++) {
      if(getRowWeight(temp, i) % 2 == 0) {
        for(int k = 0; k < temp.numCols(); k++)
          matrix.setElement(nShares - k - 1, col, temp.getElement(i, k));
        col++;
      }
    } //end for-loop    
    return matrix;
  } //end gen2xNWhiteBasis
  
  /**
   * Generates the basis matrix c1 for encrypting the black pixel for the nxn VCS.
   * Creates a zero-one {@link visualcryptography.matrix.Matrix} whose column weights are odd.
   **/  
  public Matrix genNxNBlackBasis(int nShares) {	
	Matrix matrix = new Matrix(nShares, XRaiseToY(2, (nShares - 1)));
  	Matrix temp = new Matrix(XRaiseToY(2, nShares), nShares);
    boolean flag = false;
    int j;
    for(int i = nShares - 1; i >= 0; i--) {
      for(j = 0; j < Math.pow(2, nShares);)
        if(flag == false) {          
          for(int k = 0; k < (Math.pow(2, i + 1)) / 2; k++)
            temp.setElement(j++, (nShares - i) - 1, whitePixel);
          flag = true;          
        } else if(flag == true) {          
          for(int k = 0; k < (Math.pow(2, i + 1)) / 2; k++) {            
            temp.setElement(j++, (nShares - i) - 1, blackPixel);
          }
          flag = false;
        } //end else
    } //end for-loop    
    int col = 0;
    for(int i = 0; i < temp.numRows(); i++) {
      if(getRowWeight(temp, i) % 2 == 1) {
        for(int k = 0; k < temp.numCols(); k++)
          matrix.setElement(nShares - k - 1, col, temp.getElement(i, k));
        col++;
      }
    } //end for-loop    
    return matrix;
  } //end genNxNBlackBasis
  
  /**
   * Returns the combination of n taken r.
   **/
  private int nCr(int n, int r) {
  	return(factorialN(n) / ((factorialN(n - r)) * factorialN(r)));
  }
  
  /**
   * Returns the factorial of a number n.
   **/
  private int factorialN(int n) {
  	if (n <= 1)
	  return 1;
	return (n * factorialN(n - 1));
  }
  
  /**
   * Returns the pixel expansion m-columns which is [n Combination (n / 2)].
   **/
  private int pixelExpansion2xN(int n) {
	return (factorialN(n) /  (factorialN(n - (n / 2)) * factorialN(n / 2)));
  }
  
  /**
   * Returns the integer value of a raise to b.
   **/
  private int XRaiseToY(int a, int b) {
    return (new Double(Math.pow((double)a, (double)b)).intValue());
  }
  
  /**
   * Returns the number of ones a column have.
   **/
  private int getRowWeight(Matrix temp, int row) {
  	int weight = 0;
  	for(int i = 0; i < temp.numCols(); i++) {
  	  if(temp.getElement(row, i) == 1)
  	    weight++;
  	}
  	return weight;
  }
  
} //end MatrixConstructor

//*******************Naruto x DNA2 x Azumanga Daoih***********************//