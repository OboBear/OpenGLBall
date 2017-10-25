package com.me.obo.ballview;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by obo on 2017/9/17.
 * Email:obo1993@gmail.com
 */

public class Ball {

    private static final String TAG = "Ball";

    private static int DEVIDE_COUNT = 16;
    private static int BallR = 4;

    private static final String FRAGMENT_SHADER = "precision mediump float;" +
            "uniform sampler2D u_TextureUnit;" +
            "varying vec2 v_TextureCoordinates;" +
            "void main()" +
            "{" +
            "    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates); " +
            "}";

    private static final String VERTEXT_SHADER = "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "attribute vec2 a_TextureCoordinates;" +
            "varying vec2 v_TextureCoordinates;" +

            "void main()" +
            "{" +
            "    v_TextureCoordinates = a_TextureCoordinates;" +
            "    gl_Position = uMVPMatrix * vPosition;" +
            "}";

    private int mProgram;

    private int mPositionHandler;
    private int mCoordinateHandler;

    private int mTextureUnitHandler;
    private int mMatrixHandler;

    private FloatBuffer positions;

    private FloatBuffer ballPosition;
    private FloatBuffer bmpPosition;

    private int mTexureId;

    // 笛卡尔坐标系为 （x，y，z），屏幕正中心为 0，0，0
    // 图标坐标系与手机屏幕坐标系一致，左上角为 0，0
    private static float[] traingle = {
            -0.5f, 1, 2, 0, 0,
            -1, -1, 2, 0, 1,
            1, -1, 2, 1, 1,

            -0.5f, 1, 2, 0, 0,
            1, -1, 2, 1, 1,
            0.5f, 1, 2, 1, 0
    };


    private float color[] = { 1f, 0.76953125f, 0.22265625f, 1.0f };

    private float[] matrix = new float[16];

    public Ball(int texureId) {
        mTexureId = texureId;
        init();
        Matrix.setIdentityM(matrix, 0);
    }

    private void init() {
        mProgram = GLES20.glCreateProgram();
        bindShader(mProgram, GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER);
        bindShader(mProgram, GLES20.GL_VERTEX_SHADER, VERTEXT_SHADER);
        GLES20.glLinkProgram(mProgram);
        initHandler();
        initPositions();
    }


    private void bindShader(int program, int shader, String shaderCode) {
        int shaderT = GLES20.glCreateShader(shader);
        GLES20.glShaderSource(shaderT, shaderCode);
        GLES20.glCompileShader(shaderT);
        GLES20.glAttachShader(program, shaderT);
    }

