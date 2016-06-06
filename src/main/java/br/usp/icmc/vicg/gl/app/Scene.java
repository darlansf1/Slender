package br.usp.icmc.vicg.gl.app;

import br.usp.icmc.vicg.gl.core.Light;
import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.model.Rectangle;
import br.usp.icmc.vicg.gl.model.Sphere;
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
import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;


public class Scene extends KeyAdapter implements GLEventListener {

  private final Shader shader; // Gerenciador dos shaders
 // private final Matrix4 modelMatrix;
  private final Matrix4 projectionMatrix;
  private final Matrix4 viewMatrix;
  private final Scenario scenario;
  private final Slender slender;
  private final Border border;
  private final AbandonedHouse house;
  private final Light light;
  private float aspect;
  private float alpha;
  private float beta;
  private float delta;
  private SoundEffects sounds;

  public Scene(float aspect) {
    this.aspect = aspect;
    // Carrega os shaders
    shader = ShaderFactory.getInstance(ShaderType.SPOTLIGHT_SHADER);
    //shader = ShaderFactory.getInstance(ShaderType.SPOTLIGHT_SHADER);
   // modelMatrix = new Matrix4();
    projectionMatrix = new Matrix4();
    viewMatrix = new Matrix4();

    //model = new JWavefrontObject(new File("./data/VW-new-beetle.obj"));
    scenario = new Scenario(-100.0f, 100.0f);
    slender = new Slender();
    border = new Border();
    house = new AbandonedHouse();
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

    //gl.glClearColor(0.025f, 0.025f, 0.025f, 1.0f);
    gl.glClearColor(0.025f, 0.025f, 0.025f, 1.0f);
    gl.glClearDepth(1.0f);

    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glEnable(GL.GL_CULL_FACE);
    gl.glCullFace(GL.GL_BACK);

    //inicializa os shaders
    shader.init(gl);

    //ativa os shaders
    shader.bind();

    //inicializa a matrix Model and Projection
    //modelMatrix.init(gl, shader.getUniformLocation("u_modelMatrix"));
    projectionMatrix.init(gl, shader.getUniformLocation("u_projectionMatrix"));
    viewMatrix.init(gl, shader.getUniformLocation("u_viewMatrix"));

    initModel(gl, slender.getModelMatrix(), slender.getModel());
    //initModel(gl, border.getModelMatrix(), border.getModel());
    initModel(gl, house.getModelMatrix(), house.getModel());
    initModel(gl, scenario.getModelMatrix(), scenario.getModel());
    scenario.getWorld().init(gl, shader);

    //init the light
    light.setPosition(new float[]{0.0f, 0.0f, 0.0f, 1.0f});
    light.setAmbientColor(new float[]{0.0f, 0.0f, 0.0f, 1.0f});
    light.setDiffuseColor(new float[]{0.8f, 0.8f, 0.8f, 1.0f});
    light.setSpecularColor(new float[]{0.1f, 0.1f, 0.1f, 1.0f});
    light.setConstantAttenuation(0.9f);
    light.setLinearAttenuation(0.1f);
    light.setQuadraticAttenuation(0.03f);
    /*light.setPosition(new float[]{0.0f, 0.0f, 0.0f, 1.0f});
    light.setDirection(new float[]{0.0f, 0.0f, 1.0f, 1.0f});
    light.setAmbientColor(new float[]{0.0f, 0.0f, 0.0f, 1.0f});
    light.setDiffuseColor(new float[]{0.2f, 0.2f, 0.2f, 1.0f});
    light.setSpecularColor(new float[]{0.1f, 0.1f, 0.1f, 1.0f});
    light.setQuadraticAttenuation(0.2f);
    light.setCutoffAngle(90.0f);*/
    light.init(gl, shader);
    
    sounds = new SoundEffects();
    sounds.playSoundEffects();
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

  float x = 0;
  float z = 0;
  float deltax = 0, deltaz = 0;
  @Override
  public void display(GLAutoDrawable drawable) {
    // Recupera o pipeline
    GL3 gl = drawable.getGL().getGL3();

    // Limpa o frame buffer com a cor definida
    gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

    //float x = alpha*(float)(Math.cos(Math.toRadians(beta))-Math.sin(Math.toRadians(beta)));
    float cosBeta = (float)Math.cos(Math.toRadians(beta));
    float sinBeta = (float)Math.sin(Math.toRadians(beta));
    float newx = x+(alpha*cosBeta);
    float y = 0.5f;
    //float z = alpha*(float)(Math.cos(Math.toRadians(beta))+Math.sin(Math.toRadians(beta)));
    float newz = z+(alpha*sinBeta);
    
    //System.out.println("alpha: "+alpha);
    //System.out.println("beta: "+beta);
    //System.out.println("x: "+x);
    //System.out.println("z: "+z);
    float deltax = newx-x;
    float deltaz = newz-z;
    if(!scenario.checkCollision(-newx, -newz)){
        x = newx;
        z = newz;
    }else{
        deltax = 0;
        deltaz = 0;
    }
    sounds.setDeltax(deltax);
    sounds.setDeltaz(deltaz);
    alpha = 0;
    
    projectionMatrix.loadIdentity();
    projectionMatrix.perspective(60, this.aspect, 0.01f, delta);
    /*projectionMatrix.ortho(
            -delta, delta, 
            -delta, delta, 
            -20 * delta, 20 * delta);*/
    projectionMatrix.bind();

    viewMatrix.loadIdentity();
    viewMatrix.lookAt(
            0, 0, 0, //onde vc esta
            //(float)(Math.cos(Math.toRadians(beta))-Math.sin(Math.toRadians(beta))), 0, (float)(Math.cos(Math.toRadians(beta))+Math.sin(Math.toRadians(beta))), //pra onde olha
            cosBeta, -0.1f,sinBeta,
            0, 1, 0); //pra cima
    viewMatrix.translate(x, y, z);
    viewMatrix.bind();
    light.bind();
    
    scenario.draw(-x, -z, delta);
    
    slender.draw(/*beta, alpha*/-x, -z, cosBeta, sinBeta, delta);
    //border.draw();
    house.draw(20.0f, 0, 0);
    

    // ForÃ§a execuÃ§Ã£o das operaÃ§Ãµes declaradas
    gl.glFlush();
  }

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
    slender.dispose();
    border.dispose();
    scenario.dispose();
  }
  
  @Override
  public void keyPressed(KeyEvent e) {
    float step = 0.2f;
    
    switch (e.getKeyCode()) {
        case KeyEvent.VK_PAGE_UP://faz zoom-in
            delta = delta * 0.809f;
            break;
        case KeyEvent.VK_PAGE_DOWN://faz zoom-out
            delta = delta * 1.1f;
            break;
        case KeyEvent.VK_W:
        case KeyEvent.VK_UP://gira sobre o eixo-x
            //if(alpha > -1)
                alpha = - step;
            break;
        case KeyEvent.VK_S:
        case KeyEvent.VK_DOWN://gira sobre o eixo-x
            //if(alpha < 1)
                alpha = + step;
            break;
        case KeyEvent.VK_A:
        case KeyEvent.VK_LEFT://gira sobre o eixo-y
            beta = beta - 4*step;
            break;
        case KeyEvent.VK_D:
        case KeyEvent.VK_RIGHT://gira sobre o eixo-y
            beta = beta +4*step;
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

    final JFrame frame = new JFrame("Slender");
    frame.setSize(width, height);
    frame.add(glCanvas);
    //frame.addMouseListener(listener);
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
