package graphicalClasses;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;


// Inspired from: http://www.java2s.com/Tutorials/Java/Java_Swing/1600__Java_Swing_MDI.htm 
// and https://www.comp.nus.edu.sg/~cs3283/ftp/Java/swingConnect/friends/mdi-swing/mdi-swing.html
// and https://www.comp.nus.edu.sg/~cs3283/ftp/Java/swingConnect/archive/tech_topics_arch/frames_panes/frames_panes.html

public class MDIFrame extends JFrame {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	public JDesktopPane desktopPane = new JDesktopPane();
	public DesktopManager manager;
  

  public MDIFrame(String title) {
	  super(title);
	  desktopPane = new JDesktopPane();
	  desktopPane.setOpaque(false);
      getContentPane().add(desktopPane,BorderLayout.CENTER);

      manager = new DefaultDesktopManager();
      desktopPane.setDesktopManager(manager);
      //{
     }

  public void activateFrame(JInternalFrame f) {
  //super.activateFrame(f);
  System.out.println(f);
};
  
  public void addPanel(MDIChild panel) {
	  // OK desktopPane.add(panel);
	  desktopPane.add(panel,JDesktopPane.DEFAULT_LAYER);
  }
  
  public void loadTheStuff() {

	  this.setMinimumSize(new Dimension(350, 450));
	  this.setPreferredSize(new Dimension(950, 1050));
	  this.pack();
	  this.add(desktopPane, BorderLayout.CENTER);

  }
  
}
