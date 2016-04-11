package br.usp.icmc.vicg.gl.app;

import br.usp.icmc.vicg.gl.core.Light;
import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
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

import br.usp.icmc.vicg.gl.util.Shader;
import br.usp.icmc.vicg.gl.util.ShaderFactory;
import br.usp.icmc.vicg.gl.util.ShaderFactory.ShaderType;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scene extends KeyAdapter implements GLEventListener {

  private final Shader shader; // Gerenciador dos shaders
 // private final Matrix4 modelMatrix;
  private final Matrix4 projectionMatrix;
  private final Matrix4 viewMatrix;
  private final Scenario scenario;
  private final Slender slender;
  private final Light light;
  private float aspect;
  private float alpha;
  private float beta;
  private float delta;

  public Scene(float aspect) {
    this.aspect = aspect;
    // Carrega os shaders
    shader = ShaderFactory.getInstance(ShaderType.COMPLETE_SHADER);
   // modelMatrix = new Matrix4();
    projectionMatrix = new Matrix4();
    viewMatrix = new Matrix4();

    //model = new JWavefrontObject(new File("./data/VW-new-beetle.obj"));
    scenario = new Scenario();
    slender = new Slender();
    light = new Light();

    alpha = 0;
    beta = 0;
    delta = 20;
  }
  
  public void setAspect(float aspect){
      this.aspect = aspect;
  }

  @Override
  public void init(GLAutoDrawable drawable) {
    // Get pipeline
    GL3 gl = drawable.getGL().getGL3();

    // Print OpenGL version
    System.out.println("OpenGL Version: " + gl.glGetString(GL.GL_VERSION) + "\n");

    gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    gl.glClearDepth(1.0f);

    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glEnable(GL.GL_CULL_FACE);

    //inicializa os shaders
    shader.init(gl);

    //ativa os shaders
    shader.bind();

    //inicializa a matrix Model and Projection
    //modelMatrix.init(gl, shader.getUniformLocation("u_modelMatrix"));
    projectionMatrix.init(gl, shader.getUniformLocation("u_projectionMatrix"));
    viewMatrix.init(gl, shader.getUniformLocation("u_viewMatrix"));

    initModel(gl, slender.getModelMatrix(), slender.getModel());
    initModel(gl, scenario.getModelMatrix(), scenario.getModel());
    scenario.getWorld().init(gl, shader);

    //init the light
    light.setPosition(new float[]{10, 10, 50, 1.0f});
    light.setAmbientColor(new float[]{0.1f, 0.1f, 0.1f, 1.0f});
    light.setDiffuseColor(new float[]{0.75f, 0.75f, 0.75f, 1.0f});
    light.setSpecularColor(new float[]{0.7f, 0.7f, 0.7f, 1.0f});
    light.init(gl, shader);
  }
  
  private void initModel(GL3 gl, Matrix4 modelMatrix, JWavefrontObject model){
    modelMatrix.init(gl, shader.getUniformLocation("u_modelMatrix"));
    try {
        //init the model
        model.init(gl, shader);
        model.unitize();
        model.dump();
    } catch (IOException ex) {
        Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  
  @Override
  public void display(GLAutoDrawable drawable) {
    // Recupera o pipeline
    GL3 gl = drawable.getGL().getGL3();

    // Limpa o frame buffer com a cor definida
    gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

    //float x = alpha*(float)(Math.cos(Math.toRadians(beta))-Math.sin(Math.toRadians(beta)));
    float x = (float)(alpha*Math.cos(Math.toRadians(beta)));
    float y = 0.1f;
    //float z = alpha*(float)(Math.cos(Math.toRadians(beta))+Math.sin(Math.toRadians(beta)));
    float z = (float)(alpha*Math.sin(Math.toRadians(beta)));
    
    projectionMatrix.loadIdentity();
    projectionMatrix.perspective(45, this.aspect, 0.1f, delta);
    /*projectionMatrix.ortho(
            -delta, delta, 
            -delta, delta, 
            -20 * delta, 20 * delta);*/
    projectionMatrix.bind();

    viewMatrix.loadIdentity();
    viewMatrix.lookAt(
            0, 0, 0, //onde vc esta
            //(float)(Math.cos(Math.toRadians(beta))-Math.sin(Math.toRadians(beta))), 0, (float)(Math.cos(Math.toRadians(beta))+Math.sin(Math.toRadians(beta))), //pra onde olha
            (float)(Math.cos(Math.toRadians(beta))), 0,(float)(Math.sin(Math.toRadians(beta))),
            0, 1, 0); //pra cima
    viewMatrix.translate(x, y, z);
    viewMatrix.bind();
    light.bind();
    
   scenario.draw(-x, -z, delta);
    
    slender.draw(/*beta, alpha*/-x, -z);

    // ForÃ§a execuÃ§Ã£o das operaÃ§Ãµes declaradas
    gl.glFlush();
  }

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
    slender.dispose();
    scenario.dispose();
  }

  @Override
  public void keyPressed(KeyEvent e) {

    switch (e.getKeyCode()) {
      case KeyEvent.VK_PAGE_UP://faz zoom-in
        delta = delta * 0.809f;
        break;
      case KeyEvent.VK_PAGE_DOWN://faz zoom-out
        delta = delta * 1.1f;
        break;
      case KeyEvent.VK_UP://gira sobre o eixo-x
        alpha = alpha - 0.1f;
        break;
      case KeyEvent.VK_DOWN://gira sobre o eixo-x
        alpha = alpha + 0.1f;
        break;
      case KeyEvent.VK_LEFT://gira sobre o eixo-y
        beta = beta - 1;
        break;
      case KeyEvent.VK_RIGHT://gira sobre o eixo-y
        beta = beta + 1;
        break;
    }
  }

  public static void main(String[] args) {
    // Get GL3 profile (to work with OpenGL 4.0)
    GLProfile profile = GLProfile.get(GLProfile.GL3);

    // Configurations
    GLCapabilities glcaps = new GLCapabilities(profile);
    glcaps.setDoubleBuffered(true);
    glcaps.setHardwareAccelerated(true);

    // Create canvas
    GLCanvas glCanvas = new GLCanvas(glcaps);

    int width = 600, height = 600;
    
    // Add listener to panel
    final Scene listener = new Scene((float)width/height);
    glCanvas.addGLEventListener(listener);

    final Frame frame = new Frame("Example 09");
    frame.setSize(width, height);
    frame.add(glCanvas);
    frame.addKeyListener(listener);
    final AnimatorBase animator = new FPSAnimator(glCanvas, 60);

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
    
    frame.addComponentListener(new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
            listener.setAspect((float)frame.getWidth()/frame.getHeight());
        }
     });
    frame.setVisible(true);
    animator.start();
  }
}
