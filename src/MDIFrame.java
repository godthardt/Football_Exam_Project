

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.ScrollPane;
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
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

// MDI (Multiple Document interface) is built in to C++, C# and Delphi, and a lot of functionality are build into classes like TMDIxxx classes -
// but in Java this is not the case, but Java offers JInternalFrame (as MDIchilds) (a lightweight JFrame is supplied to offer MDI functionality
// I have sought inspiration at:
// http://www.java2s.com/Tutorials/Java/Java_Swing/1600__Java_Swing_MDI.htm 
// https://www.comp.nus.edu.sg/~cs3283/ftp/Java/swingConnect/friends/mdi-swing/mdi-swing.html
// https://www.comp.nus.edu.sg/~cs3283/ftp/Java/swingConnect/archive/tech_topics_arch/frames_panes/frames_panes.html

public class MDIFrame extends JFrame {
	private static final long serialVersionUID = 2;  //Helps class control version of serialized objects
	public JDesktopPane desktopPane = new JDesktopPane();
	public DesktopManager desktopManager;
	private JLayeredPane layeredPane;
	private TurnamentManager turnamentManager;
	private static int childWindowNumber = 1;
	private JMenu windowMenu;
	private int numberOfWindowBaseMenuItems;
	public Dimension dim;


	public MDIFrame(String title) {
		super(title);

		try {
			// The turnamentManager is not important, and a relatively late invention. Main purpose is to demonstrate polymorf traversion
			turnamentManager = new TurnamentManager("Kan håndtere x turneringer, og har totallister over hold og spillere");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Add a
		desktopPane = new JDesktopPane();
		
		// Make sure other component aren't visibel "below"
		desktopPane.setOpaque(false);
		
		setMDIFrameSize();		
		getContentPane().add(desktopPane,BorderLayout.CENTER);

		// According to oracle documentation, a desktopmanager should be implemented. I have tried without, and the program still works as expected.
		// Link to Oracle documentation: https://docs.oracle.com/javase/7/docs/api/javax/swing/DesktopManager.html
		desktopManager = new DefaultDesktopManager();
		desktopPane.setDesktopManager(desktopManager);

		
		// The Pane has 5 build in layers, which lives on top of each other, eg. a dragging layer at top. It's also posible to implement user defined layers (I don't use this feature)
		layeredPane = getLayeredPane();

		// Add menu "handling / initialization in sub method, in order to better readability constructor
		addMenus();
		
		// Center main window (not tested with multiple screens)  
		centerJFrame();
		
		setVisible(true);
		
		// Deserialize a Turnament object, in order to have something to look at
		addNewTurnament("serializedTurnamentExample.ser");

	}
	
	private void setMDIFrameSize() {
		dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Make sure there is space for the mainwindow on my old laptop ;-)
		if ((dim.width < Constants.mDIFrameWidth) || (dim.height < Constants.mDIFrameHigth)) {
			setSize(dim.width, dim.height -50);
			JOptionPane.showMessageDialog(this, "Den aktuelle skærmopløsning giver ikke plads til den ønskede vinduesstørrelse - nogle ting kan se forkerte ud",
					"Skærmopløsning", JOptionPane.WARNING_MESSAGE);			
		}
		else {
			setSize(Constants.mDIFrameWidth, Constants.mDIFrameHigth);
		}
	}

