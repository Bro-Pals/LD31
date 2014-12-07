package com.modwiz.ld31.utils.assets.loaders;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.modwiz.ld31.utils.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class BufferedImageLoader implements ILoader<BufferedImage>{
    private Cache<String, BufferedImage> cache = CacheBuilder.newBuilder().build();

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage getContent(final InputStream stream, final String key) {
        try {
            BufferedImage image = cache.get(key, new Callable<BufferedImage>() {
                @Override
                @Nullable
                public BufferedImage call() throws Exception {
                    BufferedImage image = ImageIO.read(stream);
                    return image;
                }
            });
            return image;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Set<String> getKeys() {
        return cache.asMap().keySet();
    }
}
