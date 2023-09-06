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
// Some CODES have been copied from java.sun.com because 	//
// the sun company and other programmers advise me to use	//
// it since the codes that were published were already		//
// tested many times in case there is/are bugs and simly 	//
// because I know it is optimize.							//
/***********************************************************/

/***********************************************************/
// VisualCryptography Class									//
// This is the based class of the various interface we   	//
// can see in our program. It implements Visual Cryptography//
// on black and white gif files. The program will accept    //
// any colored gif image but on the process the image is    //
// converted to gray image, in short the any pixel that is  //
// not white is assumed to be black. This class uses the 	//
// GIFCodec I borrowed from the sourceforge community       //
// http://www.sourceforge.com								//
// It contains JComboBox on which scheme the user wants     //
// and the method of writing the shares.					//
/***********************************************************/

// The package where the base classes are stored
package visualcryptography;

import visualcryptography.codec.gif.MyGif;
import visualcryptography.canvas.ImageCanvas;
import visualcryptography.filemanager.FileManager;
import visualcryptography.filefilter.ExampleFileFilter;
import visualcryptography.matrix.Matrix;

// Sumimases, but I hate importing the whole package so please bare with me
// Imported from the expansion package
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.JViewport;
import javax.swing.JWindow;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.WindowConstants;

// The classes accessible to the original package
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * This is the base class of the application that implements VisualCryptography.
 * This class is responsible for all the action that we performed.
 * @author Edward P. Legaspi
 * @since 6.25.05
 **/

public class VisualCryptography extends JPanel {
  
  // The so many mother variables I used but don't worry all of them are private
  
  /**
   * External Resource File (for easier manipulations)
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
   * The maximum allowed tab count.
   **/
  public static final int maxTab = 5;
  /**
   * The tab on focus.
   **/
  public static int currentTab = 0;
  /**
   * The index of the imageCanvas object.
   **/
  public static int canvasIndex = 0;  
  /**
   * Random number used for saving images. Because windows is slow!!! That's it.
   **/
  public static int randomNum = 0;

  // The tab pane that holds the ImageCanvas
  /**
   * An instance of the JTabbedPane object.
   **/
  public JTabbedPane tabbedPane = null;
  /**
   * The canvas that we see where gif images are drawn.
   **/
  private JPanel drawPanel = null;
  /**
   * The text field that we see at the bottom of the application.
   * It describes the operations that the system currently performs.
   * It also flag errors in case.
   **/
  private static JTextField statusField = null;
  
  // Splash Screen
  /**
   * The main frame that holds the application.
   **/
  private JFrame frame = null;
  /**
   * The label of the application.
   **/
  private JLabel splashLabel = null;
  /**
   * The object that we see before the main application load.
   **/  
  private JWindow splashScreen = null;

  // Menus
  /**
   * The menubar object of the application.
   **/
  private JMenuBar menubar;
  /**
   * The toolbar present to the application.
   **/
  private JToolBar toolbar;
  
  // Suffixes
  /**
   * Suffix of denote a label object.
   **/
  private static final String labelSuffix = "Label";
  /**
   * Suffix of denote an action object.
   **/
  private static final String actionSuffix = "Action";
  /**
   * Suffix of denote a mnemonic object.
   **/
  private static final String mnemonicSuffix = "Mnemonic";
  /**
   * Suffix of denote an image object.
   **/
  private static final String imageSuffix = "Image";
  /**
   * Suffix of denote an accessible description a object.
   **/
  private static final String acSuffix = "AccessibleDescription";
  /**
   * Suffix of denote a tooltip object.
   **/
  private static final String tipSuffix = "Tooltip";
  
  
  // Possible Look & Feels
  /**
   * The standard mac look and feel.
   **/
  private static final String mac = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
  /**
   * The standard metal look and feel.
   **/
  private static final String metal = "javax.swing.plaf.metal.MetalLookAndFeel";
  /**
   * The standard motif look and feel.
   **/
  private static final String motif = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
  /**
   * The standard windows look and feel.
   **/
  private static final String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
  /**
   * The standard gtk look and feel.
   **/
  private static final String gtk = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";  
  
