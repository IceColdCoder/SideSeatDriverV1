/*
 * Class to make concurrent image drawing not clog the event dispatch thread by drawing the actual image
 * in a different thread and then copying the image into a queue that is visible to the gui.
 * 
 * Scott Franz
 */

package sharedImage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.Semaphore;

import javax.swing.SwingWorker;

import pixelWrite.PixelWrite;
import sUtils.SUtils;

public class SharedCanvas extends SwingWorker<BufferedImage,WritableRaster>{

	//Control.
	private boolean canRun = true;

	//In queue. Blocking.
	private LinkedTransferQueue<PixelWrite> inQueue = new LinkedTransferQueue<PixelWrite>();
	private final Semaphore inQueueLock = new Semaphore(1);

	//Out Queue.
	private ConcurrentLinkedQueue<PixelWrite> outQueue = new ConcurrentLinkedQueue<PixelWrite>();

	//Display Image
	private BufferedImage displayImage;
	private final Semaphore displayLock = new Semaphore(1);



	//Offset.
	private int offset;

	//Interval.
	private int interval;
	
	//Out dimensions.
	private int out_width;
	private int out_height;
	
	//In dimensions.
	private int in_width;
	private int in_height;
	
	//Scale factors.
	private float scaleX = 1;
	private float scaleY = 1;

	/**
	 * Create new shared canvas with new Raster from parameter image. If parameter image is null
	 * then it will create a canvas scaled to the input parameters.
	 * @param img
	 * @param width
	 * @param height
	 * @param offset
	 * @param interval
	 */
	public SharedCanvas(BufferedImage img, int width, int height, int offset, int interval){
		//Set the dimensions of the buffer.
		out_width = width;
		out_height = height;
		
		//Copy the raster of the input image.
		displayImage = img;
		
		//Set the offset interval.
		this.offset = offset;
		this.interval = interval;
		
		//Create internal image.
		GraphicsConfiguration gfx_config = GraphicsEnvironment.getLocalGraphicsEnvironment().
				getDefaultScreenDevice().getDefaultConfiguration();
		
		displayImage = gfx_config.createCompatibleImage(img.getWidth(), img.getHeight(), img.getTransparency());
		
		Graphics2D g2d = (Graphics2D) displayImage.createGraphics();
		
		g2d.drawImage(img, 0, 0, null);
		
		g2d.dispose();
	}

	/**
	 * Submit a PixelWrite object to be written to the screen through the stream.
	 * @param pw
	 */
	public void submitPixel(PixelWrite pw){
		//System.out.println(">>SharedCanvas::submitPizel::lock");
		//Get lock.
		inQueueLock.acquireUninterruptibly();
		
		//Add the PixelWrite to the input queue.
		inQueue.add(pw);
		
		//Release lock.
		inQueueLock.release();
	}
	
	//Streams pixels to another image.
	@Override
	protected BufferedImage doInBackground() throws Exception {

		//Set the initial progress.
		this.setProgress(offset);
		
		//Create the scaled pixel data structure.
		int[] argb = new int[4];
		int[] pos = new int[2];

		//Main loop for stream.
		while(canRun){

			//Lock inQueue.
			inQueueLock.acquireUninterruptibly(1);

			//Get all pixels from the inQueue.
			inQueue.drainTo(outQueue);
			
			inQueueLock.release();
			
			//System.out.print(SUtils.endl() + "SharedCanvas::aquire display lock.");
			
			//Lock the data raster.
			displayLock.acquireUninterruptibly();
			

			
			//Write the new pixels into a raster.
			PixelWrite px = outQueue.poll();
			while(px != null){
				
				//System.out.println(">>SharedCanvas::Writing Pixel");
				
				//Create graphics.
				Graphics2D g2d = displayImage.createGraphics();
				
				//Override pixels in raster.
				px.copyToArray(argb);
				pos[0] = px.posX;
				pos[1] = px.posY;
				this.scale(pos);
				
				//Draw the pixel.
				g2d.setColor(px.color);
				g2d.fill(new Rectangle2D.Float(pos[0], pos[1], 5, 5));
				
				//Dispose graphics.
				g2d.dispose();
				
			
				//Get next PixelWrite.
				px = outQueue.poll();
			}
			

			
			//System.out.print(">>SharedCanvas::release display lock." + SUtils.endl());
			
			//Release display lock.
			displayLock.release();
			
		}

		return null;
	}

	public void setInputDimensions(int w, int h){
		in_width = w;
		in_height = h;
		
		scaleX = (float)out_width / (float)in_width;
		scaleY = (float)out_height / (float)in_height;
	}

	/**
	 * Scale input array to output values.
	 * @param array
	 */
	private void scale(int[] array){
		array[0] = (int) (scaleX * (float)array[0]);
		array[1] = (int) (scaleY * (float)array[1]);
	}
	
	/**
	 * Copy input image to canvas image.
	 * @param img
	 */
	public void copyImageToCanvas(BufferedImage img){
		displayLock.acquireUninterruptibly();
		
		Image scaledImg = img.getScaledInstance(out_width, out_height, Image.SCALE_SMOOTH);
		
		Graphics2D g2d = displayImage.createGraphics();
		g2d.drawImage(scaledImg, 0, 0, null);
		g2d.dispose();
		
		displayLock.release();
	}
	
	public void copyToScreen(BufferedImage img){
		displayLock.acquireUninterruptibly();
		
		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(displayImage, 0, 0, null);
		g2d.dispose();
		
		displayLock.release();
	}

}
