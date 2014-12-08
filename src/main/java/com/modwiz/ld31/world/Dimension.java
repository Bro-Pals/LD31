package com.modwiz.ld31.world;

import com.modwiz.ld31.entities.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The world that will hold references to all the GameObjects in the game in the current dimension
 */
public class Dimension {
	private String name;

	private Room currentRoom;

	/**
	 * Creates a new Dimension object
	 * @param n The name of the dimension
	 */
	public Dimension(String n) {
		currentRoom = new Room();
		name = n;
	}

	public Dimension() {
		this("NoName");
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
		return currentRoom.getObjects();
	}
	
	public void updateObjects() {
		/*
		for (int i=0; i<objects.size(); i++) {
			GameObject obj = objects.get(i);
			if (obj.isDead()) {
				objects.remove(i);
				continue;
			}
			obj.update();
		}*/
		currentRoom.updateRoom();
	}

	/**
	 * Called by {@link com.modwiz.ld31.world.GameWorld} to render the dimension
	 * @param g {@link java.awt.Graphics} context to be rendered with
	 * @param camX The camera's X position in the world.
	 * @param camY The camera's Y position in the world.
	 */
	public void renderObjects(Graphics g, float camX, float camY) {
		/*
		for (GameObject obj : objects) {
			obj.render(g, camX, camY);
		}
		*/
		currentRoom.renderRoom(g, camX, camY);
	}
	
	/**
	* Adds a GameObject to this dimension.
	* @param object the object to add to the dimension
	*/
	public void addObject(GameObject object) {
		currentRoom.addObject(object);
		object.setParent(this);
	}
	
	/**
	* Removes a GameObject to this dimension.
	* @param object the object to remove to the dimension
	*/
	public void removeObject(GameObject object) {
		currentRoom.removeObject(object);
		object.setParent(null);
	}

	public Room getRoom() {
		return currentRoom;
	}

	public void setRoom(Room room) {
		currentRoom = room;
	}
	
	@Override
	public String toString() {
		return getName(); //For the level editor
	}
}
