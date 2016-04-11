package br.usp.icmc.vicg.gl.app;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import java.io.File;

public class Slender {
    private final JWavefrontObject model;
    private final Matrix4 modelMatrix;
    private float x = 2;
    private float z = 0;
    private float step = 0.01f;

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
    
    public void place(float x, float z){
        modelMatrix.loadIdentity();
        
        /*double angle = Math.atan((z-this.z)/(x-this.x));
        if(Math.abs(z-this.z) > 0.1 || Math.abs(x-this.x) > 0.1){
            this.x -= (float)(Math.cos(angle)*step);
            this.z -= (float)(Math.sin(angle)*step);
        }
        System.out.println("delta x: "+(x-this.x));
        System.out.println("delta z: "+(z-this.z));
        System.out.println("angle: "+angle);
        System.out.println("x: "+this.x);
        System.out.println("z: "+this.z);*/
        modelMatrix.translate(this.x, 0, this.z);//faz andar em direcao ao jogador
        modelMatrix.scale(0.5f, 0.5f, 0.5f);
        //modelMatrix.rotate(beta, 0, 1.0f, 0);
        //modelMatrix.rotate(alpha, 1.0f, 0, 0);
        //modelMatrix.translate(3, 0, 1.5f);
        modelMatrix.rotate(-90, 1.0f, 0, 0);
        modelMatrix.bind();
    }

    void draw(float x, float z) {
        this.place(x, z);
        model.draw();
    }

    void dispose() {
        model.dispose();
    }
}
