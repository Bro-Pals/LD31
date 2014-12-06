package com.modwiz.ld31.world;

import com.modwiz.ld31.world.draw.Animation;
import java.awt.Graphics;
import java.util.List;
import java.awt.image.BufferedImage;

/**
	The extreme creature that the Player and Enemies will extend from
*/
public class Creature extends GameBlock {

	private double health;
	private boolean dead;
	/**
		The animation for this creature
	*/
	private Animation animation;
	
	public Creature(List<Dimension> parent, float x, float y, float w, float h) {
		super(parent, x, y, w, h);
		health = 1.0; // hahahah
		animation = null; // no animation :(
		dead = false;
	}
	
	public Creature(List<Dimension> parent, float x, float y, float w, float h, int hp, Animation anim) {
		super(parent, x, y, w, h);
		health = hp;
		animation = anim;
	}
	
	public void damage(int amount) {
		health = health - amount;
		if (health <= 0) {
			dead = true;
		}
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void setAnimation(Animation a) {
		this.animation = a;
	}
	
	@Override
	public void update() {
		super.update(); // collision stuff from GameBlock
		
		animation.update(); 
	}
	
	@Override
	public void render(Graphics g) {
		if (animation != null) {
			g.drawImage(animation.getCurrentFrame(), (int)getX(), (int)getY(), null);
		} else {
			super.render(g);
		}
	}
	
}
