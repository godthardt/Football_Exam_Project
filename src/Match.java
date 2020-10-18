

import java.io.Serializable;
import java.util.ArrayList;

enum VictoryType { Home, Away, Draw}

public class Match implements Serializable {
	public Match (Team homeTeam, Team awayTeam, int matchNo) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.matchNo = matchNo;
		goals = new ArrayList<Goal>();
	}
	
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private boolean hasFinished = false;
	public boolean getHasFinished() {
		return hasFinished;
	}
	public void endMatch (int minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
		hasFinished = true;
		if (homeGoals > awayGoals) {
			victoryType = VictoryType.Home;
			homePoints = 3;
			awayPoints = 0;			
		}
		
		if (homeGoals < awayGoals) {
			victoryType = VictoryType.Away;
			awayPoints = 3;
			homePoints = 0;			
		} 

		if (homeGoals == awayGoals) {
			victoryType = VictoryType.Draw;
			homePoints = 1;
			awayPoints = 1;			
		}
		
		homeTeam.addPoints(homePoints);
		awayTeam.addPoints(awayPoints);
	}
	
	public Team homeTeam;
	public Team awayTeam;
	private int matchNo;
	private int homeGoals = 0;
	private int awayGoals = 0;
	private int homePoints = 0;
	private int awayPoints = 0;
	
	public int getHomePoints() {
		return homePoints;
	}
	public int getAwayPoints() {
		return awayPoints;
	}
	
	private ArrayList<Goal> goals;
	private int minutesPlayed = 0;
	private VictoryType victoryType;
	public VictoryType getVictoryType() {
		return victoryType;
	}
	
	public int getMatchNo() {
		return this.matchNo;
	}
	public int getHomeGoals() {
		return homeGoals;
	}
	public int getAwayGoals() {
		return awayGoals;
	}
	
	public int getNumberOfGoals() {
		return awayGoals + homeGoals;
	}
	public void addGoal(GoalType goaltype) {
		// TODO Auto-generated method stub
		if (goaltype==GoalType.Home)
		{
			homeGoals++;
		}
		else 
		{
			awayGoals++;
		}
			
	}
	
	
}
