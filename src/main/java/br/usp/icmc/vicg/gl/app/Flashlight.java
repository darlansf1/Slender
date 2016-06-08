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
    
    private float x, y, z;
    void draw(float x, float y, float z, float horizontalAngle, float verticalAngle, float xDir, float yDir) {
        float angle = (90-(float)Math.toDegrees(Math.acos(verticalAngle)));
        float anglecoef = 1f;
        anglecoef = (90-Math.abs(angle))/90;
        anglecoef*= anglecoef;
        //anglecoef*= anglecoef;
        this.x = x+1.8f;//*anglecoef*anglecoef;
        this.y = y-2.0f;
        this.z = z+0.2f;//*anglecoef*anglecoef;
        modelMatrix.loadIdentity();
        //System.out.println("asin(zDir): "+Math.toDegrees(Math.asin(-zDir)));
        System.out.println("angle: "+angle);
        
        modelMatrix.translate(x, y, z);
        //modelMatrix.rotate(angle, 0f, 0f, 1f);
        modelMatrix.rotate(-horizontalAngle, 0, 1f, 0);
        modelMatrix.translate(this.x-x, this.y-y, this.z-z);
        modelMatrix.rotate(90, 0.0f, 1.0f, 0.0f);
        modelMatrix.scale(0.5f/*anglecoef*/, 0.5f/*anglecoef*/, 0.5f/*anglecoef*/);
        
        modelMatrix.bind();
        model.draw();
    }

    void dispose() {
        model.dispose();
    }
}
