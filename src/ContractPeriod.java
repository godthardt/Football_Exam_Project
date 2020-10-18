

import java.io.Serializable;
import java.sql.Date;

public class ContractPeriod implements Serializable {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private Date startDate;
	private Date endDate;	
	private double TransferSum;
	private Boolean hasBeenExtended = false;
	private Player player; 
	public ContractPeriod(Player player) {
		this.player = player;
	}

}
