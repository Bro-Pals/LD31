package com.modwiz.ld31.world;

import java.awt.*;
import java.util.ArrayList;

/**
	The basic block with width, height, and collision
*/
public class GameBlock extends GameObject {
	
	private float width, height;
	
	public GameBlock(ArrayList<Dimension> parent, float x, float y, float w, float h) {
		super(parent, x, y);
		this.width = w;
		this.height = height;
	}
	
	@Override
	public void update() {
		super.update();
		
		// collision checking
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
