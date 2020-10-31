package graphicalClasses;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import dataHandlingClasses.*;

import java.awt.event.*;
import java.time.format.DateTimeFormatter;

public class MainPanel extends JInternalFrame{
	private static final long serialVersionUID = 1;

	JLabel teamTableLabel = new JLabel();
	JLabel matchTableLabel = new JLabel();
	JLabel goalTableLabel = new JLabel();
	JLabel playerTabelLabel = new JLabel();	

	JButton closeButton = new JButton();

	JTable teamTable;
	JTable matchTable;
	JTable goalTable;
	JTable playerTable;
	
	DefaultTableModel teamTableModel;	
	
	JTableColumnMetaData teamTableMetaData;
	JTableColumnMetaData matchTableMetaData;
	JTableColumnMetaData goalTableMetaData;
	JTableColumnMetaData playerTableMetaData;	
	
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
	Integer[] matchTableColumnWidths = { slimColumnWidth, slimColumnWidth, mediumWidth, largeWidth, mediumWidth, slimColumnWidth, slimColumnWidth};	

	final String tidGoalColumn = "Tid";	
	final String goalScorerColumn = "Målscorer";	
	String[] goalTableColumnNames =  { "Nr.", "Stilling", tidGoalColumn, goalScorerColumn};
	Integer[] goalTableColumnWidths = { slimColumnWidth, mediumWidth, slimColumnWidth, slimColumnWidth};
	
	String[] playerTableColumnNames =  { "Nr.", "Navn", "Kontraktudløb"};
	Integer[] playerTableColumnWidths = { slimColumnWidth, largeWidth, mediumWidth};	
	
	private int currentTeamId;

