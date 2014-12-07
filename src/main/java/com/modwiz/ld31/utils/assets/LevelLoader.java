package com.modwiz.ld31.utils.assets;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import com.modwiz.ld31.entities.*;
import com.modwiz.ld31.utils.assets.AssetLoader;
import com.modwiz.ld31.world.*;
import java.util.regex.Pattern;

/**
	Loads levels.
*/
public class LevelLoader {
	
    public static GameWorld getLevel(String levelPath) {
        return getLevelFromFile(new File(levelPath));
    }
    
    public static GameWorld getLevelFromFile(File levelFile) {
        try {
            GameWorld dieWelt = new GameWorld();
            BufferedReader rdr = new BufferedReader(new FileReader(levelFile));
            String currentLine;
            boolean readingDimension = false;
            Dimension current = null;
            while ( (currentLine = rdr.readLine()) != null) {
                //Initial processing
                String[] ln = splitLine(currentLine);
                if (ln[0].equals(LevelString.DIM_START.string)) {
                    readingDimension = true;
                    current = new Dimension();
                    current.setName(ln[1]);
                } else if (ln[0].equals(LevelString.DIM_END.string)) {
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
            return dieWelt;
        } catch(Exception e) {
            System.err.println("Could not load level "  + levelFile.getPath() + ": " + e.toString());
            return null;
        }
    }
    
    public static void saveLevel(File file, GameWorld level) {
        //Write stuff
        try {
            if (level==null) {
                System.err.println("ERROR: Attempting to save a null level (Should never happen)");
            }
            PrintWriter writer = new PrintWriter(file);
				/* Go through the dimensions and write each one */
            Dimension[] dimensions = level.getDimensions();
            for (Dimension dim : dimensions) {
                writer.println("" + LevelString.DIM_START + LevelString.SEP + dim.getName());
                for (GameObject obj : dim.getObjects()) {
                    writer.println(writeGameObject(obj));
                }
                writer.println("" + LevelString.DIM_END);
            }
            writer.flush();
            writer.close();
            System.out.println("Successfully saved level: " + file.getPath());
        } catch(Exception e) {
            System.err.println("Could not save level " + file.getPath());
        }
    }

    public static String writeGameObject(GameObject object) {
        if (object instanceof Player) {
            Player p = (Player)object;
            return "" + LevelString.PLAYER + LevelString.SEP + p.getX() + LevelString.SEP +
                    p.getY() + LevelString.SEP + p.getWidth() + LevelString.SEP +
                    p.getHeight() + LevelString.SEP + p.getHealth();
        } else if (object instanceof Enemy) {
            Enemy e = (Enemy)object;
            return "" + LevelString.ENEMY + LevelString.SEP + e.getX() + LevelString.SEP +
                    e.getY() + LevelString.SEP + e.getWidth() + LevelString.SEP +
                    e.getHeight() + LevelString.SEP + e.getHealth();
        } else if (object instanceof Creature) {
            Creature p = (Creature)object;
            return "" + LevelString.CREATURE + LevelString.SEP + p.getX() + LevelString.SEP +
                    p.getY() + LevelString.SEP + p.getWidth() + LevelString.SEP +
                    p.getHeight() + LevelString.SEP + p.getHealth();
        } else if (object instanceof GameBlock) {
            GameBlock p = (GameBlock)object;
            return "" + LevelString.BLOCK + LevelString.SEP + p.getX() + LevelString.SEP +
                    p.getY() + LevelString.SEP + p.getWidth() + LevelString.SEP +
                    p.getHeight() + LevelString.SEP + p.isStaticBlock() + LevelString.SEP + p.getImageForString();
        } else {
            //Must be a game object then
            return "" + LevelString.OBJECT + LevelString.SEP + object.getX() +
                    LevelString.SEP + object.getY();
        }
    }
	
    private static String[] splitLine(String line) {
        return line.split(Pattern.quote("" + LevelString.SEP));
    }
   
    /**
	 * Creates a GameObject from a string found in a GameWorld file, essentially converting it from text
	 * format into an Object format.
	 * @param parent The Dimension that this game object is in.
	 * @param 
	 */
    public static GameObject readGameObject(Dimension parent, String string) {
        String[] split = splitLine(string);
        switch(LevelString.parseEncoded(split[0])) {
            case PLAYER:
				System.out.println("Loading Player");
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
				System.out.println("Loading Enemy");
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
				System.out.println("Loading Creature");
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
				System.out.println("Loading Block");
                return new GameBlock(
                        parent,
                        Float.parseFloat(split[1]),
                        Float.parseFloat(split[2]),
                        Float.parseFloat(split[3]),
                        Float.parseFloat(split[4]),
						Boolean.parseBoolean(split[5]),
						split[6]
                );
            case OBJECT:
				System.out.println("Loading Object");
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