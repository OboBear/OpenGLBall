package com.example.obo.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by obo on 2017/9/11.
 */

public class Ball extends Shape {

    private int mProgram;



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


    float color[] = { 1f, 0.76953125f, 0.22265625f, 1.0f };


    private int mTexureId;

    private int positionHandler;
    private int colorHandler;
    private int matrixHandler;
    private int textureUnitHandler;
    private int textureCoordinatesHanlder;

    public Ball(Context context, int backgroundId) {
        init();
        loadTexure(context, backgroundId);
    }

    private void init() {
        initProgram();
        initPosition();
        initShaderHandler();
    }

    private void initShaderHandler() {
        positionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");
        colorHandler = GLES20.glGetUniformLocation(mProgram, "vColor");
        matrixHandler = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        textureUnitHandler = GLES20.glGetUniformLocation(mProgram, "u_TextureUnit");
        textureCoordinatesHanlder = GLES20.glGetAttribLocation(mProgram, "a_TextureCoordinates");
    }

    private void loadTexure(Context context, int backgroundId) {
        final int []textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            return;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        // Read in the resource
        final Bitmap bitmap = BitmapFactory.decodeResource(
                context.getResources(), backgroundId, options);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        bitmap.recycle();

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        mTexureId = textureObjectIds[0];
    }

    private void initProgram() {
        mProgram = GLES20.glCreateProgram();

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        GLES20.glShaderSource(fragmentShader, FRAGMENT_SHADER);
        GLES20.glShaderSource(vertexShader, VERTEXT_SHADER);

        GLES20.glCompileShader(fragmentShader);
        GLES20.glCompileShader(vertexShader);

        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glAttachShader(mProgram, vertexShader);

        GLES20.glLinkProgram(mProgram);
    }


    FloatBuffer positions;
    FloatBuffer texurePositions;
    List<Float> positionList;
    List<Float> texureList;

    private void initPosition() {

        float PartsCount = 32;

        double partsMDegreen = Math.PI / PartsCount;

        double partsNDegreen = Math.PI * 2 / PartsCount;


        positionList = new ArrayList<>();

        texureList = new ArrayList<>();

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


                positionList.add(x1);
                positionList.add(y1);
                positionList.add(z1);

                positionList.add(x2);
                positionList.add(y2);
                positionList.add(z2);

                positionList.add(x4);
                positionList.add(y4);
                positionList.add(z4);

                positionList.add(x1);
                positionList.add(y1);
                positionList.add(z1);

                positionList.add(x4);
                positionList.add(y4);
                positionList.add(z4);

                positionList.add(x3);
                positionList.add(y3);
                positionList.add(z3);



                float v1 = m / PartsCount;
                float u1 = n / PartsCount;

                float v2 = (m + 1) / PartsCount;
                float u2 = n / PartsCount;

                float v3 = (m + 1) / PartsCount;
                float u3 = (n + 1) / PartsCount;

                float v4 = m / PartsCount;
                float u4 =  (n + 1) / PartsCount;

                texureList.add(u1);
                texureList.add(v1);

                texureList.add(u2);
                texureList.add(v2);

                texureList.add(u3);
                texureList.add(v3);

                texureList.add(u1);
                texureList.add(v1);

                texureList.add(u3);
                texureList.add(v3);

                texureList.add(u4);
                texureList.add(v4);
            }
        }


        float []positionArray = new float[positionList.size()];
        for (int i = 0; i < positionArray.length; i++) {
            positionArray[i] = positionList.get(i);
        }

        float []texureArray = new float[texureList.size()];
        for (int i = 0; i < texureArray.length ; i++) {
            texureArray[i] = texureList.get(i);
        }


        positions = ByteBuffer.allocateDirect(positionArray.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(positionArray);
        positions.position(0);

        texurePositions = ByteBuffer.allocateDirect(texureArray.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(texureArray);
        texurePositions.position(0);
    }

    public void draw( float[] matrix) {

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_FRONT);

//        Matrix.setLookAtM(matrix, 0,
//                0, 0f, 0f,
//                0, 0, 1f,
//                0, 1f, 0);
//        Matrix.setIdentityM(mVMatrix, 0);

        GLES20.glUseProgram(mProgram);
        // 准备贴图数据
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexureId);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform4fv(colorHandler, 1, color, 0 );
        GLES20.glUniformMatrix4fv(matrixHandler,1, false, matrix, 0);
        GLES20.glUniform1i(textureUnitHandler, 0);

        // 启用一个指向三角形的顶点数组的handle
        GLES20.glEnableVertexAttribArray(positionHandler);
        // 准备三角形的坐标数据
        GLES20.glVertexAttribPointer(positionHandler, 3,
                GLES20.GL_FLOAT,
                false,
                3 * 4, positions);

        GLES20.glEnableVertexAttribArray(textureCoordinatesHanlder);
        GLES20.glVertexAttribPointer(textureCoordinatesHanlder,
                2,
                GLES20.GL_FLOAT,
                false,
                2 * 4,
                texurePositions);


        // 画三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, positionList.size() / 3);
//        GLES20.glDrawArrays(GLES20.GL_LINES, 0, positionList.size() / 3);

    }


}
