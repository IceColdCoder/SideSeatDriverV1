/*
 * Map to contain MapNodes for quick access during path finding.
 * 
 * The function (height * y) + x returns the relative position in a contigious array in memory that would
 * normally require nested arrays (pointers to values).
 * 
 * Scott Franz
 */

package map;

import java.awt.image.BufferedImage;

import mapNode.MapNode;

public class SMap {
	
	private int width;
	private int height;
	
	private MapNode[] nodes;
	
	public SMap(int sizeX, int sizeY){
		width = sizeX;
		height = sizeY;
		nodes = new MapNode[width * height];
	}
	
	/**
	 * Set MapNode n at given (x,y).
	 * @param x
	 * @param y
	 * @param n
	 */
	public void set(int x, int y, MapNode n){
		nodes[(height * y) + x] = n;
	}
	
	/**
	 * Return MapNode at given (x,y).
	 * @param x
	 * @param y
	 * @return MapNode(x,y)
	 */
	public MapNode get(int x, int y){
		return nodes[(height * y) + x];
	}
	
	public void setMapData(BufferedImage mapImg){
		//Iterate through every pixel the SMap expects.
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				//Create a new node with the pixel value.
				MapNode node = new MapNode(mapImg.getRGB(i, j), i, j);
				this.set(i, j, node);
			}
		}
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public boolean isInBounds(int x, int y){
		if(x > -1 && x < width){
			if(y > -1 && y < height){
				return true;
			}
		}
		return false;
	}

}
