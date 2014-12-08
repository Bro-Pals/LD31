package com.modwiz.ld31.entities;

import java.awt.image.BufferedImage;
import java.awt.*;
import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.world.GameWorld;
import com.modwiz.ld31.entities.*;
import com.modwiz.ld31.Main;
import com.modwiz.ld31.world.Room;

/**
	A block that lets you change dimensions when you're standing over it
*/
public class DimensionChangeBlock extends GameBlock {
	
	private Dimension dimensionTo;
	private String dimString;
	private Object secret;
	
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
            //(Player) other).jumpDimension(getDimensionTo());
			//other.getParent().getRoom().removeObject(this);
			if (secret != null && secret instanceof Room) {
				other.getParent().getRoom().removeObject(other);
				((Player) other).getVelocity().set(0, 0);
				((Player) other).getVelocity().set(1, 0);
				other.getParent().setRoom((Room)secret);
				other.getParent().getRoom().addObject(other);
			} else {
				Room room = new Room();
				//room.addObject(this);
				room.addObject(new GameBlock(getParent(), other.getX() - 100, other.getY()+50, 400, 50, true, "img/dirt.png"));
				other.getParent().getRoom().removeObject(other);
				room.addObject(other);
				((Player) other).getVelocity().set(0, 0);
				((Player) other).getVelocity().set(1, 0);
				DimensionChangeBlock dimensionChangeBlock = new DimensionChangeBlock(getParent(), other.getX()+350, other.getY()-100, 50,200,"","img/iron_door.png");
				dimensionChangeBlock.secret = other.getParent().getRoom();
				room.addObject(dimensionChangeBlock);
				other.getParent().setRoom(room);
			}
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
