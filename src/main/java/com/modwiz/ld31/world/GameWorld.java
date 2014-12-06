package com.modwiz.ld31.world;

import java.util.ArrayList;

/**
	The world that will hold references to all the dimensions and changes between it
*/
public class GameWorld {
	
	private ArrayList<Dimension> dimensions;
	
	public GameWorld() {
		dimensions = new ArrayList<>();
	}
	
	public void addDimension(Dimension dim) {
		dimensions.add(dim);
	}
	
	public void removeDimension(Dimension dim) {
		dimensions.remove(dim);
	}
	
	public int dimensionCount() {
		return dimensions.size();
	}
}
