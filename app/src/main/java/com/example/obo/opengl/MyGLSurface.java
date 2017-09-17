package com.example.obo.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by obo on 2017/9/11.
 */

public class MyGLSurface extends GLSurfaceView {
    private static final String TAG = "MyGLSurface";

    MyRenderer myRenderer;

    public MyGLSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        myRenderer = new MyRenderer(context);
        setRenderer(myRenderer);
    }

    private float lastX;
    private float lastY;

    private float startX;
    private float startY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                return true;

            case MotionEvent.ACTION_MOVE:
                float rotateX = lastX + startX - event.getX();
                float rotateY = lastY + startY - event.getY();
                Log.i(TAG, "rotateX = " + rotateX + "  rotateY = " + rotateY);
                myRenderer.setRotate(rotateX, rotateY);
                return true;

            case MotionEvent.ACTION_UP:
                lastX = lastX + startX - event.getX();
                lastY = lastY + startY - event.getY();
                return true;
        }

        return super.onTouchEvent(event);
    }
}
