package com.modwiz.ld31.entities;

import java.awt.image.BufferedImage;
import java.awt.*;
import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.world.GameWorld;
import com.modwiz.ld31.entities.*;
import com.modwiz.ld31.Main;

/**
	A block that lets you change dimensions when you're standing over it
*/
public class DimensionChangeBlock extends GameBlock {
	
	private Dimension dimensionTo;
	private String dimString;
	
	public DimensionChangeBlock(Dimension parent, float x, float y, float width, float height, Dimension dimTo) {
		super(parent, x, y, width, height, true);
		setName("Dimension Change Block");
		dimensionTo = dimTo;
		dimString = dimTo.getName();
	}
	
	public DimensionChangeBlock(Dimension parent, float x, float y, float width, float height, String dimTo, String imageString) {
		super(parent, x, y, width, height, true);
		if (imageString!=null && !imageString.equals("null")) {
			setImageString(imageString);
		}
		setName("Dimension Change Block");
		dimensionTo = null;
		dimString = dimTo;
	}
	
	/**
	 * Represents the functionality when collided with
	 * @param other The {@link com.modwiz.ld31.entities.GameBlock} we are colliding with
	 */
	public void onCollide(GameBlock other) {
		if (other instanceof Player) {
            ((Player) other).jumpDimension(getDimensionTo());
		}
	}
	
	public Dimension getDimensionTo() {
		if (dimensionTo == null && dimString!=null) {
			if (Main.getCurrentWorld()!=null) {
				dimensionTo = Main.getCurrentWorld().getDimension(dimString);
			} else {
				return null;
			}
		}
		return dimensionTo;
	}

	public String getJumpingToDimension() {
		if (getDimensionTo()!=null) {
			return getDimensionTo().getName();
		} else if (dimString!=null) {
			return dimString;
		} else {
			return "";
		}
	}
	
	public void setJumpingToDimension(String dimension) {
		this.dimString = dimension;
	}
	
	@Override
	public Object clone() {
		DimensionChangeBlock dcb = new DimensionChangeBlock(null, getX(), getY(), getWidth(), getHeight(), dimString, getImageForString());
		dcb.setName(getName());
		if (getImageForString()!=null) {
			dcb.setImageString(getImageForString());
		}
		return dcb;
	}
}
