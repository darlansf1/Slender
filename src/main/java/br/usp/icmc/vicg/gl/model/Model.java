/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.usp.icmc.vicg.gl.model;

import com.jogamp.common.nio.Buffers;
import javax.media.opengl.GL3;

/**
 *
 * @author PC
 */
public class Model {

    private GL3 gl;
    private int handle;
    private int[] vbo; // Vertex Buffer Object
    protected float[] vertices;

    public void draw(int primitive) {
        // Desenha o buffer carregado em memória (triangulos)
        gl.glDrawArrays(primitive, 0, vertices.length / 3);
    }

    public void init(GL3 gl, int handle) {
        this.gl = gl;
        this.handle = handle;
        create_object(gl);
    }  
    
    public void bind() {
         // pass buffer to shader program
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
        gl.glVertexAttribPointer(handle, 3, GL3.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(handle);
    }

    public void dispose() {
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
        if (vbo[0] > 0) {
            gl.glDeleteBuffers(1, vbo, 0);
            vbo[0] = 0;
        }
    }

    private void create_object(GL3 gl) {
        // create vertex positions buffer
        vbo = new int[1];
        gl.glGenBuffers(1, vbo, 0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertices.length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(vertices), GL3.GL_STATIC_DRAW);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }

}
