package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;

/**
 * Powerup thingy that heals the player when they touch it. It then disappears
 */
public class RadiationSucker extends GameBlock {
	
	public RadiationSucker(Dimension parent, float x, float y) {
		super(parent, x, y, 30, 30, true);
		setName("Radiation Sucker");
		setCanCollide(false);
	}
	
	/**
	 * Represents the functionality when collided with
	 * @param other The {@link com.modwiz.ld31.entities.GameBlock} we are colliding with
	 */
	public void onCollide(GameBlock other) {
		if (!isDead() && other instanceof Player) {
			((Player)other).changeRadiation(-5); // reduces radiation by 5
			setDead(true);
		}
	}
	
	@Override
	public Object clone() {
		RadiationSucker rs = new RadiationSucker(null, getX(), getY());
		rs.setName(getName());
		return rs;
	}
}