package premap;

import imageDispatch.ImageDispatch;

import java.awt.Color;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.SwingWorker;

import pixelWrite.PixelWrite;
import map.SMap;
import mapNode.MapNode;

public class SPath_Premap extends SwingWorker<Long,PixelWrite>{

	private SMap map;
	private boolean isDone = false;

	public SPath_Premap(SMap map){
		this.map = map;
	}

	@Override
	protected Long doInBackground() throws Exception {

		Long return_val = System.nanoTime();
		
		System.out.println("Premap phase start...");

		int[] x_offsets = new int[]{1 , 0, -1, 0};
		int[] y_offsets = new int[]{0, 1, 0, -1};

		//Get map dims into local memory.
		int width = map.getWidth();
		int height = map.getHeight();

		//Iterate through all and check for border.
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){

				//If the node is blocked set border.
				MapNode node = map.get(i,  j);
				if(node.isBlocked){
					//Publish pixel.
					publish(new PixelWrite(i, j, Color.GRAY));

					//Check each nearby node and check to see if it should be border.
					for(int n = 0; n < 4; n++){
						int dx = i + x_offsets[n];
						int dy = j + y_offsets[n];



						//Check for valid bounds.
						if(map.isInBounds(dx, dy)){
							
							//Put border.
							MapNode tmp_node = map.get(dx, dy);
							
							//Check for a node that hasn't been blocked.
							if(!tmp_node.isBlocked && !tmp_node.isBorder){
								
								tmp_node.isBorder = true;
								//Write pixel to screen.
								publish(new PixelWrite(dx, dy, Color.ORANGE));
							}
						}
					}
				}
			}
		}
		
		System.out.println("Premap phase complete!");

		// TODO Auto-generated method stub
		return_val = System.nanoTime() - return_val;
		return return_val;
	}

	@Override
	protected void process(List<PixelWrite> v){
		for(PixelWrite px : v)
		{
			//Submit the pixel.
			ImageDispatch.getInstance().submitPixel(px);
		}
	}

}
