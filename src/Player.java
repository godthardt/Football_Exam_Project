import java.io.Serializable;
import java.time.LocalDate;

public class Player implements Serializable {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private static int nextId = 0;
	private String name;
	public String getName() {return name; } 
	private  int id;
	public int getId() { return id;}
	int teamId;
	public int getTeamId() {return teamId;}
	LocalDate contractEndDate;
	public LocalDate getContractEndDate() { return contractEndDate; }
	
	public Player(String name, int teamId, LocalDate contractEndDate) {
		this.name = name;
		id = nextId++;
		this.teamId = teamId;
		this.contractEndDate = contractEndDate;
	}
	
	public Player(int id, String name) {
		super();
		this.name = name;
		this.id = id;
		nextId++;
	}

public String toString() {
	return name;
}
}
