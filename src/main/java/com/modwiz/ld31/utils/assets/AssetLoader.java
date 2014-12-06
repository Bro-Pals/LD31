package com.modwiz.ld31.utils.assets;

import com.google.common.base.Optional;
import com.modwiz.ld31.world.GameWorld;
import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.entities.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public abstract class AssetLoader {
    /**
     * Returns the current implementation of AssetLoader
     * @return a concrete AssetLoader implementation
     */
    public static AssetLoader getAssetLoader() {
        return new CachedLoader();
    }

    /**
     * Load a generic resource object
     * @param resourcePath The path to the generic resource object
     * @return
     */
    public abstract Optional<Resource<?>> getResource(String resourcePath, Class resourceType);

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
		/* Like this so that the level files 
			can't be changed by humans */
			PLAYER = "TEUZOD",
			CREATURE = "TEUQOD",
			BLOCK = "TEPZOW",
			OBJECT = "LEPZOQ",
			ENEMY = "LEPZVQ",
			DIM_START = "TYPZOW",
			DIM_END = "TWPZOW",
			DIM_INITIAL_DIM = "TBPZOW"
		;
		
		public Basic() {
			SEP = '$';
		}
		
        @Override
        public Optional<Resource<?>> getResource(String resourcePath, Class resourceType) {
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
            return getLevelFromFile(new File(levelPath));
        }
		
		@Override
        public Optional<Resource<GameWorld>> getLevelFromFile(File levelFile) {
            try {
				GameWorld dieWelt = new GameWorld();
				BufferedReader rdr = new BufferedReader(new FileReader(levelFile));
				String currentLine;
				boolean readingDimension = false;
				Dimension current = null;
				while ( (currentLine = rdr.readLine()) != null) {
					//Initial processing
					String[] ln = splitLine(currentLine);
					if (ln[0].equals(DIM_START)) {
						readingDimension = true;
						current = new Dimension();
						current.setName(ln[1]);
					} else if (ln[0].equals(DIM_END)) {
						readingDimension = false;
						dieWelt.addDimension(current);
						current = null;
					} else {
						//Next step processing
						if (readingDimension) {
							//Parse a game object
							current.addObject(readGameObject(current, currentLine));
							System.out.println("Read " + currentLine);
						}
					}
				}
				rdr.close();
				System.out.println("Successfully loaded level: " + levelFile.getPath());
				return Optional.of(new Resource<GameWorld>(levelFile.getPath(), dieWelt));
			} catch(Exception e) {
				System.err.println("Could not load level "  + levelFile.getPath() + ": " + e.toString());
				return Optional.absent();
			}
        }
		
		@Override
		public void saveLevel(File file, GameWorld level) {
			//Write stuff
			try {
				if (level==null) {
					System.err.println("ERROR: Attempting to save a null level (Should never happen)");
				}
				PrintWriter writer = new PrintWriter(file);
				/* Go through the dimensions and write each one */
				Dimension[] dimensions = level.getDimensions();
				for (Dimension dim : dimensions) {
					writer.println(DIM_START + SEP + dim.getName());
					for (GameObject obj : dim.getObjects()) {
						writer.println(writeGameObject(obj));
					}
					writer.println(DIM_END);
				}
				writer.flush();
				writer.close();
				System.out.println("Successfully saved level: " + file.getPath());
			} catch(Exception e) {
				System.err.println("Could not save level " + file.getPath());
			}
		}
		
		@Override
		public String writeGameObject(GameObject object) {
			if (object instanceof Player) {
				Player p = (Player)object;
				return PLAYER + SEP + p.getX() + SEP +
					p.getY() + SEP + p.getWidth() + SEP +
					p.getHeight() + SEP + p.getHealth();
			} else if (object instanceof Enemy) {
				Enemy e = (Enemy)object;
				return ENEMY + SEP + e.getX() + SEP +
					e.getY() + SEP + e.getWidth() + SEP +
					e.getHeight() + SEP + e.getHealth();
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
		
		private String[] splitLine(String line) {
			return line.split(Pattern.quote("" + SEP));
		}
		
		@Override
		public GameObject readGameObject(Dimension parent, String string) {
			String[] split = splitLine(string);
			switch(split[0]) {
				case PLAYER:
					return new Player(
						parent,
						Float.parseFloat(split[1]),
						Float.parseFloat(split[2]),
						Float.parseFloat(split[3]),
						Float.parseFloat(split[4]),
						(int)Double.parseDouble(split[5]),
						null
					);
				case ENEMY:
					return new Enemy(
						parent,
						Float.parseFloat(split[1]),
						Float.parseFloat(split[2]),
						Float.parseFloat(split[3]),
						Float.parseFloat(split[4]),
						(int)Double.parseDouble(split[5]),
						null
					);
				case CREATURE:
					return new Creature(
						parent,
						Float.parseFloat(split[1]),
						Float.parseFloat(split[2]),
						Float.parseFloat(split[3]),
						Float.parseFloat(split[4]),
						(int)Double.parseDouble(split[5]),
						null
					);
				case BLOCK:
					return new GameBlock(
						parent,
						Float.parseFloat(split[1]),
						Float.parseFloat(split[2]),
						Float.parseFloat(split[3]),
						Float.parseFloat(split[4])
					);
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
