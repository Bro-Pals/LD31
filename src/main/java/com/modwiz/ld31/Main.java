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
import java.io.File;
import java.util.regex.Pattern;

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
			preloadAssets(); //For the level editor
			LevelEditorMain editor = new LevelEditorMain();
			editor.init();
			editor.setVisible(true);
			//The level editor is now OK
		} else {
			System.out.println("This shows that it is working!");
			DrawWindow window = new DrawWindow("Super cool assasian game with next gen graphics", 800, 600, false);

			AssetLoader assetLoader = AssetLoader.getSingleton();
			// loading all the animations
			BufferedImage playerMoving = null;
			BufferedImage enemy0Moving = null;
			BufferedImage playerStab = null;
			BufferedImage projectileImage = null;
			BufferedImage texture0 = null;
			BufferedImage texture1 = null;
			
			Animation playerAnim = null;
			Animation enemyAnim = null;
			
			try {
				playerMoving = assetLoader.loadAsset(BufferedImage.class,"img/playerMove.png");
				enemy0Moving = assetLoader.loadAsset(BufferedImage.class,"img/enemy0Move.png");
				playerStab = assetLoader.loadAsset(BufferedImage.class,"img/playerStab.png");
				projectileImage = assetLoader.loadAsset(BufferedImage.class,"img/golden_projectile.png");;
				texture0 = assetLoader.loadAsset(BufferedImage.class,"img/texture0.png");
				texture1 = assetLoader.loadAsset(BufferedImage.class,"img/texture1.png");
				
				playerAnim = assetLoader.loadAsset(Animation.class, "anim/player.animation");
				enemyAnim = assetLoader.loadAsset(Animation.class, "anim/enemy0.animation");
				System.out.println(playerAnim);
				System.out.println(enemyAnim);
			} catch(NullPointerException npe) {
				System.out.println("Oh no it's a " + npe.toString());
			} catch(Exception e) {
				System.out.println("Problem loading images " + e.toString());
				System.exit(0);
			}

            GameWorld world = LevelLoader.getLevel("assets/Levels/levelTest.txt");
            world.setActiveDimension("Dimension1");

			Player player = new Player(world.getDimensions()[0], 100, 20, 65, 120, 50, playerAnim);
			player.getVelocity().set(0, 2);
			player.getAcceleration().set(1, 1); // gravity!


			
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
					player.jump(15);
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
			case KeyEvent.VK_SPACE: // Space should jump too.
			case KeyEvent.VK_W: w = value; break;
			case KeyEvent.VK_A: a = value; break;
			case KeyEvent.VK_S: s = value; break;
			case KeyEvent.VK_D: d = value; break;
			case KeyEvent.VK_SHIFT: shift = value; break;
		}
	}
	
	private static void preloadAssets() {
		//For the level editor
		AssetLoader al = AssetLoader.getSingleton();
		File imageDir = new File("assets/img");
		String[] files = imageDir.list();
		for (int i=0; i<files.length; i++) {
			String extension = files[i].split(Pattern.quote("."))[1];
			if (extension.equals("png")) {
				al.loadAsset(BufferedImage.class, "img/" + files[i]);
			}
		}
		
		File animDir = new File("assets/anim");
		files = animDir.list();
		for (int i=0; i<files.length; i++) {
			String extension = files[i].split(Pattern.quote("."))[1];
			if (extension.equals("animation")) {
				al.loadAsset(Animation.class, "anim/" + files[i]);
			}
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
