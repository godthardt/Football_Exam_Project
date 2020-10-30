package graphicalClasses;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import dataHandlingClasses.Turnament;
import dataHandlingClasses.TurnamentManager;

import java.awt.*;
import java.time.format.DateTimeFormatter;


public class MainWindow {
	
	public MDIFrame mainJFrame;
	TurnamentManager turnamentManager;
	//public Turnament turnament;
	public Dimension dim;
	public MainWindow() {
		super();
		//this.turnament = turnament;
		try {
			turnamentManager = new TurnamentManager("DBU");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initMainWindows();
	};

	private boolean initMainWindows() {
	try {
		mainJFrame = new MDIFrame("Simulering af fodboldturneringer");
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

	    fileMenu.setText("Fil");
	    fileMenu.setMnemonic(KeyEvent.VK_F);
	    refreshMenu.setText("Opdater");
	    loadSerializedTurnamentMenu.setText("Åbn serialiseret turnering fra fil");
	    saveSerializedTurnamentMenu.setText("Gem serialiseret turnering i fil");
	    closeMenu.setText("Afslut");
	    closeMenu.setMnemonic(KeyEvent.VK_A);

	    fileMenu.add(refreshMenu);
	    fileMenu.add(loadSerializedTurnamentMenu);	    
	    fileMenu.add(closeMenu);
	    menubar.add(fileMenu);

	    mainJFrame.setJMenuBar(menubar);

	    dim = Toolkit.getDefaultToolkit().getScreenSize();
	    // Center JFrame
	    mainJFrame.setLocation(dim.width/2-mainJFrame.getSize().width/2, dim.height/2-mainJFrame.getSize().height/2); //source https://stackoverflow.com/questions/12072719/centering-the-entire-window-java/34869895#34869895
	    mainJFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    mainJFrame.setExtendedState(mainJFrame.NORMAL);
	    
	    

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

}
