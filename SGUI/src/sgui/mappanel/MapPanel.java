/*
 * Class and nested classes to display the draw panel.
 * 
 * Scott Franz
 */
package sgui.mappanel;

import imageDispatch.ImageDispatch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

import sExecutor.SExecutor;
import sPath.SPath;
import sUtils.SUtils;
import sgui.SGUI;
import sgui.SGUI.MapPointSelectMode;
import sharedImage.SharedCanvas;

public class MapPanel extends JPanel{
	
	private DrawPanel drawPanel;
	private SGUI gui;
	
	public MapPanel(Dimension d, SGUI gui){
		this.gui = gui;
		this.setPreferredSize(d);
		
		//Create draw panel.
		drawPanel = new DrawPanel(d);
		this.add(drawPanel);
	}
	
	public void refresh(){
		drawPanel.repaint();
	}
	
	public void startAutoRefresh(){
		drawPanel.startAutoRefresh();
	}
	
	/*
	 * Class that contains draw panel.
	 * Gets painted image from the shared canvas.
	 */
	private class DrawPanel extends JPanel{
		
		private BufferedImage displayImg;
		
		//Vars for sizes.
		private int width;
		private int height;
		
		//Var for autorefresh.
		private AutoRefresh refresher;
		
		private class AutoRefresh implements Runnable{
			
			private DrawPanel panel;
			
			public AutoRefresh(DrawPanel p){
				panel = p;
			}

			@Override
			public void run() {
				panel.repaint(1L);
			}
		}
		
		public DrawPanel(Dimension d){
			//Dimensions.
			this.width = d.width;
			this.height = d.height;
			
			this.setMinimumSize(d);
			this.setMaximumSize(d);
			this.setPreferredSize(d);
			
			//Create Display Image
			displayImg = this.createCompatibleImage(width, height);
			
			//Instantiate Image Dispatch.
			ImageDispatch.instantiate(this.width, this.height, null);
			
			//Add a mouse listener to this.
			this.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					//If current mode is start.
					if(gui.getMapPointSelectMode() == MapPointSelectMode.START){
						SPath path = SPath.getCurrentPath();
						if(path != null){
							//Set the start point.
							path.setStartPoint(e.getPoint(), width, height);
							//Reset select mode.
							gui.resetSelectMode();
						}
					}
					//If current mode is finish.
					else if(gui.getMapPointSelectMode() == MapPointSelectMode.FINISH){
						SPath path = SPath.getCurrentPath();
						if(path != null){
							//Set the finish point.
							path.setFinishPoint(e.getPoint(), width, height);
							//Reset select mode.
							gui.resetSelectMode();
						}
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});

		}
		
		public void startAutoRefresh(){
			refresher = new AutoRefresh(this);
			SExecutor.scheduleAtFixedRate(refresher);
		}
		
		private BufferedImage createCompatibleImage(int width, int height){
			//Get system graphics settings.
			GraphicsConfiguration gfx_config = GraphicsEnvironment.getLocalGraphicsEnvironment().
					getDefaultScreenDevice().getDefaultConfiguration();
			
			//Create a new buffered image.
			BufferedImage new_image = gfx_config.createCompatibleImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
			
			//Create graphics.
			Graphics2D g2d = new_image.createGraphics();
			
			//Draw graphics.
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, width, height);
			
			//Dispose graphics object.
			g2d.dispose();
			
			return new_image;
		}
		
		/**
		 * Method to create a blank image to show on screen if no raster data.
		 * @param width
		 * @param height
		 * @return new white image.
		 */
		private BufferedImage createBlankImage(int width, int height){
			BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = img.createGraphics();
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, width, height);
			g2.dispose();
			return img;
		}
		
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			//Draw image.
			this.draw((Graphics2D)g.create());
		}
		
		/**
		 * Draw the image to the graphics instance.
		 * @param g
		 */
		private synchronized void draw(Graphics2D g){
			//System.out.print(SUtils.endl() + ">>GUI::draw start.");
			//Set rendering hints.
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			//Copy the raster from the current job.
			ImageDispatch.getInstance().getScreenImage(displayImg);
			
			//Draw the image to the screen.
			g.drawImage(displayImg, 0, 0, width, height, null);
			
			//Dispose graphics.
			g.dispose();
			
			//System.out.print(">>GUI::draw finish." + SUtils.endl());
		}
	}

}
