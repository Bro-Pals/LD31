package com.modwiz.ld31.utils.assets;


import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.modwiz.ld31.entities.GameObject;
import com.modwiz.ld31.utils.assets.loaders.BufferedImageLoader;
import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.world.GameWorld;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Uses a {@link com.google.common.cache.LoadingCache} to prevent excessive io and such
 */
public class CachedLoader extends AssetLoader {
    private LoadingCache<String, BufferedImage> bufferedImageCache;

    public CachedLoader() {
        bufferedImageCache = CacheBuilder.newBuilder().build(new BufferedImageLoader());
    }


    /**
     * Load a generic resource object
     *
     * @param resourcePath The path to the generic resource object
     * @return
     */
    @Override
    public Optional<Resource<?>> getResource(String resourcePath, Class resourceType) {
        if (resourceType == BufferedImage.class) {
            return (Optional<Resource<?>>) getBufferedImage(resourcePath);
        }
        return Optional.absent();
    }

    /**
     * Load a BufferedImage
     *
     * @param imagePath The path to the image file
     * @return A buffered image with the image loaded
     */
    @Override
    public Optional<Resource<BufferedImage>> getBufferedImage(String imagePath) {
        return Optional.fromNullable(new Resource<BufferedImage>(imagePath, bufferedImageCache.getUnchecked(imagePath)));
    }

    /**
     * Load a level from specified path
     *
     * @param levelPath The path to the level
     * @return An object representing a game level
     */
    @Override
    public Optional<Resource<GameWorld>> getLevel(String levelPath) {
        return Optional.absent();
    }

    /**
     * Load a level from specific file; for the level editor
     *
     * @param levelFile The file that holds the level data
     * @return An object representing a game level
     */
    @Override
    public Optional<Resource<GameWorld>> getLevelFromFile(File levelFile) {
        return Optional.absent();
    }

    /**
     * Load a level from specific file; for the level editor
     *
     * @param levelFile The file that holds the level data
     * @param world
     * @return An object representing a game level
     */
    @Override
    public void saveLevel(File levelFile, GameWorld world) {

    }

    /**
     * Creates a parse string for saving a GameObject.
     *
     * @param object The object to be converted into a String.
     * @return The object in String form.
     */
    @Override
    public String writeGameObject(GameObject object) {
        return null;
    }

    /**
     * Creates a GameObject from a parse string; reverse of writeGameObject.
     *
     * @param parent
     * @param string The string to be converted into a GameObject.
     * @return The String in object form.
     */
    @Override
    public GameObject readGameObject(Dimension parent, String string) {
        return null;
    }
}
