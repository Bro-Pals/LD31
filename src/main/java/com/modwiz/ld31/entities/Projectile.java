package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;

public class Projectile extends GameBlock {

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
    public Projectile(Dimension parent, float x, float y, float w, float h) {
        super(parent, x, y, w, h);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        super.update(); // collision stuff from GameBlock
        if (getVelocity().getX() != 0) {

        }
    }

}
