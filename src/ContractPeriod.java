import java.sql.Date;

public class ContractPeriod {
	private Date startDate;
	private Date endDate;	
	private double TransferSum;
	private Boolean hasBeenExtended = false;
	private Player player; 
	public ContractPeriod(Player player) {
		this.player = player;
	}

}
