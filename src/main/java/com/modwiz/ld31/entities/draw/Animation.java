package com.modwiz.ld31.entities.draw;

import java.awt.image.BufferedImage;

/**
	The animation class that holds and runs animations
*/
public class Animation {

	private BufferedImage[][] tracks;
	private int trackOn;
	private int frameOn;
	private int frameDelay;
	private int frameDelayOn;
	
	public Animation(BufferedImage[][] t) {
		tracks = t;
		trackOn = 0;
		frameOn = 0;
		frameDelay = 1; // default
		frameDelayOn = 0;
	}
	
	public Animation(BufferedImage[][] t, int fDelay) {
		this(t);
		frameDelay = fDelay;
	}
	
	/**
		Update the frameOn
	*/
	public void update() {
		frameDelayOn++;
		if (frameDelayOn >= frameDelay) {
			frameDelayOn = 0;
			frameOn++;
			if (frameOn >= tracks[trackOn].length)
				frameOn = 0;
		}
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
