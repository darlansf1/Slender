/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ex;

import br.usp.icmc.vicg.gl.core.Light;
import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.model.SimpleModel;
import br.usp.icmc.vicg.gl.model.Sphere;
import br.usp.icmc.vicg.gl.model.WiredCube;
import br.usp.icmc.vicg.gl.util.Shader;
import br.usp.icmc.vicg.gl.util.ShaderFactory;
import java.io.File;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;



/**
 *
 * @author Darlan
 */
public class SimpleScene implements GLEventListener{
    
    private Shader shader;
    private SimpleModel cube;
    private SimpleModel sphere;
    private Matrix4 modelMatrix;
    private int colorHandle;
    
    public SimpleScene(){
        shader = ShaderFactory.getInstance(ShaderFactory.ShaderType.MODEL_MATRIX_SHADER);
        cube = new WiredCube();
        sphere = new Sphere();
        modelMatrix = new Matrix4();
    }

    @Override
    public void init(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3();
        
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        shader.init(gl);
        shader.bind();
        
        cube.init(gl, shader);
        sphere.init(gl, shader);
        modelMatrix.init(gl, shader.getUniformLocation("u_modelMatrix"));
        colorHandle = shader.getUniformLocation("u_color");
    }

    private float delta1 = 0.0f;
    private float inc1 = 1.4f;
    private float delta2 = 0.0f;
    private float inc2 = 1.3f;
    private float delta3 = 0.0f;
    private float inc3 = 1.2f;
    private float delta4 = 0.0f;
    private float inc4 = 1.1f;
    private float delta5 = 0.0f;
    private float inc5 = 1f;
    private float delta6 = 0.0f;
    private float inc6 = 0.9f;
    private float delta7 = 0.0f;
    private float inc7 = 0.8f;
    private float delta8 = 0.0f;
    private float inc8 = 0.7f;
    private float delta9 = 0.0f;
    private float inc9 = 0.6f;
    private float k = 0.5f;
    @Override
    public void display(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3();
        
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        
        gl.glUniform3f(colorHandle, 1f, 1f, 0);
        
        modelMatrix.loadIdentity();
        modelMatrix.rotate(45f, 1f, 0, 1f);
        modelMatrix.translate(0.0f, 0.0f, 0.0f);
        modelMatrix.rotate(delta9, 1.0f, 1.0f, 1.0f);
        modelMatrix.scale(0.1f, 0.1f, 0.1f);
        modelMatrix.bind();
        
        sphere.bind();
        sphere.draw();
        
        delta1 += inc1;
        delta2 += inc2;
        delta3 += inc3;
        delta4 += inc4;
        delta5 += inc5;
        delta6 += inc6;
        delta7 += inc7;
        delta8 += inc8;
        delta9 += inc9;
        
        gl.glUniform3f(colorHandle, .7f, 0.61f, 0);
        
        modelMatrix.loadIdentity();
        modelMatrix.rotate(45f, 1f, 0, 1f);
        modelMatrix.rotate(delta1, 0.0f, 0.0f, 1.0f);
        modelMatrix.translate(0.2f, 0.0f, 0.2f);
        modelMatrix.scale(0.015f, 0.015f, 0.015f);
        modelMatrix.bind();
        
        sphere.bind();
        sphere.draw();
        
        gl.glUniform3f(colorHandle, .94f, 0.5f, 0.04f);
        modelMatrix.loadIdentity();
        modelMatrix.rotate(45f, 1f, 0, 1f);
        modelMatrix.rotate(delta2, 0.0f, 0.0f, 1.0f);
        modelMatrix.translate(0.3f, 0.0f, 0.3f);
        modelMatrix.scale(0.01f, 0.01f, 0.01f);
        modelMatrix.bind();
        
        sphere.bind();
        sphere.draw();
        
        gl.glUniform3f(colorHandle, 0.10f, .75f, 0.97F);
        
        float delta = delta3;
        
        modelMatrix.loadIdentity();
        modelMatrix.rotate(45f, 1f, 0, 1f);
        modelMatrix.rotate(delta3, 0.0f, 0.0f, 1.0f);
        modelMatrix.translate(0.4f, 0.0f, 0.4f);
        modelMatrix.scale(0.02f, 0.02f, 0.02f);
        modelMatrix.bind();
        
        sphere.bind();
        sphere.draw();
        
        gl.glUniform3f(colorHandle, .98f, 0.38f, 0.3f);
        
        modelMatrix.loadIdentity();
        modelMatrix.rotate(45f, 1f, 0, 1f);
        modelMatrix.rotate(delta4, 0.0f, 0.0f, 1.0f);
        modelMatrix.translate(0.45f, 0.0f, 0.45f);
        modelMatrix.scale(0.01f, 0.01f, 0.01f);
        modelMatrix.bind();
        
        sphere.bind();
        sphere.draw();
        
        gl.glUniform3f(colorHandle, .5f, 0.26f, 0.02f);
        modelMatrix.loadIdentity();
        modelMatrix.rotate(45f, 1f, 0, 1f);
        modelMatrix.rotate(delta5, 0.0f, 0.0f, 1.0f);
        modelMatrix.translate(0.65f, 0.0f, 0.65f);
        modelMatrix.scale(0.05f, 0.05f, 0.05f);
        modelMatrix.bind();
        
        sphere.bind();
        sphere.draw();
        
        gl.glUniform3f(colorHandle, .06f, 0.52f, 0.68f);
        modelMatrix.loadIdentity();
        modelMatrix.rotate(45f, 1f, 0, 1f);
        modelMatrix.rotate(delta6, 0.0f, 0.0f, 1.0f);
        modelMatrix.translate(0.75f, 0.0f, 0.75f);
        modelMatrix.scale(0.04f, 0.04f, 0.04f);
        modelMatrix.bind();
        
        sphere.bind();
        sphere.draw();
        
        gl.glUniform3f(colorHandle, 0f, 0.76f, 0.76f);
        modelMatrix.loadIdentity();
        modelMatrix.rotate(45f, 1f, 0, 1f);
        modelMatrix.rotate(delta7, 0.0f, 0.0f, 1.0f);
        modelMatrix.translate(0.85f, 0.0f, 0.85f);
        modelMatrix.scale(0.03f, 0.03f, 0.03f);
        modelMatrix.bind();
        
        sphere.bind();
        sphere.draw();
        
        gl.glUniform3f(colorHandle, .18f, 0.04f, 0.57f);
        modelMatrix.loadIdentity();
        modelMatrix.rotate(45f, 1f, 0, 1f);
        modelMatrix.rotate(delta8, 0.0f, 0.0f, 1.0f);
        modelMatrix.translate(0.9f, 0.0f, 0.9f);
        modelMatrix.scale(0.03f, 0.03f, 0.03f);
        modelMatrix.bind();
        
        sphere.bind();
        sphere.draw();
        
        gl.glUniform3f(colorHandle, .77f, 0.3f, 0.55f);
        modelMatrix.loadIdentity();
        modelMatrix.rotate(45f, 1f, 0, 1f);
        modelMatrix.rotate(delta9, 0.0f, 0.0f, 1.0f);
        modelMatrix.translate(0.99f, 0.0f, 0.99f);
        modelMatrix.scale(0.01f, 0.01f, 0.01f);
        modelMatrix.bind();
        
        sphere.bind();
        sphere.draw();
        
      /*  modelMatrix.loadIdentity();
        modelMatrix.translate(0.0f, 0.6f, 0.6f);
        modelMatrix.rotate(30f+30*delta, 1.0f, 1.0f, 1.0f);
        modelMatrix.scale(0.1f, 0.1f, 0.1f);
        modelMatrix.bind();
        
        sphere.bind();
        sphere.draw();
        
        if(delta >= 0.5 || delta <= 0)
            inc *= -1;
        
        delta+= inc;
        
        System.out.println(delta);
        
        modelMatrix.loadIdentity();
        modelMatrix.translate(0.0f, delta, 0f);
        modelMatrix.rotate(0.030f, 1.0f, 1.0f, 1.0f);
        modelMatrix.scale(0.1f, 0.1f, 0.1f);
        modelMatrix.bind();
        
        sphere.bind();
        sphere.draw();*/
    }
    
    @Override
    public void dispose(GLAutoDrawable glad) {
        
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
        
    }
}
