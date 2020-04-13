package discovery.border;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;

import discovery.SPath_Discovery;
import pixelWrite.PixelWrite;
import sPathUtils.SPathUtils;
import sslope.SSlope;
import map.SMap;
import mapNode.MapNode;

public class SPath_BorderTrace {
	
	//Map vars.
	private SMap map;

	//Start and finish locs.
	private Point start_point;
	private Point finish_point;
	
	//Pointer to parent to call pubish.
	private SPath_Discovery parent;
	
	//Pointe to math object.
	private SPathUtils sutils;
	
	
	public SPath_BorderTrace(SMap map, SPathUtils sPathUtils, Point startPoint, Point finishPoint, SPath_Discovery disc){
		//Set param.
		this.map = map;
		start_point = startPoint;
		finish_point = finishPoint;
		parent = disc;
		sutils = sPathUtils;
	}
	
	public boolean runTrace(MapNode current_node, int border_count, LinkedList<MapNode> path_list){
		
		System.out.println("Border trace start...");
		
		System.out.println("Current Node == " + current_node);
		//Lists for near nodes.
		LinkedList<MapNode> near_nodes_l = new LinkedList<MapNode>();
		LinkedList<MapNode> near_nodes_r = new LinkedList<MapNode>();
		
		//Lists for left and right nodes.
		LinkedList<MapNode> left_list = new LinkedList<MapNode>();
		LinkedList<MapNode> right_list = new LinkedList<MapNode>();
		
		//Add start node to lists.
		left_list.add(current_node);
		right_list.add(current_node);
		
		//Make pointers to current nodes.
		MapNode left_node = getBorderNode(near_nodes_l, current_node, null);
		MapNode right_node = getBorderNode(near_nodes_r, current_node, left_node);
		
		//add nodes to lists.
		left_list.add(left_node);
		right_list.add(right_node);
		
		//nest inside while loop.
		boolean border_control = true;
		while(border_control){
			
			
			MapNode tmp_node_left;
			MapNode tmp_node_right;
			
			border_count++;
			//Make temp pointers to hold last stuff in the list.
			left_node = left_list.pollLast();
			right_node = right_list.pollLast();
			
			//Position vars.
			Point left_point = left_node.position;
			Point right_point = right_node.position;
			
			//Send prints.
			parent.printToScreen(new PixelWrite(left_point.x, left_point.y, Color.PINK));
			parent.printToScreen(new PixelWrite(right_point.x, right_point.y, Color.MAGENTA));
			
			//Make pointers to next border node. Function decides best option.
			tmp_node_left = getBorderNode(near_nodes_l, left_node, left_list.peekLast());
			tmp_node_right = getBorderNode(near_nodes_r, right_node, right_list.peekLast());
			
			//Put the removed nodes back in.
			left_list.add(left_node);
			right_list.add(right_node);
			
			//Put the new nodes on the list.
			left_list.add(tmp_node_left);
			right_list.add(tmp_node_right);
			
			//Check for breakout.
			tmp_node_left = map.get(left_point.x, left_point.y);
			tmp_node_right = map.get(right_point.x, right_point.y);
			//Left breakout.
			if(!tmp_node_left.isBlocked && !tmp_node_left.isBorder){
				//break out of loop.
				border_control = false;
				//Add the left traverse to the main list.
				path_list.addAll(left_list);
				//Add the first free node to the main list.
				path_list.add(tmp_node_left);
				System.out.println("Break left");
				return false;
			}
			//Right breakout.
			else if(!tmp_node_right.isBlocked && !tmp_node_right.isBorder){
				//Break out of loop.
				border_control = false;
				//Add right traverse to main list.
				path_list.addAll(right_list);
				//Add the first free node to the main list.
				path_list.add(tmp_node_right);
				System.out.println("Break right");
				return true;
			}
		}
		
		System.out.println("left_list.size = " + left_list.size() + " || right_list.size = " + right_list.size());
		
		System.out.println("Border trace complete!");
		
		return false;
	}
	
	private MapNode getBorderNode(LinkedList<MapNode> nodeList, MapNode current_node, MapNode except_node){

		//Update border node list.
		sutils.setBorderNodeList(map, current_node, nodeList);
		
		//Path cost.
		float pathcost = 99999999;
		
		//Pointer to border node.
		MapNode borderNode = null;
		
		//Pointer to breakout node.
		MapNode breakNode = null;
		
		//Check for breakout and border node.
		for (MapNode node : nodeList){
			float tmpCost = sutils.getPathCost(node.position, finish_point);
			if(node.isBorder && !node.equals(except_node)){
				borderNode = node;
			}
			else if(pathcost > tmpCost){
				pathcost = tmpCost;
				breakNode = node;
			}
		}
		
		//If node can break free.
		if(!breakNode.isBlocked){
			return breakNode;
		}
		//If node is next border.
		else if(borderNode != null){
			return borderNode;
		}
		
		//Oooops.
		return null;
		
	}
	


}
