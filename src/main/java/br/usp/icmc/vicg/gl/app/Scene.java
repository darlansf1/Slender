package br.usp.icmc.vicg.gl.app;

import br.usp.icmc.vicg.gl.core.Light;
import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.core.Material;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.model.Rectangle;
import br.usp.icmc.vicg.gl.model.Sphere;
import br.usp.icmc.vicg.gl.model.TextureRectangle;
import br.usp.icmc.vicg.gl.model.TextureSimpleModel;
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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Scene extends KeyAdapter implements GLEventListener {

  private Shader shader; // Gerenciador dos shaders
  private final Matrix4 modelMatrix;
  private final Matrix4 projectionMatrix;
  private final Matrix4 viewMatrix;
  private final Material material;
  private final Scenario scenario;
  private final Slender slender;
  private final Flashlight flashlight;
  private final Floor floor;
  private final AbandonedHouse house;
  private final Light light;
  private final TextureSimpleModel loseScreen;
  private final TextureSimpleModel winScreen;
  private float aspect;
  private SoundEffects sounds;
  
    //controle de comandos
    private HashMap<Integer, Boolean>  pressedKeys;
    private float frentestep;
    private float virastep;
    private float campodevisao;
    private float cimastep;
    
    /*coordenadas*/
    private float x;
    private float z;
    
    private boolean messageshown;

  public Scene(float aspect) {
    this.aspect = aspect;
    this.pressedKeys = new HashMap<Integer, Boolean>();
    // Carrega os shaders
    shader = ShaderFactory.getInstance(ShaderType.SPOTLIGHT_SHADER);
    //shader = ShaderFactory.getInstance(ShaderType.SPOTLIGHT_SHADER);
    modelMatrix = new Matrix4();
    projectionMatrix = new Matrix4();
    viewMatrix = new Matrix4();

    //model = new JWavefrontObject(new File("./data/VW-new-beetle.obj"));
    scenario = new Scenario(-100.0f, 100.0f);
    slender = new Slender();
    flashlight = new Flashlight();
    floor = new Floor(-0.5f, -0.5f, 0.5f, 0.5f);
    house = new AbandonedHouse();
    light = new Light();
    
    loseScreen = new TextureRectangle();
    winScreen = new TextureRectangle();
    material = new Material();

    frentestep = 0;
    virastep = 0;
    campodevisao = 20;
    cimastep = 0;
    
    x = 0;
    z = 0;
    
    messageshown = false;
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
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    gl.glClearDepth(1.0f);

    gl.glEnable(GL.GL_DEPTH_TEST);
    
    gl.glEnable(GL.GL_CULL_FACE);
    gl.glCullFace(GL.GL_BACK);

    //inicializa os shaders
    shader.init(gl);

    //ativa os shaders
    shader.bind();

    //inicializa a matrix Model and Projection
    modelMatrix.init(gl, shader.getUniformLocation("u_modelMatrix"));
    projectionMatrix.init(gl, shader.getUniformLocation("u_projectionMatrix"));
    viewMatrix.init(gl, shader.getUniformLocation("u_viewMatrix"));

    initModel(gl, slender.getModelMatrix(), slender.getModel());
    initModel(gl, house.getModelMatrix(), house.getModel());
    initModel(gl, scenario.getModelMatrix(), scenario.getModel());
    scenario.getWorld().init(gl, shader);
    initModel(gl, flashlight.getModelMatrix(), flashlight.getModel());
    floor.init(gl, shader);
    floor.loadTexture("./images/grass.png");
    
    //init the light
    light.setPosition(new float[]{0.0f, 0.0f, 0.0f, 1.0f});
    light.setAmbientColor(new float[]{0.025f, 0.025f, 0.025f, 1.0f});
    light.setDiffuseColor(new float[]{0.8f, 0.8f, 0.8f, 1.0f});
    light.setSpecularColor(new float[]{0.2f, 0.2f, 0.2f, 1.0f});
    light.setConstantAttenuation(0.9f);
    light.setLinearAttenuation(0.05f);
    light.setQuadraticAttenuation(0.03f);
    light.init(gl, shader);
    material.init(gl, shader);
    
    sounds = new SoundEffects();
    sounds.playSoundEffects();
    
    loseScreen.init(gl, shader);
    try {
      loseScreen.loadTexture("./images/catra.jpg");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    
    winScreen.init(gl, shader);
    try {
      winScreen.loadTexture("./images/ines.jpg");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
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

  GL3 gl;
  float deltax = 0, deltaz = 0;
  Boolean lost_or_won = null;
  @Override
  public void display(GLAutoDrawable drawable) {
    // Recupera o pipeline
    GL3 gl = drawable.getGL().getGL3();
    this.gl = gl;

    // Limpa o frame buffer com a cor definida
    gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
    
    processKeyEvents();

    float y = 0.5f; // altura da camera
    float cosBeta = (float)Math.cos(Math.toRadians(virastep)); // vira
    float sinBeta = (float)Math.sin(Math.toRadians(virastep));
    float cosBetaComp = (float)Math.cos(Math.toRadians(virastep-180)); // vira
    float sinBetaComp = (float)Math.sin(Math.toRadians(virastep-180));
    float newx = x+(frentestep*cosBeta);
    float newz = z+(frentestep*sinBeta);
    float deltax = newx-x;
    float deltaz = newz-z;
    if(!scenario.checkCollision(-newx, -newz, slender, this)){
        x = newx;
        z = newz;
    }else{
        deltax = 0;
        deltaz = 0;
    }
    sounds.setDeltax(deltax);
    sounds.setDeltaz(deltaz);
    
    //zera movimento de camera
    frentestep = 0;
    
    if(lost_or_won == null){
        projectionMatrix.loadIdentity();
        projectionMatrix.perspective(60, this.aspect, 0.01f, campodevisao);
        projectionMatrix.bind();

        viewMatrix.loadIdentity();
        viewMatrix.lookAt(
                0, 0, 0, //onde vc esta
                cosBeta, cimastep, sinBeta, //pra onde olha
                0, 1, 0); //pra cima
        viewMatrix.translate(x, y, z); //seta posicao da camera
        viewMatrix.bind();
        light.bind();

        scenario.draw(-x, -z, campodevisao);

        flashlight.draw(x, y, z);
        
        slender.draw(/*beta, alpha*/-x, -z, cosBeta, sinBeta, campodevisao, this);
        //border.draw();
        house.draw(20.0f, 0, 0);        
        
        floor.draw();
    }else
        endGame(lost_or_won);

    // ForÃ§a execuÃ§Ã£o das operaÃ§Ãµes declaradas
    gl.glFlush();
  }
  
  public boolean endGame(boolean hasWon){
      try{
        if(lost_or_won == null){
            sounds.playGameOverAudio(hasWon);
            shader = ShaderFactory.getInstance(ShaderType.TEXTURE_SHADER);
            //inicializa os shaders
            shader.init(gl);

            //ativa os shaders
            shader.bind();

            projectionMatrix.loadIdentity();
            projectionMatrix.ortho(
                    -1, 1,
                    -1, 1,
                    -2, 2);
            projectionMatrix.bind();

            viewMatrix.loadIdentity();
            viewMatrix.lookAt(
                    0, 0, 1,
                    0, 0, 0,
                    0, 1, 0);
            viewMatrix.bind();

            light.init(gl, shader);
            light.setPosition(new float[]{2.0f, 2.0f, 2.0f, 1.0f});
            light.setAmbientColor(new float[]{0.0f, 0.0f, 0.0f, 1.0f});
            light.setDiffuseColor(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
            light.setSpecularColor(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
            light.bind();

            material.setAmbientColor(new float[]{1.0f, 1.0f, 1.0f, 0.25f});
            material.setDiffuseColor(new float[]{0.0f, 0.0f, 1.0f, 0.25f});
            material.setSpecularColor(new float[]{0.9f, 0.9f, 0.9f, 0.25f});
            material.setSpecularExponent(32);
            material.bind();
        }
        modelMatrix.loadIdentity();
        modelMatrix.bind();
        
        lost_or_won = hasWon;
        if(lost_or_won){
            winScreen.bind();
            winScreen.draw();
            if (!messageshown){
                JOptionPane.showMessageDialog(null, "\"Graças a Deus, você venceu! "
                    + "Em nome de Jesus, Graças a Deus!\" -- Ines Brasil\nProduzido por Sons of Catra");
                messageshown = true;
            }
        }else{
            loseScreen.bind();
            loseScreen.draw();
            if (!messageshown){
                JOptionPane.showMessageDialog(null, "Você perdeu!\n"
                    + "Aprenda mais sobre os ensinamentos de Mr. Catra e tente novamente...\n"
                    + "Produzido por Sons of Catra");
                messageshown = true;
            }
        }
      }catch(Exception e){
          e.printStackTrace();
      }
      return true;
  }

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
    slender.dispose();
    floor.dispose();
    scenario.dispose();
    flashlight.dispose();
    loseScreen.dispose();
    winScreen.dispose();
    house.dispose();
  }
        
    private void processKeyEvents(){
        float step;
        if (pressedKeys.containsKey(KeyEvent.VK_SHIFT) && pressedKeys.get(KeyEvent.VK_SHIFT))
            step = 0.2f; //correndo
        else
            step = 0.1f; //andando deboas pelo vale das sombras
        
        if (pressedKeys.containsKey(KeyEvent.VK_PAGE_UP) && pressedKeys.get(KeyEvent.VK_PAGE_UP)) //diminui campo de visao
            campodevisao = campodevisao * 0.809f;
        if (pressedKeys.containsKey(KeyEvent.VK_PAGE_DOWN) && pressedKeys.get(KeyEvent.VK_PAGE_DOWN)) //aumenta campo de visao
            campodevisao = campodevisao * 1.1f;
        if (pressedKeys.containsKey(KeyEvent.VK_W) && pressedKeys.get(KeyEvent.VK_W)) // olha pra cima
            if (cimastep+step <= 1.0)
                cimastep += step;
        if (pressedKeys.containsKey(KeyEvent.VK_S) && pressedKeys.get(KeyEvent.VK_S)) // olha pra baixo
            if (cimastep-step >= -1.0)
                cimastep -= step;
        if (pressedKeys.containsKey(KeyEvent.VK_A) && pressedKeys.get(KeyEvent.VK_A)) // vira pra esquerda
            virastep = virastep - 20*step;
        if (pressedKeys.containsKey(KeyEvent.VK_D) && pressedKeys.get(KeyEvent.VK_D)) // vira pra direita
            virastep = virastep + 20*step;
        if (pressedKeys.containsKey(KeyEvent.VK_UP) && pressedKeys.get(KeyEvent.VK_UP)) // anda pra frente
            frentestep = - step;
        if (pressedKeys.containsKey(KeyEvent.VK_DOWN) && pressedKeys.get(KeyEvent.VK_DOWN)) // anda pra tras
            frentestep = step;        
    }
  
  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
        case KeyEvent.VK_PAGE_UP:
            pressedKeys.put(KeyEvent.VK_PAGE_UP, true);
            break;
        case KeyEvent.VK_PAGE_DOWN:
            pressedKeys.put(KeyEvent.VK_PAGE_DOWN, true);
            break;
        case KeyEvent.VK_W:
            pressedKeys.put(KeyEvent.VK_W, true);
            break;
        case KeyEvent.VK_S:
            pressedKeys.put(KeyEvent.VK_S, true);
            break;
        case KeyEvent.VK_A:
            pressedKeys.put(KeyEvent.VK_A, true);
            break;
        case KeyEvent.VK_D:
            pressedKeys.put(KeyEvent.VK_D, true);
            break;
        case KeyEvent.VK_UP:
            pressedKeys.put(KeyEvent.VK_UP, true);
            break;
        case KeyEvent.VK_DOWN:
            pressedKeys.put(KeyEvent.VK_DOWN, true);
            break;
        case KeyEvent.VK_SHIFT:
            pressedKeys.put(KeyEvent.VK_SHIFT, true);
            break;
    }
  }
  
  public void keyReleased(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_PAGE_UP:
            pressedKeys.put(KeyEvent.VK_PAGE_UP, false);
            break;
        case KeyEvent.VK_PAGE_DOWN:
            pressedKeys.put(KeyEvent.VK_PAGE_DOWN, false);
            break;
        case KeyEvent.VK_W:
            pressedKeys.put(KeyEvent.VK_W, false);
            break;
        case KeyEvent.VK_S:
            pressedKeys.put(KeyEvent.VK_S, false);
            break;
        case KeyEvent.VK_A:
            pressedKeys.put(KeyEvent.VK_A, false);
            break;
        case KeyEvent.VK_D:
            pressedKeys.put(KeyEvent.VK_D, false);
            break;
        case KeyEvent.VK_UP:
            pressedKeys.put(KeyEvent.VK_UP, false);
            break;
        case KeyEvent.VK_DOWN:
            pressedKeys.put(KeyEvent.VK_DOWN, false);
            break;
        case KeyEvent.VK_SHIFT:
            pressedKeys.put(KeyEvent.VK_SHIFT, false);
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
