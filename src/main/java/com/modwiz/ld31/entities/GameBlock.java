package com.modwiz.ld31.entities;

import java.awt.image.BufferedImage;
import java.awt.*;
import com.modwiz.ld31.world.Dimension;

/**
	The basic block with width, height, and collision
*/
public class GameBlock extends GameObject {
	
	private float width, height;
	private boolean grounded; // has this object collided with something below it?
	private boolean staticBlock; // Platform
	private BufferedImage image;

	/**
	 * Represents a {@link GameObject} with a width and height
	 * @param parent The dimension for this object to be loaded into
	 * @param x The initial x position of this GameBlock
	 * @param y The initial y position of this GameBlock
	 * @param w The width of this GameBlock
	 * @param h The height of this GameBlock
	 * @see com.modwiz.ld31.entities.GameObject
	 */
	public GameBlock(Dimension parent, float x, float y, float w, float h) {
		super(parent, x, y);
		this.width = w;
		this.height = h;
		this.grounded = false;
		this.staticBlock = false;
		image = null;
	}

	public GameBlock(Dimension parent, float x, float y, float w, float h, boolean staticBlock) {
		this(parent, x, y, w, h);
		this.staticBlock = true;
	}
	
	public GameBlock(Dimension parent, float x, float y, float w, float h, boolean staticBlock, BufferedImage image) {
		this(parent, x, y, w, h, staticBlock);
		this.staticBlock = true;
		this.image = image;
	}

	public void setImage(BufferedImage img) {
		this.image = img;
	}
	
	/**
	 * Represents the functionality when collided with
	 * @param other The {@link com.modwiz.ld31.entities.GameBlock} we are colliding with
	 */
	public void onCollide(GameBlock other) {
		// what happens when there is a collision with the other block
		
		//System.out.println("I have ran into another block!");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update() {
		super.update();
		
		// if it's not moving it can't move
		if (getVelocity().getX() == 0 && getVelocity().getY() == 0) {
			return;
		}

		grounded = false;
		// collision checking
		for (GameObject obj : getParent().getObjects()) {
			if (obj instanceof GameBlock && obj != this) {
				GameBlock bl = (GameBlock) obj; // we're working with another block
				
				float smallestMaxX = bl.getX() + bl.getWidth() < this.getX() + this.getWidth() ?
					bl.getX() + bl.getWidth() : this.getX() + this.getWidth();
				float largestMinX = bl.getX() > this.getX() ? bl.getX() : this.getX();
				
				float smallestMaxY = bl.getY() + bl.getHeight() < this.getY() + this.getHeight() ?
					bl.getY() + bl.getHeight() : this.getY() + this.getHeight();
				float largestMinY = bl.getY() > this.getY() ? bl.getY() : this.getY();
				
				float penX = largestMinX - smallestMaxX;
				float penY = largestMinY - smallestMaxY;

				if (penX < 0 && penY < 0) {
					if (Math.abs(penY) < Math.abs(penX)) {
						if (this.getY() < bl.getY()) {
							setY(bl.getY() - getHeight());
							// NOTE: This prevents jumping off entities
							if (bl.staticBlock) {
								grounded = true;
							}
						} else {
							setY(bl.getY() + bl.getHeight());
						}
						getVelocity().set(1, 0);
					} else {
						if (this.getX() < bl.getX()) {
							setX(bl.getX() - getWidth());
						} else {
							setX(bl.getX() + bl.getWidth());
						}
						getVelocity().set(0, 0);
					}
					onCollide(bl); // collide with other blocks
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Graphics g) {
		//if (image == null) {
			g.setColor(Color.BLACK);
			g.fillRect((int)getX(), (int)getY(), (int)width, (int)height);
		//} else {
			
		//}
	}

	/**
	 * Sets the height of this game block
	 * @param h The height
	 */
	public void setHeight(float h) {
		this.height = h;
	}

	/**
	 * Gets if this block is on the ground. Only really matters if it has gravity
	 * @return If the block is grounded or not
	 */
	public boolean isGrounded() {
		return grounded;
	}

	/**
	 * Sets the game block to be static and thus grounding
	 * @param isStatic Should be treated as ground or not
	 */
	public void setStaticBlock(boolean isStatic) {
		staticBlock = isStatic;
	}

	/**
	 * Gets if the game block is static and will cause grounding
	 * @return Should be treated as ground or not
	 */
	public boolean isStaticBlock() {
		return staticBlock;
	}
	
	/**
	 * Sets the width of this game block
	 * @param w The width
	 */
	public void setWidth(float w) {
		this.width = w;
	}

	/**
	 * Gets the height of this game block
	 * @return The height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Gets the width of this game block
	 * @return The width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Sets the size of this game block
	 * @param w The width
	 * @param h The height
	 */
	public void setSize(float w, float h) {
		this.width = w;
		this.height = h;
	}
}
