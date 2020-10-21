

import java.awt.*;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

public class MainWindow {
	
	private JFrame mainJFrame;
	public Turnament turnament;
	public Dimension dim;
	public MainWindow(Turnament turnament) {
		super();
		this.turnament = turnament;
		initMainWindows();
	};

	private boolean initMainWindows() {
	try {
	    mainJFrame = new JFrame("Turnering: " + turnament.getName() + " - afvikles fra " + turnament.GetStartDate().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString() + " til " + turnament.GetEndDate().format(DateTimeFormatter.ofPattern("dd. MMM YYYY")).toString());
	    mainJFrame.setSize(1100, 1000);
	    

	    dim = Toolkit.getDefaultToolkit().getScreenSize();
	    // Center JFrame
	    mainJFrame.setLocation(dim.width/2-mainJFrame.getSize().width/2, dim.height/2-mainJFrame.getSize().height/2); //source https://stackoverflow.com/questions/12072719/centering-the-entire-window-java/34869895#34869895
	    mainJFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    mainJFrame.add(new MainPanel(this, turnament));
	    mainJFrame.setVisible(true);
	    
	    return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	

}
