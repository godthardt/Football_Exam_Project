import java.io.IOException;
//import GUI.*;
import utils.Serialize;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class Main {

	public static void main(String[] args) throws Exception {
		TurnamentManager turnamentManager = new TurnamentManager("DBU"); 
		Turnament turnament = new Turnament(turnamentManager, "Superliga", LocalDate.of(2020, Month.AUGUST, 15), LocalDate.of(2021, Month.MAY, 14));
		turnamentManager.addTurnament(turnament);
		Turnament cupTurnament = new Turnament(turnamentManager, "Pokalturnering", LocalDate.of(2020, Month.SEPTEMBER, 15), LocalDate.of(2021, Month.JUNE, 2));
		turnamentManager.addTurnament(cupTurnament);		

		Serialize.save(turnament, "turnament_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString() + ".ser");
		Serialize.save(turnamentManager, "turnamentManager_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString() + ".ser");		
		
		// How to restore a turnament object 
		//Turnament turnamentRestore = (Turnament) Serialize.load("turnament.ser");
		//System.out.println("turnamentRestore goals " + turnamentRestore.getNumberOfGoals());
	
		// Start the Circus :-)
		new MainWindow(turnament);
	}
	
}
