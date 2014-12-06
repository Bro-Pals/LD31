package com.modwiz.ld31.leveleditor;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.ListSelectionEvent;
import javax.swing.ListSelectionListener;
import java.awt.*;
import com.modwiz.ld31.world.*;
import javax.swing.JOptionPane;

/**
	The JFrame that is the level editor, create a new instance to have the level editor be created.
*/
public class LevelEditorMain extends JFrame {
	
	private Viewport viewport;
	private PropertyPanel propertyPanel;
	private JList dimensionList;
	private JList objectList;
	private FileManager fileManager;
	private GameWorld currentLevel;
	
	public LevelEditor() {
		super("Ludum Dare 31 Level Editor");
	}
	
	public void init() {
		addWindowListener(
			new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					if (fileManager.hasCurrent()) {
						if (confirmAction("")) {
							
						}
					}
				}
			}
		);
		currentLevel = null;
		viewport = new Viewport(600, 400);
		fileManager = new FileManager();
		propertyPanel = new PropertyPanel();
		dimensionList = new JList();
		objectList = new JList();
	}
	
	private boolean confirmAction(String message) {
		return JOptionPane.showConfirmDialog(this, message) == JOptionPane.YES_OPTION;
	}

	public void onClose() {
		
	}
	
	public void handleMousePressed(MouseEvent e) {
		
	}
}