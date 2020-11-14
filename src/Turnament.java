

import java.time.LocalDate;
import java.util.*;

import java.io.*;

public class Turnament implements Serializable {
	public Turnament(ArrayList<Team> turnamentTeams, String name, LocalDate startDate, LocalDate endDate) throws Exception {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.turnamentTeams = turnamentTeams;
		matches = new ArrayList<Match>();
		generateMatchesAndGoals();
		sortTeamsByPoint();
	}

	protected String name;  // In this case, is it not necessary to protect the attribute, since there is both a set and get method, but good style to demonstrate encapsulation 
	public String getName() { return name; };
	public void setName(String name) { this.name = name;}
	protected LocalDate startDate;
	public LocalDate GetStartDate() { return startDate; }
	protected LocalDate endDate;
	public LocalDate GetEndDate() { return endDate; }	
	protected int nextMatchId = 1;
	public int getNextMatchId() { return nextMatchId++; }

	protected int roundNumber = 1;
	protected static final long serialVersionUID = 3;  //Helps class control version of serialized objects
	protected ArrayList<Team> turnamentTeams;
	protected ArrayList<Match> matches;
	public  ArrayList<Match> getMatches() { return matches; }
	
	public int GetGoalsForPlayer(int playerId) {
		// Purpose: Find out how many goal a particular player has scored, in this turnament. Not optimized for efficiency
		int numberOfGoals = 0;
		for (Match match : matches) {
			for (Goal goal : match.getGoals()) {
				if (goal.getGoalScorer().getId() == playerId) {
					numberOfGoals++;
				}
			}
		}
		return numberOfGoals;
	}
	
	public Team GetTeam(int teamId) {
		// Purpose: Find a team from a teamId
		Team returnTeam = null;
		for (Team team : turnamentTeams) {
			if(team.getId() == teamId) {
				returnTeam = team;
				break;
			}
		}
		
		// This condition should never be met
		if (returnTeam== null) {
			throw new NullPointerException("Team not found in method GetTeam(teamID="+ teamId +")");  
		}
		
		return returnTeam;
	}

	public int getNumberOfMatches() {
		return  matches.size();
	}
	
	public void addMatch(Match match) {
		matches.add(match);
	}
	
	public ArrayList<Team> getTeams() {
		return turnamentTeams;
	}
	
	public String toString() {
		String returnString = "";
		for (Team team : turnamentTeams) {
			returnString = returnString + team.getName() + "\n"; 
		}
		return returnString;
	}
	
	public void sortTeamsAlfabetecally() {
		// Purpose: Sort teams, using the Team Class compareTo() method, which look at the name of the team 
		turnamentTeams.sort(null);
	}

	public void addGoal(int matchnumber, Goal.GoalType goaltype, int goalMinute, int goalSecond ) throws Exception {
		Match m = matches.get(matchnumber -1);//-1 because first matchnumber is 1 and first element in ArrryList is 0

		// if the match has ended, it should not be allowed to add goals
		if (m.getHasFinished() == true) {
			throw new Exception("Alarm, nogen prøver at tilføje et mål til en afsluttet kamp - kontakt spilletilsynet! (eller måske bare programmøren :-)");
		}
		
		// Pick a random goal scorer
		Player goalScorer;
		if (goaltype == Goal.GoalType.Home) {
			goalScorer = m.getHomeTeam().getRandomgoalScorer();
		} else {
			goalScorer = m.getAwayTeam().getRandomgoalScorer();			
		}
		
		m.addGoal(goaltype, goalMinute, goalSecond, goalScorer);
	}

	public void sortTeamsByPoint() {
		Collections.sort(turnamentTeams, new SortbyPoints(false));
		int rank = 1;
		for (Team team : turnamentTeams) {
			team.setRankInTurnament(rank);
			rank++;
		}
	}
	
	public void sortGoalsByTime()
	{
		for (Match match : matches) {
			Collections.sort(match.getGoals());			
		}
	}
	
	public void setPointsForTeams() {
		for (Match match : matches) {
			if (match.getHasFinished()) {
				switch (match.getVictoryType()) {
					case Home: {
						match.getHomeTeam().addPoints(Constants.pointForWin);
					}
					case Away:
					{
						match.getAwayTeam().addPoints(Constants.pointForWin);						
					}
					case Draw:
					{
						match.getHomeTeam().addPoints(Constants.pointForDraw);
						match.getAwayTeam().addPoints(Constants.pointForDraw);						
					}
				}
			}
		}
	}

