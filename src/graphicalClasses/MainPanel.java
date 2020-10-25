package graphicalClasses;



import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import dataHandlingClasses.Constants;
import dataHandlingClasses.ContractPeriod;
import dataHandlingClasses.Goal;
import dataHandlingClasses.GoalResult;
import dataHandlingClasses.Match;
import dataHandlingClasses.Team;
import dataHandlingClasses.Turnament;

import java.awt.event.*;
import java.time.format.DateTimeFormatter;

public class MainPanel extends JPanel{

	MainWindow mainWindow;
	JLabel scoreBoardLabel = new JLabel();
	JTextField player1nameTextField = new JTextField();
	JLabel matchesLabel = new JLabel();
	JTextField player2nameTextField = new JTextField();
	JButton closeButton = new JButton();
	JTable teamTable;
	DefaultTableModel teamTableModel;	
	JTable matchTable;
	JTable goalTable;
	JTable playerTable;
	
	JTableColumnMetaData teamTableMetaData;
	JTableColumnMetaData matchTableMetaData;
	JTableColumnMetaData goalTableMetaData;
	JTableColumnMetaData playerTableMetaData;	
	

	Random rand;
	Turnament turnament;
	int modus = 16; // Predefined modus for margin space etc.
	int slimColumnWidth = modus;
	int mediumWidth = 60;
	int largeWidth = 80;	


	// Column Names
	final String teamIdColumn = "Hold Id";
	String[] teamTableColumnNames =  { "Nr.", teamIdColumn, "Holdnavn", "Kampe", "Målscore", "Point"};
	// Column widths
	Integer[] teamTableColumnWidths = { slimColumnWidth, slimColumnWidth, largeWidth, slimColumnWidth, mediumWidth, slimColumnWidth};

	final String matchIdColumn = "Kamp Id";
	String[] matchTableColumnNames = { "Nr.", matchIdColumn, "Dato", "Hjemmehold", "Udehold", "Score", "Runde"};
	Integer[] matchTableColumnWidths = { slimColumnWidth, slimColumnWidth, slimColumnWidth, largeWidth, mediumWidth, slimColumnWidth, slimColumnWidth};	

	final String tidGoalColumn = "Tid";	
	String[] goalTableColumnNames =  { "Nr.", "Stilling", tidGoalColumn, "Målscorer"};
	Integer[] goalTableColumnWidths = { slimColumnWidth, mediumWidth, slimColumnWidth, slimColumnWidth};
	
	String[] playerTableColumnNames =  { "Nr.", "Navn", "Kontraktudløb"};
	Integer[] playerTableColumnWidths = { slimColumnWidth, largeWidth, mediumWidth};	

	
	private int currentTeamId;

	public MainPanel(MainWindow mainWindow, Turnament turnament) {
		super();
		try {
			this.turnament = turnament;
			this.mainWindow = mainWindow;
			//this.setVisible(true);
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g); // call super to ensure draw basic drawing
	}

	private void jbInit() throws Exception {

		Rectangle rect = new Rectangle(modus, modus, 8*modus, modus);
		scoreBoardLabel.setText("Stillingen");
		scoreBoardLabel.setBounds(rect);
		matchesLabel.setText("Kampe");
		rect.y = 300;
		matchesLabel.setBounds(rect);

		player1nameTextField.setText("");
		player1nameTextField.setBounds(new Rectangle(110, 12, 112, 29));
		player2nameTextField.setText("");
		player2nameTextField.setBounds(new Rectangle(350, 12, 112, 29));

		closeButton.setText("Close");
		closeButton.setBounds(10*modus,  modus, 8*modus, 2*modus);

		int numberOfMatchesPrTeam = (turnament.getNumberOfTeams() - 1) * 2;
		teamTableModel = new DefaultTableModel(turnament.getNumberOfTeams() + 1, teamTableColumnNames.length); //+ 1 = line for column names
		DefaultTableModel matchTableModel = new DefaultTableModel(numberOfMatchesPrTeam + 1, matchTableColumnNames.length);
		DefaultTableModel goalTableModel = new DefaultTableModel(Constants.maxGoalsOnePrMatch + 1, goalTableColumnNames.length);
		DefaultTableModel playerTableModel = new DefaultTableModel(turnament.getHighestNumberOfPlayersInOneTeam() + 1, playerTableColumnNames.length);		

		teamTable = new JTable(teamTableModel);
		teamTable.setBounds(new Rectangle(modus, 4*modus, 600, 210));

		matchTable = new JTable(matchTableModel);
		matchTable.setBounds(new Rectangle(modus, 20*modus, 600, 370));

		goalTable = new JTable(goalTableModel);
		goalTable.setBounds(new Rectangle(modus, 740, 600, 180));
		
		playerTable = new JTable(playerTableModel);
		playerTable.setBounds(new Rectangle(700, 4*modus, 300, 660));

		teamTableMetaData = new JTableColumnMetaData(teamTable, teamTableModel, teamTableColumnNames, teamTableColumnWidths);
		matchTableMetaData = new JTableColumnMetaData(matchTable, matchTableModel, matchTableColumnNames, matchTableColumnWidths);
		goalTableMetaData = new JTableColumnMetaData(goalTable, goalTableModel, goalTableColumnNames, goalTableColumnWidths);
		playerTableMetaData = new JTableColumnMetaData(playerTable, playerTableModel, playerTableColumnNames, playerTableColumnWidths);	
		
		// Set column Names and column widths
		setColumnNamesAndWidthOnTable(teamTableMetaData);
		setColumnNamesAndWidthOnTable(matchTableMetaData);
		setColumnNamesAndWidthOnTable(goalTableMetaData);
		setColumnNamesAndWidthOnTable(playerTableMetaData);
		
		loadTeamsIntoTable();
		
		this.setLayout(null);
		this.add(scoreBoardLabel);
		this.add(matchesLabel);
		this.add(closeButton);
		this.add(teamTable);
		this.add(matchTable);
		this.add(goalTable);
		this.add(playerTable);		

		rand = new Random();

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeButton_actionPerformed(e);
			}

