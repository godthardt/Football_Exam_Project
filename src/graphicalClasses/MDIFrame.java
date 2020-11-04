package graphicalClasses;

import java.awt.BorderLayout;
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
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import dataHandlingClasses.Constants;
import dataHandlingClasses.CupTurnament;
import dataHandlingClasses.Team;
import dataHandlingClasses.Turnament;
import dataHandlingClasses.TurnamentManager;
import utils.Serialize;

// MDI (Multiple Document interface) is built in to C++, C# and Delphi, and lot of functionality are build into classes like TMDIxxx clases -
// but in Java this is not the case, but from Java 8 (check) JInternalFrame (as MDI.childs) (a lightweight JFrame is supplied to offer MDI functionality
// I have sought inspiration from:
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
		getContentPane().add(desktopPane,BorderLayout.CENTER);

		desktopManager = new DefaultDesktopManager();
		desktopPane.setDesktopManager(desktopManager);

		layeredPane = getLayeredPane();
		setSize(Constants.mDIFrameWidth, Constants.mDIFrameHigth);

		addMenus();
		centerJFrame();
		setVisible(true);

		addNewTurnament("turnament_v3.ser");

	}

	private void centerJFrame() {
		try {
			dim = Toolkit.getDefaultToolkit().getScreenSize();

			// Center JFrame
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
			addNewTurnament(deSerializedTurnamentObject);	    
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Kunne ikke deserialisere filen " + fileName + " som et Turnament objekt! Exception:\n\n" + e.getMessage(),
					"Fejl ved deserialisering", JOptionPane.ERROR_MESSAGE);
			System.err.println("Turnament object in " + fileName + " could not be de-serialized :-(");
			e.printStackTrace();
		}
	}

	private void addNewTurnament(Turnament turnament) {
		turnamentManager.addTurnament(turnament);
		MDIChild mainPanel = new MDIChild(turnament);
		Insets i = this.getInsets(); // Insets contains top (size of titlebar), left, etc. of the "JFrame", found on https://www.programcreek.com/java-api-examples/?class=java.awt.Container&method=getInsets
		mainPanel.setLocation(childWindowNumber* i.top / 2, childWindowNumber * i.top / 2); //on my Pc i.top = 31
		childWindowNumber++;
		mainPanel.setSize(Constants.mDIChildWidth + i.left, Constants.mDIChildHigth + i.left); //on my Pc i.left = 8		
		desktopPane.add(mainPanel);	    
		mainPanel.setVisible(true);
		JMenuItem childWindowMenu   = new JMenuItem();
		childWindowMenu.setText(childWindowNumber + " " + turnament.getName());
		windowMenu.add(childWindowMenu);
		layeredPane.moveToFront(mainPanel);
		repaint();
//		try {
//			String turnamentFileNameOfToday = "turnament_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString() + ".ser";
//			Serialize.save(turnament, turnamentFileNameOfToday);			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private void addMenus() {
		JMenuBar  menubar = new JMenuBar();

		JMenu     fileMenu   = new JMenu();
		JMenuItem refreshMenu   = new JMenuItem();
		JMenuItem newTurnamentMenu   = new JMenuItem();
		JMenuItem newCupTurnamentMenu   = new JMenuItem();	    
		JMenuItem loadSerializedTurnamentMenu = new JMenuItem();
		JMenuItem saveSerializedTurnamentMenu = new JMenuItem();	    
		JMenuItem closeMenu  = new JMenuItem();

		windowMenu   = new JMenu();	    
		JMenuItem closeAllWindowsMenu  = new JMenuItem();
		JMenuItem minimizeAllWindowsMenu  = new JMenuItem();

		fileMenu.setText("Fil");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		refreshMenu.setText("Opdater");
		newTurnamentMenu.setText("Vis ny superliga");
		newCupTurnamentMenu.setText("Vis ny pokalturnering");	    
		loadSerializedTurnamentMenu.setText("Åbn serialiseret turnering fra fil");
		saveSerializedTurnamentMenu.setText("Gem serialiseret turnering i fil");
		closeMenu.setText("Afslut");
		closeMenu.setMnemonic(KeyEvent.VK_A);

		windowMenu.setText("Vindue");
		minimizeAllWindowsMenu.setText("Minimer alle Vinduer");		
		closeAllWindowsMenu.setText("Luk alle Vinduer");
		
		fileMenu.setMnemonic(KeyEvent.VK_V);

		fileMenu.add(refreshMenu);
		fileMenu.add(loadSerializedTurnamentMenu);
		fileMenu.add(saveSerializedTurnamentMenu);		

		fileMenu.add(newTurnamentMenu);
		fileMenu.add(newCupTurnamentMenu);	    

		fileMenu.add(closeMenu);
		menubar.add(fileMenu);
		
		windowMenu.add(minimizeAllWindowsMenu);
		windowMenu.add(closeAllWindowsMenu);		
		
		menubar.add(windowMenu);	    

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
					// ToDo!!! MDIChild recalc

				} catch (Exception e1) {
					e1.printStackTrace();
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
					} catch (PropertyVetoException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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
			}
		});


		
		closeMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				System.out.println("Farvel og tak til Jacob Nordfalk for kapitel 11 i OOP 6. udgave :-)");
				System.exit(0);
			}
		});

	}
	
	public void addPanel(MDIChild panel) {
		desktopPane.add(panel,JDesktopPane.DEFAULT_LAYER);
	}

}
