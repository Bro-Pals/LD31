package com.modwiz.ld31.utils.assets.loaders;

import com.google.common.cache.CacheLoader;
import com.modwiz.ld31.entities.*;
import com.modwiz.ld31.utils.assets.LevelString;
import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.world.GameWorld;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Pattern;

public class GameWorldLoader extends CacheLoader<String, GameWorld> {

    /**
     * {@inheritDoc}
     */
    @Override
    public GameWorld load(String key) throws Exception {
        return getLevelFromFile(new File(key));
    }

    public GameWorld getLevelFromFile(File levelFile) {
        try {
            GameWorld dieWelt = new GameWorld();
            BufferedReader rdr = new BufferedReader(new FileReader(levelFile));
            String currentLine;
            boolean readingDimension = false;
            Dimension current = null;
            while ( (currentLine = rdr.readLine()) != null) {
                //Initial processing
                String[] ln = splitLine(currentLine);
                if (ln[0].equals(LevelString.DIM_START)) {
                    readingDimension = true;
                    current = new Dimension();
                    current.setName(ln[1]);
                } else if (ln[0].equals(LevelString.DIM_END)) {
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

    private String[] splitLine(String line) {
        return line.split(Pattern.quote("" + LevelString.SEP));
    }

    public GameObject readGameObject(Dimension parent, String string) {
        String[] split = splitLine(string);
        switch(LevelString.parseEncoded(split[0])) {
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
