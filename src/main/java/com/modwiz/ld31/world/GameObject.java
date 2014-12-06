package com.modwiz.ld31.world;

import horsentpmath.Vector2;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
	The extreme game object for everything that is going to go in the world
*/
public class GameObject {
	
	private List<Dimension> parent;
	private float x, y;
	private Vector2 velocity;
	private Vector2 acceleration;
	
	public GameObject(List<Dimension> parent, float x, float y) {
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
	
	public List<Dimension> getParent() {
		return parent;
	}
	
	public void setParent(List<Dimension> p) {
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
