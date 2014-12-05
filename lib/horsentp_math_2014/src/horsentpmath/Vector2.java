
package horsentpmath;

/**
 * A vector in the 2nd dimension.
 * @author Jonathon
 */
public class Vector2 implements Vector {

    private float[] vector;
    
    public Vector2() {
        vector = new float[]{0,0};
    }
    
    public Vector2(float x, float y) {
        vector = new float[]{x,y};
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
        return new Vector2(vector[0]/mag,vector[1]/mag);
    }

    @Override
    public Vector add(Vector vector) {
        return new Vector2(getX()+vector.get(0), getY()+vector.get(1));
    }

    @Override
    public float dot(Vector vector) {
        return (getX()*vector.get(0))+(getY()*vector.get(1));
    }
    
    public float getX() {
        return vector[0];
    }
    
    public float getY() {
        return vector[1];
    }

    @Override
    public int getComponentCount() {
        return 2;
    }

    @Override
    public float[] getComponents() {
        return vector;
    }

    @Override
    public float getMagnitude() {
        return (float)Math.sqrt(dot(this));
    }

    @Override
    public void printVector() {
        System.out.println("[ " + get(0) + ", " + get(1) + " ]");
    }
    
}
