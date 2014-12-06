package com.modwiz.ld31.leveleditor;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import javax.swing.JList;

/**
	The JFrame that is the level editor, create a new instance to have the level editor be created.
*/
public class LevelEditorMain extends JFrame {
	
	private Viewport viewport;
	private PropertyPanel propertyPanel;
	private JList dimensionList;
	private JList objectList;
	private FileManager fileManager;
	
	public LevelEditor() {
		super("Ludum Dare 31 Level Editor");
	}
	
	public void init() {
		addWindowListener(
			new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					
				}
			}
		);
		viewport = new Viewport(600, 400);
		fileManager = new FileManager();
		propertyPanel = new PropertyPanel();
		dimensionList = new JList();
		objectList = new JList();
	}

	public void onClose() {
		
	}
	
	public void handleMousePressed(MouseEvent e) {
		
	}
}