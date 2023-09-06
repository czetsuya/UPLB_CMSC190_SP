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
// This file is actually collection of several classes that //
// are called by the VisualCryptography class to perform    //
// the desired functions. It includes save, open, close 	//
// actions perform to an image so as the exit window action.//
// I found this file convenient for editing since the main  //
// class is t00 deep.										//
/***********************************************************/

// The public where the base classes are stored
package visualcryptography;

import visualcryptography.canvas.ImageCanvas;
import visualcryptography.codec.gif.MyGif;
import visualcryptography.filefilter.ExampleFileFilter;
import visualcryptography.imageencryptor.ImageEncryptor;
import visualcryptography.matrix.MatrixConstructor;
import visualcryptography.VisualCryptography;

// Sumimases, but I hate importing the whole public so please bare with me
// Imported from the expansion public
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// The classes accessible to the original public
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

// Imported from the sourceforge community
import net.sourceforge.jiu.codecs.UnsupportedCodecModeException;
import net.sourceforge.jiu.ops.OperationFailedException;
import net.sourceforge.jiu.ops.MissingParameterException;
import net.sourceforge.jiu.ops.Operation;
import net.sourceforge.jiu.ops.WrongParameterException;

  /**
   * SaveAction class is responsible in saving the image on the drawing board
   * to an external file specified in the JFileChooser.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/
  class SaveAction extends AbstractAction {
  	/**
  	 * An instance of the main class for variable reference
  	 **/
  	private VisualCryptography vc = null;
  	/**
  	 * Brings up the file chooser box.
  	 **/
  	private JFileChooser chooser = null;
  	/**
  	 * Filters the file chooser box.
  	 **/
    private ExampleFileFilter filter = null;
    
    /**
     * Creates an instance of OpenAction class.
     **/  	
  	public SaveAction(VisualCryptography vc) {
  	  super("Save Action");
  	  this.vc = vc;
  	  filter = new ExampleFileFilter();
	  filter.addExtension("gif");
      filter.setDescription("Gif Images");
      chooser = new JFileChooser();
      chooser.setFileFilter(filter);
      chooser.setApproveButtonText("Save");
      chooser.setApproveButtonMnemonic('S');
      chooser.setDialogTitle("Save Image As...");
      chooser.setDialogType(JFileChooser.SAVE_DIALOG);      
      chooser.setCurrentDirectory(new File(vc.getResourceString("externalPath")));
  	}
  	
  	public void actionPerformed(ActionEvent e) {  	  
      int returnVal = chooser.showSaveDialog(vc.getFrame());
      
      if(returnVal == JFileChooser.APPROVE_OPTION) { //ok button pressed
        if(null == chooser.getSelectedFile().toString()) {
      	  vc.setStatus("Cannot save a null image.");
          return;
        }
        vc.filename = chooser.getSelectedFile();
        
        //check if the file ends with a .gif
        if(new String(vc.filename.toString().substring(vc.filename.toString().length() - 3, vc.filename.toString().length())).equals("gif") == false) {
	      StringBuffer s = new StringBuffer(vc.filename.toString());
	      s.append(".gif");
	      vc.filename = new File(s.toString());
	    }
        
        try { //Try saving the image drawn on the draw panel
          vc.currentGifImage = new MyGif(vc.fileManager.getFileAt(vc.currentTab).toString());
	      vc.currentGifImage.saveGifImage(vc.filename.toString());
    	} catch(IOException ie) { //Exceptions handled
      	  System.out.println("Exception " + ie);
      	  vc.setStatus("Uncaught Exception : " + ie);
      	} catch(UnsupportedCodecModeException ucme) {
      	  System.out.println("Exception " + ucme);
      	  vc.setStatus("Uncaught Exception : " + ucme);
      	} catch(MissingParameterException mpe) {
      	  System.out.println("Exception " + mpe);
      	  vc.setStatus("Uncaught Exception : " + mpe);
      	} catch(OperationFailedException ofe) {
      	  System.out.println("Exception " + ofe);
      	  vc.setStatus("Uncaught Exception : " + ofe);
      	}
      	vc.tabbedPane.setTitleAt(vc.currentTab, chooser.getSelectedFile().getName());
      	vc.imageCanvas[vc.currentTab].setImage(new ImageIcon(vc.filename.toString()));
      	vc.imageCanvas[vc.currentTab].repaint();
      	vc.setStatus("Saving " + vc.filename.toString() + "...");
      	System.out.println("Saving " +  vc.filename.toString() + "...");
      } else //ok button pressed
        return; //cancel
  	} //end actionPerformed
  } //end SaveAction
  
  /**
   * CoalesceAction class is responsible in making the coalesce image
   * from the n shares specified this action removes the noise.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/
  class CoalesceAction extends AbstractAction {
  	/**
  	 * Instance of the VisualCryptography class for variable reference.
  	 **/
  	private VisualCryptography vc = null;
  	/**
  	 * The file chooser box.
  	 **/
  	private JFileChooser chooser = null;
  	/**
  	 * Filters the file types of file chooser box.
  	 **/
  	private ExampleFileFilter filter = null;
  	/**
  	 * Filenames of shares or participants.
  	 **/
  	private Vector shareNames = null;
  	/**
  	 * Gateway for encryption and decryption of image.
  	 **/
  	private ImageEncryptor imageEncryptor = null;
  	/**
  	 * The number of shares. Inputted by the user.
  	 **/
  	private int nShares = 0;
  	/**
  	 * Pointer to the currently open file.
  	 **/
  	private File f = null;
    
    /**
     * Creates an instance of the CoalesceAction class.
     **/
    public CoalesceAction(VisualCryptography vc) {
      super("Coalesce Action");
      this.vc = vc;
      shareNames = new Vector();
	  filter = new ExampleFileFilter();
	  filter.addExtension("gif");
      filter.setDescription("Gif Images");
      chooser = new JFileChooser();
      chooser.setFileFilter(filter);
      chooser.setCurrentDirectory(new File(vc.getResourceString("externalPath")));
    }
    
    public void actionPerformed(ActionEvent e) {
      String n = JOptionPane.showInputDialog(null, "Enter the number of shares to combine n = ", 
      							  		    "Coalesce Shares", JOptionPane.PLAIN_MESSAGE);
	  if(n == null) {	  	
	  	vc.imageCanvas[vc.canvasIndex].repaint();
	  	System.out.println("Coalesce: Null");
	  	vc.setStatus("Number of shares cannot be null.");
	    return;
	  }
	  
	  try { //test if the input is an integer	    
	    nShares = Integer.parseInt(n);
	  
	    if(nShares < 1) {
		  JOptionPane.showMessageDialog(vc.getFrame(), "Positive number required.", "Coalesce Error", JOptionPane.WARNING_MESSAGE);	  	
		  vc.setStatus("Fatal Error. Positive Numeric Data Type Required.");
		  System.out.println("Error negative nShares.");
	      return;
	    }
	    
	    for(int i = 0; i < nShares; i++) {
	  	  chooser.setDialogTitle("Select share: " + (i + 1));
          int returnVal = chooser.showOpenDialog(vc.getFrame());
          if(returnVal == JFileChooser.APPROVE_OPTION) {
      	    //vc.filename = chooser.getSelectedFile(); //input files from the user
      	    //shareNames.add(i, vc.filename.toString());
      	    shareNames.add(i, chooser.getSelectedFile().toString());
      	  } else {
      	  	vc.setStatus("Coalesce image has been cancelled.");
      	    return;
      	  }
        }
	  
	    //create a new instance of ImageEncryptor with n participants	  
        imageEncryptor = new ImageEncryptor(vc, nShares); //create a new imageEncrytor with n shares
	  
	    try {
          imageEncryptor.coalesceShares(shareNames); //superimpose the shares
        } catch(IOException ie) { //Exceptions handles        
      	  System.out.println("Exception: " + ie);
      	  vc.setStatus("Uncaught Exception : " + ie);
        } catch(UnsupportedCodecModeException ucme) {
      	  System.out.println("Exception: " + ucme);
      	  vc.setStatus("Uncaught Exception : " + ucme);
        } catch(MissingParameterException mpe) {
      	  System.out.println("Exception: " + mpe);
      	  vc.setStatus("Uncaught Exception : " + mpe);
        } catch(OperationFailedException ofe) {
          System.out.println("Exception: " + ofe);
          vc.setStatus("Uncaught Exception : " + ofe);
        }
        
        if((vc.fileManager.getFileAt(vc.currentTab)) == null || (vc.tabbedPane.getTitleAt(vc.currentTab) == "null")) { //first call
          vc.canvasIndex = vc.fileManager.lastSelectedIndex;
          vc.currentTab = 0;
          f = new File(vc.getResourceString("externalPath") + "coalesce.gif");
          vc.fileManager.addFile(f.toString());
          vc.tabbedPane.setForegroundAt(vc.currentTab, Color.blue);
    	} else
          vc.canvasIndex = vc.fileManager.getSelectedCanvasIndex(vc.fileManager.getFileAt(vc.currentTab).toString());
        f = new File(vc.getResourceString("externalPath") + "/temp/coalesce_" + vc.randomNum + ".gif");
        vc.imageCanvas[vc.canvasIndex].setImage(new ImageIcon(f.toString()));
        vc.imageCanvas[vc.canvasIndex].repaint();
        vc.fileManager.insertFileAt(vc.currentTab, f.toString());
        f = null;
        vc.tabbedPane.setTitleAt(vc.currentTab, "coalesce.gif");
        vc.setStatus("Coalesce the shares.");
        
	  } catch(NumberFormatException nfe) { //Catch if the user input a string or char
	  	vc.setStatus("Numeric data type required.");
	  } //need integer
	  imageEncryptor = null;
    } //end actionPerformed
  } //end CoalesceAction
  
  
  /**
   * CoalesceAction class is responsible in making the coalesce image
   * from the n shares specified. This action retains the noise for visual printing
   * purposes.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/
  class CoalesceWithNoiseAction extends AbstractAction {
  	/**
  	 * Instance of the VisualCryptography class for variable reference.
  	 **/
  	private VisualCryptography vc = null;
  	/**
  	 * The file chooser box.
  	 **/
  	private JFileChooser chooser = null;
  	/**
  	 * Filters the file types of file chooser box.
  	 **/
  	private ExampleFileFilter filter = null;
  	/**
  	 * Filenames of shares or participants.
  	 **/
  	private Vector shareNames = null;
  	/**
  	 * Gateway for encryption and decryption of image.
  	 **/
  	private ImageEncryptor imageEncryptor = null;
  	/**
  	 * The number of shares. Inputted by the user.
  	 **/
  	private int nShares = 0;
  	/**
  	 * Pointer to the currently open file.
  	 **/
  	private File f = null;
    
    /**
     * Creates an instance of the CoalesceAction class.
     **/
    public CoalesceWithNoiseAction(VisualCryptography vc) {
      super("Coalesce With Noise Action");
      this.vc = vc;
      shareNames = new Vector();
	  filter = new ExampleFileFilter();
	  filter.addExtension("gif");
      filter.setDescription("Gif Images");
      chooser = new JFileChooser();
      chooser.setFileFilter(filter);
      chooser.setCurrentDirectory(new File(vc.getResourceString("externalPath")));
    }
    
    public void actionPerformed(ActionEvent e) {
      String n = JOptionPane.showInputDialog(null, "Enter the number of shares to combine n = ", 
      							  		    "Coalesce Shares", JOptionPane.PLAIN_MESSAGE);
	  if(n == null) {	  	
	  	vc.imageCanvas[vc.canvasIndex].repaint();
	  	System.out.println("Coalesce: Null");
	  	vc.setStatus("Number of shares cannot be null.");
	    return;
	  }
	  
	  try { //test if the input is an integer	    
	    nShares = Integer.parseInt(n);
	  
	    if(nShares < 1) {
		  JOptionPane.showMessageDialog(vc.getFrame(), "Positive number required.", "Coalesce Error", JOptionPane.WARNING_MESSAGE);	  	
		  vc.setStatus("Fatal Error. Positive Numeric Data Type Required.");
		  System.out.println("Error negative nShares.");
	      return;
	    }
	    
	    for(int i = 0; i < nShares; i++) {
	  	  chooser.setDialogTitle("Select share: " + (i + 1));
          int returnVal = chooser.showOpenDialog(vc.getFrame());
          if(returnVal == JFileChooser.APPROVE_OPTION) {
      	    //vc.filename = chooser.getSelectedFile(); //input files from the user
      	    //shareNames.add(i, vc.filename.toString());
      	    shareNames.add(i, chooser.getSelectedFile().toString());
      	  } else {
      	  	vc.setStatus("Coalesce image has been cancelled.");
      	    return;
      	  }
        }
	  
	    //create a new instance of ImageEncryptor with n participants	  
        imageEncryptor = new ImageEncryptor(vc, nShares); //create a new imageEncrytor with n shares
	  
	    try {
          imageEncryptor.coalesceSharesWithNoise(shareNames); //superimpose the shares
        } catch(IOException ie) { //Exceptions handles        
      	  System.out.println("Exception: " + ie);
      	  vc.setStatus("Uncaught Exception : " + ie);
        } catch(UnsupportedCodecModeException ucme) {
      	  System.out.println("Exception: " + ucme);
      	  vc.setStatus("Uncaught Exception : " + ucme);
        } catch(MissingParameterException mpe) {
      	  System.out.println("Exception: " + mpe);
      	  vc.setStatus("Uncaught Exception : " + mpe);
        } catch(OperationFailedException ofe) {
          System.out.println("Exception: " + ofe);
          vc.setStatus("Uncaught Exception : " + ofe);
        }
        
        if((vc.fileManager.getFileAt(vc.currentTab)) == null || (vc.tabbedPane.getTitleAt(vc.currentTab) == "null")) { //first call
          vc.canvasIndex = vc.fileManager.lastSelectedIndex;
          vc.currentTab = 0;
          f = new File(vc.getResourceString("externalPath") + "coalesce.gif");
          vc.fileManager.addFile(f.toString());
          vc.tabbedPane.setForegroundAt(vc.currentTab, Color.blue);
    	} else
          vc.canvasIndex = vc.fileManager.getSelectedCanvasIndex(vc.fileManager.getFileAt(vc.currentTab).toString());
        f = new File(vc.getResourceString("externalPath") + "/temp/coalesce_" + vc.randomNum + ".gif");
        vc.imageCanvas[vc.canvasIndex].setImage(new ImageIcon(f.toString()));
        vc.imageCanvas[vc.canvasIndex].repaint();
        vc.fileManager.insertFileAt(vc.currentTab, f.toString());
        f = null;
        vc.tabbedPane.setTitleAt(vc.currentTab, "coalesce.gif");
        vc.setStatus("Coalesce the shares.");
        
	  } catch(NumberFormatException nfe) { //Catch if the user input a string or char
	  	vc.setStatus("Numeric data type required.");
	  } //need integer
	  imageEncryptor = null;
    } //end actionPerformed
  } //end CoalesceWithNoiseAction
  
  /**
   * EncryptAction class the class responsible on choosing the vcs and
   * write method of shares it also changes the draw panel to the first
   * share of the encrypted image.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/
  class EncryptAction extends AbstractAction {
  	/**
  	 * Instance of the VisualCryptography class for variable reference.
  	 **/
  	private VisualCryptography vc;
  	private MatrixConstructor matrixConstructor = new MatrixConstructor();
  	private ImageEncryptor imageEncryptor;  	
  	private File f = null;
  	
  	/**
     * Creates an instance of EncryptAction class.
     **/  	
    public EncryptAction(VisualCryptography vc) {
      super("Encrypt Action");      
      this.vc = vc;      
    }
    
    public void actionPerformed(ActionEvent e) {
      f = new File(vc.getResourceString("externalPath") + "/temp");
      try {
      	f.mkdir();
      } catch(SecurityException se) {}
      f = null;
      try { //Set the currentImage for encrypting
	 	vc.currentGifImage = new MyGif(vc.fileManager.getFileAt(vc.currentTab).toString());
	 	System.out.println(vc.fileManager.getFileAt(vc.currentTab).toString());
	  } catch(IOException ie) { //Exceptions handled
	    vc.currentGifImage = null;
	  	System.out.println("Exception " + ie);
	  	vc.setStatus("Uncaught Exception : " + ie);
	  } catch(UnsupportedCodecModeException ucme) {
	  	vc.currentGifImage = null;
	  	System.out.println("Exception " + ucme);
	    vc.setStatus("Uncaught Exception : " + ucme);
	  } catch(MissingParameterException mpe) {
	  	vc.currentGifImage = null;
	  	System.out.println("Exception " + mpe);
	    vc.setStatus("Uncaught Exception : " + mpe);
	  } catch(OperationFailedException ofe) {
	  	vc.currentGifImage = null;
	  	System.out.println("Exception " + ofe);
	    vc.setStatus("Uncaught Exception : " + ofe);
	  } catch(Exception exc) {
	  	vc.currentGifImage = null;
	  	System.out.println("Exception " + exc);
	  	vc.setStatus("Uncaught Exception : " + exc);
	  }
      
      if(vc.currentGifImage != null) {
      	String strShares = null;
        switch(vc.vcsScheme.getSelectedIndex()) { //Encryption Schemes
      	  case 0 : //The 2x2 Encryption Scheme      	    
      	    vc.c0 = matrixConstructor.gen2x2WhiteBasis();
 	 	    vc.c1 = matrixConstructor.gen2x2BlackBasis();
 	 	    imageEncryptor = new ImageEncryptor(vc, 2); //I hardcode 2 since its constant
 	 	    System.out.println("2x2");
      	  break;
      	  case 1 :      	    
      	    strShares = JOptionPane.showInputDialog(vc.getFrame(), "Enter the number of shares n = ", "Encrypt the image", JOptionPane.PLAIN_MESSAGE);
      	    if(strShares == null) {
	  		  System.out.println("Encrypt: Null");
	  		  vc.setStatus("Number of participants cannot be null.");
	    	  return;
      	    } else { //input not null
      	      try { //make sure the input is numeric data type
      	      	int nShares = Integer.parseInt(strShares);
      	        vc.c0 = matrixConstructor.gen2xNWhiteBasis(nShares);
 	 	      	vc.c1 = matrixConstructor.gen2xNBlackBasis(nShares);
 	 	      	imageEncryptor = new ImageEncryptor(vc, nShares);
 	 	      } catch(NumberFormatException nfe) {
 	 	      	vc.setStatus("Numeric data type required.");
 	 	      } //catch not numeric input
 	 		} //input not null
 	 		System.out.println("2xn");
      	  break;
      	  case 2 :
      	    strShares = JOptionPane.showInputDialog(vc.getFrame(), "Enter the number of shares n = ", "Encrypt the image", JOptionPane.PLAIN_MESSAGE);
      	    if(strShares == null) {
	  		  System.out.println("Encrypt: Null");
	  		  vc.setStatus("Number of participants cannot be null.");
	    	  return;
      	    } else { //input not null
      	      try { //make sure the input is numeric data type
      	      	int nShares = Integer.parseInt(strShares);
      	        vc.c0 = matrixConstructor.genNxNWhiteBasis(nShares);
 	 	      	vc.c1 = matrixConstructor.genNxNBlackBasis(nShares);
 	 	      	imageEncryptor = new ImageEncryptor(vc, nShares);
 	 	      } catch(NumberFormatException nfe) {
 	 	      	vc.setStatus("Numeric data type required.");
 	 	      } //catch not numeric input
 	 		} //input not null
 	 		System.out.println("nxn");
      	  break;      	
        }
      
        switch(vc.writeShareMethod.getSelectedIndex()) { //Pixel writing methods
    	  case 0 : //Random Basis Column
    	    try {
    	      imageEncryptor.randomBasisColumn(vc.c0, vc.c1, vc.currentGifImage);
    	    } catch(IOException ie) {
        	  System.out.println("Exception: " + ie);
        	  vc.setStatus("Uncaught Exception : " + ie);
            } catch(UnsupportedCodecModeException ucme) {
      		  System.out.println("Exception: " + ucme);
      		  vc.setStatus("Uncaught Exception : " + ucme);
      	    } catch(MissingParameterException mpe) {
      		  System.out.println("Exception: " + mpe);
      		  vc.setStatus("Uncaught Exception : " + mpe);
      	    } catch(OperationFailedException ofe) {
      		  System.out.println("Exception: " + ofe);
      		  vc.setStatus("Uncaught Exception : " + ofe);
      	    }
    	  break;    	  
        }      
        
        //Sets the visible image on the drawing panel        
        vc.canvasIndex = vc.fileManager.getSelectedCanvasIndex(vc.fileManager.getFileAt(vc.currentTab).toString());        
        f = new File(vc.getResourceString("externalPath") + "/temp/share1_" + vc.randomNum + ".gif");
        vc.imageCanvas[vc.canvasIndex].setImage(new ImageIcon(f.toString()));        
        vc.imageCanvas[vc.canvasIndex].repaint();
        vc.tabbedPane.setTitleAt(vc.currentTab, "share1.gif");
        vc.fileManager.insertFileAt(vc.currentTab, f.toString());
        vc.setStatus("Encrypting image");
        f = null;
        System.out.println("Encrypting the image: ");
        
      } else {
      	vc.setStatus("Open a source image first.");
        JOptionPane.showMessageDialog(vc.getFrame(), "Please Open a Source Image first.", "Encryption Error", JOptionPane.WARNING_MESSAGE);
      } //no open image available for encryption
      imageEncryptor = null;
      try {
        new File(vc.getResourceString("externalPath") + "coalesce.gif").delete();
        System.out.println("*********************");
      } catch(SecurityException se) {}
    } //end actionPerformed
  } //EncryptAction
  
  /**
   * CloseAction Class Closing the file, updates the screen to null or white.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/  
  class CloseAction extends AbstractAction {  	
    /**
  	 * Instance of the VisualCryptography class for variable reference.
  	 **/
  	private VisualCryptography vc;
  	
  	/**
     * Creates an instance of CloseAction class.
     **/    
    public CloseAction(VisualCryptography vc) {
      super("CloseAction");
      this.vc = vc;
    }
    
    public void actionPerformed(ActionEvent e) {
      if((vc.tabbedPane.getTabCount() == 1) && (vc.tabbedPane.getTitleAt(vc.currentTab) == "null")) {
      	vc.setStatus("No more file is open. None not even one. :-)");
      } else if(vc.tabbedPane.getTabCount() == 1) {      	      	
      	vc.canvasIndex = vc.fileManager.getSelectedCanvasIndex(vc.fileManager.getFileAt(vc.currentTab).toString());
      	vc.fileManager.reHash(vc.canvasIndex);
      	vc.fileManager.delFile();
      	vc.imageCanvas[vc.canvasIndex].imageBuffer = null;
      	vc.imageCanvas[vc.canvasIndex].repaint();
      	vc.tabbedPane.setForegroundAt(vc.currentTab, Color.black);
      	vc.tabbedPane.setTitleAt(vc.currentTab, "null");      	
      	vc.setStatus("File has been closed successfully.");
      } else {
      	vc.canvasIndex = vc.fileManager.getSelectedCanvasIndex(vc.fileManager.getFileAt(vc.currentTab).toString());
      	vc.fileManager.reHash(vc.canvasIndex);      	
      	vc.imageCanvas[vc.canvasIndex] = null;      	
        vc.fileManager.delFileAt(vc.currentTab);                
        vc.tabbedPane.remove(vc.currentTab);
   		vc.tabbedPane.setSelectedIndex(vc.tabbedPane.getTabCount() - 1);   		   		
   		vc.setStatus("File has been closed successfully.");
      } //end else of tabCount == 1
      System.out.println("CloseIndex: " + vc.canvasIndex);
      vc.currentGifImage = null;
    } //end public
  } //CloseAction
  
  /**
   * CloseAllAction Closes all the open file in an instant.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/  
  class CloseAllAction extends AbstractAction {
    /**
  	 * Instance of the VisualCryptography class for variable reference.
  	 **/
  	private VisualCryptography vc;

	/**
     * Creates an instance of CloseAllAction class.
     **/    
    public CloseAllAction(VisualCryptography vc) {
      super("CloseAllAction");
      this.vc = vc;
    }
    
    public void actionPerformed(ActionEvent e) {	  
	  if((vc.tabbedPane.getTabCount() == 1) && (vc.tabbedPane.getTitleAt(vc.currentTab) == "null")) {
	    vc.setStatus("There is no open file to be close.");
	  } else {	  	
	  	int ans = JOptionPane.showConfirmDialog(vc.getFrame(), 
	  	"Warning close all the files without prompting", "Close all the open files", 
	  	JOptionPane.YES_NO_OPTION);
	  
	    if(ans == JOptionPane.NO_OPTION) { //do nothing just return
	  	  return;
	    } else if (ans == JOptionPane.YES_OPTION) { //close without asking
	      
	      for(int i = 0; i < vc.maxTab; i++)
	        if(vc.imageCanvas[i] != null)
	          vc.imageCanvas[i] = null;
	          
	      vc.fileManager.resetVariables();
	      vc.tabbedPane.removeAll();
	      
	      vc.fileManager.lastSelectedIndex = vc.currentTab = 0;
	      vc.canvasIndex = -1;
	      vc.imageCanvas[vc.currentTab] = new ImageCanvas(vc);
		  vc.imageCanvas[vc.currentTab].setPreferredSize(new Dimension(	Integer.parseInt(vc.getResourceString("dimensionW")),
											   			   		    	Integer.parseInt(vc.getResourceString("dimensionH"))));
		  JScrollPane scrollPane = new JScrollPane();
		  scrollPane.getViewport().add(vc.imageCanvas[vc.currentTab]);
		  vc.tabbedPane.addTab("null", scrollPane);
		  vc.currentGifImage = null;
	      vc.tabbedPane.setTitleAt(vc.currentTab, "null");
	      vc.setStatus("All files has been successfully closed.");
	    } //end close all
	  } //end check for open file	  
    } //end actionPerformed
  } //CloseAllAction
  
  /**
   * OpenAction, it then calls the paint method from the ImageCanvas
   * and updates the screen.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/
  class OpenAction extends AbstractAction {
  	/**
  	 * Instance of the VisualCryptography class for variable reference.
  	 **/
    private VisualCryptography vc = null;
    /**
     * The file chooser box.
     **/
    private JFileChooser chooser = null;    
    /**
     * Filters the file types of file chooser box.
     **/
    private ExampleFileFilter filter = null;
    /**
     * Used when the image size is greater than the dimension of the screen.
     **/
    private JScrollPane scrollPane = null;
    
    /**
     * Creates an instance of OpenAction class.
     **/    
    public OpenAction(VisualCryptography vc) {
      super("Open Action");
	  this.vc = vc;
	  filter = new ExampleFileFilter();
	  chooser = new JFileChooser();
	  filter.addExtension("gif");
      filter.setDescription("Gif Images");
      chooser.setFileFilter(filter);
      chooser.setDialogType(JFileChooser.OPEN_DIALOG);
      chooser.setDialogTitle("Select the Source Image...");
	  chooser.setCurrentDirectory(new File(vc.getResourceString("externalPath")));	  
    }
    
    public void actionPerformed(ActionEvent e) {      
      int returnVal = chooser.showOpenDialog(vc.getFrame());
   
      if(returnVal == JFileChooser.APPROVE_OPTION) { //if ok button pressed
        vc.filename = chooser.getSelectedFile();
        String s = new String(vc.filename.toString().substring(vc.filename.toString().length() - 3, vc.filename.toString().length()));
        
        if((s.equals("gif") == false) && (s.equals("jpg") == false)) {
          System.out.println("File Extension: " + s);
          vc.setStatus("Unsupported file format. Gif format only.");
	    } else { //supported
	      System.out.println("You chose to open this file: " +
          					  chooser.getSelectedFile().getName());
	      if(vc.tabbedPane.getTitleAt(vc.currentTab) == "null") { //open the first image	        
      	    vc.tabbedPane.setTitleAt(vc.currentTab, chooser.getSelectedFile().getName());
      	    vc.tabbedPane.setForegroundAt(vc.currentTab, Color.blue);
      	    vc.canvasIndex = vc.fileManager.lastSelectedIndex;
            vc.imageCanvas[vc.canvasIndex].setImage(new ImageIcon(vc.filename.toString()));
            vc.imageCanvas[vc.canvasIndex].repaint();
            vc.fileManager.addFile(vc.filename.toString());//why here
      	    vc.setStatus("Opening the file " + vc.filename + ".");
      	    System.out.println("ActionClasses: firstAddImage");
      	  } else if(vc.tabbedPane.getTabCount() >= vc.maxTab) { //close then open image
      	    if(vc.fileManager.v.contains(vc.filename.toString())) { //check if the file is already open
      	      vc.setStatus("The file " + vc.filename.toString() + " has already been opened");
      	    } else {
      	      System.out.println("OpenAction: tabCount > 4: " + vc.currentTab);
      	      vc.canvasIndex = vc.fileManager.getAvailableIndex();
      	      vc.tabbedPane.remove(vc.currentTab);
      	      vc.imageCanvas[vc.canvasIndex] = new ImageCanvas(vc);
			
			  vc.imageCanvas[vc.canvasIndex].setPreferredSize(new Dimension(new ImageIcon(vc.filename.toString()).getIconWidth(),
											   		  				     new ImageIcon(vc.filename.toString()).getIconHeight()));
			  scrollPane = new JScrollPane();
			  scrollPane.getViewport().add(vc.imageCanvas[vc.canvasIndex]);
			  vc.tabbedPane.addTab(chooser.getSelectedFile().getName(), scrollPane);
      	      vc.imageCanvas[vc.canvasIndex].setImage(new ImageIcon(vc.filename.toString()));
              vc.imageCanvas[vc.canvasIndex].repaint();
            
              vc.fileManager.addFile(vc.filename.toString());//i need to add the file first to
              vc.tabbedPane.setSelectedIndex(vc.currentTab + 1);
              vc.setStatus("Opening the file " + vc.filename + ".");
              System.out.println("ActionClasses: removeThenAddImage");            
            } //else
      	  } else { //just open an image      	    
      	    if(vc.fileManager.v.contains(vc.filename.toString())) { //check if the file is already open
      	      vc.setStatus("The file " + vc.filename.toString() + " has already been opened");
      	    } else {
      	      vc.canvasIndex = vc.fileManager.getAvailableIndex();      	    
      	      vc.imageCanvas[vc.canvasIndex] = new ImageCanvas(vc);      	    
			  vc.imageCanvas[vc.canvasIndex].setPreferredSize(new Dimension(new ImageIcon(vc.filename.toString()).getIconWidth(),
											   		  				     	new ImageIcon(vc.filename.toString()).getIconHeight()));
			
			  scrollPane = new JScrollPane();
			  scrollPane.getViewport().add(vc.imageCanvas[vc.canvasIndex]);
			
			  vc.tabbedPane.addTab(chooser.getSelectedFile().getName(), scrollPane);
      	      vc.imageCanvas[vc.canvasIndex].setImage(new ImageIcon(vc.filename.toString()));
              vc.imageCanvas[vc.canvasIndex].repaint();
              vc.fileManager.addFile(vc.filename.toString());//the fileManager b4 querying
              vc.tabbedPane.setSelectedIndex(vc.tabbedPane.getTabCount() - 1);
      	      vc.setStatus("Opening the file " + vc.filename + ".");
      	      System.out.println("ActionClasses: justAddImage");
      	   } //end check open file
      	  } // end open
    	} //file supported    	
      } //end approveOption      
	} //end actionPerformed
  } //end OpenAction
  
  /**
   * Shutdown the application check if there is an open image.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/
  final class VCCloser extends WindowAdapter {
  	/**
  	 * Instance of the VisualCryptography class for variable reference.
  	 **/
  	private VisualCryptography vc = null;
  	/**
     * The file chooser box.
     **/
    private JFileChooser chooser = null;    
    /**
     * Filters the file types of file chooser box.
     **/
    private ExampleFileFilter filter = null;
	
	/**
     * Creates an instance of VCCloser class.
     **/  	
  	public VCCloser(VisualCryptography vc) {  	  
  	  this.vc = vc;  	
  	}
	
    public void windowClosing(WindowEvent e) {
	  if(vc.tabbedPane.getTitleAt(0) != "null") {
        int ans = JOptionPane.showConfirmDialog(vc.getFrame(), "Do you want to save the image/s?", "There is/are image/s on the canvas", JOptionPane.YES_NO_CANCEL_OPTION);
        if(ans == JOptionPane.NO_OPTION) {
          System.out.println("VCCloser.Cancel");
          System.exit(0);
	    } else if(ans == JOptionPane.YES_OPTION) {
	      filter = new ExampleFileFilter();
	  	  filter.addExtension("gif");
      	  filter.setDescription("Gif Images");
      	  chooser = new JFileChooser();
	      chooser.setFileFilter(filter);
	      chooser.setApproveButtonText("Save");
	      chooser.setApproveButtonMnemonic('S');
	      chooser.setDialogTitle("Save Image As...");
	      chooser.setDialogType(JFileChooser.SAVE_DIALOG);      
	      chooser.setCurrentDirectory(new File(vc.getResourceString("externalPath")));
	  	
	      while(vc.tabbedPane.getTabCount() != 0) { //loop while there is an open image
	  	  
	        int returnVal = chooser.showSaveDialog(vc.getFrame());      
	      
	      	if(returnVal == JFileChooser.APPROVE_OPTION) { //ok button pressed
	          if(null == chooser.getSelectedFile().toString()) {
	      	    vc.setStatus("Cannot save a null image.");
	            return;
	          }
	        
	          vc.filename = chooser.getSelectedFile();
	        
	          //check if the file ends with a .gif else append one
	          if(new String(vc.filename.toString().substring(vc.filename.toString().length() - 3, vc.filename.toString().length())).equals("gif") == false) {
		        StringBuffer s = new StringBuffer(vc.filename.toString());
		        s.append(".gif");
		        vc.filename = new File(s.toString());
		      }
	       
	          try { //Try saving the image drawn on the draw panel
	            vc.currentGifImage = new MyGif(vc.fileManager.getFileAt(vc.currentTab).toString());
	            vc.currentGifImage.saveGifImage(vc.filename.toString());
	    	  } catch(IOException ie) { //Exceptions handled
	      	    System.out.println("Exception " + ie);
	      	    vc.setStatus("Uncaught Exception : " + ie);
	      	  } catch(UnsupportedCodecModeException ucme) {
  	      	    System.out.println("Exception " + ucme);
	      	    vc.setStatus("Uncaught Exception : " + ucme);
	      	  } catch(MissingParameterException mpe) {
	      	    System.out.println("Exception " + mpe);
	      	    vc.setStatus("Uncaught Exception : " + mpe);
	      	  } catch(OperationFailedException ofe) {
	      	    System.out.println("Exception " + ofe);
	      	    vc.setStatus("Uncaught Exception : " + ofe);
	      	  }
	      	  
	      	  vc.canvasIndex = vc.fileManager.getSelectedCanvasIndex(vc.fileManager.getFileAt(vc.currentTab).toString());
	      	  vc.imageCanvas[vc.canvasIndex] = null;			
	      	  vc.setStatus("Saving " + vc.filename.toString() + "...");
	      	  vc.tabbedPane.remove(vc.currentTab);
	          vc.currentTab = vc.tabbedPane.getTabCount() - 1;
	      	  System.out.println("Saving " +  vc.filename.toString() + "...");	      	
	        } else //end ok button pressed
	          return; //cancel
	      } //end while tabCount is not 1
	      System.out.println("WindowEvent: An image has been saved");
	    } else { //an image has been opened
	      System.out.println("VCCloser.No");
	      return; //cancel save
	    }
	  } else { //no image
	  	System.out.println("WindowEvent.NoImageOpen");
	  } //end not null	  
	  
	  System.out.println("VCCloser: Save file");
	  vc.delTree(vc.getResourceString("externalPath") + "/temp");
	  
	} //end WindowEvent
  } //end VCCloser
  
  /**
   * ExitAction Class for Exit, when exit is chosen from the menu item.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/
  class ExitAction extends AbstractAction {
  	/**
  	 * Instance of the VisualCryptography class for variable reference.
  	 **/
    private VisualCryptography vc;
    /**
     * Filters the file chooser box.
     **/
    private ExampleFileFilter filter = null;
    /**
     * The file chooser box.
     **/
	private JFileChooser chooser = null;
    
    /**
     * Creates an instance of ExitAction class.
     **/
    public ExitAction(VisualCryptography vc) {      
      super("Exit Action");
	  this.vc = vc;	  
    }

    public void actionPerformed(ActionEvent e) {
      if(vc.tabbedPane.getTitleAt(0) != "null") {
        int ans = JOptionPane.showConfirmDialog(vc.getFrame(), "Do you want to save the image/s?", "There is/are image on the canvas", JOptionPane.YES_NO_CANCEL_OPTION);
        if(ans == JOptionPane.NO_OPTION) {
          System.out.println("Do not save.");
	    } else if(ans == JOptionPane.CANCEL_OPTION) {
	      System.out.println("ExitAction.Cancel");
	      return;		    
	    } else if(ans == JOptionPane.YES_OPTION) {
	      
	      filter = new ExampleFileFilter();
	  	  filter.addExtension("gif");
      	  filter.setDescription("Gif Images");
      	  chooser = new JFileChooser();
	      chooser.setFileFilter(filter);
	      chooser.setApproveButtonText("Save");
	      chooser.setApproveButtonMnemonic('S');
	      chooser.setDialogTitle("Save Image As...");
	      chooser.setDialogType(JFileChooser.SAVE_DIALOG);      
	      chooser.setCurrentDirectory(new File(vc.getResourceString("externalPath")));
	  	  
	  	  while(vc.tabbedPane.getTabCount() != 0) { //loop while there is an open image
	  	  
	        int returnVal = chooser.showSaveDialog(vc.getFrame());      
	      
	      	if(returnVal == JFileChooser.APPROVE_OPTION) { //ok button pressed
	          if(null == chooser.getSelectedFile().toString()) {
	      	    vc.setStatus("Cannot save a null image.");
	            return;
	          }
	        
	          vc.filename = chooser.getSelectedFile();
	        
	          //check if the file ends with a .gif else append one
	          if(new String(vc.filename.toString().substring(vc.filename.toString().length() - 3, vc.filename.toString().length())).equals("gif") == false) {
		        StringBuffer s = new StringBuffer(vc.filename.toString());
		        s.append(".gif");
		        vc.filename = new File(s.toString());
		      }
	       
	          try { //Try saving the image drawn on the draw panel
	            vc.currentGifImage = new MyGif(vc.fileManager.getFileAt(vc.currentTab).toString());
	            vc.currentGifImage.saveGifImage(vc.filename.toString());
	    	  } catch(IOException ie) { //Exceptions handled
	      	    System.out.println("Exception " + ie);
	      	    vc.setStatus("Uncaught Exception : " + ie);
	      	  } catch(UnsupportedCodecModeException ucme) {
  	      	    System.out.println("Exception " + ucme);
	      	    vc.setStatus("Uncaught Exception : " + ucme);
	      	  } catch(MissingParameterException mpe) {
	      	    System.out.println("Exception " + mpe);
	      	    vc.setStatus("Uncaught Exception : " + mpe);
	      	  } catch(OperationFailedException ofe) {
	      	    System.out.println("Exception " + ofe);
	      	    vc.setStatus("Uncaught Exception : " + ofe);
	      	  }
	      	  
	      	  vc.canvasIndex = vc.fileManager.getSelectedCanvasIndex(vc.fileManager.getFileAt(vc.currentTab).toString());
	      	  vc.imageCanvas[vc.canvasIndex] = null;			
	      	  vc.setStatus("Saving " + vc.filename.toString() + "...");
	      	  vc.tabbedPane.remove(vc.currentTab);
	          vc.currentTab = vc.tabbedPane.getTabCount() - 1;
	      	  System.out.println("Saving " +  vc.filename.toString() + "...");
	        } else//end ok button pressed
	          return; //cancel
	      } //end while tabCount is not 1
	      System.out.println("ExitAction: Save image");
	    } //end saving the image
	  } else { //no image
	  	System.out.println("There is no open image.");
	  } //end not null
	  
	  System.out.println("ExitAction.Exit");
	  vc.delTree(vc.getResourceString("externalPath") + "/temp");
	  	  
    } //end ActionEvent
  } //end ExitAction
  
  /**
   * AboutPanel - It initialize the about panel.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/
  class AboutPanel extends JPanel {
  	/**
  	 * Image that is put into the AboutBox objects.
  	 **/
    private ImageIcon aboutImage = null;
    /**
  	 * Instance of the VisualCryptography class for variable reference.
  	 **/
    private VisualCryptography vc = null;
    /**
     * Creates an instance of AboutPanel class.
     **/
    public AboutPanel(VisualCryptography vc) {
      this.vc = vc;
  	  aboutImage = vc.createImageIcon("About.jpg", "aboutBoxAccessibleDescription");
	  setOpaque(false);
    }

    public void paint(Graphics g) {
      aboutImage.paintIcon(this, g, 0, 0);
	  super.paint(g);
    }

    public Dimension getPreferredSize() {
      return new Dimension(aboutImage.getIconWidth(), aboutImage.getIconHeight());
    }
  } //end AboutPanel
  
  /**
   * Display information about the module.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/
  class AboutAction extends AbstractAction {
  	/**
  	 * Instance of the VisualCryptography class for variable reference.
  	 **/
    private VisualCryptography vc = null;
    /**
     * Holds information about the application.
     **/
    private JDialog aboutBox = null;
	
	/**
	 * Creates an instance of the AboutAction class.
	 **/
    public AboutAction(VisualCryptography vc) {
      super("About Action");
      this.vc = vc;      
    }
	
    public void actionPerformed(ActionEvent e) {
      System.out.println("About the author");
      if(aboutBox == null) {
	    // JPanel panel = new JPanel(new BorderLayout());
	    JPanel panel = new AboutPanel(vc);
	    panel.setLayout(new BorderLayout());

  	    aboutBox = new JDialog(vc.getFrame(), vc.getResourceString("aboutBoxTitle"), false);
	    aboutBox.getContentPane().add(panel, BorderLayout.CENTER);

        JPanel buttonpanel = new JPanel();
	    buttonpanel.setOpaque(false);
	    JButton button = (JButton) buttonpanel.add(new JButton(vc.getResourceString("aboutBoxOkButtonText")));
	    panel.add(buttonpanel, BorderLayout.SOUTH);
	    button.addActionListener(new OkAction(aboutBox));
	  } //end if
	  aboutBox.pack();
	  Point p = vc.getLocationOnScreen();
	  aboutBox.setLocation(p.x + 10, p.y +10);
	  aboutBox.show();
	  vc.setStatus("About the author.");
    } //end public
  } //end AboutAction

  /**
   * OkAction Class, when ok is push from the about panel.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/
  class OkAction extends AbstractAction {
  	/**
  	 * Holds data information about the application.
  	 **/
    private JDialog aboutBox = null;
    
    /**
     * Creates an instance of OkAction class.
     **/
    public OkAction(JDialog aboutBox) {
      super("Ok Action");
      this.aboutBox = aboutBox;
    }
    public void actionPerformed(ActionEvent e) {      
      aboutBox.setVisible(false);      
    }
  } //end OkAction
  
  /**
   * HelpAction Class - It sends out the help panel if there is???.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/
  class HelpAction extends AbstractAction {
  	/**
  	 * Instance of the VisualCryptography class for variable reference.
  	 **/
  	private VisualCryptography vc = null;  	
  	/**
  	 * A JDialog object box for helpbox.
  	 **/
  	private JDialog helpBox = null;
  	
  	/**
     * Creates an instance of HelpAction class.
     **/
    public HelpAction(VisualCryptography vc) {
      super("Help Action");
      this.vc = vc;
    }
	
    public void actionPerformed(ActionEvent e) {
      /*if(helpBox == null) {	    
	    JTextArea myLabel = new JTextArea();
	    JPanel panel = new JPanel();	
	    panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
	    
	    myLabel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
	    myLabel.setLineWrap(true);
	    myLabel.setText(vc.getResourceString("helpItemAccessibleDescription"));
	    myLabel.setForeground(Color.black);
	    myLabel.setBackground(Color.lightGray);
	    myLabel.setEditable(false);
	    
	    panel.setLayout(new BorderLayout());
	    panel.setSize(400, 400);
	    panel.add(myLabel);

  	    helpBox = new JDialog(vc.getFrame(), vc.getResourceString("helpBoxTitle"), false);
	    helpBox.getContentPane().add(panel, BorderLayout.CENTER);
	  } //end if
	  helpBox.pack();
	  Point p = vc.getLocationOnScreen();
	  helpBox.setLocation(p.x + 10, p.y +10);
	  helpBox.setSize(200, 200);
	  helpBox.show();*/
	  String msg = vc.getResourceString("helpItemAccessibleDescription");
	  String title = vc.getResourceString("helpBoxTitle");
	  JOptionPane.showMessageDialog(vc.getFrame(), msg, title, JOptionPane.INFORMATION_MESSAGE);
	  vc.setStatus("Help from the author.");
    } //end public
  } //end HelpAction
  
  /**
   * ChangeLookAndFeelAction - Look and Feel Class
   * The lazy of doing things instead of defining it to actionPerformed
   * This takes some of my time really.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/
  class ChangeLookAndFeelAction extends AbstractAction {
  	/**
  	 * Instance of the VisualCryptography class for variable reference.
  	 **/
	private VisualCryptography vc;
	/**
	 * String representation of the look and feel object.
	 **/
	String laf;
	
	/**
     * Creates an instance of ChangeLookAndFeelAction class.
     **/	
    public ChangeLookAndFeelAction(VisualCryptography vc, String laf) {
      super("Change Theme");
	  this.vc = vc;
	  this.laf = laf;
    }

    public void actionPerformed(ActionEvent e) {
      vc.setStatus("Setting Look and Feel to " + laf);
	  vc.setLookAndFeel(laf);
	}
  } //end ChangeLookAndFeel
  
  /**
   * Tablistener class is called when you navigate from one tab to another.
   * @author Edward P. Legaspi
   * @since 6.25.05
   **/
  class TabListener implements ChangeListener {
  	/**
  	 * Instance of the VisualCryptography class for variable reference.
  	 **/
  	private VisualCryptography vc = null;
  	/**
  	 * The last selected tab index of the JTabbedPane object.
  	 **/
  	private static int lastIndex;
  	
  	/**
     * Creates an instance of TabListener class.
     **/  	
  	public TabListener(VisualCryptography vc) {
  	  this.vc = vc;
  	  lastIndex = 0;
  	}
  	
	public void stateChanged(ChangeEvent e) {
	  if((vc.canvasIndex == -1) || (vc.filename == null) || (vc.tabbedPane.getSelectedIndex() == -1) || (vc.tabbedPane.getTabCount() == 0)) { //no tab yet
		//and i thought i would be h....... but here i am............		
		lastIndex = vc.canvasIndex = 0;
	  } else {
	  	vc.tabbedPane.setForegroundAt(lastIndex, Color.black);
	  	lastIndex = vc.currentTab = vc.tabbedPane.getSelectedIndex();	  	
	  	vc.tabbedPane.setForegroundAt(vc.currentTab, Color.blue);
	    vc.setStatus(vc.fileManager.getFileAt(vc.currentTab).toString());
	    System.out.println(vc.fileManager.getFileNames());
	    System.out.println(vc.currentTab + " Index:" + vc.fileManager.getSelectedCanvasIndex(vc.fileManager.getFileAt(vc.currentTab).toString()));	    
	  }
	} //end ChangeEvent
  } //end Tablistener

//**************Midori No Hibi x Samurai7 x Air x Grenadier***************//