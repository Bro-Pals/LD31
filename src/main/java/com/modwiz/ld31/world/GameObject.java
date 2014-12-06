package com.modwiz.ld31.world;

import horsentpmath.Vector2;

import java.awt.*;

/**
	The extreme game object for everything that is going to go in the world
*/
public class GameObject {
	
	private ArrayList<Dimension> parent;
	private float x, y;
	private Vector2 velocity;
	private Vector2 acceleration;
	
	public GameObject(ArrayList<Dimension> parent, float x, float y) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		velocity = new Vector2();
		acceleration = new Vector2();
	}
	
	public void update() {
		velocity.add(acceleration);
		this.x += velocity.getX();
		this.y += velocity.getY();
	}
	
	public void render(Graphics g) {
		// tell the object how to render itself
		g.setColor(Color.BLACK);
		g.fillRect((int)x, (int)y, 3, 3);
	}
	
	public ArrayList<Dimension> getParent() {
		return parent;
	}
	
	public void setParent(ArrayList<Dimension> p) {
		this.parent = p;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public Vector2 getAcceleration() {
		return acceleration;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
