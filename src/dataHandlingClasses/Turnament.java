package dataHandlingClasses;

import java.time.LocalDate;
import java.util.*;

//import TurnamentManager;

import java.io.*;

public class Turnament implements Serializable {
	public Turnament(ArrayList<Team> turnamentTeams, ArrayList<Contract> turnamentContracts , String name, LocalDate startDate, LocalDate endDate) throws Exception {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.turnamentTeams = turnamentTeams;
		this.players = new ArrayList<Player>();
		//this.turnamentContractPeriods = turnamentContracts;
		populatePlayers();
		matches = new ArrayList<Match>();
		generateMatchesAndGoals();
		listTeamsByPoint(false);
		//"Turnering: " + turnament.getName() + " - afvikles fra " + turnament.GetStartDate().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString() + " til " + turnament.GetEndDate().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString()
		//turnament.listTeamsAlfabetecally();
	
	}

	private void populatePlayers() {
		// TODO Auto-generated method stub
		
	}


	private String name;
	public String getName() { return name; };
	private LocalDate startDate;
	public LocalDate GetStartDate() { return startDate; }
	private LocalDate endDate;
	public LocalDate GetEndDate() { return endDate; }	
	private int nextMatchId = 1;
	public int getNextMatchId() {
		return nextMatchId++;
	}

	private static final long serialVersionUID = 2;  //Helps class control version of serialized objects
	private ArrayList<Team> turnamentTeams;
	private ArrayList<Match> matches;
	public  ArrayList<Match> getMatches() { return matches; }
	private ArrayList<Player> players;
	public ArrayList<Player> getPlayers(LocalDate matchDay, int teamID) { return players; } //TODO filter players
	//private ArrayList<Contract> turnamentContractPeriods;
	
	public Team GetTeam(int teamId) {
		Team returnTeam = null;
		for (Team team : turnamentTeams) {
			if(team.getId() == teamId) {
				returnTeam = team;
				break;
			}
		}
		
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
	
	public void listTeamsAlfabetecally(boolean doPrint) {
		turnamentTeams.sort(null);
		if (doPrint) {
			for (Team team : turnamentTeams) {
				System.out.println(team.getName());			
			}
		}		
	}

	public void addGoal(int matchnumber, Goal.GoalType goaltype, int goalMinute, int goalSecond ) throws Exception {
		Match m = matches.get(matchnumber -1);//-1 because first matchnumber is 1 and first element in ArrryList is 0
		// Pick a random goalscorer
		Player goalScorer;
		if (goaltype == Goal.GoalType.Home) {
			goalScorer = m.getHomeTeam().getRandomgoalScorer();
		} else {
			goalScorer = m.getAwayTeam().getRandomgoalScorer();			
		}
		
		m.addGoal(goaltype, goalMinute, goalSecond, goalScorer);
	}

	public void listTeamsByPoint(Boolean doPrint) {
		Collections.sort(turnamentTeams, new SortbyPoints());
		if (doPrint) {
			for (int i = 0; i < turnamentTeams.size(); i++) {
				turnamentTeams.get(i).print();
			}
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
						match.getHomeTeam().addPoints(3);
					}
					case Away:
					{
						match.getAwayTeam().addPoints(3);						
					}
					case Draw:
					{
						match.getHomeTeam().addPoints(1);
						match.getAwayTeam().addPoints(1);						
					}
				}
			}
		}
	}

	public void generateMatchesAndGoals() throws Exception {
		
		long DaysBetweenStartAndEnd = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
		Random r = new Random();
		for (Team team : turnamentTeams) {
			int roundNo = 1;
			for (int i = 0; i < turnamentTeams.size(); i++) {
				// If team not equals itself
				if (turnamentTeams.get(i).getId() != team.getId()) {
					int nextAdd = r.nextInt((int) DaysBetweenStartAndEnd);
					LocalDate matchDate = this.startDate.plusDays(nextAdd); //NB Does not check that a team does not play more than one match a day :-(
					Match m = new Match(team, turnamentTeams.get(i), getNextMatchId(), matchDate, roundNo++);
					m.endMatch(90);
					addMatch(m);
					generateRandomGoals(m);
				}
			}
		}
	}
	
	public int getNumberOfGoals() {
		int numberOfGoals = 0;
		for (Match match : matches) {
			numberOfGoals += match.getNumberOfGoals();
		}
		return numberOfGoals;
	}
	
	public void generateRandomGoals(Match match) throws Exception {
		final int stdMachTimeMiutes = 90;
		Random r = new Random();
		int minuteScored = 0;
		int exstraTime = r.nextInt(Constants.maxExtraTimePrMatch);

		int numberOfGoalsToAdd = r.nextInt(Constants.maxGoalsOnePrMatch);

		for (int i = 0; i < numberOfGoalsToAdd; i++) {
			int homeOrAway = r.nextInt(3);
			int nextScoreMinute = r.nextInt(stdMachTimeMiutes - minuteScored);
			minuteScored = nextScoreMinute;
			switch (homeOrAway) {
			case 0: {
				// 0=no goal "pseudo random" ;-)
				break;
			}
			case 1: {
				addGoal(match.getMatchNo(), Goal.GoalType.Home, nextScoreMinute, r.nextInt(59));
				break;						
			}
			case 2: {
				addGoal(match.getMatchNo(), Goal.GoalType.Away, nextScoreMinute, r.nextInt(59));
				break;						
			}
			}
		}
		match.endMatch(90 + exstraTime); // Add up to 9 extra minutes 
	}

	public int getNumberOfTeams() {
		return turnamentTeams.size();
	}

//	public void listMatches() {
//		System.out.println("No.  Hometeam         Awayteam score homepoints awaypoints\n");		
//		for (Match m : matches) {
//			System.out.println( m.getMatchNo() + " " + m.getHomeTeam().getName() + " " + m.getAwayTeam().getName() +" " + m.getHomeGoals() + " " + m.getAwayGoals() + " " + m.getHomePoints() + " " + m.getAwayPoints());
//		}
//
//	}

	public int getHomeGoals(Team t) {
		int homeGoals = 0;
		for (Match m : matches) {
			if (m.getHomeTeam().getId()==t.getId()) {
				homeGoals += m.getHomeGoals();
			}
		}
		return homeGoals;
	}

	
	public GoalResult goalsScoredAndTakenForTeam(Team t) {
		GoalResult gr = new GoalResult();
		for (Match m : matches) {
			if (m.teamInvolvedInMatch(t)==true) {
				if (m.getHomeTeam().getId()==t.getId()) {
					gr.scored += m.getHomeGoals();
					gr.taken += m.getAwayGoals();
				}
				// the parameter team (t) is the awayteam, since it is involved, and not the home team
				else {
					gr.scored += m.getAwayGoals();
					gr.taken += m.getHomeGoals();
				}
					
				//awayGoals += m.getHomeGoals();
			}

		}
		return gr;		
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
		int highestNumber = 0;
		
		for (Team team : turnamentTeams) {
			if (team.getNumberOfPlayersInTeam() > highestNumber) {
				highestNumber = team.getNumberOfPlayersInTeam();
			}
		}
		//System.out.println("highestNumber of player in one team " + highestNumber);
		return highestNumber; 

	}

	public void reGenerateGoals() throws Exception {
		matches.clear();
		nextMatchId = 1;
		generateMatchesAndGoals();
		listTeamsByPoint(false);
	}
	
	
}
