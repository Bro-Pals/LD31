package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.entities.draw.Animation;
import java.awt.geom.Rectangle2D;
import horsentpmath.Vector2;
import java.awt.Graphics;

/**
 * A generic baddie
 */
public class Enemy extends Creature {

    private int frame;
	private int patrolPoint;
    private int spawnX;
	private int timeOnPoint, timeOnPointMax, normalLOS, sneakLOS; // field of view in radians
    private boolean patrolPointOn, spottedPlayer;
    /** How close the Enemy needs to be near a point before they start going to the next one*/
	private int distanceNear;
	private float fieldOfView;
	private Player player;

    /**
     * Creates a new Enemy instance
     *
     * @param parent Dimension in the game world to bde loaded into
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
		normalLOS = 400;
		sneakLOS = 100;
		fieldOfView = (float)(Math.PI / 6);
		player = Player.getSingleton();
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
		timeOnPoint = 0;
		timeOnPointMax = 20; // frames
		normalLOS = 300;
		sneakLOS = 100;
		fieldOfView = (float)(Math.PI / 6);
		player = Player.getSingleton();
    }
	
	/**
     * Creates a new entity with an {@link com.modwiz.ld31.entities.draw.Animation} and a Patrol path
     *
     * @param parent Dimension in the game world to be loaded into
     * @param x      The x position of the entity
     * @param y      The y position of the entity
     * @param w      The bounding width of the entity
     * @param h      The bounding height of the entity
     * @param health The initial health value for the entity
     * @param animString   The animation for the entity
	 * @param initialPatrolPoint The initial patrol point.
	 * @param finalPatrolPoint The final patrol point.
	 * @param weaponRange This enemy's weapon's range
	 * @param weaponDamage This enemy's weapon's damage
	 * @param weaponCooldown This enemy's weapon's cooldown
     * @see com.modwiz.ld31.entities.Creature
     */
    public Enemy(Dimension parent, float x, float y, float w, float h, double health, String animString, int initialPatrolPoint, 
			int finalPatrolPoint, double weaponRange, double weaponDamage, int weaponCooldown) {
        super(parent, x, y, w, h, health, animString);
		setWeapon(new Weapon(this, weaponRange, weaponDamage, weaponCooldown));
		System.out.println("WEAPON RANGE: "  + getWeapon().getRange());
        setPatrolPath(initialPatrolPoint, finalPatrolPoint);
		patrolPointOn = true;
		distanceNear = 50;
		timeOnPoint = 0;
		timeOnPointMax = 20; // frames
		normalLOS = 300;
		sneakLOS = 100;
		fieldOfView = (float)(Math.PI / 6);
		player = Player.getSingleton();
		spottedPlayer = false;
    }
	
	public boolean canSeePlayer() {
		//only can track the player when it's in the same dimension
		if (player == null || getParent() != player.getParent()) {
			return false;
		}
		
		// do some checks
		// maybe check:
		// * Is the player in the direction Enemy is facing
		if (isFacingRight()) {
			if (player.getX() + (player.getWidth()/2) < (getX() + (getWidth()/2))) {
				return false;
			}
		} else {
			if (player.getX() + (player.getWidth()/2) > (getX() + (getWidth()/2))) {
				return false;
			}
		} 
		
		// * Is the player close enough to the Enemy
		float diffX = player.getX() + (player.getWidth()/2) - (getX() + getWidth()/2);
		float diffY = player.getY() - getY();
		float distanceFromSqred = (diffX * diffX) + (diffY * diffY);
		if (player.isSneaking() && !spottedPlayer) { // don't reduce it if already seen when he starts sneaking
			if (distanceFromSqred > sneakLOS * sneakLOS) {
				return false;
			}
		}
		if (distanceFromSqred > normalLOS * normalLOS) {
			return false;
		}
		
		// * is the player in the enemy's field of view?
		Vector2 enemyLOS = new Vector2(isFacingRight() ? 1 : -1, 0);
		Vector2 posVect = (Vector2)((new Vector2(diffX, diffY)).normalize());
		double angle = Math.acos(enemyLOS.dot(posVect));
		if (angle > 0 && angle > fieldOfView) {
			//System.out.println(angle + " > " + fieldOfView);
			return false;
		}
		
		// * Does the enemy have line of sight of the player?
		for (GameObject obj : getParent().getObjects()) {
			if (obj != this && obj instanceof GameBlock && !(obj instanceof Creature)) {
				GameBlock bl = (GameBlock) obj;
				if ((new Rectangle2D.Float(bl.getX(), bl.getY(), bl.getWidth(), bl.getHeight())).intersectsLine(
					(int)getX(), (int)getY(), (int)player.getX(), (int)player.getY())) {
						return false;
				}
			}			
		}
		
		return true;
	}
	
