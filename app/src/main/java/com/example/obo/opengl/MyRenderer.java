package com.example.obo.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by obo on 2017/9/11.
 */

public class MyRenderer implements GLSurfaceView.Renderer {

    private Context mContext;

//    private Ball mBall;

    private Triangle mTriangle;

    public MyRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
    }

    private static float[] mVMatrix = new float[16];//摄像机位置朝向9参数矩阵
    private static float[] mProjMatrix = new float[16];//4x4矩阵 投影用
    private static float[] mMVPMatrix = new float[16];//最后起作用的总变换矩阵
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1f, 20);
//        mBall = new Ball(mContext, R.mipmap.ic_launcher);
//        mBall = new Ball(mContext, R.drawable.imgbug);

        mTriangle = new Triangle();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清除先前的缓存
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_FRONT);
//        GLES20.glCullFace(GLES20.GL_BACK);

        // 设置相机的位置(视口矩阵)
//        Matrix.setLookAtM(mVMatrix, 0, 0, 0, 0, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

//        Matrix.setLookAtM(mVMatrix, 0,
//                0, 0f, 0f,
//                0, 0, 1f,
//                0, 1f, 0);

        Matrix.setLookAtM(mProjMatrix, 0,
                0, 0f, 0f,
                0, 0, 10f,
                0, 1f, 0f);
//        Matrix.scaleM(mVMatrix, 0 , (float) (Math.PI/10), 0,0);
        Matrix.setIdentityM(mVMatrix, 0);
//        Matrix.translateM(mVMatrix, 0, 0.1f, 0, 0);

        float rate = 1.1f;
        Matrix.scaleM(mVMatrix, 0 , rate,rate/2, rate);


//        Matrix.rotateM(mVMatrix, 0, mRotate, 0, 1, 0);


//        Matrix.scaleM(mVMatrix, 0, 4.0F, 4.0F, 4.0F);

        // 计算投影和视口变换
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

//        mBall.draw(mMVPMatrix);

        mTriangle.draw();
    }

    float mRotate;
    public void setRotate(float rotate) {
        mRotate = rotate;
    }
}
