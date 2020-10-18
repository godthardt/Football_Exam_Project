

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.io.*;

public class Turnament implements Serializable {
	public Turnament(String name, LocalDate startDate, LocalDate endDate) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		teams = new ArrayList<Team>();
		matches = new ArrayList<Match>();
	}
	
	private String name;
	private LocalDate startDate;
	private int nextMatchId = 1;
	public int getNextMatchId() {
		return nextMatchId++;
	}

	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private LocalDate endDate;
	private ArrayList<Team> teams;
	private ArrayList<Match> matches;	
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

	public boolean indLaesHold(String filename) throws IOException {

		FileReader fil = new FileReader(filename);
		BufferedReader ind = new BufferedReader(fil);

		try {
			
		String linje = ind.readLine();
		while (linje != null)
		{
			String[] arrOfStr = linje.split(",");
			addTeam(new Team(Integer.parseInt(arrOfStr[0]), arrOfStr[1]));
			//System.out.println("Added: "+linje);
			linje = ind.readLine();
		}
		ind.close();
		return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;			
		}

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

	public void addGoal(int matchnumber, GoalType goaltype ) throws Exception {
		Match m = matches.get(matchnumber -1);//-1 fordi først Matchnumber er 1, første element i ArrryList er 0 
		m.addGoal(goaltype);
	}

	public void listTeamsByPoint(Boolean doPrint) {
		// TODO Auto-generated method stub
		Collections.sort(teams, new SortbyPoints());
		if (doPrint) {
			for (int i = 0; i < teams.size(); i++) {
				teams.get(i).print();
			}
		}
	}
	
	public void setPointsForTeams() {
		for (Match match : matches) {
			if (match.getHasFinished()) {
				switch (match.getVictoryType()) {
					case Home: {
						match.homeTeam.addPoints(3);
					}
					case Away:
					{
						match.awayTeam.addPoints(3);						
					}
					case Draw:
					{
						match.homeTeam.addPoints(1);
						match.awayTeam.addPoints(1);						
					}
				}
			}
			
		}
	}

	public void generateMatches() {
		for (Team team : teams) 
		{
			for (int i = 0; i < teams.size(); i++) 
			{
				if (i+1 != team.getId()) {
					Match m = new Match(team, teams.get(i), getNextMatchId());
					m.endMatch(90);
					addMatch(m);
				}
			}
		}
		//System.out.println("Number of matches " + matches.size());
	}
	
	public int getNumberOfGoals() {
		int numberOfGoals = 0;
		for (Match match : matches) {
			numberOfGoals += match.getNumberOfGoals();
		}
		return numberOfGoals;
		
	}
	
	public void generateRandomGoals() throws Exception {
		Random r = new Random();
		Random homeAway = new Random();
		GoalType goaltype;
		
		for (Match match : matches) {
			int numberOfGoalsToAdd = r.nextInt(10);
			
			for (int i = 0; i < numberOfGoalsToAdd; i++) {
				int homeOrAway = homeAway.nextInt(3); 
				switch (homeOrAway) {
					case 0: {
						// 0=no goal "pseudo random" ;-)
						break;
					}
					case 1: {
						goaltype = GoalType.Home;
						addGoal(match.getMatchNo(), goaltype);
						break;						
					}
					case 2: {
						goaltype = GoalType.Away;
						addGoal(match.getMatchNo(), goaltype);
						break;						
					}
				}
			}
			match.endMatch(90 + r.nextInt(10)); // Add up to 9 extra minutes 
		}
	}

	public int getNumberOfTeams() {
		return teams.size();
	}
	public void listMatches() {
		// TODO Auto-generated method stub
			System.out.println("No.  Hometeam         Awayteam score homepoints awaypoints\n");		
		for (Match m : matches) {
			//System.out.println( "%d   %2$20s %20s        %d-%d %d %d\n", m.getMatchNo(), m.homeTeam.getName(), m.awayTeam.getName(), m.getHomeGoals(), m.getAwayGoals(), m.getHomePoints(), m.getAwayPoints());
			System.out.println( m.getMatchNo() + " " + m.homeTeam.getName() + " " + m.awayTeam.getName() +" " + m.getHomeGoals() + " " + m.getAwayGoals() + " " + m.getHomePoints() + " " + m.getAwayPoints());
		}
		
	}
	
}
