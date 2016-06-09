/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.vicg.gl.app;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import java.io.File;

/**
 *
 * @author Leonardo
 */
public class Flashlight {
    private final JWavefrontObject model;
    private final Matrix4 modelMatrix;
    private float x, y, z;
    
    public Flashlight() {
        this.modelMatrix = new Matrix4();
        this.model = new JWavefrontObject(new File("./models/Flashlight/Flashlight.obj"));
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

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
    
    

    void draw(float x, float y, float z, float fx, float fy, float fz) {
        float angle = (90-(float)Math.toDegrees(Math.acos(fy)));
        float anglecoef = 1f;
        anglecoef = (90-Math.abs(angle))/90;
        
        this.x = x+1.8f*anglecoef;
        this.y = y-1.9f;
        this.z = z+0.2f;//*anglecoef*anglecoef;
        modelMatrix.loadIdentity();
        
        modelMatrix.translate(x, y, z);
        modelMatrix.lookAt(
                0, 0, 0, 
                -fz, 0, -fx, 
                0, 1, 0);
        modelMatrix.rotate(angle, 0.0f, 0.0f, 1.0f);
        modelMatrix.translate(this.x-x, this.y-y, this.z-z);
        modelMatrix.rotate(90, 0.0f, 1.0f, 0.0f);
        modelMatrix.scale(0.5f, 0.5f, 0.5f);
        
        modelMatrix.bind();
        model.draw();
    }

    void dispose() {
        model.dispose();
    }
}
