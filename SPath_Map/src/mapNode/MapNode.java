/*
 * Class to contain a path data from a pixel in a map for ready access.
 * 
 * Scott Franz
 */

package mapNode;

import java.awt.Point;
import java.util.Arrays;
import java.util.Vector;

public class MapNode {
	
	//Position of node.
	public int posX;
	public int posY;
	public Point position;
	
	//Path values from color in pixel.
	public int green;
	public int blue;
	public int red;
	public int alpha;
	
	//Path values for terrain attributes.
	public boolean isBlocked = true;
	public boolean isLand = false;
	public boolean isAir = false;
	public boolean isWater = false;
	public boolean isBorder = false;
	
	private Vector<MapNode> borders = new Vector<MapNode>(2, 1);
	
	public MapNode(int argb, int posX, int posY){
		
		this.posX = posX;
		this.posY = posY;
		position = new Point(posX, posY);
		
		blue = 0xFF & (argb >> 0);
		green = 0xFF & (argb >> 8);
		red = 0xFF & (argb >> 16);
		alpha = 0xFF & (argb >> 24);
		
		if(green > 50){
			isLand = true;
			isBlocked = false;
		}
		else if(red > 50){
			isAir = true;
			isBlocked = false;
		}
		else if(blue > 50){
			isWater = true;
			isBlocked = false;
		}
		else{
			//Print error.
		}
	}
	
	public void addBorderNode(MapNode node){
		borders.addElement(node);
	}
	
	public Vector<MapNode> getBorderNodes(){
		return borders;
	}
	
	public boolean hasBorderNode(MapNode n){
		if(borders.contains(n)){
			return true;
		}
		return false;
		
	}
	
	public String toString(){
		return "<MapNode @(" + position + ")::" + "(isBlocked=" + isBlocked + ";isBorder=" + isBorder + ")>";
	}

}
