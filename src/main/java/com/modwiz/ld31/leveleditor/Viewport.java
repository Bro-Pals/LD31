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
import com.modwiz.ld31.entities.GameBlock;
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
	private final int minX = -4000;
	private final int maxX = 4000;
	private final int minY = -4000;
	private final int maxY = 4000;
	private boolean camMoving;
	private float camStartX;
	private float camStartY;
	private LevelEditorMain main;
	
	public LevelEditorMain getMain() {
		return main;
	}
	
	public Viewport(LevelEditorMain main, int viewPortWidth, int viewPortHeight) {
		this.main = main;
		cursor = new Cursor2D(this);
		camX = 0;
		camY = 0;
		camStartX = 0;
		camStartY = 0;
		camSpeed = 200;
		camMoving = false;
		setPreferredSize(new java.awt.Dimension(viewPortWidth, viewPortHeight));
		setSize(viewPortWidth, viewPortHeight);
		gridVisible = false;
		selecting = null;
		level = null;
		gridSpacing = 50;
	}
	
	public int getGridSpacing() {
		return gridSpacing;
	}
	
	public boolean getGridVisible() {
		return gridVisible;
	}
	
	public boolean camIsMoving() {
		return camMoving;
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
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		if (this.level != null) {
			level.renderDimension(g, camX, camY);
			drawCursor(g);
			if (gridVisible) {
				drawGrid(g, camX, camY);
			}
			/* Draw a box around the selected object */
			if (selecting!=null) {
				g.setColor(Color.BLUE);
				if (selecting instanceof GameBlock) {
					//When the selected object has size
					//The highlight size is 8
					g.drawRect(
						(int)(selecting.getX()-camX)-4,
						(int)(selecting.getY()-camY)-4,
						(int)((GameBlock)selecting).getWidth()+8,
						(int)((GameBlock)selecting).getHeight()+8
					);
				} else {
					//When the selected object has no size
					g.drawOval( (int)(selecting.getX()-camX)-7, (int)(selecting.getY()-camY)-7, 14, 14 );
				}
			}
		} else {
			g.setColor(Color.RED);
			g.drawString("No level being edited", 25, 25);
		}
	}
	
	private void drawGrid(Graphics g, float camX, float camY) {
		g.setColor(Color.BLACK);
		for (int x=minX; x<maxX; x += gridSpacing) {
			int xPos = (int)(x - camX);
			g.drawLine(
				xPos,
				minY,
				xPos,
				maxX
			);
		}
		for (int y=minY; y<maxY; y += gridSpacing) {
			int yPos = (int)(y - camY);
			g.drawLine(
				minX,
				yPos,
				maxX,
				yPos
			);
		}
	}
	
	private void drawCursor(Graphics g) {
		int x = (int)(cursor.getX()-camX);
		int y = (int)(cursor.getY()-camY);
		//Cursor width is 15 lets say
		g.setColor(Color.RED);
		g.drawLine(x-15, y, x+15, y);
		g.drawLine(x, y-15, x, y+15);
	}
		
	public void setupListeners(final LevelEditorMain frame) {
		setFocusable(true);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
					case KeyEvent.VK_W:
						camY -= camSpeed;
						repaint();
						break;
					case KeyEvent.VK_A:
						camX -= camSpeed;
						repaint();
						break;
					case KeyEvent.VK_S:
						camY += camSpeed;
						repaint();
						break;
					case KeyEvent.VK_D:
						camX += camSpeed;
						repaint();
						break;
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				cursor.setCursorLocation(e.getX()+camX,e.getY()+camY);
				if (e.getButton() == MouseEvent.BUTTON1) {
					frame.checkSelection(cursor);
					repaint();
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					camMoving = true;
					camStartX = cursor.getX();
					camStartY = cursor.getY();
					repaint();
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				cursor.endDrag();
				if (e.getButton() == MouseEvent.BUTTON3) {
					camMoving = false;
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				cursor.setCursorLocation(e.getX()+camX,e.getY()+ camY);
				frame.mouseDragged(cursor);
				repaint();
			}
		});
	}
	
	public Cursor2D get2DCursor() {
		return cursor;
	}
}