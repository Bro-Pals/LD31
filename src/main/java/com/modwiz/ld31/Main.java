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
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
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

	private static GameWorld world;
	private static int jumpCooldown;

	public static GameWorld getCurrentWorld() { return world; }
	
	// Ratio of our 1 to real 9.8
	public static final double GRAVITY_RATIO = 1.1;
	private static boolean manualCam = false;
	private static int manualCamCooldown;
	private static final float camMoveRate = 4;
	private static final AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f);

	public static void main(String[] args) {
		if (args.length == 1 && args[0].equals("LEVEL_EDITOR")) {
			preloadAssets(); //For the level editor
			LevelEditorMain editor = new LevelEditorMain();
			editor.init();
			editor.setVisible(true);
			editor.postInit();
			//The level editor is now OK
		} else {
			GameWorld level1 = LevelLoader.getLevel("Levels/level1.txt");
			GameWorld level2 = LevelLoader.getLevel("Levels/level2.txt");
			GameWorld level3 = LevelLoader.getLevel("Levels/level3.txt");
			GameWorld testLevel = LevelLoader.getLevel("Levels/testLevel.txt");
			ArrayList<GameWorld> worlds = new ArrayList<GameWorld>();
			worlds.add(level1);
			worlds.add(level2);
			worlds.add(level3);
			worlds.add(testLevel);
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
				
				enemyAnim = assetLoader.loadAsset(Animation.class, "anim/enemy0.animation");
				System.out.println(playerAnim);
				System.out.println(enemyAnim);
			} catch(NullPointerException npe) {
				System.out.println("Oh no it's a " + npe.toString());
			} catch(Exception e) {
				System.out.println("Problem loading images " + e.toString());
				System.exit(0);
			}

            world = level1;
            world.setActiveDimension("MainDimension");

			Player player = Player.getSingleton();
			player.setParent(world.getActiveDimension());
            world.getActiveDimension().addObject(player);
			player.getVelocity().set(0, 2);

			
			long start = System.currentTimeMillis();
			int millisBetweenFrames = 30;

			int width = window.getRawFrame().getWidth();
			int height = window.getRawFrame().getHeight();

			window.getRawFrame().setBackground(Color.gray);

			window.getRawFrame().addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {}

				@Override
				public void keyPressed(KeyEvent e) {
					keys[e.getKeyCode()] = true;
				}

				@Override
				public void keyReleased(KeyEvent e) {
					keys[e.getKeyCode()] = false;
				}
			});

			window.getRawFrame().addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//mouse[e.getButton()] = false;
				}

				@Override
				public void mousePressed(MouseEvent e) {
					mouse[e.getButton()] = true;
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					mouse[e.getButton()] = false;
				}

				@Override
				public void mouseEntered(MouseEvent e) {}

				@Override
				public void mouseExited(MouseEvent e) {}
			});

			int currentStartScreen = 0;
			boolean canChange = true;
			BufferedImage[] startScreens = new BufferedImage[] {
				assetLoader.loadAsset(BufferedImage.class, "img/introScreen1.png"),
				assetLoader.loadAsset(BufferedImage.class, "img/introScreen2.png"),
				assetLoader.loadAsset(BufferedImage.class, "img/introScreen3.png"),
				assetLoader.loadAsset(BufferedImage.class, "img/introScreen4.png")
			};
			
			boolean gameWon = false;
			
			while(window.exists()) {
				start = System.currentTimeMillis();
				if (currentStartScreen>=startScreens.length) {
					if(world.getActiveDimension() != player.getParent()){
						world.setActiveDimension(player.getParent().getName());
					}
					Graphics g = window.getDrawGraphics();
					Graphics2D g2d = null;
					if (g instanceof Graphics2D) {
						g2d = (Graphics2D) g;
					}

					g.clearRect(0, 0, width, height);

					Point mousePosition = new Point((int)(MouseInfo.getPointerInfo().getLocation().getX() -
								window.getRawFrame().getLocation().getX() + camX), 
								(int)(MouseInfo.getPointerInfo().getLocation().getY() -
								window.getRawFrame().getLocation().getY()+ camY));
					if (keys[KeyEvent.VK_S]) {
						player.cycleMessages();
					}

					player.setPreviewJump(true);
					if (keys[KeyEvent.VK_1]) {
						if (mouse[MouseEvent.BUTTON3] && jumpCooldown <= 0) {
							world.setActiveDimension("past");
							player.jumpDimension(world.getActiveDimension());
							jumpCooldown = 20;
						} else {
							world.renderDimension(g, camX, camY);
							Composite old = g2d.getComposite();
							g2d.setComposite(composite);
							world.getDimension("past").renderObjects(g, camX, camY);
							player.render(g, camX, camY);
							g2d.setComposite(old);
						}
					} else if (keys[KeyEvent.VK_2]) {
						if (mouse[MouseEvent.BUTTON3] && jumpCooldown <= 0) {
							world.setActiveDimension("MainDimension");
							player.jumpDimension(world.getActiveDimension());
							jumpCooldown = 20;
						} else {
							world.renderDimension(g, camX, camY);
							Composite old = g2d.getComposite();
							g2d.setComposite(composite);
							world.getDimension("MainDimension").renderObjects(g, camX, camY);
							player.render(g, camX, camY);
							g2d.setComposite(old);
						}
					} else if (keys[KeyEvent.VK_3]) {
						if (mouse[MouseEvent.BUTTON3] && jumpCooldown <= 0) {
							world.setActiveDimension("future");
							player.jumpDimension(world.getActiveDimension());
							jumpCooldown = 20;
						} else {
							world.renderDimension(g, camX, camY);
							Composite old = g2d.getComposite();
							g2d.setComposite(composite);
							world.getDimension("future").renderObjects(g, camX, camY);
							player.render(g, camX, camY);
							g2d.setComposite(old);
						}
					} else {
						player.setPreviewJump(false);
					}

					jumpCooldown--;


					changeKey();

					if (mouse[MouseEvent.BUTTON1]) {
						player.useWeapon((int)mousePosition.getX(), (int)mousePosition.getY());
					}
					
					// make the player face wherever the mouse is pointing
					player.setFacingRight(player.getX() + (player.getWidth()/2) < mousePosition.getX());
					if (player.isDead()) {
						player.setX(0);
						player.setY(0);
						player.setDead(false);
					}
					// moving and stuff
					if (d) {
						player.getVelocity().set(0, 8);
					} else if (a) {
						player.getVelocity().set(0, -8);
					}

					if (w) {
						player.jump(15);
					}

					doCameraUpdate(player, width, height);

					player.setSneaking(shift);
					
					player.setMessages(null); // in case we're no longer by a message block
					world.updateDimension();
					
					if (!player.isPreviewJump()) {
						world.renderDimension(g, camX, camY);
					}
				
					if (gameWon) {
						// if you win the game
					}
				
					window.showBuffer(g);

					
					long diff = System.currentTimeMillis() - start;
					if (diff < millisBetweenFrames) {
						try { Thread.sleep(millisBetweenFrames - diff); } catch(Exception e) { }
					}
					player.setPreviewJump(false);
				} else {
				/* Start screen code */
				if (mouse[MouseEvent.BUTTON1] && canChange) {
					currentStartScreen++;
					canChange = false;
				} else if (mouse[MouseEvent.BUTTON1] == false) {
					canChange = true;
				}
				Graphics g = window.getDrawGraphics();
				if (currentStartScreen<startScreens.length) {
					g.drawImage(startScreens[currentStartScreen], 0, 0, null);
				}
				window.showBuffer(g);
			}
		}
    }
	}

	private static void doCameraUpdate(final Player player, final int width, final int height) {
		if (keys[KeyEvent.VK_NUMPAD5]) {
			camX = player.getX() - (width / 2.0f);
			if (camX < 0) {
				camX = 0;
			}
			camY = player.getY() - (height / 2.0f);
		}

		if (keys[KeyEvent.VK_NUMPAD4]) {
			camX -= camMoveRate;
		}

		if (keys[KeyEvent.VK_NUMPAD6]) {
			camX += camMoveRate;
		}

		if (keys[KeyEvent.VK_NUMPAD0]) {
			if (manualCamCooldown <= 0) {
				manualCam = !manualCam;
				manualCamCooldown = 20;
			}
		}
		if (keys[KeyEvent.VK_NUMPAD2]) {
			camY += camMoveRate;
		}

		if (keys[KeyEvent.VK_NUMPAD8]) {
			camY -= camMoveRate;
		}

		if (keys[KeyEvent.VK_NUMPAD1]) {
			camY += camMoveRate;
			camX -= camMoveRate;
		}

		if (keys[KeyEvent.VK_NUMPAD3]) {
			camY += camMoveRate;
			camX += camMoveRate;
		}

		if (keys[KeyEvent.VK_NUMPAD7]) {
			camY -= camMoveRate;
			camX -= camMoveRate;
		}

		if (keys[KeyEvent.VK_NUMPAD9]) {
			camY -= camMoveRate;
			camX += camMoveRate;
		}
		manualCamCooldown--;

		if (!manualCam) {
			camX = player.getX() - (width / 2.0f);

			if (camX < 0) {
				camX = 0;
			} else if (camX > (width / 2.0)) {
				//System.out.println(camX);
			}

			if (camX < 0) {
				camX = 0;
			}
			camY = player.getY() - (height / 2.0f);
		}
	}

	/**
	 * Makes the game play in the given level.
	 * @param wrld path to the playing level relative to the assets directory.
	 */
	public GameWorld setPlayingLevel(GameWorld wrld, ArrayList<GameWorld> wrlds) {
		if (wrld!=null) {
			for (GameWorld g : wrlds){
                if(g != wrld){
                    for (Dimension dim : g.getDimensions()){
                        dim.removeObject(Player.getSingleton());
                    }
                }
            }
            wrld.getActiveDimension().addObject(Player.getSingleton());
		}
        return wrld;
	}

	private static final boolean[] keys = new boolean[65535];
	private static final boolean[] mouse = new boolean[10];

	private static void changeKey() {
		w = keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_W];
		a = keys[KeyEvent.VK_A];
		s = keys[KeyEvent.VK_S];
		d = keys[KeyEvent.VK_D];
		shift = keys[KeyEvent.VK_SHIFT];
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
