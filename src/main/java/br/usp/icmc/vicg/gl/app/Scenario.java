package br.usp.icmc.vicg.gl.app;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.model.SimpleModel;
import br.usp.icmc.vicg.gl.model.Sphere;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scenario {
    private final JWavefrontObject model;
    private final Matrix4 modelMatrix;
    private final ArrayList<ArrayList<ArrayList<Float>>> map;
    private ArrayList<Float> theta;
    private ArrayList<Float> scaley;
    private ArrayList<Float> scalex;
    private float worldSize;
    private float minDistance = 0.2f;
    private float min, max;

    public Scenario(float min, float max) {
        this.min = min;
        this.max = max;
        this.worldSize = max-min;
        this.modelMatrix = new Matrix4();
        this.model = new JWavefrontObject(new File("./models/tree2/needle01.obj"));
        this.map = this.createMap();
    }

    public float getWorldSize() {
        return worldSize;
    }
    
    public JWavefrontObject getModel() {
        return model;
    }

    public Matrix4 getModelMatrix() {
        return modelMatrix;
    }

    public void dispose() {
        model.dispose();
    }

    public void draw(float x, float z, float delta) {
        int count = 0;
        delta = delta/2;
        
        for(int i = 0; i < map.size(); i++)
            for(int j = 0; j < map.get(i).size(); j++){
                if(map.get(i).get(j).get(0) >= x-delta && map.get(i).get(j).get(0) <= x+delta
                        && map.get(i).get(j).get(1) >= z-delta && map.get(i).get(j).get(1) <= z+delta)
                    drawTree(map.get(i).get(j).get(0), 0.2f, map.get(i).get(j).get(1), theta.get(count), scalex.get(count), scaley.get(count));
                count++;
            }
    }
    
    protected void drawTree(float x, float y, float z, float angle_y, float scale_x, float scale_y){
        modelMatrix.loadIdentity();
        modelMatrix.translate(x, y, z);
        modelMatrix.scale(scale_x, scale_y, (Math.abs(scale_x)+Math.abs(scale_y))/2);
        //modelMatrix.rotate(angle_y, 0, 1.0f, 0);
        //modelMatrix.rotate(-90, 1.0f, 0, 0);
        modelMatrix.bind();
        model.draw();
    }

    private ArrayList<ArrayList<ArrayList<Float>>> createMap() {
        this.theta = new ArrayList<Float>();
        this.scaley = new ArrayList<Float>();
        this.scalex = new ArrayList<Float>();
        
        float angle = 0, delta = 2f;
        ArrayList<ArrayList<ArrayList<Float>>> map = new ArrayList<ArrayList<ArrayList<Float>>>();
        this.worldSize = max-min;
        float houseSpace = 5.5f;
        float minSpace = 1;
        
        for(float x = min; x < max; x += delta/2+Math.random()*2*delta){
            ArrayList<ArrayList<Float>> row = new ArrayList<ArrayList<Float>>();
            for(float z = min; z < max; z += delta/2+Math.random()*2*delta){
                if(Math.abs(x-AbandonedHouse.X) < houseSpace && Math.abs(z-AbandonedHouse.Z) < houseSpace)
                    continue;
                if(Math.abs(x) < minSpace && Math.abs(z) < minSpace)
                    continue;
                
                ArrayList<Float> pair = new ArrayList<Float>();
                pair.add(x);
                pair.add(z);
                this.theta.add(angle);
                this.scaley.add(2.4f);
                this.scalex.add(2f+2f*(float)Math.random());
                angle += Math.random()*delta*10;
                row.add(pair);
            }
            map.add(row);
        }
        
        return map;
    }

    public boolean checkCollision(float x, float z, Slender slender, Scene scene) {
        for(int i = 0; i < map.size(); i++)
            for(int j = 0; j < map.get(i).size(); j++){
                //System.out.printf("deltax: %f, deltaz: %f\n", Math.abs(map.get(i).get(j).get(0)-x), Math.abs(map.get(i).get(j).get(1) - z));
                if(Math.abs(map.get(i).get(j).get(0)-x) < minDistance && 
                       Math.abs(map.get(i).get(j).get(1) - z) < minDistance){
                  //  try {
                    //    System.in.read();
                    //} catch (IOException ex) {
                      //  Logger.getLogger(Scenario.class.getName()).log(Level.SEVERE, null, ex);
                    //}
                    return true;
                }
                float houseColision = 4f;
                if(Math.abs(x-AbandonedHouse.X) < houseColision && Math.abs(z-AbandonedHouse.Z) < houseColision/4)
                    return scene.endGame(true);
                if(Math.abs(x-slender.getX()) < minDistance && Math.abs(z-slender.getZ()) < minDistance)
                    return scene.endGame(false);
                if(x <= min || x >= max || z <= min || z >= max)
                    return true;
            }
        return false;
    }
}
