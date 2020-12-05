

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.time.format.DateTimeFormatter;

// Inspired by https://docs.oracle.com/javase/tutorial/uiswing/components/internalframe.html
public class MDIChild extends JInternalFrame implements Comparable<MDIChild> {
	private static final long serialVersionUID = 1;

	private boolean teamRankColumnSortOrderAscending = false; // Should not be called directly
	// this, and the next 4 methods is used to switch between ascending and descending sort order
	public boolean getTeamRankColumnSortOrderAscending() {  
		teamRankColumnSortOrderAscending = !teamRankColumnSortOrderAscending;
		return teamRankColumnSortOrderAscending;
	}
	private boolean teamNameColumnSortOrderAscending = false; // Should not be called directly
	public boolean getTeamNamekColumnSortOrderAscending() {  
		teamNameColumnSortOrderAscending = !teamNameColumnSortOrderAscending;
		return teamNameColumnSortOrderAscending;
	}
	private boolean teamPointColumnSortOrderAscending = false; // Should not be called directly
	public boolean getTeamPointColumnSortOrderAscending() {  
		teamPointColumnSortOrderAscending = !teamPointColumnSortOrderAscending;
		return teamPointColumnSortOrderAscending;
	}
	
	private boolean teamGoalScoreColumnSortOrderAscending = false; // Should not be called directly
	public boolean getTeamGoalScoreColumnSortOrderAscending() {  
		teamGoalScoreColumnSortOrderAscending = !teamGoalScoreColumnSortOrderAscending;
		return teamGoalScoreColumnSortOrderAscending;
	}
	
	private boolean teamNumberOfMatchesColumnSortOrderAscending = false; // Should not be called directly
	public boolean getTeamNumberOfMatchesColumnSortOrderAscending() {  
		teamNumberOfMatchesColumnSortOrderAscending = !teamNumberOfMatchesColumnSortOrderAscending;
		return teamNumberOfMatchesColumnSortOrderAscending;
	}

	private int windowNumber; // keep tract of sequence of windows
	public int getWindowNumber() { return windowNumber; }
	private String windowName;
	public String getWindowName() { return windowName; }
	
	private JPanel panel = new JPanel();

	private JLabel teamTableLabel = new JLabel();
	private JLabel matchTableLabel = new JLabel();
	private JLabel goalTableLabel = new JLabel();
	private JLabel playerTabelLabel = new JLabel();
	private JLabel boringLabel = new JLabel();

	private JTable teamTable;
	private JTable matchTable;
	private JTable goalTable;
	private JTable playerTable;
	
	private JTableData teamTableMetaData;
	private JTableData matchTableMetaData;
	private JTableData goalTableMetaData;
	private JTableData playerTableMetaData;	
	
	Turnament turnament;
	private int modus = Constants.getModus(); // Predefined modus for margin space etc. Use local variable to shorten code line lengths
	int stdTableWidth = Constants.getStdTableWidth();	
	private int slimColumnWidth = modus;
	private int mediumColumnWidth = 4 * modus;
	private int largeColimnWidth = 5 * modus;
	private int dontShow = 0;

	// Column Names (some "tagged" by "constants" (final strings) so I can search for column names later)
	private final String teamRankingColumn = "Placering";
	private final String teamIdColumn = "Hold Id";
	private final String teamNameColumn = "Holdnavn";
	private final String teamNumberOfMatchesColumn = "Kampe";	
	private final String teamGoalScoreColumn = "Målscore";
	private final String teamPointColumn = "Point";
	
	private String[] teamTableColumnNames =  { teamRankingColumn, teamIdColumn, teamNameColumn, teamNumberOfMatchesColumn, teamGoalScoreColumn, teamPointColumn};
	// Column widths
	private Integer[] teamTableColumnWidths = { slimColumnWidth, dontShow, largeColimnWidth, slimColumnWidth, mediumColumnWidth, slimColumnWidth};
	private Integer[] teamTableAlignments = { SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER};

	private final String matchIdColumn = "Kamp Id"; 
	private final String matchRoundNo = "Runde";	
	private String[] matchTableColumnNames = {matchRoundNo, matchIdColumn, "Art", "Dato", "Hjemmehold", "Udehold", "Resultat"};
	private Integer[] matchTableColumnWidths = { slimColumnWidth, dontShow, largeColimnWidth, mediumColumnWidth, largeColimnWidth, largeColimnWidth, slimColumnWidth};

