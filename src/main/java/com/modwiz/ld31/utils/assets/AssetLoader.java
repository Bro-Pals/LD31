package com.modwiz.ld31.utils.assets;

import com.google.common.base.Optional;
import com.modwiz.ld31.world.GameWorld;

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
     * Load a level from specified path
     * @param levelPath The path to the level
     * @return An object representing a game level
     */
    public abstract Optional<Resource<GameWorld>> getLevel(String levelPath);

    static class Basic extends AssetLoader{

        @Override
        public Optional<Resource<?>> getResource(String resourcePath) {
            return Optional.absent();
        }

        @Override
        public Optional<Resource<GameWorld>> getLevel(String levelPath) {
            return Optional.absent();
        }
    }
}
