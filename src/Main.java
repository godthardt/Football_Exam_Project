import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import utils.Serialize;

// project packages
import dataHandlingClasses.CupTurnament;
import dataHandlingClasses.Team;
import dataHandlingClasses.Turnament;
import dataHandlingClasses.TurnamentManager;
import graphicalClasses.*;

public class Main {

	public static void main(String[] args) throws Exception {
		
		

//		String turnamentManagerFileNameOfToday = "turnamentManager_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString() + ".ser";
//		Serialize.save(turnamentManager, turnamentManagerFileNameOfToday);		
//
//		try {
//			// How to de-serialize / restore a TurnamentManager object
//			TurnamentManager deSerializedTurnamentManagerObject = (TurnamentManager) Serialize.load(turnamentManagerFileNameOfToday);
//			System.out.println("de-serialized TurnamentRestore object: number of goals " + deSerializedTurnamentManagerObject.getTurnaments().get(0).getNumberOfGoals());			
//		} catch (Exception e) {
//			System.out.println("TurnamentManager object could not be de-serialized :-(");
//			e.printStackTrace();
//		}

		// Start the Circus :-)
		MainWindow mainWindow = new MainWindow();
		
//	    MainPanel mainPanel = new MainPanel(turnament);
//	    mainPanel.setLocation(40, 40);
//	    mainPanel.setSize(900, 900);
//	    mainWindow.mainJFrame.desktopPane.add(mainPanel);	    
//	    mainPanel.setVisible(true);


//	    MainPanel mainPanel2 = new MainPanel(cupTurnament);
//	    mainPanel2.setLocation(10, 10);
//	    mainPanel2.setSize(900, 900);	    
//	    mainPanel2.setVisible(true);	    
//	    mainWindow.mainJFrame.desktopPane.add(mainPanel2);
	    
	    mainWindow.mainJFrame.setVisible(true);
		
	}
	
}
