

import java.io.Serializable;

public class Goal implements Comparable<Goal>, Serializable {

	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	
	public static enum GoalType { Home, Away}
	private int minute;  // The minute in which the goal was scored
	public int getMinute() { return minute;}
	private int sec; // The second in which the goal was scored
	public int getSecond() { return sec;}	
	private GoalType goalType;
	public GoalType getGoalType() { return goalType; }
	private Player goalScorer;
	public Player getGoalScorer() { return goalScorer; }
	
	public int compareTo(Goal b) {
		int result = 0;
		if (this.getMinute() > b.getMinute())
			result = 1;
		if (this.getMinute() < b.getMinute())
			result = -1;
		// If goals are scored in the same minute, then look at the seconds
		if (this.getMinute() == b.getMinute())
		{
			if (this.getSecond() > b.getSecond())
				result = 1;
			if (this.getSecond() < b.getSecond())
				result = -1;
			if (this.getSecond() == b.getSecond()) {
				result = 0;
			}

		}
		return result;
	}

	public Goal(GoalType goalType, int minute, int sec, Player goalScorer) { 
		this.minute = minute;
		this.sec = sec;
		this.goalType = goalType;
		this.goalScorer = goalScorer;
	}
}
