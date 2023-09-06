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
// This class maintains the filenames of the image files 	//
// that has been previously opened by the application.		//
// This class can keep track of up to vc.maxTab (variable   //
// from the VisualCryptography class) open file store their	//
// filenames in a hashtable to be used by the ActionClasses //
// class that operates the actions from the					//
// VisualCryptography class.								//
// Obviously the image files are gif files. 				//
// The filenames/path whatever are stored as string			//
/***********************************************************/

//The package where the base classes are stored
package visualcryptography.filemanager;

import visualcryptography.VisualCryptography;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

/**
 * A filemanager class that maintains the index of the open images on the canvas.
 * To provide synchronization with the tab object.
 *
 * @author Edward P. Legaspi
 * @since 6.24.05
 */

public class FileManager {
  /** 
   * An instance of the main class to provide usefull variables.
   **/
  private VisualCryptography vc = null;
  /** 
   * The filenames and the index of the open image.
   **/
  public Hashtable h = null;  
  /** 
   * The filenames used to get the index on the hashtable.
   **/
  public Vector v = null;
  /**
   * The last selected index of the canvas.
   **/
  public int lastSelectedIndex = 0;
  
  /**
   * Creates a new FileManager object and instantiate its variable to null.
   **/
  public FileManager(VisualCryptography vc) {
  	this.vc = vc;
  	h = new Hashtable(vc.maxTab);
  	v = new Vector(5);
  	for(int i = 0; i < vc.maxTab; i++)
  	  h.put(new Integer(i), "null");
  }
  
  /**   
   * Adds the new open filename of the image to the hashtable.
   **/
  public void addFile(String s) {  	
  	h.put(new Integer(this.getAvailableIndex()), s);
  	
  	if(v.size() >= vc.maxTab) {
  	  v.remove(vc.maxTab - 1);
  	  v.addElement(s);
  	} else
  	  v.addElement(s);
  }
  
  /**   
   * Deletes a path from the hashtable.
   **/
  public void delFile() {
  	if(v.size() != 0)
  	  try {
  	    v.removeElementAt(v.size() - 1);    
  	  } catch(ArrayIndexOutOfBoundsException aiobe) {}
  }
  
  /**
   * Removes the file at the specified index.
   **/
  public void delFileAt(int i) {
    v.remove(i);
  }
  
  
  /**
   * Sets the s as the i content of the vector v.
   **/
  public void insertFileAt(int i, String s) {  	
  	int x = vc.fileManager.getSelectedCanvasIndex(vc.fileManager.getFileAt(i).toString());
    try {
      h.remove(new Integer(x));
      h.put(new Integer(x), s);
    } catch(NullPointerException npe) {}    
  	try {
  	  v.setElementAt(s, i);
  	} catch(ArrayIndexOutOfBoundsException aiobe) {}  	
  }
  
  /**
   * Returns the file stored at the hashtable vat index i.
   **/
  public File getFileAt(int i) {
  	try {
      return new File((String)v.get(i));
	} catch (ArrayIndexOutOfBoundsException ae) { //useful for coalesce first open app'n
	  return null;
	}
  }
  
  /**
   * Returns the string representation of the hashtable filenames
   * for debugging purposes. I found it very convenient.   
   **/
  public String getFileNames() {
    return h.toString() + "\n" + v.toString();
  }
  
  /**
   * Returns the available index of the ImageCanvas object.
   **/  
  public int getAvailableIndex() {
    for(int i = 0; i < h.size(); i++)      
      if((String)h.get(new Integer(i)) == "null")
        return i;
    return (h.size() - 1);
  }
  
  /**
   * Returns the index of the selected indexCanvas.   
   **/
  public int getSelectedCanvasIndex(String s) {  	
  	for(int i = 0; i < h.size(); i++) {  	  
      if(new String((String)h.get(new Integer(i))).equals(s))
        return i;
	}
    return (h.size() - 1);
  }
  
  /**
   * Remove x from the hashtable.
   **/  
  public void reHash(int x) {
  	try {
  	  h.remove(new Integer(x));
    } catch(NullPointerException npe) {
      //First try 2
    }
  	h.put(new Integer(x), "null");
  	lastSelectedIndex = x;  	
  }
  
  /**
   * Returns the first available element of the hashtable.   
   **/
  public int getFirstElement() {
    for(int i = 0; i < h.size(); i++)
      if((String)h.get(new Integer(i)) != "null")
        return i;
    return(h.size() - 1);
  }
  
  /**
   * Reset the content of vector v and hashtable h to empty or null.
   **/
  public void resetVariables() {
  	v = null;
  	h = null;
  	v = new Vector(5);
  	h = new Hashtable(5);
  	for(int i = 0; i < vc.maxTab; i++)
  	  h.put(new Integer(i), "null");
  }
  
} //end FileManager

//****************Final Fantasy Series x Diablo x StarCraft***************//