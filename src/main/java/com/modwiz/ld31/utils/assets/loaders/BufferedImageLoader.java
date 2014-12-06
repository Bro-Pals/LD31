package com.modwiz.ld31.utils.assets.loaders;

import com.google.common.cache.CacheLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class BufferedImageLoader extends CacheLoader<String, BufferedImage>{
    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage load(String key) throws Exception {
        BufferedImage image = ImageIO.read(new File(key));
        return image;
    }
}
