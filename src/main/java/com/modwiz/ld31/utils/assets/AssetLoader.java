package com.modwiz.ld31.utils.assets;

public abstract class AssetLoader {
    /**
     * Returns the current implementation of AssetLoader
     * @return a concrete AssetLoader implementation
     */
    public static AssetLoader getAssetLoader() {
        return new CachedLoader();

}

}
