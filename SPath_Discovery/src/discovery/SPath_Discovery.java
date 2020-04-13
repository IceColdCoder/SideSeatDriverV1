package discovery;

import imageDispatch.ImageDispatch;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingWorker;

import discovery.border.SPath_BorderTrace;
import map.SMap;
import mapNode.MapNode;
import pixelWrite.PixelWrite;
import sPathUtils.SPathUtils;
import sslope.SSlope;

public class SPath_Discovery  extends SwingWorker<Long,PixelWrite>{

	//Map vars.
	private SMap map;

	//Start and finish locs.
	private Point start_point;
	private Point finish_point;
	
	//Number for max path cost.
	private final float max_path_cost = 9999999;
	
	//Array for node offsets.
	private final int[] x_offset = new int[]{-1, 0, 1, -1, 1, -1, 0, 1};
	private final int[] y_offset = new int[]{1, 1, 1, 0, 0, -1, -1, -1};
	
	//Linked list containing final path.
	private LinkedList<MapNode> final_path_list = null;
	
	//Utils object.
	//Create utils object.
	private SPathUtils sutils = new SPathUtils();

	public SPath_Discovery(SMap map, Point startPoint, Point finishPoint){
		//Set param.
		this.map = map;
		start_point = startPoint;
		finish_point = finishPoint;
	}

	@Override
	protected Long doInBackground() throws Exception {

		Long return_val = System.nanoTime();
		
		System.out.println("Discovery phase start...");
	
		//Create linked list with near nodes.
		LinkedList<MapNode> near_nodes = new LinkedList<MapNode>();
		
		//Create list.
		LinkedList<MapNode> path_list = new LinkedList<MapNode>();

		//Current location vars.
		Point current_loc = new Point(start_point);
		
		//Add node to path list.
		path_list.add(map.get(current_loc.x, current_loc.y));

		//Direction vars.
		SSlope slope = new SSlope(start_point, finish_point);

		boolean control = true;
		int border_count = 0;

		//Loop until path discovered.
		while(control){
			
			//Make a pointer to the current node.
			MapNode current_node = path_list.peekLast();
			current_loc = current_node.position;
			
			System.out.println("<Var>current_node position: " + current_node.toString());
			
			//Check for arrival.
			if(finish_point.y - current_node.position.y < 3 && finish_point.x - current_node.position.x < 3){
				control = false;
				//Send Pixel write.
				publish(new PixelWrite(start_point.x, start_point.y, Color.BLUE));
				publish(new PixelWrite(finish_point.x, finish_point.y, Color.BLUE));
				publish(new PixelWrite(current_node.posX, current_node.posY, Color.RED));
				
			}
			else{
				
				//Send pixel write.
				publish(new PixelWrite(current_node.posX, current_node.posY, Color.CYAN));
				
				//If a border was reached.
				if(current_node.isBorder){
					System.out.println("Current Node == " + current_node);
					
					//Create border trace.
					SPath_BorderTrace borderTrace = new SPath_BorderTrace(map, sutils, start_point, finish_point, this);
					if(!borderTrace.runTrace(current_node, border_count, path_list)){
						System.out.println("Border Trace failed.");
					}
					
				}
				
				//Default cause - open ground.
				else{
					System.out.println("Before getNext(): " + current_node);
					//Get the next location. Current_node set by function.
					current_node = this.getNext(current_node, near_nodes);
					System.out.println("After getNext(): " + current_node);
					//Add node to list.
					path_list.add(current_node);
				}
				
				
			}
				


		}

		System.out.println("Discovery phase complete!");
		System.out.println("Border count = " + border_count);

		return_val = System.nanoTime() - return_val;
		return return_val;
	}

	protected void process(List<PixelWrite> v){
		for(PixelWrite px : v){
			ImageDispatch.getInstance().submitPixel(px);
		}
	}
	
	private void printArray(MapNode[] array){
		System.out.println("[" + array[0] + "] [" + array[1] + "] [" + array[2] + "]");
		System.out.println("[" + array[3] + "] [" + "xxxxxx" + "] [" + array[4] + "]");
		System.out.println("[" + array[5] + "] [" + array[6] + "] [" + array[7] + "]");
	}
	
	@Deprecated
	private MapNode getBorderNode(int x, int y, MapNode except_node){
		//Get border node.
		for(int i = -1; i < 2; i++){
			for(int j = -1; j < 2; j++){
				int dx = x + i;
				int dy = y + j;
				
				if(map.isInBounds(dx, dy)){
					MapNode tmp_node = map.get(dx, dy);
					if(tmp_node.isBorder){
						if(!tmp_node.equals(except_node)){
							return tmp_node;
						}
							
					}
				}
			}

		}
		return null;
		
	}
	
	private int abso(int val){
		if(val < 0){
			val = val * -1;
		}
		return val;
	}
	
	/**
	 * Set the input point "current" location to the new location.
	 * @param current
	 * @param finishPoint
	 */
	@Deprecated
	private void getNextPoint(Point current, Point finishPoint){
		int x = finishPoint.y - current.y;
		int y = finishPoint.x - current.x;
		int dx = 0;
		int dy = 0;
		
		if(x < 0){
			dx = -1;
		}
		else if(x > 0){
			dx = 1;
		}
		
		if(y < 0){
			dy = -1;
		}
		else if(y > 0){
			dy = 1;
		}
		
		if(x == 0){ //If vertical.
			current.y = current.y + dy;
		}
		else if(y == 0){ //If horizontal.
			current.x = current.x + dx;
		}
		else{ //If diagonal.
			if(this.abso(2 * x) < this.abso(y)){ //if y/2 > x
				current.y = current.y + dy;
			}
			else if(this.abso(x * y) < this.abso(x)){ //if x/2 > y
				current.x = current.x + dx;
			}
			else{
				current.x = current.x + dx;
				current.y = current.y + dy;
			}
		}
	}
	
	public MapNode getNext(MapNode currentNode, LinkedList<MapNode> nodeList){
		
		//Update node list.
		sutils.setBorderNodeList(map, currentNode, nodeList);
		
		System.out.println(nodeList.size());
		
		//pathcost.
		float pathcost = 99999999;
		
		
		
		//Iterate through list and get best path cost.
		for(MapNode node : nodeList){
			//Get path cost.
			float tmpCost = sutils.getPathCost(node.position, finish_point);
			
			//If cost is less, set best node.
			if(tmpCost < pathcost){
				pathcost = tmpCost;
				currentNode = node;
			}
		}
		
		return currentNode;
	}
	
	public void printToScreen(PixelWrite pw){
		this.publish(pw);
	}

}
