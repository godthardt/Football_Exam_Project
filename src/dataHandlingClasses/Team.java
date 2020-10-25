package dataHandlingClasses;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Team implements Comparable<Team>, Serializable {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects	
	private String name;
	private int id;
	public int getId() {
		return this.id;
	}
	
	private int points = 0;
	private ArrayList<Player> players;

	public Team (int id, String name) {
		this.name = name;
		this.id = id;
		players = new ArrayList<Player>();
	}

	public int getNumberOfPayersInTeam () {
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
		int randomNumber = (new Random().nextInt(players.size()));
		return players.get(randomNumber);
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
