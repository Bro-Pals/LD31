package com.modwiz.ld31.leveleditor;

import java.io.File;

public class FileManager {
	
	private File current;
	
	public FileManager() {
		current = null;
	}
	
	public void save() {
		if (current!=null) {
			//Save the file
		}
	}
	
	public boolean hasCurrent() {
		return current != null;
	}
	
	public void saveAs(File file) {
		current = file;
	}
	
	public void open(File file) {
		current = file;
	}
}