package com.me.obo.ballview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by obo on 2017/9/17.
 * Email:obo1993@gmail.com
 */

public class MySurfaceView extends GLSurfaceView {
    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(new MyRenderer(context));
    }
}
