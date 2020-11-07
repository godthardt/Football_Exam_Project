


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
	private boolean kicedkOut = false;
	public boolean getkickedOut() { return kicedkOut; }

	// constructor
	public Team (int id, String name, int level, ArrayList<Contract> contracts) {
		this.name = name;
		this.id = id;
		this.level = level;
		this.teamContracts = contracts;
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
	
	
	public void print() {
		System.out.println(id + "\t" + name + "\t\t\t" + "Points = " + points);
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
        if (a.getPoints() == b.getPoints())
        	result = 0;
        return result;
    } 
} 
