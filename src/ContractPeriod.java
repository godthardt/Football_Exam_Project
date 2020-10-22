import java.io.Serializable;
import java.time.LocalDate;

public class ContractPeriod implements Comparable<ContractPeriod>, Serializable {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	//private LocalDate startDate;
	private LocalDate endDate;
	public LocalDate  getEndDatee() { return endDate;}
	//private double TransferSum;
	//private Boolean hasBeenExtended = false;
	private int playerId;
	public int getPlayerId() { return playerId; }	

	private int teamId;
	public int getTeamId() { return teamId; }
	public ContractPeriod(int playerId, int teamId, LocalDate endDate) {
		this.playerId = playerId;
		this.teamId = teamId; 
		this.endDate = endDate;
	}
	
	public int compareTo(ContractPeriod c) {
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
