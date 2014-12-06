package main.java.com.modwiz.ld31.leveleditor;

import java.awt.Canvas;
import java.awt.Graphics;

/**
	Provides a view to the level that is being edited.
*/
public class Viewport extends Canvas {

	private Cursor2D cursor;

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
	}
}