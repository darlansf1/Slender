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
public class AbandonedHouse {
    private final JWavefrontObject model;
    private final Matrix4 modelMatrix;
    public static final float X = 20;
    public static final float Y = 0;
    public static final float Z = 0;
    
    public AbandonedHouse() {
        this.modelMatrix = new Matrix4();
        if (new File("./models/Farmhouse/OBJ/Farmhouse_OBJ.obj").exists())
            System.out.println("ARQUIVO OBJ EXISTE");
        else
            System.out.println("ARQUIVO OBJ NAO EXISTE, SE FUDEU");
        this.model = new JWavefrontObject(new File("./models/Farmhouse/OBJ/Farmhouse_OBJ.obj"));
    }
    
    public JWavefrontObject getModel() {
        return model;
    }

    public Matrix4 getModelMatrix() {
        return modelMatrix;
    }
    
    public void draw(float x, float y, float z){
        modelMatrix.loadIdentity();
        modelMatrix.translate(x, y, z);
        //modelMatrix.rotate(90.0f, 0.0f, 0.0f, 1.0f);
        modelMatrix.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        modelMatrix.scale(4.0f, 4.0f, 4.0f);
        modelMatrix.bind();
        model.draw();
    }
    
    void dispose() {
        model.dispose();
    }
}
