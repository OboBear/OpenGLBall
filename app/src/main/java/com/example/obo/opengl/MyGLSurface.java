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

    boolean isTouch = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                isTouch = true;
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
                startX = 0;
                startY = 0;
                isTouch = false;
                return true;
        }

        return super.onTouchEvent(event);
    }

    public void setX(float x , float y) {
        if (!isTouch) {
            lastX -= x * 2;
            lastY -= y ;
            myRenderer.setRotate(lastX + startX, lastY + startY);
        }
    };
}