	private void centerJFrame() {
		try {
			dim = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2); //source https://stackoverflow.com/questions/12072719/centering-the-entire-window-java/34869895#34869895
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setExtendedState(MDIFrame.NORMAL); //.NORMAL or MAXIMIZED_BOTH

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addNewTurnament(String fileName) {
		try {

			// Restore Turnament object from file
			Turnament deSerializedTurnamentObject = (Turnament) Serialize.load(fileName);

			// Indicate to the GUI, that the turnament has been deserialized, by adding info to title line
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
		
		// Create a new 
		MDIChild mainPanel = new MDIChild(childWindowNumber, turnament);
		Insets i = this.getInsets(); // Insets contains top (size of titlebar), left, etc. of the "JFrame", found on https://www.programcreek.com/java-api-examples/?class=java.awt.Container&method=getInsets
		mainPanel.setLocation(childWindowNumber* i.top / 2, childWindowNumber * i.top / 2); //on my Pc i.top = 31
		childWindowNumber++;
		mainPanel.setSize(Constants.mDIChildWidth + i.left, Constants.mDIChildHigth + i.left); //on my Pc i.left = 8		
		desktopPane.add(mainPanel);	    
		mainPanel.setVisible(true);
		layeredPane.moveToFront(mainPanel);
		repaint();
		listChildMenusInWindowTopMenu();
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
		JMenuItem loadSerializedTurnamentMenu = new JMenuItem();
		JMenuItem saveSerializedTurnamentMenu = new JMenuItem();	    
		JMenuItem closeMenu  = new JMenuItem();
		JMenuItem closeAllWindowsMenu  = new JMenuItem();
		JMenuItem minimizeAllWindowsMenu  = new JMenuItem();
		
		// Set text and shortcuts on sub menu items, and attach submenu to top menu, 
		// found inspiration on https://www.codejava.net/java-se/swing/setting-shortcut-key-and-hotkey-for-menu-item-and-button-in-swing
		addMenuMneMonics(fileMenu, refreshMenu, "Resimuler matchafvikling i valgt turnering", KeyEvent.VK_F5, 0);		
		addMenuMneMonics(fileMenu, newTurnamentMenu, "Ny superliga", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);		
		addMenuMneMonics(fileMenu, newCupTurnamentMenu, "Ny pokalturnering", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
		addMenuMneMonics(fileMenu, loadSerializedTurnamentMenu, "Åbn serialiseret turnering fra fil", KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
		addMenuMneMonics(fileMenu, saveSerializedTurnamentMenu, "Gem serialiseret turnering i fil", KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
		fileMenu.add(new JSeparator()); // plagiat from http://www.java2s.com/Tutorial/Java/0240__Swing/AddSeparatortoJMenu.htm		
		addMenuMneMonics(fileMenu, closeMenu, "Afslut", KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK);
		
		addMenuMneMonics(windowMenu, minimizeAllWindowsMenu, "Minimer alle Vinduer", KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);		
		addMenuMneMonics(windowMenu, closeAllWindowsMenu, "Luk alle Vinduer", KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK);
		windowMenu.add(new JSeparator()); // plagiat from http://www.java2s.com/Tutorial/Java/0240__Swing/AddSeparatortoJMenu.htm
		numberOfWindowBaseMenuItems = windowMenu.getItemCount(); 
		
		// add top menus to menubar
		menubar.add(fileMenu);
		menubar.add(windowMenu);	    

		// add menu to MDIFrame
		setJMenuBar(menubar);
		
		// Add menu listeners
		newTurnamentMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					ArrayList<Team> test = turnamentManager.getTeamsOfLevel(0);
					Turnament turnament = new Turnament(test, turnamentManager.getContractPeriods(), "Superliga", LocalDate.of(2020, Month.AUGUST, 15), LocalDate.of(2021, Month.MAY, 14));
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

					Turnament cupTurnament = new CupTurnament(teamsAtBothLevels, turnamentManager.getContractPeriods(), "Pokalturnering " + childWindowNumber, LocalDate.of(2020, Month.SEPTEMBER, 15), LocalDate.of(2021, Month.JUNE, 2));
					addNewTurnament(cupTurnament);	        		

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});	    

		refreshMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					MDIChild mDIchild = (MDIChild) desktopPane.getSelectedFrame();
					mDIchild.regenerateGoals();
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
						mDIChild.setClosed(true);
					} catch (PropertyVetoException e1) {
						e1.printStackTrace();
					}
				}
				//Reposition the next "first" window
				childWindowNumber = 1;
				// Update the window menu
				listChildMenusInWindowTopMenu();
			
			}
		});
		
		closeMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				System.out.println("Farvel og tak til Jacob Nordfalk for kapitel 11 i OOP 6. udgave :-)");
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
			mDIChilds.add((MDIChild) jInternalFrame);
		}
		
		// Sort in order to get Window number 1 at top
		mDIChilds.sort(null);  // implemented Comparable interface om MDIChild which sorts by windowNumber
		
		for (MDIChild aChild : mDIChilds) {
			try {
				JMenuItem jMenuItem = new JMenuItem();
				jMenuItem.setText(Integer.toString(aChild.getWindowNumber()) + ". " + aChild.getWindowName());
				
				// Add a listener, so the menu item can activate the window via calling ActiveMDIChild(aChild.getWindowNumber() 
				jMenuItem.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						try {
							ActiveMDIChild(aChild.getWindowNumber());
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
			mDIChilds.add((MDIChild) jInternalFrame);
		}

		for (MDIChild aChild : mDIChilds) {
			if (aChild.getWindowNumber() == windowNumber) {
				if (aChild.isIcon()) {
					aChild.setIcon(false);
				}	
				aChild.moveToFront();
				aChild.setSelected(true);
				break;

			}
		}

	}
	
	public void addPanel(MDIChild panel) {
		desktopPane.add(panel,JDesktopPane.DEFAULT_LAYER);
	}


}
