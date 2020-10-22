import java.io.IOException;
//import GUI.*;
import utils.Serialize;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class Main {

	public static void main(String[] args) throws Exception {
		Turnament turnament = new Turnament("Superliga", LocalDate.of(2020, Month.SEPTEMBER, 15), LocalDate.of(2021, Month.JUNE, 2));

		Serialize.save(turnament, "turnament_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString() + ".ser");
		// Restore a turnament object 
		//Turnament turnamentRestore = (Turnament) Serialize.load("turnament.ser");
		//System.out.println("turnamentRestore goals " + turnamentRestore.getNumberOfGoals());
	
		// Start the Circus
		new MainWindow(turnament);
	}
	
}
