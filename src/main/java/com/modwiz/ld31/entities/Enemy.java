package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.entities.draw.Animation;

/**
 * A generic baddie
 */
public class Enemy extends Creature {

    private int frame;
	private int patrolPoint;
    private int spawnX;
	private int timeOnPoint, timeOnPointMax;
    private boolean patrolPointOn;
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
		spawnX = (int) x;
		patrolPointOn = true;
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
        spawnX = (int) x;
		patrolPointOn = true;
		distanceNear = 50;
    }
	
	@Override
	public void update() {
		super.update();
		float distFromNextX = Math.abs(getX() - (patrolPointOn ? patrolPoint : spawnX));
			System.out.println("I am going to move towards it now");
			System.out.println("Distance from on X: " + distFromNextX);
        if (patrolPointOn) {

            getVelocity().set(0, -3);

		} else {
            getVelocity().set(0, 3);

        }
        if ((int)distFromNextX <= 5 && frame > 1){
            patrolPointOn = !patrolPointOn;
        }

        frame++;
    }

	
	/**
	 * Set the patrol path for the Enemy object.
	 * The path is in the format {{x0, y0}, {x1, y1,} ... {xn, yn}} for
	 * a patrol path of n points on it. The enemy will walk towards each
	 * point until they are near it, then start heading towards the next
	 * one.
	 * @param path The patrol path in the format described above.
	 */
	public void setPatrolPath(int path) {
		patrolPoint = path;
	}
}
