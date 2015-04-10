/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.test.cy.magicCubeOpenGL;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private GLCube   mCube;
    private MagicCube mMC;
    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        mMC = new MagicCube();

        Matrix.setIdentityM(mRotationMatrix, 0);

    }
    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3.0f, 0f, 0f, -1.0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // calculate rotate matrix
        float[] xAxis = {1.0f, 0.0f, 0.0f, 0.0f};
        float[] yAxis = {0.0f, 1.0f, 0.0f, 0.0f};
        float[] invRotation = new float[16];

        Matrix.invertM(invRotation, 0, mRotationMatrix, 0);
        Matrix.multiplyMV(xAxis, 0, invRotation, 0, xAxis, 0);
        Matrix.rotateM(mRotationMatrix, 0, xAngle, xAxis[0], xAxis[1], xAxis[2]);

        Matrix.invertM(invRotation, 0, mRotationMatrix, 0);
        Matrix.multiplyMV(yAxis, 0, invRotation, 0, yAxis, 0);
        Matrix.rotateM(mRotationMatrix, 0, yAngle, yAxis[0], yAxis[1], yAxis[2]);

        // multiply final matrix by previous calculated rotation matrix
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        // draw

     //   mCube.draw(scratch);
        mMC.draw(scratch);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {

        mWidth = width;
        mHeight = height;

        Log.d(TAG,"w="+mWidth+" h="+mHeight);

        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1.0f, 100);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
    public static int mWidth, mHeight;

    private static float[] eye = {0.0f, 0.0f, 3.0f};

    private float mAngle;
    public float xAngle = 0, yAngle = 0;
    private float[] mAxis = {1, 0, 0};
    public void setRotateAxis(float[] axis, float angle)
    {
        mAxis = axis;
        mAngle = angle;
    }

    public static boolean interact(float screenX, float screenY){

        float[] viewCord = new float[3];
        viewCord[0] = (2 * screenX - mWidth)/ mWidth * mWidth/mHeight;
        viewCord[1] = -(2 * screenY - mHeight)/mHeight;
        viewCord[2] = -1;
        Ray ray = new Ray(new GLVector(eye), new GLVector(viewCord));

        return MagicCube.interact(ray);
    }
}