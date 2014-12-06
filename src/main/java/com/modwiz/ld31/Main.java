package com.modwiz.ld31;

import com.modwiz.ld31.entities.*;
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
	//The position of the camera in the game world
	private static float camX = 0;
	private static float camY = 0; 

	// Ratio of our 1 to real 9.8
	private static final double GRAVITY_RATIO = 0.1020408163265306;

	// Projectile speed ratio, gravity -> speed for projectiles is important

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
					playerAnimations[1][i] = flipImage(playerAnimations[0][i], true);
				}
			}
			Animation playerAnim = new Animation(playerAnimations, 8);
			
			Dimension firstDimension = new Dimension();
			GameBlock firstBlock = new GameBlock(firstDimension, 50, 400, 300, 30, true);
			GameBlock secondBlock = new GameBlock(firstDimension, 375, 400, 200, 30, true);
			Projectile projectile = new Projectile(firstDimension, 70, 300, 16, 16);
			projectile.getVelocity().set(0, 1);

			Player player = new Player(firstDimension, 100, 20, 65, 120, 50, playerAnim);
			player.getVelocity().set(0, 2);
			player.getAcceleration().set(1, 1); // gravity!

			firstDimension.getObjects().add(player); // our first block!!
			firstDimension.getObjects().add(firstBlock); // our first block!!
			firstDimension.getObjects().add(secondBlock);
			firstDimension.getObjects().add(projectile);

			GameWorld world = new GameWorld();
			world.addDimension(firstDimension);
			world.setActiveDimension(firstDimension.getName());
			
			long start = System.currentTimeMillis();
			int millisBetweenFrames = 30;

			int width = window.getRawFrame().getWidth();
			int height = window.getRawFrame().getHeight();

			window.getRawFrame().setBackground(Color.gray);

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
				
				// moving and stuff
				if (d && !a) {
					player.getVelocity().set(0, 3);
				} else if (a && !d) {
					player.getVelocity().set(0, -3);
				} else if (!a && !d) {
					player.getVelocity().set(0, 0);
				}
				if (w) {
					if (player.isGrounded()) {
						System.out.println("JUMP");
						player.getVelocity().set(1, -14); // jumping
					}
				}
				player.setSneaking(shift);
				
				world.updateDimension();
				g.clearRect(0, 0, width, height);
				world.renderDimension(g, camX, camY);
			
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
	
	private static BufferedImage flipImage(BufferedImage img, boolean horiz) {
		BufferedImage flipped = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TRANSLUCENT);
		Graphics g = flipped.getGraphics();
		if (horiz) {
			for (int i=0; i<img.getWidth(); i++) {
				g.drawImage(img.getSubimage(img.getWidth() - i - 1, 0, 1, img.getHeight()), i, 0, null);
			}
		} else {
			for (int i=0; i<img.getHeight(); i++) {
				g.drawImage(img.getSubimage(0, img.getHeight() - i - 1, img.getWidth(), 1), 0, i, null);
			}
		}
		return flipped;
	}
}
