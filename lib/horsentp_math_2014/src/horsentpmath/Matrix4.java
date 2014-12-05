
package horsentpmath;

/**
 * A transformation on R^4.
 * @author Jonathon
 */
public class Matrix4 implements Matrix {

    public static final float[] IDENTITY = new float[] {
        1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1
    };
    
    private float[] matrix;
    
    public Matrix4() {
        matrix = IDENTITY;
    }
    
    public Matrix4(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33
    ) {
        matrix = new float[] {
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
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
        return new Matrix4(
            (get(0)*matrix.get(0))+(get(1)*matrix.get(4))+(get(2)*matrix.get(8))+(get(3)*matrix.get(12)),(get(0)*matrix.get(1))+(get(1)*matrix.get(5))+(get(2)*matrix.get(9))+(get(3)*matrix.get(13)),(get(0)*matrix.get(2))+(get(1)*matrix.get(6))+(get(2)*matrix.get(10))+(get(3)*matrix.get(14)),(get(0)*matrix.get(3))+(get(1)*matrix.get(7))+(get(2)*matrix.get(11))+(get(3)*matrix.get(15)),
            (get(4)*matrix.get(0))+(get(5)*matrix.get(4))+(get(6)*matrix.get(8))+(get(7)*matrix.get(12)),(get(4)*matrix.get(1))+(get(5)*matrix.get(5))+(get(6)*matrix.get(9))+(get(7)*matrix.get(13)),(get(4)*matrix.get(2))+(get(5)*matrix.get(6))+(get(6)*matrix.get(10))+(get(7)*matrix.get(14)),(get(4)*matrix.get(3))+(get(5)*matrix.get(7))+(get(6)*matrix.get(11))+(get(7)*matrix.get(15)),
            (get(8)*matrix.get(0))+(get(9)*matrix.get(4))+(get(10)*matrix.get(8))+(get(11)*matrix.get(12)),(get(8)*matrix.get(1))+(get(9)*matrix.get(5))+(get(10)*matrix.get(9))+(get(11)*matrix.get(13)),(get(8)*matrix.get(2))+(get(9)*matrix.get(6))+(get(10)*matrix.get(10))+(get(11)*matrix.get(14)),(get(8)*matrix.get(3))+(get(9)*matrix.get(7))+(get(10)*matrix.get(11))+(get(11)*matrix.get(15)),
            (get(12)*matrix.get(0))+(get(13)*matrix.get(4))+(get(14)*matrix.get(8))+(get(15)*matrix.get(12)),(get(12)*matrix.get(1))+(get(13)*matrix.get(5))+(get(14)*matrix.get(9))+(get(15)*matrix.get(13)),(get(12)*matrix.get(2))+(get(13)*matrix.get(6))+(get(14)*matrix.get(10))+(get(15)*matrix.get(14)),(get(12)*matrix.get(3))+(get(13)*matrix.get(7))+(get(14)*matrix.get(11))+(get(15)*matrix.get(15))
        );
    }

    @Override
    public Vector transform(Vector vector) {
        return new Vector4(
            (get(0)*vector.get(0))+(get(1)*vector.get(1))+(get(2)*vector.get(2))+(get(3)*vector.get(3)),
            (get(4)*vector.get(0))+(get(5)*vector.get(1))+(get(6)*vector.get(2))+(get(7)*vector.get(3)),
            (get(8)*vector.get(0))+(get(9)*vector.get(1))+(get(10)*vector.get(2))+(get(11)*vector.get(3)),
            (get(12)*vector.get(0))+(get(13)*vector.get(1))+(get(14)*vector.get(2))+(get(15)*vector.get(3))
        );
    }

    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    public Matrix getInverse() {
        float invDet = 1/getDeterminant();
        return new Matrix4(
/*Row 1*/   invDet*((get(5)*get(10)*get(15))+(get(6)*get(11)*get(13))+(get(7)*get(9)*get(14))-(get(5)*get(11)*get(14))-(get(6)*get(9)*get(15))-(get(7)*get(10)*get(13))),
            invDet*((get(1)*get(11)*get(14))+(get(2)*get(9)*get(15))+(get(3)*get(10)*get(13))-(get(1)*get(10)*get(15))-(get(2)*get(11)*get(13))-(get(3)*get(9)*get(14))),
            invDet*((get(1)*get(6)*get(15))+(get(2)*get(7)*get(13))+(get(3)*get(5)*get(14))-(get(1)*get(7)*get(14))-(get(2)*get(5)*get(15))-(get(3)*get(6)*get(13))),
            invDet*((get(1)*get(7)*get(10))+(get(2)*get(5)*get(11))+(get(3)*get(6)*get(9))-(get(1)*get(6)*get(11))-(get(2)*get(7)*get(9))-(get(3)*get(5)*get(10))),
/*Row 2*/   invDet*((get(4)*get(11)*get(14))+(get(6)*get(8)*get(15))+(get(7)*get(10)*get(12))-(get(4)*get(10)*get(15))-(get(6)*get(11)*get(12))-(get(7)*get(8)*get(14))),
            invDet*((get(0)*get(10)*get(15))+(get(2)*get(11)*get(12))+(get(3)*get(8)*get(14))-(get(0)*get(11)*get(14))-(get(2)*get(8)*get(15))-(get(3)*get(10)*get(12))),
            invDet*((get(0)*get(7)*get(14))+(get(2)*get(4)*get(15))+(get(3)*get(6)*get(12))-(get(0)*get(6)*get(15))-(get(2)*get(7)*get(12))-(get(3)*get(4)*get(14))),
            invDet*((get(0)*get(6)*get(11))+(get(2)*get(7)*get(8))+(get(3)*get(4)*get(10))-(get(0)*get(7)*get(10))-(get(2)*get(4)*get(11))-(get(3)*get(6)*get(8))),
/*Row 3*/   invDet*((get(4)*get(9)*get(15))+(get(5)*get(11)*get(12))+(get(7)*get(8)*get(13))-(get(4)*get(11)*get(13))-(get(5)*get(8)*get(15))-(get(7)*get(9)*get(12))),
            invDet*((get(0)*get(11)*get(13))+(get(1)*get(8)*get(15))+(get(3)*get(9)*get(12))-(get(0)*get(9)*get(15))-(get(1)*get(11)*get(12))-(get(3)*get(8)*get(13))),
            invDet*((get(0)*get(5)*get(15))+(get(1)*get(7)*get(12))+(get(3)*get(4)*get(13))-(get(0)*get(7)*get(13))-(get(1)*get(4)*get(15))-(get(3)*get(5)*get(12))),
            invDet*((get(0)*get(7)*get(9))+(get(1)*get(4)*get(11))+(get(3)*get(5)*get(8))-(get(0)*get(5)*get(11))-(get(1)*get(7)*get(8))-(get(3)*get(4)*get(9))),
/*Row 4*/   invDet*((get(4) *get(10)*get(13))+(get(5)*get(8)*get(14))+(get(6)*get(9)*get(12))-(get(4)*get(9)*get(14))-(get(5)*get(10)*get(12))-(get(6)*get(8)*get(13))),
            invDet*((get(0) *get(9)*get(14))+(get(1)*get(10)*get(12))+(get(2)*get(8)*get(13))-(get(0)*get(10)*get(13))-(get(1)*get(8)*get(14))-(get(2)*get(9)*get(12))),
            invDet*((get(0) *get(6)*get(13))+(get(1)*get(4)*get(14))+(get(2)*get(5)*get(12))-(get(0)*get(5)*get(14))-(get(1)*get(6)*get(12))-(get(2)*get(4)*get(13))),
            invDet*((get(0) *get(5)*get(10))+(get(1)*get(6)*get(8))+(get(2)*get(4)*get(9))-(get(0)*get(6)*get(9))-(get(1)*get(4)*get(10))-(get(2)*get(5)*get(8)))
        );
    }
    
    @Override
    public Matrix getTranspose() {
        return new Matrix4(
            get(0), get(4), get(8), get(12),
            get(1), get(5), get(9), get(13),
            get(2), get(6), get(10), get(14),
            get(3), get(7), get(11), get(15)
        );
    }

    @Override
    public float getDeterminant() {
        return 
        (get(0)*get(5)*get(10)*get(15))+(get(0)*get(6)*get(11)*get(13))+(get(0)*get(7)*get(9)*get(14))+
        (get(1)*get(4)*get(11)*get(14))+(get(1)*get(6)*get(8)*get(15))+(get(1)*get(7)*get(10)*get(12))+
        (get(2)*get(4)*get(9)*get(15))+(get(2)*get(5)*get(11)*get(12))+(get(2)*get(7)*get(8)*get(13))+
        (get(3)*get(4)*get(10)*get(13))+(get(3)*get(5)*get(8)*get(14))+(get(3)*get(6)*get(9)*get(12))
       -(get(0)*get(5)*get(11)*get(14))-(get(0)*get(6)*get(9)*get(15))-(get(0)*get(7)*get(10)*get(13))
       -(get(1)*get(4)*get(10)*get(15))-(get(1)*get(6)*get(11)*get(12))-(get(1)*get(7)*get(8)*get(14))
       -(get(2)*get(4)*get(11)*get(13))-(get(2)*get(5)*get(8)*get(15))-(get(2)*get(7)*get(9)*get(12))
       -(get(3)*get(4)*get(9)*get(14))-(get(3)*get(5)*get(10)*get(12))-(get(3)*get(6)*get(8)*get(13));
    }

    @Override
    public void printMatrix() {
        System.out.println("[ " + get(0) + ", " + get(1) + ", " + get(2) + ", " + get(3) + " ]");
        System.out.println("[ " + get(4) + ", " + get(5) + ", " + get(6) + ", " + get(7) + " ]");
        System.out.println("[ " + get(8) + ", " + get(9) + ", " + get(10) + ", " + get(11) + " ]");
        System.out.println("[ " + get(12) + ", " + get(13) + ", " + get(14) + ", " + get(15) + " ]");
    }
    
}
