/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.vicg.gl.util;

/**
 *
 * @author paulovich
 */
public class ShaderFactory {

  public enum ShaderType {

    SIMPLE_SHADER 
  };

  public static Shader getInstance(ShaderType type) {
    if (type == ShaderType.SIMPLE_SHADER) {
      return new Shader("simple_vertex.glsl",
              "simple_fragment.glsl", null, null, null);
    }
    return null;
  }
}
