package com.example.obo.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by obo on 2017/9/11.
 */

public class MyRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyRenderer";

    private Context mContext;

//    private Triangle mTriangle;

    private Ball mBall;

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
    private static float[] mRotateMatrixX = new float[16];//旋转矩阵
    private static float[] mRotateMatrixY = new float[16];//旋转矩阵
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1f, 20);
//        Matrix.translateM(this.mProjMatrix, 0, 0.0F, 0.0F, -2.0F);
        Matrix.scaleM(this.mProjMatrix, 0, 4.0F, 4.0F, 4.0F);
//        mBall = new Ball(mContext, R.mipmap.ic_launcher);
        mBall = new Ball(mContext, R.drawable.imgbug2);

//        mTriangle = new Triangle();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清除先前的缓存
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.setIdentityM(mMVPMatrix, 0);

        Matrix.translateM(mMVPMatrix, 0, 0, 1, 0);

        Matrix.setIdentityM(mRotateMatrixX, 0);
        Matrix.rotateM(mRotateMatrixX, 0, mRotateX, 0, 5, 0);


        double degreen = mRotateX / 360 * 2 * Math.PI;
        Log.i(TAG, "mRotateX = " + mRotateX);
        Log.i(TAG, "degreen = " + degreen);
        Matrix.setIdentityM(mRotateMatrixY, 0);
        Matrix.rotateM(mRotateMatrixY, 0, mRotateY, (float) Math.cos(degreen), 0, (float) Math.sin(degreen));


//        Matrix.scaleM(mVMatrix, 0, 4.0F, 4.0F, 4.0F);

        // 计算投影和视口变换
//        Matrix.multiplyMM(mProjMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);

        Matrix.multiplyMM(mVMatrix, 0, mProjMatrix, 0, mRotateMatrixX, 0);
        Matrix.multiplyMM(mVMatrix, 0, mVMatrix, 0, mRotateMatrixY, 0);
        mBall.draw(mVMatrix);

//        mTriangle.draw(mMVPMatrix);
    }

    float mRotateX;
    float mRotateY;
    public void setRotate(float rotateX, float rotateY) {
        mRotateX = rotateX;
        mRotateY = rotateY;
    }
}
