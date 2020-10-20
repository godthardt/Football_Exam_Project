

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;

public class MainPanel extends JPanel{

	JLabel player1Label = new JLabel();
	JTextField player1nameTextField = new JTextField();
	JLabel player2Label = new JLabel();
	JTextField player2nameTextField = new JTextField();
	JButton closeButton = new JButton();
	//JTextArea textArea = new JTextArea();
	JTable teamTable;
	JTable matchTable;
	Random rand;
	Turnament turnament;

	// Column Names
	final String teamIdColumn = "Team Id";
	String[] teamTableColumnNames =  { "No.", teamIdColumn, "Team name", "Points"};
	String[] matchTableColumnNames = { "No.", "MatchId", "Hometeam", "Awayteam", "Score"};    
	private int currentTeamId;



	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);              // tegn først baggrunden på panelet

		//    g.drawLine(0,0,50,50);
		//    g.fillOval(5,10,300,30);
		//    g.setColor(Color.GREEN);
		//    g.drawString("Hej grafiske verden!",100,30);
	}

	public MainPanel(Turnament turnament) {
		super();
		try {
			this.turnament = turnament;
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		player1Label.setText("Label1");
		player1Label.setBounds(new Rectangle(16, 16, 128, 16));
		player2Label.setText("Label2");
		player2Label.setBounds(new Rectangle(256, 16, 128, 16));


		player1nameTextField.setText("");
		player1nameTextField.setBounds(new Rectangle(110, 12, 112, 29));
		player2nameTextField.setText("");
		player2nameTextField.setBounds(new Rectangle(350, 12, 112, 29));

		closeButton.setText("Close");
		closeButton.setBounds(600, 12, 112, 29);

		DefaultTableModel teamTableModel = new DefaultTableModel(turnament.getNumberOfTeams() + 1, teamTableColumnNames.length);
		DefaultTableModel matchTableModel = new DefaultTableModel((turnament.getNumberOfTeams() ) * 2 + 1, matchTableColumnNames.length);

		//model = (DefaultTableModel)table.getModel();
		//model.addColumn("S.No");https://www.tutorialspoint.com/how-to-change-each-column-width-of-a-jtable-in-java


		// Set column Names 
		for (int i = 0; i < teamTableColumnNames.length; i++) {
			teamTableModel.setValueAt(teamTableColumnNames[i], 0,i);
		}

		for (int i = 0; i < matchTableColumnNames.length; i++) {
			matchTableModel.setValueAt(matchTableColumnNames[i], 0,i);
		}

		int j = 1;
		for (Team t : turnament.getTeams()) {
			int colNum = 0;
			teamTableModel.setValueAt(j, j, colNum++);    	
			teamTableModel.setValueAt(t.getId(), j, colNum++);
			teamTableModel.setValueAt(t.getName(), j, colNum++);
			teamTableModel.setValueAt(t.getPoints(), j, colNum++);    	
			j++;
		}

		teamTable = new JTable(teamTableModel);
		teamTable.setBounds(new Rectangle(16, 64, 400, 210));

		matchTable = new JTable(matchTableModel);
		matchTable.setBounds(new Rectangle(16, 316, 400, 400));

		this.setLayout(null);
		this.add(player1nameTextField);
		this.add(player1Label);
		this.add(player2nameTextField);
		this.add(player2Label);
		this.add(closeButton);
		this.add(teamTable);
		//this.add(textArea);
		this.add(matchTable);


		rand = new Random();
		//this.repaint();

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeButton_actionPerformed(e);
			}

			private void closeButton_actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//player2nameTextField.setText("Yes");
				System.exit(0);

			}
		});

		teamTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = teamTable.rowAtPoint(evt.getPoint());
				int col = teamTable.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					//player1nameTextField.setText(tm.getValueAt(row, col).toString());
					currentTeamId = Integer.parseInt(teamTableModel.getValueAt(row, Arrays.asList(teamTableColumnNames).indexOf(teamIdColumn)).toString());
					//player2nameTextField.setText("Current team id = " + tm.getValueAt(row, Arrays.asList(columnNames).indexOf(teamIdColumn)).toString());
					listMatches(currentTeamId);
				}
			}
		});

	}

	private void listMatches(int teamID) {
		//textArea.setText("");
		int rowNumber = 1;
		for (Match m : turnament.getMatches()) {
			int colNum = 0;

			if ((m.getHomeTeam().getId() == teamID) || (m.getAwayTeam().getId() == teamID)) {
				matchTable.setValueAt(rowNumber, rowNumber, colNum++);
				matchTable.setValueAt(m.getMatchNo(), rowNumber, colNum++);
				matchTable.setValueAt(m.getHomeTeam().getName(), rowNumber, colNum++);
				matchTable.setValueAt(m.getAwayTeam().getName(), rowNumber, colNum++);
				matchTable.setValueAt(m.getHomeGoals() + " - " + m.getAwayGoals(), rowNumber, colNum++);			
				//textArea.append(m.getHomeTeam().getName() + " - " + m.getAwayTeam().getName() + " " + m.getHomeGoals() + " - " + m.getAwayGoals() + "\n");
				rowNumber++;

			}
		}

	}


}
