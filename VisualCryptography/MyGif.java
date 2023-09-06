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
// The MyGif Class 											//
// This class is used to create the image and combine shares//
// This class process black and white gif class				//
// It can read and write black and white gif files 			//
// It only tells you if a pixel is black or white but doesn't//
// give the intensity or whatsoever, it doesn't say what the//
// color of the pixel is.									//
/***********************************************************/

//The package where the base classes are stored
package visualcryptography.codec.gif;

//The extension class from my comrades
import net.sourceforge.jiu.codecs.CodecMode;
import net.sourceforge.jiu.codecs.GIFCodec;
import net.sourceforge.jiu.codecs.UnsupportedCodecModeException;
import net.sourceforge.jiu.color.reduction.RGBToGrayConversion;
import net.sourceforge.jiu.data.RGBImage;
import net.sourceforge.jiu.data.GrayImage;
import net.sourceforge.jiu.data.MemoryBilevelImage;
import net.sourceforge.jiu.data.PixelImage;
import net.sourceforge.jiu.gui.awt.ToolkitLoader;
import net.sourceforge.jiu.ops.MissingParameterException;
import net.sourceforge.jiu.ops.MissingParameterException;
import net.sourceforge.jiu.ops.Operation;
import net.sourceforge.jiu.ops.OperationFailedException;
import net.sourceforge.jiu.ops.WrongParameterException;

//The classes imported from the original java package
import java.io.IOException;
import java.io.File;

/**
 * MyGif is the main class for creating a black and white image it accepts any RGB
 * image and then convert it to gray scale. Colored pixel will be discarded so it's
 * advice to used a black and white GIF image.
 * The application used Gif image format.
 * @author Edward P. Legaspi
 * @since 6.25.05
 **/

public class MyGif extends GIFCodec {
  /**
   * The GrayImage object used for typecasting an RGB image.
   **/
  private GrayImage grayImage = null;
  /**
   * The current open file accessible to the entire module for processing.
   **/
  private String filename = null;
  /**
   * The height of the gif object.
   **/
  private int height = 0;
  /**
   * The width of the gif object.
   **/
  private int width = 0;  
  
  /**
   * Constructors
   **/
  
  /**
   * The default constructor used to instantiate a MyGif class and sets its dimension to 0x0.   
   **/
  public MyGif() {
    height = 0;
    width = 0;
  }
  
  /**
   * Constructor used to open an instance of the MyGif class if open it sets the Bilevel image's
   * dimension to the dimension of the opened image.
   * This constructor was primarily constructed for testing purposes
   **/  
  public MyGif(String filename) throws IOException, 
  								  		  UnsupportedCodecModeException, 
  								  		  MissingParameterException, 
  								  		  OperationFailedException {
  
    PixelImage coloredImage = ToolkitLoader.loadViaToolkitOrCodecs(filename);
  	RGBToGrayConversion rgbtogray = new RGBToGrayConversion();
 	rgbtogray.setInputImage(coloredImage);
 	rgbtogray.process();
 	PixelImage pixelImage = rgbtogray.getOutputImage();
 	
 	GIFCodec codec = new GIFCodec();
 	codec.setImage(pixelImage); 	
 	grayImage = (GrayImage)codec.getImage(); 	
 	height = pixelImage.getHeight();
 	width = pixelImage.getWidth(); 	
 	codec.close();
 	this.filename = filename; 	
  }
  
  /**
   * Constructor used to createan instance of the MyGif class if open it sets the 
   * Bilevel image's dimension to (height)x(width).
   **/
  public MyGif(String fname, int height, int width) {  	
  	filename = fname;
  	this.height = height;
  	this.width = width;
  	grayImage = new MemoryBilevelImage(width, height);
  	for(int i = 0; i < height; i++)
  	  for(int j = 0; j < width; j++)
  	    grayImage.putWhite(j, i);
  	this.filename = filename;
  }
  
  /**
   * Used to get the pixel value of the gif stored in (row, col) where 0 <= row <= rows and 0 <= col <= cols.
   * If the pixel is white it returns 0 else it returns 1.
   * It can only distinguish a pixel as either white or black no color distinction.   
   **/
  public int getPixel(int row, int col) {
    if(grayImage.isBlack(col, row) == true)
      return 1;
    else 
      return 0;      
  }
  
  /**
   * Sets the pixel value of (row, col)) to color. It only makes black and white gifs not color.
   **/
  public void setPixel(int row, int col, int color) {  	
  	if(color == 1)
  	  grayImage.putBlack(col, row);
  	else if(color == 0)
  	  grayImage.putWhite(col, row);
  }   
  
  /**
   * Returns the filename of the MyGif instance.
   **/
  public String getFilename() {
  	return filename;
  }
  
  /**
   * Returns the height of the MyGif instance.
   **/
  public int getHeight() {
    return height;
  }
  
  /**
   * Returns the width of the MyGif instance.
   **/
  public int getWidth() {
    return width;
  }
  
  /**
   * Returns the GrayImage that we used in manipulating the black and white pixels of the gif image.
   **/
  public GrayImage getGrayImage() {
    return grayImage;
  }
  
  /**
   * Typecast the grayImage to PixelImage object since we need it when saving in the gifCodec.
   **/
  public PixelImage getPixelImage() {
    return (PixelImage)grayImage;
  }
  
  /**
   * Saves the image using the GIFCodec.
   **/  
  public void saveGifImage(String filename) throws IOException, 
  								  		  			  UnsupportedCodecModeException, 
  								  		  			  MissingParameterException, 
  								  		  			  OperationFailedException {
  								  		  			  	
    GIFCodec codec = new GIFCodec();
    codec.appendComment("Edward P. Legaspi - Laizzez_faire07@yahoo.com");
 	codec.setImage(getPixelImage());
 	codec.setInterlacing(true);
 	codec.setFile(filename, CodecMode.SAVE);
 	codec.process();
 	codec.close();  
  }
} //end MyGif

//***********HunterXHunter x Chrno Crusade x Hand Maid May*********//