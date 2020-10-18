

import java.io.Serializable;

enum GoalType { Home, Away}

public class Goal implements Serializable {
	
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private int minute;
	private int sec;
	private Object goalScorer;

	public Goal(int minute, int sec) {
		this.minute = minute;
		this.sec = sec;
		this.goalScorer = goalScorer;
	}

}
