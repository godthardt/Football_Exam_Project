

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CupTurnament extends Turnament implements Serializable {

	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	public CupTurnament(ArrayList<Team> turnamentTeams, String name, LocalDate startDate, LocalDate endDate)
			throws Exception {
		super(turnamentTeams, name, startDate, endDate);

	}
	
	@Override
	protected void generateMatchesAndGoals() throws Exception {
		long DaysBetweenStartAndEnd = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
		Random r = new Random();

		int numberOfTeamsNextPowerOf2 = nextPowerOf2(turnamentTeams.size());
		
		
		// Add a number of "Sitter Outs" (in danish: "oversiddere") to match a power of two 
		for (int i = turnamentTeams.size(); i < numberOfTeamsNextPowerOf2; i++) {
			turnamentTeams.add(new Team(i, "Oversidder " + (i + 1), 1, new ArrayList<Player>(), Match.MustLoooeType.DeterminedToLoose));			
		}
		
		int round = 0;
		
		int numberOfRemaingTeams = 0;
		numberOfRemaingTeams = getNumberOfRemainingTeams();		
		
		while(numberOfRemaingTeams > 2) {  // if 2 teams back, then it's the final	
			round++;
			
			// sort by kicked out, so that I can pick remaining teams from start of the arrayList
			Collections.sort(turnamentTeams, new SortbyKickedOut());
			
			// Count number of reaming teams
			numberOfRemaingTeams = getNumberOfRemainingTeams();
			
			for (int i = 0; i < numberOfRemaingTeams; i+=2) {
				// Spread matches on a "pseudo" time line
				int nextAdd = r.nextInt((int) DaysBetweenStartAndEnd);
				LocalDate matchDate = this.startDate.plusDays(nextAdd); //NB Does not check that a team does not play more than one match a day :-(
				// set team 1 up against 2; 3 against 4, etc
				Match m = new Match(getMatchType(numberOfRemaingTeams), turnamentTeams.get(i), turnamentTeams.get(i+1), getNextMatchId(), round, matchDate);
				addMatch(m);
				generateRandomGoals(m, true);
				sortGoalsByTime();
			}
		}
	}
	
	private String getMatchType(int numberOfRemaingTeams) {
		switch (numberOfRemaingTeams) {
		case 4: 
			return "Semifinale";
		case 2: 
			return "Finale";
		default:
			return "Pokalkamp";
		}
	}
	
	private int getNumberOfRemainingTeams() {
		int count = 0;
		// Count number of reaming teams, in order to plan next round of matches
		for (Team team : turnamentTeams) {
			if (team.getkickedOut()==false) {
				count++;
			}
		}
		return count;
	}
	
	// purpose: find the next power of 2, in order to continue to be able to divide turnament in half, and keeping whole number of teams
	private int nextPowerOf2(int a) throws Exception 
	{
		int result = 1;
		if (a < 0) {
			throw new Exception("Method nextPowerOf2 only Works with input parameters above 0");
		}
		// not very elegant, but it works. I found another more elegant solution (something with n >>= 1;), but I would not be able to explain that :-(
		switch (a) {
		case 0: return 0;
		case 1: return 2;
		case 2: return 2;		
		default:
			while (result < a) {
				result = 2 * result; 
			}
		}

		return result;
	}
}
