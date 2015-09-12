package voxngine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;

public class Mesh {
	
	private FloatBuffer vertBuffer;
    private IntBuffer indecesBuffer;
    
    private int entityCount;
    private HashSet<String> culledCoords = new HashSet<String>();
    
	public Mesh(){}
    
    public Mesh(FloatBuffer vertBuffer, IntBuffer indecesBuffer) {
    	this.vertBuffer = vertBuffer;
    	this.indecesBuffer = indecesBuffer;
    }

	public FloatBuffer getVertBuffer() {
		return vertBuffer;
	}

	public void setVertBuffer(FloatBuffer vertBuffer) {
		this.vertBuffer = vertBuffer;
	}

	public IntBuffer getIndecesBuffer() {
		return indecesBuffer;
	}

	public void setIndecesBuffer(IntBuffer indecesBuffer) {
		this.indecesBuffer = indecesBuffer;
	}
	
	public int getEntityCount() {
		return entityCount;
	}

	public void setEntityCount(int entityCount) {
		this.entityCount = entityCount;
	}

	public HashSet<String> getCulledCoords() {
		return culledCoords;
	}

	public void setCulledCoords(HashSet<String> culledCoords) {
		this.culledCoords = culledCoords;
	}
    
}
