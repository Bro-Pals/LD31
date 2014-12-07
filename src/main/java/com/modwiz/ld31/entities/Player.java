package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.entities.draw.Animation;

import java.awt.*;

public class Player extends Creature {

	private boolean sneaking, stabbing;
	private double radiationLevel, dimensionJumpDamageDuration;
	/** If the player is standing on a DimensionChangeBlock, then this is a reference to that
		block. Otherwise, this value is null. */
	private Dimension dimensionToGoTo;

    /**
     * Creates a new player instance
     *
     * @param parent Dimension in the game world to be loaded into
     * @param x      The x position of the player
     * @param y      The y position of the player
     * @param w      The bounding width of the player
     * @param h      The bounding height of the player
     * @param health The initial health value for the player
     * @see com.modwiz.ld31.entities.Creature
     */
    public Player(Dimension parent, float x, float y, float w, float h, double health) {
        super(parent, x, y, w, h, health);
		sneaking = false;
		stabbing = false;
		radiationLevel = 0;
		dimensionToGoTo = null;
		setWeapon(new Weapon(this, 35, 3, 42));
		dimensionJumpDamageDuration = 0;
    }
	
    /**
     * Creates a new player with an {@link com.modwiz.ld31.entities.draw.Animation}
     *
     * @param parent Dimension in the game world to be loaded into
     * @param x      The x position of the player
     * @param y      The y position of the player
     * @param w      The bounding width of the player
     * @param h      The bounding height of the player
     * @param health The initial health value for the player
     * @param anim   The animation for the player
     * @see com.modwiz.ld31.entities.Creature
     */
    public Player(Dimension parent, float x, float y, float w, float h, double health, Animation anim) {
        super(parent, x, y, w, h, health, anim);
		sneaking = false;
		stabbing = false;
		radiationLevel = 0;
		dimensionToGoTo = null;
		dimensionJumpDamageDuration = 0;
		setWeapon(new Weapon(this, 35, 3, 42));
    }

	/**
     * Creates a new player with an {@link com.modwiz.ld31.entities.draw.Animation}
     *
     * @param parent Dimension in the game world to be loaded into
     * @param x      The x position of the player
     * @param y      The y position of the player
     * @param w      The bounding width of the player
     * @param h      The bounding height of the player
     * @param health The initial health value for the player
     * @param animationString   The animation for the player in string form (path)
     * @see com.modwiz.ld31.entities.Creature
     */
    public Player(Dimension parent, float x, float y, float w, float h, double health, String animationString) {
        super(parent, x, y, w, h, health, animationString);
		sneaking = false;
		stabbing = false;
		radiationLevel = 0;
		dimensionToGoTo = null;
		dimensionJumpDamageDuration = 0;
		setWeapon(new Weapon(this, 35, 3, 42));
    }
	
	@Override
	public void render(Graphics g, float camX, float camY) {
		if (getAnimation() != null) {
			int rightAnim = stabbing ? 2 : 0;
			int leftAnim = stabbing ? 3 : 1;
			int delay = stabbing ? 2 : 8;
			if (stabbing && getAnimation().getFrameOn() >= 4) {
				rightAnim = 0;
				leftAnim = 1;
				delay = 8;
				stabbing = false;
			}
			if (isFacingRight()) { 
				getAnimation().setTrack(rightAnim, delay); // won't reset the frame if the track it's changing to is the same as it already was
					g.drawImage(getAnimation().getCurrentFrame(), (int)(getX()-camX), (int)(getY()-camY), null);
			} else {
				getAnimation().setTrack(leftAnim, delay);
				g.drawImage(getAnimation().getCurrentFrame(), (int)(getX()-camX-20), (int)(getY()-camY), null);
			}
		} else {
			super.render(g, camX, camY);
		}
		// bounding box
		//g.setColor(Color.BLACK);
		//g.drawRect((int)(getX()-camX), (int)(getY()-camY), (int)getWidth(), (int)getHeight());
	}
	
	public void changeRadiation(double amount) {
		this.radiationLevel += amount;
		if (this.radiationLevel < 0) 
			this.radiationLevel = 0;
	}
	
	/**
		If dimensionToGoTo is NOT null, then it jumps a dimension.
		The Player's parent becomes the new dimension. After this
		is called, dimensionToGoTo becomes null
	*/
	public void jumpDimension() {
		if (dimensionToGoTo != null) {
			dimensionJumpDamageDuration += 40; // frames
			getParent().getObjects().remove(this);
			setParent(dimensionToGoTo);
			getParent().getObjects().add(this);
			dimensionToGoTo = null;
		}
	}
	
	public Dimension getDimensionToGoTo() {
		return this.dimensionToGoTo;
	}
	
	public void setDimensionToGoTo(Dimension d) {
		this.dimensionToGoTo = d;
	}
	
    /**
     * Set sneaking mode, this reduces enemy detection? TODO: Clarify what this does
     * @param s Whether or not the player is sneaking
     */
	public void setSneaking(boolean s) {
		this.sneaking = s;
	}

	@Override
	public void useWeapon(int x, int y) {
		super.useWeapon(x, y);
		stabbing = true;
	}
	
    /**
     * Gets whether or not the player is in sneaking mode
     * @return Whether this player is sneaking
     */
	public boolean isSneaking() {
		return sneaking;
	}
	
	@Override
	public Object clone() {
		//TO DO : Copy the animation
		Player p = new Player(null, getX(), getY(), getWidth(), getHeight(), (int)getHealth(), getAnimationString());
		p.setName(getName());
		return p;
	}
}
