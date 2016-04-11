package br.usp.icmc.vicg.gl.app;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.model.SimpleModel;
import br.usp.icmc.vicg.gl.model.Sphere;
import java.io.File;
import java.util.ArrayList;

public class Scenario {
    private final JWavefrontObject model;
    private final SimpleModel world;
    private final Matrix4 modelMatrix;
    private final ArrayList<ArrayList<ArrayList<Float>>> map;
    private ArrayList<Float> theta;
    private ArrayList<Float> scaley;
    private ArrayList<Float> scalex;
    private float worldSize;

    public Scenario() {
        this.modelMatrix = new Matrix4();
        this.model = new JWavefrontObject(new File("./models/tree/Tree1.obj"));
        this.world = new Sphere();
        this.map = this.createMap();
    }

    public SimpleModel getWorld() {
        return world;
    }
    
    public JWavefrontObject getModel() {
        return model;
    }

    public Matrix4 getModelMatrix() {
        return modelMatrix;
    }

    public void dispose() {
        model.dispose();
        world.dispose();
    }

    public void draw(float x, float z, float delta) {
       // System.out.println("x: "+x);
        //System.out.println("z: "+z);
        //System.out.println("delta: "+delta);
        int count = 0;
        for(int i = 0; i < map.size(); i++)
            for(int j = 0; j < map.get(i).size(); j++){
                if(map.get(i).get(j).get(0) >= x-delta && map.get(i).get(j).get(0) <= x+delta
                        && map.get(i).get(j).get(1) >= z-delta && map.get(i).get(j).get(1) <= z+delta)
                    drawTree(map.get(i).get(j).get(0), 0.5f, map.get(i).get(j).get(1), theta.get(count), scalex.get(count), scaley.get(count));
                count++;
            }
        
        modelMatrix.loadIdentity();
        modelMatrix.scale(worldSize, worldSize, worldSize);
        modelMatrix.bind();
        
        world.bind();
        world.draw();
    }
    
    protected void drawTree(float x, float y, float z, float angle_y, float scale_x, float scale_y){
        modelMatrix.loadIdentity();
        modelMatrix.translate(x, y, z);
        modelMatrix.scale(scale_x, scale_y, (Math.abs(scale_x)+Math.abs(scale_y))/2);
        modelMatrix.rotate(angle_y, 0, 1.0f, 0);
        modelMatrix.rotate(-90, 1.0f, 0, 0);
        modelMatrix.bind();
        model.draw();
    }

    private ArrayList<ArrayList<ArrayList<Float>>> createMap() {
        this.theta = new ArrayList<Float>();
        this.scaley = new ArrayList<Float>();
        this.scalex = new ArrayList<Float>();
        
        float angle = 0, delta = 2f;
        ArrayList<ArrayList<ArrayList<Float>>> map = new ArrayList<ArrayList<ArrayList<Float>>>();
        float min = -100f, max = 100f;
        this.worldSize = max-min;
        
        for(float x = min; x < max; x += delta+Math.random()*delta){
            ArrayList<ArrayList<Float>> row = new ArrayList<ArrayList<Float>>();
            for(float z = min; z < max; z += delta+Math.random()*delta){
                ArrayList<Float> pair = new ArrayList<Float>();
                pair.add(x);
                pair.add(z);
                this.theta.add(angle);
                this.scaley.add(1f+0.5f*(float)Math.random());
                this.scalex.add(1f+0.5f*(float)Math.random());
                angle += Math.random()*delta*10;
                row.add(pair);
            }
            map.add(row);
        }
        
        return map;
    }
}
