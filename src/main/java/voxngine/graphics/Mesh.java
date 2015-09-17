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
		this.vertBuffer = BufferUtils.createFloatBuffer(i);
	}
	
	public void updateVertBuffer() {
		this.vertBuffer.clear();
		this.vertBuffer.position(0);
	}

	public IntBuffer getIndecesBuffer() {
		return indecesBuffer;
	}

	public void setIndecesBuffer(int i) {
		this.indecesBuffer = BufferUtils.createIntBuffer(i);
	}
	
	public void updateIndecesBuffer() {
		this.indecesBuffer.clear();
		this.indecesBuffer.position(0);
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
