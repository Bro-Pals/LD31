package com.modwiz.ld31.entities;

import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.entities.draw.Animation;

/**
 * Created by Starbuck on 12/6/2014.
 */
public class Player extends Creature {
    public Player(Dimension parent, float x, float y, float w, float h) {
        super(parent, x, y, w, h);
    }

    public Player(Dimension parent, float x, float y, float w, float h, int hp, Animation anim) {
        super(parent, x, y, w, h, hp, anim);
    }
}
