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

    /**
     * Load a level from specified path
     * @param levelPath The path to the level
     * @return An object representing a game level
     */
    public abstract Optional<Resource<GameWorld>> getLevel(String levelPath);
	
	 /**
     * Load a level from specific file; for the level editor
     * @param levelFile The file that holds the level data
     * @return An object representing a game level
     */
    public abstract Optional<Resource<GameWorld>> getLevelFromFile(File levelFile);
	
	 /**
     * Load a level from specific file; for the level editor
     * @param levelFile The file that holds the level data
     * @return An object representing a game level
     */
    public abstract void saveLevel(File levelFile, GameWorld world);
	
	/**
	* Creates a parse string for saving a GameObject.
	* @param object The object to be converted into a String.
	* @return The object in String form.
	*/
	public abstract String writeGameObject(GameObject object);
	
	/**
	* Creates a GameObject from a parse string; reverse of writeGameObject.
	* @param string The string to be converted into a GameObject.
	* @return The String in object form.
	*/
	public abstract GameObject readGameObject(Dimension parent, String string);

}
