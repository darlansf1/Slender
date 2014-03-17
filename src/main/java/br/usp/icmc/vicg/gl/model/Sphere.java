/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.vicg.gl.model;

/**
 *
 * @author paulovich
 */
public class Sphere extends Model {

  private final static int MAX_ELEMENTS = 4096;

  public Sphere(float radius, int numberSlices) {

    if (numberSlices < 3 || numberSlices > MAX_ELEMENTS) {
      numberSlices = 3;
    }

    int numberParallels = numberSlices / 2;
    int numberVertices = (numberParallels + 1) * (numberSlices + 1);
    int numberIndices = numberParallels * numberSlices * 6;

    float angleStep = (float) ((2.0f * Math.PI) / ((float) numberSlices));

    int indexIndices;

    positions = new float[3 * numberVertices];
    indices = new int[numberIndices];

    for (int i = 0; i < numberParallels + 1; i++) {
      for (int j = 0; j < numberSlices + 1; j++) {

        int vertexIndex = (i * (numberSlices + 1) + j) * 3;
        int normalIndex = (i * (numberSlices + 1) + j) * 3;
        int texCoordsIndex = (i * (numberSlices + 1) + j) * 2;

        positions[vertexIndex + 0] = (float) (radius * Math.sin(angleStep * (float) i) * Math.sin(angleStep * (float) j));
        positions[vertexIndex + 1] = (float) (radius * Math.cos(angleStep * (float) i));
        positions[vertexIndex + 2] = (float) (radius * Math.sin(angleStep * (float) i) * Math.cos(angleStep * (float) j));
      }
    }

    indexIndices = 0;
    for (int i = 0; i < numberParallels; i++) {
      for (int j = 0; j < numberSlices; j++) {

        indices[indexIndices++] = i * (numberSlices + 1) + j;
        indices[indexIndices++] = (i + 1) * (numberSlices + 1) + j;
        indices[indexIndices++] = (i + 1) * (numberSlices + 1) + (j + 1);

        indices[indexIndices++] = i * (numberSlices + 1) + j;
        indices[indexIndices++] = (i + 1) * (numberSlices + 1) + (j + 1);
        indices[indexIndices++] = i * (numberSlices + 1) + (j + 1);
      }
    }
  }
}
