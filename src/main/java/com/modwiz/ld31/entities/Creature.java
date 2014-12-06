package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;
import horsentpmath.Vector2;
import com.modwiz.ld31.entities.draw.Animation;
import java.awt.Graphics;
import java.awt.Color;

/**
	The extreme creature that the Player and Enemies will extend from
*/
public class Creature extends GameBlock {

	private double health;
	private boolean facingRight;
	private Weapon weapon;
	
	// The animation for this creature
	private Animation animation;

	/**
	 * Creates a new creature instance
	 * @param parent Dimension in the game world to be loaded into
	 * @param x The x position of the creature
	 * @param y The y position of the creature
	 * @param w The bounding width of the creature
	 * @param h The bounding height of the creature
	 * @param health The initial health value for the creature
	 * @see com.modwiz.ld31.entities.GameBlock
	 */
	public Creature(Dimension parent, float x, float y, float w, float h, double health) {
		super(parent, x, y, w, h);
		animation = null; // no animation :(
		this.health = health;
		weapon = new Weapon(this, 15, 3, 12); // default weapon
	}

	/**
	 * Creates a new creature with an {@link com.modwiz.ld31.entities.draw.Animation}
	 * @param parent Dimension in the game world to be loaded into
	 * @param x The x position of the creature
	 * @param y The y position of the creature
	 * @param w The bounding width of the creature
	 * @param h The bounding height of the creature
	 * @param health The initial health value for the creature
	 * @param anim The animation for the creature
	 * @see com.modwiz.ld31.entities.GameBlock
	 */
	public Creature(Dimension parent, float x, float y, float w, float h, double health, Animation anim) {
		this(parent, x, y, w, h, health);
		animation = anim;
	}

	/**
	 * Damage this creature the specified amount.
	 * If health becomes less than or equal to zero, then dead will be set to true.
	 * @param amount The amount of damage to do
	 */
	public void damage(double amount) {
		health = health - amount;
		if (health <= 0) {
			setDead(true);
		}
	}

	public void useWeapon(int x, int y) {
		facingRight = x > getX();
		weapon.use((Vector2)(new Vector2(getX() - x, getY() - y).normalize())); // use the weapon if it's within range;
	}
	
	/**
	 * Set a new animation for the creature
	 * @param a The animation to set
	 */
	public void setAnimation(Animation a) {
		this.animation = a;
	}
	
	public Animation getAnimation() {
		return animation;
	}

	/**
	 * Get the current health for the creature
	 * @return The health
	 */
	public double getHealth() {
		return health;
	}

	/**
	 * Get if the creature is facing rihgt
	 * @return Return true if the player is facing right, or false
				if the player is facing left.
	*/
	public boolean isFacingRight() {
		return facingRight;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update() {
		super.update(); // collision stuff from GameBlock
		if (getVelocity().getX() != 0) {
			facingRight = getVelocity().getX() > 0;
		}
		weapon.decreaseCooldown();
		animation.update(); 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Graphics g) {
		if (animation != null) {
			if (facingRight) { 
				animation.setTrack(0); // won't reset the frame if the track it's changing to is the same as it already was
			} else {
				animation.setTrack(1);
			}
			g.drawImage(animation.getCurrentFrame(), (int)getX(), (int)getY(), null);
		} else {
			super.render(g);
		}
		// bounding box
		g.setColor(Color.BLACK);
		g.drawRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
	}
	
}
