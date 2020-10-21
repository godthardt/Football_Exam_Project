import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDate;

enum VictoryType { Home, Away, Draw}

public class Match implements Serializable {
	public Match (Team homeTeam, Team awayTeam, int matchNo, LocalDate matchDate) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.matchNo = matchNo;
		this.date = matchDate;
		goals = new ArrayList<Goal>();
	}
	
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private boolean hasFinished = false;
	public boolean getHasFinished() { return hasFinished; }
	private Team homeTeam;
	public Team getHomeTeam() {return homeTeam;}
	private Team awayTeam;
	public Team getAwayTeam() {return awayTeam;}	
	private int matchNo;
	public int getMatchNo() { return this.matchNo; }
	private LocalDate date;
	public LocalDate getDate() { return this.date; }
	private int homeGoals = 0;
	public int getHomeGoals() { return homeGoals; }
	private int awayGoals = 0;
	public int getAwayGoals() { return awayGoals; }	
	private int homePoints = 0;
	public int getHomePoints() { return homePoints; }
	private int awayPoints = 0;
	public int getAwayPoints() { return awayPoints; }
	private ArrayList<Goal> goals;
	private int minutesPlayed = 0;
	private VictoryType victoryType;
	public VictoryType getVictoryType() { return victoryType; }
	public ArrayList<Goal> getGoals() { return goals; }

	public boolean teamInvolvedInMatch(Team t) {
		if (t==this.homeTeam || t==this.awayTeam) {
			return true;
		}
		return false;
	}
	
	public int getNumberOfGoals() {
		return awayGoals + homeGoals;
	}

	public void addGoal(GoalType goaltype, int scoreMinute, int scoreSecond) {
		// TODO Auto-generated method stub
		if (goaltype==GoalType.Home)
		{
			homeGoals++;
			goals.add(new Goal(GoalType.Home, scoreMinute, scoreSecond));
		}
		else 
		{
			awayGoals++;
			goals.add(new Goal(GoalType.Away, scoreMinute, scoreSecond));			
		}
			
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

}
