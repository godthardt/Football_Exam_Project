
import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDate;

public class Match implements Serializable {
	enum VictoryType { Home, Away, Draw}
	enum 	MustLoooeType { RandomLoose, DeterminedToLoose } //, HomeTeamDeterminedToLoose, AwayTeamDeterminedToLoose} 
	
	public Match (Team homeTeam, Team awayTeam, int matchId, int roundNo, LocalDate matchDate) {
		this.matchId = matchId;
		this.roundNo = roundNo;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.date = matchDate;
		// Is the home team an "out sitter" and must therefore loose
		if (homeTeam.getMustLoose()==Match.MustLoooeType.DeterminedToLoose) {
			this.mustWin = Match.MustLoooeType.DeterminedToLoose;			
		// Is the away team an "out sitter" and must therefore loose
		} else if (awayTeam.getMustLoose()==Match.MustLoooeType.DeterminedToLoose) {
			this.mustWin = Match.MustLoooeType.DeterminedToLoose;			
		// Normal procedure: random winnner
		} else {
			this.mustWin = Match.MustLoooeType.RandomLoose;
		}
		goals = new ArrayList<Goal>();
	}
	
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private boolean hasFinished = false;
	public boolean getHasFinished() { return hasFinished; }
	private Team homeTeam;
	public Team getHomeTeam() {return homeTeam;}
	private Team awayTeam;
	public Team getAwayTeam() {return awayTeam;}	
	private int matchId;
	public int getMatchId() { return this.matchId; }
	private int roundNo;
	public int getRoundNo() { return this.roundNo; }
	private LocalDate date;
	public LocalDate getDate() { return this.date; }
	private MustLoooeType mustWin;
	public MustLoooeType	getMustWin() { return mustWin;} 
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
	public int getMinutesPlayed() { return minutesPlayed;};
	
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

	public void addGoal(Goal.GoalType goaltype, int scoreMinute, int scoreSecond, Player goalScorer) {
		// TODO Auto-generated method stub
		if (goaltype==Goal.GoalType.Home)
		{
			homeGoals++;
		}
		else 
		{
			awayGoals++;
						
		}
		goals.add(new Goal(goaltype, scoreMinute, scoreSecond, goalScorer));
			
	}

	public void endMatch (int minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
		hasFinished = true;
		this.homeTeam.setHomeGoals(homeGoals);
		this.homeTeam.setAwayGoals(awayGoals);
		
		this.awayTeam.setHomeGoals(awayGoals);
		this.awayTeam.setAwayGoals(homeGoals);
		
		
		if (homeGoals > awayGoals) {
			victoryType = VictoryType.Home;
			homePoints = 3;
			awayPoints = 0;
			awayTeam.kickOut(); // cup speciality
		} else
		
		if (homeGoals < awayGoals) {
			victoryType = VictoryType.Away;
			awayPoints = 3;
			homePoints = 0;		
			homeTeam.kickOut();  // cup speciality
		} else

		if (homeGoals == awayGoals) {
			victoryType = VictoryType.Draw;
			homePoints = 1;
			awayPoints = 1;			
		}
		
		homeTeam.addPoints(homePoints);
		awayTeam.addPoints(awayPoints);
	}

}
