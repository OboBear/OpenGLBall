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
    private int mTexureId;
    private float[] matrix = new float[16];
    private float[] matrixLook = new float[16];
    private float[] matrixFrustum = new float[16];

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
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        bitmap.recycle();

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return texureObjectId[0];
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.4f, 0.4f, 0.4f, 1);
        mBall = new Ball(getTexure(R.mipmap.ic_launcher));
        Matrix.setLookAtM(matrixLook, 0,
                0, 0, 100f,
                0, 0, -100f,
                0, 1, 0);
    }


    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Matrix.frustumM(matrixFrustum, 0,
                -1, 1,
                -1, 1,
                90f, 120);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
//        Matrix.setIdentityM(matrix, 0);
        Matrix.multiplyMM(matrix, 0, matrixFrustum, 0, matrixLook, 0);
        mBall.draw(matrix);
    }
}
