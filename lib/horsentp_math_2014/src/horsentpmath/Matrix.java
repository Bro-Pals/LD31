
package horsentpmath;

/**
 * A linear transformation on space.
 * <p>
 * Inverse equations taken from <a href="http://www.cg.info.hiroshima-cu.ac.jp/~miyazaki/knowledge/teche23.html">here</a>
 * @author Jonathon
 */
public interface Matrix {
    /**
     * Gets the component of this matrix at the index.
     * @param index the index of the component.
     * @return the value of the component at the index.
     */
    public float get(int index);
    
    /**
     * Sets the component of this Matrix at the index.
     * @param index the index of the component.
     * @param value the new value of the component
     */
    public void set(int index, float value);
    
    /**
     * Creates a new transform that is the concatenation of this Matrix and the given matrix.
     * @param matrix the matrix to concatenate this matrix with.
     * @return the concatenated transform.
     */
    public Matrix concatenate(Matrix matrix);
    
    /**
     * Transforms the given vector by the transformation contained by this Matrix.
     * @param vector the vector to transform.
     * @return the transformed vector.
     */
    public Vector transform(Vector vector);
    
    /**
     * @return the order of this Matrix.
     */
    public int getOrder();
    
    /**
     * @return the inverse of this Matrix.
     */
    public Matrix getInverse();
    
    /**
     * @return the transpose of this Matrix.
     */
    public Matrix getTranspose();
    
    /**
     * @return the determinant of this Matrix.
     */
    public float getDeterminant();
    
    /**
     * Prints this Matrix to System.out, good for debugging.
     */
    public void printMatrix();
}
