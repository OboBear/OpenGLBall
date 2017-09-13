package com.example.obo.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by obo on 2017/9/11.
 */

public class MyGLSurface extends GLSurfaceView {

    MyRenderer myRenderer;

    public MyGLSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        myRenderer = new MyRenderer(context);
        setRenderer(myRenderer);
    }

    private float lastX;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX() - lastX;
                return true;

            case MotionEvent.ACTION_MOVE:
                float re = lastX - event.getX();
                myRenderer.setRotate(re);

                return true;

            case MotionEvent.ACTION_UP:
                lastX = lastX - event.getX();
                return true;
        }

        return super.onTouchEvent(event);
    }
}
