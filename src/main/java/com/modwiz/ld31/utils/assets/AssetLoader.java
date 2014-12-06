package com.modwiz.ld31.utils.assets;

import com.google.common.base.Optional;
import com.modwiz.ld31.world.GameWorld;
import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.entities.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public abstract class AssetLoader {
    /**
     * Returns the current implementation of AssetLoader
     * @return a concrete AssetLoader implementation
     */
    public static AssetLoader getAssetLoader() {
        return new Basic();
    }

    /**
     * Load a generic resource object
     * @param resourcePath The path to the generic resource object
     * @return
     */
    public abstract Optional<Resource<?>> getResource(String resourcePath);

    /**
     * Load a BufferedImage
     * @param imagePath The path to the image file
     * @return A buffered image with the image loaded
     */
    public abstract Optional<Resource<BufferedImage>> getBufferedImage(String imagePath);

    /**
     * Load a level from specified path
     * @param levelPath The path to the level
     * @return An object representing a game level
     */
    public abstract Optional<Resource<GameWorld>> getLevel(String levelPath);
	
	 /**
     * Load a level from specific file; for the level editor
     * @param levelFile The file that holds the level data
     * @return An object representing a game level
     */
    public abstract Optional<Resource<GameWorld>> getLevelFromFile(File levelFile);
	
	 /**
     * Load a level from specific file; for the level editor
     * @param levelFile The file that holds the level data
     * @return An object representing a game level
     */
    public abstract void saveLevel(File levelFile, GameWorld world);
	
	/**
	* Creates a parse string for saving a GameObject.
	* @param object The object to be converted into a String.
	* @return The object in String form.
	*/
	public abstract String writeGameObject(GameObject object);
	
	/**
	* Creates a GameObject from a parse string; reverse of writeGameObject.
	* @param string The string to be converted into a GameObject.
	* @return The String in object form.
	*/
	public abstract GameObject readGameObject(Dimension parent, String string);

    static class Basic extends AssetLoader{		
		
		private final char SEP;
		private final String
			PLAYER = "TEUZOD",
			CREATURE = "TEUQOD",
			BLOCK = "TEPZOW",
			OBJECT = "LEPZOQ"
		;
		
		public Basic() {
			SEP = '$';
		}
		
        @Override
        public Optional<Resource<?>> getResource(String resourcePath) {
            return Optional.absent();
        }

        @Override
        public Optional<Resource<BufferedImage>> getBufferedImage(String imagePath) {
            try {
                BufferedImage image = ImageIO.read(new File(imagePath));
                return Optional.of(new Resource<BufferedImage>(imagePath, image));
            } catch (IOException e) {
                // Don't think we need to throw an exception here.
                return Optional.absent();
            }
        }

        @Override
        public Optional<Resource<GameWorld>> getLevel(String levelPath) {
            return Optional.absent();
        }
		
		@Override
        public Optional<Resource<GameWorld>> getLevelFromFile(File levelFile) {
            return Optional.absent();
        }
		
		@Override
		public void saveLevel(File file, GameWorld level) {
			
		}
		
		@Override
		public String writeGameObject(GameObject object) {
			if (object instanceof Player) {
				Player p = (Player)object;
				return PLAYER + SEP + p.getX() + SEP +
					p.getY() + SEP + p.getWidth() + SEP +
					p.getHeight() + SEP + p.getHealth();
			} else if (object instanceof Creature) {
				Creature p = (Creature)object;
				return CREATURE + SEP + p.getX() + SEP +
					p.getY() + SEP + p.getWidth() + SEP +
					p.getHeight() + SEP + p.getHealth();
			} else if (object instanceof GameBlock) {
				GameBlock p = (GameBlock)object;
				return BLOCK + SEP + p.getX() + SEP +
					p.getY() + SEP + p.getWidth() + SEP +
					p.getHeight();
			} else {
				//Must be a game object then
				return OBJECT + SEP + object.getX() +
					SEP + object.getY();
			}
		}
		
		@Override
		public GameObject readGameObject(Dimension parent, String string) {
			String[] split = string.split(Pattern.quote("" + SEP));
			switch(split[0]) {
				case PLAYER:
					
					break;
				case CREATURE:
					
					break;
				case BLOCK:
					
					break;
				case OBJECT:
					return new GameObject(
						parent,
						Float.parseFloat(split[1]),
						Float.parseFloat(split[2])
					);
				default:
					System.err.println("Unrecognisable entity type: " + split[0]);
			}
			return null;
		}
    }
}
