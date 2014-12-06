package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;
import horsentpmath.Vector2;

import java.awt.Graphics;
import java.awt.Color;

/**
	The extreme game object for everything that is going to go in the world
*/
public class GameObject {
	
	private Dimension parent;
	private float x, y;
	private Vector2 velocity;
	private Vector2 acceleration;
	private boolean dead;

	/**
	 * Creates a new GameObject instance
	 * @param parent The dimension for the GameObject to be loaded into
	 * @param x The initial x position of the GameObject
	 * @param y The initial y position of the GameObject
	 */
	public GameObject(Dimension parent, float x, float y) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		dead = false;
		velocity = new Vector2();
		acceleration = new Vector2();
	}

	/**
	 * Update logic called every game tick
	 */
	public void update() {
		velocity = (Vector2)velocity.add(acceleration);
		this.x += velocity.getX();
		this.y += velocity.getY();
	}

	/**
	 * Render code, called 60 times a second
	 * @param g The graphics context to render with
	 */
	public void render(Graphics g) {
		// tell the object how to render itself
		g.setColor(Color.BLACK);
		g.fillRect((int)x, (int)y, 3, 3);
	}

	/**
	 * Get the current dimension of this game object
	 * @return The Dimension object it is loaded into
	 */
	public Dimension getParent() {
		return parent;
	}
	
	/**
	 * Check if the creature is dead
	 * @return True if the creature is dead
	 */
	public boolean isDead() {
		return dead;
	}
	
	public void setDead(boolean d) {
		dead = d;
	}
	
	/**
	 * Sets the current dimension of this game object
	 * @param p The new dimension for this game object
	 */
	public void setParent(Dimension p) {
		this.parent = p;
	}

	/**
	 * Gets a velocity vector for this game object
	 * @return A Vector2 representing velocity
	 */
	public Vector2 getVelocity() {
		return velocity;
	}

	/**
	 * Gets an acceleration vector for this game object
	 * @return A Vector2
	 */
	public Vector2 getAcceleration() {
		return acceleration;
	}

	/**
	 * Sets the current x position in the world
	 * @param x The new x position
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Sets the current y position in the world
	 * @param y The new y position
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Gets the current x position of this game object
	 * @return The x position
	 */
	public float getX() {
		return x;
	}

	/**
	 * Gets the current y position of this game object
	 * @return The y position
	 */
	public float getY() {
		return y;
	}

	/**
	 * Sets both the x and y components of this game object
	 * @param x The new x position
	 * @param y The new y position
	 */
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
