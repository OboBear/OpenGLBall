package com.example.obo.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by obo on 2017/9/11.
 */

public class MyGLSurface extends GLSurfaceView {

    public MyGLSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(new MyRenderer(context));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                return true;

            case MotionEvent.ACTION_MOVE:

                return true;

            case MotionEvent.ACTION_UP:

                return true;
        }

        return super.onTouchEvent(event);
    }
}
