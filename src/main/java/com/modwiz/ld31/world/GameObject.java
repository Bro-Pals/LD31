package com.modwiz.ld31.world;

import horsentpmath.Vector2;

/**
	The extreme game object for everything that is going to go in the world
*/
public class GameObject {
	
	float x, y;
	Vector2 velocity;
	
	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
		velocity = new Vector2();
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
	
	public void setLocaton(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
