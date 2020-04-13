package sPathUtils;

import java.awt.Point;
import java.util.LinkedList;

import map.SMap;
import mapNode.MapNode;

public class SPathUtils {
	
	
	
	/**
	 * Function to get and return the path cost. Function rectangular area between both points.
	 * @param p0
	 * @param p1
	 * @return
	 */
	public float getPathCost(Point p0, Point p1){
		int dx = p1.x - p0.x;
		int dy = p1.y - p0.y;
		
		return this.abs((int)Math.sqrt(dx * dx + dy * dy));
	}
	
	/**
	 * Send in a list border nodes. Clears it and ands all border nodes to the new list.
	 * @param currentNode
	 * @param nodeList
	 */
	public void setBorderNodeList(SMap map, MapNode currentNode, LinkedList<MapNode> nodeList){
		nodeList.clear();
		
		int dx;
		int dy;
		
		for(int i = -1; i < 2; i++){
			for(int j = -1; j < 2; j++){
				
				dx = currentNode.posX + i;
				dy = currentNode.posY + j;
				
				if(map.isInBounds(dx, dy)){
					
					if(!(i == 0 && j == 0)){
						nodeList.add(map.get(dx, dy));
					}
				}
			}
		}
	}
	
	private int abs(int val){
		if(val < 0){
			val = val * -1;
		}
		return val;
	}

}
