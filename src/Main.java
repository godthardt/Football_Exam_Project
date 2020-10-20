import java.io.IOException;
//import GUI.*;
import utils.Serialize;

import java.time.LocalDate;
import java.time.Month;



public class Main {

	public static void main(String[] args) throws Exception {
		Turnament turnament = new Turnament("Superliga", LocalDate.of(2020, Month.SEPTEMBER, 15), LocalDate.of(2021, Month.JUNE, 2));

		turnament.indLaesHold("hold.txt");
		System.out.println("Number of teams in turnament " + turnament.getNumberOfTeams());
		turnament.generateMatches();
		System.out.println("Number of matches in turnament " + turnament.getNumberOfMatches());
		turnament.generateRandomGoals();
		System.out.println("Number of goals in turnament " + turnament.getNumberOfGoals());
		
		turnament.listMatches();
		
		//turnament.listTeamsAlfabetecally();
		turnament.listTeamsByPoint(true);
		// serialize turnament into stream
		Serialize.save(turnament, "turnament.ser");
		
		// Restore a turnament object 
		Turnament turnamentRestore = (Turnament) Serialize.load("turnament.ser");
		System.out.println("turnamentRestore goals " + turnamentRestore.getNumberOfGoals());
	
		MainWindow mainWindow = new MainWindow(turnament);
		//mainWindow.initMainWindows();
		//mainWindow.
		//mainWindow.mainJFrame.repaint();
		

	}
	
}
