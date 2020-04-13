package imageDispatch;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Vector;





import pixelWrite.PixelWrite;
import sExecutor.SExecutor;
import sharedImage.SharedCanvas;

public class ImageDispatch {
	
	//Number of parallel SharedCanvas.
	private final int SIZE = 2;
	
	//Array for parallel SharedCanvas.
	private SharedCanvas[] canvasArray = new SharedCanvas[SIZE];
	
	//Display offset.
	private int offset = 2;
	
	//Interval for reads.
	private int interval = 1;
	
	//Current index for which canvas to draw from first.
	private int currentIndex = -1; //Must start at -1;
	
	//Private instance of image dispatch.
	private static ImageDispatch imgDisp = null;
	
	//Variables for output scale.
	private int width;
	private int height;
	
	//Vars for input scale.
	private int inputWidth;
	private int inputHeight;
	
	private ImageDispatch(int w, int h, BufferedImage coreImg){
		this.width = w;
		this.height = h;
		
		BufferedImage coreImage;
		//Check to see if there is an initial display image of some kind.
		if(coreImg == null){
			//If no initial display image, create blank image.
			coreImage = ImageDispatch.getBlankImage(w, h);
		}
		else{
			//Otherwise set core image to input.
			coreImage = coreImg;
		}
		
		/*Loop through all images.*/
		for(int i = 0; i < SIZE; i++){
			/*Create new shared canvas.*/
			canvasArray[i] = new SharedCanvas(coreImage, this.width, this.height, offset * i, interval);
			
			//Insert the canvas into the thread executor.
			SExecutor.scheduleAtFixedRate(canvasArray[i]);
		}
	}
	
	/**
	 * Create new instance of ImageDispatch. If coreImg == null then a blank image with input width and height will be
	 * created.
	 * @param w
	 * @param h
	 * @param coreImg
	 */
	public static void instantiate(int w, int h, BufferedImage coreImg){
		imgDisp = new ImageDispatch(w, h, coreImg);
	}
	
	/**
	 * Create a new white image with width w and height h.
	 * @param w
	 * @param h
	 * @return New blank image.
	 */
	public static BufferedImage getBlankImage(int w, int h){
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, w, h);
		return img;
	}
	
	/**
	 * Get the current index for the canvas.
	 * @return
	 */
	private int getSharedCanvasIndex(){
		currentIndex++;
		if(currentIndex % SIZE == 0){
			currentIndex = 0;
		}
		return currentIndex;
	}
	
	/**
	 * Return the current instance of the ImageDispatch.
	 * @return
	 */
	public static ImageDispatch getInstance(){
		return imgDisp;
	}
	
	/**
	 * Set the core image of all SharedCanvas instances to this image.
	 * @param img
	 */
	public void setCoreImage(BufferedImage img){
		this.width = img.getWidth();
		this.height = img.getHeight();
		
		//Copy the raster into all shared canvas.
		for(int i = 0; i < SIZE; i++){
			canvasArray[i].copyImageToCanvas(img);
		}
	}
	
	/**
	 * Submit pixel to be written to all SharedCanvases.
	 * @param pw
	 */
	public void submitPixel(PixelWrite pw){
		//System.out.println(">>ImageDispatch::submitPixel");
		//Insert the pixels into the stream.
		for(int i = 0; i < SIZE; i++){
			canvasArray[i].submitPixel(pw.clone()); //Clone pixel to assure no memory overlap. Creates new object.
		}
	}
	
	public void getScreenImage(BufferedImage img){
		canvasArray[this.getSharedCanvasIndex()].copyToScreen(img);
	}
	
	/**
	 * Set the input dims for every canvas.
	 * @param w
	 * @param h
	 */
	public void setInputDims(int w, int h){
		for(int i = 0; i < SIZE; i++){
			canvasArray[i].setInputDimensions(w, h);
		}
	}

}
