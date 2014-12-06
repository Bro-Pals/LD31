package com.modwiz.ld31.world;

import java.awt.*;
import java.util.List;
import java.util.Iterator;

/**
	The basic block with width, height, and collision
*/
public class GameBlock extends GameObject {
	
	private float width, height;
	
	public GameBlock(List<Dimension> parent, float x, float y, float w, float h) {
		super(parent, x, y);
		this.width = w;
		this.height = height;
	}
	
	@Override
	public void update() {
		super.update();
		
		// collision checking
		Iterator<GameObject> stuff = getParent().getObjects().iterator();
		while(stuff.hasNext()) {
			GameObject obj = stuff.next();
			if (obj instanceof GameBlock && obj != this) {
				GameBlock bl = (GameBlock) obj; // we're working with another block
				
				float smallestMaxX = bl.getX() + bl.getWidth() < this.getX() + this.getWidth() ?
					bl.getX() + bl.getWidth() : this.getX() + this.getWidth();
				float largestMinX = bl.getX() > this.getX() ? bl.getX() : this.getX();
				
				float smallestMaxY = bl.getY() + bl.getHeight() < this.getY() + this.getHeight() ?
					bl.getY() + bl.getHeight() : this.getY() + this.getHeight();
				float largestMinY = bl.getY() > this.getY() ? bl.getY() : this.getY();
				
				float penX = largestMinX - smallestMaxX;
				float penY = largestMinY - smallestMaxY;
				
				if (penX < 0 && penY < 0) {
					if (Math.abs(penY) < Math.abs(penX)) {
						if (this.getY() < bl.getY()) {
							setY(bl.getY() - getHeight());
						} else {
							setY(bl.getY() + bl.getHeight());
						}
						getVelocity().setY(0);
					} else {
						if (this.getX() < bl.getX()) {
							getX(bl.getX() - getWidth());
						} else {
							getX(bl.getX() + bl.getWidth());
						}
						getVelocity().setX(0);
					}
				}
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect((int)getX(), (int)getY(), (int)width, (int)height);
	}
	
	public void setHeight(float h) {
		this.height = h;
	}
	
	public void setWidth(float w) {
		this.width = w;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getWidth() {
		return width;
	}
	
	public void setSize(float w, float h) {
		this.width = w;
		this.height = h;
	}
}
