package main.java.com.modwiz.ld31.leveleditor;

public class Cursor2D {
	
	private float x;
	private float y;
	
	public Cursor2D() {
		x = 0;
		y = 0;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setCursorLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}
}