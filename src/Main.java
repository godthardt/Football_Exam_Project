import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.awt.event.*;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


import utils.Serialize;

// project packages
import dataHandlingClasses.CupTurnament;
import dataHandlingClasses.Team;
import dataHandlingClasses.Turnament;
import dataHandlingClasses.TurnamentManager;
import graphicalClasses.*;

public class Main {

	public static void main(String[] args) throws Exception {
		TurnamentManager turnamentManager = new TurnamentManager("DBU");
		ArrayList<Team> test = turnamentManager.getTeamsOfLevel(0);
		Turnament turnament = new Turnament(test, turnamentManager.getContractPeriods(), "Superliga", LocalDate.of(2020, Month.AUGUST, 15), LocalDate.of(2021, Month.MAY, 14));
		turnamentManager.addTurnament(turnament);
		//turnamentManager.addTurnament();
		
		ArrayList<Team> teamsAtBothLevels = (turnamentManager.getTeamsOfLevel(0));
		teamsAtBothLevels.addAll(turnamentManager.getTeamsOfLevel(1));
		
		Turnament cupTurnament = new CupTurnament(teamsAtBothLevels, turnamentManager.getContractPeriods(), "Pokalturnering", LocalDate.of(2020, Month.SEPTEMBER, 15), LocalDate.of(2021, Month.JUNE, 2));
		turnamentManager.addTurnament(cupTurnament);		

		String turnamentManagerFileNameOfToday = "turnamentManager_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString() + ".ser";
		Serialize.save(turnamentManager, turnamentManagerFileNameOfToday);		

		try {
			// How to de-serialize / restore a TurnamentManager object
			TurnamentManager deSerializedTurnamentManagerObject = (TurnamentManager) Serialize.load(turnamentManagerFileNameOfToday);
			System.out.println("de-serialized TurnamentRestore object: number of goals " + deSerializedTurnamentManagerObject.getTurnaments().get(0).getNumberOfGoals());			
		} catch (Exception e) {
			System.out.println("TurnamentManager object could not be de-serialized :-(");
			e.printStackTrace();
		}

		// Start the Circus :-)
		MainWindow mainWindow = new MainWindow();
		
	    MainPanel mainPanel = new MainPanel(turnament);
	    mainPanel.setLocation(40, 40);
	    mainPanel.setSize(300, 300);
	    mainWindow.mainJFrame.add(mainPanel);	    
	    mainPanel.setVisible(true);


//	    MainPanel mainPanel2 = new MainPanel(cupTurnament);
//	    mainPanel2.setLocation(80, 80);
//	    mainPanel2.setSize(400, 400);	    
//	    mainPanel2.setVisible(true);	    
//	    mainWindow.mainJFrame.add(mainPanel2);
	    
	    mainWindow.mainJFrame.setVisible(true);
		
	}
	
}
