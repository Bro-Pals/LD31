package com.modwiz.ld31.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.Graphics;
import java.util.Map;

/**
	The world that will hold references to all the dimensions and changes between it
*/
public class GameWorld {
	
	private Map<String, Dimension> dimensions;
	private Dimension activeDimension;
	
	public GameWorld() {
		dimensions = new HashMap<>();
		activeDimension = null;
	}

	/**
	 * Updates all the dimensions in the game world, not just the active one
	 */
	public void updateDimension() {
		for (Dimension dim : dimensions.values()) {
			if (dim == null) {
				continue;
			}

			dim.updateObjects();
		}
	}

	/**
	 * Called to render the active dimension onto the screen
	 * @param g The graphics context to be used for rendering
	 */
	public void renderDimension(Graphics g) {
		if (activeDimension != null) {
			activeDimension.renderObjects(g);
		}
	}

	/**
	 * Sets a dimension as active, used when the player switches dimensions
	 * TODO: Notify dimension of it's new status for loading up.
	 * @param dimensionName The name of the dimension to become active
	 */
	public void setActiveDimension(String dimensionName) {
		if (dimensions.get(dimensionName)!=null) {
			activeDimension = dimensions.get(dimensionName);
		} else {
			System.out.println("Dimension can't be made active: That Dimension is " +
				"not in the game world");
		}
	}

	/**
	 * Add a new dimension to the world, it's internal name must be unique in the namespace of the game world.
	 * @param dim The dimension to add
	 */
	public void addDimension(Dimension dim) {
		dimensions.put(dim.getName(), dim);
	}

	/**
	 * Get all the dimensions present
	 * @return An array of all dimensions in the world
	 */
	public Dimension[] getDimensions() {
		return (Dimension[])dimensions.values().toArray(new Dimension[0]);
	}

	/**
	 * Removes a dimension from the world
	 * @param dim The dimension object to remove
	 */
	public void removeDimension(Dimension dim) {
		dimensions.remove(dim.getName());
	}

	/**
	 * The dimension where the player is present
	 * @return The active dimension
	 */
	public Dimension getActiveDimension() {
		return activeDimension;
	}

	/**
	 * Gets the current number of dimensions
	 * @return The number of dimensions
	 */
	public int dimensionCount() {
		return dimensions.size();
	}
}
