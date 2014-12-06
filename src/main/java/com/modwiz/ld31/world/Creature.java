package com.modwiz.ld31.world;

import com.modwiz.ld31.world.draw;

/**
	The extreme creature that the Player and Enemies will extend from
*/
public class Creature extends GameBlock {

	private int health;
	private Animation animation;
	
	public GameBlock(ArrayList<Dimension> parent, float x, float y, float w, float h) {
		super(parent, x, y, w, h);
		health = 1; // hahahah
	}
	
	
}
