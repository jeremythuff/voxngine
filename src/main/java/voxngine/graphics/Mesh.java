package voxngine.graphics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.Buffer;
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
			destroyBuffer(this.vertBuffer);
			this.vertBuffer = null;
			this.vertBuffer = BufferUtils.createFloatBuffer(i);
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
			destroyBuffer(this.indecesBuffer);
			this.indecesBuffer = null;
			this.indecesBuffer = BufferUtils.createIntBuffer(i);
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
	
	
	public void destroyBuffer(Buffer buffer) {
	    if(buffer.isDirect()) {
	        try {
	            if(!buffer.getClass().getName().equals("java.nio.DirectByteBuffer")) {
	                Field attField = buffer.getClass().getDeclaredField("att");
	                attField.setAccessible(true);
	                buffer = (Buffer) attField.get(buffer);
	            }

	            Method cleanerMethod = buffer.getClass().getMethod("cleaner");
	            cleanerMethod.setAccessible(true);
	            Object cleaner = cleanerMethod.invoke(buffer);
	            Method cleanMethod = cleaner.getClass().getMethod("clean");
	            cleanMethod.setAccessible(true);
	            cleanMethod.invoke(cleaner);
	        } catch(Exception e) {
	        	System.err.println(e);
	        }
	        
	    } 
	}
    
}
