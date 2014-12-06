package com.modwiz.ld31.world;

/**
	The basic block with width, height, and collision
*/
public class GameBlock extends GameObject {
	
	private float width, height;
	
	public GameBlock(float x, float y, float w, float h) {
		super(x, y);
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
