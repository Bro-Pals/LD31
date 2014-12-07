package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.entities.draw.Animation;

/**
 * A generic baddie
 */
public class Enemy extends Creature {

	private int[][] patrolPoints;
	private int patrolPointOn, timeOnPoint, timeOnPointMax;
	/** How close the Enemy needs to be near a point before they start going to the next one*/
	private int distanceNear;

    /**
     * Creates a new Enemy instance
     *
     * @param parent Dimension in the game world to be loaded into
     * @param x      The x position of the entity
     * @param y      The y position of the entity
     * @param w      The bounding width of the entity
     * @param h      The bounding height of the entity
     * @param health The initial health value for the entity
     * @see com.modwiz.ld31.entities.Creature
     */
    public Enemy(Dimension parent, float x, float y, float w, float h, double health) {
        super(parent, x, y, w, h, health);
		patrolPoints = null; // no patrol path
		patrolPointOn = 0;
		distanceNear = 50;
		timeOnPoint = 0;
		timeOnPointMax = 20; // frames
    }

    /**
     * Creates a new entity with an {@link com.modwiz.ld31.entities.draw.Animation}
     *
     * @param parent Dimension in the game world to be loaded into
     * @param x      The x position of the entity
     * @param y      The y position of the entity
     * @param w      The bounding width of the entity
     * @param h      The bounding height of the entity
     * @param health The initial health value for the entity
     * @param anim   The animation for the entity
     * @see com.modwiz.ld31.entities.Creature
     */
    public Enemy(Dimension parent, float x, float y, float w, float h, double health, Animation anim) {
        super(parent, x, y, w, h, health, anim);
		patrolPoints = null; // no patrol paths
		patrolPointOn = 0;
		distanceNear = 50;
    }
	
	public boolean canSeePlayer() {
		Player player = null;
		for (GameObject obj : getParent().getObjects()) {
			if (obj instanceof Player) {
				player = (Player) obj;
				break;
			}
		}
		if (player == null) {
			return false;
		}
		
		// do some checks
		// maybe check:
		// * Is the player in the direction Enemy is facing
		// * Is the player close enough to the Enemy
		// * Does the enemy have line of sight of the player
		
		return true;
	}
	
	@Override
	public void update() {
		super.update();
		
		float distFromNextX = (getX() + (getWidth()/2)) - patrolPoints[patrolPointOn][0];
		float distFromNextY = (getY() + (getHeight()/2)) - patrolPoints[patrolPointOn][1];
		if ((distanceNear * distanceNear) < (distFromNextX * distFromNextX) + (distFromNextY * distFromNextY)) {
			timeOnPoint--;
			if (timeOnPoint < 0) {
				timeOnPoint = timeOnPointMax;
				patrolPointOn++;
				if (patrolPointOn >= patrolPoints.length)
					patrolPointOn = 0;
			}
		} else {
			System.out.println("I am going to move towards it now");
			System.out.println("Distance from on X: " + distFromNextX);
			if (distFromNextX < 0) {
				getVelocity().set(0, -3);
			} else {
				getVelocity().set(0, 3);
			}
			
		}
	}
	
	/**
	 * Set the patrol path for the Enemy object.
	 * The path is in the format {{x0, y0}, {x1, y1,} ... {xn, yn}} for
	 * a patrol path of n points on it. The enemy will walk towards each
	 * point until they are near it, then start heading towards the next
	 * one.
	 * @param path The patrol path in the format described above.
	 */
	public void setPatrolPath(int[][] path) {
		patrolPoints = path;
	}
}
