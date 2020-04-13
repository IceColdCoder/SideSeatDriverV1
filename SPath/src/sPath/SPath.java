package sPath;

import imageDispatch.ImageDispatch;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.SwingWorker;

import discovery.SPath_Discovery;
import pixelWrite.PixelWrite;
import premap.SPath_Premap;
import sExecutor.SExecutor;
import smath.SMath;
import map.SMap;

public class SPath extends SwingWorker<Integer,Integer>{
	
	//Vector for paths.
	private static Vector<SPath> pathVect = new Vector<SPath>(1,1);
	
	//Map in SPath.
	private SMap map = null;
	
	//Start point.
	private Point startPoint = null;
	
	//Finish point.
	private Point finishPoint = null;
	
	private final Color startPointColor = new Color(100, 0, 255, 0);
	private final Color finishPointColor = new Color(50, 0, 255, 0);
	
	private SPath(BufferedImage mapImg){
		//Create new map.
		map = new SMap(mapImg.getWidth(), mapImg.getHeight());
		//Set map data.
		map.setMapData(mapImg);
		//Inform the display system of the new map.
		ImageDispatch.getInstance().setCoreImage(mapImg);
		ImageDispatch.getInstance().setInputDims(map.getWidth(), map.getHeight());
	}
	
	public static SPath getCurrentPath(){
		if(pathVect.isEmpty()){
			return null;
		}
		return pathVect.lastElement();
	}
	
	public static void createNewSPath(BufferedImage mapImg){
		pathVect.addElement(new SPath(mapImg));
	}
	
	/**
	 * Sets the start point for the trace.
	 * @param p
	 * @param guiWidth
	 * @param guiHeight
	 */
	public void setStartPoint(Point p, int guiWidth, int guiHeight){
		//Scale point.
		p.x = SMath.scale(p.x, SMath.getScalar(map.getWidth(), guiWidth));
		p.y = SMath.scale(p.y, SMath.getScalar(map.getHeight(), guiHeight));
		
		//Set point.
		startPoint = p;
	}
	
	/**
	 * Sets the finish point for the trace.
	 * @param p
	 * @param guiWidth
	 * @param guiHeight
	 */
	public void setFinishPoint(Point p, int guiWidth, int guiHeight){
		//Scale point.
		p.x = SMath.scale(p.x, SMath.getScalar(map.getWidth(), guiWidth));
		p.y = SMath.scale(p.y, SMath.getScalar(map.getHeight(), guiHeight));
		
		//Set point.
		finishPoint = p;
	}
	
	public boolean startTrace(){
		//Check for start points.
		if(startPoint == null){
			return false;
		}
		if(finishPoint == null){
			return false;
		}
		
		//Start trace here.
		ImageDispatch.getInstance().submitPixel(new PixelWrite(startPoint.x, startPoint.y, startPointColor.getRGB()));
		ImageDispatch.getInstance().submitPixel(new PixelWrite(finishPoint.x, finishPoint.y, finishPointColor.getRGB()));
		
		
		this.execute();
		
		//Return true.
		return true;
	}
	
	public boolean isReady(){
		//Check start positions.
		if(startPoint == null){
			return false;
		}
		if(finishPoint == null){
			return false;
		}
		
		//Is ready to run.
		return true;
	}

	@Override
	protected Integer doInBackground() throws Exception {
		//Vars for trace.
		long premap_runtime;
		long discovery_runtime;
		
		//Start Trace.
		
		//Premap phase.
		SPath_Premap path_premap = new SPath_Premap(map);
		path_premap.execute();
		
		premap_runtime = path_premap.get();
		System.out.println("Premap run time = " + premap_runtime);
		
		//Discovery phase.
		SPath_Discovery path_discovery = new SPath_Discovery(map, startPoint, finishPoint);
		path_discovery.execute();
		
		discovery_runtime = path_discovery.get();
		System.out.println("Discovery run time = " + discovery_runtime);
		
		return null;
	}

}
