package com.modwiz.ld31.utils.assets;

/**
 * Represents a loaded asset resource
 * @param <T> The content type of the asset
 */
public class Resource<T> {
    private String path;
    private T content;

    public Resource(String path, T content) {
        this.path = path;
        this.content = content;
    }

    /**
     * Gets the base path used when loading the resource
     * @return A string representing the path to the resource.
     */
    public String getPath() {
        return path;
    }

    /**
     * Get the content stored in the resource
     * @return The resource's content
     */
    public T getContent() {
        return content;
    }
}
