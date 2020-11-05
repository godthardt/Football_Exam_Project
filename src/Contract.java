
import java.io.Serializable;
import java.time.LocalDate;

public class Contract implements Comparable<Contract>, Serializable {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	//private LocalDate startDate;
	private LocalDate endDate;
	public LocalDate  getEndDatee() { return endDate;}
	//private double TransferSum;
	//private Boolean hasBeenExtended = false;
	private int playerId;
	public int getPlayerId() { return playerId;	}	
	private String playerName; 
	public String getPlayerName() { return playerName; }	

	private int teamId;
	public int getTeamId() { return teamId; }
	public Contract(int playerId, String playerName, int teamId, LocalDate endDate) {
		this.playerId = playerId;
		this.teamId = teamId; 
		this.endDate = endDate;
		this.playerName = playerName;
	}
	
	public int compareTo(Contract c) {
		int result = 0;
		if (this.teamId > c.teamId)
			result = 1;
		if (this.teamId < c.teamId)
			result = -1;
		if (this.teamId == c.teamId)
		{
			result = 0;
		}
		return result;
	}
	
	@Override
	public String toString() {
		return  Integer.toString(teamId);
	}
	
}
