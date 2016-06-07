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
    
    void draw(float x, float y, float z) {
        modelMatrix.loadIdentity();
        modelMatrix.translate(0.6f, -0.8f, 0.0f);
        modelMatrix.rotate(90, 0.0f, 1.0f, 0.0f);
        modelMatrix.scale(0.2f, 0.2f, 0.2f);
        modelMatrix.bind();
        model.draw();
    }

    void dispose() {
        model.dispose();
    }
}
