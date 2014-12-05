
package horsentpmath;

/**
 * A transformation on R^3.
 * @author Jonathon
 */
public class Matrix3 implements Matrix {

    public static final float[] IDENTITY = new float[] {
        1, 0, 0,
        0, 1, 0,
        0, 0, 1
    };
    
    private float[] matrix;
    
    public Matrix3() {
        matrix = IDENTITY;
    }
    
    public Matrix3(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22
    ) {
        matrix = new float[] {
            m00, m01, m02,
            m10, m11, m12,
            m20, m21, m22
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
        return new Matrix3(
            (get(0)*matrix.get(0))+(get(1)*matrix.get(3))+(get(2)*matrix.get(6)),(get(0)*matrix.get(1))+(get(1)*matrix.get(4))+(get(2)*matrix.get(7)),(get(0)*matrix.get(2))+(get(1)*matrix.get(5))+(get(2)*matrix.get(8)),
            (get(3)*matrix.get(0))+(get(4)*matrix.get(3))+(get(5)*matrix.get(6)),(get(3)*matrix.get(1))+(get(4)*matrix.get(4))+(get(5)*matrix.get(7)),(get(3)*matrix.get(2))+(get(4)*matrix.get(5))+(get(5)*matrix.get(8)),
            (get(6)*matrix.get(0))+(get(7)*matrix.get(3))+(get(8)*matrix.get(6)),(get(6)*matrix.get(1))+(get(7)*matrix.get(4))+(get(8)*matrix.get(7)),(get(6)*matrix.get(2))+(get(7)*matrix.get(5))+(get(8)*matrix.get(8))
        );
    }

    @Override
    public Vector transform(Vector vector) {
        return new Vector3(
                (get(0)*vector.get(0))+(get(1)*vector.get(1))+(get(2)*vector.get(2)),
                (get(3)*vector.get(0))+(get(4)*vector.get(1))+(get(5)*vector.get(2)),
                (get(6)*vector.get(0))+(get(7)*vector.get(1))+(get(8)*vector.get(2))
        );
    }

    @Override
    public int getOrder() {
        return 3;
    }
    
    @Override
    public Matrix getInverse() {
        float invDet = 1/getDeterminant();
        return new Matrix3(
            invDet*((get(4)*get(8))-(get(5)*get(7))), invDet*((get(2)*get(7))-(get(1)*get(8))), invDet*((get(1)*get(5))-(get(2)*get(4))),
            invDet*((get(5)*get(6))-(get(3)*get(8))), invDet*((get(0)*get(8))-(get(2)*get(6))), invDet*((get(2)*get(3))-(get(0)*get(5))),
            invDet*((get(3)*get(7))-(get(4)*get(6))), invDet*((get(1)*get(6))-(get(0)*get(7))), invDet*((get(0)*get(4))-(get(1)*get(3)))
        );
    }

    @Override
    public Matrix getTranspose() {
        return new Matrix3(
                get(0), get(3), get(6),
                get(1), get(4), get(7),
                get(2), get(5), get(8)
        );
    }
   
    @Override
    public float getDeterminant() {
        return (get(0)*get(4)*get(8))+(get(3)*get(7)*get(2))+(get(6)*get(1)*get(5))-(get(0)*get(7)*get(5))-(get(6)*get(4)*get(2))-(get(3)*get(1)*get(8));
    }

    @Override
    public void printMatrix() {
        System.out.println("[ " + get(0) + ", " + get(1) + ", " + get(2) + " ]");
        System.out.println("[ " + get(3) + ", " + get(4) + ", " + get(5) + " ]");
        System.out.println("[ " + get(6) + ", " + get(7) + ", " + get(8) + " ]");
    }
    
}
