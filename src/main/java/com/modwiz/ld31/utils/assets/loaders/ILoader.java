package com.modwiz.ld31.utils.assets.loaders;

import com.modwiz.ld31.utils.annotations.Nullable;

import java.io.InputStream;

/**
 * A loader for the {@link com.modwiz.ld31.utils.assets.AssetLoader}
 */
public interface ILoader<T> {
    /**
     * Returns a loaded asset of type {@link T} from the {@link java.io.InputStream}
     * @param stream The {@link java.io.InputStream} to load the asset from
     * @param key The asset's key in the store
     * @return A loaded instance of {@link T}
     */
    @Nullable
    T getContent(final InputStream stream, final String key);
}
