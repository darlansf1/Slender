/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.usp.icmc.vicg.gl.app;

import br.usp.icmc.vicg.gl.util.Shader;
import br.usp.icmc.vicg.gl.util.ShaderFactory;
import br.usp.icmc.vicg.gl.util.ShaderFactory.ShaderImplementation;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

/**
 *
 * @author paulovich
 */
public class Example01 implements GLEventListener {

    private final Shader vertex_shader; // Gerenciador do vertex shader
    private final Shader fragment_shader; // Gerenciador do fragment shader
    private int[] vbo; // Vertex Buffer Object
    private int nr_vertices;  // Numero de vertices no VBO

    public Example01() {
        // Carrega os shaders
        vertex_shader = ShaderFactory.getInstance(ShaderImplementation.TRANSFORM_VERTEX_SHADER);
        fragment_shader = ShaderFactory.getInstance(ShaderImplementation.SIMPLE_FRAGMENT_SHADER);
    }

    @Override
    public void init(GLAutoDrawable glad) {
        System.out.println("init()");
        GL3 gl = glad.getGL().getGL3();

        gl.glClearColor(0, 0, 0, 0);

        //inicializa os shaders
        vertex_shader.init(gl);
        fragment_shader.init(gl);

        //ativa os shaders
        vertex_shader.bind();
        fragment_shader.bind();

        //cria o objeto a ser desenhado
        nr_vertices = create_object(gl);
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        // Recupera o pipeline
        GL3 gl = glad.getGL().getGL3();

        // Apaga o buffer
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
        if (vbo[0] > 0) {
            gl.glDeleteBuffers(1, vbo, 0);
            vbo[0] = 0;
        }
    }

    @Override
    public void display(GLAutoDrawable glad) {
        // Recupera o pipeline
        GL3 gl = glad.getGL().getGL3();

        // Limpa o frame buffer com a cor definida
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT);

        // Desenha o buffer carregado em memória (triangulos)
        gl.glDrawArrays(GL3.GL_TRIANGLES, 0, nr_vertices);

        // Força execução das operações declaradas
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
    }

    private int create_object(GL3 gl) {
        // Triangle
        float[] TRIANGLE_VERTICES = new float[]{
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f,};

        // Cria um buffer contendo posicoes
        vbo = new int[1];
        gl.glGenBuffers(1, vbo, 0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, TRIANGLE_VERTICES.length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(TRIANGLE_VERTICES), GL3.GL_STATIC_DRAW);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);

        // Passa o buffer para o vertex shader
        int vertexPositions = vertex_shader.getAttribLocation("a_position");
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
        gl.glVertexAttribPointer(vertexPositions, 3, GL3.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(vertexPositions);

        return TRIANGLE_VERTICES.length / 3;
    }

    public static void main(String[] args) {
        // Define GL3 profile para trabalhar com OpenGL 3.0
        GLProfile profile = GLProfile.get(GLProfile.GL3);

        // Configuracoes
        GLCapabilities glcaps = new GLCapabilities(profile);
        glcaps.setDoubleBuffered(true);
        glcaps.setHardwareAccelerated(true);

        // Criar o canvas
        GLCanvas glCanvas = new GLCanvas(glcaps);

        // Adiciona o ouvinte ao canvas
        glCanvas.addGLEventListener(new Example01());

        // Adiciona o canvas em um Frame
        JFrame frame = new JFrame("Example 01");
        frame.setSize(600, 600);
        frame.getContentPane().add(glCanvas);
        final AnimatorBase animator = new FPSAnimator(glCanvas, 60);

        // Controla evento de fechamento do frame
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }

                }).start();
            }

        });

        // Torna o frame visivel e inicia o loop de animacao
        frame.setVisible(true);
        animator.start();
    }

}
