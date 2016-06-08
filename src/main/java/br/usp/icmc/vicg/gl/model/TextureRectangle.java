/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.vicg.gl.model;

import javax.media.opengl.GL;

/**
 *
 * @author paulovich
 */
public class TextureRectangle extends TextureSimpleModel {

  public TextureRectangle() {
    vertex_buffer = new float[]{
      -0.5f, -0.5f, 0.0f,
      0.5f, -0.5f, 0.0f,
      0.5f, 0.5f, 0.0f,
      
      0.5f, 0.5f, 0.0f,
      -0.5f, 0.5f, 0.0f,
      -0.5f, -0.5f, 0.0f};

    normal_buffer = new float[]{
      0, 0, 1,
      0, 0, 1,
      0, 0, 1,
      
      0, 0, 1,
      0, 0, 1,
      0, 0, 1
    };

    texture_buffer = new float[]{
      0, 0,
      1, 0,
      1, 1,
      
      1, 1,
      0, 1,
      0, 0
    };
  }
  
  public TextureRectangle(float xmin, float zmin, float xmax, float zmax) {
    vertex_buffer = new float[]{
      xmin, 0.0f, zmin,
      xmax, 0.0f, zmin,
      xmax, 0.0f, zmax,
      
      xmax, 0.0f, zmax,
      xmin, 0.0f, zmax,
      xmin, 0.0f, zmin};

    normal_buffer = new float[]{
      0, -1, 0,
      0, -1, 0,
      0, -1, 0,
      
      0, -1, 0,
      0, -1, 0,
      0, -1, 0
    };

    texture_buffer = new float[]{
      0, 0,
      1, 0,
      1, 1,
      
      1, 1,
      0, 1,
      0, 0
    };
  }

  @Override
  public void draw() {
    draw(GL.GL_TRIANGLES);
  }
}
