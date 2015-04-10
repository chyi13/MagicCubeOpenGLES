package com.test.cy.magicCubeOpenGL;

import android.opengl.Matrix;
import android.util.Log;

/**
 * Created by Administrator on 2015/3/30.
 */
public class MagicCube {

    private final static String TAG = "MagicCube";

    // front face
    private static float[] leftBot = {-1.3f, -1.3f, 1.3f}; // (Xleft,Yleft) and (Xright,Yright)
    private static float[] rightTop = {1.3f, 1.3f, 1.3f};
    private static float[] rightBot = {1.3f, -1.3f, 1.3f};
    // measurement = 1.5 * 1.5 * 1.5;
    private float cubesPos[][] = {{-0.5f, -0.5f, 0.5f}, {-0.5f,  0.0f,  0.5f}, {-0.5f,  0.5f,  0.5f},
            { 0.0f, -0.5f,  0.5f}, { 0.0f,  0.0f,  0.5f}, { 0.0f,  0.5f,  0.5f},
            { 0.5f, -0.5f,  0.5f}, { 0.5f,  0.0f,  0.5f}, { 0.5f,  0.5f,  0.5f},    // front
            {-0.5f, -0.5f,  0.0f}, {-0.5f,  0.0f,  0.0f}, {-0.5f,  0.5f,  0.0f},
            { 0.0f, -0.5f,  0.0f}, { 0.0f,  0.0f,  0.0f}, { 0.0f,  0.5f,  0.0f},
            { 0.5f, -0.5f,  0.0f}, { 0.5f,  0.0f,  0.0f}, { 0.5f,  0.5f,  0.0f},    // mid
            {-0.5f, -0.5f, -0.5f}, {-0.5f,  0.0f, -0.5f}, {-0.5f,  0.5f, -0.5f},
            { 0.0f, -0.5f, -0.5f}, { 0.0f,  0.0f, -0.5f}, { 0.0f,  0.5f, -0.5f},
            { 0.5f, -0.5f, -0.5f}, { 0.5f,  0.0f, -0.5f}, { 0.5f,  0.5f, -0.5f}     // back
    };
    float[] scale = new float[16];

    private GLCube[][][] cubes;
    public MagicCube(){
        cubes = new GLCube[3][3][3];

        int i,j,k;
        for (i = 0; i<3; i++) {
            for (j = 0; j < 3; j++) {
                for (k = 0; k < 3; k++) {
                    cubes[i][j][k] = new GLCube();
                }
            }
        }
    }
    private float[] mModelMatrix = new float[16];

    public void draw(float[] mvpMatrix){
        int i,j,k;
        for (i = 0; i<3; i++){
            for (j = 0; j<3; j++){
                for (k = 0; k<3; k++){
                    Matrix.setIdentityM(mModelMatrix, 0);
                    Matrix.translateM(mModelMatrix, 0,
                            cubesPos[i*9+j*3+k][0], cubesPos[i*9+j*3+k][1], cubesPos[i*9+j*3+k][2]);
//                    Log.d(TAG, "id="+ (i*9+j*3+k) +
//                            "x="+cubesPos[i*9+j*3+k][0]+
//                            "y="+cubesPos[i*9+j*3+k][1]+
//                            "z="+cubesPos[i*9+j*3+k][2]);
                    float[] scratch = new float[16];
                    Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, mModelMatrix, 0);

                    Matrix.setIdentityM(scale, 0);
                    Matrix.scaleM(scale, 0, 0.245f, 0.245f, 0.245f);
                    Matrix.multiplyMM(scratch, 0, scratch, 0, scale, 0);

                    float[] posMatrix = new float[4];
                    float[] tempPos = {1.0f, 1.0f, 1.0f, 1.0f};

                    Matrix.multiplyMV(posMatrix, 0, scratch, 0, tempPos, 0);
                    if (i ==0 && j == 1 && k == 1)
                        Log.d(TAG, "x="+posMatrix[0]+ " y="+posMatrix[1]+ " z="+posMatrix[2]);

                    cubes[i][j][k].draw(scratch);
                }
            }
        }
    }

    public static boolean interact(Ray r){
        GLVector lb = new GLVector(leftBot);
        GLVector rt = new GLVector(rightTop);
        GLVector rb = new GLVector(rightBot);

        lb = GLVector.multiplyVF(lb, 0.245f);
        rt = GLVector.multiplyVF(rt, 0.245f);
        rb = GLVector.multiplyVF(rb, 0.245f);

  //      Log.d(TAG, "lb"+ lb.values[0]+" "+lb.values[1]);

        GLVector n = GLVector.cross(GLVector.minus(rt, rb), GLVector.minus(lb, rb));

        float t;
        t = - GLVector.dot(n, GLVector.minus(r.startPoint, lb))/ GLVector.dot(n, r.direction);

 //       Log.d(TAG, "t="+t);

        GLVector pos;
        pos = GLVector.add(r.startPoint, GLVector.multiplyVF(r.direction, t));

        Log.d(TAG, "x="+pos.values[0] + " y="+pos.values[1]);

        if ( (pos.values[0] > lb.values[0] && pos.values[0] < rt.values[0]) &&
                (pos.values[1] > lb.values[1] && pos.values[1] < rt.values[1]) )
            return true;
        return false;
    }
}
