package dataHandlingClasses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;


public class TurnamentManager implements Serializable  {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private String organisation;
	public String getOrganisation() {return organisation; };
	private ArrayList<Turnament> turnaments;
	private ArrayList<Player> playersMasterList;
	//public ArrayList<Player> getPlayers() { return playersMasterList; }
	private ArrayList<Team> teamsMasterList;
	//public ArrayList<Team> getTeams() { return teamsMasterList; }	
	private ArrayList<Contract> periodsMasterList;
	public ArrayList<Contract> getContractPeriods() {return periodsMasterList; } 
	public ArrayList<Turnament> getTurnaments() {return turnaments; }
	
	// Constructor
	public TurnamentManager(String organisation) throws IOException {
		turnaments = new ArrayList<Turnament>();
		this.organisation = organisation;
		teamsMasterList = new ArrayList<Team>();
		playersMasterList = new ArrayList<Player>();
		periodsMasterList = new ArrayList<Contract>();
		loadPlayers(Constants.stdDatafileFolder + "players.txt");
		loadTeams(Constants.stdDatafileFolder + "teams.txt");
		getHighestNumberOfPlayersInOneTeam();
		//populatePlayersMasterList();
	}
	
//	private void populatePlayersMasterList() {
//		for (Contract contract : periodsMasterList) {
//			playersMasterList.add(new)
//			contract.getPlayerId()
//		}
//		
//	}

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
	
	public boolean loadPlayers(String filename) throws IOException {

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
				periodsMasterList.add(contract );
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
		
//		// Inspired from https://www.techiedelight.com/count-frequency-elements-list-java/
//		List<Integer> list=new ArrayList<Integer>();
//        
//        // Extract teamIds, and put them into <Integer> list
//		for (ContractPeriod contractPeriod : contractPeriods) {
//			list.add(contractPeriod.getTeamId());
//		}
//        
//        // Do a map (consists of a key + value) the key is the teamID 
//		Map<Integer, Integer> frequencyMap = new HashMap<>();
//        for (Integer teamId: list) {
//            Integer count = frequencyMap.get(teamId);
//            if (count == null) count = 0;
// 
//            frequencyMap.put(teamId, count + 1);
//        }
// 
//        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
		System.out.println(highestNumber);
		return highestNumber;
	}
	
	public boolean loadTeams(String filename) throws IOException {

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
		for (Contract contract : periodsMasterList) {
			// correct level ?
			if (contract.getTeamId() == teamId) {
				// add contractperiod for this team(id)
				returnContracts.add(contract);
			}
		}

		return returnContracts;		
	}
	
}
