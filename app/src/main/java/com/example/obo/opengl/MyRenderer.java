package com.example.obo.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by obo on 2017/9/11.
 */

public class MyRenderer implements GLSurfaceView.Renderer {

    private Context mContext;

    private Ball mBall;

    public MyRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mBall = new Ball(mContext, R.mipmap.ic_launcher);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清除先前的缓存
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        mBall.draw();
    }
}
