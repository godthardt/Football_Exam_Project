

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class TurnamentManager implements Serializable  {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private String organisation;
	public String getOrganisation() {return organisation; };
	private ArrayList<Turnament> turnaments;
	private ArrayList<Player> playersMasterList;
	private ArrayList<Team> teamsMasterList;
	private ArrayList<Contract> contractsMasterList;
	public ArrayList<Contract> getContractPeriods() {return contractsMasterList; } 
	public ArrayList<Turnament> getTurnaments() {return turnaments; }

	// Constructor
	public TurnamentManager(String organisation) throws IOException {
		turnaments = new ArrayList<Turnament>();
		this.organisation = organisation;
		teamsMasterList = new ArrayList<Team>();
		playersMasterList = new ArrayList<Player>();
		contractsMasterList = new ArrayList<Contract>();
		loadPlayersFromTxtFile("players.txt");
		loadTeamsFromTxtFile("teams.txt");
		getHighestNumberOfPlayersInOneTeam();
	}

	public void addTurnament(Turnament turnament) {
		turnaments.add(turnament);
	}

	public Player getPlayer(int playerID) {
		Player returnPlayer = null;
		for (Player player : playersMasterList) {
			if (player.getId() == playerID)
				returnPlayer = player;
		}
		if (returnPlayer == null) {
			throw new NullPointerException("PlayerId" + playerID + " not found in method getPlayer");  
		}

		return returnPlayer;
	}

	private boolean loadPlayersFromTxtFile(String filename) throws IOException {

		FileReader fil = new FileReader(filename);
		BufferedReader ind = new BufferedReader(fil);
		int lineNo = 0;
		try {

			String linje = ind.readLine();
			while (linje != null)
			{
				String[] arrOfStr = linje.split(",");
				// Input file line has format "Nicolaj Thomsen,52769,2021-06-30" the part after the last comma is the end date for the contract)
				LocalDate contractExpire = LocalDate.parse(arrOfStr[2]);
				Player player = new Player(arrOfStr[0]);
				playersMasterList.add(player);
				int teamId = Integer.parseInt(arrOfStr[1]);
				Contract contract = new Contract(player.getId(), player.getName(), teamId, contractExpire);
				contractsMasterList.add(contract );
				// Add the player to the relevant team
				//findTeamFromTeamId(teamId).addPlayer(player);				
				linje = ind.readLine();
				lineNo++;			
			}
			ind.close();
			//contractPeriods.sort(null);			
			return true;
		} 
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while reading line number " + lineNo);
			e.printStackTrace();
			return false;			
		}

	}

	public int getHighestNumberOfPlayersInOneTeam() {
		int highestNumber = 0;

		for (Team team : teamsMasterList) {
			if (team.getNumberOfPlayersInTeam() > highestNumber) {
				highestNumber = team.getNumberOfPlayersInTeam();
			}
		}

		return highestNumber;
	}

	private boolean loadTeamsFromTxtFile(String filename) throws IOException {

		FileReader fil = new FileReader(filename);
		BufferedReader ind = new BufferedReader(fil);
		int lineNo = 0;
		try {

			String linje = ind.readLine();
			while (linje != null)
			{
				try {
					String[] arrOfStr = linje.split(",");
					int teamID = Integer.parseInt(arrOfStr[0]);
					teamsMasterList.add(new Team(teamID, arrOfStr[1], Integer.parseInt(arrOfStr[2]), getContractPeriodsOfTeam(teamID)));

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Exception " + e.toString() + " at line " + lineNo + " in file: " + filename);
				}
				linje = ind.readLine();
				lineNo++;
			}
			ind.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while reading line number " + lineNo);
			e.printStackTrace();
			return false;			
		}
	}

	public Team findTeamFromTeamId(int teamId) {
		for (Team team : teamsMasterList) {
			if (team.getId() == teamId) {
				return team; 
			}
		}
		throw new NullPointerException("Team not Found");
	}

	public ArrayList<Team> getTeamsOfLevel(int level) {
		// construct an object to return to caller
		ArrayList<Team> returnTeams = new ArrayList<Team>(); 
		for (Team team : teamsMasterList) {
			// correct level ?
			if (team.getLevel() == level) {
				// add team
				returnTeams.add(team);
			}
		}

		return returnTeams;		
	}

	private ArrayList<Contract> getContractPeriodsOfTeam(int teamId) {
		// construct an object to return to caller
		ArrayList<Contract> returnContracts = new ArrayList<Contract>(); 
		for (Contract contract : contractsMasterList) {
			// correct level ?
			if (contract.getTeamId() == teamId) {
				// add contractperiod for this team(id)
				returnContracts.add(contract);
			}
		}

		return returnContracts;		
	}

}
