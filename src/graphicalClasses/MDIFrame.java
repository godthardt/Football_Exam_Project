package graphicalClasses;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;


// Inspired from: http://www.java2s.com/Tutorials/Java/Java_Swing/1600__Java_Swing_MDI.htm 
public class MDIFrame extends JFrame {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private final JDesktopPane desktopPane = new JDesktopPane();
  

  public MDIFrame(String title) {
	  super(title);
  }
  
  public void addPanel(MainPanel panel) {
	  desktopPane.add(panel);
	  
  }
  
  public void loadTheStuff() {

//	  JInternalFrame frame1 = new JInternalFrame("Frame 1", true, true, true,
//			  true);
//
//	  JInternalFrame frame2 = new JInternalFrame("Frame 2", true, true, true,
//			  true);
//
//	  frame1.getContentPane().add(new JLabel("Frame 1  contents..."));
//	  frame1.pack();
//	  frame1.setVisible(true);
//
//	  frame2.getContentPane().add(new JLabel("Frame 2  contents..."));
//	  frame2.pack();
//	  frame2.setVisible(true);
//
//	  int x2 = frame1.getX() + frame1.getWidth() + 10;
//	  int y2 = frame1.getY();
//	  frame2.setLocation(x2, y2);

//	  desktopPane.add(frame1);
//	  desktopPane.add(frame2);

	  this.add(desktopPane, BorderLayout.CENTER);

	  this.setMinimumSize(new Dimension(950, 1050));
	  //this.pack();

  }
  
}
