/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.vicg.gl.app;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.model.SimpleModel;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Leonardo
 */
public class Border {
    private final JWavefrontObject model;
    private final Matrix4 modelMatrix;
    
    public Border(){
        this.modelMatrix = new Matrix4();
        this.model = new JWavefrontObject(new File("./models/SnowTerrain/SnowTerrain.obj"));
    }
    
    public JWavefrontObject getModel() {
        return model;
    }

    public Matrix4 getModelMatrix() {
        return modelMatrix;
    }

    void draw() {
        modelMatrix.loadIdentity();
        modelMatrix.scale(10, 10, 10);
        modelMatrix.bind();
        model.draw();
    }

    void dispose() {
        model.dispose();
    }
}
