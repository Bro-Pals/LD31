package com.modwiz.ld31.utils.assets.loaders;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.modwiz.ld31.utils.annotations.Nullable;
import com.modwiz.ld31.entities.draw.Animation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * The class for loading {@link com.modwiz.ld31.entities.draw.Animation} class through
 * text files. The purpose of the text files is to store the information about the
 * animation that one is about to load. 
 */
public class AnimationLoader implements ILoader<Animation> {
	
	private Cache<String, Animation> cache = CacheBuilder.newBuilder().build();
	
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
                    return anim;
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
	}
}