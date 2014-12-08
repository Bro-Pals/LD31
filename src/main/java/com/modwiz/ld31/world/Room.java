package com.modwiz.ld31.world;

import com.modwiz.ld31.entities.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {
    private List<GameObject> objects = new ArrayList<>();

    public void addObject(GameObject object) {
        objects.add(object);
    }

    public void removeObject(GameObject object) {
        objects.remove(object);
    }

    public void renderRoom(Graphics g, float camX, float camY) {
        for (GameObject object : objects) {
            object.render(g, camX, camY);
        }
    }

    public void updateRoom() {
        List<GameObject> deadList = new ArrayList<>();
        for (int i=0; i<objects.size(); i++) {
            GameObject obj = objects.get(i);
            if (obj.isDead()) {
                objects.remove(i);
                continue;
            }
            obj.update();
        }
    }

    public List<GameObject> getObjects() {
        //return Collections.unmodifiableList(objects);
        return objects;
    }
}
