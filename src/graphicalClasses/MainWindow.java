package graphicalClasses;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import dataHandlingClasses.*;
import utils.Serialize;

import java.awt.*;

public class MainWindow {
	
	public MDIFrame mainJFrame;
	JLayeredPane layeredPane;
	TurnamentManager turnamentManager;
	private static int childWindowsNumber = 1;
	private JMenu windowMenu;   
	public Dimension dim;
	public MainWindow() {
		super();
		
		try {
			turnamentManager = new TurnamentManager("Kan håndtere x turneringer, og har totallister over hold og spillere");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initMainWindows();
	};

	private boolean initMainWindows() {
	try {
		mainJFrame = new MDIFrame("Simulering af fodboldturneringer");
		layeredPane = mainJFrame.getLayeredPane();
	    //mainJFrame.setSize(1100, 1000);
		mainJFrame.setSize(500, 600);

	    JMenuBar  menubar = new JMenuBar();

	    JMenu     fileMenu   = new JMenu();
	    JMenuItem refreshMenu   = new JMenuItem();
	    JMenuItem newTurnamentMenu   = new JMenuItem();
	    JMenuItem newCupTurnamentMenu   = new JMenuItem();	    
	    JMenuItem loadSerializedTurnamentMenu = new JMenuItem();
	    JMenuItem saveSerializedTurnamentMenu = new JMenuItem();	    
	    JMenuItem closeMenu  = new JMenuItem();
	    
	    windowMenu   = new JMenu();	    

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
	    fileMenu.setMnemonic(KeyEvent.VK_V);

	    fileMenu.add(refreshMenu);
	    fileMenu.add(loadSerializedTurnamentMenu);

	    fileMenu.add(newTurnamentMenu);
	    fileMenu.add(newCupTurnamentMenu);	    
	    
	    fileMenu.add(closeMenu);
	    menubar.add(fileMenu);
	    menubar.add(windowMenu);	    

	    mainJFrame.setJMenuBar(menubar);

	    dim = Toolkit.getDefaultToolkit().getScreenSize();

	    // Center JFrame
	    mainJFrame.setLocation(dim.width/2-mainJFrame.getSize().width/2, dim.height/2-mainJFrame.getSize().height/2); //source https://stackoverflow.com/questions/12072719/centering-the-entire-window-java/34869895#34869895
	    mainJFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    mainJFrame.setExtendedState(MDIFrame.MAXIMIZED_BOTH); //.NORMAL
	    
	    newTurnamentMenu.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent e) {
	        	try {
	        		ArrayList<Team> test = turnamentManager.getTeamsOfLevel(0);
	        		Turnament turnament = new Turnament(test, turnamentManager.getContractPeriods(), "Superliga", LocalDate.of(2020, Month.AUGUST, 15), LocalDate.of(2021, Month.MAY, 14));
	        		addNewTurnament(turnament);

	        	} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }
	      });	    

	    newCupTurnamentMenu.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent e) {
	        	try {
	        		ArrayList<Team> teamsAtBothLevels = (turnamentManager.getTeamsOfLevel(0));
	        		teamsAtBothLevels.addAll(turnamentManager.getTeamsOfLevel(1));
	        		
	        		Turnament cupTurnament = new CupTurnament(teamsAtBothLevels, turnamentManager.getContractPeriods(), "Pokalturnering " + childWindowsNumber, LocalDate.of(2020, Month.SEPTEMBER, 15), LocalDate.of(2021, Month.JUNE, 2));
	        		addNewTurnament(cupTurnament);	        		

	        	} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }
	      });	    

	    refreshMenu.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent e) {
	        	try {
//	        		mainPanel.clearTables();
//	        		turnament.reGenerateGoals();
//	        		mainPanel.loadTeamsIntoTable();
	        		
				} catch (Exception e1) {
					// TODO Auto-generated catch block
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
	        	    
//	        		String turnamentManagerFileNameOfToday = "turnamentManager_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString() + ".ser";
//	        		Serialize.save(turnamentManager, turnamentManagerFileNameOfToday);		

	        		try {
	        			// How to de-serialize / restore a Turnament object
	        			Turnament deSerializedTurnamentObject = (Turnament) Serialize.load(selectedFile.getAbsolutePath());
	        			addNewTurnament(deSerializedTurnamentObject);
	        			//System.out.println("de-serialized TurnamentRestore object: number of goals " + deSerializedTurnamentManagerObject.getTurnaments().get(0).getNumberOfGoals());			
	        		} catch (Exception ex) {
	        			JOptionPane.showMessageDialog(null, "Kunne ikke deserialisere filen " + selectedFile.getAbsolutePath() + " som et Turnament objekt!",
	        				      "Fejl ved deserialisering", JOptionPane.ERROR_MESSAGE);
	        			System.err.println("Turnament object in " + selectedFile + " could not be de-serialized :-(");
	        			ex.printStackTrace();
	        		}
	        	    
	        	}
	        }
	      });

	    closeMenu.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent e) {
	          System.out.println("Farvel og tak til Jacob Nordfalk for kapitel 11 i OOP 6. udgave :-)");
	          System.exit(0);
	        }
	      });

	    
	    return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void addNewTurnament(Turnament turnament) {
		turnamentManager.addTurnament(turnament);
		MainPanel mainPanel = new MainPanel(turnament);
	    mainPanel.setLocation(childWindowsNumber*10, childWindowsNumber*10);
	    childWindowsNumber++;
	    mainPanel.setSize(900, 900);
	    mainJFrame.desktopPane.add(mainPanel);	    
	    mainPanel.setVisible(true);
	    JMenuItem childWindowMenu   = new JMenuItem();
	    childWindowMenu.setText(childWindowsNumber + " " + turnament.getName());
	    windowMenu.add(childWindowMenu);
	    layeredPane.moveToFront(mainPanel);
	    mainJFrame.repaint();
	    try {
	    	String turnamentFileNameOfToday = "turnament_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString() + ".ser";
	    	Serialize.save(turnament, turnamentFileNameOfToday);			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	    
	}

}
