package com.example.obo.opengl;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by obo on 2017/9/13.
 */

public class Triangle extends Shape {

    private final String VERTEXT_SHADER =
            "attribute vec4 vPosition;"
                    + "uniform mat4 uMatrix;"
                    + "void main() {"
                    + "  gl_Position = uMatrix * vPosition;"
//                    + "  gl_Position = vPosition * uMatrix;"
                    + "}";

    private final String FRAGMENT_SHADER =
            "precision mediump float;"
                    + "uniform vec4 vColor;"
                    + "void main() {"
                    + "  gl_FragColor = vColor;"
                    + "}";


    private int mProgram;
    private int mColorHandler;
    private int mPositionHandler;
    private int mMatrixHandler;
    private FloatBuffer mPosition;

    private static final float []positionArray = {
            -0.5f, -0.5f, 0,
            0.5f, -0.5f, 0,
            0, 0.5f, 0
    };

//    static float positionArray[] = { // 按逆时针方向顺序:
//            0.0f,  0.622008459f, 0.0f,   // top
//            -0.5f, -0.311004243f, 0.0f,   // bottom left
//            0.5f, -0.311004243f, 0.0f    // bottom right
//    };


//    private static final float []color = {1f, 0.5f, 0.6f, 1};
    // 设置颜色，分别为red, green, blue 和alpha (opacity)
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    public Triangle() {
        initProgram();
        initHandler();
        initPosition();
    }

    private void initProgram() {

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        int vertextShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        GLES20.glShaderSource(fragmentShader, FRAGMENT_SHADER);
        GLES20.glShaderSource(vertextShader, VERTEXT_SHADER);

        GLES20.glCompileShader(fragmentShader);
        GLES20.glCompileShader(vertextShader);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glAttachShader(mProgram, vertextShader);
        GLES20.glLinkProgram(mProgram);
    }

    private void initHandler() {
        mColorHandler = GLES20.glGetUniformLocation(mProgram, "vColor");
        mPositionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "uMatrix");
    }

    private void initPosition() {
        mPosition = ByteBuffer
                .allocateDirect(positionArray.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(positionArray);
        mPosition.position(0);
    }

    public void draw(float [] matrix) {

        GLES20.glUseProgram(mProgram);
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        GLES20.glVertexAttribPointer(mPositionHandler, 3, GLES20.GL_FLOAT, false, 3 * 4, mPosition);
        GLES20.glUniform4fv(mColorHandler, 1, color, 0);
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, matrix, 0);
        // 画三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
//        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 3);
    }





}
