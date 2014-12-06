package com.modwiz.ld31.leveleditor;

import com.modwiz.ld31.entities.GameObject;

import javax.swing.*;

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
	
	public void loadEnity(GameObject entity) {
		this.editing = entity;
		//Create fields
	}
	
	public void clear() {
		editing = null;
	}
}