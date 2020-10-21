import java.io.Serializable;
import java.time.LocalDate;

public class ContractPeriod implements Serializable {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private LocalDate startDate;
	private LocalDate endDate;	
	private double TransferSum;
	private Boolean hasBeenExtended = false;
	private int playerId;
	private int teamId;
	public int getTeamId() { return teamId; }
	public ContractPeriod(int playerId, int teamId) {
		this.playerId = playerId;
		this.teamId = teamId; 
	}

	public int getPlayerId() {
		return playerId;
	}

}
