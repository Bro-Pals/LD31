package com.modwiz.ld31.entities;

import com.modwiz.ld31.utils.assets.AssetLoader;
import com.modwiz.ld31.utils.math.VectorUtils;
import com.modwiz.ld31.world.Dimension;
import horsentpmath.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile extends GameBlock {

    BufferedImage projectileImage;

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
        try {
            projectileImage = AssetLoader.getAssetLoader().getBufferedImage("assets/img/golden_projectile.png").get().getContent();
        } catch (IllegalStateException e) {
            System.err.println("Projectile image not found!");
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        super.update(); // collision stuff from GameBlock
       // System.out.println(isDead());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollide(GameBlock other) {
        // what happens when there is a collision with the other block
        if (other instanceof Player) {
            Player player = (Player) other;
            player.damage(15);
            setDead(true);
            Vector2 knockBack = (Vector2)getVelocity().normalize();
            knockBack = VectorUtils.multiplyScalar(20, knockBack);
            VectorUtils.setVector2(player.getVelocity(), (Vector2) player.getVelocity().add(knockBack));
            System.out.println(isDead());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Graphics g) {
        if (projectileImage != null) {
            g.drawImage(projectileImage, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
        } else {
            super.render(g);
        }
        // bounding box
        g.setColor(Color.BLACK);
        g.drawRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }
}
