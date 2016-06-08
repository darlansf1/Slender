package br.usp.icmc.vicg.gl.app;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import java.io.File;

public class Slender {
    private final JWavefrontObject model;
    private final Matrix4 modelMatrix;
    private final int MAX_CLOSENESS = 20;
    private final int CLOSENESS_STEP = 4;
    private float x = 2;
    private float z = 0;
    private float step = 0.08f;
    private boolean firstMet = true;
    private int closenessFactor = CLOSENESS_STEP;

    public Slender() {
        this.modelMatrix = new Matrix4();
        this.model = new JWavefrontObject(new File("./models/Slender/Slenderman.obj"));
    }

    public JWavefrontObject getModel() {
        return model;
    }

    public Matrix4 getModelMatrix() {
        return modelMatrix;
    }
    
    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }
    
    public boolean place(float x, float z, float xDir, float zDir, float maxDistance, Scene scene){
        float deltaz = (z-this.z);
        float deltax = (x-this.x);
        //if(Math.abs(deltax) <= 0.000001)
          //  deltax = 0.000001f;
        double angle = Math.atan2(deltaz, deltax);
        boolean toDraw = true;
        if(Math.abs(deltaz) > maxDistance || Math.abs(deltax) > maxDistance){
            this.x += (float)(Math.cos(angle)*step);
            this.z += (float)(Math.sin(angle)*step);
            toDraw = false;
            firstMet = true;
        }else{
            if(firstMet && closenessFactor < MAX_CLOSENESS){
                this.x = x+xDir*maxDistance/closenessFactor;
                this.z = z+zDir*maxDistance/closenessFactor;
                firstMet = false;
                closenessFactor+=CLOSENESS_STEP;
                scene.turnLightOff();
            }else if(closenessFactor >= MAX_CLOSENESS){
                scene.endGame(false);
                closenessFactor = CLOSENESS_STEP;
            }
        }
        //System.out.println("delta x: "+deltax);
        //System.out.println("delta z: "+deltaz);
        //System.out.println("angle: "+angle);
        //System.out.println("x: "+this.x);
        //System.out.println("z: "+this.z);
        modelMatrix.loadIdentity();
        modelMatrix.translate(this.x, -0.8f, this.z);//faz andar em direcao ao jogador
        modelMatrix.rotate(180-(float)Math.toDegrees(angle), 0, 1f, 0);
        modelMatrix.scale(0.7f, 0.7f, 0.7f);
        //modelMatrix.rotate(beta, 0, 1.0f, 0);
        //modelMatrix.rotate(alpha, 1.0f, 0, 0);
        //modelMatrix.translate(3, 0, 1.5f);
        modelMatrix.rotate(-90, 0, 1.0f, 0);
        //modelMatrix.rotate(-90, 1.0f, 0, 0);
        modelMatrix.bind();
        return toDraw;
    }

    void draw(float x, float z, float xDir, float zDir, float maxDistance, Scene scene) {
        if(this.place(x, z, xDir, zDir, maxDistance, scene))
            model.draw();
    }

    void dispose() {
        model.dispose();
    }
}
