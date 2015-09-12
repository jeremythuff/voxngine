package voxngine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;

import org.lwjgl.BufferUtils;

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

	public void setVertBuffer(int i) {
		if(this.getVertBuffer() == null) { 
			this.vertBuffer = BufferUtils.createFloatBuffer(i);
		} else {
			BufferUtils.zeroBuffer(this.vertBuffer);
			this.vertBuffer.clear();
			this.vertBuffer.rewind();
			this.vertBuffer = (FloatBuffer) this.vertBuffer.limit(i);
			
			
		}
	}

	public IntBuffer getIndecesBuffer() {
		return indecesBuffer;
	}

	public void setIndecesBuffer(int i) {
		if(this.indecesBuffer == null) {
			this.indecesBuffer = BufferUtils.createIntBuffer(i);
		} else {
			BufferUtils.zeroBuffer(this.indecesBuffer);
			this.indecesBuffer.clear();
			this.indecesBuffer.rewind();
			this.indecesBuffer = (IntBuffer) this.indecesBuffer.limit(i);
		}
		
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
