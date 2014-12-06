package com.modwiz.ld31.world;

import com.modwiz.ld31.entities.Creature;
import com.modwiz.ld31.entities.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;

/**
 * The world that will hold references to all the GameObjects in the game in the current dimension
 */
public class Dimension {
	
	private List<GameObject> objects;
	private String name;

	/**
	 * Creates a new Dimension object
	 */
	public Dimension() {
		objects = new ArrayList<>();
		name = "";
	}

	/**
	 * Sets the name for this dimension
	 * @param name The name for the dimension
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the name for this dimension
	 * @return The name of the dimension
	 */
	public String getName() { return name; }

	/**
	 * Get all the {@link com.modwiz.ld31.entities.GameObject} instances in the dimension
	 * @return A {@link java.util.List} of {@link com.modwiz.ld31.entities.GameObject} instances
	 */
	public List<GameObject> getObjects() {
		return objects;
	}
	
	public void updateObjects() {
		for (int i=0; i<objects.size(); i++) {
			GameObject obj = objects.get(i);
			if (obj.isDead()) {
				objects.remove(i);
				continue;
			}
			obj.update();
		}
	}

	/**
	 * Called by {@link com.modwiz.ld31.world.GameWorld} to render the dimension
	 * @param g {@link java.awt.Graphics} context to be rendered with
	 * @param camX The camera's X position in the world.
	 * @param camY The camera's Y position in the world.
	 */
	public void renderObjects(Graphics g, float camX, float camY) {
		for (GameObject obj : objects) {
			obj.render(g, camX, camY);
		}
	}
	
	/**
	* Adds a GameObject to this dimension.
	* @param object the object to add to the dimension
	*/
	public void addObject(GameObject object) {
		objects.add(object);
		object.setParent(this);
	}
	
	/**
	* Removes a GameObject to this dimension.
	* @param object the object to remove to the dimension
	*/
	public void removeObject(GameObject object) {
		objects.remove(object);
		object.setParent(null);
	}
	
	@Override
	public String toString() {
		return getName(); //For the level editor
	}
}
