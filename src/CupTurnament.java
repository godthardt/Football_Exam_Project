

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CupTurnament extends Turnament implements Serializable {

	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	public CupTurnament(ArrayList<Team> turnamentTeams, ArrayList<Contract> turnamentContractPeriods , String name, LocalDate startDate, LocalDate endDate)
			throws Exception {
		super(turnamentTeams, turnamentContractPeriods, name, startDate, endDate);

	}
	
	@Override
	protected void generateMatchesAndGoals() throws Exception {

		long DaysBetweenStartAndEnd = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
		Random r = new Random();

		int numberOfRounds = 5;// Math.round(turnamentTeams.size() / 2); // there are half the number of teams rounds (halfed every round)
		System.out.println("numberOfRounds = " + numberOfRounds);
		

		int numberOfTeamsNextPowerOf2 = nextPowerOf2(turnamentTeams.size());
		
		// Add a number of "Sitter Outs" (på dansk oversiddere) to match a power of two 
		for (int i = turnamentTeams.size(); i < numberOfTeamsNextPowerOf2; i++) {
			turnamentTeams.add(new Team(i, "Oversidder " + (i + 1), 1, new ArrayList<Contract>(), true));			
		}
		
		System.out.println("new turnamentTeams.size = " + turnamentTeams.size());
		
		for (int round = 0; round < numberOfRounds; round++) {
			
			System.out.println("Roundno = " + round);
			
			// sort by kicked out
			Collections.sort(turnamentTeams, new SortbyKickedOut());
			
			// Count number of reaming teams
			int numberOfRemaingTeams = 0;
			for (Team team : turnamentTeams) {
				if (team.getkickedOut()==false) {
					numberOfRemaingTeams++;
				}
			}
			
			System.out.println("numberOfRemaingTeams = " + numberOfRemaingTeams);

			int counter = 0;
			for (int i = 0; i < numberOfRemaingTeams; i+=2) {

//				Team homeTeam = null;
//				Team awayTeam = null;
//				
//				while ((homeTeam==null) && (counter < numberOfRemaingTeams)) {
//					if (turnamentTeams.get(counter).getkickedOut() == false) {
//						homeTeam = turnamentTeams.get(counter);
//						System.out.println("Hometeam found: " + homeTeam.getName() + " Roundno = " + round + " i = ");
//						counter++;
//						break;
//					}
//					counter++;
//				}
//				
//				while ((awayTeam==null) && (counter < numberOfRemaingTeams)) {
//					if (turnamentTeams.get(counter).getkickedOut() == false) {
//						awayTeam = turnamentTeams.get(counter);
//						System.out.println("AwayTeam found: " + awayTeam.getName() + " Roundno = " + round + " i = ");						
//						break;
//					}
//					counter++;
//				}
//				
//				if (homeTeam==null) {
//					homeTeam = new Team(-1, "Oversidder", 1, new ArrayList<Contract>(), true);
//				} 
//				
//				if (awayTeam==null) {
//					awayTeam = new Team(-1, "Oversidder", 1, new ArrayList<Contract>(), true);
//				} 
//				
				// set team 1 up against 2; 3 against 4, etc
				// Spread matches on a "pseudo" time line
				int nextAdd = r.nextInt((int) DaysBetweenStartAndEnd);
				LocalDate matchDate = this.startDate.plusDays(nextAdd); //NB Does not check that a team does not play more than one match a day :-(
				Match m = new Match(turnamentTeams.get(i), turnamentTeams.get(i+1), getNextMatchId(), round+1, matchDate);
				//Match m = new Match(homeTeam, awayTeam, getNextMatchId(), round+1, matchDate);
				System.out.println("Round " + round + " Match : " + turnamentTeams.get(i).getName() + " - " + turnamentTeams.get(i+1).getName());
				addMatch(m);
				generateRandomGoals(m, true);
				
			}
			
			
		}

		System.out.println("Number of matches = " + matches.size());	

	}
	
	private int nextPowerOf2(int a)  // plagiat: https://stackoverflow.com/questions/5242533/fast-way-to-find-exponent-of-nearest-superior-power-of-2 
    {
        int b = 1;
        while (b < a)
        {
            b = b << 1;
        }
        System.out.println("a = " + a + " b = " +b );
        return b;
    }	
	

}
