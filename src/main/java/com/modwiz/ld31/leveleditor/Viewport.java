package com.modwiz.ld31.leveleditor;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyAdapter;
import com.modwiz.ld31.entities.GameObject;
import com.modwiz.ld31.world.GameWorld;

/**
	Provides a view to the level that is being edited.
*/
public class Viewport extends Canvas {

	private Cursor2D cursor;
	private float camX, camY;
	private final float camSpeed;
	private GameWorld level;
	private int gridSpacing;
	private boolean gridVisible;
	
	public Viewport(int viewPortWidth, int viewPortHeight) {
		cursor = new Cursor2D();
		camX = 0;
		camY = 0;
		camSpeed = 40;
		setPreferredSize(new java.awt.Dimension(viewPortWidth, viewPortHeight));
		setSize(viewPortWidth, viewPortHeight);
		gridVisible = false;
		gridSpacing = 25;
	}
	
	public int getGridSpacing() {
		return gridSpacing;
	}
	
	public boolean getGridVisible() {
		return gridVisible;
	}
	
	public void setGridVisible(boolean gridVisible) {
		this.gridVisible = gridVisible;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.translate(-(int)camX, -(int)camY);
		if (this.level != null) {
			level.renderDimension(g);
		}
		g.translate((int)camX, (int)camY);
	}
	
	public void setLevel(GameWorld level) {
		this.level = level;
	}
	
	public void setupListeners(final LevelEditorMain frame) {
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				switch(e.getKeyCode()) {
					case KeyEvent.VK_W:
						camY -= camSpeed;
						break;
					case KeyEvent.VK_A:
						camX -= camSpeed;
						break;
					case KeyEvent.VK_S:
						camY += camSpeed;
						break;
					case KeyEvent.VK_D:
						camX += camSpeed;
						break;
				}
			}
		});
		
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					cursor.setCursorLocation(e.getX() + camX,e.getY() + camY);
					frame.checkSelection(cursor);
				}
			}
		});
	}
}