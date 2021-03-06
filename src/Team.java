


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Team implements Comparable<Team>, Serializable {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private String name;
	private int id;
	private int points = 0;
	private int rankInTurnament = 0; //Winner of turnament has rankInTurnament=1 number 2 
	private ArrayList<Player> teamPlayers;
	public ArrayList<Player> getTeamPlayers() { return teamPlayers; } 
	private int level;  //eg. Champion League=-1, Superliga=0, 1.division=1, etc.
	public int getLevel() { return level; }
	private boolean kickedkOut = false; // out of Cup turnament
	public boolean getkickedOut() { return kickedkOut; }
	private Match.MustLoooeType mustLoose; // true if the team is a virtual "Sit Out team" (in danish "oversidderhold")
	public Match.MustLoooeType getMustLoose() { return mustLoose; }
	private int homeGoals = 0;
	public int getHomeGoals() { return homeGoals; }
	public void setHomeGoals(int goals) { homeGoals += goals; }
	private int awayGoals = 0;
	public int getAwayGoals() { return awayGoals; }
	public void setAwayGoals(int goals) { awayGoals += goals; }
	public int goalDifference() { return homeGoals - awayGoals;}
	
	// Called, when a team must leave a cupTurnament
	public void kickOut() {
		kickedkOut = true;
	}
	
	// Part of reset procedure
	public void kickin() {
		kickedkOut = false;
	}

	// constructor
	public Team (int id, String name, int level, ArrayList<Player> teamPlayers, Match.MustLoooeType mustLoose) {
		this.name = name;
		this.id = id;
		this.level = level;
		this.teamPlayers = teamPlayers;
		this.mustLoose = mustLoose;
	}
	
	// another constructor to support deep copy
	public Team(Team team) {
		this.name = team.name;
		this.id = team.id;
		this.level = team.level;
		// no need for deep copy of teamPlayers, since they are never changed
		this.teamPlayers = team.teamPlayers;
    }

	public int getId() {
		return this.id;
	}

	public int getNumberOfPlayersInTeam () {
		return teamPlayers.size();
	}
	
	public void resetPointsAndGoals() {
		points = 0;
		homeGoals = 0;
		awayGoals = 0;
	}

	public void addPoints(int newPoints) {
		points = points + newPoints;
	}

	public void addPlayer(Player player) {
		teamPlayers.add(player);
	}

	public int getPoints() {
		return points;
	}

	public String getName() {
		return name;
	}

	public int compareTo(Team t) {
		//Default sorter. Is being called, when somebody tries to .sort(null) a list containing Team objects 
		return this.name.compareTo(t.name);
	}

	@Override
    public String toString()
    {
        return (id + " " + name + " " + "Points = " + points);
    }
	
	public Player getRandomgoalScorer() {
		if (teamPlayers.size() > 0) {
			int randomNumber = (new Random().nextInt(teamPlayers.size()));
			return teamPlayers.get(randomNumber);
		}
		// Return an object, in order not to get an exception
		return new Player("Ukendt spiller", getId(), LocalDate.now());
	}

	public void setRankInTurnament(int rank) {
		this.rankInTurnament = rank;
	}
	
	public int getRankInTurnament() {
		return this.rankInTurnament;
	}
	

}

class SortbyName implements Comparator<Team> 
{
	private boolean sortAscending;
	public SortbyName(boolean sortAscending) {
		super();
		this.sortAscending = sortAscending;
	}

	public int compare(Team a, Team b) 
    { 
        int result = 0;
        result = a.compareTo(b); // use default sorter

		if (sortAscending==false) 
			return result;
		else
			return result * -1;
    } 
}

class SortbyPoints implements Comparator<Team> 
{ 
	private boolean sortAscending;
	public SortbyPoints(boolean sortAscending) {
		super();
		this.sortAscending = sortAscending;
	}
	
	public int compare(Team a, Team b) 
	{ 
		// pseudo: Most Points, Biggest goalDifference, most Homegoals 
		int result = 0;
		if (a.getPoints() > b.getPoints())
			result = -1;
		if (a.getPoints() < b.getPoints())
			result = 1;
		if (a.getPoints() == b.getPoints()) {
			if (a.goalDifference() > b.goalDifference()) 
				result = -1;
			if (a.goalDifference() < b.goalDifference()) 
				result = 1;
			if (a.goalDifference() == b.goalDifference()) {
				if(a.getHomeGoals() > b.getHomeGoals())
					result = -1;
				if(a.getHomeGoals() < b.getHomeGoals())
					result = 1;
			} // goaldifference ==
		}  // if point==

		if (sortAscending==true) 
			return result * -1;
		else
			return result;
	}
}	

class SortbyNumberOfMatches implements Comparator<Team> 
{ 
	private boolean sortAscending;
	public SortbyNumberOfMatches(boolean sortAscending) {
		super();
		this.sortAscending = sortAscending;
	}

	public int compare(Team a, Team b) 
    { 
        int result = 0;
		if (a.goalDifference() > b.goalDifference()) 
			result = -1;
		if (a.goalDifference() < b.goalDifference()) 
			result = 1;
		// If goal difference is equal, then look at most home goals
		if (a.goalDifference() == b.goalDifference()) {
			if(a.getHomeGoals() > b.getHomeGoals())
				result = -1;
			if(a.getHomeGoals() < b.getHomeGoals())
				result = 1;
		} 

		if (sortAscending==true) 
			return result;
		else
			return result * -1;

    } 
} 


class SortbyGoalScore implements Comparator<Team> 
{ 
	private boolean sortAscending;
	public SortbyGoalScore(boolean sortAscending) {
		super();
		this.sortAscending = sortAscending;
	}

	public int compare(Team a, Team b) 
    { 
        int result = 0;
		if (a.goalDifference() > b.goalDifference()) 
			result = -1;
		if (a.goalDifference() < b.goalDifference()) 
			result = 1;
		// If goal difference is equal, then look at most home goals
		if (a.goalDifference() == b.goalDifference()) {
			if(a.getHomeGoals() > b.getHomeGoals())
				result = -1;
			if(a.getHomeGoals() < b.getHomeGoals())
				result = 1;
		} 

		if (sortAscending==true) 
			return result;
		else
			return result * -1;

    } 
} 

class SortbyRanking implements Comparator<Team> 
{
	private boolean sortAscending;
	public SortbyRanking(boolean sortAscending) {
		super();
		this.sortAscending = sortAscending;
	}

	public int compare(Team a, Team b) 
    { 
        int result = 0;
		if (a.getRankInTurnament() > b.getRankInTurnament()) 
			result = -1;
		if (a.getRankInTurnament() < b.getRankInTurnament()) 
			result = 1;

		if (sortAscending==true) 
			return result;
		else
			return result * -1;
    } 
}

class SortbyKickedOut implements Comparator<Team> 
{ 

	public int compare(Team a, Team b) 
    { 
        int result = 0;
    	if ((a.getkickedOut() == false) && (b.getkickedOut() == true))
    		result = -1;
    	if ((a.getkickedOut() == true) && (b.getkickedOut() == false))
        	result = 1;
        return result;
    } 
} 



