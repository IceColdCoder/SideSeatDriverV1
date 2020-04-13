/*
 * Utility to create file filters.
 * 
 * Scott Franz
 */

package fileFilters;

import javax.swing.filechooser.FileFilter;

public class FileFilters {
	
	public static FileFilter getNewImageFilter(){
		return new ImageFilter();
	}

}
