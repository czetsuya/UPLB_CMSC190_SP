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
// The ImageEncryptor Class								 	//
// This class is responsible for the various methods     	//
// responsible for creating shares and making the coalesce  //
// image. The algorithms are all based on the construction  //
// methods given by Doug Stinson in his papers on Visual    //
// Cryptography that can be downloaded at			     	//
// http://www.cacr.math.uwaterloo.ca/~dstinson/visual.html  //
/***********************************************************/

// The package where the base classes are stored
package visualcryptography.imageencryptor;

import visualcryptography.codec.gif.MyGif;
import visualcryptography.matrix.Matrix;
import visualcryptography.VisualCryptography;


// Imported from the sourceforge community
import net.sourceforge.jiu.data.BilevelImage;
import net.sourceforge.jiu.ops.MissingParameterException;
import net.sourceforge.jiu.ops.Operation;
import net.sourceforge.jiu.codecs.CodecMode;
import net.sourceforge.jiu.codecs.GIFCodec;
import net.sourceforge.jiu.codecs.UnsupportedCodecModeException;
import net.sourceforge.jiu.color.reduction.RGBToGrayConversion;
import net.sourceforge.jiu.data.PixelImage;
import net.sourceforge.jiu.gui.awt.ToolkitLoader;
import net.sourceforge.jiu.ops.MissingParameterException;
import net.sourceforge.jiu.ops.OperationFailedException;
import net.sourceforge.jiu.ops.WrongParameterException;

// The classes accessible to the original package
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * ImageEncryptor is responsible for the creating the share images
 * and so as in performing the coalesce function.
 * @author Edward P. Legaspi
 * @since 6.25.05
 **/

public class ImageEncryptor {
  /**
   * The main class. Defined here to accessed main class variables.
   **/
  private VisualCryptography vc = null;  
  /**
   * An instance of a Random object padded with the filename of the images.
   * To be used in saving the image files.
   **/
  private static Random rand = null;
  /**
   * The number of shares used in encrypting images.
   **/
  private int nShares;
  /**
   * An array instance of MyGif Object. Used for encryption.
   **/
  private MyGif[] share;
  /**
   * An array instance of MyGif Object. Used for coalesce.
   **/
  private MyGif[] participant;
  /**
   * The matrix object use to encrypt white pixel.
   **/
  private Matrix c0 = null;
  /**
   * The matrix object use to encrypt black pixel.
   **/
  private Matrix c1 = null;
  
  /**
   * External Resource File (for easier manipulations).
   **/
  private static ResourceBundle resources;

  static {
    try {
      resources = ResourceBundle.getBundle("resources.visual_encryptor", Locale.getDefault());
    } catch (MissingResourceException mre) {
	  System.err.println("resources/visual_encryptor.properties not found");
      System.exit(1);
    }
  }
  
  /**
   * Constructor
   **/
   
  /**
   * Creates an ImageEncryptor object.
   **/
  public ImageEncryptor(VisualCryptography vc, int n) {
  	nShares = n;
  	participant	= new MyGif[nShares];
  	share = new MyGif[nShares];
  	this.vc = vc;
  }
  
  /**   
   * Preserves the size of the image by distributing only one column
   * of the matrix that is randomly selected. This results in a lost of
   * detail since some white pixel from the source image is encoded 
   * using black pixel on the n shares. xor-ing them produces a white.
   * @throws IOException, UnsupportedCodecModeException, MissingParameterException, OperationFailedException
   **/
  public void randomBasisColumn(Matrix c0, Matrix c1, MyGif sourceImage) 
  								   throws IOException,
  								  		  UnsupportedCodecModeException, 
				  						  MissingParameterException, 
  								  		  OperationFailedException {
	System.out.println("Random Basis Column:");  								  	
	//number of participant pixel expansion
    int n = c0.numRows();
    int m = c0.numCols();
    Integer snum = new Integer(1);    
    String sname = null;
    Random rand;    
    int x, randcol;
    
    //create the shares
    rand = new Random();
    for(int j = 0; j < n; j++) {
      sname = snum.toString();
      sname = "share" + sname;
      share[j] = new MyGif(sname, sourceImage.getHeight(), sourceImage.getWidth());
      x = snum.intValue();
      snum = new Integer(++x);
    } 
    for(int i = 0; i < sourceImage.getHeight(); i++)
      for(int j = 0; j < sourceImage.getWidth(); j++)
		if(sourceImage.getPixel(i, j) == 0) { //white pixel encode using c0
		  randcol = Math.abs(rand.nextInt() % m);
		  for(int k = 0; k < n; k++) {
		    if(c0.getElement(k, randcol) == 0)
		      share[k].setPixel(i, j, 0); //create white subpixel
		    else
		      share[k].setPixel(i, j, 1);	//create black subpixel
		  }
		} else { //black pixel encode using c1
		  randcol = Math.abs(rand.nextInt() % m);
		  for(int k = 0; k < n; k++) {
		    if(c1.getElement(k, randcol) == 0)
		      share[k].setPixel(i, j, 0); //create white subpixel
		    else
		      share[k].setPixel(i, j, 1); //create black subpixel
		  }
		} //end else of c1
	vc.randomNum = Math.abs(rand.nextInt());
	for(int i = 0; i < n; i++) {
	  GIFCodec codec = new GIFCodec();
      codec.setImage(share[i].getPixelImage());
      //codec.setFile(getResourceString("externalPath") + "share" + (i + 1) + ".gif", CodecMode.SAVE);
      codec.setFile(getResourceString("externalPath") + "/temp/share" + (i + 1) + "_" + vc.randomNum + ".gif", CodecMode.SAVE);
      codec.process();
      share[i] = null;
      codec.close();
	}
  } //end randBasisColumn
  