  // Miscellaneous for laf
  /**
   * ButtonGroup object where look and feel strings are placed.
   **/
  private ButtonGroup lafMenuGroup = new ButtonGroup();
  /**
   * The current set look and feel of the system.
   **/
  private static String currentLookAndFeel = metal;
  /**
   * The menu that holds the look and feel strings.
   **/
  private JMenu lafMenu = null;
  
  // The File Chooser things
  /**
   * A JFileChooser object used to open files for encryption.
   **/
  private JFileChooser chooser = null;    
  
  // The Icon you'll see in each frame, you can set the url from the resource file
  /**
   * The icon of the main frame.
   **/
  private ImageIcon minIcon = null;
  
  // Some useful variables here
  /**
   * An object used to display an object inside a JScrollPane.
   **/
  private JViewport port = null;
  /**
   * Used to hold the JPanel object that serves as the drawing board.
   **/
  private JScrollPane scrollPane = null;
  /**
   * An array collection of ImageCanvas object.
   **/
  public ImageCanvas[] imageCanvas = null;  
  /**
   * Used to get the screen size to maximize the application upon loading.
   **/
  private Rectangle screenRect = null;
  
  // The choices you have from the main interface
  /**
   * The scheme desired for encryption.
   **/
  public JComboBox vcsScheme = null;
  /**
   * The method of encryption used.
   **/
  public JComboBox writeShareMethod = null;
  /**
   * The current accessed file.
   **/
  public File filename = null;
  
  // The basis matrices  
  /**
   * The matrix used to encrypt white pixel.
   **/
  public Matrix c0 = null;
  /**
   * The matrix used to encrypt black pixel.
   **/
  public Matrix c1 = null;
  /**
   * The current open instance of MyGif object used for encryption and decryption.
   **/  
  public MyGif currentGifImage = null;
  
  // I'm trying to maintain say the maxTab open file if I can buy some more time
  /**
   * An instance of the FileManager class.
   **/
  public FileManager fileManager = null;
  

  
   // Constructors: Now this is the mother class.   
   
   /**
    * Used GraphicsConfiguration for setting the screen size and splash screen.
    **/
  public VisualCryptography(GraphicsConfiguration gc) {
	
	super(true);

    // Create Frame here for app-mode so the splash screen can get the
    // GraphicsConfiguration from it in createSplashScreen()
    frame = new JFrame(gc);
    
    frame.addWindowListener(new VCCloser(this));
    
    // The size of the screen
    screenRect = frame.getGraphicsConfiguration().getBounds();
    
    // The FileManager
    fileManager = new FileManager(this);
	
	try {
	  UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	  // If you want the System L&F instead, comment out the above line and
	  // uncomment the following:
	  // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception exc) {		
	  System.err.println("Error loading L&F: " + exc);
	  //setStatus("Uncaught Exception : " + exc); loading p lng
	}

	setBorder(BorderFactory.createEtchedBorder());

    // setLayout(new BorderLayout());
    setLayout(new BorderLayout());
    
    // set the preferred size of the project from the resource file
    //setPreferredSize(new Dimension(Integer.parseInt(getResourceString("dimensionW")), 
    // 							   Integer.parseInt(getResourceString("dimensionH"))));
    

    // Create and throw the splash screen up. Since this will
    // physically throw bits on the screen, we need to do this
    // on the GUI thread using invokeLater.
    createSplashScreen();	
	// do the following on the gui thread
    SwingUtilities.invokeLater(new Runnable() {
      // I am trying to slow things up but errors popped out
      /*public void init() {
        try {
          Thread.sleep(500);
        } catch(InterruptedException ie) {}
      }*/
      public void run() {        
      	showSplashScreen();      	
	  }
    });
	
	// prepare the main window
	preparePanel();
	setStatus("Welcome to Visual Encryptor.");

	SwingUtilities.invokeLater(new Runnable() {
	  public void run() {
		showVisualEncryptor();
		hideSplash();
	  }
	});
	
	setVisible(true);

  } //end constructor

