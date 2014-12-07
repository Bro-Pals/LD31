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

    /**
     * The image texture registry, should be able to be shared.
     */
    public static AssetRegistry<BufferedImage> bufferedImageRegistry = new AssetRegistry<>();

    /**
     * Registers an asset into the asset loader
     * @param assetName The name of the asset in the loader
     * @param asset The {@link com.modwiz.ld31.utils.assets.Resource} storing the asset
     * @return Whether or not the registration was successful
     */
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


    /**
     * Gets the content stored in an asset directly
     * @param assetName The name of the asset
     * @return The {@link T} loaded and stored in the asset
     */
    public Optional<T> getContent(String assetName) {
        if (!map.containsKey(assetName)) {
            return Optional.absent();
        }
        return Optional.of(map.get(assetName).getContent());
    }

    /**
     * Gets the path of an asset directly
     * @param assetName The name of the asset
     * @return The path to the asset's content
     */
    public Optional<String> getPath(String assetName) {
        if (!map.containsKey(assetName)) {
            return Optional.absent();
        }
        return Optional.of(map.get(assetName).getPath());
    }

    /**
     * Gets an asset from the asset registry
     * @param assetName The name of the asset
     * @return A {@link com.modwiz.ld31.utils.assets.Resource} asset
     */
    public Optional<Resource<T>> getAsset(String assetName) {
        if (!map.containsKey(assetName)) {
            return Optional.absent();
        }
        return Optional.of(map.get(assetName));
    }

    /**
     * Checks if an asset by the specified name exists
     * @param assetName The name of the asset
     * @return Whether or not the asset exists
     */
    public boolean assetExists(String assetName) {
        return map.containsKey(assetName);
    }
	
	/**
	 * Returns a list of all the keys for all the loaded BufferedImages.
	 * @return The array of loaded images.
	 */
	public String[] getAssetKeys() {
		return (String[])map.keySet().toArray(new String[0]);
	}
}
