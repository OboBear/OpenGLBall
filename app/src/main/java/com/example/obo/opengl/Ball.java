package com.example.obo.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by obo on 2017/9/11.
 */

public class Ball {

    private int mProgram;

    private int positionHandler;
    private int colorHandler;


    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 uMVPMatrix;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    float color[] = { 1f, 0.76953125f, 0.22265625f, 1.0f };

    private static float[] mProjMatrix = new float[16];//4x4矩阵 投影用
    private static float[] mVMatrix = new float[16];//摄像机位置朝向9参数矩阵
    private static float[] mMVPMatrix;//最后起作用的总变换矩阵

    public Ball(Context context, int background) {
        init();
    }

    private void init() {
        initProgram();
        initPosition();
    }

    private void initProgram() {
        mProgram = GLES20.glCreateProgram();

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
        GLES20.glShaderSource(vertexShader, vertexShaderCode);

        GLES20.glCompileShader(fragmentShader);
        GLES20.glCompileShader(vertexShader);

        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glAttachShader(mProgram, vertexShader);

        GLES20.glLinkProgram(mProgram);



    }


    FloatBuffer positions;
    List<Float> vetexList;

    private void initPosition() {

//        int PartsCount = 16;
        int PartsCount = 32;

        double partsMDegreen = Math.PI / PartsCount;

        double partsNDegreen = Math.PI * 2 / PartsCount;


        vetexList = new ArrayList<>();


//
        for (int m = 0; m < PartsCount; m ++) {
            for (int n = 0; n < PartsCount; n ++) {

                float x1 = (float) (Math.sin(partsMDegreen * m) * Math.cos(partsNDegreen * n));
                float y1 = (float) Math.cos(partsMDegreen * m);
                float z1 = - (float) (Math.sin(partsMDegreen * m) * Math.sin(partsNDegreen * n));

                float x2 = (float) (Math.sin(partsMDegreen * (m + 1)) * Math.cos(partsNDegreen * n));
                float y2 = (float) Math.cos(partsMDegreen * (m + 1));
                float z2 = - (float) (Math.sin(partsMDegreen * (m + 1)) * Math.sin(partsNDegreen * n));


                float x3 = (float) (Math.sin(partsMDegreen * m) * Math.cos(partsNDegreen * (n + 1)));
                float y3 = (float) Math.cos(partsMDegreen * m);
                float z3 = - (float) (Math.sin(partsMDegreen * m) * Math.sin(partsNDegreen * (n + 1)));

                float x4 = (float) (Math.sin(partsMDegreen * (m + 1)) * Math.cos(partsNDegreen * (n + 1)));
                float y4 = (float) Math.cos(partsMDegreen * (m + 1));
                float z4 = - (float) (Math.sin(partsMDegreen * (m + 1)) * Math.sin(partsNDegreen * (n + 1)));


                vetexList.add(x1);
                vetexList.add(y1);
                vetexList.add(z1);

                vetexList.add(x2);
                vetexList.add(y2);
                vetexList.add(z2);

                vetexList.add(x4);
                vetexList.add(y4);
                vetexList.add(z4);

                vetexList.add(x1);
                vetexList.add(y1);
                vetexList.add(z1);

                vetexList.add(x4);
                vetexList.add(y4);
                vetexList.add(z4);

                vetexList.add(x3);
                vetexList.add(y3);
                vetexList.add(z3);

            }
        }


        float []positionArray = new float[vetexList.size()];

        for (int i = 0; i < positionArray.length; i++) {
            positionArray[i] = vetexList.get(i);
        }

        positions = ByteBuffer.allocateDirect(positionArray.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(positionArray);
        positions.position(0);

    }

    public void draw() {

//
//        Matrix.setLookAtM(mVMatrix, 0,
//                0, 0.5f, 0,
//                0, 0, 1,
//                0, 1, 0);

        Matrix.setLookAtM(mVMatrix, 0,
                0, 0f, 0f,
                0, 0, 1f,
                0, 1f, 0);

//        Matrix.setRotateM(mVMatrix, 0, 1, );


        GLES20.glUseProgram(mProgram);



        positionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");


        // 启用一个指向三角形的顶点数组的handle
        GLES20.glEnableVertexAttribArray(positionHandler);


        // 准备三角形的坐标数据
        GLES20.glVertexAttribPointer(positionHandler, 3,
                GLES20.GL_FLOAT,
                false,
                3 * 4, positions);


        colorHandler = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(colorHandler, 1, color, 0 );


        int matrixHandler = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(matrixHandler,1, false, mVMatrix, 0);




        // 画三角形
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vetexList.size() / 3);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vetexList.size() / 3);

    }


}