  /**   
   * This method lets the user to superimpose the shares of images.
   * It saves the file into the path specified located in the 
   * resource directory. And named it coalesce.gif.
   * Without noise.
   * @throws IOException, UnsupportedCodecModeException, MissingParameterException, OperationFailedException
   **/
  public void coalesceShares(Vector filenames) throws IOException, 
								  						 UnsupportedCodecModeException, 
				  				  						 MissingParameterException, 
  								  						 OperationFailedException {
	System.out.println("Coalesce:");
	rand = new Random();
  	for(int k = 0; k < nShares; k++)  	  
 	  participant[k] = new MyGif((String)filenames.get(k));    
 	
 	//the superimpose image  
    MyGif coalesceImage = new MyGif("coalesce", participant[0].getHeight(), participant[0].getWidth());
    
    for(int i = 0; i < coalesceImage.getHeight(); i++)
	  for(int j = 0; j < coalesceImage.getWidth(); j++)
	    coalesceImage.setPixel(i, j, xOrPixel(i, j, nShares));
    
    vc.randomNum = Math.abs(rand.nextInt());
    GIFCodec codec = new GIFCodec();
    codec.setImage(coalesceImage.getPixelImage());
    codec.setFile(getResourceString("externalPath") + "/temp/coalesce_" + vc.randomNum + ".gif", CodecMode.SAVE);
    codec.process();
    codec.close();
  } //end coalesceShares without noise
  
  /**
   * Calculated by treating a white pixel as 0 and a black pixel as 1.
   * Cancel even number of pixels, return them to white.
   *  We used the xOr-bit operator.
   **/
  private int xOrPixel(int row, int col, int n) {
	int ctr = 0;	
	for(int j = 0; j < n; j++)
	  if(participant[j].getPixel(row, col) == 1)	
		ctr++;
	if((ctr % 2) == 0)
	  return 0;
	else 
	  return 1;
  } //end xOrPixel
  
  /**   
   * This method lets the user to superimpose the shares of images.
   * It saves the file into the path specified located in the 
   * resource directory. And named it coalesce.gif.
   * With noise.
   * @throws IOException, UnsupportedCodecModeException, MissingParameterException, OperationFailedException
   **/
  public void coalesceSharesWithNoise(Vector filenames) throws IOException, 
								  						 UnsupportedCodecModeException, 
				  				  						 MissingParameterException, 
  								  						 OperationFailedException {
	System.out.println("Coalesce:");
	rand = new Random();
  	for(int k = 0; k < nShares; k++)  	  
 	  participant[k] = new MyGif((String)filenames.get(k));    
 	
 	//the superimpose image  
    MyGif coalesceImage = new MyGif("coalesce", participant[0].getHeight(), participant[0].getWidth());
    
    for(int i = 0; i < coalesceImage.getHeight(); i++)
	  for(int j = 0; j < coalesceImage.getWidth(); j++)
	    coalesceImage.setPixel(i, j, orPixel(i, j, nShares));
    
    vc.randomNum = Math.abs(rand.nextInt());
    GIFCodec codec = new GIFCodec();
    codec.setImage(coalesceImage.getPixelImage());
    codec.setFile(getResourceString("externalPath") + "/temp/coalesce_" + vc.randomNum + ".gif", CodecMode.SAVE);
    codec.process();
    codec.close();
  } //end coalesceShares with noise.
  
  /**
   * Calculated by treating a white pixel as 0 and a black pixel as 1.
   * If there is a black pixel in the column a black pixel is return.
   * We used the or-bit operator.
   **/
  private int orPixel(int row, int col, int n) {
  	int pixelValue = 0;	
	for(int j = 0; j < n; j++)
	  if(participant[j].getPixel(row, col) == 1)	
		pixelValue = 1;	
	  return pixelValue;
  } //end xOrPixel
  
  /**
   * This method returns a string from the visual_encryptor's resource bundle.   
   **/
  private String getResourceString(String key) {
	String value = null;
	try {
	  value = resources.getString(key);
	} catch (MissingResourceException e) {
	  value = null;
	} 
	return value;
  }  
} //end ImageEncryptor

//*********************Love Hina x Vandread******************************//