    private void initHandler() {
        mPositionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mCoordinateHandler = GLES20.glGetAttribLocation(mProgram, "a_TextureCoordinates");

        mTextureUnitHandler = GLES20.glGetUniformLocation(mProgram, "u_TextureUnit");
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    private void initPositions() {

        float[] ballFloats = new float[DEVIDE_COUNT * DEVIDE_COUNT * 6 * 3];
        float[] ballBitmapFloats = new float[DEVIDE_COUNT * DEVIDE_COUNT * 6 * 2];

        List<Float> ballPoints = new ArrayList<>();
        List<Float> balBitmapPoints = new ArrayList<>();
        double YDegreen = Math.PI / DEVIDE_COUNT;
        double XZDegreen = 2 * Math.PI / DEVIDE_COUNT;

        float bmpUDegreen = 1f / DEVIDE_COUNT;
        float bmpVDegreen = 1f / DEVIDE_COUNT;

        for (int i = 0; i < DEVIDE_COUNT; i ++) { // 纬度
            for (int j = 0; j < DEVIDE_COUNT; j ++) { // 经度

                float X1 = (float) (Math.sin(YDegreen * i) * Math.cos(XZDegreen * j));  // X1
                float Y1 = (float) (Math.cos(YDegreen * i));// Y1
                float Z1 = (float) (Math.sin(YDegreen * i) * Math.sin(XZDegreen * j)); // Z1

                float X2 = (float) (Math.sin(YDegreen * (i + 1)) * Math.cos(XZDegreen * (j)));
                float Y2 = (float) (Math.cos(YDegreen * (i + 1)));
                float Z2 = (float) (Math.sin(YDegreen * (i + 1)) * Math.sin(XZDegreen * (j)));

                float X3 = (float) (Math.sin(YDegreen * i) * Math.cos(XZDegreen * (j + 1)));
                float Y3 = (float) (Math.cos(YDegreen * i));
                float Z3 = (float) (Math.sin(YDegreen * i) * Math.sin(XZDegreen * (j + 1)));

                float X4 = (float) (Math.sin(YDegreen * (i + 1)) * Math.cos(XZDegreen * (j + 1)));
                float Y4 = (float) (Math.cos(YDegreen * (i + 1)));
                float Z4 = (float) (Math.sin(YDegreen * (i + 1)) * Math.sin(XZDegreen * (j + 1)));

                ballPoints.add(X1);
                ballPoints.add(Y1);
                ballPoints.add(Z1);

                ballPoints.add(X2);
                ballPoints.add(Y2);
                ballPoints.add(Z2);

                ballPoints.add(X4);
                ballPoints.add(Y4);
                ballPoints.add(Z4);

                ballPoints.add(X1);
                ballPoints.add(Y1);
                ballPoints.add(Z1);

                ballPoints.add(X4);
                ballPoints.add(Y4);
                ballPoints.add(Z4);

                ballPoints.add(X3);
                ballPoints.add(Y3);
                ballPoints.add(Z3);


                balBitmapPoints.add(i * bmpUDegreen);
                balBitmapPoints.add(j * bmpVDegreen);

                balBitmapPoints.add((i + 1)* bmpUDegreen);
                balBitmapPoints.add(j * bmpVDegreen);

                balBitmapPoints.add((i + 1) * bmpUDegreen);
                balBitmapPoints.add((j + 1) * bmpVDegreen);

                balBitmapPoints.add(i * bmpUDegreen);
                balBitmapPoints.add(j * bmpVDegreen);

                balBitmapPoints.add((i + 1) * bmpUDegreen);
                balBitmapPoints.add((j + 1) * bmpVDegreen);

                balBitmapPoints.add(i * bmpUDegreen);
                balBitmapPoints.add((j + 1) * bmpVDegreen);

            }
        }


        for (int i = 0; i < ballPoints.size() ; i ++) {
            ballFloats[i] = ballPoints.get(i);
        }

        for (int i = 0; i < balBitmapPoints.size() ; i ++) {
            ballBitmapFloats[i] = balBitmapPoints.get(i);
        }


//        positions = ByteBuffer.allocateDirect(traingle.length * 4)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer().put(traingle);
//        positions.position(0);

        ballPosition = ByteBuffer.allocateDirect(ballFloats.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer().put(ballFloats);
        ballPosition.position(0);

        bmpPosition = ByteBuffer.allocateDirect(ballBitmapFloats.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer().put(ballBitmapFloats);
        bmpPosition.position(0);
    }

    public void draw(float []matrix) {

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
//        GLES20.glCullFace(GLES20.GL_FRONT);


        GLES20.glUseProgram(mProgram);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexureId );
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);


        GLES20.glUniformMatrix4fv(mMatrixHandler,1, false, matrix, 0);
        GLES20.glUniform1i(mTextureUnitHandler, 0);

        // 启用一个指向三角形的顶点数组的handle
//        GLES20.glEnableVertexAttribArray(mPositionHandler);
//        GLES20.glVertexAttribPointer(mPositionHandler, 3, GLES20.GL_FLOAT, false, 5 * 4, positions);
//
//        positions.position(3);
//        GLES20.glEnableVertexAttribArray(mCoordinateHandler);
//        GLES20.glVertexAttribPointer(mCoordinateHandler, 2, GLES20.GL_FLOAT, false, 5 * 4, positions);
//        positions.position(0);


        ballPosition.position(0);
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        GLES20.glVertexAttribPointer(mPositionHandler, 3, GLES20.GL_FLOAT, false, 3 * 4, ballPosition);

        bmpPosition.position(0);
        GLES20.glEnableVertexAttribArray(mCoordinateHandler);
        GLES20.glVertexAttribPointer(mCoordinateHandler, 2, GLES20.GL_FLOAT, false, 2 * 4, bmpPosition);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, DEVIDE_COUNT * DEVIDE_COUNT * 6);

//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, traingle.length /5);

    }

}