			private void closeButton_actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});

		teamTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = teamTable.rowAtPoint(evt.getPoint());
				int col = teamTable.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					currentTeamId = Integer.parseInt(teamTableModel.getValueAt(row, Arrays.asList(teamTableColumnNames).indexOf(teamIdColumn)).toString());
					teamTableSelectionChanged(currentTeamId);
				}
			}
		});
		
		teamTable.getSelectionModel().addListSelectionListener((ListSelectionListener) new ListSelectionListener(){ //https://stackoverflow.com/questions/10128064/jtable-selected-row-click-event
			public void valueChanged(ListSelectionEvent event) {
				// ignore row 1 (column headers)
				if (teamTable.getSelectedRow() > 0) {
					currentTeamId = Integer.parseInt(teamTableModel.getValueAt(teamTable.getSelectedRow(), Arrays.asList(teamTableColumnNames).indexOf(teamIdColumn)).toString());	        	
					teamTableSelectionChanged(currentTeamId);
				}
				else {
					// It is the column header row which i selected
					clearTable(matchTable);
					clearTable(goalTable);
					clearTable(playerTable);					
				}
				
			}
		});

		matchTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = matchTable.rowAtPoint(evt.getPoint());
				int col = matchTable.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					int currentMatchId = Integer.parseInt(matchTableModel.getValueAt(row, Arrays.asList(matchTableColumnNames).indexOf(matchIdColumn)).toString());
					matchTableSelectionChanged(currentMatchId);
				}
			}
		});

		// Respond to change in selected row (eg. use arraw up / down
		matchTable.getSelectionModel().addListSelectionListener((ListSelectionListener) new ListSelectionListener(){ //https://stackoverflow.com/questions/10128064/jtable-selected-row-click-event
			public void valueChanged(ListSelectionEvent event) {
				// ignore row 1 (column headers)
				if (matchTable.getSelectedRow() > 0) {
					int currentmatchId = Integer.parseInt(matchTableModel.getValueAt(matchTable.getSelectedRow(), Arrays.asList(matchTableColumnNames).indexOf(matchIdColumn)).toString());	        	
					matchTableSelectionChanged(currentmatchId);
				}
				else {
					// It is the column header row which i selected
					clearTable(goalTable);
				}
			}
		});
		

	}

	public void loadTeamsIntoTable() {
		
		//clearTable(teamTable);
		int j = 1;
		for (Team t : turnament.getTeams()) {
			int colNum = 0;
			teamTableModel.setValueAt(j, j, colNum++);    	
			teamTableModel.setValueAt(t.getId(), j, colNum++);
			teamTableModel.setValueAt(t.getName(), j, colNum++);
			teamTableModel.setValueAt(turnament.GetNumberOfMatchesForTeam(t), j, colNum++);
			GoalResult goalResult = turnament.goalsScoredAndTakenForTeam(t);
			teamTableModel.setValueAt(goalResult.scored + " - " + goalResult.taken, j, colNum++);			
			teamTableModel.setValueAt(t.getPoints(), j, colNum++);    	
			j++;
		}
	}
	
	public void teamTableSelectionChanged(int teamId)
	{
		listMatches(currentTeamId);
		listPlayers(currentTeamId);
	}

	public void matchTableSelectionChanged(int matchId)
	{
		listGoals(matchId);
	}


	
	public void setColumnNamesAndWidthOnTable(JTableColumnMetaData jTableColumnMetaData)
	{
		try {

			//jTableColumnMetaData.jTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
			for (int i = 0; i < jTableColumnMetaData.jTable.getColumnCount(); i++) {
				
				jTableColumnMetaData.modelTable.setValueAt(jTableColumnMetaData.getColumnHeader(i), 0, i);
				int width = jTableColumnMetaData.getColumnWidth(i);
				//jTableColumnMetaData.jTable.getColumnModel().getColumn(i).setFont(new Font("SansSerif", Font.BOLD, 12));
				jTableColumnMetaData.jTable.getColumnModel().getColumn(i).setPreferredWidth(width);
				
				//jTable.getColumnModel().getColumn(i).setPreferredWidth(width);
				//modelTable..getColumn(i).setMaxWidth(width);

				//			if (columnWidths != null) {
				//				TableColumn tc = jTable.getColumn(headerNames[i]);
				//				tc.setMaxWidth(width);				
				//			}
			}			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//.setRowColour(1, Color.YELLOW);
	}
	
	
	
	
//	public void addSchrollbar (DefaultTableModel table) {
//		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
//	}
	
	private void listMatches(int teamID) {
		clearTable(goalTable);
		
		int rowNumber = 1;
		for (Match m : turnament.getMatches()) {
			int colNum = 0;

			if ((m.getHomeTeam().getId() == teamID) || (m.getAwayTeam().getId() == teamID)) {
				matchTable.setValueAt(rowNumber, rowNumber, colNum++);
				matchTable.setValueAt(m.getMatchNo(), rowNumber, colNum++);
				matchTable.setValueAt(m.getDate().format(DateTimeFormatter.ofPattern(Constants.dkDateFormat)).toString(), rowNumber, colNum++); 			
				matchTable.setValueAt(m.getHomeTeam().getName(), rowNumber, colNum++);
				matchTable.setValueAt(m.getAwayTeam().getName(), rowNumber, colNum++);
				matchTable.setValueAt(m.getHomeGoals() + " - " + m.getAwayGoals(), rowNumber, colNum++);
				matchTable.setValueAt(m.getRoundNo(), rowNumber, colNum++);				
				rowNumber++;
			}
		}
	}

	private void clearTable(JTable table) {
		// clearJTable
		for (int i = 1; i < table.getRowCount(); i++) {
			for (int j = 0; j < table.getColumnCount(); j++) {
				table.setValueAt("", i, j);
			}
		}
		
	}
	
	private void listGoals(int matchId) {

		clearTable(goalTable);
		
		turnament.sortGoalsByTime();
		
		int rowNumber = 1;
		// ToDo Kig på findMatch via ID i stedet for at rulle
		for (Match m : turnament.getMatches()) {
			if (m.getMatchNo() == matchId) {
				int homeGoals = 0;
				int awayGoals = 0;
				
				for (Goal goal : m.getGoals()) {
					int colNum = 0;					
					goalTable.setValueAt(rowNumber, rowNumber, colNum++);
					goalTable.setValueAt(goal.getMinute() + ":" + goal.getSecond(), rowNumber, Arrays.asList(goalTableColumnNames).indexOf(tidGoalColumn));
					if (goal.getGoalType()== Goal.GoalType.Home) {
						homeGoals++;
					}
					else {
						awayGoals++;
					}
					goalTable.setValueAt(homeGoals + "-" + awayGoals, rowNumber, 1);
					rowNumber++;
				}
				break;
			}

		}
	}
	
	private void listPlayers(int teamId)
	{
		clearTable(playerTable);
		
		int rowNumber = 1;		
		for (ContractPeriod period : turnament.getTurnamentManager().getContractPeriods()) {
			if (period.getTeamId() == teamId) {
				int colNum = 0;
				playerTable.setValueAt(rowNumber, rowNumber, colNum++);
				playerTable.setValueAt(turnament.getPlayers().get(period.getPlayerId()).getName(), rowNumber, colNum++);
				playerTable.setValueAt(period.getEndDatee().format(DateTimeFormatter.ofPattern(Constants.dkDateFormat)).toString(), rowNumber, colNum++);				
				rowNumber++;
			}
		}
	}

	public void clearTables() {
		clearTable(teamTable);
		clearTable(matchTable);
		clearTable(goalTable);
		clearTable(playerTable);					
	}

}

class JTableColumnMetaData {
	// Purpose: To organize data relating to the same JTable
	private ArrayList<String> columnHeaderTitles;
	private ArrayList<Integer> columnHeaderWidths;
	JTable jTable;
	DefaultTableModel modelTable; 

	JTableColumnMetaData(JTable jTable, DefaultTableModel modelTable, String[] headerTitlesArray, Integer[] HeaderWidthsArray) {
		columnHeaderTitles = new ArrayList<String>(Arrays.asList(headerTitlesArray));
		columnHeaderWidths = new ArrayList<Integer>(Arrays.asList(HeaderWidthsArray));
		this.jTable = jTable;
		this.modelTable = modelTable;
	}
	
	public String getColumnHeader(int index) {
		return columnHeaderTitles.get(index).toString();
	}

	public int getColumnWidth(int index) {
		return columnHeaderWidths.get(index).intValue();
	}
	
}
