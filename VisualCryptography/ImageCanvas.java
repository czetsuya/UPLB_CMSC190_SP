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
// The ImageCanvas Class								 	//
// This class holds all the operation that draws the image	//
// onto the panel. However, I can't put the JPanel inside	//
// the JScrollPane, In fact I already spent	a lot of time   //
// debugging hoping something would pop out but I am until  //
// now is a failure, hopeless. This class will just draw the//
// part of the image that would fit the screen				//
/***********************************************************/

// The package where the base classes are stored
package visualcryptography.canvas;

import visualcryptography.VisualCryptography;

// The classes accessible to the original package
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

// Imported from the expansion package
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

/**
 * ImageCanvas holds all the operation that draws the image onto
 * the panel.
 * @author Edward P. Legaspi
 * @since 6.24.05
 **/

public class ImageCanvas extends JPanel {
  /** 
   * Instance of the main class to provide usefull variables.
   **/
  private VisualCryptography visualEncryptor = null;
  /** 
   * The source image. 
   **/
  public Image imageSource = null;
  /**
   * An instance of the Image object. Used in drawing an Image on the JPanel.
   **/
  public Image imageBuffer = null;
  /**
   * An instance of the ImageIcon class.
   **/
  private ImageIcon imageIcon = null;
  /** 
   * The height of the canvas. 
   **/
  private int height = 0;
  /**
   * The width of the canvas. 
   **/
  private int width = 0;
  
  /**
   * Constructors
   **/
  
  /**
   * Constructor used to instantiate an object 
   * of this class, sets the variables imageSource and imageBuffer to null.
   **/
  public ImageCanvas(VisualCryptography vc) {    
    this.visualEncryptor = visualEncryptor;
    imageSource = null;
    imageBuffer = null;
    setBackground(Color.white);
    setOpaque(true);
    setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
  }
  
  /**
   * Constructor used to instantiate an object 
   * of this class, sets the variables imageSource and imageBuffer to null
   * This has been used for debugging purposes that proves to be useful.
   **/
  public ImageCanvas() {
    imageSource = null;
    imageBuffer = null;
    setBackground(Color.white);
    setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
  }
  
  /**
   * Constructor is used to set the image of this object to ImageIcon.
   **/
  public ImageCanvas(ImageIcon img) {
    setImage(img);
    setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
    //this.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
  }
   
  /**
   * Draws the image to the drawing JPanel.
   **/
  public void setImage(ImageIcon img) {
  	if(img != null) {  		
  	  imageIcon = img;
      imageSource = imageIcon.getImage();      
      imageBuffer = createImage(imageIcon.getIconWidth(), imageIcon.getIconHeight());
      if(imageSource != null) {
        Graphics g = imageBuffer.getGraphics();
        g.clearRect(0, 0, this.getWidth(), this.getHeight()); //I like this method its very convenient.8.19.05
        g.drawImage (imageSource, 0, 0, imageIcon.getIconWidth(), 
      	  							    imageIcon.getIconHeight(), this);
        g.dispose();
        width = imageIcon.getIconWidth();
        height = imageIcon.getIconHeight();
      }
      //this.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
    } else {
      //first open or closes a file
      imageSource = null;
      imageBuffer = null;
      repaint();
    }  
    //setBounds(0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());    
  }
   
  /**
   * Returns the current Image drawn on the panel.
   **/
  public Image getCurrentImage() {
    return imageBuffer;
  }
  
  /**
   * Invoked by swing to draw components. Draw the image
   * on the panel. Should not invoked paint directly, but used the repaint method
   * to schedule the component redrawing instead.
   **/
  public void paint(Graphics g) {
    if (imageBuffer != null) {      
      Rectangle imgRect = getBounds();
      g.clearRect(0, 0, this.getWidth(), this.getHeight()); //I like this method its very convenient
      g.drawImage (imageBuffer, 0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight(), this);
    } else { //now we remove the unnecessary *@#$%%^@!^& figures that sucks 8.2.05
      g.clearRect(0, 0, this.getWidth(), this.getHeight()); //I like this method its very convenient.8.19.05
      //System.out.println("ImageCanvas: Clears the drawing board.");
    }
  }
  
  /**
   * Calls paint. Doesn't clear the background.
   **/  
  public void update(Graphics g) {
    paint(g);
  }
  
  
  /**
   * Returns the width of the image.
   **/
  public int returnWidth() {
    return width;
  }
  
  /**   
   * Returns the height of the image.
   **/
  public int returnHeight() {
    return height;
  }
  
} //end ImageCanvas

//****************I's Iori Yoshizuki x Evangelion*********************//