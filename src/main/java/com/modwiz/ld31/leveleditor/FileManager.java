package com.modwiz.ld31.leveleditor;

import java.io.File;
import com.modwiz.ld31.world.GameWorld;

public class FileManager {
	
	private File current;
	
	public FileManager() {
		current = null;
	}
	
	public void save(GameWorld world) {
		if (current!=null) {
			//Save the file
		}
	}
	
	public boolean hasCurrent() {
		return current != null;
	}
	
	public void clear() {
		current = null;
	}
	
	public void saveAs(File file, GameWorld world) {
		current = file;
	}
	
	public void open(File file) {
		current = file;
	}
}