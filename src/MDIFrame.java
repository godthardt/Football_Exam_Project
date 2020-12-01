import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

// MDI (Multiple Document interface) is built in to C++, C# and Delphi, and a lot of functionality are build into classes like TMDIxxx classes -
// but in Java this is not the case, but Java offers JInternalFrame (as MDIchilds) (a "lightweight" JFrame is supplied to offer MDI functionality
// I have found inspiration at:
// http://www.java2s.com/Tutorials/Java/Java_Swing/1600__Java_Swing_MDI.htm 
// https://www.comp.nus.edu.sg/~cs3283/ftp/Java/swingConnect/friends/mdi-swing/mdi-swing.html
// https://www.comp.nus.edu.sg/~cs3283/ftp/Java/swingConnect/archive/tech_topics_arch/frames_panes/frames_panes.html

public class MDIFrame extends JFrame implements InternalFrameListener {
	private static final long serialVersionUID = 2;  //Helps class control version of serialized objects
	public JDesktopPane desktopPane = new JDesktopPane();
	public DesktopManager desktopManager;
	private JLayeredPane layeredPane;
	private TurnamentManager turnamentManager;
	private static int childWindowNumber = 1;
	private JMenu windowMenu;
	private int numberOfWindowBaseMenuItems;
	public Dimension dim;
	private StatusBar statusBar;
	private MDIChildTurnamentInfo mDIChildTurnamentInfo;

	// Constructor
	public MDIFrame(String title) {
		super(title);

		try {
			// The turnamentManager is not important, and a relatively late invention. Main purpose is to demonstrate polymorf iteration
			turnamentManager = new TurnamentManager("Kan håndtere x turneringer, og har totallister over hold og spillere");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create a desktopPane  
		desktopPane = new JDesktopPane();
		
		// Make sure other component aren't visible "below"
		desktopPane.setOpaque(false);
		
		setMDIFrameSize();		
		getContentPane().add(desktopPane);

		// According to oracle documentation, a desktopmanager should be implemented. I have tried without, and the program still works as expected.
		// Link to Oracle documentation: https://docs.oracle.com/javase/7/docs/api/javax/swing/DesktopManager.html
		desktopManager = new DefaultDesktopManager();
		desktopPane.setDesktopManager(desktopManager);

		
		// The Pane has 5 build in standard layers, which lives on top of each other, eg. a dragging layer at top. It's also possible to implement user defined layers (I don't use this feature)
		layeredPane = getLayeredPane();

		// Add menu "handling / initialization in sub method, in order to better readability constructor
		addMenus();
		
		// Create a Status bar
		statusBar = new StatusBar(dim.width, Constants.getModus());
		getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);
		
		// Center main window (not tested with multiple screens)
		centerJFrame();

		mDIChildTurnamentInfo = new MDIChildTurnamentInfo(turnamentManager);
		mDIChildTurnamentInfo.setLocation(Constants.getModus(), Constants.getModus()); 

		mDIChildTurnamentInfo.setSize(Constants.getMDIChildWidth() + Constants.getMDIChildWidth() / 5, Constants.getMDIChildHigth() - Constants.getMDIChildHigth() / 5 );		
		desktopPane.add(mDIChildTurnamentInfo);	    
		mDIChildTurnamentInfo.setVisible(true);
		layeredPane.moveToFront(mDIChildTurnamentInfo);
		
		setVisible(true);
		
		// In developmnt phase, I loaded a deserialize a Turnament object, in order to have something to look at
		// addNewTurnament("serializedTurnamentExample.ser");

	}
	
