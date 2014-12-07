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
		dimensionTo = dimTo;
	}
	
	/**
	 * Represents the functionality when collided with
	 * @param other The {@link com.modwiz.ld31.entities.GameBlock} we are colliding with
	 */
	public void onCollide(GameBlock other) {
		if (other instanceof Player) {
            ((Player) other).jumpDimension(dimensionTo);
		}
	}
	
	public Dimension getDimensionTo() {
		return dimensionTo;
	}


}
