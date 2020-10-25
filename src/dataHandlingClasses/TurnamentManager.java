package dataHandlingClasses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TurnamentManager implements Serializable  {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private String organisation;
	public String getOrganisation() {return organisation; };
	private ArrayList<Turnament> turnaments;
	private ArrayList<Player> players;
	public ArrayList<Player> getPlayers() { return players; }
	private ArrayList<Team> teams;
	public ArrayList<Team> getTeams() { return teams; }	
	private ArrayList<ContractPeriod> contractPeriods;
	public ArrayList<ContractPeriod> getContractPeriods() {return contractPeriods; } 
	public ArrayList<Turnament> getTurnaments() {return turnaments; }
	
	// Constructor
	public TurnamentManager(String organisation) throws IOException {
		turnaments = new ArrayList<Turnament>();
		this.organisation = organisation;
		teams = new ArrayList<Team>();
		players = new ArrayList<Player>();
		contractPeriods = new ArrayList<ContractPeriod>();
		loadTeams(Constants.stdDatafileFolder + "teams.txt");
		loadPlayers(Constants.stdDatafileFolder + "players.txt");
		
	}
	
	public void addTurnament(Turnament turnament) {
		turnaments.add(turnament);
	}
	
	public boolean loadPlayers(String filename) throws IOException {

		FileReader fil = new FileReader(filename);
		BufferedReader ind = new BufferedReader(fil);
		int lineNo = 0;
		try {

			String linje = ind.readLine();
			while (linje != null)
			{
				String[] firstArrOfStr = linje.split(",");
				// Input file line has format "Nicolaj Thomsen,52769,2021-06-30" the part after the last comma is the end date for the contract)
				LocalDate contractExpire = LocalDate.parse(firstArrOfStr[2]);
				Player player = new Player(firstArrOfStr[0]);
				players.add(player);
				int teamId = Integer.parseInt(firstArrOfStr[1]);
				ContractPeriod contractPeriod = new ContractPeriod(player.getId(), teamId, contractExpire);
				contractPeriods.add(contractPeriod );
				linje = ind.readLine();
				lineNo++;			
			}
			ind.close();
			getHighestNumberOfPlayersInOneTeam();
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
		//sort contracts by teamId 
		contractPeriods.sort(null);
		
		Instant startTime;
		Instant endTime;
		startTime = Instant.now();


		int currentTeamId;
		int prevTeamId = -1;
		int counter = 0;
		int number = 0;
		// loop over contractPeriods, to find the  number of players in one team
		for (ContractPeriod contractPeriod : contractPeriods) {
			counter++;
			currentTeamId = contractPeriod.getTeamId();

			// is it a new team ?
			if (currentTeamId != prevTeamId) {
				// new  ?
				if (counter > number) {
					number = counter;
					System.out.println("New  team: " + contractPeriod.getTeamId() + " - Number " + number); 
				}
				// continue over a new teamId
				counter = 0;
			}
			// record the current team, in order to discover if the team changes
			prevTeamId = currentTeamId;
		}
		
		endTime = Instant.now();
		//Duration d = Duration.between(startTime, endTime);
		//long milisecBeteen = d.toSecondsPart(); 
		long timeElapsed = Duration.between(startTime, endTime).toNanos();
		System.out.println("Simple method seconds: " + timeElapsed);		

		
		
		// Inspired from https://www.techiedelight.com/count-frequency-elements-list-java/
		List<Integer> list=new ArrayList<Integer>();
        
        // Extract teamIds, and put them into <Integer> list
		for (ContractPeriod contractPeriod : contractPeriods) {
			list.add(contractPeriod.getTeamId());
		}
        
        // Do a map (consists of a key + value) the key is the teamID 
		Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (Integer teamId: list) {
            Integer count = frequencyMap.get(teamId);
            if (count == null) count = 0;
 
            frequencyMap.put(teamId, count + 1);
        }
 
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }		
		System.out.println("Largest number = " +number );
		return number;
		
	}
	
	public boolean loadTeams(String filename) throws IOException {

		FileReader fil = new FileReader(filename);
		BufferedReader ind = new BufferedReader(fil);
		int lineNo = 0;
		try {
			
		String linje = ind.readLine();
		while (linje != null)
		{
			String[] arrOfStr = linje.split(",");
			teams.add(new Team(Integer.parseInt(arrOfStr[0]), arrOfStr[1]));
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
	

	
	
	
}
