package com.modwiz.ld31.utils.math;

import horsentpmath.Vector2;

import java.util.Vector;

/**
 * Some helper methods to do common vector manipulations
 */
public class VectorUtils {
    public static Vector2 multiply(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.getX()*v2.getX(), v1.getY()*v2.getY());
    }

    public static Vector2 multiplyScalar(float scalar, Vector2 vec) {
        return new Vector2(scalar * vec.getX(), scalar * vec.getY());
    }

    public static void setVector2(Vector2 original, Vector2 replacement) {
        original.set(0, replacement.get(0));
        original.set(1, replacement.get(1));
    }

}