  /**
   * The main method
   **/  
  public static void main(String args[]) {
    try {
      String vers = System.getProperty("java.version");
      if (vers.compareTo("1.1.2") < 0) {
        System.out.println("!!!WARNING: Swing must be run with a 1.1.2 or higher version VM!!!");
      }
      VisualCryptography application = new VisualCryptography(GraphicsEnvironment.
										   getLocalGraphicsEnvironment().
										   getDefaultScreenDevice().										  
										   getDefaultConfiguration());
	  System.out.println("Welcome to Visual Cryptography:");
	  System.out.println("Author: Edward P. Legaspi laizzez_faire07@yahoo.com");
	} catch (Throwable t) {
      System.out.println("Uncaught Exception: " + t);
      t.printStackTrace();
    }    
  }

  /**
   * Brings up the Visual Encryptor frame.
   **/
  private void showVisualEncryptor() {
    if(getFrame() != null) {
	  // put VE in a frame and show it
	  JFrame f = getFrame();
	  // full screen mode
	  minIcon = new ImageIcon(getResourceString("minIcon"));	  
      Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
      f.getGraphicsConfiguration());
	  
	  // the visible configuration of the main frame
	  f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	  f.setTitle(getResourceString("Title"));	  
	  f.getContentPane().setLayout(new BorderLayout());
	  f.getContentPane().add(this, BorderLayout.CENTER);
	  f.setIconImage(minIcon.getImage());
	  f.setBackground(Color.lightGray);	  
	  f.setSize(screenRect.width, screenRect.height);
	  f.pack();
	  f.setResizable(false); //this is convenient
	  f.show();

      // Make sure we don't place the visual encryptor off the screen.
      int centerWidth = screenRect.width < f.getSize().width ?
		  screenRect.x : screenRect.x + screenRect.width / 2 - f.getSize().width / 2;
      int centerHeight = screenRect.height < f.getSize().height ? 
		  screenRect.y : screenRect.y + screenRect.height / 2 - f.getSize().height / 2;
		  centerHeight = centerHeight < screenInsets.top ?
		  screenInsets.top : centerHeight;

      f.setLocation(centerWidth, centerHeight);
	  f.show();      
    } 
  }
  
  /**
   * Creates Splash Screen but hides it.
   **/
  private void createSplashScreen() {
    splashLabel = new JLabel(createImageIcon("Splash.jpg", "SplashAccessibleDescription"));
	splashScreen = new JWindow(getFrame());
	splashScreen.getContentPane().add(splashLabel);
	splashScreen.pack();
	screenRect = getFrame().getGraphicsConfiguration().getBounds();
	splashScreen.setLocation(
      screenRect.x + screenRect.width / 2 - splashScreen.getSize().width / 2,
	  screenRect.y + screenRect.height / 2 - splashScreen.getSize().height / 2);	
  }

  /**
   * Hides the splash screen.
   **/
  private void hideSplash() {
	splashScreen.setVisible(false);
	splashScreen = null;
	splashLabel = null; 
  }
  
  /**
   * Shows the splash screen.
   **/
  private void showSplashScreen() {	
    splashScreen.show();	
  }

  /**
   * Creates an icon from an image contained in the "resources" directory.
   **/
  public ImageIcon createImageIcon(String filename, String description) {
	String path = "/resources/" + filename;
	return new ImageIcon(getClass().getResource(path)); 
  }

  /**
   * Returns the frame instance.
   **/
  public JFrame getFrame() {
	return frame;
  }
  
  /**
   * Create the button on the toolbar located in the resource budle.
   **/
  private JButton createToolbarButton(String key) {
	URL url = getResource(key + imageSuffix);
    JButton b = new JButton(new ImageIcon(url)) {
      public float getAlignmentY() { 
	    return 0.5f; 
	  }
	};
    b.setRequestFocusEnabled(false);
    b.setMargin(new Insets(1,1,1,1));

	String astr = getResourceString(key + actionSuffix);	
	if (astr == null) {
	  astr = key;
	}
	setAction(astr, b); //sets the action
	String tip = getResourceString(key + tipSuffix);
	if (tip != null) {
 	  b.setToolTipText(tip);
	} 
    return b;
  }

  /**
   * Hook toolbar items from the resource bundle into component.
   **/
  private Component createTool(String key) {
	return createToolbarButton(key);
  }

  /**
   * Creates the toolbar from the resource string.
   **/
  private Component createToolbar() {
	toolbar = new JToolBar();
	String[] toolKeys = tokenize(getResourceString("toolbar"));
	for (int i = 0; i < toolKeys.length; i++) {
	  if (toolKeys[i].equals("-")) {
		toolbar.add(Box.createHorizontalStrut(5));
	  } else {
		toolbar.add(createTool(toolKeys[i]));
	  }
	}
	toolbar.add(Box.createHorizontalGlue());
	return toolbar;
  }
  
  /**
   * Prepare the main window. This method sets everything visible in our interface.
   **/
  private void preparePanel() {    
  	JPanel panel = new JPanel();
  	JPanel iconPanel = new JPanel();  	

	imageCanvas = new ImageCanvas[maxTab];
	
	panel.setLayout(new BorderLayout());
	panel.setBorder(new EtchedBorder());	
	iconPanel.setLayout(new BorderLayout());	
	
	iconPanel.add("West", createToolbar());
	iconPanel.add("Center", createComboBox());
	
	menubar = createMenubar();
	
	panel.add("North", menubar);
	panel.add("Center", iconPanel);
		
	imageCanvas[currentTab] = new ImageCanvas(this);	
	imageCanvas[currentTab].setPreferredSize(new Dimension(screenRect.width - 25, screenRect.height));
	
	scrollPane = new JScrollPane();
	scrollPane.getViewport().add(imageCanvas[currentTab]);
	tabbedPane = new JTabbedPane();
	tabbedPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));	
	tabbedPane.getModel().addChangeListener(new TabListener(this));
	tabbedPane.addTab("null", scrollPane);
	tabbedPane.setForegroundAt(0, Color.black);
		
	// adds component to the main panel	
	add(panel, BorderLayout.NORTH);
	add(tabbedPane, BorderLayout.CENTER);
	add(createStatusbar(), BorderLayout.SOUTH);
	
	doLayout();
	validate();	
  } //end prepareFrame
  
  /**
   * Builds the VCS Scheme choices and the column permutation.
   * I seperated it for easier manipulations, I hope so.
   **/
  private JComponent createComboBox() {
  	JPanel panel = new JPanel();
  	Vector vector = new Vector();
  	vcsScheme = new JComboBox();
  	writeShareMethod = new JComboBox();
  	
  	vector.add("2x2 - Visual Cryptography");
  	vector.add("2xn - Visual Cryptography");
  	vector.add("nxn - Visual Cryptography");
  	vcsScheme = new JComboBox(vector);
  	panel.add(vcsScheme);
  	
  	vector = new Vector();
  	vector.add("Random Basis Column");  	
  	writeShareMethod = new JComboBox(vector);
  	panel.add(writeShareMethod);
  	return panel;
  }
  
  /**
   * Create the status bar.
   **/
  private JComponent createStatusbar() {
  	JPanel panel;
  	panel = new JPanel();
  	panel.setLayout(new BorderLayout());
  	panel.setBorder(new EtchedBorder());
	statusField = new JTextField("");
	statusField.setEditable(false);
	panel.add(statusField);
	return panel;
  }

  /**
   * Tokenize the string from the resource bundle and make it the menus. 
   * It returns the array of strings tokenized.
   **/
  private String[] tokenize(String input) {
	Vector v = new Vector();
	StringTokenizer t = new StringTokenizer(input);
	String cmd[];

	while (t.hasMoreTokens())
	  v.addElement(t.nextToken());
	cmd = new String[v.size()];
	for (int i = 0; i < cmd.length; i++)
	  cmd[i] = (String) v.elementAt(i);

	return cmd;
  }

  /**
   * This method returns a string from the visual_encryptor's resource bundle.
   **/
  public String getResourceString(String key) {
	String value = null;
	try {
	  value = resources.getString(key);
	} catch (MissingResourceException e) {
	  value = null;
	  //setStatus("Uncaught Exception : " + e); loading p lng
	} 
	return value;
  }

  /**
   * Returns a mnemonic from the resource bundle. Typically used askeyboard shortcuts in menu items.
   **/
  private char getMnemonic(String key) {
	return getResourceString(key).charAt(0);
  }

  /**
   * Returns the url of the image.
   **/ 
  public URL getResource(String key) {
	String name = getResourceString(key);
	if (name != null) {
	  URL url = this.getClass().getResource(name);
	  return url;
	}
	return null;
  }

  /**
   * Creates the menubar from the resource file.
   **/
  private JMenuBar createMenubar() {	
	JMenuBar mb = new JMenuBar();
	JMenuItem mi;
    mb.getAccessibleContext().setAccessibleName(getResourceString("menuBar" + acSuffix));
	String[] menuKeys = tokenize(getResourceString("menubar"));
	for (int i = 0; i < menuKeys.length; i++) {
	  JMenu m = createMenu(menuKeys[i]);
	  if (m != null) {
		mb.add(m);
	  }
	}
	lafMenu = (JMenu) mb.add(new JMenu(getResourceString("laf" + labelSuffix)));
    lafMenu.setMnemonic(getMnemonic("lafMnemonic"));
	lafMenu.getAccessibleContext().setAccessibleDescription(
	    	getResourceString("lafAccessibleDescription"));

	mi = createLafMenuItem(lafMenu, "metalLabel", "metalMnemonic", "metalAccessibleDescription", metal);
	mi.setSelected(true); // this is the default l&f

	createLafMenuItem(lafMenu, "macLabel", "macMnemonic","macAccessibleDescription", mac);
	createLafMenuItem(lafMenu, "motifLabel", "motifMnemonic", "motifAccessibleDescription", motif);
	createLafMenuItem(lafMenu, "windowsLabel", "windowsMnemonic", "windowsAccessibleDescription", windows);
	createLafMenuItem(lafMenu, "gtkLabel", "gtkMnemonic", "gtkAccessibleDescription", gtk);	
	
	// for laf
	
	return mb;
  }  

  /**
   * Create menu from the source resource bundle. This one is convenient.
   **/
  private JMenu createMenu(String key) {
	String[] itemKeys = tokenize(getResourceString(key));
	JMenu menu = new JMenu(getResourceString(key + labelSuffix));	
	menu.setMnemonic(getMnemonic(key + mnemonicSuffix));
	menu.getAccessibleContext().setAccessibleDescription(getResourceString(key + acSuffix));
	for (int i = 0; i < itemKeys.length; i++) {
	  if (itemKeys[i].equals("-")) {
		menu.addSeparator();
	  } else {
		JMenuItem mi = createMenuItem(itemKeys[i]);
		menu.add(mi);
	  }
	}
	return menu;
  }  
  
  /**
   * Creates the menu item for each menu.
   **/
  private JMenuItem createMenuItem(String cmd) {
	JMenuItem mi = new JMenuItem(getResourceString(cmd + labelSuffix));
    URL url = getResource(cmd + imageSuffix);
	if (url != null) {
	  mi.setHorizontalTextPosition(JButton.RIGHT);
	  mi.setIcon(new ImageIcon(url));	  
	}
	mi.setMnemonic(getMnemonic(cmd + mnemonicSuffix));
	mi.getAccessibleContext().setAccessibleDescription(getResourceString(cmd + acSuffix));
	String astr = getResourceString(cmd + actionSuffix);
	if (astr == null) {
	  astr = cmd;	  
	}
	setAction(astr, mi);		
	return mi;
  }
  
  /**
   * Creates the laf menu item for each menu. Fasten things up.
   **/  
  private JMenuItem createLafMenuItem(JMenu menu, String label, String mnemonic,
			       String accessibleDescription, String laf) {
    JMenuItem mi = (JRadioButtonMenuItem) menu.add(new JRadioButtonMenuItem(getResourceString(label)));
	lafMenuGroup.add(mi);
	mi.setMnemonic(getMnemonic(mnemonic));
	mi.getAccessibleContext().setAccessibleDescription(getResourceString(accessibleDescription));
	mi.addActionListener(new ChangeLookAndFeelAction(this, laf));

	mi.setEnabled(isAvailableLookAndFeel(laf));

	return mi;
  }  
  
  /** 
   * The L&F menu will use this method to detemine whether the various L&F 
   * options should be active or inactive. Of cource the first three are defaults 
   * available from the package but the other two needs internet connection. 
   * I just hope the site is up!
   **/
  public boolean isAvailableLookAndFeel(String laf) {
    try { 
      Class lnfClass = Class.forName(laf);
      LookAndFeel newLAF = (LookAndFeel)(lnfClass.newInstance());
      return newLAF.isSupportedLookAndFeel();
    } catch(Exception e) { // If ANYTHING weird happens, return false
      //I don't know why error pops up when i uncomment this line , it's reachable ins't it
      //setStatus("Uncaught Exception : " + e);
      return false;      
    }
  }

  /**
   * THE long way of settting action damn it. No choice?? 7/2/2005
   * Wow how did I came up with these?
   **/
  private void setAction(String action, JMenuItem mi) {
    if(action.equals("ExitAction"))
	  mi.addActionListener((Action)new ExitAction(this));	  
  	else if(action.equals("AboutAction"))
	  mi.addActionListener((Action)new AboutAction(this));	
	else if(action.equals("HelpAction"))
	  mi.addActionListener((Action)new HelpAction(this));
	else if(action.equals("OpenAction"))
	  mi.addActionListener((Action)new OpenAction(this));
	else if(action.equals("CloseAction"))
	  mi.addActionListener((Action)new CloseAction(this));	  
	else if(action.equals("EncryptAction"))
	  mi.addActionListener((Action)new EncryptAction(this));
	else if(action.equals("CoalesceAction"))
	  mi.addActionListener((Action)new CoalesceAction(this));
	else if(action.equals("CoalesceWithNoiseAction"))
	  mi.addActionListener((Action)new CoalesceWithNoiseAction(this));
	else if(action.equals("SaveAction"))
	  mi.addActionListener((Action)new SaveAction(this));
	else if(action.equals("CloseAllAction"))
	  mi.addActionListener((Action)new CloseAllAction(this));
	else
	  System.out.print("Ping ");	
  }

  /**
   * THE long way of settting action damn it. No choice?? 7/2/2005
   * This time I am lazy hahahaha.
   **/
  private void setAction(String action, JButton mi) {  	
    if(action.equals("ExitAction"))
	  mi.addActionListener((Action)new ExitAction(this));
	else if(action.equals("HelpAction"))
	  mi.addActionListener((Action)new HelpAction(this));
	else if(action.equals("OpenAction"))
	  mi.addActionListener((Action)new OpenAction(this));
	else if(action.equals("CloseAction"))
	  mi.addActionListener((Action)new CloseAction(this));
	else if(action.equals("CloseAllAction"))
	  mi.addActionListener((Action)new CloseAllAction(this));
	else if(action.equals("EncryptAction"))
	  mi.addActionListener((Action)new EncryptAction(this));
	else if(action.equals("CoalesceAction"))
	  mi.addActionListener((Action)new CoalesceAction(this));
	else if(action.equals("CoalesceWithNoiseAction"))
	  mi.addActionListener((Action)new CoalesceWithNoiseAction(this));
	else if(action.equals("SaveAction"))
	  mi.addActionListener((Action)new SaveAction(this));
	else	
	  System.out.print("Pong ");	
  }
  
  /**
   * Stores the current L&F, and calls updateLookAndFeel, below.
   **/
  public void setLookAndFeel(String laf) {
	if(currentLookAndFeel != laf) {
	    currentLookAndFeel = laf;
	    updateLookAndFeel();
	}
  }  

  /**
   * Sets the current L&F on each visual encryptor module
   * uses the class variable currentLookAndFeel to set the laf.
   **/
  private void updateLookAndFeel() {
	try {
	  UIManager.setLookAndFeel(currentLookAndFeel);
      SwingUtilities.updateComponentTreeUI(this);      
      // update LAF for the toplevel frame, too
      SwingUtilities.updateComponentTreeUI(getFrame());       	  
	} catch (Exception ex) {
	  System.out.println("Failed loading L&F: " + currentLookAndFeel);
	  System.out.println(ex);
	}
  }
  
  /**
   * Removes all the files and subdirectories found at the root directory/temp of the class.
   * The directory removed was created prior to encryption it serves as a virtual
   * directory for temporary image files.
   **/
  public void delTree(String f) {
  	File path = null;
  	System.out.println("Deletes the directory tree.");
  	
  	for(int i = 0; i < maxTab; i++)
	  if(imageCanvas[i] != null)
	    imageCanvas[i] = null;
	          
	fileManager.resetVariables();
	tabbedPane.removeAll();
  	
  	try {
  	  path = new File(f);
  	  path.deleteOnExit();
    } catch(NullPointerException npe) {
      System.out.println("Exception : " + npe);
  	  System.exit(0);
    } catch(SecurityException se) {
      System.out.println("Folder is locked.");
    }
    
    //check if temp file exists, temp file is not accepted
    if(path.exists())
      System.out.println("Path is valid.");
    if(!path.exists() || !path.isDirectory())
      try {
        System.exit(0);
      } catch(SecurityException se) {}
    
    System.out.println("VisualCryptography.Path: " + path);
    
  	try {
  	  String files[] = path.list();
  	  for(int i = 0; i < files.length; i++) {
  	  	try {
  	      new File(path + "/" + files[i]).deleteOnExit();
  	    } catch(SecurityException se1) {
  	      System.out.println("Unable to delete " + new File(path + "/" + files[i]).toString());
  	    }
  	  }
  	} catch(SecurityException se) {
  	  System.out.println("Exception : " + se);
  	  System.exit(0);
  	}  	
  	
  	try {
  	  path.delete();
    } catch(SecurityException se) {
      System.out.println("SecurityException : " + se);
    }
    
    try { //buy some time to exit
      Thread.sleep(1000);
    } catch(InterruptedException ie) {
      System.out.println("InterruptedException: " + ie);
	}
	
	//deallocate all the memory used.
	getFrame().dispose();
	System.gc();
    
    System.out.println("Normal Program Termination.");
    System.exit(0); //Close the application
    
  } //I like the dos and c implementation wahahahahaha, simple and destructive
  
  /**
   * Sets the status of the application.
   **/  
  public void setStatus(String s) {
  	final Thread t = new Thread();  	
	// do the following on the gui thread
	SwingUtilities.invokeLater(new VisualEncryptorRunnable(this, s) {
      public void run() {
        //well It seems I cannot buy some time here
        //errors just keep popping out
	    vc.statusField.setText((String) obj);
	  }
	});
  }
  
  /**
   * Class InsetPanel
   **/
  private class InsetPanel extends JPanel {
	Insets i;
	InsetPanel(Insets i) {
	  this.i = i;
	}
	public Insets getInsets() {
	  return i;
	}
  }
  
  /**
   * VisualEncryptorRunnable() 
   * Class to avoid to much gui work
   * I like this implementation its the best
   **/
  private class VisualEncryptorRunnable implements Runnable {
	public VisualCryptography vc;
	public Object obj;
	
	public VisualEncryptorRunnable(VisualCryptography vc, Object obj) {
	  this.vc = vc;
	  this.obj = obj;
	}

	public void run() {
	}
  } //end VisualEncryptorRunnable
  
} //end VisualCryptography

//Watashi wa daisuki kommento desu ne.
//******Midori No Hibi x Ah My Goddess x SamuraiX-Reminescence************//