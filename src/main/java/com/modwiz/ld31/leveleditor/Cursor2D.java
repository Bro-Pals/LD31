package com.modwiz.ld31.leveleditor;

import com.modwiz.ld31.entities.GameObject;
import com.modwiz.ld31.entities.GameBlock;

public class Cursor2D {
	
	private float x;
	private float y;
	private float initialLeftX;
	private float initialRightX;
	private float initialTopY;
	private float initialBottomY;
	private float offsetFromObjectX;
	private float offsetFromObjectY;
	private GameObject dragging;
	private DragType type;
	
	public Cursor2D() {
		x = 0;
		initialLeftX = 0;
		initialRightX = 0;
		initialTopY = 0;
		initialBottomY = 0;
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
				if (dragging instanceof GameBlock) {
					GameBlock gb = (GameBlock)dragging;
					gb.setX(getX());
					gb.setWidth(initialRightX - getX());
				}
				break;
			case RESIZE_UP:
				if (dragging instanceof GameBlock) {
					GameBlock gb = (GameBlock)dragging;
					gb.setY(getY());
					gb.setHeight(initialBottomY - getY());
				}
				break;
			case RESIZE_RIGHT:
				if (dragging instanceof GameBlock) {
					GameBlock gb = (GameBlock)dragging;
					gb.setWidth(-initialLeftX + getX());
				}
				break;
			case RESIZE_DOWN:
				if (dragging instanceof GameBlock) {
					GameBlock gb = (GameBlock)dragging;
					gb.setHeight(-initialTopY + getY());
				}
				break;
		}
	}
	
	public void startDragging(GameObject dragging) {
		this.dragging = dragging;
		if (dragging instanceof GameBlock) {
			GameBlock gb = (GameBlock)dragging;
			initialLeftX = gb.getX();
			initialRightX = gb.getX()+gb.getWidth();
			initialTopY = gb.getY();
			initialBottomY = gb.getY()+gb.getHeight();
			offsetFromObjectX = getX() - gb.getX();
			offsetFromObjectY = getY() - gb.getY();
		}
		type = DragType.MOVE;
		//Check to see
		if (dragging instanceof GameBlock) {
			GameBlock gb = (GameBlock)dragging;
			int leftBounds = Math.abs((int)(getX() - gb.getX()));
			int rightBounds = Math.abs((int)(getX() - (gb.getX()+gb.getWidth())));
			
			int topBounds = Math.abs((int)(getY() - gb.getY()));
			int bottomBounds = Math.abs((int)(getY() - (gb.getY()+gb.getHeight())));
			
			if (topBounds < 6) {
				type = DragType.RESIZE_UP;
			} else if (bottomBounds < 6) {
				type = DragType.RESIZE_DOWN;
			} else if (leftBounds < 6) {
				type = DragType.RESIZE_LEFT;
			} else if (rightBounds < 6) {
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