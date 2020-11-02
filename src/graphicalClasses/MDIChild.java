package graphicalClasses;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.time.format.DateTimeFormatter;

// Own classes
import dataHandlingClasses.*;

public class MDIChild extends JInternalFrame{
	private static final long serialVersionUID = 1;

	private JPanel panel = new JPanel();
	JDesktopPane desktopPane = new JDesktopPane();
	
	private JLabel teamTableLabel = new JLabel();
	private JLabel matchTableLabel = new JLabel();
	private JLabel goalTableLabel = new JLabel();
	private JLabel playerTabelLabel = new JLabel();	

	private JButton closeButton = new JButton();

	private JTable teamTable;
	private JTable matchTable;
	private JTable goalTable;
	private JTable playerTable;
	
	//DefaultTableModel teamTableModel;	
	
	private JTableColumnMetaData teamTableMetaData;
	private JTableColumnMetaData matchTableMetaData;
	private JTableColumnMetaData goalTableMetaData;
	private JTableColumnMetaData playerTableMetaData;	
	
	Turnament turnament;
	private int modus = 16; // Predefined modus for margin space etc.
	private int slimColumnWidth = modus;
	private int mediumColumnWidth = 60;
	private int largeColimnWidth = 80;	

	// Column Names
	private final String teamIdColumn = "Hold Id";
	private String[] teamTableColumnNames =  { "Nr.", teamIdColumn, "Holdnavn", "Kampe", "Målscore", "Point"};
	// Column widths
	private Integer[] teamTableColumnWidths = { slimColumnWidth, slimColumnWidth, largeColimnWidth, slimColumnWidth, mediumColumnWidth, slimColumnWidth};

	private final String matchIdColumn = "Kamp Id";
	private String[] matchTableColumnNames = { "Nr.", matchIdColumn, "Dato", "Hjemmehold", "Udehold", "Score", "Runde"};
	private Integer[] matchTableColumnWidths = { slimColumnWidth, slimColumnWidth, mediumColumnWidth, largeColimnWidth, mediumColumnWidth, slimColumnWidth, slimColumnWidth};	

	private final String tidGoalColumn = "Tid";	
	private final String goalScorerColumn = "Målscorer";	
	private String[] goalTableColumnNames =  { "Nr.", "Stilling", tidGoalColumn, goalScorerColumn};
	private Integer[] goalTableColumnWidths = { slimColumnWidth, mediumColumnWidth, slimColumnWidth, slimColumnWidth};
	
	private String[] playerTableColumnNames =  { "Nr.", "Navn", "Kontraktudløb"};
	private Integer[] playerTableColumnWidths = { slimColumnWidth, largeColimnWidth, mediumColumnWidth};
		
	private int currentTeamId;

