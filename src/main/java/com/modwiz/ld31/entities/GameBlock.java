package com.modwiz.ld31.entities;

import java.awt.*;
import com.modwiz.ld31.world.Dimension;

/**
	The basic block with width, height, and collision
*/
public class GameBlock extends GameObject {
	
	private float width, height;
	
	public GameBlock(Dimension parent, float x, float y, float w, float h) {
		super(parent, x, y);
		this.width = w;
		this.height = height;
	}
	
	public void onCollide(GameBlock other) {
		// what happens when there is a collision with the other block
		
		System.out.println("I have ran into another blcok!");
	}
	
	@Override
	public void update() {
		super.update();
		
		// collision checking
		for (GameObject obj : getParent().getObjects()) {
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
						getVelocity().set(1, 0);
					} else {
						if (this.getX() < bl.getX()) {
							setX(bl.getX() - getWidth());
						} else {
							setX(bl.getX() + bl.getWidth());
						}
						getVelocity().set(0, 0);
					}
					onCollide(bl); // collide with other blocks
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
