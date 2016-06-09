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
    private ArrayList<ArrayList<ArrayList<Float>>> positions;
    private final Matrix4 modelMatrix;
    private TextureRectangle rectangle;
    
    public Floor(float xmin, float ymin, float xmax, float ymax, float worldSize){
        this.modelMatrix = new Matrix4();
        this.rectangle = new TextureRectangle(xmin, ymin, xmax, ymax);
        this.positions = new ArrayList<>();

        int min = (int)(-worldSize/2);
        int max = -min;
        
        for(int x = min; x < max; x++){
            positions.add(new ArrayList<ArrayList<Float>>());
            for(int y = min; y < max; y++){
                positions.get(x-min).add(new ArrayList<Float>());
                positions.get(x-min).get(y-min).add((float)x);
                positions.get(x-min).get(y-min).add((float)y);
            }
        }
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

    public void draw(float x, float z, float delta){
        for(int i = 0; i < positions.size(); i++)
            for(int j = 0; j < positions.get(i).size(); j++){
                if(positions.get(i).get(j).get(0) >= x-delta && positions.get(i).get(j).get(0) <= x+delta
                        && positions.get(i).get(j).get(1) >= z-delta && positions.get(i).get(j).get(1) <= z+delta)
                    draw(positions.get(i).get(j).get(0), positions.get(i).get(j).get(1));
            }
    }
    
    public void draw(float x, float z) {
        modelMatrix.loadIdentity();
        modelMatrix.translate(x, -2f, z);
        modelMatrix.rotate(180, 1.0f, 0.0f, 0.0f);
        modelMatrix.scale(1.0f, 1.0f, 1.0f);
        modelMatrix.bind();
        rectangle.bind();
        rectangle.draw();
    }

    public void dispose() {
        rectangle.dispose();
    }
}
