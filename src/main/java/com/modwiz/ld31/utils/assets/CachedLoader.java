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
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<Resource<T>> getResource(String resourcePath, Class<T> resourceType) {
        if (resourceType == BufferedImage.class) {
            return Optional.fromNullable(new Resource<>(resourcePath, (T) bufferedImageCache.getUnchecked(resourcePath)));
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Resource<BufferedImage>> getBufferedImage(String imagePath) {
        return Optional.fromNullable(new Resource<>(imagePath, bufferedImageCache.getUnchecked(imagePath)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Resource<GameWorld>> getLevel(String levelPath) {
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Resource<GameWorld>> getLevelFromFile(File levelFile) {
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveLevel(File levelFile, GameWorld world) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String writeGameObject(GameObject object) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameObject readGameObject(Dimension parent, String string) {
        return null;
    }
}