	public MDIChild(Turnament turnament) {
		super(turnament.getName(), true, true, true, true);
		try {
			this.turnament = turnament;
			this.title = turnament.getName();
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


		teamTableLabel.setText("Stillingen:");
		matchTableLabel.setText("Kampe:");
		goalTableLabel.setText("Mål:");
		playerTabelLabel.setText("Kontraktspillere:");
		
		int numberOfMatchesPrTeam = (turnament.getNumberOfTeams() - 1) * 2;
		int stdTableWidth = 34 * modus;

		closeButton.setText("Luk");
		closeButton.setBounds(stdTableWidth + 6* modus,  26*modus, 4*modus, 2*modus);
		
		DefaultTableModel teamTableModel = new DefaultTableModel(turnament.getNumberOfTeams() + 1, teamTableColumnNames.length);
		DefaultTableModel matchTableModel = new DefaultTableModel(numberOfMatchesPrTeam + 1, matchTableColumnNames.length);
		DefaultTableModel goalTableModel = new DefaultTableModel(Constants.maxGoalsOnePrMatch + 1, goalTableColumnNames.length);
		DefaultTableModel playerTableModel = new DefaultTableModel(turnament.getHighestNumberOfPlayersInOneTeam() + 1, goalTableColumnNames.length);

		teamTableMetaData = new JTableColumnMetaData(teamTable, teamTableModel, teamTableColumnNames, teamTableColumnWidths, new Rectangle(modus, 2*modus, stdTableWidth, 13*modus), teamTableLabel);
		matchTableMetaData = new JTableColumnMetaData(matchTable, matchTableModel, matchTableColumnNames, matchTableColumnWidths, new Rectangle(modus, 17*modus, stdTableWidth, 18*modus), matchTableLabel);
		goalTableMetaData = new JTableColumnMetaData(goalTable, goalTableModel, goalTableColumnNames, goalTableColumnWidths, new Rectangle(modus, 37*modus, stdTableWidth, 11*modus), goalTableLabel);
		playerTableMetaData = new JTableColumnMetaData(playerTable, playerTableModel, playerTableColumnNames, playerTableColumnWidths, new Rectangle(stdTableWidth + 4* modus, 2*modus, stdTableWidth / 2, 41*modus), playerTabelLabel);	
		
		// Set column Names and column widths
		teamTable = createJtables(teamTableMetaData);
		matchTable = createJtables(matchTableMetaData);
		goalTable = createJtables(goalTableMetaData);
		playerTable = createJtables(playerTableMetaData);
		
//		matchTable.getColumnModel().getColumn(0).setHeaderValue("newHeader");
//		matchTable.getTableHeader().repaint();
		
//		JTableHeader th = matchTable.getTableHeader();
//		TableColumnModel tcm = th.getColumnModel();
//		TableColumn tc = tcm.getColumn(1);
//		tc.setHeaderValue( "???" );
//		th.repaint();
//		matchTable.repaint();
		
		
		loadTeamsIntoTable(teamTable);
		getContentPane().setLayout(null);
		
		// Labels
		panel.add(teamTableLabel);
		panel.add(matchTableLabel);
		panel.add(goalTableLabel);		
		panel.add(playerTabelLabel);		

		//panel.add(closeButton);
		
		// Tables
		
		
//		scrollpane.setViewportView(teamTable);
//		this.getContentPane().add(scrollpane, BorderLayout.CENTER);

		panel.add(teamTable);
		panel.add(matchTable);
		panel.add(goalTable);
		panel.add(playerTable);
		
		
		panel.setSize(Constants.mDIChildWidth, Constants.mDIChildHigth);
		panel.setLayout(null);
		panel.setVisible(true);
		
	    desktopPane.setPreferredSize(panel.getSize());
	    desktopPane.setVisible(true);
		desktopPane.add(panel);
		
		
//		JScrollPane scrollpane = new JScrollPane(panel);
//		scrollpane.setViewportView(panel);
//		scrollpane.setVisible(true);
		
		this.getContentPane().add(panel);
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
					// It is the column header row which was selected
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
					try {
						String cellValue = matchTableModel.getValueAt(row, Arrays.asList(matchTableColumnNames).indexOf(matchIdColumn)).toString();
						if (cellValue.isEmpty() == false) {
							int currentmatchId = Integer.parseInt(cellValue);	        	
							matchTableSelectionChanged(currentmatchId);
						}
					} catch (Exception e) {
						//e.printStackTrace();
					}


				}
			}
		});

		// Respond to change in selected row (eg. use arrow up / down
		matchTable.getSelectionModel().addListSelectionListener((ListSelectionListener) new ListSelectionListener(){ //https://stackoverflow.com/questions/10128064/jtable-selected-row-click-event
			public void valueChanged(ListSelectionEvent event) {
				// ignore row 1 (column headers)
				if (matchTable.getSelectedRow() > 0) {
					try {
						String cellValue = matchTableModel.getValueAt(matchTable.getSelectedRow(), Arrays.asList(matchTableColumnNames).indexOf(matchIdColumn)).toString();
						if (cellValue.isEmpty() == false) {
							int currentmatchId = Integer.parseInt(cellValue);	        	
							matchTableSelectionChanged(currentmatchId);
						}
						
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}
				else {
					// It is the column header row which i selected
					clearTable(goalTable);
				}
			}
		});

	}

	private void loadTeamsIntoTable(JTable jTable) {
		
		//clearTable(teamTable);
		int j = 1;
		for (Team t : turnament.getTeams()) {
			int colNum = 0;
			jTable.setValueAt(j, j, colNum++);    	
			jTable.setValueAt(t.getId(), j, colNum++);
			jTable.setValueAt(t.getName(), j, colNum++);
			jTable.setValueAt(turnament.GetNumberOfMatchesForTeam(t), j, colNum++);
			// Get goal score for team 
			GoalResult goalResult = turnament.goalsScoredAndTakenForTeam(t);
			jTable.setValueAt(goalResult.scored + " - " + goalResult.taken, j, colNum++);			
			jTable.setValueAt(t.getPoints(), j, colNum++);    	
			j++;
		}
	}
	
	private void teamTableSelectionChanged(int teamId)
	{
		
		listMatches(currentTeamId);
		listPlayers(currentTeamId);
	}

	private void matchTableSelectionChanged(int matchId)
	{
		listGoals(matchId);
	}
	
	private JTable createJtables(JTableColumnMetaData jTableColumnMetaData)
	{
		try {
			jTableColumnMetaData.jTable = new JTable(jTableColumnMetaData.modelTable);
			jTableColumnMetaData.jTable.setBounds(jTableColumnMetaData.rectangle);

			// Place label 2 * modus above JTable
			jTableColumnMetaData.rectangle.y = jTableColumnMetaData.rectangle.y - jTableColumnMetaData.jTable.getHeight() / 2 - modus; 		
			jTableColumnMetaData.tableLabel.setBounds(jTableColumnMetaData.rectangle);		

			// Set column"Header"Titles
			for (int i = 0; i < jTableColumnMetaData.getColumnHeaderTitles().size(); i++) {
				
				//String test = jTableColumnMetaData.getColumnHeader(i);
				//System.out.println(test);
				//jTableColumnMetaData.modelTable.addColumn(test);
				//TableColumn c = new TableColumn(i);
		        //c.setHeaderValue(jTableColumnMetaData.getColumnHeader(i));
		        //System.out.println("c = " +  c.getHeaderValue().toString());
		        //jTableColumnMetaData.modelTable.addColumn(c);
				//jTableColumnMetaData.jTable.addColumn(test);
				
				// Set Headervalue like normal row, since I can't get JTable columns to show :-(
				jTableColumnMetaData.modelTable.setValueAt(jTableColumnMetaData.getColumnHeader(i), 0, i);
				//goalTableColumnNames
				
				
				int width = jTableColumnMetaData.getColumnWidth(i);
				//jTableColumnMetaData.jTable.getColumnModel().getColumn(i).setFont(new Font("SansSerif", Font.BOLD, 12));
				jTableColumnMetaData.jTable.getColumnModel().getColumn(i).setPreferredWidth(width);
				
			}
			jTableColumnMetaData.jTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
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
		// Make the first row the selected
		matchTable.setRowSelectionInterval(1, 1);

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
					goalTable.setValueAt(goal.getMinute() + ":" + String.format("%02d", goal.getSecond()), rowNumber, Arrays.asList(goalTableColumnNames).indexOf(tidGoalColumn));
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
		
		int rowNumber = 1;
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
