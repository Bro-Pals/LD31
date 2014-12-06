package com.modwiz.ld31.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.Graphics;

/**
	The world that will hold references to all the dimensions and changes between it
*/
public class GameWorld {
	
	private HashMap<String, Dimension> dimensions;
	private Dimension activeDimension;
	
	public GameWorld() {
		dimensions = new HashMap<>();
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
	
	public void setActiveDimension(String dimensionName) {
		if (dimensions.get(dimensionName)!=null) {
			activeDimension = dimensions.get(dimensionName);
		} else {
			System.out.println("Dimension can't be made active: That Dimension is " +
				"not in the game world");
		}
	}
	
	public void addDimension(Dimension dim) {
		dimensions.put(dim.getName(), dim);
	}
	
	public Dimension[] getDimensions() {
		return (Dimension[])dimensions.values().toArray(new Dimension[0]);
	}
	
	public void removeDimension(Dimension dim) {
		dimensions.remove(dim.getName());
	}
	
	public Dimension getActiveDimension() {
		return activeDimension;
	}
	
	public int dimensionCount() {
		return dimensions.size();
	}
}
