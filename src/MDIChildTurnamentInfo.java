import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class MDIChildTurnamentInfo extends JInternalFrame  {

	private static final long serialVersionUID = 1;
	private JPanel panel = new JPanel();
	private JLabel infoTableLabel = new JLabel();
	private JTable infoTable;
	DefaultTableModel infoTableModel;
	private String[] infoTableColumnNames =  { "", "Name", "Antal hold", "Antal kampe", "Antal mål", "Vinder"};
	private Integer[] infoTableColumnWidths = { Constants.modus, Constants.modus * 2, Constants.modus, Constants.modus, Constants.modus, Constants.modus * 2};	
	private JTableData infoTableMetaData;
	private TurnamentManager turnamentManager;

	// Constructor
	MDIChildTurnamentInfo(TurnamentManager turnamentManager) {

		// Call constructor of JInternalFrame, in order to get resize, close, maximize, minimize icons and proper initialization
		super("Information om åbne turneringer", true, false, true, true);
		this.turnamentManager = turnamentManager;

		infoTableModel = new DefaultTableModel(0, infoTableColumnNames.length);
		infoTableMetaData = new JTableData(infoTable, infoTableModel, infoTableColumnNames, infoTableColumnWidths, new Rectangle(Constants.modus, Constants.modus, 2 * Constants.stdTableWidth, Constants.stdTableWidth), infoTableLabel);
		infoTable = infoTableMetaData.createJtable(); 
		
		try {
			panel.add(infoTableLabel);
			panel.add(new TablePanel(infoTableMetaData));
			
			panel.setBackground(Color.LIGHT_GRAY);
			panel.setLayout(null);

			panel.setSize(Constants.mDIChildWidth, Constants.mDIChildHigth);
			panel.setVisible(true);
			
			getContentPane().add(panel);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g)
	{
		if (iconable==false) {
			super.paintComponent(g); // call super to ensure basic drawing			
		}
	}
	
	public void refreshInfoTable() {

		// Clear existing content of JTable
		for (int i = 0; i < infoTable.getRowCount(); i++) {
			for (int j = 0; j < infoTable.getColumnCount(); j++) {
				infoTable.setValueAt("", i, j);
			}
		}
		
		int requiredRows = turnamentManager.getTurnaments().size() - infoTableModel.getRowCount() +1;
		// Add rows to infoTable, if required
		for (int count = 1; count < requiredRows; count++) {
			infoTableModel.addRow(new Object[]{ "", "", "", "", "", ""});
		}
		
//		if (requiredRows < 0) {
//			for (int i = requiredRows; i <= 0; i++) {
//				infoTableModel.removeRow(0);
//
//			}
//		}
		
		infoTableLabel.setText(turnamentManager.getTurnaments().size() + " åbne turneringer");
		int rowNumber = 0;		
		for (Turnament turnament : turnamentManager.getTurnaments()) {

			int colNum = 0;
			infoTable.setValueAt("", rowNumber, colNum++);			
			infoTable.setValueAt(turnament.getName(), rowNumber, colNum++);
			infoTable.setValueAt(turnament.getNumberOfTeams(), rowNumber, colNum++);					
			infoTable.setValueAt(turnament.getNumberOfMatches(), rowNumber, colNum++);					
			infoTable.setValueAt(turnament.getNumberOfGoals(), rowNumber, colNum++);
			infoTable.setValueAt(turnament.getWinner().getName(), rowNumber, colNum++);			
			//System.out.println("Row " + rowNumber + " " + turnament.getName() + " " + turnament.getNumberOfTeams() );
			rowNumber++;
		}
	}
	
	

}
