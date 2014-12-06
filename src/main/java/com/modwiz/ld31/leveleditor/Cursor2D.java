package com.modwiz.ld31.leveleditor;

import com.modwiz.ld31.entities.GameObject;

public class Cursor2D {
	
	private float x;
	private float y;
	private float offsetFromObjectX;
	private float offsetFromObjectY;
	private GameObject dragging;
	
	public Cursor2D() {
		x = 0;
		offsetFromObjectX = 0;
		y = 0;
		offsetFromObjectY = 0;
		dragging = null;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public boolean isDragging() {
		return dragging!=null;
	}
	
	public void updateDrag() {
		dragging.setX(getX() - offsetFromObjectX);
		dragging.setY(getY() - offsetFromObjectY);
	}
	
	public void startDragging(GameObject dragging) {
		this.dragging = dragging;
		offsetFromObjectX = getX() - dragging.getX();
		offsetFromObjectY = getY() - dragging.getY();
	}
	
	public void endDrag() {
		dragging = null;
	}
	
	public void setCursorLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}
}