	public MainPanel(Turnament turnament) {
		super(turnament.getName(), true, true, true, true);
		try {
			this.turnament = turnament;
			this.title = turnament.getName();
//			this.setMaximizable(true); // maximize
//			this.setIconifiable(true); // set minimize
//			this.setClosable(true); // set closed
//			this.setResizable(true); // set resizable
			
			initGraphics();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g)
	{
		if (iconable==false) {
			super.paintComponent(g); // call super to ensure basic drawing			
		}

	}

	private void initGraphics() throws Exception {


		//setLayout(new BorderLayout()); 
		teamTableLabel.setText("Stillingen:");
		matchTableLabel.setText("Kampe:");
		goalTableLabel.setText("Mål:");
		playerTabelLabel.setText("Kontraktspillere:");
		
		closeButton.setText("Close");
		closeButton.setBounds(10*modus,  modus, 6*modus, 2*modus);

		int numberOfMatchesPrTeam = (turnament.getNumberOfTeams() - 1) * 2;
		int tableWidth = 38 * modus;

		
		teamTableModel = new DefaultTableModel(turnament.getNumberOfTeams(), 0);
		DefaultTableModel matchTableModel = new DefaultTableModel(numberOfMatchesPrTeam, 0);
		DefaultTableModel goalTableModel = new DefaultTableModel(Constants.maxGoalsOnePrMatch, 0);
		DefaultTableModel playerTableModel = new DefaultTableModel(turnament.getHighestNumberOfPlayersInOneTeam(), 0);

		teamTableMetaData = new JTableColumnMetaData(teamTable, teamTableModel, teamTableColumnNames, teamTableColumnWidths, new Rectangle(modus, 4*modus, tableWidth, 210), teamTableLabel);
		matchTableMetaData = new JTableColumnMetaData(matchTable, matchTableModel, matchTableColumnNames, matchTableColumnWidths, new Rectangle(modus, 20*modus, 600, 370), matchTableLabel);
		goalTableMetaData = new JTableColumnMetaData(goalTable, goalTableModel, goalTableColumnNames, goalTableColumnWidths, new Rectangle(modus, 46*modus, tableWidth, 180), goalTableLabel);
		playerTableMetaData = new JTableColumnMetaData(playerTable, playerTableModel, playerTableColumnNames, playerTableColumnWidths, new Rectangle(tableWidth + 6* modus, 4*modus, tableWidth/2, 660), playerTabelLabel);	
		
		// Set column Names and column widths
		teamTable = createJtables(teamTableMetaData);
		matchTable = createJtables(matchTableMetaData);
		goalTable = createJtables(goalTableMetaData);
		playerTable = createJtables(playerTableMetaData);
		
		matchTable.getColumnModel().getColumn(0).setHeaderValue("newHeader");
		matchTable.getTableHeader().repaint();
		
		JTableHeader th = matchTable.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(1);
		tc.setHeaderValue( "???" );
		th.repaint();
		matchTable.repaint();
		
		
		loadTeamsIntoTable();
		
		this.getContentPane().setLayout(null);
		
		//setOpaque(false);
		
		// Labels
		this.getContentPane().add(teamTableLabel);
		this.getContentPane().add(matchTableLabel);
		this.getContentPane().add(goalTableLabel);		
		this.getContentPane().add(playerTabelLabel);		

		this.getContentPane().add(closeButton);
		
		
//		JScrollPane pane = new JScrollPane(teamTable);
//		this.getContentPane().add(pane);
//		JScrollPane pane = new JScrollPane(teamTable);
//		this.getContentPane().add(pane);
//		
//		Object rowData[][] = { { "Row1-Column1", "Row1-Column2", "Row1-Column3"},
//                { "Row2-Column1", "Row2-Column2", "Row2-Column3"} };
//            Object columnNames[] = { "Column One", "Column Two", "Column Three"};
//            JTable jTable = new JTable(rowData, columnNames);
//            jTable.setBounds(new Rectangle(1, 1, 600, 370));
//            jTable.getColumnModel().getColumn(0).setHeaderValue("newHeader");
//            this.getContentPane().add(jTable);
            
            
		// Tables
		this.getContentPane().add(teamTable);
		this.getContentPane().add(matchTable);
		this.getContentPane().add(goalTable);
		this.getContentPane().add(playerTable);
		System.out.println("Fully loaded");

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeButton_actionPerformed(e);
			}

			private void closeButton_actionPerformed(ActionEvent e) {
				try {
					// Close the panel (MDI child)
					setClosed(true);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.err.println("Closing Exception");
				}
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
		int j = 0;
		for (Team t : turnament.getTeams()) {
			int colNum = 0;
			teamTableModel.setValueAt(j+1, j, colNum++);    	
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


	
	public JTable createJtables(JTableColumnMetaData jTableColumnMetaData)
	{
		try {
			jTableColumnMetaData.jTable = new JTable(jTableColumnMetaData.modelTable);
			jTableColumnMetaData.jTable.setBounds(jTableColumnMetaData.rectangle);

			// Place label 2 * modus above JTable
			jTableColumnMetaData.rectangle.y = jTableColumnMetaData.rectangle.y - jTableColumnMetaData.jTable.getHeight() / 2 - modus; 		
			jTableColumnMetaData.tableLabel.setBounds(jTableColumnMetaData.rectangle);		
			
			
			//jTableColumnMetaData.jTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

			//columnHeaderTitles
			for (int i = 0; i < jTableColumnMetaData.getColumnHeaderTitles().size(); i++) {
				
				String test = jTableColumnMetaData.getColumnHeader(i);
				System.out.println(test);
				jTableColumnMetaData.modelTable.addColumn(test);
				
				//String test2 =  jTableColumnMetaData.modelTable.setValueAt(jTableColumnMetaData.getColumnHeader(i), 0, i);
				
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
		return jTableColumnMetaData.jTable;
	}
	
	
	
	
//	public void addSchrollbar (DefaultTableModel table) {
//		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
//	}
	
	private void listMatches(int teamID) {
		clearTable(goalTable);
		
		int rowNumber = 0;
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
		
		int rowNumber = 0;
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
					goalTable.setValueAt(goal.getGoalScorer().getName(), rowNumber, Arrays.asList(goalTableColumnNames).indexOf(goalScorerColumn));
					rowNumber++;
				}
				break;
			}

		}
	}
	
	private void listPlayers(int teamId)
	{
		clearTable(playerTable);
		
		int rowNumber = 0;
		Team team = turnament.GetTeam(teamId);
		for (Contract contract : team.getTeamContracts()) {

				int colNum = 0;
				playerTable.setValueAt(rowNumber, rowNumber, colNum++);
				playerTable.setValueAt(contract.getPlayerName(), rowNumber, colNum++);
				playerTable.setValueAt(contract.getEndDatee().format(DateTimeFormatter.ofPattern(Constants.dkDateFormat)).toString(), rowNumber, colNum++);				
				rowNumber++;

		}
	}

	public void clearTables() {
		clearTable(teamTable);
		clearTable(matchTable);
		clearTable(goalTable);
		clearTable(playerTable);					
	}
	
	public void regerateTurnament() {
		//TODO Call turnament regenerate
	}

}

class JTableColumnMetaData {
	// Purpose: To organize data relating to the same JTable
	private ArrayList<String> columnHeaderTitles;
	private ArrayList<Integer> columnHeaderWidths;
	public ArrayList<String> getColumnHeaderTitles() { return columnHeaderTitles; }
	JTable jTable;
	JLabel tableLabel;
	DefaultTableModel modelTable;
	Rectangle rectangle;

	JTableColumnMetaData(JTable jTable, DefaultTableModel modelTable, String[] headerTitlesArray, Integer[] HeaderWidthsArray, Rectangle rectangle, JLabel tableLabel) {
		columnHeaderTitles = new ArrayList<String>(Arrays.asList(headerTitlesArray));
		columnHeaderWidths = new ArrayList<Integer>(Arrays.asList(HeaderWidthsArray));
		this.jTable = jTable;
		this.modelTable = modelTable;
		this.rectangle = rectangle;
		this.tableLabel = tableLabel; 
	}
	
	public String getColumnHeader(int index) {
		return columnHeaderTitles.get(index).toString();
	}

	public int getColumnWidth(int index) {
		return columnHeaderWidths.get(index).intValue();
	}
	
}
