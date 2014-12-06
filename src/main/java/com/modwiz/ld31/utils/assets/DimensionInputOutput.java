package com.modwiz.ld31.utils.assets;

import horsentp.stp.DataInputOutput;
import com.modwiz.ld31.world.*;
import java.io.File;
import java.util.ArrayList;

public class DimensionInputOutput extends DataInputOutput<Dimension> {
	
	@Override
	protected Dimension createDefaultObject() {
		return new Dimension();
	}
	
	@Override
	public void readProperty(Dimension data, String property, String value) {
		
	}
	
	@Override
	public String writeProperty(Dimension data, String property) {
		
		return "";
	}
	
	@Override
	public String[] getPropertyList() {
		return new String[] { };
	}
}	