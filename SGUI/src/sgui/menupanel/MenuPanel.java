package sgui.menupanel;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;

import sPath.SPath;
import sgui.SGUI;
import sgui.SGUI.MapPointSelectMode;
import fileFilters.FileFilters;
import fileUtils.FileUtils;

public class MenuPanel extends JPanel{
	
	
	
	private final int height = 50;
	private UpperPanel upperPanel;
	private LowerPanel lowerPanel;
	
	private SGUI gui;
	
	public MenuPanel(int width, SGUI gui){
		this.gui = gui;
		Dimension d = new Dimension(width, height);
		this.setPreferredSize(d);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		upperPanel = new UpperPanel(new Dimension(width, height / 2));
		this.add(upperPanel);
		
		lowerPanel = new LowerPanel(new Dimension(width, height /2));
		this.add(lowerPanel);
	}
	
	public void resetSelectButtons(){
		lowerPanel.resetButtonToggles();
	}
	
	//Class for upper panel.
	private class UpperPanel extends JPanel{
		
		//File chooser.
		//Scottought file path.
		//private final JFileChooser fileChooser = new JFileChooser("H:\\Dropbox\\Dropbox\\scott_umt_cs\\SideSeatDriverX2\\workspace_1\\SideSeatDriverX2\\res\\pathimg");
		//User-PC file path.
		private final JFileChooser fileChooser = new JFileChooser("D:\\Dropbox\\Dropbox\\scott_umt_cs\\SideSeatDriverX2\\workspace_1\\SideSeatDriverX2\\res\\pathimg");
		
		//Menu buttons.
		private JMenuItem closeButton;
		private JMenuItem openButton;
		
		public UpperPanel(Dimension d){
			//Set layout manager.
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.setPreferredSize(d);
			
			//Set file chooser.
			fileChooser.addChoosableFileFilter(FileFilters.getNewImageFilter());
			
			//Create JMenuBar.
			JMenuBar menuBar = new JMenuBar();
			
			//Add menu bar to this panel.
			this.add(menuBar);
			
			//Create file menu.
			JMenu filemenu = new JMenu("File");
			filemenu.setPreferredSize(new Dimension(75, 35));
			
			//Add file menu to menu bar.
			menuBar.add(filemenu);
			
			openButton = new JMenuItem("Open");
			openButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//Handle open file action.
					int retval = fileChooser.showOpenDialog(openButton);
					
					if(retval == JFileChooser.APPROVE_OPTION){
						//Get selected file.
						File file = fileChooser.getSelectedFile();
						//Have application open file.
						SPath.createNewSPath(FileUtils.openImage(file));
						//Enable buttons.
						lowerPanel.enableButtons();
						//Repaint map.
						gui.repaintMap();
					}
		
				}
				
			});
			filemenu.add(openButton);
			
			//Create close button.
			closeButton = new JMenuItem("Close");
			closeButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//Exit system.
					System.exit(1);
				}
				
			});
			filemenu.add(closeButton);
			

			//Create a horizontal glue to force left alignment.
			this.add(Box.createHorizontalGlue());

		}
	}
	
	
	//Class for lower panel.
	private class LowerPanel extends JPanel{
		
		private JToggleButton setPoint_0; //Button for start point.
		private JToggleButton setPoint_1; //Button for finish point.
		private JButton startTrace; //Button for start trace.
		
		
		public LowerPanel(Dimension d){
			//Set default vars.
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.setPreferredSize(d);
			
			//Create set start point.
			setPoint_0 = new JToggleButton("Set: Start Point");
			setPoint_0.setEnabled(false);
			setPoint_0.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					switch(gui.getMapPointSelectMode()){
					//Case for the mode being set for start. Reset status to default.
					case START:
						setPoint_0.setSelected(false);
						gui.setMapPointSelectMode(MapPointSelectMode.NULL);
						break;
					//Case for button being in finish mode. Set to start mode.
					case FINISH:
						setPoint_1.setSelected(false);
						setPoint_0.setSelected(true);
						gui.setMapPointSelectMode(MapPointSelectMode.START);
						break;
					//Case for button being in default mode. Set to start mode.
					case NULL:
						setPoint_0.setSelected(true);
						gui.setMapPointSelectMode(MapPointSelectMode.START);
						break;
					//This should never happen.
					default:
						System.out.println("ERROR::setPoint_0 fell through to default on button press.");
					}
				}
				
			});
			this.add(setPoint_0);
			
			//Create set finish point.
			setPoint_1 = new JToggleButton("Set: Finish Point");
			setPoint_1.setEnabled(false);
			setPoint_1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					switch(gui.getMapPointSelectMode()){
					//Case for button being in start mode. Switch to finish mode.
					case START:
						setPoint_0.setSelected(false);
						setPoint_1.setSelected(true);
						gui.setMapPointSelectMode(MapPointSelectMode.FINISH);
						break;
					//Case for button being in finish mode. Switch to default mode.
					case FINISH:
						setPoint_1.setSelected(false);
						gui.setMapPointSelectMode(MapPointSelectMode.NULL);
						break;
					//Case for button being in default mode. Switch to finish mode.
					case NULL:
						setPoint_1.setSelected(true);
						gui.setMapPointSelectMode(MapPointSelectMode.FINISH);
						break;
					//This should never happen.
					default:
						System.out.println("ERROR:: setPoint_1 fell through to default on button press");
					}
				}
				
			});
			this.add(setPoint_1);
			
			//Create start trace button.
			startTrace = new JButton("Start Trace");
			startTrace.setEnabled(false);
			startTrace.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					startTrace.setEnabled(false);
					if(SPath.getCurrentPath().startTrace() == false){
						startTrace.setEnabled(true);
					}

				}
				
			});
			this.add(startTrace);
			
			//Create horizontal glue to force left alignment.
			this.add(Box.createHorizontalGlue());
		}
		
		public void enableButtons(){
			//Enable all buttons.
			setPoint_0.setEnabled(true);
			setPoint_1.setEnabled(true);
		}
		
		public boolean enableTraceButton(){
			//Check SPath to see if it's ready.
			if(SPath.getCurrentPath().isReady() == true){
				startTrace.setEnabled(true);
				return true;
			}
			return false;
		}
		
		public void resetButtonToggles(){
			//Reset button toggles.
			setPoint_0.setSelected(false);
			setPoint_1.setSelected(false);
			
			//Check trace button
			enableTraceButton();
		}
	}

}
