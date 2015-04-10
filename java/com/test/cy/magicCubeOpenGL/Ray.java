package com.test.cy.magicCubeOpenGL;

/**
 * Created by Administrator on 2015/4/1.
 * Ray class. To
 */
public class Ray {

    private final static String TAG = "RAY";

    // x(t) = Pr + t*Ur
    public GLVector startPoint;    // Pr

    public GLVector direction;     // Ur

    private float t = 1;            // t

    public Ray(){

    }
    public Ray(GLVector sp, GLVector ep){
        startPoint = sp;
        direction = GLVector.minus(ep, sp);
        direction.normalize();
    }
}
