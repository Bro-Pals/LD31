package com.modwiz.ld31.utils.assets;

import com.modwiz.ld31.utils.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Provides the InputStream for {@link com.modwiz.ld31.utils.assets.AssetLoader}
 */
public class ProviderBase {
    private File rootDir;

    public ProviderBase(File rootDir) {
        this.rootDir = rootDir;
    }

    @Nullable
    public InputStream provideAsset(String path) {
        try {
            return new FileInputStream(new File(rootDir, path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
