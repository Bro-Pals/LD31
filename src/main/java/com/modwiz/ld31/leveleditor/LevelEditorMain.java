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
	private FileManager fileManager;
	private JList dimList;
	private JList objectLib;
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
	private GameWorld currentLevel;
	
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
	}
	
	private boolean confirmAction(String message) {
		return JOptionPane.showConfirmDialog(this, message) == JOptionPane.YES_OPTION;
	}
	
	public void checkSelection(Cursor2D cursor) {
		//Check to see if the cursor is selecting something
	}

	private void makeFileMenu() {
		newButton = new JMenuItem("New Level");
		if (fileManager.hasCurrent()) {
			if (confirmAction("Save current level before overwriting with a new level?")) {
				//Make a new level
				fileManager.save(currentLevel);
				fileManager.clear();
				
			}
		}
	}
	
	private void makeNewButton() {
		
	}
	
	private void makeSaveButton() {
		
	}
	
	private void makeSaveAsButton() {
		
	}
	
	private void makeOpenButton() {
		
	}
	
	private void makeSnapToGrid() {
		
	}
	
	private void makeSnapToObjects() {
		
	}
	
	private void makeShowGrid() {
		
	}
	
	private void makeDimList() {
		
	}
	
	private void makeObjectLib() {
		
	}
	
	private void makeAddDim() {
		
	}
	
	private void makeRemoveDim() {
		
	}
	
	private void makeAddObject() {
		
	}
	
	private void makeRemoveObject() {
		
	}
	
	private void makeEditorMenu() {
		
	}
	
	private void choseDimension() {
	
	}
	
	private void addToWorldFromObjectLib() {
	
	}
	
	private void newLevel() {
	
	}
	
	private void saveLevel() {
	
	}
	
	private void saveLevelAs() {
	
	}
	
	private void openLevel() {
	
	}
	
	private void selectGameObject(GameObject selecting) {
	
	}
	
	private void showGrid() {
	
	}
	
	private void hideGrid() {
	
	}
	
	private void startSnappingToGrid() {
	
	}
	
	private void stopSnappingToGrid() {
	
	}
	
	private void startSnappingToObjects() {
	
	}
	
	private void stopSnappingToObjects() {
	
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