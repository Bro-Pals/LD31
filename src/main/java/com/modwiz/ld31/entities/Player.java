package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.entities.draw.Animation;

public class Player extends Creature {

	private boolean sneaking;

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
    }

	@Override
	public void render(Graphics g) {
		if (getAnimation() != null) {
			if (facingRight) { 
				getAnimation().setTrack(0); // won't reset the frame if the track it's changing to is the same as it already was
				g.drawImage(getAnimation().getCurrentFrame(), (int)getX(), (int)getY(), null);
			} else {
				getAnimation().setTrack(1);
				g.drawImage(getAnimation().getCurrentFrame(), (int)getX() - 15, (int)getY(), null);
			}
		} else {
			super.render(g);
		}
		// bounding box
		g.setColor(Color.BLACK);
		g.drawRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
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
		Player p = new Player(null, getX(), getY(), getWidth(), getHeight(), (int)getHealth(), null);
		p.setName(getName());
		return p;
	}
}
