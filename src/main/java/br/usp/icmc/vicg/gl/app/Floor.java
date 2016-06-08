/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.vicg.gl.app;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.model.Rectangle;
import br.usp.icmc.vicg.gl.model.SimpleModel;
import br.usp.icmc.vicg.gl.model.TextureRectangle;
import br.usp.icmc.vicg.gl.util.Shader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL3;

/**
 *
 * @author Leonardo
 */
public class Floor{
    private final Matrix4 modelMatrix;
    private TextureRectangle rectangle;
    
    public Floor(float xmin, float ymin, float xmax, float ymax){
        this.modelMatrix = new Matrix4();
        this.rectangle = new TextureRectangle(xmin, ymin, xmax, ymax);
    }
    
    public void init(GL3 gl, Shader shader){
        modelMatrix.init(gl, shader.getUniformLocation("u_modelMatrix"));
        rectangle.init(gl, shader);
    }
    
    public void loadTexture(String filename){
        try {
          rectangle.loadTexture(filename);
      } catch (IOException ex) {
          ex.printStackTrace();
      }
    }

    public void draw() {
        modelMatrix.loadIdentity();
        modelMatrix.translate(0.0f, -1.6f, 0.0f);
        modelMatrix.rotate(180, 1.0f, 0.0f, 0.0f);
        modelMatrix.scale(100.0f, 100.0f, 100.0f);
        modelMatrix.bind();
        rectangle.bind();
        rectangle.draw();
    }

    public void dispose() {
        rectangle.dispose();
    }
}