	private void setMDIFrameSize() {
		dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Make sure there is space for the mainwindow on my old laptop ;-) - warn the user, if dimensions must be downscaled
		if ((dim.width < Constants.getMDIFrameWidth()) || (dim.height < Constants.getMDIFrameHigth())) {
			int lessergModus = Constants.getModus();
			int orgMDIFrameHigth = Constants.getMDIFrameHigth();
			int orgMDIFrameWidth = Constants.getMDIFrameWidth();
			while ((dim.height < Constants.getMDIFrameHigth()) )  {
				lessergModus--;
				Constants.recalcDimensions(lessergModus);
			}
			JOptionPane.showMessageDialog(this, "Den aktuelle skærmopløsning giver ikke plads til den ønskede vinduesstørrelse" + 
					" - nogle ting kan se lidt forkerte ud. Programmet kører bedst med minimum " + orgMDIFrameHigth + " x " +  orgMDIFrameWidth,
					"Skærmopløsning " + dim.height + " x " + dim.width, JOptionPane.INFORMATION_MESSAGE);			
		}
		setSize(Constants.getMDIFrameWidth(), Constants.getMDIFrameHigth());

	}

	private void centerJFrame() {
		try {
			// Plagiat from https://stackoverflow.com/questions/12072719/centering-the-entire-window-java/34869895#34869895
			dim = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setExtendedState(MDIFrame.NORMAL);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addNewTurnament(String fileName) {
		try {

			// Restore Turnament object from file
			Turnament deSerializedTurnamentObject = (Turnament) Serialize.load(fileName);

			// Indicate to the user, that the turnament has been deserialized, by adding info to title line
			String newTurnamentName = deSerializedTurnamentObject.getName() + " - deserialiseret d. " + LocalDate.now().format(DateTimeFormatter.ofPattern(Constants.dkDateFormat)).toString() + " fra filen " + fileName;
			deSerializedTurnamentObject.setName(newTurnamentName);
			
			addNewTurnament(deSerializedTurnamentObject);	    
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Kunne ikke deserialisere filen " + fileName + " som et Turnament objekt! Exception:\n\n" + e.getMessage(),
					"Fejl ved deserialisering", JOptionPane.ERROR_MESSAGE);
			System.err.println("Turnament object in " + fileName + " could not be de-serialized :-(");
			e.printStackTrace();
		}
	}

	private void addNewTurnament(Turnament turnament) {
		turnamentManager.addTurnament(turnament);
		
		// Create a new MDIChild to display the turnament details 
		MDIChild mDIChild = new MDIChild(childWindowNumber, turnament);
		
		mDIChild.addInternalFrameListener(this);
		Insets i = this.getInsets(); // Insets contains top (size of titlebar), left, etc. of the "JFrame", found on https://www.programcreek.com/java-api-examples/?class=java.awt.Container&method=getInsets

		// try to prevent MDIchildwindows to be located outside JFrame i do some modulus on the position
		int modulusChildWindowNumber = childWindowNumber % Constants.getModus();  
		
		int x = modulusChildWindowNumber * i.top / 2 + 2* Constants.getModus(); //on my Pc i.top = 31
		int y = modulusChildWindowNumber * i.top / 2  + 2* Constants.getModus();
		mDIChild.setLocation(x, y); 

		mDIChild.setSize(Constants.getMDIChildWidth() + i.left, Constants.getMDIChildHigth() + i.left); //on my Pc i.left = 8		
		desktopPane.add(mDIChild);	    
		mDIChild.setVisible(true);
		// Make sure the "status window is "notified"
		mDIChildTurnamentInfo.refreshInfoTable();		
		layeredPane.moveToFront(mDIChild);
		statusBar.setText("MDIChild vindue nr. " + childWindowNumber + " åbnet");
		childWindowNumber++;		
	}

	private void addMenus() {
		// Menubar to contain top menus
		JMenuBar  menubar = new JMenuBar();

		//Top menus
		JMenu     fileMenu   = new JMenu();
		fileMenu.setText("Fil");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		windowMenu   = new JMenu();
		windowMenu.setText("Vindue");
		windowMenu.setMnemonic(KeyEvent.VK_V);		
		
		//Sub menus
		JMenuItem refreshMenu   = new JMenuItem();
		JMenuItem newTurnamentMenu   = new JMenuItem();
		JMenuItem newCupTurnamentMenu   = new JMenuItem();
		JMenuItem newfinal4CupTurnamentMenu   = new JMenuItem();		
		JMenuItem loadSerializedTurnamentMenu = new JMenuItem();
		JMenuItem saveSerializedTurnamentMenu = new JMenuItem();
		JMenuItem listAllTurnamentsMenu = new JMenuItem();		
		JMenuItem closeMenu  = new JMenuItem();
		JMenuItem closeAllWindowsMenu  = new JMenuItem();
		JMenuItem minimizeAllWindowsMenu  = new JMenuItem();
		
		
		// Set text and shortcuts on sub menu items, and attach submenu to top menu, 
		// found inspiration on https://www.codejava.net/java-se/swing/setting-shortcut-key-and-hotkey-for-menu-item-and-button-in-swing
		addMenuMneMonics(fileMenu, refreshMenu, "Resimuler matchafvikling i valgt turnering", KeyEvent.VK_F5, 0);		
		addMenuMneMonics(fileMenu, newTurnamentMenu, "Ny superliga", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);		
		addMenuMneMonics(fileMenu, newCupTurnamentMenu, "Ny pokalturnering", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
		addMenuMneMonics(fileMenu, newfinal4CupTurnamentMenu, "Ny Final 4 pokalturnering", KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK);		
		addMenuMneMonics(fileMenu, loadSerializedTurnamentMenu, "Åbn serialiseret turnering fra fil", KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
		addMenuMneMonics(fileMenu, saveSerializedTurnamentMenu, "Gem serialiseret turnering i fil", KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
		addMenuMneMonics(fileMenu, listAllTurnamentsMenu, "Vis info om alle turneringer", KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK);		
		fileMenu.add(new JSeparator()); // plagiat from http://www.java2s.com/Tutorial/Java/0240__Swing/AddSeparatortoJMenu.htm		
		addMenuMneMonics(fileMenu, closeMenu, "Afslut", KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK);
		
		addMenuMneMonics(windowMenu, minimizeAllWindowsMenu, "Minimer alle Vinduer", KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);		
		addMenuMneMonics(windowMenu, closeAllWindowsMenu, "Luk alle turneringsvinduer", KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK);
		windowMenu.add(new JSeparator());		
		addMenuMneMonics(windowMenu, listAllTurnamentsMenu, "Vis info om alle åbne turneringer", KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK);		
		windowMenu.add(new JSeparator());
		numberOfWindowBaseMenuItems = windowMenu.getItemCount(); 
		
		// add top menus to menubar
		menubar.add(fileMenu);
		menubar.add(windowMenu);	    

		// add menu to MDIFrame
		setJMenuBar(menubar);
		
		// Add menu listeners

		// JMenus require MenuListener
		windowMenu.addMenuListener(new MenuListener() {
	        // Make sure the Window menu contains the relevant windows 
			public void menuSelected(MenuEvent e) { listChildMenusInWindowTopMenu(); }
	        public void menuCanceled(MenuEvent e) { } // Must implement this abstract method to use addMenuListener  
	        public void menuDeselected(MenuEvent e) { }  // Must implement this abstract method to use addMenuListener
	      });		

		listAllTurnamentsMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					mDIChildTurnamentInfo.refreshInfoTable();
					activateChild(mDIChildTurnamentInfo);		
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});	    
		
		// JMenuItem(s) require ActionListener(s)
		newTurnamentMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					Turnament turnament = new Turnament(turnamentManager.getTeamsOfLevel(0), "Superliga (" + childWindowNumber + ")", LocalDate.of(2020, Month.AUGUST, 15), LocalDate.of(2021, Month.MAY, 14));
					addNewTurnament(turnament);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});	    

		newCupTurnamentMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					ArrayList<Team> teamsAtBothLevels = (turnamentManager.getTeamsOfLevel(0));
					teamsAtBothLevels.addAll(turnamentManager.getTeamsOfLevel(1));

					Turnament cupTurnament = new CupTurnament(teamsAtBothLevels, "Pokalturnering (" + childWindowNumber + ")", LocalDate.of(2020, Month.SEPTEMBER, 15), LocalDate.of(2021, Month.JUNE, 2));
					addNewTurnament(cupTurnament);	        		

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});	    

		newfinal4CupTurnamentMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					Turnament cupTurnament = new CupTurnament(turnamentManager.getTeamsOfLevel(-1), "Final 4 turnering (" + childWindowNumber + ")", LocalDate.of(2020, Month.OCTOBER, 17), LocalDate.of(2020, Month.OCTOBER, 24));
					addNewTurnament(cupTurnament);	        		

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});	    

		refreshMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					// is the active window it a MDIChild object ?
					if (desktopPane.getSelectedFrame() instanceof MDIChild) {
						MDIChild mDIChild = (MDIChild) desktopPane.getSelectedFrame();
						mDIChild.reGenerateMatchesAndGoals();
					}
					else {
						JOptionPane.showMessageDialog(getWindow(), "Kan ikke resimulere matchafvikling for dette vindue, da det ikke indeholder en turnering",
								"Kan ikke resimuleres", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		loadSerializedTurnamentMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				File initialDir = new File(System.getProperty("java.class.path"));
				initialDir = initialDir.getParentFile();
				fileChooser.setCurrentDirectory(initialDir);
				FileFilter filter = new FileNameExtensionFilter("Serialiserede objekter","ser");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setFileFilter(filter);
				int result = fileChooser.showOpenDialog(getWindow());
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					System.out.println("Selected file: " + selectedFile.getAbsolutePath());
					try {
						// de-serialize a Turnament object
						addNewTurnament(selectedFile.getAbsolutePath());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(getWindow(), "Kunne ikke deserialisere filen " + selectedFile.getAbsolutePath() + " som et Turnament objekt!",
								"Fejl ved deserialisering", JOptionPane.ERROR_MESSAGE);
						System.err.println("Turnament object in " + selectedFile + " could not be de-serialized :-(");
						ex.printStackTrace();
					}
				}
			}
		});
		
		saveSerializedTurnamentMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				File initialDir = new File(System.getProperty("java.class.path"));
				initialDir = initialDir.getParentFile();
				fileChooser.setCurrentDirectory(initialDir);
				fileChooser.setAcceptAllFileFilterUsed(false);				
				FileFilter filter = new FileNameExtensionFilter("Serialiserede objekter (*.ser)","ser");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setFileFilter(filter);				
				int result = fileChooser.showSaveDialog(getWindow());
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					// Try to enforce *.ser extension
					// TODO File selectedFileWithExtension = selectedFile.getPath().replace(".", ".ser");
					System.out.println("Selected file: " + selectedFile.getAbsolutePath());
					try {
						MDIChild child = (MDIChild) desktopPane.getSelectedFrame();
						if (child != null) {
							boolean serializationResult = child.serializeTurnament(selectedFile.getAbsolutePath());
							if (serializationResult==false) {
								throw new Exception("Turnering blev ikke serialiseret.");
							}
							else {
								JOptionPane.showMessageDialog(getWindow(), "Turneringen blev serialiseret til filen " + selectedFile.getAbsolutePath() + " som et Turnament objekt.",
										"Serialisering", JOptionPane.INFORMATION_MESSAGE);
							}
						}

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(getWindow(), "Kunne ikke deserialisere filen " + selectedFile.getAbsolutePath() + " som et Turnament objekt!",
								"Fejl ved deserialisering", JOptionPane.ERROR_MESSAGE);
						System.err.println("Turnament object in " + selectedFile + " could not be de-serialized :-(");
						ex.printStackTrace();
					}
				}
			}
		});

		minimizeAllWindowsMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JInternalFrame[] frames   = desktopPane.getAllFrames();
				for (JInternalFrame mDIChild : frames) {
					try {
						mDIChild.setIcon(true);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		closeAllWindowsMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JInternalFrame[] frames   = desktopPane.getAllFrames();
				for (JInternalFrame mDIChild : frames) {
					try {
						// test if object is a MDIChild (I want to skip the MDIChildTurnamentInfo child 
						if (mDIChild instanceof MDIChild ) {
							mDIChild.setClosed(true);
						}

					} catch (PropertyVetoException e1) {
						e1.printStackTrace();
					}
				}
				//Reposition the next "first" window
				childWindowNumber = 1;
			}
		});
		
		closeMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				System.exit(0);
			}
		});
		
	}
	
	protected Component getWindow() {
		// TODO Auto-generated method stub
		return this.getContentPane();
	}

	private void addMenuMneMonics(JMenu topMenu, JMenuItem jMenuItem, String menuTekst, int keyStroke, int KeyEvent) { 
		jMenuItem.setText(menuTekst);
		jMenuItem.setMnemonic(keyStroke);		
		jMenuItem.setAccelerator(KeyStroke.getKeyStroke( keyStroke, KeyEvent));
		topMenu.add(jMenuItem);
	}
	
	private void listChildMenusInWindowTopMenu() {

		// Easy household with MDI child windows. Strategy: Remove all, add known
		// Remove all menu items corresponding to actual windows (the 2 first menu items is Close All, and Minimize All + separator)

		// Remove all menuitems on "top" menu windowMenu
		int numberOfMenuItemsToRemove = windowMenu.getItemCount() - numberOfWindowBaseMenuItems;  
		for (int i = 0; i < numberOfMenuItemsToRemove; i++) {
			windowMenu.remove(numberOfWindowBaseMenuItems);
		}
		
		// Get all MDI childwindows, which unfortunately comes back as JInternalFrames - consider to override getAllFrames() ?! 
		JInternalFrame[] frames = desktopPane.getAllFrames();
		
		// need to "cast" (add to another list), in order to access MDIChild attributes :-(
		ArrayList<MDIChild> mDIChilds = new ArrayList<MDIChild>();
		for (JInternalFrame jInternalFrame : frames) {
			try {
				mDIChilds.add((MDIChild) jInternalFrame);				
			} catch (ClassCastException e) {
				//e.printStackTrace();
				// Don't want to see stacktrace in console that MDIChildTurnamentInfo can't be casted as MDIChild
			}

		}
		
		// Sort in order to get Window number 1 at top
		mDIChilds.sort(null);  // implemented Comparable interface on MDIChild which sorts by windowNumber
		
		for (MDIChild aChild : mDIChilds) {
			try {
				JMenuItem jMenuItem = new JMenuItem();
				jMenuItem.setText(Integer.toString(aChild.getWindowNumber()) + ". " + aChild.getWindowName());
				
				// Add a listener, so the menu item can activate the window via calling ActiveMDIChild(aChild.getWindowNumber() 
				jMenuItem.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						try {
							if (aChild  instanceof MDIChild  ) {
								ActiveMDIChild(aChild.getWindowNumber());								
							}

						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}

				});				
				
				windowMenu.add(jMenuItem);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void ActiveMDIChild(int windowNumber) throws Exception {
		JInternalFrame[] frames = desktopPane.getAllFrames();

		// need to cast, in order to access MDIChild attributes :-(
		ArrayList<MDIChild> mDIChilds = new ArrayList<MDIChild>();
		for (JInternalFrame jInternalFrame : frames) {
			try {
				if (jInternalFrame instanceof MDIChild) {
					mDIChilds.add((MDIChild) jInternalFrame);					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		for (MDIChild aChild : mDIChilds) {
			if (aChild.getWindowNumber() == windowNumber) {
				activateChild(aChild);				
				break;
			}
		}

	}
	
	private void activateChild(JInternalFrame child) throws Exception {
		if (child.isIcon()) {
			child.setIcon(false);
		}	
		child.moveToFront();
		child.setSelected(true);
	}

	// JInternalFrameListeners methods - could also have been done using Class InternalFrameAdapter
	// https://docs.oracle.com/javase/tutorial/uiswing/events/internalframelistener.html
	public void internalFrameClosing(InternalFrameEvent e) {
		MDIChild closingChildFrame = (MDIChild) e.getInternalFrame();
		closingChildFrame.turnament.setInActive();
		turnamentManager.removeInactiveTurnaments();		
		mDIChildTurnamentInfo.refreshInfoTable();
	}

	// 	Additional InternalFrameListeners methods must be implemented in order to "satisfy" interface
	public void internalFrameOpened(InternalFrameEvent e) {}
	public void internalFrameClosed(InternalFrameEvent e) {}
	public void internalFrameIconified(InternalFrameEvent e) {}
	public void internalFrameDeiconified(InternalFrameEvent e) {}
	public void internalFrameActivated(InternalFrameEvent e) {}
	public void internalFrameDeactivated(InternalFrameEvent e) {}
}

//plagiat from https://www.java-tips.org/java-se-tips-100019/15-javax-swing/39-creating-a-status-bar.html
class StatusBar extends JLabel {  
	private static final long serialVersionUID = 1;
    
    /** Creates a new instance of StatusBar */
    public StatusBar(int width, int hieght) {
        super();
        super.setPreferredSize(new Dimension(width, hieght));
        // plagiat from https://www.tutorialspoint.com/swingexamples/add_border_to_label.htm
        Border line = BorderFactory.createLineBorder(Color.DARK_GRAY);
        setBorder(line);
        setMessage("");
    }
     
    public void setMessage(String message) {
        setText(" "+message);        
    }        
}