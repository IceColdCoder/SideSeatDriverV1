/*
 * ImageFilter object. Code taken from java tutorials.
 * 
 * Scott Franz
 */

package fileFilters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import fileUtils.FileUtils;

public class ImageFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()){
			return true;
		}
		
		String extension = FileUtils.getExtension(f);
	    if (extension != null) {
	        if (extension.equals(FileUtils.tiff) ||
	            extension.equals(FileUtils.tif) ||
	            extension.equals(FileUtils.gif) ||
	            extension.equals(FileUtils.jpeg) ||
	            extension.equals(FileUtils.jpg) ||
	            extension.equals(FileUtils.png)) {
	                return true;
	        } else {
	            return false;
	        }
	    }
		return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Image file types.";
	}
	
}
