

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

	private UUID guid = java.util.UUID.randomUUID(); // use GUID to identify the object - inspired from https://stackoverflow.com/questions/2982748/create-a-guid-in-java
	public UUID getGuid() { return guid;}
	private int windowNumber = 1;
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
	
	private JTableColumnMetaData teamTableMetaData;
	private JTableColumnMetaData matchTableMetaData;
	private JTableColumnMetaData goalTableMetaData;
	private JTableColumnMetaData playerTableMetaData;	
	
	Turnament turnament;
	private int modus = 16; // Predefined modus for margin space etc.
	int stdTableWidth = 34 * modus;	
	private int slimColumnWidth = modus;
	private int mediumColumnWidth = 60;
	private int largeColimnWidth = 80;	

	// Column Names
	private final String teamIdColumn = "Hold Id";
	private final String pointColumn =  "Point";
	private final String teamNameColumn =  "Holdnavn";	
	private String[] teamTableColumnNames =  { "Nr.", teamIdColumn, teamNameColumn, "Kampe", "Målscore", pointColumn};
	// Column widths
	private Integer[] teamTableColumnWidths = { slimColumnWidth, slimColumnWidth, largeColimnWidth, slimColumnWidth, mediumColumnWidth, slimColumnWidth};

	private final String matchIdColumn = "Kamp Id"; //"tagged" so I can search for column later
	private String[] matchTableColumnNames = { "Nr.", "Runde", matchIdColumn, "Dato", "Hjemmehold", "Udehold", "Score"};
	private Integer[] matchTableColumnWidths = { slimColumnWidth, slimColumnWidth, slimColumnWidth, mediumColumnWidth, largeColimnWidth, mediumColumnWidth, slimColumnWidth};	

	private final String tidGoalColumn = "Tidspunkt";	 //"tagged" so I can search for column later
	private final String goalScorerColumn = "Målscorer"; //"tagged" so I can search for column later
	private String[] goalTableColumnNames =  { "Nr.", "Stilling", tidGoalColumn, goalScorerColumn};
	private Integer[] goalTableColumnWidths = { slimColumnWidth, mediumColumnWidth, slimColumnWidth, largeColimnWidth};
	
	private String[] playerTableColumnNames =  { "Nr.", "Navn", "Kontraktudløb", "Mål"};
	private Integer[] playerTableColumnWidths = { slimColumnWidth, largeColimnWidth, mediumColumnWidth, slimColumnWidth};
	
	ImageIcon imageIcon;
		
	// Constructor
	public MDIChild(int windowNumber, Turnament turnament) {
		super(turnament.getName(), true, true, true, true);
		try {
			this.turnament = turnament;
			this.windowNumber = windowNumber; 
			this.title = Integer.toString(windowNumber++) + ". "  + turnament.getName();
			windowName = turnament.getName();
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

        loadImage("boring.jpg");
        boringLabel.setIcon(imageIcon);
		
		int numberOfMatchesPrTeam = (turnament.getNumberOfTeams() - 1) * 2;

		DefaultTableModel teamTableModel = new DefaultTableModel(turnament.getNumberOfTeams(), teamTableColumnNames.length);
		DefaultTableModel matchTableModel = new DefaultTableModel(numberOfMatchesPrTeam, matchTableColumnNames.length);
		DefaultTableModel goalTableModel = new DefaultTableModel(Constants.maxGoalsPrMatch, goalTableColumnNames.length);
		DefaultTableModel playerTableModel = new DefaultTableModel(turnament.getHighestNumberOfPlayersInOneTeam(), playerTableColumnNames.length);

		teamTableMetaData = new JTableColumnMetaData(teamTable, teamTableModel, teamTableColumnNames, teamTableColumnWidths, new Rectangle(modus, 2*modus, stdTableWidth, 13*modus), teamTableLabel);
		matchTableMetaData = new JTableColumnMetaData(matchTable, matchTableModel, matchTableColumnNames, matchTableColumnWidths, new Rectangle(modus, 17*modus, stdTableWidth, 14*modus), matchTableLabel);
		goalTableMetaData = new JTableColumnMetaData(goalTable, goalTableModel, goalTableColumnNames, goalTableColumnWidths, new Rectangle(modus, 33*modus, stdTableWidth, 9*modus), goalTableLabel);
		playerTableMetaData = new JTableColumnMetaData(playerTable, playerTableModel, playerTableColumnNames, playerTableColumnWidths, new Rectangle(stdTableWidth + 3* modus, 2*modus, stdTableWidth / 2, 40*modus), playerTabelLabel);	
		
		// Set column Names and column widths
		teamTable = createJtable(teamTableMetaData);
		matchTable = createJtable(matchTableMetaData);
		goalTable = createJtable(goalTableMetaData);
		playerTable = createJtable(playerTableMetaData);
		playerTable.setAutoCreateRowSorter(true);
		
		goalTable.add(boringLabel);
		goalTable.setAutoCreateRowSorter(true);


		// Labels
		panel.add(teamTableLabel);
		panel.add(matchTableLabel);
		panel.add(goalTableLabel);		
		panel.add(playerTabelLabel);		

		panel.add(new TablePanel(teamTableMetaData));
		panel.add(new TablePanel(matchTableMetaData));
		panel.add(new TablePanel(playerTableMetaData));
		panel.add(new TablePanel(goalTableMetaData));		
		
		panel.setSize(Constants.mDIChildWidth, Constants.mDIChildHigth);

		
		// Tried with a layoutmanager, which is the Java "way" but it doesn't look good in this app in my opinion
		//FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		//panel.setLayout(flowLayout);
		//panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		panel.setLayout(null);

		
		panel.setVisible(true);
		
		getContentPane().add(panel);

		loadTeamsIntoTable(teamTable);		

		// listeners
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
		
		teamTable.getSelectionModel().addListSelectionListener((ListSelectionListener) new ListSelectionListener(){ //https://stackoverflow.com/questions/10128064/jtable-selected-row-click-event
		    @Override
			public void valueChanged(ListSelectionEvent event) {
				// If no row is selected getSelectedRow() returns minus 1
				if (teamTable.getSelectedRow() > -1) {
					teamTableSelectionChanged(Integer.parseInt(teamTableModel.getValueAt(teamTable.getSelectedRow(), Arrays.asList(teamTableColumnNames).indexOf(teamIdColumn)).toString()));
				}
			}
		});
		
		teamTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int col = teamTable.columnAtPoint(e.getPoint());

		        JTableHeader th = teamTable.getTableHeader();
				TableColumnModel tcm = th.getColumnModel();
				TableColumn tc = tcm.getColumn(col);
				String name = tc.getHeaderValue().toString();
				if (name==pointColumn) {
					Collections.sort(turnament.getTeams(), new SortbyPoints());
				}
				if (name ==teamNameColumn) {
					turnament.getTeams().sort(null);
				}
				
				loadTeamsIntoTable(teamTable);
		        System.out.println("Column index selected " + col + " " + name);
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
						e.printStackTrace();
					}
				}
			}
		});

		// Respond to change in selected row (eg. use arrow up / down)
		matchTable.getSelectionModel().addListSelectionListener((ListSelectionListener) new ListSelectionListener(){ //https://stackoverflow.com/questions/10128064/jtable-selected-row-click-event
		    @Override			
			public void valueChanged(ListSelectionEvent event) {
		    	matchTableSelectionChanged(getSelectedMacthId());
			}
		});
		
		System.out.println("MDIChild number " + windowNumber + " loaded");		
	}
	
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

	private void loadTeamsIntoTable(JTable jTable) {
		clearTable(teamTable);
		int j = 0;
		for (Team t : turnament.getTeams()) {
			int colNum = 0;
			jTable.setValueAt(j+1, j, colNum++);    	
			jTable.setValueAt(t.getId(), j, colNum++);
			jTable.setValueAt(t.getName(), j, colNum++);
			jTable.setValueAt(turnament.GetNumberOfMatchesForTeam(t), j, colNum++);
			// Get goal score for team 
			GoalResult goalResult = turnament.goalsScoredAndTakenForTeam(t);
			jTable.setValueAt(goalResult.scored + " - " + goalResult.taken, j, colNum++);			
			jTable.setValueAt(t.getPoints(), j, colNum++);    	
			j++;
		}
		teamTable.setRowSelectionInterval(0, 0);
		teamTableSelectionChanged(Constants.dummyGetItYourself);
	}
	
	private void teamTableSelectionChanged(int teamId)
	{
		// If it is an initial call on program startup
		if (teamId==Constants.dummyGetItYourself) {
			teamId = Integer.parseInt(teamTable.getValueAt(0, Arrays.asList(teamTableColumnNames).indexOf(teamIdColumn)).toString());	
		}
		listMatches(teamId);
		listPlayers(teamId);
	}

	private void matchTableSelectionChanged(int matchId)
	{
		listGoals(matchId);
	}
	
	private JTable createJtable(JTableColumnMetaData jTableColumnMetaData)
	{
		try {
			jTableColumnMetaData.jTable = new JTable(jTableColumnMetaData.modelTable);

			// Place label 2 * modus above JTable
			jTableColumnMetaData.tableLabel.setBounds(new Rectangle(jTableColumnMetaData.rectangle.x, jTableColumnMetaData.rectangle.y - 3 * modus, stdTableWidth, 5 * modus));

			// Set column"Header"Titles
			for (int i = 0; i < jTableColumnMetaData.getColumnHeaderTitles().size(); i++) {
				
				int width = jTableColumnMetaData.getColumnWidth(i);
				jTableColumnMetaData.jTable.getColumnModel().getColumn(i).setPreferredWidth(width);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jTableColumnMetaData.jTable;
	}
	
	private void listMatches(int teamId) {
		clearTable(matchTable);
		clearTable(goalTable);
		int rowNumber = 0;
		matchTableLabel.setText("Kampe for " + (turnament.GetTeam(teamId)).getName() + ":");
		for (Match m : turnament.getMatches()) {
			int colNum = 0;

			if ((m.getHomeTeam().getId() == teamId) || (m.getAwayTeam().getId() == teamId)) {
				matchTable.setValueAt(rowNumber + 1, rowNumber, colNum++);
				matchTable.setValueAt(m.getRoundNo(), rowNumber, colNum++);
				matchTable.setValueAt(m.getMatchNo(), rowNumber, colNum++);
				matchTable.setValueAt(m.getDate().format(DateTimeFormatter.ofPattern(Constants.dkDateFormat)).toString(), rowNumber, colNum++); 			
				matchTable.setValueAt(m.getHomeTeam().getName(), rowNumber, colNum++);
				matchTable.setValueAt(m.getAwayTeam().getName(), rowNumber, colNum++);
				matchTable.setValueAt(m.getHomeGoals() + " - " + m.getAwayGoals(), rowNumber, colNum++);
				rowNumber++;
			}
		}
		// Make the first row the selected
		matchTable.setRowSelectionInterval(0, 0);
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
	
	private void listGoals(int matchId) {

		clearTable(goalTable);
		
		
		turnament.sortGoalsByTime();
		
		int rowNumber = 0;
		boolean noGoals = true;
		boolean goalTableLabelSat = false;
		// Iterate through matches
		for (Match m : turnament.getMatches()) {
			if (m.getMatchNo() == matchId) {
				if (goalTableLabelSat==false) {
					goalTableLabel.setText("Mål i kampen: " + m.getHomeTeam().getName() + " - " + m.getAwayTeam().getName() );
					goalTableLabelSat = true;
				}
				int homeGoals = 0;
				int awayGoals = 0;
				
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

        boringLabel.setBounds(modus, modus, 19* modus, 6 * modus);
        boringLabel.setLocation(8 * modus, 1);
		boringLabel.setVisible(noGoals);  // show a "stamp" in 0-0 matches
		boringLabel.repaint();
	}
	
	private void listPlayers(int teamId)
	{
		clearTable(playerTable);
		
		int rowNumber = 0;
		Team team = turnament.GetTeam(teamId);
		playerTabelLabel.setText("Kontraktspillere i " + team.getName());
		for (Contract contract : team.getTeamContracts()) {
				int colNum = 0;
				playerTable.setValueAt(rowNumber+1, rowNumber, colNum++);
				playerTable.setValueAt(contract.getPlayerName(), rowNumber, colNum++);
				playerTable.setValueAt(contract.getEndDatee().format(DateTimeFormatter.ofPattern(Constants.dkDateFormat)).toString(), rowNumber, colNum++);
				int goalsForPlayer = turnament.GetGoalsForPlayer(contract.getPlayerId());
				if (goalsForPlayer > 0) {
					playerTable.setValueAt(turnament.GetGoalsForPlayer(contract.getPlayerId()), rowNumber, colNum++);					
				} 
				rowNumber++;
		}
	}

	public void clearTables() {
		clearTable(teamTable);
		clearTable(matchTable);
		clearTable(goalTable);
		clearTable(playerTable);					
	}
	
	public void regenerateGoals() {
		try {
			turnament.reGenerateGoals();
			loadTeamsIntoTable(teamTable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean serializeTurnament(String filename) {
		try {
			turnament.serializeTurnament(filename);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
	
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

// Help class to organize data/component related to a JTable
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

// In order for JScrollPane to work with JTable on JInternalFrames, the JTalbe (and JSCrollPane) must be
// placed on their own panels - used 2 full days to make this work :-( maybe I have missed something ?!
class TablePanel extends JPanel{

	private static final long serialVersionUID = 1;

	public TablePanel(JTableColumnMetaData jTableColumnMetaData){

		setBackground(Color.GRAY); // Very usefull when I debugged, found that it looked nice afterwards
		
		for (int i = 0; i < jTableColumnMetaData.getColumnHeaderTitles().size(); i++) {
			// Set column widths
			jTableColumnMetaData.jTable.getColumnModel().getColumn(i).setPreferredWidth(jTableColumnMetaData.getColumnWidth(i));
		
			// Set column Headers (Title of column)
			JTableHeader th = jTableColumnMetaData.jTable.getTableHeader();
			TableColumnModel tcm = th.getColumnModel();
			TableColumn tc = tcm.getColumn(i);
			tc.setHeaderValue(jTableColumnMetaData.getColumnHeader(i));
		}

		try {
			// Try to set header in bold font
			jTableColumnMetaData.jTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));	
		} catch (Exception e) {
			// Bold font could not be set - not very important
			e.printStackTrace();
		}

		// Define the size of the panel on which the JTable is placed
		setSize(new Dimension(jTableColumnMetaData.rectangle.width , jTableColumnMetaData.rectangle.height));
		jTableColumnMetaData.jTable.setPreferredScrollableViewportSize(new Dimension(jTableColumnMetaData.rectangle.width - 30, jTableColumnMetaData.rectangle.height - 30));
		// Use predefined columnsorter, which sorts on strings "11" before "2" - could be improved
		//jTableColumnMetaData.jTable.setAutoCreateRowSorter(true); 
		setLocation(jTableColumnMetaData.rectangle.x, jTableColumnMetaData.rectangle.y);

		JScrollPane jScrollPane=new JScrollPane(jTableColumnMetaData.jTable);
		jScrollPane.setVisible(true);
		add(jScrollPane);

	}


}