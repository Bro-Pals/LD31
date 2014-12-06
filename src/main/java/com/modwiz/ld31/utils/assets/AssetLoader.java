package com.modwiz.ld31.utils.assets;

import com.google.common.base.Optional;
import com.modwiz.ld31.world.GameWorld;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

    static class Basic extends AssetLoader{
		
		private GameObjectInputOutput gameWorldLoader;
		
		public Basic() {
			gameWorldLoader = new GameObjectInputOutput();
		}
		
        @Override
        public Optional<Resource<?>> getResource(String resourcePath) {
            return Optional.absent();
        }

        @Override
        public Optional<Resource<BufferedImage>> getBufferedImage(String imagePath) {
            try {
                BufferedImage image = ImageIO.read(new File(imagePath));
                return Optional.of(new Resource<BufferedImage>(imagePath, image));
            } catch (IOException e) {
                // Don't think we need to throw an exception here.
                return Optional.absent();
            }
        }

        @Override
        public Optional<Resource<GameWorld>> getLevel(String levelPath) {
            return Optional.absent();
        }
		
		@Override
        public Optional<Resource<GameWorld>> getLevelFromFile(File levelFile) {
            return Optional.absent();
        }
		
		@Override
		public void saveLevel(File file, GameWorld level) {
			
		}
    }
}
