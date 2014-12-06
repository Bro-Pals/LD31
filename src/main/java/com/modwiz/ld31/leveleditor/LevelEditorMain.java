package com.modwiz.ld31.leveleditor;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

/**
	The JFrame that is the level editor, create a new instance to have the level editor be created.
*/
public class LevelEditor extends JFrame {
	
	private Viewport viewport;
	private PropertyPanel propertyPanel;
	private DimensionsPanel dimensionsPanel;
	private ObjectLibraryPanel objectLibraryPanel;
	
	public LevelEditor() {
		
	}

	public void onClose() {
		
	}
	
	public void handleMousePressed(MouseEvent e) {
		
	}
}