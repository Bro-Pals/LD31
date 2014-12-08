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
        return getLevelFromFile(new File("assets/" + levelPath));
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
						if (!currentLine.equals("NO_LOAD")) {
							GameObject obj = readGameObject(current, currentLine);
							current.addObject(obj);
						}
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
                    if (!(obj instanceof Player)) {
						writer.println(writeGameObject(obj));
					}
                }
                writer.println("" + LevelString.DIM_END);
            }
            writer.flush();
            writer.close();
            System.out.println("Successfully saved level: " + file.getPath());
        } catch(Exception e) {
            System.err.println("Could not save level " + file.getPath());
			e.printStackTrace();
        }
    }

    public static String writeGameObject(GameObject object) {
        if (object instanceof Player) {
			return "NO_LOAD";
        } else if (object instanceof Enemy) {
            Enemy e = (Enemy)object;
            return "" + LevelString.ENEMY + LevelString.SEP + e.getX() + LevelString.SEP +
                    e.getY() + LevelString.SEP + e.getWidth() + LevelString.SEP +
                    e.getHeight() + LevelString.SEP + e.getHealth() + LevelString.SEP + e.getAnimationString() +
					LevelString.SEP + e.getInitialPoint() + LevelString.SEP + e.getFinalPoint() +
					LevelString.SEP + e.getWeapon().getRange() + LevelString.SEP + e.getWeapon().getDamage() + 
					LevelString.SEP + e.getWeapon().getCooldown();
        } else if (object instanceof Creature) {
            Creature p = (Creature)object;
            return "" + LevelString.CREATURE + LevelString.SEP + p.getX() + LevelString.SEP +
                    p.getY() + LevelString.SEP + p.getWidth() + LevelString.SEP +
                    p.getHeight() + LevelString.SEP + p.getHealth() + LevelString.SEP + p.getAnimationString();
        } else if (object instanceof TextBlock) {
            TextBlock p = (TextBlock)object;
            return "" + LevelString.TEXT + LevelString.SEP + p.getX() + LevelString.SEP +
                    p.getY() + LevelString.SEP + p.getWidth() + LevelString.SEP +
                    p.getHeight() + LevelString.SEP + p.getTextLabel() + LevelString.SEP + p.getFontSize();
        } else if (object instanceof MessageBlock) {
            MessageBlock p = (MessageBlock)object;
            String str = "" + LevelString.MESSAGE + LevelString.SEP + p.getX() + LevelString.SEP +
                    p.getY() + LevelString.SEP + p.getWidth() + LevelString.SEP +
                    p.getHeight() + LevelString.SEP + p.getImageForString();
			str += ("" + LevelString.SEP + (p.getMessages().length-1));
			for (int i=0; i<p.getMessages().length; i++) {
				str += ("" + LevelString.SEP + p.getMessages()[i]);
			}
			return str;
        } else if (object instanceof RadiationSucker) {
            RadiationSucker p = (RadiationSucker)object;
            return "" + LevelString.RADIATION_SUCKER + LevelString.SEP + p.getX() + LevelString.SEP +
                    p.getY();
        }  else if (object instanceof DNARepairCell) {
            DNARepairCell p = (DNARepairCell)object;
            return "" + LevelString.DNA_REPAIR_CELL + LevelString.SEP + p.getX() + LevelString.SEP +
                    p.getY();
        }  else if (object instanceof DimensionChangeBlock) {
            DimensionChangeBlock p = (DimensionChangeBlock)object;
            return "" + LevelString.DIMENSION_CHANGE_BLOCK + LevelString.SEP + p.getX() + LevelString.SEP +
                    p.getY() + LevelString.SEP + p.getWidth() + LevelString.SEP +
                    p.getHeight()
					+ LevelString.SEP + p.getJumpingToDimension() + LevelString.SEP + p.getImageForString();
        }  else if (object instanceof GameBlock) {
            GameBlock p = (GameBlock)object;
            return "" + LevelString.BLOCK + LevelString.SEP + p.getX() + LevelString.SEP +
                    p.getY() + LevelString.SEP + p.getWidth() + LevelString.SEP +
                    p.getHeight() + LevelString.SEP + p.isStaticBlock() + LevelString.SEP + p.getImageForString() + LevelString.SEP + p.getCanCollide();
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
            case ENEMY:
				System.out.println("Loading Enemy");
                return new Enemy(
                        parent,
                        Float.parseFloat(split[1]),
                        Float.parseFloat(split[2]),
                        Float.parseFloat(split[3]),
                        Float.parseFloat(split[4]),
                        (int)Double.parseDouble(split[5]),
                        split[6],
						Integer.parseInt(split[7]),
						Integer.parseInt(split[8]),
						Double.parseDouble(split[9]),
						Double.parseDouble(split[10]),
						Integer.parseInt(split[11])
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
                        split[6]
                );
			case TEXT:
				System.out.println("Loading Text");
                return new TextBlock(
                        parent,
                        Float.parseFloat(split[1]),
                        Float.parseFloat(split[2]),
                        Float.parseFloat(split[3]),
                        Float.parseFloat(split[4]),
						split[5],
						Integer.parseInt(split[6])
                );
			case MESSAGE:
				System.out.println("Loading Message");
                MessageBlock mb = new MessageBlock(
                        parent,
                        Float.parseFloat(split[1]),
                        Float.parseFloat(split[2]),
                        Float.parseFloat(split[3]),
                        Float.parseFloat(split[4]),
						null,
						split[5]
                );
				//Parse the message block
				int msgCount = Integer.parseInt(split[6]);
				String[] messages = new String[split.length-6];
				for (int i=0; i<msgCount; i++) {
					messages[i] = split[6+i];
				}
				mb.setMessages(messages);
				return mb;
			case DIMENSION_CHANGE_BLOCK:
				System.out.println("Loading Dimension Change Block");
                DimensionChangeBlock dcb= new DimensionChangeBlock(
					parent,
					Float.parseFloat(split[1]),
					Float.parseFloat(split[2]),
					Float.parseFloat(split[3]),
					Float.parseFloat(split[4]),
					split[5],
					split[6]
				);
				return dcb;
			case DNA_REPAIR_CELL:
				System.out.println("Loading DNA Repair cell");
                return new DNARepairCell(
                        parent,
                        Float.parseFloat(split[1]),
                        Float.parseFloat(split[2])
                );
			case RADIATION_SUCKER:
				System.out.println("Loading Radiation Sucker");
                return new RadiationSucker(
                        parent,
                        Float.parseFloat(split[1]),
                        Float.parseFloat(split[2])
                );
            case BLOCK:
				System.out.println("Loading Block");
                GameBlock gb =  new GameBlock(
                        parent,
                        Float.parseFloat(split[1]),
                        Float.parseFloat(split[2]),
                        Float.parseFloat(split[3]),
                        Float.parseFloat(split[4]),
						Boolean.parseBoolean(split[5]),
						split[6]
                );
				gb.setCanCollide(Boolean.parseBoolean(split[7]));
				return gb;
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