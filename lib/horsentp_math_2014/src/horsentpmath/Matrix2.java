
package horsentpmath;

/**
 * A transformation on R^2.
 * @author Jonathon
 */
public class Matrix2 implements Matrix {

    public static final float[] IDENTITY = {
        1, 0,
        0, 1
    };
    
    private float[] matrix;
    
    public Matrix2() {
        matrix = IDENTITY;
    }
    
    /**
     * Creates a matrix from the values.
     * @param m00 row 1 column 1
     * @param m01 row 1 column 2
     * @param m10 row 2 column 1
     * @param m11 row 2 column 2
     */
    public Matrix2(
            float m00, float m01, 
            float m10, float m11
    ) {
        matrix = new float[] {
            m00, m01,
            m10, m11
        };
    }
    
    @Override
    public float get(int index) {
        return matrix[index];
    }

    @Override
    public void set(int index, float value) {
        matrix[index] = value;
    }

    @Override
    public Matrix concatenate(Matrix matrix) {
        return new Matrix2(
            (get(0)*matrix.get(0))+(get(1)*matrix.get(2)),(get(0)*matrix.get(1))+(get(1)*matrix.get(3)),
            (get(2)*matrix.get(0))+(get(3)*matrix.get(2)),(get(2)*matrix.get(1))+(get(3)*matrix.get(3))
        );
    }

    @Override
    public Vector transform(Vector vector) {
        return new Vector2(
            (get(0)*vector.get(0))+(get(1)*vector.get(1)),
            (get(2)*vector.get(0))+(get(3)*vector.get(1))
        );
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public Matrix getInverse() {
        float invDet = 1/getDeterminant();
        return new Matrix2(
            invDet*get(3), -(invDet*get(1)),
            -(invDet*get(2)), invDet*get(0)
        );
    }

    @Override
    public Matrix getTranspose() {
        return new Matrix2(
            get(0), get(2),
            get(1), get(3)
        );
    }

    @Override
    public float getDeterminant() {
        return (get(0)*get(3))-(get(1)*get(2));
    }

    @Override
    public void printMatrix() {
        System.out.println("[ " + get(0) + ", " + get(1) + " ]");
        System.out.println("[ " + get(2) + ", " + get(3) + " ]");
    }
}
