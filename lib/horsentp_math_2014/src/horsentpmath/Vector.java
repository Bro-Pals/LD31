
package horsentpmath;

/**
 * Has a magnitude and a direction.
 * @author Jonathon
 */
public interface Vector {
    
    /**
     * Gets the component of this Vector at the index.
     * @param index the index of the component.
     * @return the value of the component at the index.
     */
    public float get(int index);
    
    /**
     * Sets the component of this Vector at the index.
     * @param index the index of the component.
     * @param value the new value of the component
     */
    public void set(int index, float value);
    
    /**
     * @return a vector that is the same direction as this Vector, but the magnitude is 1.
     */
    public Vector normalize();
    
    /**
     * Adds this Vector to the given vector and returns the result.
     * @param vector the vector to add to this one.
     * @return the resultant vector
     */
    public Vector add(Vector vector);
    
    /**
     * Gets the dot product of this Vector to the given vector.
     * @param vector the vector to dot with this vector
     * @return the dot product of this vector and <code>vector</code>
     */
    public float dot(Vector vector);
    
    /**
     * @return  the number of components this Vector has.
     */
    public int getComponentCount();
    
    /**
     * @return the value of this Vector's components.
     */
    public float[] getComponents();
    
    /**
     * @return the magnitude of this Vector.
     */
    public float getMagnitude();
    
    /**
     * Prints this Vector to System.out, good for debugging.
     */
    public void printVector();
}
