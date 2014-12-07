package com.modwiz.ld31.utils.assets;

import com.google.common.base.Optional;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Pretty names for assets
 * @param <T> The type of asset to be stored in the registry
 */
public class AssetRegistry<T> {
    private Map<String, Resource<T>> map = new HashMap<>();

    public static AssetRegistry<BufferedImage> bufferedImageRegistry = new AssetRegistry<>();

    public boolean registerAsset(String assetName, Resource<T> asset) {
        if (map.containsKey(assetName)) {
            return false; // Existing asset with that name.
        }
        if (asset == null) {
            return false; // No null assets.
        }
        map.put(assetName, asset);
        return true;
    }


    public Optional<T> getContent(String assetName) {
        if (!map.containsKey(assetName)) {
            return Optional.absent();
        }
        return Optional.of(map.get(assetName).getContent());
    }

    public Optional<String> getPath(String assetName) {
        if (!map.containsKey(assetName)) {
            return Optional.absent();
        }
        return Optional.of(map.get(assetName).getPath());
    }

    public Optional<Resource<T>> getAsset(String assetName) {
        if (!map.containsKey(assetName)) {
            return Optional.absent();
        }
        return Optional.of(map.get(assetName));
    }

    public boolean assetExists(String assetName) {
        return map.containsKey(assetName);
    }
}
