package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;
import horsentpmath.Vector2;
import com.modwiz.ld31.entities.draw.Animation;
import java.awt.Graphics;
import java.awt.Color;
import com.modwiz.ld31.utils.assets.AssetLoader;
import com.modwiz.ld31.utils.assets.loaders.AnimationLoader;
import com.modwiz.ld31.Main;

/**
	The extreme creature that the Player and Enemies will extend from
*/
public class Creature extends GameBlock {

	private double health, maxHealth;
	private boolean attacking;
	protected int attackAnimDelay, normalAnimDelay;
	protected boolean facingRight;
	private Weapon weapon;
	
	// The animation for this creature
	private Animation animation;
	private String animString;

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
		this.maxHealth = health;
		weapon = new Weapon(this, 50, 3, 20); // default weapon
		animString = null;
		attacking = false;
		attackAnimDelay = 2;
		normalAnimDelay = 7;
		getAcceleration().set(1, (float)Main.GRAVITY_RATIO);
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
		animString = null;
		attackAnimDelay = 2;
		normalAnimDelay = 7;
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
	public Creature(Dimension parent, float x, float y, float w, float h, double health, String animString) {
		this(parent, x, y, w, h, health, (Animation)null);
		if (animString!=null && !animString.equals("null")) {
			animation = AssetLoader.getSingleton().loadAsset(Animation.class, animString);
		}
		this.animString = animString;
		attackAnimDelay = 2;
		normalAnimDelay = 7;
	}

	public void setAttacking(boolean a) {
		this.attacking = a;
	}
	
	public boolean isAttacking() {
		return attacking;
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
		} else if (health > maxHealth) {
			health = maxHealth;
		}
	}

	public void jump(float vel) {
		if (isGrounded()) {
			getVelocity().set(1, -vel); // jumping
		}
	}
	
	/**
	 * Handles logic for when a weapon is used on a specific position
	 * Where refers to the actual attacking, not the position of the creature
	 * Ex: A bow would be shot from one place and fire an arrow to a different place
	 *
	 * @param x The x coordinate where the attack was attempted
	 * @param y The y coordinate where the attack was attempted
	 */
	public void useWeapon(int x, int y) {
		facingRight = x > (getX() + (getWidth()/2));
		weapon.use((Vector2)(new Vector2(x - (getX() + (getWidth()/2)), 
							y - (getY() + (getHeight()/2))).normalize())); // use the weapon if it's within range;
		setAttacking(true);
	}

	/**
	 * Set a new animation for the creature
	 * @param a The animation to set
	 */
	public void setAnimation(Animation a) {
		this.animation = a;
	}
	
	/**
	 * Sets the animation string, setting the actual Animation object as well as the internal string.
	 * Use this instead of setting the animation directly.
	 * @param animationString the new animation string
	 */
	public void setAnimationString(String animationString) {
		animString = animationString;
		setAnimation(AssetLoader.getSingleton().loadAsset(Animation.class, animationString));
	}
	
	/**
	 * Makes it so that this Creature has no animation
	 */
	public void noAnimation() {
		animString = null;
		animation = null;
	}
	
	public Animation getAnimation() {
		return animation;
	}

	/**
	 * Gets the string that represents this Creature's animation.
	 * @return the string that represents this Creature's animation.
	 */
	public String getAnimationString() {
		return animString;
	}
	
	/**
	 * Get the current health for the creature
	 * @return The health
	 */
	public double getHealth() {
		return health;
	}
	
	/**
	 * Set the current health for the creature
	 * @param health The new health
	 */
	public void setHealth(double health) {
		this.health = health;
	}
	
	/**
	 * Set the maximum current health for the creature
	 * @param health The new health
	 */
	public void setMaxHealth(double mhealth) {
		this.maxHealth = mhealth;
	}
	
	public double getMaxHealth() {
		return maxHealth;
	}

	/**
	 * Get if the creature is facing rihgt
	 * @return Return true if the player is facing right, or false
				if the player is facing left.
	*/
	public boolean isFacingRight() {
		return facingRight;
	}
	
	public void setFacingRight(boolean fr) {
		facingRight = fr;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update() {
		super.update(); // collision stuff from GameBlock
        if (getY()> 1000){
            this.setDead(true);
        }
		if (!(this instanceof Player) && getVelocity().getX() != 0) {
			facingRight = getVelocity().getX() > 0;
		}
		weapon.decreaseCooldown();
	}

	public Weapon getWeapon() {
		return weapon;
	}
	
	public void setWeapon(Weapon w) {
		this.weapon = w;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Graphics g, float camX, float camY) {
		if (getAnimation() != null) {
			int rightAnim = attacking ? 2 : 0;
			int leftAnim = attacking ? 3 : 1;
			int delay = attacking ? attackAnimDelay : normalAnimDelay;
			if (attacking && getAnimation().getFrameOn() >= getAnimation().getTrackSize() - 1) {
				rightAnim = 0;
				leftAnim = 1;
				delay = normalAnimDelay;
				attacking = false;
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
		/*
		if (animation != null) {
			if (facingRight) { 
				animation.setTrack(0); // won't reset the frame if the track it's changing to is the same as it already was
			} else {
				animation.setTrack(1);
			}
			g.drawImage(animation.getCurrentFrame(), (int)(getX()-camX), (int)(getY()-camY), null);
		} else {
			super.render(g, camX, camY);
		}
		*/
		
		renderHealthBar(g, camX, camY);
		// bounding box
		//g.setColor(Color.BLACK);
		//g.drawRect((int)(getX()-camX), (int)(getY()-camY), (int)getWidth(), (int)getHeight());
	}
	
	protected void renderHealthBar(Graphics g, float camX, float camY) {
		int barWidth = 60;
		int barFilledIn = (int)((health / maxHealth) * barWidth);
		int barHeight = 8;
		int barX = (int)(getX() + (getWidth()/2) - (barWidth/2));
		int barY = (int)(getY() - 10);
		g.setColor(Color.BLACK);
		g.fillRect(barX - (int)camX, barY - (int)camY, barWidth, barHeight);
		g.setColor(Color.RED);
		g.fillRect(barX + 1 - (int)camX, barY + 1 - (int)camY, barWidth - 2, barHeight - 2);
		if (health >= 0) {
			g.setColor(Color.GREEN);
			g.fillRect(barX + 1 + (barWidth - barFilledIn) - (int)camX, barY + 1 - (int)camY, 
				barFilledIn - 2, barHeight - 2);
		}
	}
	
	@Override
	public Object clone() {
		//TO DO : Copy the animation
		Creature c;
		if (animString!=null) {
			c = new Creature(null, getX(), getY(), getWidth(), getHeight(), (int)getHealth(), animString);
		} else {
			c = new Creature(null, getX(), getY(), getWidth(), getHeight(), (int)getHealth());
		}
		c.setName(getName());
		return c;
	}
}
