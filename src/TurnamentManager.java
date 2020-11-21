

import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class TurnamentManager implements Serializable  {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private String organisation;
	public String getOrganisation() {return organisation; };
	private ArrayList<Turnament> turnaments;
	private ArrayList<Player> playersMasterList;
	private ArrayList<Team> teamsMasterList;
	public ArrayList<Turnament> getTurnaments() {return turnaments; }

	// Constructor
	public TurnamentManager(String organisation) throws IOException {
		turnaments = new ArrayList<Turnament>();
		this.organisation = organisation;
		teamsMasterList = new ArrayList<Team>();
		playersMasterList = new ArrayList<Player>();
		loadPlayersFromTxtFile("players.txt");
		//System.out.println("playersMasterList.size efter loadPlayersFromTxtFile " + playersMasterList.size());
		
		loadTeamsFromTxtFile("teams.txt");
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
				String playerName = arrOfStr[0];
				int teamId = Integer.parseInt(arrOfStr[1]);				
				LocalDate contractExpire = LocalDate.parse(arrOfStr[2]);
				Player player = new Player(playerName, teamId, contractExpire);
				playersMasterList.add(player);

				// Add the player to the relevant team
				//findTeam(teamId).addPlayer(player);				
				linje = ind.readLine();
				lineNo++;			
			}
			ind.close();
			
			//contractPeriods.sort(null);			
			return true;
		} 
		catch (Exception e) {
			System.out.println("Error while reading line number " + lineNo);
			e.printStackTrace();
			return false;			
		}

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
					// Inputfile has format: Id, name, level eg. 11906,Randers FC,0
					int teamID = Integer.parseInt(arrOfStr[0]);
					String teamName = arrOfStr[1];
					int level = Integer.parseInt(arrOfStr[2]);
					ArrayList<Player> playersInTeam = getPlayers(teamID);
					teamsMasterList.add(new Team(teamID, teamName, level, playersInTeam, Match.MustLoooeType.RandomLoose));

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

	private ArrayList<Player> getPlayers(int teamId) {
		ArrayList<Player> resultPlayers = new ArrayList<Player>();
		for (Player player : playersMasterList) {
			if (player.getTeamId() == teamId) {
				resultPlayers.add(player);
			}
		}
		return resultPlayers;
	}

	public Team findTeam(int teamId) {
		System.out.println("findTeam with teamId " + teamId + " teamsMasterList.size() " + teamsMasterList.size());
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
				// add team as deep copy in order not to have side effect
				returnTeams.add(new Team(team));
			}
		}
		// Using "copy" constructor inspired from 
		// https://www.generacodice.com/en/articolo/201023/How-to-clone-ArrayList-and-also-clone-its-contents
		
		return returnTeams;		
	}

	public void iterateTurnaments(Component parent) {
		String info = "";
		for (Turnament turnament : turnaments) {
			if (turnament.getInActive() == true) {
				turnaments.remove(turnament);
			} else {
				info = info + "Navn: " + turnament.getName() + " Antal hold: " + turnament.getNumberOfTeams() + " Antal kampe: " + turnament.getMatches().size() + "\n";				
			}

		}
		JOptionPane.showMessageDialog(parent, "Info om åbnede turneringer\n" + info, "Turneringer", JOptionPane.INFORMATION_MESSAGE);

	}

	
}
