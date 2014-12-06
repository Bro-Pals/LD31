package com.modwiz.ld31.world;

import java.util.ArrayList;

/**
	The world that will hold references to all the GameObjects in the game in the current dimension
*/
public class Dimension {
	
	private ArrayList<GameObject> objects;
	
	public Dimension() {
		objects = new ArrayList<>();
	}
	
	
}
