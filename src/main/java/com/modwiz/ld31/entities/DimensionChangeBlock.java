package com.modwiz.ld31.entities;

import java.awt.image.BufferedImage;
import java.awt.*;
import com.modwiz.ld31.world.Dimension;

/**
	A block that lets you change dimensions when you're standing over it
*/
public class DimensionChangeBlock extends GameBlock {
	
	private Dimension dimensionTo;

	public DimensionChangeBlock(Dimension parent, float x, float y, float width, float height, Dimension dimTo) {
		super(parent, x, y, width, height, true);
		setName("Dimension Change Block");
		dimensionTo = dimTo;
	}
	
	public DimensionChangeBlock(Dimension parent, float x, float y, float width, float height, String dimTo) {
		super(parent, x, y, width, height, true);
		setName("Dimension Change Block");
		dimensionTo = null;
	}
	
	/**
	 * Represents the functionality when collided with
	 * @param other The {@link com.modwiz.ld31.entities.GameBlock} we are colliding with
	 */
	public void onCollide(GameBlock other) {
		if (other instanceof Player) {
			((Player)other).setDimensionToGoTo(dimensionTo);
		}
	}
	
	public Dimension getDimensionTo() {
		return dimensionTo;
	}

	public String getJumpingToDimension() {
		return "";
	}
	
	public void setJumpingToDimension(String jumpingTo) {
		
	}
}
