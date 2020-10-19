

import java.awt.*;
import javax.swing.*;

public class MainWindow {
	
	private JFrame mainJFrame;
	public Turnament turnament; 
	public MainWindow(Turnament turnament) {
		super();
		this.turnament = turnament;
		initMainWindows();
	};

	private boolean initMainWindows() {
	try {
	    mainJFrame = new JFrame("Turnering");
	    mainJFrame.setSize(1000, 900);
	    
	    // Center JFrame
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    mainJFrame.setLocation(dim.width/2-mainJFrame.getSize().width/2, dim.height/2-mainJFrame.getSize().height/2); //source https://stackoverflow.com/questions/12072719/centering-the-entire-window-java/34869895#34869895
	    
	    mainJFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    
	    mainJFrame.add(new MainPanel(turnament));
	
	    mainJFrame.setVisible(true);
	    
	    return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	

}
