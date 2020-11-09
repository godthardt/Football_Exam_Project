


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Team implements Comparable<Team>, Serializable {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects	
	private String name;
	private int id;
	private int points = 0;
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Contract> teamContracts;
	public ArrayList<Contract> getTeamContracts() { return teamContracts; } 
	private int level;  //eg. Superliga=0, 1.division=1, etc.
	public int getLevel() { return level; }
	private boolean kickedkOut = false;
	public boolean getkickedOut() { return kickedkOut; }
	private Match.MustLoooeType mustLoose; // true if the team is a virtual Sit Out team (på dansk oversidderhold)
	public Match.MustLoooeType getMustLoose() { return mustLoose; }
	
	public void kickOut() {
		kickedkOut = true;
	}

	// constructor
	public Team (int id, String name, int level, ArrayList<Contract> contracts, Match.MustLoooeType mustLoose) {
		this.name = name;
		this.id = id;
		this.level = level;
		this.teamContracts = contracts;
		this.mustLoose = mustLoose;
		try {
			populatePlayersFromContractPeriods();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// another constructor to support deep copy
	public Team(Team team) {
		this.name = team.name;
		this.id = team.id;
		this.level = team.level;
		// no need for deep copy of contracts, since they are never changed
		this.teamContracts = team.teamContracts;
		try {
			populatePlayersFromContractPeriods();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	private void populatePlayersFromContractPeriods() throws Exception {
		for (Contract contract : teamContracts) {
			if (contract.getTeamId() == id) {
				players.add(new Player(contract.getPlayerId(), contract.getPlayerName()));
			}
			else
				throw new Exception("Should not happen - wrong teamID in taems contracts");
		}
	}

	public int getId() {
		return this.id;
	}

	public int getNumberOfPlayersInTeam () {
		return players.size();
	}
	
	public void resetPoints() {
		points=0;
	}

	public void addPoints(int newPoints) {
		points = points + newPoints;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public int getPoints() {
		return points;
	}

	public String getName() {
		return name;
	}

	public int compareTo(Team t) {
		return this.name.compareTo(t.name);

	}

	@Override
    public String toString()
    {
        return (id + " " + name + " " + "Points = " + points);
    }
	
	public Player getRandomgoalScorer() {
		if (players.size() > 0) {
			int randomNumber = (new Random().nextInt(players.size()));
			return players.get(randomNumber);
		}
		// Return an object, in order not to get an exception
		// TODO Ensure that all team has attached players
		return new Player("Ukendt spiller");
	}

}

class SortbyPoints implements Comparator<Team> 
{ 
    public int compare(Team a, Team b) 
    { 
        int result = 0;
    	if (a.getPoints() > b.getPoints())
    		result = -1;
        if (a.getPoints() < b.getPoints())
        	result = 1;
// Burde implemeterer bedst målscore i forbindelse med pointlighed
//        if (a..getGolsScored() > b.getPoints()) {
//    		if (a.getcondition) {
//				
//			}
//        }

        return result;
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

