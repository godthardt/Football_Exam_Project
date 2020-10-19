

import java.awt.*;
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
	JTextArea textArea = new JTextArea();
	JTable teamTable; 
	Random rand;
	Turnament turnament;

  
	
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
	player1Label.setText("Player 1 name:");
	player1Label.setBounds(new Rectangle(16, 16, 128, 16));
	player2Label.setText("Player 2 name:");
	player2Label.setBounds(new Rectangle(256, 16, 128, 16));
	
	
	player1nameTextField.setText("");
	player1nameTextField.setBounds(new Rectangle(110, 12, 112, 29));
	player2nameTextField.setText("");
	player2nameTextField.setBounds(new Rectangle(350, 12, 112, 29));
	
	closeButton.setText("Close");
	closeButton.setBounds(600, 12, 112, 29);
	
	textArea.setBounds(new Rectangle(16, 64, 200, 300));
	textArea.setEditable(false);
	textArea.setColumns(7);
	textArea.setRows(14);
	textArea.append("A");
	textArea.append("B\n");
	textArea.append("C\n");
	
	// Column Names 
    String[] columnNames = { "No.", "Team Id", "Team name", "Points"};

	DefaultTableModel tm = new DefaultTableModel(turnament.getNumberOfTeams() + 1,columnNames.length);
	
	// Set column Names 
	for (int i = 0; i < columnNames.length; i++) {
        tm.setValueAt(columnNames[i], 0,i);
	}
    
    int j = 1;
    for (Team t : turnament.getTeams()) {
    	int k = 0;
    	tm.setValueAt(j, j, k++);    	
    	tm.setValueAt(t.getId(), j, k++);
    	tm.setValueAt(t.getName(), j, k++);
    	tm.setValueAt(t.getPoints(), j, k++);    	
    	j++;
	}
    
    teamTable = new JTable(tm);
    teamTable.setBounds(new Rectangle(316, 64, 600, 210));
    
    this.setLayout(null);
    this.add(player1nameTextField);
    this.add(player1Label);
    this.add(player2nameTextField);
    this.add(player2Label);
    this.add(closeButton);
    this.add(textArea);
    this.add(teamTable);    
    
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
	            //JOptionPane optionPane = new JOptionPane(); optionPane.showMessageDialog(this, tm.getValueAt(row, col).toString(), "Mouse click");
	        	player1nameTextField.setText(tm.getValueAt(row, col).toString());
	
	        }
	    }
	});
    
  }	  

}
