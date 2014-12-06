package com.modwiz.ld31;

import com.modwiz.ld31.entities.Creature;
import com.modwiz.ld31.entities.GameBlock;
import com.modwiz.ld31.entities.draw.Animation;
import com.modwiz.ld31.leveleditor.LevelEditorMain;
import com.modwiz.ld31.utils.assets.*;
import com.modwiz.ld31.world.*;
import com.modwiz.ld31.world.Dimension;
import horsentp.simpledrawing.DrawWindow;
import java.awt.event.KeyEvent;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

	// Values for tracking what keys are pressed at the moment
	private static boolean w = false;
	private static boolean a = false;
	private static boolean s = false;
	private static boolean d = false;
	private static boolean shift = false;

    public static void main(String[] args) {
		if (args.length == 1 && args[0].equals("LEVEL_EDITOR")) {
			LevelEditorMain editor = new LevelEditorMain();
			editor.init();
			editor.setVisible(true);
			//The level editor is now OK
		} else {
			System.out.println("This shows that it is working!");
			DrawWindow window = new DrawWindow("Super cool assasian game with next gen graphics", 800, 600, false);
			
			// loading all the animations
			BufferedImage playerMoving = null;
			try {
				playerMoving = AssetLoader.getAssetLoader().getBufferedImage("assets/img/playerMove.png").get().getContent();
			} catch(NullPointerException npe) {
				System.out.println("Oh no it's a " + npe.toString());
			} catch(Exception e) {
				System.out.println("Problem loading images " + e.toString());
				System.exit(0);
			}
			
			BufferedImage[][] playerAnimations = new BufferedImage[4][3];
			if (playerMoving != null) {
				for (int i=0; i<3; i++) {
					playerAnimations[0][i] = playerMoving.getSubimage(i * 80, 0, 80, 120);
				}
			}
			Animation playerAnim = new Animation(playerAnimations, 8);
			
			Dimension firstDimension = new Dimension();
			GameBlock firstBlock = new GameBlock(firstDimension, 50, 50, 100, 100);
			firstBlock.getVelocity().set(0, 2);
			firstBlock.getVelocity().set(1, 1);

			Creature testCreature = new Creature(firstDimension, 50, 400, 500, 100, 1, playerAnim);
			testCreature.getVelocity().set(0, 2);
			testCreature.getAcceleration().set(1, 1); // gravity!

			firstDimension.getObjects().add(testCreature); // our first block!!
			GameWorld world = new GameWorld();
			world.addDimension(firstDimension);
			world.setActiveDimension(firstDimension);
			
			long start = System.currentTimeMillis();
			int millisBetweenFrames = 30;

			int width = window.getRawFrame().getWidth();
			int height = window.getRawFrame().getHeight();

			window.getRawFrame().setBackground(Color.black);
		
			
			while(window.exists()) {
				start = System.currentTimeMillis();
				Graphics g = window.getDrawGraphics();
			
			
				// handle key events
				KeyEvent keyEvent;
				while ((keyEvent = window.nextKeyPressedEvent()) != null) {
					changeKey(keyEvent.getKeyCode(), true);
				}
				while ((keyEvent = window.nextKeyReleasedEvent()) != null) {
					changeKey(keyEvent.getKeyCode(), false);
				}
				
				
				world.updateDimension();
				
				g.clearRect(0, 0, width, height);
				world.renderDimension(g);
			
				window.showBuffer(g);

				
				long diff = System.currentTimeMillis() - start;
				if (diff < millisBetweenFrames) {
					try { Thread.sleep(millisBetweenFrames - diff); } catch(Exception e) { }
				}
			}
		}
    }
	
	private static void changeKey(int keyCode, boolean value) {
		switch(keyCode) {
			case KeyEvent.VK_W: w = value; break;
			case KeyEvent.VK_A: a = value; break;
			case KeyEvent.VK_S: s = value; break;
			case KeyEvent.VK_D: d = value; break;
			case KeyEvent.VK_SHIFT: shift = value; break;
		}
	}
}
