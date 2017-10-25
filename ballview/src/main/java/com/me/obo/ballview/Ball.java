package com.me.obo.ballview;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by obo on 2017/9/17.
 * Email:obo1993@gmail.com
 */

public class Ball {

    private static final String TAG = "Ball";

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
        positions = ByteBuffer.allocateDirect(traingle.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer().put(traingle);
        positions.position(0);
    }

    public void draw(float []matrix) {

        GLES20.glEnable(GLES20.GL_CULL_FACE);
//        GLES20.glCullFace(GLES20.GL_FRONT);
        GLES20.glCullFace(GLES20.GL_BACK);

        GLES20.glUseProgram(mProgram);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexureId );
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);


        GLES20.glUniformMatrix4fv(mMatrixHandler,1, false, matrix, 0);
        GLES20.glUniform1i(mTextureUnitHandler, 0);

        // 启用一个指向三角形的顶点数组的handle
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        GLES20.glVertexAttribPointer(mPositionHandler, 3, GLES20.GL_FLOAT, false, 5 * 4, positions);

        positions.position(3);
        GLES20.glEnableVertexAttribArray(mCoordinateHandler);
        GLES20.glVertexAttribPointer(mCoordinateHandler, 2, GLES20.GL_FLOAT, false, 5 * 4, positions);
        positions.position(0);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, traingle.length /5);

    }

}
