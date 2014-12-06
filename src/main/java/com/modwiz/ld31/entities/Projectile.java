package com.modwiz.ld31.entities;

import com.modwiz.ld31.utils.assets.AssetLoader;
import com.modwiz.ld31.utils.math.VectorUtils;
import com.modwiz.ld31.world.Dimension;
import horsentpmath.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile extends GameBlock {

    private BufferedImage projectileImageRight;
    private BufferedImage projectileImageLeft;
	private Creature source;
	private double range, distanceTraveled;

    /**
     * Represents a projectile
     *
     * @param parent The dimension for this object to be loaded into
     * @param x      The initial x position of this projectile
     * @param y      The initial y position of this projectile
     * @param w      The width of this projectile
     * @param h      The height of this projectile
     * @see com.modwiz.ld31.entities.GameObject
     */
    public Projectile(Creature source, Dimension parent, float x, float y, float w, float h, BufferedImage right, BufferedImage left,
			float range) {
        super(parent, x, y, w, h);
		setCanCollide(false);
		this.source = source;
		projectileImageRight = right;
		projectileImageLeft = left;
		this.range = range;
		this.distanceTraveled = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        super.update(); // collision stuff from GameBlock
       // System.out.println(isDead());
	   distanceTraveled += getVelocity().getMagnitude();
	   if (distanceTraveled > range) {
			setDead(true);
	   }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollide(GameBlock other) {
        // what happens when there is a collision with the other block
        if (!isDead() && other instanceof Creature && other != source) {
            Creature creature = (Creature) other;
            creature.damage(15);
            setDead(true);
			Vector2 pushNormal = (Vector2) getVelocity().normalize();
			float pushX = pushNormal.getX() * 6;
			float pushY = pushNormal.getY() * 6;
            creature.getVelocity().set(0, creature.getVelocity().getX() + pushX);
            creature.getVelocity().set(1, creature.getVelocity().getY() + pushY);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Graphics g, float camX, float camY) {
		BufferedImage projectileImage = getVelocity().getX() > 0 ? projectileImageRight : projectileImageLeft;
        if (projectileImage != null) {
            g.drawImage(projectileImage, (int)(getX()-camX), (int)(getY()-camY), (int)getWidth(), (int)getHeight(), null);
        } else {
            super.render(g, camX, camY);
        }
        // bounding box
        g.setColor(Color.BLACK);
        g.drawRect((int)(getX()-camX), (int)(getY()-camY), (int)getWidth(), (int)getHeight());
    }
}
