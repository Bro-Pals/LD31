package com.modwiz.ld31;

import com.modwiz.ld31.entities.*;
import com.modwiz.ld31.entities.draw.Animation;
import com.modwiz.ld31.leveleditor.LevelEditorMain;
import com.modwiz.ld31.utils.assets.*;
import com.modwiz.ld31.utils.math.VectorUtils;
import com.modwiz.ld31.world.*;
import com.modwiz.ld31.world.Dimension;
import horsentp.simpledrawing.DrawWindow;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
		loadAssets();
		if (args.length == 1 && args[0].equals("LEVEL_EDITOR")) {
			LevelEditorMain editor = new LevelEditorMain();
			editor.init();
			editor.setVisible(true);
			//The level editor is now OK
		} else {
			System.out.println("This shows that it is working!");
			DrawWindow window = new DrawWindow("Super cool assasian game with next gen graphics", 800, 600, false);
			
			// loading all the animations
			Resource<BufferedImage> playerMoving = null;
			Resource<BufferedImage> enemy0Moving = null;
			Resource<BufferedImage> playerStab = null;
			Resource<BufferedImage> projectileImage = null;
			Resource<BufferedImage> texture0 = null;
			Resource<BufferedImage> texture1 = null;
			try {
				projectileImage = AssetLoader.getAssetLoader().getBufferedImage("assets/img/golden_projectile.png").get();
				enemy0Moving = AssetLoader.getAssetLoader().getBufferedImage("assets/img/enemy0Move.png").get();
				playerMoving = AssetLoader.getAssetLoader().getBufferedImage("assets/img/playerMove.png").get();
				playerStab = AssetLoader.getAssetLoader().getBufferedImage("assets/img/playerStab.png").get();
				texture0 = AssetLoader.getAssetLoader().getBufferedImage("assets/img/texture0.png").get();
				texture1 = AssetLoader.getAssetLoader().getBufferedImage("assets/img/texture1.png").get();
			} catch(NullPointerException npe) {
				System.out.println("Oh no it's a " + npe.toString());
			} catch(Exception e) {
				System.out.println("Problem loading images " + e.toString());
				System.exit(0);
			}
			
			BufferedImage[][] playerAnimations = new BufferedImage[4][];
			BufferedImage[][] enemyAnimations = new BufferedImage[2][4];
			/*
				track | description
				0     | walk right
				1     | walk left
				2     | stab right
				3     | stab left
			*/
			playerAnimations[0] = new BufferedImage[3];
			playerAnimations[1] = new BufferedImage[3];
			playerAnimations[2] = new BufferedImage[6];
			playerAnimations[3] = new BufferedImage[6];
			if (playerMoving != null) {
				for (int i=0; i<3; i++) {
					playerAnimations[0][i] = playerMoving.getContent().getSubimage(i * 80, 0, 80, 120);
					playerAnimations[1][i] = flipImage(playerAnimations[0][i], true);
				}
			}
			if (playerStab != null) {
				for (int i=0; i<5; i++) {
					playerAnimations[2][i] = playerStab.getContent().getSubimage(i * 80, 0, 80, 120);
					playerAnimations[3][i] = flipImage(playerAnimations[2][i], true);
				}
			}
			playerAnimations[2][5] = playerAnimations[2][5];
			playerAnimations[3][5] = playerAnimations[3][5];
			
			if (enemy0Moving != null) {
				for (int i=0; i<4; i++) {
					enemyAnimations[0][i] = enemy0Moving.getContent().getSubimage(i * 60, 0, 60, 100);
					enemyAnimations[1][i] = flipImage(enemyAnimations[0][i], true);
				}
			}

			
			Animation playerAnim = new Animation(playerAnimations, 7);
			Animation enemyAnim = new Animation(enemyAnimations, 7);
			
			Dimension firstDimension = new Dimension("First");
			GameBlock firstBlock = new GameBlock(firstDimension, 50, 400, 300, 30, true, texture0);
			GameBlock secondBlock = new GameBlock(firstDimension, 375, 400, 100, 30, true, texture0);

			Player player = new Player(firstDimension, 100, 20, 65, 120, 50, playerAnim);
			player.getVelocity().set(0, 2);
			player.getAcceleration().set(1, 1); // gravity!

			Enemy enemy = new Enemy(firstDimension, 450, 80, 60, 100, 50, enemyAnim);
			enemy.setPatrolPath(new int[][]{{100, 350}, {400, 350}});
			enemy.givePlayerRef(player);
			//enemy.getVelocity().set(0, -10f);
			enemy.getAcceleration().set(1, 1); // gravity!
			
			firstDimension.getObjects().add(player); // our first block!!
			firstDimension.getObjects().add(firstBlock); // our first block!!
			firstDimension.getObjects().add(secondBlock);
			firstDimension.getObjects().add(new GameBlock(firstDimension, 475, 395, 50, 20, true, texture0));
			firstDimension.getObjects().add(new GameBlock(firstDimension, 525, 389, 50, 20, true, texture0));
			firstDimension.getObjects().add(new GameBlock(firstDimension, 575, 383, 50, 20, true, texture0));
			firstDimension.getObjects().add(enemy);

			GameWorld world = new GameWorld();
			world.addDimension(firstDimension);
			world.setActiveDimension("First");
			
			long start = System.currentTimeMillis();
			int millisBetweenFrames = 30;

			int width = window.getRawFrame().getWidth();
			int height = window.getRawFrame().getHeight();

			window.getRawFrame().setBackground(Color.gray);

			while(window.exists()) {
				start = System.currentTimeMillis();
				Graphics g = window.getDrawGraphics();
				Point mousePosition = new Point((int)(MouseInfo.getPointerInfo().getLocation().getX() -
							window.getRawFrame().getLocation().getX() + camX), 
							(int)(MouseInfo.getPointerInfo().getLocation().getY() -
							window.getRawFrame().getLocation().getY()+ camY));
			
				// handle key events
				KeyEvent keyEvent;
				while ((keyEvent = window.nextKeyPressedEvent()) != null) {
					changeKey(keyEvent.getKeyCode(), true);
				}
				while ((keyEvent = window.nextKeyReleasedEvent()) != null) {
					changeKey(keyEvent.getKeyCode(), false);
				}
				MouseEvent mouseEvent;
				while((mouseEvent = window.nextMousePressedEvent() ) != null) {
					if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
						player.useWeapon((int)mousePosition.getX(), (int)mousePosition.getY());
					}
				}
				
				// make the player face wherever the mouse is pointing
				player.setFacingRight(player.getX() + (player.getWidth()/2) < mousePosition.getX());
				
				// moving and stuff
				if (d && !a) {
					player.getVelocity().set(0, 4);
				} else if (a && !d) {
					player.getVelocity().set(0, -4);
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
	
	private static void loadAssets() {
		loadImage("Brick", "assets/img/brick.png");
		loadImage("Metal", "assets/img/metal.png");
		loadImage("Wood", "assets/img/wood.png");
	}
	
	private static void loadImage(String key, String path) {
		AssetRegistry.bufferedImageRegistry.registerAsset(
			key,
			AssetLoader.getAssetLoader().getBufferedImage(path).get()
		);
		if (AssetRegistry.bufferedImageRegistry.assetExists(key)) {
			System.out.println("Successfully loaded asset: " + key + ", " + path);
		} else {
			System.err.println("ERROR: Unable to load asset: " + key + ", " + path);
		}
	}
	
	private static void changeKey(int keyCode, boolean value) {
		switch(keyCode) {
			case KeyEvent.VK_SPACE: // Space should jump too.
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
