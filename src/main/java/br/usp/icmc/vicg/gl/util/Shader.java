
package br.usp.icmc.vicg.gl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL3;

import com.jogamp.common.nio.Buffers;

public class Shader {

    private GL3 gl;
    private int handle = -1;

    private final String filename;
    private final ShaderType shader_type;

    public enum ShaderType {

        GL_VERTEX_SHADER(GL3.GL_VERTEX_SHADER),
        GL_FRAGMENT_SHADER(GL3.GL_FRAGMENT_SHADER),
        GL_GEOMETRY_SHADER(GL3.GL_GEOMETRY_SHADER),
        GL_TESS_CONTROL_SHADER(GL3.GL_TESS_CONTROL_SHADER),
        GL_TESS_EVALUATION_SHADER(GL3.GL_TESS_EVALUATION_SHADER);

        private final int type;

        private ShaderType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

    };

    public Shader(String filename, ShaderType shader_type) {
        this.filename = filename;
        this.shader_type = shader_type;
    }

    public void init(final GL3 gl) {
        this.gl = gl;

        //read source
        String source = readSource();

        //compile the shader
        int shader_handle = compileSource(source, shader_type);

        //link the shader
        handle = linkShader(shader_handle);

        if (handle >= 0) {
            gl.glDeleteShader(shader_handle);
        }
    }

    public void bind() {
        gl.glUseProgram(handle);
    }

    public void dispose() {
        gl.glDeleteProgram(handle);
    }

    public int getUniformLocation(String varName) {
        int location = gl.glGetUniformLocation(handle, varName);
        if (location < 0) {
            System.err.println(varName + " uniform not found.");
            return -1;
        } else {
            System.out.println(varName + " uniform found.");
            return location;
        }
    }

    public int getAttribLocation(String varName) {
        int location = gl.glGetAttribLocation(handle, varName);
        if (location < 0) {
            System.err.println(varName + " attribute not found");
            return -1;
        } else {
            System.out.println(varName + " attribute found");
            return location;
        }
    }

    private String readSource() {
        InputStream input = getClass().getClassLoader().getResourceAsStream(filename);
        final StringBuilder source = new StringBuilder();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                source.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (final IOException e) {
            System.err.println("Invalid shader file.");
        }
        return source.toString();
    }

    private int compileSource(final String source, final ShaderType type) {
        final IntBuffer buffer = Buffers.newDirectIntBuffer(1);
        final int handle_aux = gl.glCreateShader(type.getType());

        gl.glShaderSource(handle_aux, 1, new String[]{source}, null, 0);
        gl.glCompileShader(handle_aux);

        gl.glGetShaderiv(handle_aux, GL3.GL_COMPILE_STATUS, buffer);

        if (buffer.get(0) == 1) {
            return handle_aux;
        } else {
            gl.glGetShaderiv(handle_aux, GL3.GL_INFO_LOG_LENGTH, buffer);

            final ByteBuffer byteBuffer = Buffers.newDirectByteBuffer(buffer
                    .get(0));
            gl.glGetShaderInfoLog(handle_aux, byteBuffer.capacity(), buffer,
                    byteBuffer);

            System.err.println("\nshader compile error: ");
            for (int i = 0; i < buffer.get(0); i++) {
                System.err.print((char) byteBuffer.get(i));
            }

            return -1;
        }
    }

    private int linkShader(int shadersHandle) {
        final int programHandle = gl.glCreateProgram();

        if (shadersHandle >= 0) {
            gl.glAttachShader(programHandle, shadersHandle);
        }

        gl.glLinkProgram(programHandle);
        gl.glValidateProgram(programHandle);

        final IntBuffer buffer = Buffers.newDirectIntBuffer(1);
        gl.glGetProgramiv(programHandle, GL3.GL_VALIDATE_STATUS, buffer);

        if (buffer.get(0) == 1) {
            return programHandle;
        } else {
            gl.glGetProgramiv(programHandle, GL3.GL_INFO_LOG_LENGTH, buffer);

            final ByteBuffer byteBuffer = Buffers.newDirectByteBuffer(buffer
                    .get(0));
            gl.glGetProgramInfoLog(programHandle, byteBuffer.capacity(), buffer,
                    byteBuffer);

            System.err.println("\nshader link error: ");
            for (int i = 0; i < buffer.get(0); i++) {
                System.err.print((char) byteBuffer.get(i));
            }

            return -1;
        }
    }

}
