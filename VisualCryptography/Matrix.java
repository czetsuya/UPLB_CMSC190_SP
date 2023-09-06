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
// The Matrix Class 										//
// This class is used to create the basis matrices			//
/***********************************************************/

//The package where the base classes are stored
package visualcryptography.matrix;

//The classes imported from the original java package
import java.util.Random;

/**
 * The Matrix class is the holder of the object created by the MatrixConstructor
 * object. It also facilitates various operations to the Matrix itself.
 * 
 **/

public class Matrix {
  /**
   * 2 Dimensional array of integers.
   **/
  private int M[][] = null;
  /**
   * The row count of the matrix object.
   **/
  private int rows = 0;
  /**
   * The column count of the matrix object.
   **/
  private int  cols = 0;
   
  /**
   * Constructors
   **/
  
  /**
   * Constructor is used to create a 0x0 matrix M.
   * Rows and Columns can be inserted later using {@link #insertRow} and {@link #insertCol}.
   **/  
  public void Matrix() {
  	rows = cols = 0;
  }
  
  /**
   * Constructor is used to create a (height)x(width) matrix M of integers.
   **/    
  public Matrix(int height, int width) {
  	if(height < 0)
  	  height = 0;
  	if(width < 0) 
  	  width = 0;
  	rows = height;
  	cols = width;
  	M = new int[rows][cols];
  }
  
  /**
   * Returns the number of rows in the matrix M.
   **/
  public int numRows() {
    return rows;
  }
  
  /**
   * Returns the number of columns in the matrix M
   **/
  public int numCols() {
  	return cols;
  }
  
  /**
   * Returns the (row, col) element of the matrix M.
   **/
  public int getElement(int row, int col) {
    return M[row][col];
  }
  
  /**
   * Sets the entry (row, col) of the matrix M into value.
   **/	
  public void setElement(int row, int col, int value) {
    M[row][col] = value;   
  }
  
  /**
   * Randomly permutes the columns of the matrix.
   **/
  public void permuteCols() {  	
	int[] added = new int[cols];
	Random rand = new Random();	
	int row, column;	
	
	for(int i = 0; i < cols; i++)		//initialize array to false
	  added[i] = 0;
	int permuted[][] = new int[rows][cols];	//store permutation in a new matrix	
	
	for(int i = 0; i < cols; i++) {
	  column = Math.abs(rand.nextInt() % cols);
	  while(added[column] == 1)
		column = (column + 1) % cols;
	  for(row = 0; row < rows; row++)
	    permuted[row][column] = M[row][i];
	  added[column] = 1;
	}	
	M = permuted;
  }
  
  /**
   * Inserts the row[] in the (n + 1)th row in the matrix where 0 <= n <= rows.
   **/
  public void insertRow(int row[], int n) {
  	if((n < 0) || (n > rows))
  	  return;
  	int newRow[][] = new int[rows + 1][];
  	for(int i = rows; i > n; i--)
  	  newRow[i] = M[i - 1];
  	for(int i = 0; i < n; i++)
  	  newRow[i] = M[i];
  	M = newRow;
  	M[n] = new int[cols];
  	for(int i = 0; i < cols; i++)
  	  M[n][i] = row[i];
  	rows++;
  }
  
  /**
   * Inserts the col[] in the (n + 1)th column in the matrix where 0 <= n <= cols.
   **/  
  public void insertCol(int col[], int n) {
  	if((n < 0) || (n > cols))
  	  return;
  	int newCol[][] = new int[rows][];
  	for(int i = 0; i < rows; i++)
  	  newCol[i] = new int[cols + 1];
  	for(int i = 0; i < rows; i++) {
  	  for(int j = cols; j > n; j--)
  	    newCol[i][j] = M[i][j - 1];
	  for(int j = 0; j < n; j++)
		newCol[i][j]=M[i][j];
	}
	for(int i = 0; i < rows; i++)
	  newCol[i][n] = col[i];	
	M = newCol;
	cols++;  	
  }
  
  /**
   * Deletes the row row in the matrix M, where 0 <= row <= rows - 1.
   **/
  public void delRow(int row) {  	
  	if((row < 0) || (row >= rows))
  	  return;
  	for(int i = row; i < rows - 1; i++)
  	  M[i] = M[i + 1];
  	rows--;
  }
  
  /**
   * Deletes the column col in the matrix M, where 0 <= col <= cols - 1.
   **/  
  public void delCol(int col) {
  	if((col < 0) || (col >= cols))
  	  return;
  	for(int i = col; i < cols - 1; i++)
  	  for(int j = 0; j < rows; j++)
  	    M[j][i] = M[j][i + 1];
  	cols--;
  }
  
  /**
   * Convert the content of the matrix M into its string representation.
   * The columns are seperated by tabs and rows are by newlines.   
   **/
  public String convertToString() {
   	String result = "";
    int i, j;
    for(i = 0; i < rows; i++) { //rows
      for(j = 0; j < cols; j++) //columns
        result += (M[i][j] + "\t");
      result += "\n";
    }
    return result;
  }  
}

//******************************Air x AppleSeed*************************//