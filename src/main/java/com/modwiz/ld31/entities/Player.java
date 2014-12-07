package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.entities.draw.Animation;

import java.awt.*;

public class Player extends Creature {

	private boolean sneaking;
	private double radiationLevel;
	/** If the player is standing on a DimensionChangeBlock, then this is a reference to that
		block. Otherwise, this value is null. */
	/** The message the player is currently viewing. If this is null, there is no messages*/
	private String[] messagesViewing;
	private int messageOn;

	private static Player player;
	
	static {
		player = new Player(null, 0, 0, 60, 120, 100, "anim/player.animation");
		player.setName("ThePlayer");
	}
	
	public static Player getSingleton() {
		return player;
	}
	
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
        radiationLevel = 0;
        setWeapon(new Weapon(this, 35, 30, 42));
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
		radiationLevel = 0;
		setWeapon(new Weapon(this, 35, 30, 42));
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
		radiationLevel = 0;
		setWeapon(new Weapon(this, 35, 30, 42));
    }
	
	@Override
	public void render(Graphics g, float camX, float camY) {
		super.render(g, camX, camY);
		
		// render the message from a MessageBlock
		if (messagesViewing != null) {
			int boxWidth = 100;
			int boxHeight = 50;
			int boxX = (int)(getX() + (getWidth()/2) - (boxWidth/2) - camX);
			int boxY = (int)(getY() - boxHeight - 10 - camY);
			g.setColor(Color.BLACK);
			g.fillRect(boxX, boxY, boxWidth, boxHeight);
			g.setColor(Color.WHITE);
			g.fillRect(boxX+1, boxY+1, boxWidth-2, boxHeight-2);
			g.setColor(Color.BLACK);
			g.drawString(messagesViewing[messageOn], boxX + 5, boxY + 20);
			if (messageOn < messagesViewing.length - 1) { // not on the last message
				g.drawString("Press S to continue...", boxX+15, boxY + 35);
			}
		}
		
		// bounding box
		//g.setColor(Color.BLACK);
		//g.drawRect((int)(getX()-camX), (int)(getY()-camY), (int)getWidth(), (int)getHeight());
	}
	
	@Override
	public void update() {
		super.update();
		radiationLevel--;
		if (radiationLevel > 0) {
			damage(0.1); // damage a little
		}
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
	public void jumpDimension(Dimension dim) {
			getParent().getObjects().remove(this);
			setParent(dim);
            getParent().addObject(this);
			radiationLevel += 20;
    }

	
	/**
		View the next message
	*/
	public void cycleMessages() {
		if (messagesViewing != null) {
			if (messageOn < messagesViewing.length - 2) {
				messageOn++;
			}
		}
	}
	
	public void setMessages(String[] msgs) {
		if (messagesViewing != msgs) {
			messagesViewing = msgs;
			messageOn = 0;
		}
	}

    /**
     * Set sneaking mode, this reduces enemy detection? TODO: Clarify what this does
     * @param s Whether or not the player is sneaking
     */
	public void setSneaking(boolean s) {
		this.sneaking = s;
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
