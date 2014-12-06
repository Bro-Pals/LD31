package com.modwiz.ld31.entities.draw;

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
	 * Tick the animation
	 */
	public void update() {
		frameOn++;
		if (frameOn >= tracks[trackOn].length)
			frameOn = 0;
	}

	/**
	 * If multiple animation tracks are used this switches between them
	 * Ex. Running track, Jumping track, etc
	 * @param track The track number to switch to
	 */
	public void setTrack(int track) {
		if (track >= 0 && track < tracks.length) {
			frameOn = 0;
			trackOn = track;
		}	
	}

	/**
	 * Returns the current image for the animation
	 * @return Image to display by the object that is being animated
	 */
	public BufferedImage getCurrentFrame() {
		return tracks[trackOn][frameOn];
	}
	
}
