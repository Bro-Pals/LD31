
package horsentpmath;

/**
 * A vector in the 3nd dimension.
 * @author Jonathon
 */
public class Vector4 implements Vector {
    
    private float[] vector;
    
    public Vector4() {
        vector = new float[]{0,0,0,0};
    }
    
    public Vector4(float x, float y, float z, float w) {
        vector = new float[]{x,y,z,w};
    }

    @Override
    public float get(int index) {
        return vector[index];
    }

    @Override
    public void set(int index, float value) {
        vector[index] = value;
    }

    @Override
    public Vector normalize() {
        float mag = getMagnitude();
        return new Vector4(vector[0]/mag,vector[1]/mag, vector[2]/mag, vector[3]/mag);
    }

    @Override
    public Vector add(Vector vector) {
        return new Vector4(getX()+vector.get(0), getY()+vector.get(1), getZ()+vector.get(2), getW()+vector.get(3));
    }

    @Override
    public float dot(Vector vector) {
        return (getX()*vector.get(0))+(getY()*vector.get(1))+(getZ()*vector.get(2))+(getW()*vector.get(3));
    }

    @Override
    public int getComponentCount() {
        return 4;
    }

    @Override
    public float[] getComponents() {
        return vector;
    }

    @Override
    public float getMagnitude() {
        return (float)Math.sqrt(dot(this));
    }

    public float getX() {
        return vector[0];
    }
    
    public float getY() {
        return vector[1];
    }
    
    public float getZ() {
        return vector[2];
    }
    
    public float getW() {
        return vector[3];
    }
    
    @Override
    public void printVector() {
        System.out.println("[ " + get(0) + ", " + get(1) + ", " + get(2) + ", " + get(3) + " ]");
    }
}
