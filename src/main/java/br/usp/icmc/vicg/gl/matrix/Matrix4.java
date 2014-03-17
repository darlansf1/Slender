/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.usp.icmc.vicg.gl.matrix;

import java.util.Stack;
import javax.media.opengl.GL3;

/**
 *
 * @author PC
 */
public class Matrix4 {

    private Stack<float[]> stack;
    private GL3 gl;
    private int handle;
    private float[] matrix;

    public Matrix4() {
        matrix = new float[16];
        stack = new Stack<float[]>();
        loadIdentity();
    }

    public void init(final GL3 gl, int handle) {
        this.gl = gl;
        this.handle = handle;
    }

    public void bind() {
        gl.glUniformMatrix4fv(handle, 1, false, this.matrix, 0);
    }

    public static float[] multiplyVector3(float[] matrix, float[] vector) {
        float[] result = new float[3];

        for (int i = 0; i < 3; i++) {
            result[i] = matrix[i] * vector[0] + matrix[4 + i] * vector[1] + matrix[8 + i] * vector[2];
        }
        return result;
    }

    public final void loadIdentity() {
        for (int i = 0; i < this.matrix.length; i++) {
            this.matrix[i] = 0.0f;
        }

        this.matrix[0] = 1.0f;
        this.matrix[5] = 1.0f;
        this.matrix[10] = 1.0f;
        this.matrix[15] = 1.0f;
    }

    public void multiply(Matrix4 multMatrix) {
        float[] mat2 = multMatrix.matrix;

        // Cache the matrix values (makes for huge speed increases!)
        float a00 = this.matrix[ 0], a01 = this.matrix[ 1], a02 = this.matrix[ 2], a03 = this.matrix[3];
        float a10 = this.matrix[ 4], a11 = this.matrix[ 5], a12 = this.matrix[ 6], a13 = this.matrix[7];
        float a20 = this.matrix[ 8], a21 = this.matrix[ 9], a22 = this.matrix[10], a23 = this.matrix[11];
        float a30 = this.matrix[12], a31 = this.matrix[13], a32 = this.matrix[14], a33 = this.matrix[15];

        // Cache only the current line of the second matrix
        float b0 = mat2[0], b1 = mat2[1], b2 = mat2[2], b3 = mat2[3];
        this.matrix[0] = b0 * a00 + b1 * a10 + b2 * a20 + b3 * a30;
        this.matrix[1] = b0 * a01 + b1 * a11 + b2 * a21 + b3 * a31;
        this.matrix[2] = b0 * a02 + b1 * a12 + b2 * a22 + b3 * a32;
        this.matrix[3] = b0 * a03 + b1 * a13 + b2 * a23 + b3 * a33;

        b0 = mat2[4];
        b1 = mat2[5];
        b2 = mat2[6];
        b3 = mat2[7];
        this.matrix[4] = b0 * a00 + b1 * a10 + b2 * a20 + b3 * a30;
        this.matrix[5] = b0 * a01 + b1 * a11 + b2 * a21 + b3 * a31;
        this.matrix[6] = b0 * a02 + b1 * a12 + b2 * a22 + b3 * a32;
        this.matrix[7] = b0 * a03 + b1 * a13 + b2 * a23 + b3 * a33;

        b0 = mat2[8];
        b1 = mat2[9];
        b2 = mat2[10];
        b3 = mat2[11];
        this.matrix[8] = b0 * a00 + b1 * a10 + b2 * a20 + b3 * a30;
        this.matrix[9] = b0 * a01 + b1 * a11 + b2 * a21 + b3 * a31;
        this.matrix[10] = b0 * a02 + b1 * a12 + b2 * a22 + b3 * a32;
        this.matrix[11] = b0 * a03 + b1 * a13 + b2 * a23 + b3 * a33;

        b0 = mat2[12];
        b1 = mat2[13];
        b2 = mat2[14];
        b3 = mat2[15];
        this.matrix[12] = b0 * a00 + b1 * a10 + b2 * a20 + b3 * a30;
        this.matrix[13] = b0 * a01 + b1 * a11 + b2 * a21 + b3 * a31;
        this.matrix[14] = b0 * a02 + b1 * a12 + b2 * a22 + b3 * a32;
        this.matrix[15] = b0 * a03 + b1 * a13 + b2 * a23 + b3 * a33;
    }

    public void translate(float tx, float ty, float tz) {
        Matrix4 translate = new Matrix4();
        translate.loadIdentity();
        translate.matrix[12] = tx;
        translate.matrix[13] = ty;
        translate.matrix[14] = tz;
        multiply(translate);
    }
    
    public void scale(float sx, float sy, float sz) {
        Matrix4 scale = new Matrix4();
        scale.loadIdentity();
        scale.matrix[0] = sx;
        scale.matrix[5] = sy;
        scale.matrix[10] = sz;        
        multiply(scale);        
    }

    public void push() {
        stack.push(this.matrix.clone());
    }

    public void pop() {
        if (stack.size() == 0) {
            System.err.println("Matrix stack is empty.");
        } else {
            this.matrix = stack.pop();
        }
    }
}
