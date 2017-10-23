package com.me.obo.ballview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by obo on 2017/9/17.
 * Email:obo1993@gmail.com
 */

public class MyRenderer implements GLSurfaceView.Renderer {

    private Ball mBall;
    private Context mContext;
    private float[] matrix = new float[16];
    private float[] eyeMatrix = new float[16];
    private float[] frustumMatrix = new float[16];
    private float[] scaleMatrix = new float[16];



    public MyRenderer(Context context) {
        this.mContext = context;
    }

    private int getTexure(int bmpId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), bmpId, options);
        int []texureObjectId = new int[1];
        GLES20.glGenTextures(1, texureObjectId, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texureObjectId[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        bitmap.recycle();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return texureObjectId[0];
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.4f, 0.4f, 0.4f, 1);
//        mBall = new Ball(getTexure(R.mipmap.ic_launcher));
//        mBall = new Ball(getTexure(R.drawable.aaa));
        mBall = new Ball(getTexure(R.drawable.bbb));
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Matrix.setIdentityM(eyeMatrix, 0);
        Matrix.setLookAtM(eyeMatrix, 0,
                0, 0, 1f,
                0, 0, -1,
                -1, 0, 1f);

        Matrix.setIdentityM(frustumMatrix, 0);
        Matrix.frustumM(frustumMatrix, 0, -1f * height/width, 1f * height/width, -1, 1, 0.5f, 20f);

        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, 2, 2, 2);
    }


    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Matrix.setIdentityM(matrix, 0);

        Matrix.multiplyMM(matrix, 0, matrix, 0, eyeMatrix, 0);
        Matrix.multiplyMM(matrix, 0, matrix, 0, frustumMatrix, 0);
        Matrix.multiplyMM(matrix, 0, matrix, 0, scaleMatrix, 0);

        mBall.draw(matrix);
    }
}
