package voxngine.graphics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Mesh {
	
	private FloatBuffer vertBuffer;
    private IntBuffer indecesBuffer;
    
    private Float[] vertArray;
    private int[] intArray;
    
    private int entityCount;

	public Mesh(){}
    
    public Mesh(FloatBuffer vertBuffer, IntBuffer indecesBuffer) {
    	this.vertBuffer = vertBuffer;
    	this.indecesBuffer = indecesBuffer;
    }

	public FloatBuffer getVertBuffer() {
		return vertBuffer;
	}

	public void setVertBuffer(int i) {
		if(this.vertBuffer == null) {
			this.vertBuffer = BufferUtils.createFloatBuffer(i);
		} else {
			updateVertBuffer();
		}
	}
	
	public void updateVertBuffer() {
		this.vertBuffer.clear();
		this.vertBuffer.position(0);
	}

	public IntBuffer getIndecesBuffer() {
		return indecesBuffer;
	}

	public void setIndecesBuffer(int i) {
		if(this.indecesBuffer == null) {
			this.indecesBuffer = BufferUtils.createIntBuffer(i);
		} else {
			updateIndecesBuffer();
		}
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
