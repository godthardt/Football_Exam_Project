

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class CupTurnament extends Turnament implements Serializable {

	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	public CupTurnament(ArrayList<Team> turnamentTeams, ArrayList<Contract> turnamentContractPeriods , String name, LocalDate startDate, LocalDate endDate)
			throws Exception {
		super(turnamentTeams, turnamentContractPeriods, name, startDate, endDate);
		// TODO Auto-generated constructor stub
	}
	
	

}
