package com.modwiz.ld31.world;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
	The world that will hold references to all the GameObjects in the game in the current dimension
*/
public class Dimension {
	
	private List<GameObject> objects;
	
	public Dimension() {
		objects = new ArrayList<>();
	}
	
	public List<GameObject> getObjects() {
		return objects;
	}
	
	public void updateObjects() {
		for (int i=0; i<objects.size(); i++) {
			GameObject obj = objects.get(i);
			if (obj instanceof Creature) { //remove the creature if it's dead
				if (((Creature)obj).isDead()) {
					objects.remove(i);
					continue;
				}
			}
			obj.update();
		}
	}
	
	public void renderObjects(Graphics g) {
		for (GameObject obj : objects) {
			obj.render(g);
		}
	}
}
