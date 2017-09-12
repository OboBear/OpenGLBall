package com.example.obo.opengl;

import android.content.Context;
import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by obo on 2017/9/11.
 */

public class GLESUtil {

    private int mProgram;

    private int mPositionHandler;
    private int mTextureHandler;
    private int mAttributeMatrixHandler;

    public void initProgram(Context context) {
        this.mProgram = GLES20.glCreateProgram();
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        String fragmentShaderSource = getShaderSource(context, "");

    }


    private String getShaderSource(Context context ,String shaderFileName) {
        try {
            StringBuffer shaderStringBuffer = new StringBuffer();
            InputStream shaderInputStream = context.getAssets().open(shaderFileName);
            Reader reader = new InputStreamReader(shaderInputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String inputLine;
            bufferedReader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
