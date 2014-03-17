/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.usp.icmc.vicg.gl.model;

/**
 *
 * @author PC
 */
public class MySphere extends MyModel {

    private final static int MAX_ELEMENTS = 4096;

    public MySphere(float radius, int numberSlices) {
        if (numberSlices < 3 || numberSlices > MAX_ELEMENTS) {
            numberSlices = 3;
        }

        int numberParallels = numberSlices / 2;
        int numberVertices = (numberParallels + 1) * (numberSlices + 1);

        float angleStep = (float) ((2.0f * Math.PI) / ((float) numberSlices));

        int indexIndices;

        vertices = new float[3 * numberVertices];

        for (int i = 0; i < numberParallels + 1; i++) {
            for (int j = 0; j < numberSlices + 1; j++) {
                int vertexIndex = (i * (numberSlices + 1) + j) * 3;
                vertices[vertexIndex + 0] = (float) (radius * Math.sin(angleStep * (float) i) * Math.sin(angleStep * (float) j));
                vertices[vertexIndex + 1] = (float) (radius * Math.cos(angleStep * (float) i));
                vertices[vertexIndex + 2] = (float) (radius * Math.sin(angleStep * (float) i) * Math.cos(angleStep * (float) j));
            }
        }
    }

}
