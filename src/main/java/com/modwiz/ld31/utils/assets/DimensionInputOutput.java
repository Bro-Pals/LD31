package com.modwiz.ld31.utils.assets;

import horsentp.stp.DataInputOutput;
import com.modwiz.ld31.world.*;
import java.io.File;
import java.util.ArrayList;

public class DimensionInputOutput extends DataInputOutput<GameWorld> {
	
	@Override
	protected GameWorld createDefaultObject() {
		return new GameWorld();
	}
	
	@Override
	public void readProperty(GameWorld data, String property, String value) {
		
	}
	
	@Override
	public String writeProperty(GameWorld data, String property) {
		
		return "";
	}
	
	@Override
	public String[] getPropertyList() {
		return new String[] { };
	}
}	