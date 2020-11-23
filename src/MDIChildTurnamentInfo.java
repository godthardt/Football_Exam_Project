import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
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
	private String[] infoTableColumnNames =  { "Name", "Antal hold", "Antal kampe", "Antal mål"};
	private Integer[] infoTableColumnWidths = { Constants.modus, Constants.modus, Constants.modus, Constants.modus};	
	private JTableData infoTableMetaData;	

	MDIChildTurnamentInfo(TurnamentManager turnamentManager) {

		infoTableModel = new DefaultTableModel(12, infoTableColumnNames.length);

		infoTableMetaData = new JTableData(infoTable, infoTableModel, infoTableColumnNames, infoTableColumnWidths, new Rectangle(Constants.modus, 2*Constants.modus, Constants.stdTableWidth, 13*Constants.modus), infoTableLabel);
		infoTable = infoTableMetaData.createJtable(); 
		
		try {
			for (int i = 0; i < infoTableColumnNames.length; i++) {

				// Set column Headers (Title of column)
				JTableHeader th = infoTable.getTableHeader();
				TableColumnModel tcm = th.getColumnModel();
				TableColumn tc = tcm.getColumn(i);
				tc.setHeaderValue(infoTableColumnNames[i]);
				infoTable.getColumnModel().getColumn(i).setPreferredWidth(Constants.modus);
			}

			panel.add(infoTableLabel);
			panel.add(new TablePanel(infoTableMetaData));
			
			panel.setBackground(Color.YELLOW);
			panel.setLayout(null);

			panel.setSize(Constants.mDIChildWidth, Constants.mDIChildHigth);
			panel.setVisible(true);
			
			getContentPane().add(panel);

		} catch (Exception e) {

			e.printStackTrace();
		}


	}

}