	private final String tidGoalColumn = "Tidspunkt";	 //"tagged" so I can search for column later
	private final String goalScorerColumn = "Målscorer"; //"tagged" so I can search for column later
	private String[] goalTableColumnNames =  { "Nr.", "Stilling", tidGoalColumn, goalScorerColumn};
	private Integer[] goalTableColumnWidths = { slimColumnWidth, mediumColumnWidth, slimColumnWidth, largeColimnWidth};
	
	private String[] playerTableColumnNames =  { "Nr.", "Navn", "Mål", "Kontraktudløb"};
	private Integer[] playerTableColumnWidths = { slimColumnWidth, largeColimnWidth, slimColumnWidth, mediumColumnWidth};
	
	ImageIcon imageIcon; //used for boringLabel
		
	// Constructor
	public MDIChild(int windowNumber, Turnament turnament) {
		super(turnament.getName(), true, true, true, true);
		try {
			this.turnament = turnament;
			this.windowNumber = windowNumber; 
			this.title = turnament.getName();
			windowName = turnament.getName();
			initGraphics();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	// "Main"-method for loading components
	private void initGraphics() throws Exception {

		teamTableLabel.setText("Stillingen:");
		matchTableLabel.setText("Kampe:");
		goalTableLabel.setText("Mål:");
		playerTabelLabel.setText("Kontraktspillere:");

        // Special label on goalTable
		if (loadImage("boring.jpg")==false) {
			System.out.println("Could not load boring.jpg which was expected to be sited in the project root directory");
		}
        boringLabel.setIcon(imageIcon);
		boringLabel.setBounds(modus, modus, 19* modus, 6 * modus);
        boringLabel.setLocation(8 * modus, 35*modus);
		
		int numberOfMatchesPrTeam = (turnament.getNumberOfTeams() - 1) * 2;

		// construct 4 DefaultTableModel objects, to be used with the 4 JTables 
		DefaultTableModel teamTableModel = new DefaultTableModel(turnament.getNumberOfTeams(), teamTableColumnNames.length);
		DefaultTableModel matchTableModel = new DefaultTableModel(numberOfMatchesPrTeam, matchTableColumnNames.length);
		DefaultTableModel goalTableModel = new DefaultTableModel(Constants.maxGoalsPrMatch, goalTableColumnNames.length);
		DefaultTableModel playerTableModel = new DefaultTableModel(turnament.getHighestNumberOfPlayersInOneTeam(), playerTableColumnNames.length);

		// Create the JTable
		teamTableMetaData = new JTableData(teamTable, teamTableModel, teamTableColumnNames, teamTableColumnWidths, new Rectangle(modus, 2*modus, stdTableWidth, 14*modus), teamTableLabel);
		matchTableMetaData = new JTableData(matchTable, matchTableModel, matchTableColumnNames, matchTableColumnWidths, new Rectangle(modus, 18*modus, stdTableWidth, 13*modus), matchTableLabel);
		goalTableMetaData = new JTableData(goalTable, goalTableModel, goalTableColumnNames, goalTableColumnWidths, new Rectangle(modus, 33*modus, stdTableWidth, 9*modus), goalTableLabel);
		playerTableMetaData = new JTableData(playerTable, playerTableModel, playerTableColumnNames, playerTableColumnWidths, new Rectangle(stdTableWidth + 2 * modus, 2*modus, 20*modus, 40*modus), playerTabelLabel);	
		
		// Set column Names and column widths
		teamTable = teamTableMetaData.createJtable();
		matchTable = matchTableMetaData.createJtable();
		goalTable = goalTableMetaData.createJtable();
		playerTable = playerTableMetaData.createJtable();

		// Put prebuild "string"-sorter on playerTable and goalTable 
		playerTable.setAutoCreateRowSorter(true);
		goalTable.setAutoCreateRowSorter(true);

		// Tried with different layoutmanagers (Flow and Border), which is the "Java way" but it doesn't look good in this app in my opinion
		//panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		//panel.setLayout(new BorderLayout()); // remembered to add components to BorderLayout.CENTER and BorderLayout.EAST
		panel.setLayout(null);
		
		// Labels and tables
		panel.add(teamTableLabel);
		panel.add(new TablePanel(teamTableMetaData));
		
		for (int i = 0; i < teamTableAlignments.length; i++) {
			DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
			cellRenderer.setHorizontalAlignment(teamTableAlignments[i]);
			teamTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);			
		}

		panel.add(matchTableLabel);
		panel.add(new TablePanel(matchTableMetaData));

		panel.add(goalTableLabel);
		panel.add(boringLabel);
		
		panel.add(new TablePanel(goalTableMetaData));		
		
		panel.add(playerTabelLabel);
		panel.add(new TablePanel(playerTableMetaData));
		
		panel.setSize(Constants.getMDIChildWidth(), Constants.getMDIChildHigth());
		panel.setVisible(true);
		
		getContentPane().add(panel);

		// Load some initial data into the teamTable
		loadTeamsIntoTable(teamTableMetaData);		

		// attach a number of listeners to teamTable and matchTable
		
		// Respond to the mouse, being click on the teamTable
		teamTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = teamTable.rowAtPoint(evt.getPoint());
				int col = teamTable.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					teamTableSelectionChanged(Integer.parseInt(teamTableModel.getValueAt(row, Arrays.asList(teamTableColumnNames).indexOf(teamIdColumn)).toString()));
				}
			}
		});
		
		// Respond to row selection change - typically by the arrow keys
		teamTable.getSelectionModel().addListSelectionListener((ListSelectionListener) new ListSelectionListener(){ //https://stackoverflow.com/questions/10128064/jtable-selected-row-click-event
		    @Override
			public void valueChanged(ListSelectionEvent event) {
				// If no row is selected getSelectedRow() returns minus 1
				if (teamTable.getSelectedRow() > -1) {
					teamTableSelectionChanged(Integer.parseInt(teamTableModel.getValueAt(teamTable.getSelectedRow(), Arrays.asList(teamTableColumnNames).indexOf(teamIdColumn)).toString()));
				}
			}
		});

		// Respond to the mouse, being click on the HEADER of the teamTable
		teamTable.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int clickedColumn = teamTable.columnAtPoint(e.getPoint());
				boolean columnSorted = true;
				boolean ascOrDescSorted = true;
				
				// Get the column header text of the column in which the user clicked 
				JTableHeader th = teamTable.getTableHeader();
				TableColumnModel tcm = th.getColumnModel();
				TableColumn tc = tcm.getColumn(clickedColumn);
				String columnName = tc.getHeaderValue().toString();

				// Sort columns acsending or descending according to what have been clicked on
				if (columnName==teamPointColumn) {
					ascOrDescSorted = getTeamPointColumnSortOrderAscending();
					Collections.sort(turnament.getTeams(), new SortbyPoints(ascOrDescSorted));
				} 
				else if (columnName==teamGoalScoreColumn) {
					ascOrDescSorted = getTeamGoalScoreColumnSortOrderAscending();					
					Collections.sort(turnament.getTeams(), new SortbyGoalScore(ascOrDescSorted));
				} 
				else if (columnName==teamRankingColumn) {
					ascOrDescSorted = getTeamRankColumnSortOrderAscending();					
					Collections.sort(turnament.getTeams(), new SortbyRanking(ascOrDescSorted));
				} 
				else if (columnName==teamNumberOfMatchesColumn) {
					ascOrDescSorted = getTeamNumberOfMatchesColumnSortOrderAscending();					
					Collections.sort(turnament.getTeams(), new SortbyNumberOfMatches(ascOrDescSorted));
				} 
				else if (columnName ==teamNameColumn) {
					ascOrDescSorted = getTeamNamekColumnSortOrderAscending();					
					Collections.sort(turnament.getTeams(), new SortbyName(ascOrDescSorted));
				} else {
					System.out.println("Column " + clickedColumn + " \"" + columnName + "\" does not support sorting");
					columnSorted = false;
				}

				if (columnSorted==true) {
					loadTeamsIntoTable(teamTableMetaData);
					teamTable.setRowSelectionInterval(0, 0);
					teamTableLabel.setText("Stilling: (sorteret " + ascOrDesc(ascOrDescSorted) + " efter " + columnName + ")"  );					
				}
			}

			private String ascOrDesc(boolean ascOrDescSorted) {
				if (ascOrDescSorted == false) 
					return "stigende";
				else
					return "faldende";
			}
		});		

		// Respond to the mouse, being click on the matchTable  
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
						e.printStackTrace();
					}
				}
			}
		});

		// Respond to change in selected row in the matchTable (eg. arrow up / down)
		matchTable.getSelectionModel().addListSelectionListener((ListSelectionListener) new ListSelectionListener(){ //https://stackoverflow.com/questions/10128064/jtable-selected-row-click-event
		    @Override			
			public void valueChanged(ListSelectionEvent event) {
		    	matchTableSelectionChanged(getSelectedMacthId());
			}
		});
		
	}
	
	// return the MatchId of the row which is selected in the matchTable
	private int getSelectedMacthId()
	{
		int currentmatchId = -1;
		
		// If no row is selected getSelectedRow() returns minus 1
		if (matchTable.getSelectedRow() > -1) {
			try {
				String cellValue = matchTable.getValueAt(matchTable.getSelectedRow(), Arrays.asList(matchTableColumnNames).indexOf(matchIdColumn)).toString();
				if (cellValue.isEmpty() == false) {
					currentmatchId = Integer.parseInt(cellValue);
					return currentmatchId;
				}
				
				return currentmatchId;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return currentmatchId;
	}

	private void loadTeamsIntoTable(JTableData tableData) {
		clearTable(tableData.jTable);
		int j = 0;
		tableData.ajustNumberOfRows(turnament.getTeams().size());

		for (Team t : turnament.getTeams()) {
			int colNum = 0;
			//System.out.println("J= " + j);
			tableData.jTable.setValueAt(t.getRankInTurnament()+".", j, colNum++);
			tableData.jTable.setValueAt(t.getId(), j, colNum++);
			tableData.jTable.setValueAt(t.getName(), j, colNum++);
			tableData.jTable.setValueAt(turnament.GetNumberOfMatchesForTeam(t), j, colNum++);
			int goalDifference = t.getHomeGoals()-t.getAwayGoals();
			String plusSign = "";
			if (goalDifference > 0)
				plusSign = "+";
			tableData.jTable.setValueAt(t.getHomeGoals() + " - " + t.getAwayGoals() +" (" + plusSign + (t.getHomeGoals()-t.getAwayGoals())  + ")", j, colNum++);			
			tableData.jTable.setValueAt(t.getPoints(), j, colNum++);    	
			j++;
		}
		teamTable.setRowSelectionInterval(0, 0);
		teamTableSelectionChanged(Integer.parseInt(teamTable.getValueAt(0, Arrays.asList(teamTableColumnNames).indexOf(teamIdColumn)).toString()));
	}
	
	private void teamTableSelectionChanged(int teamId)
	{
		listMatches(matchTableMetaData, teamId);
		listPlayers(playerTableMetaData, teamId);
	}

	private void matchTableSelectionChanged(int matchId)
	{
		listGoals(matchId);
	}
	
	private void listMatches(JTableData tableData, int teamId) {
		clearTable(tableData.jTable);
		clearTable(goalTable);
		int rowNumber = 0;
		tableData.tableLabel.setText("Kampe for " + (turnament.GetTeam(teamId)).getName() + ":");
		
		
		for (Match m : turnament.getMatches()) {
			int colNum = 0;

			if ((m.getHomeTeam().getId() == teamId) || (m.getAwayTeam().getId() == teamId)) {
				//Add one row at a time
				tableData.ajustNumberOfRows(rowNumber + 1);
				
				if (m.getRoundNo()==0)
					// superliga rounds is not assigned explicitly, just counted via for loop
					tableData.jTable.setValueAt(rowNumber + 1, rowNumber, colNum++);					
				else
					tableData.jTable.setValueAt(m.getRoundNo(), rowNumber, colNum++);
				
				tableData.jTable.setValueAt(m.getMatchId(), rowNumber, colNum++);
				tableData.jTable.setValueAt(m.getMatchType(), rowNumber, colNum++);				
				tableData.jTable.setValueAt(m.getDate().format(DateTimeFormatter.ofPattern(Constants.dkDateFormat)).toString(), rowNumber, colNum++); 			
				tableData.jTable.setValueAt(m.getHomeTeam().getName(), rowNumber, colNum++);
				tableData.jTable.setValueAt(m.getAwayTeam().getName(), rowNumber, colNum++);
				tableData.jTable.setValueAt(m.getHomeGoals() + " - " + m.getAwayGoals(), rowNumber, colNum++);
				rowNumber++;
			}
		}
		// Make the first row the selected
		tableData.jTable.setRowSelectionInterval(0, 0);
		// Make sure the matchTable is updated to reflect the selected team
		matchTableSelectionChanged(getSelectedMacthId());

	}

	private void clearTable(JTable table) {
		// clearJTable
		for (int i = 0; i < table.getRowCount(); i++) {
			for (int j = 0; j < table.getColumnCount(); j++) {
				table.setValueAt("", i, j);
			}
		}
	}
	
	// Show relevant goal, belonging to the match, which is selected is the matchTable
	private void listGoals(int matchId) {

		clearTable(goalTable);
		
		int rowNumber = 0;
		boolean noGoals = true;
		boolean goalTableLabelSat = false;
		// Iterate through matches
		for (Match m : turnament.getMatches()) {
			if (m.getMatchId() == matchId) {
				if (goalTableLabelSat==false) {
					goalTableLabel.setText("Mål i kampen: " + m.getHomeTeam().getName() + " - " + m.getAwayTeam().getName() );
					goalTableLabelSat = true;
				}
				int homeGoals = 0;
				int awayGoals = 0;
				
				goalTableMetaData.ajustNumberOfRows(m.getGoals().size());
				for (Goal goal : m.getGoals()) {
					int colNum = 0;					
					goalTable.setValueAt(rowNumber +1, rowNumber, colNum++);
					goalTable.setValueAt(goal.getMinute() + ":" + String.format("%02d", goal.getSecond()), rowNumber, Arrays.asList(goalTableColumnNames).indexOf(tidGoalColumn));
					if (goal.getGoalType()== Goal.GoalType.Home) {
						homeGoals++;
						noGoals = false;
					}
					else {
						awayGoals++;
						noGoals = false;						
					}
					goalTable.setValueAt(homeGoals + "-" + awayGoals, rowNumber, 1);
					goalTable.setValueAt(goal.getGoalScorer().getName(), rowNumber, Arrays.asList(goalTableColumnNames).indexOf(goalScorerColumn));
					rowNumber++;
				}
				break;
			}
		}
		boringLabel.setVisible(noGoals);  // show a "stamp" in 0-0 matches
	}
	
	private void listPlayers(JTableData tableData, int teamId)
	{
		clearTable(playerTable);
		
		int rowNumber = 0;
		Team team = turnament.GetTeam(teamId);
		
		tableData.ajustNumberOfRows(team.getTeamPlayers().size());
		tableData.tableLabel.setText("Kontraktspillere i " + team.getName());
		for (Player player : team.getTeamPlayers()) {
				int colNum = 0;
				tableData.jTable.setValueAt(rowNumber+1, rowNumber, colNum++);
				tableData.jTable.setValueAt(player.getName(), rowNumber, colNum++);
				int goalsForPlayer = turnament.GetGoalsForPlayer(player.getId());
				if (goalsForPlayer > 0) 
					tableData.jTable.setValueAt(goalsForPlayer, rowNumber, colNum++);					
				else
					colNum++;
				
				tableData.jTable.setValueAt(player.getContractEndDate().format(DateTimeFormatter.ofPattern(Constants.dkDateFormat)).toString(), rowNumber, colNum++);				
				rowNumber++;
		}
	}

	public void clearTables() {
		clearTable(teamTable);
		clearTable(matchTable);
		clearTable(goalTable);
		clearTable(playerTable);					
	}
	
	// "re-simulate matches and goals 
	public void reGenerateMatchesAndGoals() {
		try {
			turnament.reGenerateMatchesAndGoals();
			loadTeamsIntoTable(teamTableMetaData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Serialize turnament into filestream
	public boolean serializeTurnament(String filename) {
		try {
			turnament.serializeTurnament(filename);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// Try to load an image from a file
	private boolean loadImage(String filename) {
		try {
			imageIcon = new ImageIcon(filename);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}	

	@Override
	public int compareTo(MDIChild m) {
		if (this.windowNumber > m.windowNumber) 
			return 1;
		else	
			return -1;
	}
}

// Help class to organize data/components related to a JTable
class JTableData {
	// Purpose: To organize data relating to the same JTable
	private ArrayList<String> columnHeaderTitles;
	private ArrayList<Integer> columnHeaderWidths;
	public ArrayList<String> getColumnHeaderTitles() { return columnHeaderTitles; }
	JTable jTable;
	JLabel tableLabel;
	DefaultTableModel modelTable;
	Rectangle rectangle;

	JTableData(JTable jTable, DefaultTableModel modelTable, String[] headerTitlesArray, Integer[] HeaderWidthsArray, Rectangle rectangle, JLabel tableLabel) {
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
	
	public JTable createJtable()
	{
		try {
			jTable = new JTable(modelTable);
			
			// make sure only 1 row at a time can be selected, found at https://stackoverflow.com/questions/28128035/how-to-add-table-header-and-scrollbar-in-jtable-java
			jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);  

			// Place label 2 * modus above JTable and indent a little modus / 2
			tableLabel.setBounds(new Rectangle(rectangle.x + Constants.getModus() / 2, rectangle.y - 3 * Constants.getModus(), Constants.getStdTableWidth(), 5 * Constants.getModus()));

			// Set column size and preferred width
			for (int i = 0; i < getColumnHeaderTitles().size(); i++) {
				
				int width = getColumnWidth(i);
				jTable.getColumnModel().getColumn(i).setPreferredWidth(width);
				// Hide columns, if preferred width is set to 0 (used for "lookup" columns TeamId and MatchId
				if (width==0) {
					jTable.getColumnModel().getColumn(i).setMinWidth(width);
					jTable.getColumnModel().getColumn(i).setMaxWidth(width);					
				}
				
				// Set column Headers (Title of column)
				JTableHeader th = jTable.getTableHeader();
				TableColumnModel tcm = th.getColumnModel();
				TableColumn tc = tcm.getColumn(i);
				tc.setHeaderValue(getColumnHeader(i));
			}
			
			try {
				// Try to set header in bold font
				jTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));	
			} catch (Exception e) {
				// Bold font could not be set - not very important
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jTable;
	}
	
	// Makes sure, that the dataModel contains the required number of rows
	public void ajustNumberOfRows(int requiredNumberOfRows)
	{
		// not enough rows ?
		if (modelTable.getRowCount() < requiredNumberOfRows) {
			// add a number of rows
			for (int count = modelTable.getRowCount(); count < requiredNumberOfRows; count++) {
				modelTable.addRow(getRowData(modelTable.getColumnCount()));
			}
		} else
		
		// to many rows ?
		if (modelTable.getRowCount() > requiredNumberOfRows) {
			// Remove unnecessary row
			int lastRowToRemain = requiredNumberOfRows;
			int numberOfRowsBeforeRemove = modelTable.getRowCount();
			for (int i = requiredNumberOfRows; i < numberOfRowsBeforeRemove; i++) {
				modelTable.removeRow(lastRowToRemain);
			}
		}

	}
	
	private Vector<Object> getRowData(int columnCount) {

		// Construct an empty row - inspired from https://stackoverflow.com/questions/30525235/dynamic-row-creation-in-jtable
		Vector<Object> rowData = new Vector<Object>();
		for (int i = 0; i < columnCount; i++) {
			rowData.add("");
		}
		return rowData;
	}
}

// In order for JScrollPane to work with JTable on JInternalFrames, the JTable (and JSCrollPane) must be
// placed on their own panels - used 2 full days to make this work :-( I probably have missed something ?!
class TablePanel extends JPanel{

	private static final long serialVersionUID = 1;

	public TablePanel(JTableData jTableColumnMetaData){

		//setBackground(Color.LIGHT_GRAY); // Very useful when I debugged, found that it looked nice afterwards
		
		// Define the size of the panel on which the JTable (JScrollPane) is placed
		setSize(new Dimension(jTableColumnMetaData.rectangle.width , jTableColumnMetaData.rectangle.height));
		jTableColumnMetaData.jTable.setPreferredScrollableViewportSize(new Dimension(jTableColumnMetaData.rectangle.width - 30, jTableColumnMetaData.rectangle.height - 30));
		setLocation(jTableColumnMetaData.rectangle.x, jTableColumnMetaData.rectangle.y);

		JScrollPane jScrollPane=new JScrollPane(jTableColumnMetaData.jTable);
		jScrollPane.setVisible(true);
		add(jScrollPane);
	}
}