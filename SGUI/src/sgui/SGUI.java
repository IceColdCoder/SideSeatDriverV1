package sgui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

import sgui.mappanel.MapPanel;
import sgui.menupanel.MenuPanel;

public class SGUI {
	
	//Enum for selecting points on the map.
	public enum MapPointSelectMode{NULL, START, FINISH};
	
	private JFrame mainframe;
	private MapPanel mapPanel;
	private MenuPanel menuPanel;
	
	private MapPointSelectMode selectMode = MapPointSelectMode.NULL;
	
	public SGUI(int width, int height){
		//Create JFrame.
		mainframe = new JFrame();
		
		//Create main panel.
		JPanel mainpanel = new JPanel(new GridBagLayout());
		mainframe.add(mainpanel);
		GridBagConstraints gbc = new GridBagConstraints();
		
		//Create menu panel.
		menuPanel = new MenuPanel(width, this);
		gbc.gridx = 0;
		gbc.gridy = 0;
		mainpanel.add(menuPanel, gbc);
		
		//Create map panel.
		mapPanel = new MapPanel(new Dimension(width, height), this);
		gbc.gridx = 0;
		gbc.gridy = 1;
		mainpanel.add(mapPanel, gbc);
		
		//Finalize.
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.pack();
		mainframe.setResizable(false);
	}


	
	public void setVisible(boolean v){
		mainframe.setVisible(v);
	}
	
	/**
	 * Set map point select mode.
	 * @param mode
	 */
	public void setMapPointSelectMode(MapPointSelectMode mode){
		selectMode = mode;
	}
	
	/**
	 * Get the gui's current select mode.
	 * @return
	 */
	public MapPointSelectMode getMapPointSelectMode(){
		return selectMode;
	}
	
	/**
	 * Reset the menu panel's select buttons.
	 */
	public void resetSelectMode(){
		//Reset select mode.
		this.setMapPointSelectMode(MapPointSelectMode.NULL);
		//Reset buttons.
		menuPanel.resetSelectButtons();
	}
	
	/**
	 * Calls for a repaint of the map panel.
	 */
	public void repaintMap(){
		mapPanel.refresh();
	}
	
	/**
	 * Call to start the map draw panel to refresh its frame.
	 */
	public void startAutoRefresh(){
		mapPanel.startAutoRefresh();
	}

}
