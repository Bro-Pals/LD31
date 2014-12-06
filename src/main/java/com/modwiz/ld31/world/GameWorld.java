package com.modwiz.ld31.world;

import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;

/**
	The world that will hold references to all the dimensions and changes between it
*/
public class GameWorld {
	
	private List<Dimension> dimensions;
	private Dimension activeDimension;
	
	public GameWorld() {
		dimensions = new ArrayList<>();
		activeDimension = null;
	}
	
	public void updateDimension() {
		if (activeDimension != null) {
			activeDimension.updateObjects();
		}
	}
	
	public void renderDimension(Graphics g) {
		if (activeDimension != null) {
			activeDimension.renderObjects(g);
		}
	}
	
	public void setActiveDimension(Dimension dim) {
		if (dimensions.contains(dim)) {
			activeDimension = dim;
		} else {
			System.out.println("Dimension can't be made active: That Dimension is " +
				"not in the game world");
		}
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
