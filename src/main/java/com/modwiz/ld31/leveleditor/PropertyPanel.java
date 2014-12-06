package com.modwiz.ld31.leveleditor;

import javax.swing.JPanel;
import javax.swing.JViewport;

/**
	Edit the properties of an object from the property panel.
	Loading an object changes what the properties are, depending on 
	the type of the game entity.
*/
public class PropertyPanel extends JPanel {
	
	private JViewport view;
	private JTextField[] fields;
	private JPanel fieldsPanel;
	private GameObject editing;
	
	public PropertyPanel() {
		editing = null;
		
	}
	
	public void loadEnity(GameEntity entity) {
		this.editing = entity;
		//Create fields
	}
	
	public void clear() {
		editing = null;
	}
}