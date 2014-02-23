package br.usp.icmc.vicg.gl.model;

import javax.media.opengl.GL3;

import br.usp.icmc.vicg.gl.buffer.ArrayBuffer;
import br.usp.icmc.vicg.gl.buffer.ArrayElementsBuffer;

public abstract class Model {
	
	protected GL3 gl;
	
	protected float[] positions;
	protected int[] indices; 
	
	private ArrayBuffer positionsBuffer;
	private ArrayElementsBuffer indicesBuffer;
	
	private int positionHandle;
	
	int[] vaoHandle;
	
	public void init(GL3 gl, int positionHandle) {
		this.gl = gl;
		this.positionHandle = positionHandle;
		initBuffers();
	}
	
	private void initBuffers() {
		if (positions != null) {
			positionsBuffer = new ArrayBuffer(gl, positions, 3, positionHandle);
		}
		
		if (indices != null) {
			indicesBuffer = new ArrayElementsBuffer(gl, indices);
		}
		
		vaoHandle = new int[1];
		gl.glGenVertexArrays(1, vaoHandle, 0);
		gl.glBindVertexArray(vaoHandle[0]);
	}
	
	public void bind() {
		if (positionsBuffer != null) {
			positionsBuffer.bind();
		}
		if (indicesBuffer != null) {
			indicesBuffer.bind();
		}
		
		gl.glBindVertexArray(vaoHandle[0]);
	}
	
	public void draw(int primitive) {
		if (indicesBuffer != null) {
			gl.glDrawElements(primitive, indices.length, GL3.GL_UNSIGNED_INT, 0);
		} else {
			gl.glDrawArrays(primitive, 0, positions.length / 3);
		}
	}
	
	public void dispose() {
		if (positionsBuffer != null) {
			positionsBuffer.dispose();
		}
		
		if (indicesBuffer != null) {
			indicesBuffer.dispose();
		}
	}
}
