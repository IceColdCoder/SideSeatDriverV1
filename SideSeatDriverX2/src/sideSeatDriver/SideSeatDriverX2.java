package sideSeatDriver;

import sExecutor.SExecutor;
import sgui.SGUI;
import sharedImage.SharedCanvas;

public class SideSeatDriverX2 {
	
	SGUI sgui;
	private final int width = 800;
	private final int height = 800;
	
	public SideSeatDriverX2(){
		
		//Create map.
		
		
		//Create the gui and make it visible.
		sgui = new SGUI(width, height);
		sgui.setVisible(true);
		sgui.startAutoRefresh();
	}

}
