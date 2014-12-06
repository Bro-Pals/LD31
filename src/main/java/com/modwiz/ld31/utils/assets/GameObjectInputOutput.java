package com.modwiz.ld31.utils.assets;

import horsentp.stp.DataInputOutput;
import com.modwiz.ld31.entities.*;
import com.modwiz.ld31.world.Dimension;
import java.io.File;
import java.util.ArrayList;

public class GameObjectInputOutput extends DataInputOutput<GameObject> {
	
	private Dimension dimension;
	
	public GameObjectInputOutput() {
		dimension = null;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	
	@Override
	protected GameObject createDefaultObject() {
		return new GameObject(dimension, 0, 0);
	}
	
	@Override
	public void readProperty(GameObject data, String property, String value) {
		
	}
	
	@Override
	public String writeProperty(GameObject data, String property) {
		
		return "";
	}
	
	@Override
	public String[] getPropertyList() {
		return new String[] { };
	}
}	