	protected void generateMatchesAndGoals() throws Exception {

		long DaysBetweenStartAndEnd = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
		Random r = new Random();
		for (Team team : turnamentTeams) {
			for (int i = 0; i < turnamentTeams.size(); i++) {
				// If team not equals itself, set up a match
				if (turnamentTeams.get(i).getId() != team.getId()) {
					// Spread matches on a "pseudo" time line - not chronological order
					int nextAdd = r.nextInt((int) DaysBetweenStartAndEnd);
					LocalDate matchDate = this.startDate.plusDays(nextAdd); //NB Does not check that a team does not play more than one match a day :-(
					int roundNo = 0;//getNextRoundNo(team.getId());//(matches.size() % (turnamentTeams.size()*2 - 2)) + 1;
					System.out.println(team.getName() + " mod " + turnamentTeams.get(i).getName() + " runde " + roundNo);
					Match m = new Match(team, turnamentTeams.get(i), getNextMatchId(), roundNo, matchDate);
					addMatch(m);
					generateRandomGoals(m, false);
					System.out.println("Round = " + roundNo);
				}
			}
		}
	}

//	private int getNextRoundNo(int teamId) {
//		int result = 1;
//		for (Match match : matches) {
//			if ((match.getHomeTeam().getId() == teamId) || match.getAwayTeam().getId() == teamId ) {
//				result++;
//			}
//		}
//		return result;
//	}
	
	public int getNumberOfGoals() {
		int numberOfGoals = 0;
		for (Match match : matches) {
			numberOfGoals += match.getNumberOfGoals();
		}
		return numberOfGoals;
	}

	public void generateRandomGoals(Match match, boolean mustHaveWinner) throws Exception {
		final int stdMatchTimeMiutes = 90;
		Random r = new Random();
		int minuteScored = 0;
		int exstraTime = r.nextInt(Constants.maxExtraTimePrMatch);
		int numberOfGoalsToAdd = r.nextInt(Constants.maxGoalsPrMatch);

		// if it is it cup match, I need to have an uneven number of goals
		if (mustHaveWinner==true) {
			if (numberOfGoalsToAdd==0) {
				numberOfGoalsToAdd = 1;
			}
			else {
				// Make number of goals uneven -> match gets a winner
				if (numberOfGoalsToAdd % 2 == 0) {
					numberOfGoalsToAdd--;
				}
			}
		}

		if (match.getHomeTeam().getMustLoose()==Match.MustLoooeType.DeterminedToLoose) {
			// Home team looses 0-2
			addGoal(match.getMatchId(), Goal.GoalType.Away, r.nextInt(30), r.nextInt(59));// Random seconds
			addGoal(match.getMatchId(), Goal.GoalType.Away, r.nextInt(30) + 30, r.nextInt(59));// Random seconds	
		} else if (match.getAwayTeam().getMustLoose()==Match.MustLoooeType.DeterminedToLoose) {
			// Away team looses 2-0
			addGoal(match.getMatchId(), Goal.GoalType.Home, r.nextInt(30), r.nextInt(59));// Random seconds
			addGoal(match.getMatchId(), Goal.GoalType.Home, r.nextInt(30) + 30, r.nextInt(59));// Random seconds	
		} else {
			// Random victory
			for (int i = 0; i < numberOfGoalsToAdd; i++) {
				int homeOrAway = r.nextInt(2);
				int nextScoreMinute = r.nextInt(stdMatchTimeMiutes - minuteScored);
				minuteScored = nextScoreMinute;
				switch (homeOrAway) {
				case 0: {
					addGoal(match.getMatchId(), Goal.GoalType.Home, nextScoreMinute, r.nextInt(59));// Random seconds
					break;						
				}
				case 1: {
					addGoal(match.getMatchId(), Goal.GoalType.Away, nextScoreMinute, r.nextInt(59));
					break;						
				}
				}
			}
		}		
		match.endMatch(stdMatchTimeMiutes + exstraTime); // Add extra minutes 
	}

	public int getNumberOfTeams() {
		return turnamentTeams.size();
	}

	public int getHomeGoals(Team t) {
		int homeGoals = 0;
		for (Match m : matches) {
			if (m.getHomeTeam().getId()==t.getId()) {
				homeGoals += m.getHomeGoals();
			}
		}
		return homeGoals;
	}

	public int getAwayGoals(Team t) {
		int awayGoals = 0;
		for (Match m : matches) {
			if (m.getAwayTeam().getId()==t.getId()) {
				awayGoals += m.getHomeGoals();
			}
		}
		return awayGoals;
	}
	
	public int GetNumberOfMatchesForTeam(Team t) {
		int numberOfMatches = 0;
		for (Match m : matches) {
			if (m.getAwayTeam().getId()==t.getId() || m.getHomeTeam().getId()==t.getId()) {
				numberOfMatches ++;
			}
		}
		return numberOfMatches;
	}
	
	public int getHighestNumberOfPlayersInOneTeam() {
		// Purpose: Find the team with the highest number of players. Used for securing sufficient number of rows in GUI
		int highestNumber = 0;
		
		for (Team team : turnamentTeams) {
			if (team.getNumberOfPlayersInTeam() > highestNumber) {
				highestNumber = team.getNumberOfPlayersInTeam();
			}
		}

		return highestNumber; 
	}

	public void reGenerateGoals() throws Exception {
		// Remove existing matches with "attached" goals
		matches.clear();
		
		// Make sure new matches is "renumbered" from 1
		nextMatchId = 1;		
		
		for (Team team : turnamentTeams) {
			team.resetPointsAndGoals();
			team.kickin();
		}

		generateMatchesAndGoals();
		sortTeamsByPoint();
	}

	public boolean serializeTurnament(String filename) {
		// Purpose: save the current turnament into a filestream
		try {
			Serialize.save(this, filename);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
