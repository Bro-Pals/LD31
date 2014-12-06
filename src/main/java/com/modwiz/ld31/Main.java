package com.modwiz.ld31;

import com.modwiz.ld31.world.GameObject;

public class Main {

    public static void main(String[] args) {
		if (args.length == 1 && args[0].equals("LEVEL_EDITOR")) {
			LevelEditorMain editor = new LevelEditorMain();
			//The level editor is now OK
		} else {
			System.out.println("This shows that it is working!");
			GameObject object = new GameObject(0, 0);
			object.getVelocity().set(0, 20);
			object.getVelocity().printVector();
		}
       System.out.println("This shows that it is working!");
        /*GameObject object = new GameObject();
        object.getVelocity().set(0, 20);
        object.getVelocity().printVector();
        */
    }
}
