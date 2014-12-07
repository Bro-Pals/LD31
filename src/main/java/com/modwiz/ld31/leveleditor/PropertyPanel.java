package com.modwiz.ld31.leveleditor;

import com.modwiz.ld31.entities.*;

import com.modwiz.ld31.utils.assets.CachedLoader;
import com.modwiz.ld31.utils.assets.AssetLoader;
import com.modwiz.ld31.utils.assets.AssetRegistry;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
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
	private JButton updateAll;
	private CachedLoader cachedLoader;
	
	public PropertyPanel(final LevelEditorMain lem) {
		cachedLoader = (CachedLoader)AssetLoader.getAssetLoader();
		this.lem = lem;
		editing = null;
		setLayout(new GridLayout(7, 2, 10, 10));
		checkboxes = new JCheckBox[2];
		fields = new JTextField[5];
		
		fields[POSITION] = new JTextField(6);
		fields[POSITION].addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { setPosition();  } });
		fields[SIZE] = new JTextField(6);
		fields[SIZE].addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { setSize();  } });
		fields[NAME] = new JTextField(6);
		fields[NAME].addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { setName();  } });
		checkboxes[STATIC] = new JCheckBox("Static");
		checkboxes[STATIC].addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { setStatic();  } });
		checkboxes[CAN_COLLIDE] = new JCheckBox("Can collide");
		checkboxes[CAN_COLLIDE].addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { setCanCollide();  } });
		fields[IMAGE] = new JTextField(6);
		fields[IMAGE].addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { setImage();  } });
		fields[ANIMATION] = new JTextField(6);
		fields[ANIMATION].addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { setAnimation();  } });
		imageBrowse = new JButton("Browse");
		animationBrowse = new JButton("Browse");
		updateAll = new JButton("Update");
		
		imageBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] keySet = AssetRegistry.bufferedImageRegistry.getAssetKeys();
				final JDialog log = new JDialog(lem);
				log.setVisible(true);
				log.pack();
				log.setLocationRelativeTo(lem);
				log.setTitle("Chooser an image");
				final JList<String> list = new JList<String>(keySet);
				final JButton button = new JButton("Accept");
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String str = list.getSelectedValue();
						log.dispose();
						if (str!=null) {
							fields[IMAGE].setText(str);
							setImage();
						}
					}
				});
				log.add(list);
				log.add(button);
			}
		});
	}
	
	private void setPosition() {
		try {
			String[] pos = fields[POSITION].getText().split(Pattern.quote(","));
			editing.setX(Float.parseFloat(pos[0]));
			editing.setY(Float.parseFloat(pos[1]));
		} catch(Exception e) {
			fields[POSITION].setText("" + editing.getX() + "," + editing.getY());
		}
		lem.repaintViewport();
	}
	
	private void setSize() {
		try {
			String[] size = fields[SIZE].getText().split(Pattern.quote(","));
			((GameBlock)editing).setWidth(Float.parseFloat(size[0]));
			((GameBlock)editing).setHeight(Float.parseFloat(size[1]));
		} catch(Exception e) {
			fields[SIZE].setText("" + ((GameBlock)editing).getWidth() + "," + ((GameBlock)editing).getHeight());
		}
		lem.repaintViewport();
	}
	
	private void setName() {
		editing.setName(fields[NAME].getText());
		lem.repaintViewport();
	}
	
	private void setImage() {
		((GameBlock)editing).setImage(AssetRegistry.bufferedImageRegistry.getAsset(fields[IMAGE].getText()).get());
		lem.repaintViewport();
	}
	
	private void setAnimation() {
		
		lem.repaintViewport();
	}
	
	private void setStatic() {
		((GameBlock)editing).setStaticBlock(checkboxes[STATIC].isSelected());
		lem.repaintViewport();
	}
	
	private void setCanCollide() {
		((GameBlock)editing).setCanCollide(checkboxes[CAN_COLLIDE].isSelected());
		lem.repaintViewport();
	}
	
	public void loadObject(GameObject entity) {
		this.editing = entity;
		//Add more fields to this
		if (entity instanceof GameBlock) {
			addName();
			addPosition();
			addSize();
			addImage();
			lem.repaintViewport();
		} else if (entity instanceof Enemy) {
			addName();
			addPosition();
			addSize();
			addAnimation();
			lem.repaintViewport();
		}
		lem.revalidate();
	}
	
	private void addPosition() {
		add(new JLabel("Position (x, y)"));
		add(fields[POSITION]);
		fields[POSITION].setText("" + editing.getX() + "," + editing.getY());
	}
	
	public void updatePositionField() {
		fields[POSITION].setText("" + editing.getX() + "," + editing.getY());
	}
	
	private void addSize() {
		add(new JLabel("Size (width, height)"));
		add(fields[SIZE]);
		fields[SIZE].setText("" + ((GameBlock)editing).getWidth() + "," + ((GameBlock)editing).getHeight());
	}
	
	public void updateSizeField() {
		fields[SIZE].setText("" + ((GameBlock)editing).getWidth() + "," + ((GameBlock)editing).getHeight());
	}
	
	public void updateFields() {
		updatePositionField();
		updateSizeField();
	}
	
	private void addName() {
		add(new JLabel("Name"));
		add(fields[NAME]);
		fields[NAME].setText(editing.getName());
	}
	
	private void addStatic() {
		add(checkboxes[STATIC]);
		checkboxes[STATIC].setSelected(((GameBlock)editing).isStaticBlock());
	}
	
	private void addCanCollide() {
		add(checkboxes[CAN_COLLIDE]);
		checkboxes[CAN_COLLIDE].setSelected(((GameBlock)editing).getCanCollide());
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