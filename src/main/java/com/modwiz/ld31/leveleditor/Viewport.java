package com.modwiz.ld31.leveleditor;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseMotionAdapter;
import com.modwiz.ld31.entities.GameObject;
import com.modwiz.ld31.world.GameWorld;

/**
	Provides a view to the level that is being edited.
*/
public class Viewport extends JComponent {

	private Cursor2D cursor;
	private float camX, camY;
	private final float camSpeed;
	private GameWorld level;
	private GameObject selecting;
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
		selecting = null;
		level = null;
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
	
	public void setSelected(GameObject object) {
		selecting = object;
	}
	
	public void setLevel(GameWorld level) {
		selecting = null;
		this.level = level;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.translate(-(int)camX, -(int)camY);
		if (this.level != null) {
			level.renderDimension(g);
			drawCursor(g);
		} else {
			g.setColor(Color.WHITE);
			g.drawString("No level being edited", 25, 25);
		}
		g.translate((int)camX, (int)camY);
	}
	
	private void drawCursor(Graphics g) {
		int x = (int)(cursor.getX()-camX);
		int y = (int)(cursor.getY()-camY);
		//Cursor width is 15 lets say
		g.setColor(Color.RED);
		g.drawLine(x-15, y, x+15, y);
		g.drawLine(x, y-15, x, y+15);
	}
	
	public void centerCameraOn(GameObject obj) {
		camX = obj.getX() - (getWidth()/2);
		camY = obj.getY() - (getHeight()/2);
	}
	
	public void setupListeners(final LevelEditorMain frame) {
		setFocusable(true);
		
		addKeyListener(new KeyAdapter() {
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
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					cursor.setCursorLocation(e.getX() + camX,e.getY() + camY);
					frame.checkSelection(cursor);
					repaint();
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				cursor.setCursorLocation(e.getX() + camX,e.getY() + camY);
				frame.mouseDragged(e.getX()+camX, e.getY()+camY);
				repaint();
			}
		});
	}
}