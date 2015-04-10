package com.test.cy.magicCubeOpenGL;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;


public class OpenGLES extends Activity {

    class MyGLSurfaceView extends GLSurfaceView {

        private static final String TAG = "MyGLSurfaceView";

        private final MyGLRenderer mRenderer;

        public MyGLSurfaceView(Context context) {
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            mRenderer = new MyGLRenderer();

            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(mRenderer);

            //    setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }

        private float touchDownX, touchDownY;

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            int width = MyGLRenderer.mWidth;
            int height = MyGLRenderer.mHeight;

            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE: {
                    mRenderer.yAngle = -(touchDownX - e.getX()) / 20.f;
                    //  rotX += (touchDownX - e.getX()) /20.f
                    mRenderer.xAngle = -(touchDownY - e.getY()) / 20.f;
                    touchDownX = e.getX();
                    touchDownY = e.getY();

                }
                case MotionEvent.ACTION_DOWN: {
                    touchDownX = e.getX();
                    touchDownY = e.getY();

                    if (MyGLRenderer.interact(touchDownX, touchDownY))
                        Log.d(TAG, "true");
                    else
                        Log.d(TAG, "false");
                }

            }
            return true;
        }

    }

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new MyGLSurfaceView(this);

        setContentView(mGLView);
    }

}
