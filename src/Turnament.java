import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.io.*;

public class Turnament implements Serializable {
	public Turnament(String name, LocalDate startDate, LocalDate endDate) throws IOException {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		teams = new ArrayList<Team>();
		matches = new ArrayList<Match>();
		players = new ArrayList<Player>();
		contractPeriods = new ArrayList<ContractPeriod>();

		loadTeams("teams.txt");
		loadPlayers("players.txt");
		//System.out.println("Number of teams in turnament " + turnament.getNumberOfTeams());
		generateMatches();
		//System.out.println("Number of matches in turnament " + turnament.getNumberOfMatches());
		try {
			generateRandomGoals();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Number of goals in turnament " + turnament.getNumberOfGoals());
		
		//turnament.listMatches();
		
		//turnament.listTeamsAlfabetecally();
		//turnament.listTeamsByPoint(true);
		// serialize turnament into stream
		
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

	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private ArrayList<Team> teams;
	private ArrayList<Match> matches;
	public  ArrayList<Match> getMatches() { return matches; }
	private ArrayList<ContractPeriod> contractPeriods;
	private ArrayList<Player> players;
	public ArrayList<Player> getPlayers() { return players; }
	public ArrayList<ContractPeriod> getContractPeriods() {return contractPeriods; } 
	
	public Team GetTeam(int teamNumber) {
		return teams.get(teamNumber);
	}
	public void addTeam(Team team) {
		teams.add(team);
	}

	public int getNumberOfMatches() {
		return  matches.size();
	}
	
	public void addMatch(Match match) {
		matches.add(match);
	}
	
	public ArrayList<Team> getTeams() {
		return teams;
	}
	
	public String toString() {
		String returnString = "";
		for (Team team : teams) {
			returnString = returnString + team.getName() + "\n"; 
		}
		return returnString;
	}
	
	public boolean loadTeams(String filename) throws IOException {

		FileReader fil = new FileReader(filename);
		BufferedReader ind = new BufferedReader(fil);
		int lineNo = 0;
		try {
			
		String linje = ind.readLine();
		while (linje != null)
		{
			String[] arrOfStr = linje.split(",");
			addTeam(new Team(Integer.parseInt(arrOfStr[0]), arrOfStr[1]));
			linje = ind.readLine();
			lineNo++;
		}
		ind.close();
		return true;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while reading line number " + lineNo);
			e.printStackTrace();
			return false;			
		}

	}

	
	public int highestNumberOfPlayersInOneTeam() {
		//sort contracts by teamId 
		contractPeriods.sort(null);
		
		
        //java.util.List<String> strings = Arrays.asList("Zohne", "Redy", "Zohne", "Redy", "Stome");
//        java.util.Map<ContractPeriod, Long> teamCount = contractPeriods.stream().collect(Collectors.groupingBy(Integer -> Integer, Collectors.counting()));
//        int lineNo = 1;
//        teamCount.forEach((name, count) -> {
//            System.out.println(name.getTeamId() + ":" + count);
//
//        });		
		int currentTeamId;
		int prevTeamId = -1;
		int counter = 0;
		int highestNumber = 0;
		// loop over contractPeriods, to find the highest number of players in one team
		for (ContractPeriod contractPeriod : contractPeriods) {
			counter++;
			currentTeamId = contractPeriod.getTeamId();

			// is it a new team ?
			if (currentTeamId != prevTeamId) {
				// new highest ?
				if (counter > highestNumber) {
					highestNumber = counter;
					System.out.println("New highest team: " + contractPeriod.getTeamId() + " - highestNumber " + highestNumber); 
				}
				// start over on new teamId
				counter = 0;
			}
			prevTeamId = currentTeamId;
			
		}
		System.out.println("Highest number: " + highestNumber);
		return highestNumber;

	}
	
	public boolean loadPlayers(String filename) throws IOException {

		FileReader fil = new FileReader(filename);
		BufferedReader ind = new BufferedReader(fil);
		int lineNo = 0;
		try {

			String linje = ind.readLine();
			while (linje != null)
			{
				String[] firstArrOfStr = linje.split(",");
				// Input file line has format "Nicolaj Thomsen,52769,2021-06-30" the part after the last comma is the date for contract end)

				LocalDate contractExpire = LocalDate.parse(firstArrOfStr[2]);
				Player player = new Player(firstArrOfStr[0]);
				players.add(player);
				int teamId = Integer.parseInt(firstArrOfStr[1]);
				ContractPeriod contractPeriod = new ContractPeriod(player.getId(), teamId, contractExpire);
				contractPeriods.add(contractPeriod );
				linje = ind.readLine();
				lineNo++;			
			}
			ind.close();
			highestNumberOfPlayersInOneTeam();
			return true;
		} 
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while reading line number " + lineNo);
			e.printStackTrace();
			return false;			
		}

	}
	
	private void addContractPeriod(int parseInt) {
		// TODO Auto-generated method stub
		
	}

	public void SkrivTekstfil() throws IOException 
	{
//		FileWriter fil = new FileWriter("hold.txt");
//		PrintWriter ud = new PrintWriter(fil);
//		ud.println("1,Randers FC");
//		ud.println("2,AGF");
//		ud.println("3,OB");
//		ud.println("4,SønderjyskE");
//		ud.println("5,FC Midtjylland");
//		ud.println("6,FC Nordsjælland");
//		ud.println("7,Lyngby");
//		ud.println("8,FCK");
//		ud.println("9,AC Horsens");
//		ud.println("10,Aab");
//		ud.println("11,Vejle");
//		ud.println("12,Brøndby");		
//	    ud.close(); // luk så alle data skrives til disken

	}
	
	public void listTeamsAlfabetecally() {
		teams.sort(null);
		for (Team team : teams) {
			System.out.println(team.getName());			
		}
	}

	public void addGoal(int matchnumber, GoalType goaltype, int goalMinute, int goalSecond ) throws Exception {
		Match m = matches.get(matchnumber -1);//-1 because first matchnumber is 1 and first element in ArrryList is 0 
		m.addGoal(goaltype, goalMinute, goalSecond);
	}

	public void listTeamsByPoint(Boolean doPrint) {
		Collections.sort(teams, new SortbyPoints());
		if (doPrint) {
			for (int i = 0; i < teams.size(); i++) {
				teams.get(i).print();
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

	public void generateMatches() {
		
		long DaysBetweenStartAndEnd = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
		Random r = new Random();
		for (Team team : teams) 
		{
			for (int i = 0; i < teams.size(); i++) 
			{
				// If team not equals itself
				if (teams.get(i) != team) {
					int nextAdd = r.nextInt((int) DaysBetweenStartAndEnd);
					LocalDate matchDate = this.startDate.plusDays(nextAdd); //NB Does not check that a team does not play more than one match a day :-(
					Match m = new Match(team, teams.get(i), getNextMatchId(), matchDate);
					m.endMatch(90);
					addMatch(m);
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
	
	public void generateRandomGoals() throws Exception {
		final int stdMachTimeMiutes = 90;
		Random r = new Random();
		GoalType goaltype;
		int minuteScored = 0;
		int secondScored = 0;
		int exstraTime = r.nextInt(Constants.maxExtraTimePrMatch);
		
		for (Match match : matches) {
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
						goaltype = GoalType.Home;
						addGoal(match.getMatchNo(), goaltype, nextScoreMinute, r.nextInt(59));
						break;						
					}
					case 2: {
						goaltype = GoalType.Away;
						addGoal(match.getMatchNo(), goaltype, nextScoreMinute, r.nextInt(59));
						break;						
					}
				}
			}
			match.endMatch(90 + exstraTime); // Add up to 9 extra minutes 
		}
	}

	public int getNumberOfTeams() {
		return teams.size();
	}

	public void listMatches() {
		System.out.println("No.  Hometeam         Awayteam score homepoints awaypoints\n");		
		for (Match m : matches) {
			System.out.println( m.getMatchNo() + " " + m.getHomeTeam().getName() + " " + m.getAwayTeam().getName() +" " + m.getHomeGoals() + " " + m.getAwayGoals() + " " + m.getHomePoints() + " " + m.getAwayPoints());
		}

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
	
}
