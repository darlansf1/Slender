package br.usp.icmc.vicg.gl.app;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import br.usp.icmc.vicg.gl.util.ShadersManager;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;

public class Example01 implements GLEventListener {

    public static final int FPS = 60;
    public static final String SHADERS_FOLDER = "shaders/1/";
    
    // Triangle
    private static final float[] TRIANGLE_VERTICES = new float[] {
        -0.5f, -0.5f, 0.0f, 
        0.5f, -0.5f, 0.0f, 
        0.0f, 0.5f, 0.0f,
    };
    
    // OpenGL Pipeline Object
    private GL3 gl;
    
    // Shaders Manager
    private ShadersManager shadersManager;

    // GLCanvas
    private GLCanvas glCanvas;

    // Vertex Buffer Object
    private int[] vbo;

    public Example01() {
        // Get profile
        GLProfile profile = GLProfile.getDefault();

        // Configurations
        GLCapabilities glcaps = new GLCapabilities(profile);
        glcaps.setDoubleBuffered(true);
        glcaps.setHardwareAccelerated(true);

        // Create canvas
        glCanvas = new GLCanvas(glcaps);

        // Add listener to panel
        glCanvas.addGLEventListener(this);
        
        // Load shaders
        shadersManager = new ShadersManager(SHADERS_FOLDER);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // Get pipeline
        gl = drawable.getGL().getGL3();

        // Print OpenGL version
        System.out.println("OpenGL Version: " + gl.glGetString(GL.GL_VERSION) + "\n");

        // Set background color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        shadersManager.init(gl);
        shadersManager.bind();

        // create vertex positions buffer
        vbo = new int[1];
        gl.glGenBuffers(1, vbo, 0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, TRIANGLE_VERTICES.length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(TRIANGLE_VERTICES), GL3.GL_STATIC_DRAW);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);

        // pass buffer to shader program
        int vertexPositions = shadersManager.getAttribLocation("a_position");
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
        gl.glVertexAttribPointer(vertexPositions, 3, GL3.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(vertexPositions);
    }


    @Override
    public void display(GLAutoDrawable drawable) {
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT);
        
        gl.glDrawArrays(GL3.GL_TRIANGLES, 0, TRIANGLE_VERTICES.length / 3);
        
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}

    @Override
    public void dispose(GLAutoDrawable drawable) {
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
        if (vbo[0] > 0) {
            gl.glDeleteBuffers(1, vbo, 0);
            vbo[0] = 0;
        }        
    }

    public void run(String title, int fps) {
        Frame frame = new Frame(title);
        frame.setSize(800, 600);
        frame.add(this.glCanvas);
        final AnimatorBase animator = new FPSAnimator(this.glCanvas, fps);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        frame.setVisible(true);
        animator.start();
    }

    public static void main(String[] args) {
        // Run example
        Example01 example01 = new Example01();
        example01.run("Example 01", FPS);
    }
}
