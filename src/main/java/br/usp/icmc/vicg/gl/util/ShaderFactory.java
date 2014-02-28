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

    public enum ShaderImplementation {

        SIMPLE_VERTEX_SHADER,
        SIMPLE_FRAGMENT_SHADER,
        TRANSFORM_VERTEX_SHADER
    };

    public static Shader getInstance(ShaderImplementation type) {
        if (type == ShaderImplementation.SIMPLE_VERTEX_SHADER) {
            return new Shader("simple_vertex.glsl", Shader.ShaderType.GL_VERTEX_SHADER);
        } else if (type == ShaderImplementation.SIMPLE_FRAGMENT_SHADER) {
            return new Shader("simple_fragment.glsl", Shader.ShaderType.GL_FRAGMENT_SHADER);
        } else if (type == ShaderImplementation.TRANSFORM_VERTEX_SHADER) {
            return new Shader("transform_vertex.glsl", Shader.ShaderType.GL_VERTEX_SHADER);
        }
        return null;
    }

}
