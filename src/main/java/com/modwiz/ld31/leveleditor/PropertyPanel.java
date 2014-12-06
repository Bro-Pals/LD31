package com.modwiz.ld31.leveleditor;

import com.modwiz.ld31.entities.GameObject;
import com.modwiz.ld31.entities.GameBlock;

import javax.swing.*;
import java.awt.GridLayout;

/**
	Edit the properties of an object from the property panel.
	Loading an object changes what the properties are, depending on 
	the type of the game entity.
*/
public class PropertyPanel extends JPanel {
	
	/* Indices for common text fields */
	private final int
		POSITION = 0,
		SIZE = 1,
		NAME = 2,
		STATIC = 0,
		CAN_COLLIDE = 1,
		IMAGE = 3,
		ANIMATION = 4
	;
	
	private JViewport view;
	private JTextField[] fields;
	private JCheckBox[] checkboxes;
	private JPanel fieldsPanel;
	private GameObject editing;
	private JButton imageBrowse, animationBrowse;
	private LevelEditorMain lem;
	
	public PropertyPanel(LevelEditorMain lem) {
		this.lem = lem;
		editing = null;
		setLayout(new GridLayout(6, 2, 10, 10));
		checkboxes = new JCheckBox[2];
		fields = new JTextField[5];
		
		fields[POSITION] = new JTextField(10);
		fields[SIZE] = new JTextField(10);
		fields[NAME] = new JTextField(10);
		checkboxes[STATIC] = new JCheckBox("Static");
		checkboxes[CAN_COLLIDE] = new JCheckBox("Can collide");
		fields[IMAGE] = new JTextField(10);
		fields[ANIMATION] = new JTextField(10);
		imageBrowse = new JButton("Browse");
		animationBrowse = new JButton("Browse");
	}
	
	public void loadObject(GameObject entity) {
		this.editing = entity;
		//Add more fields to this
		if (entity instanceof GameBlock) {
			addName();
			addPosition();
			addSize();
			lem.repaintViewport();
		}
		revalidate();
		repaint();
	}
	
	private void addPosition() {
		add(new JLabel("Position (x, y)"));
		add(fields[POSITION]);
	}
	
	private void addSize() {
		add(new JLabel("Size (width, height)"));
		add(fields[SIZE]);
	}
	
	private void addName() {
		add(new JLabel("Name"));
		add(fields[NAME]);
	}
	
	private void addStatic() {
		add(checkboxes[STATIC]);
	}
	
	private void addCanCollide() {
		add(checkboxes[CAN_COLLIDE]);
	}
	
	private void addImage() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.add(new JLabel("Image"));
		p.add(imageBrowse);
		add(p);
		add(fields[IMAGE]);
	}
	
	private void addAnimation() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.add(new JLabel("Animation"));
		p.add(animationBrowse);
		add(p);
		add(fields[ANIMATION]);
	}
	
	public void clear() {
		editing = null;
		removeAll();
		revalidate();
	}
}