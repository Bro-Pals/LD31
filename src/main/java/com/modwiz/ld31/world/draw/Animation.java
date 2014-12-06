package com.modwiz.ld31.world.draw;

import java.awt.image.BufferedImage;

/**
	The animation class that holds and runs animations
*/
public class Animation {

	private BufferedImage[][] tracks;
	private int trackOn;
	private int frameOn;
	
	public Animation(BufferedImage[][] t) {
		tracks = t;
		trackOn = 0;
		frameOn = 0;
	}
	
	/**
		Update the frameOn
	*/
	public void update() {
		frameOn++;
		if (frameOn >= tracks[trackOn].length)
			frameOn = 0;
	}
	
	public void setTrack(int track) {
		if (track >= 0 && track < tracks.length) {
			frameOn = 0;
			trackOn = track;
		}	
	}
	
	public BufferedImage getCurrentFrame() {
		return tracks[trackOn][frameOn];
	}
	
}
