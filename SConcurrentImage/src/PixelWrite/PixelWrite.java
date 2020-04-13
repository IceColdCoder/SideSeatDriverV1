/*
 * Class that contains pixel write request.
 * 
 * Scott Franz
 */

package pixelWrite;

import java.awt.Color;

public class PixelWrite {
	
	public int posX;
	public int posY;
	public int argb;
	public Color color;
	
	public PixelWrite(int posX, int posY, int argb){
		this.posX = posX;
		this.posY = posY;
		this.argb = argb;
		this.color = new Color(argb);
	}
	
	public PixelWrite(int posX, int posY, Color c){
		this.posX = posX;
		this.posY = posY;
		this.argb = c.getRGB();
		this.color = c;
	}
	
	/**
	 * Returns a hard copy of this PixelWrite.
	 */
	public PixelWrite clone(){
		PixelWrite px = new PixelWrite(this.posX, this.posY, this.argb);
		return px;
	}
	
	/**
	 * Pass array into this method to perform bit shifts and copy value to array.
	 * @param inARGB
	 */
	public void copyToArray(int[] inARGB){
		inARGB[0] = 0xFF & (argb >> 24);
		inARGB[1] = 0xFF & (argb >> 16);
		inARGB[2] = 0xFF & (argb >> 8);
		inARGB[3] = 0xFF & (argb >> 0);
	}

}
