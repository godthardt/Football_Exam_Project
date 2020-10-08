import java.util.ArrayList;
import java.util.Comparator;

public class Team implements Comparable<Team> {
	private String name;
	private int id;
	public int getId() {
		return this.id;
	}
	
	private int points = 0;
	private ArrayList<Player> players;

	public void resetPoints() {
		points=0;
	}

	public void addPoints(int newPoints) {
		points = points + newPoints;
	}

	public int getPoints() {
		return points;
	}

	public String getName() {
		return name;
	}

	public Team (int id, String name) {
		this.name = name;
		this.id = id;
		players = new ArrayList<Player>();
	}
	
	public int compareTo(Team t) {
		return this.name.compareTo(t.name);

	}

	public void print() {
		System.out.println(id + "\t" + name + "\t\t\t" + "Points = " + points);
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
