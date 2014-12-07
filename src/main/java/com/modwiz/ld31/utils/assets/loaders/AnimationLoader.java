package com.modwiz.ld31.utils.assets.loaders;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.modwiz.ld31.utils.annotations.Nullable;
import com.modwiz.ld31.utils.assets.AssetLoader;
import com.modwiz.ld31.entities.draw.Animation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.awt.Graphics;

/**
 * The class for loading {@link com.modwiz.ld31.entities.draw.Animation} class through
 * text files. The purpose of the text files is to store the information about the
 * animation that one is about to load. 
 */
public class AnimationLoader implements ILoader<Animation> {
	
	private Cache<String, Animation> cache = CacheBuilder.newBuilder().build();
	
	private AssetLoader assetLoader;
	
	public AnimationLoader(AssetLoader al) {
		assetLoader = al;
	}
	
	 /**
     * {@inheritDoc}
     */
	 @Override
	public Animation getContent(final InputStream stream, final String key) {
		try {
            Animation animatation = cache.get(key, new Callable<Animation>() {
                @Override
                @Nullable
                public Animation call() throws Exception {
                    Animation anim = null;
					BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
					ArrayList<BufferedImage[]> list = new ArrayList<>();
					String nextLine;
					while((nextLine = reader.readLine()) != null) {
						if (!nextLine.startsWith("#")) { // otherwise a comment
							String[] tokens = nextLine.split(" ");
							BufferedImage entireImage = assetLoader.loadAsset(BufferedImage.class, tokens[1]);
							int imgNum = Integer.parseInt(tokens[2]);
							int imgWidth = Integer.parseInt(tokens[3]);
							int imgHeight = Integer.parseInt(tokens[4]);
							BufferedImage[] track = new BufferedImage[imgNum];
							for (int i=0; i<imgNum; i++) {
								track[i] = entireImage.getSubimage(i * imgWidth, 0, imgWidth, imgHeight);
							}
							if (tokens[5].equals("true")) {
								for (int i=0; i<track.length; i++) {
									track[i] = flipImage(track[i], true);
								}
							}
						}
					}
					anim = new Animation((BufferedImage[][])list.toArray(), 7);
                    return anim;
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
	}
	
	private static BufferedImage flipImage(BufferedImage img, boolean horiz) {
		BufferedImage flipped = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TRANSLUCENT);
		Graphics g = flipped.getGraphics();
		if (horiz) {
			for (int i=0; i<img.getWidth(); i++) {
				g.drawImage(img.getSubimage(img.getWidth() - i - 1, 0, 1, img.getHeight()), i, 0, null);
			}
		} else {
			for (int i=0; i<img.getHeight(); i++) {
				g.drawImage(img.getSubimage(0, img.getHeight() - i - 1, img.getWidth(), 1), 0, i, null);
			}
		}
		return flipped;
	}
}