package com.modwiz.ld31.utils.assets;


import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.modwiz.ld31.entities.*;
import com.modwiz.ld31.utils.assets.loaders.BufferedImageLoader;
import com.modwiz.ld31.utils.assets.loaders.GameWorldLoader;
import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.world.GameWorld;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.regex.Pattern;

/**
 * Uses a {@link com.google.common.cache.LoadingCache} to prevent excessive io and such
 */
public class CachedLoader extends AssetLoader {
    private LoadingCache<String, BufferedImage> bufferedImageCache;
    private LoadingCache<String, GameWorld> gameWorldLoadingCache;

    public CachedLoader() {
        bufferedImageCache = CacheBuilder.newBuilder().build(new BufferedImageLoader());
        gameWorldLoadingCache = CacheBuilder.newBuilder().build(new GameWorldLoader());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<Resource<T>> getResource(String resourcePath, Class<T> resourceType) {
        if (resourceType == BufferedImage.class) {
            return Optional.fromNullable(new Resource<>(resourcePath, (T) bufferedImageCache.getUnchecked(resourcePath)));
        } else if (resourceType == GameWorld.class) {
            return Optional.fromNullable(new Resource<>(resourcePath, (T) gameWorldLoadingCache.getUnchecked(resourcePath)));
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Resource<BufferedImage>> getBufferedImage(String imagePath) {
        return getResource(imagePath, BufferedImage.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Resource<GameWorld>> getLevel(String levelPath) {
        return getResource(levelPath, GameWorld.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Resource<GameWorld>> getLevelFromFile(File levelFile) {
        return getResource(levelFile.getPath(), GameWorld.class);
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
                writer.println("" + LevelString.DIM_START + LevelString.SEP + dim.getName());
                for (GameObject obj : dim.getObjects()) {
                    writer.println(writeGameObject(obj));
                }
                writer.println("" + LevelString.DIM_END);
            }
            writer.flush();
            writer.close();
            gameWorldLoadingCache.put(file.getPath(), level);
            System.out.println("Successfully saved level: " + file.getPath());
        } catch(Exception e) {
            System.err.println("Could not save level " + file.getPath());
        }
    }

    @Override
    public String writeGameObject(GameObject object) {
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
                    p.getHeight() + LevelString.SEP + p.isStaticBlock() + LevelString.SEP + p.getImageName();
        } else {
            //Must be a game object then
            return "" + LevelString.OBJECT + LevelString.SEP + object.getX() +
                    LevelString.SEP + object.getY();
        }
    }

    private String[] splitLine(String line) {
        return line.split(Pattern.quote("" + LevelString.SEP));
    }
    /**
     * {@inheritDoc}
     */
    @Override
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
                        Float.parseFloat(split[4]),
						Boolean.parseBoolean(split[5]),
						AssetRegistry.bufferedImageRegistry.getAsset(split[6]).get()
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
