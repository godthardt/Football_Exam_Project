package graphicalClasses;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import javax.swing.*;

import dataHandlingClasses.*;
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
		
		//this.turnament = turnament;
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
//	        		turnamentManager.addTurnament(turnament);
//	        		MainPanel mainPanel = new MainPanel(turnament);
//	        	    mainPanel.setLocation(childWindowsNumber*10, childWindowsNumber*10);
//	        	    childWindowsNumber++;
//	        	    mainPanel.setSize(900, 900);
//	        	    mainJFrame.desktopPane.add(mainPanel);	    
//	        	    mainPanel.setVisible(true);

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
//	        		turnamentManager.addTurnament(cupTurnament);		
//
//	        		MainPanel mainPanel2 = new MainPanel(cupTurnament);
//	        		mainPanel2.setLocation(childWindowsNumber*10, childWindowsNumber*10);
//	        		childWindowsNumber++;
//	        	    mainPanel2.setSize(900, 900);	    
//	        	    mainPanel2.setVisible(true);	    
//	        	    mainJFrame.desktopPane.add(mainPanel2);

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
	        	//fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
	        	int result = fileChooser.showOpenDialog(null);
	        	if (result == JFileChooser.APPROVE_OPTION) {
	        	    File selectedFile = fileChooser.getSelectedFile();
	        	    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
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
	    
	}

}
