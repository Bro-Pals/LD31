package com.modwiz.ld31.world;

import horsentpmath.Vector2;

import java.awt.Graphics;
import java.awt.Color;

/**
	The extreme game object for everything that is going to go in the world
*/
public class GameObject {
	
	float x, y;
	Vector2 velocity;
	Vector2 acceleration;
	
	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
		velocity = new Vector2();
	}
	
	public void update() {
		this.x += velocity.getX();
		this.y += velocity.getY();
	}
	
	public void render(Graphics g) {
		// tell the object how to render itself
		g.setColor(Color.BLACK);
		g.fillRect((int)x, (int)y, 3, 3);
	}
	
	public Vector2 getVelocity() {
		return velocity;
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
