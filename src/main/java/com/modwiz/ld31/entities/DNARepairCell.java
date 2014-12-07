package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;

/**
 * Powerup thingy that heals the player when they touch it. It then disappears
 */
public class DNARepairCell extends GameBlock {
	
	public DNARepairCell(Dimension parent, float x, float y) {
		super(parent, x, y, 30, 30, true);
		setName("DNA Repair Cell");
		setCanCollide(false);
	}
	
	/**
	 * Represents the functionality when collided with
	 * @param other The {@link com.modwiz.ld31.entities.GameBlock} we are colliding with
	 */
	public void onCollide(GameBlock other) {
		if (!isDead() && other instanceof Player) {
			((Player)other).damage(-5); // heal 5 health
			setDead(true);
		}
	}
}