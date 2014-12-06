package com.modwiz.ld31.leveleditor;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import com.modwiz.ld31.world.*;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;

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
	private JButton addObject;
	private JButton removeObject;
	private JButton dimAdd;
	private JButton dimRemove;
	private JMenu fileMenu;
	private JMenu editorMenu;
	private JMenuItem newButton;
	private JMenuItem saveButton;
	private JMenuItem saveAsButton;
	private JMenuItem openButton;
	private JCheckBoxMenuItem snapToGrid;
	private JCheckBoxMenuItem snapToObjects;
	private JCheckBoxMenuItem gridVisible;
	private GameObject selecting;
	
	public LevelEditorMain() {
		super("Ludum Dare 31 Level Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 800);
		setVisible(true);
	}
	
	public void init() {
		addWindowListener(
			new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					onClose();
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
	
	public void checkSelection(Cursor2D cursor) {
		//Check to see if the cursor is selecting something
	}

	public void onClose() {
		if (fileManager.hasCurrent()) {
			if (confirmAction("Save current work?")) {
				fileManager.save(currentLevel);
				dispose();
			}
		}
	}
}