	@Override
	public void render(Graphics g, float camX, float camY) {
        super.render(g, camX, camY);
        Vector2 enemyLOS = new Vector2(isFacingRight() ? 1 : -1, 0);

        int startX = (int) (getX() - camX + (getWidth()/2));
        int startY = (int) (getY() - camY);
        int endX = (int) ((enemyLOS.getX() * sneakLOS) - camX);
        if (player.isSneaking() && !spottedPlayer){
            g.drawLine(startX, startY, startX + endX, startY + (int) ((Math.sin(fieldOfView) * sneakLOS) - camY));
            g.drawLine(startX, startY, startX + endX, startY + (int) (-(Math.sin(fieldOfView) * sneakLOS) - camY));
            g.drawLine(startX, startY, startX + ((facingRight) ? sneakLOS : -sneakLOS), startY);
        } else {

            g.drawLine(startX, startY, startX + endX, startY + (int) ((Math.sin(fieldOfView) * normalLOS) - camY));
            g.drawLine(startX, startY, startX + endX, startY + (int) (-(Math.sin(fieldOfView) * normalLOS) - camY));
            g.drawLine(startX, startY, startX + ((facingRight) ? normalLOS : -normalLOS), startY);
        }
	}
	
	@Override
	public void update() {
		super.update();
        float distFromNextX = distFrom(getX());
		if (distFrom(getX()-1)<distFromNextX){
            getVelocity().set(0,-3);
        } else {
            getVelocity().set(0,3);
        }
        if ((int)distFromNextX <= 5 && frame > 3){
            patrolPointOn = !patrolPointOn;
        }
        if(canSeePlayer()){
			spottedPlayer = true;
            if(Math.abs(getX()-player.getX())>(Math.abs(getX()-1-player.getX()))){
                getVelocity().set(0,-3);
            } else {
                getVelocity().set(0,3);
            }
			if (Math.abs((getX() + (getWidth()/2)) - (player.getX() + (player.getWidth()/2))) < getWeapon().getRange()) {
				useWeapon((int)(player.getX() + (player.getWidth()/2)), (int)(player.getY() + 10));
			}
        } else {
			spottedPlayer = false;
		}
        frame++;

    }

    public float distFrom(float x){
        return Math.abs(x - (patrolPointOn ? spawnX + patrolPoint : spawnX));
    }

	
	/**
	 * Set the patrol path for the Enemy object.
	 * The path is in the format {{x0, y0}, {x1, y1,} ... {xn, yn}} for
	 * a patrol path of n points on it. The enemy will walk towards each
	 * point until they are near it, then start heading towards the next
	 * one.
	 * @param initialPoint The spawn point for this Enemy.
	 * @param finalPoint The destination point for this Enemy.
	 */
	public void setPatrolPath(int initialPoint, int finalPoint) {
		if (finalPoint<initialPoint) {
			throw new IllegalStateException("Initial must be less than Final");
		}
		spawnX = initialPoint;
		patrolPoint = finalPoint;
	}
	
	/**
	 * Gets the initial patrol path point.
	 * @return the initial patrol path point.
	 */
	public int getInitialPoint() {
		return spawnX;
	}
	 
	 /**
	 * Gets the final patrol path point.
	 * @return the final patrol path point.
	 */
	public int getFinalPoint() {
		return patrolPoint;
	}
	
	@Override
	public Object clone() {
		//TO DO : Copy the animation
		Enemy e;
		if (getAnimationString()!=null) {
			e = new Enemy(null, getX(), getY(), getWidth(), getHeight(), (int)getHealth(), getAnimationString(), getInitialPoint(),getFinalPoint(), getWeapon().getRange(), getWeapon().getDamage(), getWeapon().getCooldown());
		} else {
			e = new Enemy(null, getX(), getY(), getWidth(), getHeight(), (int)getHealth());
			e.setPatrolPath(getInitialPoint(),getFinalPoint());
		}
		e.setName(getName());
		return e;
	}
}
