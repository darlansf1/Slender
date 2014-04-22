
package br.usp.icmc.vicg.gl.core;

import javax.media.opengl.GL3;

import com.jogamp.common.nio.Buffers;
import java.util.Arrays;

public class Light {

    private GL3 gl;

    private float[] ambientColor;
    private float[] diffuseColor;
    private float[] specularColor;
    private float[] position;

    private int positionHandle;
    private int ambientColorHandle;
    private int diffuseColorHandle;
    private int specularColorHandle;

    public Light(float[] position, float[] ambientColor, float[] diffuseColor,
            float[] specularColor) {
        setPosition(position);
        setAmbientColor(ambientColor);
        setDiffuseColor(diffuseColor);
        setSpecularColor(specularColor);
    }

    public Light() {
        this(new float[]{0.0f, 0.0f, 1.0f, 0.0f},
                new float[]{0.0f, 0.0f, 0.0f, 1.0f},
                new float[]{1.0f, 1.0f, 1.0f, 1.0f},
                new float[]{1.0f, 1.0f, 1.0f, 1.0f});
    }

    public final void setPosition(float[] position) {
        this.position = Arrays.copyOf(position, position.length);
    }

    public final void setAmbientColor(float[] ambientColor) {
        this.ambientColor = Arrays.copyOf(ambientColor, ambientColor.length);
    }

    public final void setDiffuseColor(float[] diffuseColor) {
        this.diffuseColor = Arrays.copyOf(diffuseColor, diffuseColor.length);
    }

    public final void setSpecularColor(float[] specularColor) {
        this.specularColor = Arrays.copyOf(specularColor, specularColor.length);
    }

    public void init(GL3 gl, int positionHandle, int ambientColorHandle,
            int diffuseColorHandle, int specularColorHandle) {
        this.gl = gl;
        this.positionHandle = positionHandle;
        this.ambientColorHandle = ambientColorHandle;
        this.diffuseColorHandle = diffuseColorHandle;
        this.specularColorHandle = specularColorHandle;
    }

    public void bind() {
        gl.glUniform4fv(positionHandle, 1, Buffers.newDirectFloatBuffer(position));
        gl.glUniform4fv(ambientColorHandle, 1, Buffers.newDirectFloatBuffer(ambientColor));
        gl.glUniform4fv(diffuseColorHandle, 1, Buffers.newDirectFloatBuffer(diffuseColor));
        gl.glUniform4fv(specularColorHandle, 1, Buffers.newDirectFloatBuffer(specularColor));
    }

}
