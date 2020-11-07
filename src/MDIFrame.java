

import java.awt.BorderLayout;
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
// but in Java this is not the case, but from Java 8 (check) JInternalFrame (as MDIchilds) (a lightweight JFrame is supplied to offer MDI functionality
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
	public Dimension dim;


	public MDIFrame(String title) {
		super(title);

		try {
			turnamentManager = new TurnamentManager("Kan håndtere x turneringer, og har totallister over hold og spillere");
		} catch (IOException e) {
			e.printStackTrace();
		}

		desktopPane = new JDesktopPane();
		desktopPane.setOpaque(false);
		
		setMDIFrameSize();		
		getContentPane().add(desktopPane,BorderLayout.CENTER);

		desktopManager = new DefaultDesktopManager();
		desktopPane.setDesktopManager(desktopManager);

		layeredPane = getLayeredPane();
		
		//setSize(Constants.mDIFrameWidth, Constants.mDIFrameHigth);

		addMenus();
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

			Turnament deSerializedTurnamentObject = (Turnament) Serialize.load(fileName);
			// Indicate to the GUI, that the turnament has been deserialized
			String newTurnamentName = deSerializedTurnamentObject.getName() + " - deserialiseret d. " + LocalDate.now().format(DateTimeFormatter.ofPattern(Constants.dkDateFormat)).toString();
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
		JMenuItem testMenu  = new JMenuItem();
		
		// Set text and shortcuts on sub menu items, and attach submenu to top menu
		addMenuMneMonics(fileMenu, refreshMenu, "Resimuler matchafvikling i valgt turnering", KeyEvent.VK_F5);		
		addMenuMneMonics(fileMenu, newTurnamentMenu, "Ny superliga", KeyEvent.VK_N);		
		addMenuMneMonics(fileMenu, newCupTurnamentMenu, "Ny pokalturnering", KeyEvent.VK_P);
		addMenuMneMonics(fileMenu, loadSerializedTurnamentMenu, "Åbn serialiseret turnering fra fil", KeyEvent.VK_O);
		addMenuMneMonics(fileMenu, saveSerializedTurnamentMenu, "Gem serialiseret turnering i fil", KeyEvent.VK_S);		
		addMenuMneMonics(fileMenu, closeMenu, "Afslut", KeyEvent.VK_A);
		addMenuMneMonics(fileMenu, testMenu, "Test", KeyEvent.VK_T);
		
		
		
		addMenuMneMonics(windowMenu, minimizeAllWindowsMenu, "Minimer alle Vinduer", KeyEvent.VK_M);		
		addMenuMneMonics(windowMenu, closeAllWindowsMenu, "Luk alle Vinduer", KeyEvent.VK_L);		
		
		menubar.add(fileMenu);
		menubar.add(windowMenu);	    

		setJMenuBar(menubar);
		
		// Add menu listeners
		testMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				listChildMenusInWindowTopMenu();
			}
		});	    

		

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
				fileChooser.setAcceptAllFileFilterUsed(false);				
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					System.out.println("Selected file: " + selectedFile.getAbsolutePath());
					try {
						// de-serialize a Turnament object
						addNewTurnament(selectedFile.getAbsolutePath());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Kunne ikke deserialisere filen " + selectedFile.getAbsolutePath() + " som et Turnament objekt!",
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
				FileFilter filter = new FileNameExtensionFilter("Serialiserede objekter (*.ser)","ser");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setAcceptAllFileFilterUsed(false);
				int result = fileChooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					System.out.println("Selected file: " + selectedFile.getAbsolutePath());
					try {
						MDIChild child = (MDIChild) desktopPane.getSelectedFrame();
						if (child != null) {
							boolean serializationResult = child.serializeTurnament(selectedFile.getAbsolutePath());
							if (serializationResult==false) {
								throw new Exception("Turnering blev ikke serialiseret.");
							}
							else {
								JOptionPane.showMessageDialog(null, "Turneringen blev serialisert til filen " + selectedFile.getAbsolutePath() + " som et Turnament objekt.",
										"Serialisering", JOptionPane.INFORMATION_MESSAGE);
							}
						}

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Kunne ikke deserialisere filen " + selectedFile.getAbsolutePath() + " som et Turnament objekt!",
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
	
	private void addMenuMneMonics(JMenu topMenu, JMenuItem jMenuItem, String menuTekst, int keyStroke) { 
		jMenuItem.setText(menuTekst);
		jMenuItem.setMnemonic(keyStroke);		
		jMenuItem.setAccelerator(KeyStroke.getKeyStroke( keyStroke, KeyEvent.CTRL_DOWN_MASK));
		topMenu.add(jMenuItem);
	}
	
	private void listChildMenusInWindowTopMenu() {

		// Remove all menu items corresponding to actual windows (the 2 first menu items is Close All, and Minimize All)
		for (int i = windowMenu.getItemCount() -1; i > 1; i--) {
			windowMenu.remove(2);
		}
		
		// Get all MDI childwindows, which unfortunately comes back as JInternalFrames - consider to override getAllFrames() ?! 
		JInternalFrame[] frames = desktopPane.getAllFrames();
		
		// need to cast, in order to access MDIChild attributes :-(
		ArrayList<MDIChild> mDIChilds = new ArrayList<MDIChild>();
		for (JInternalFrame jInternalFrame : frames) {
			mDIChilds.add((MDIChild) jInternalFrame);
		}
		
		// Sort in order to get Windows number 1 at top
		mDIChilds.sort(null);
		
		for (MDIChild aChild : mDIChilds) {
			try {
				JMenuItem jMenuItem = new JMenuItem();
				jMenuItem.setText(Integer.toString(aChild.getWindowNumber()) + ". " + aChild.getWindowName());
				
				jMenuItem.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						try {
							ActiveMDIChild(aChild.getWindowNumber());
						} catch (Exception e1) {
							// TODO Auto-generated catch block
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
