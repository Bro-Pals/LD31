package com.modwiz.ld31;

import com.modwiz.ld31.entities.GameBlock;
import com.modwiz.ld31.leveleditor.LevelEditorMain;
import com.modwiz.ld31.utils.assets.*;
import com.modwiz.ld31.world.*;
import horsentp.simpledrawing.DrawWindow;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
		if (args.length == 1 && args[0].equals("LEVEL_EDITOR")) {
			LevelEditorMain editor = new LevelEditorMain();
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
			
			BufferedImage[][] playerAnimations = new BufferedImage[4][];
			if (playerMoving != null) {
				for (int i=0; i<3; i++) {
					playerAnimations[0][i] = playerMoving.getSubimage(i * 80, 0, 80, 120);
				}
			}
			
			Dimension firstDimension = new Dimension();
			GameBlock firstBlock = new GameBlock(firstDimension, 50, 50, 100, 100);
			firstBlock.getVelocity().set(0, 2);
			firstBlock.getVelocity().set(1, 1);
			firstDimension.getObjects().add(firstBlock); // our first block!!
			GameWorld world = new GameWorld();
			world.addDimension(firstDimension);
			world.setActiveDimension(firstDimension);
			
			long start = System.currentTimeMillis();
			int millisBetweenFrames = 30;
			
			while(window.exists()) {
				start = System.currentTimeMillis();			
				Graphics g = window.getDrawGraphics();
			
				world.updateDimension();
				world.renderDimension(g);
			
				window.showBuffer(g);
				
				long diff = System.currentTimeMillis() - start;
				if (diff < millisBetweenFrames) {
					try { Thread.sleep(millisBetweenFrames - diff); } catch(Exception e) { }
				}
			}
		}
    }
}
