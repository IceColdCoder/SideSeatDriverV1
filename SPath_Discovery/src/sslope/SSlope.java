package sslope;

import java.awt.Point;

import smath.SMath;

public class SSlope {
	
	private int x0;
	private int xf;
	private int y0;
	private int yf;
	
	private float uX = 0;
	private float uY = 0;
	private int dx = 0;
	private int dy = 0;
	
	private float netX = 0;
	private float netY = 0;
	
	public SSlope(Point startPoint, Point finishPoint){
		this.getNewSlope(startPoint,finishPoint);
	}
	
	/**
	 * Set new slope.
	 * @param startPoint
	 * @param finishPoint
	 */
	public void getNewSlope(Point startPoint, Point finishPoint){
		//Set points.
		x0 = startPoint.x;
		y0 = startPoint.y;
		xf = finishPoint.x;
		yf = finishPoint.y;
		
		//Calc length.
		float length = (float)Math.sqrt(Math.pow(yf - y0, 2) + Math.pow(xf - x0, 2));
		
		//Calc unit vectors.
		uX = (float)(xf - x0) / length;
		uY = (float)(yf - y0) / length;
		
		float xMulti = 1;
		float yMulti = 1;
		
		if(uX < 0){
			xMulti = -1;
		}
		if(uY < 0){
			yMulti = -1;
		}
		
		//normalize unit vectors.
		if(SMath.abs(uX) > SMath.abs(uY)){
			uY = yMulti * SMath.abs(uY) / SMath.abs(uX);
			uX = xMulti;
		}
		else{
			uX = xMulti * SMath.abs(uX) / SMath.abs(uY);
			uY = yMulti;
		}
		
		netX = 0;
		netY = 0;
		
		netX = uX;
		netY = uY;
		
	}
	
	/**
	 * Get next point.
	 * @param coords
	 */
	public void getNext(Point p){
		netX += uX;
		netY += uY;
		
		if((int)SMath.abs(netX) > 0){
			p.x = p.x + (int)netX;
			netX = netX - (int)netX;
		}
		if((int)SMath.abs(netY) > 0){
			p.y = p.y + (int)netY;
			netY = netY - (int)netY;
		}
		
	}
	
	public float getDistance(Point p0, Point p1){
		return (float)Math.sqrt(Math.pow(p1.y - p0.y, 2) + Math.pow(p1.x - p0.x, 2));
	}
	
	private float abs(float val){
		if(val < 0){
			val = -1 * val;
		}
		return val;
	}

}
