package graphicalClasses;

import java.awt.event.*;
import javax.swing.*;

import dataHandlingClasses.Turnament;

import java.awt.*;
import java.time.format.DateTimeFormatter;


public class MainWindow {
	
	private JFrame mainJFrame;
	public Turnament turnament;
	public Dimension dim;
	public MainWindow(Turnament turnament) {
		super();
		this.turnament = turnament;
		initMainWindows();
	};

	private boolean initMainWindows() {
	try {
		mainJFrame = new JFrame("Turnering: " + turnament.getName() + " - afvikles fra " + turnament.GetStartDate().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString() + " til " + turnament.GetEndDate().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString());
	    mainJFrame.setSize(1100, 1000);

	    JMenuBar  menubar = new JMenuBar();

	    JMenu     fileMenu   = new JMenu();
	    JMenuItem refreshMenu   = new JMenuItem();
	    JMenuItem closeMenu  = new JMenuItem();

	    fileMenu.setText("Fil");
	    fileMenu.setMnemonic(KeyEvent.VK_F);
	    refreshMenu.setText("Opdater");
	    closeMenu.setText("Afslut");
	    closeMenu.setMnemonic(KeyEvent.VK_A);

	    fileMenu.add(refreshMenu);
	    fileMenu.add(closeMenu);
	    menubar.add(fileMenu);

	    mainJFrame.setJMenuBar(menubar);

	    dim = Toolkit.getDefaultToolkit().getScreenSize();
	    // Center JFrame
	    mainJFrame.setLocation(dim.width/2-mainJFrame.getSize().width/2, dim.height/2-mainJFrame.getSize().height/2); //source https://stackoverflow.com/questions/12072719/centering-the-entire-window-java/34869895#34869895
	    mainJFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    MainPanel mainPanel = new MainPanel(this, turnament);
	    mainJFrame.add(mainPanel);
	    //mainJFrame.add(new footballJPanel());
	    mainJFrame.setVisible(true);
	    

	    refreshMenu.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent e) {
	        	try {
	        		mainPanel.clearTables();
	        		turnament.reGenerateGoals();
	        		mainPanel.loadTeamsIntoTable();
	        		
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }
	      });	    
	    
	    closeMenu.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent e) {
	          System.out.println("Farvel!");
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
