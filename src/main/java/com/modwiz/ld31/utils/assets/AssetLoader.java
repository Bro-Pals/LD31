package com.modwiz.ld31.utils.assets;

import com.google.common.base.Optional;
import com.modwiz.ld31.entities.GameObject;
import com.modwiz.ld31.world.Dimension;
import com.modwiz.ld31.world.GameWorld;

import java.awt.image.BufferedImage;
import java.io.File;

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
    public abstract <T> Optional<Resource<T>> getResource(String resourcePath, Class<T> resourceType);

    /**
     * Load a BufferedImage
     * @param imagePath The path to the image file
     * @return A buffered image with the image loaded
     */
    public abstract Optional<Resource<BufferedImage>> getBufferedImage(String imagePath);


}
