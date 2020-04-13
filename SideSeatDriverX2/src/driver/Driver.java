package driver;

import javax.swing.UIManager;

import sideSeatDriver.SideSeatDriverX2;

public class Driver {

	public static void main(String[] args) {
		
		//Set program to feel like windows - I hope.
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		//Create new side seat driver.
		SideSeatDriverX2 ssd = new SideSeatDriverX2();

	}

}
