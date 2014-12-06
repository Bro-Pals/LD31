package com.modwiz.ld31.leveleditor;

import com.modwiz.ld31.entities.GameObject;
import com.modwiz.ld31.entities.GameBlock;

public class Cursor2D {
	
	private float x;
	private float y;
	private float offsetFromObjectX;
	private float offsetFromObjectY;
	private GameObject dragging;
	private DragType type;
	
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
		switch(type) {
			case MOVE:
				dragging.setX(getX() - offsetFromObjectX);
				dragging.setY(getY() - offsetFromObjectY);
				break;
			case RESIZE_LEFT:
				
				break;
			case RESIZE_UP:
				
				break;
			case RESIZE_RIGHT:
				
				break;
			case RESIZE_DOWN:
				
				break;
		}
	}
	
	public void startDragging(GameObject dragging) {
		this.dragging = dragging;
		offsetFromObjectX = getX() - dragging.getX();
		offsetFromObjectY = getY() - dragging.getY();
		type = DragType.MOVE;
		//Check to see
		if (dragging instanceof GameBlock) {
			GameBlock gb = (GameBlock)dragging;
			int leftBounds = Math.abs((int)(getX() - gb.getX()));
			int rightBounds = Math.abs((int)(getX() - (gb.getX()+gb.getWidth())));
			
			int topBounds = Math.abs((int)(getY() - gb.getY()));
			int bottomBounds = Math.abs((int)(getY() - (gb.getY()+gb.getHeight())));
			
			if (topBounds < 3) {
				type = DragType.RESIZE_UP;
			} else if (bottomBounds < 3) {
				type = DragType.RESIZE_DOWN;
			} else if (leftBounds < 3) {
				type = DragType.RESIZE_LEFT;
			} else if (rightBounds < 3) {
				type = DragType.RESIZE_RIGHT;
			}
		}
	}
	
	public void endDrag() {
		dragging = null;
	}
	
	public void setCursorLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}
}