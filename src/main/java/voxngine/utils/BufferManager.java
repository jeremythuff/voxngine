package voxngine.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

public class BufferManager {
	
	int bufferCount = 10;
	int floatBufferSize = 8400000;
	int intBufferSize = 1800000;
	
	private List<FloatBuffer> availableFloatBuffers;
	private List<FloatBuffer> usedFloatBuffers;
	
	private List<IntBuffer> availableIntBuffers;
	private List<IntBuffer> usedIntBuffers;
	
	private List<FloatBuffer> tempFloatBuffers;
	private List<IntBuffer> tempIntBuffers;
	
	
	public BufferManager() {
		
		availableFloatBuffers = new ArrayList<FloatBuffer>();
		usedFloatBuffers = new ArrayList<FloatBuffer>();
		tempFloatBuffers = new ArrayList<FloatBuffer>();

		availableIntBuffers = new ArrayList<IntBuffer>();
		usedIntBuffers = new ArrayList<IntBuffer>();
		tempIntBuffers = new ArrayList<IntBuffer>();

		for(int i=0; i<bufferCount; i++) {
			availableFloatBuffers.add(BufferUtils.createFloatBuffer(floatBufferSize));
			availableIntBuffers.add(BufferUtils.createIntBuffer(intBufferSize)); 
		}
		
	}
	
	public FloatBuffer getFloatBuffer() {
		
		FloatBuffer buffer = null;
		
		if(availableFloatBuffers.isEmpty()) {
			buffer = BufferUtils.createFloatBuffer(floatBufferSize);
			tempFloatBuffers.add(buffer);
		} else {
			buffer = availableFloatBuffers.get(availableFloatBuffers.size()-1);
			availableFloatBuffers.remove(availableFloatBuffers.size()-1);
			usedFloatBuffers.add(buffer);
		}
		
		return buffer;	
	}
	
	public IntBuffer getIntBuffer() {
		
		IntBuffer buffer = null;
		
		if(availableIntBuffers.isEmpty()) {
			buffer = BufferUtils.createIntBuffer(intBufferSize);
			tempIntBuffers.add(buffer);
		} else {
			buffer = availableIntBuffers.get(availableIntBuffers.size()-1);
			availableIntBuffers.remove(availableIntBuffers.size()-1);
			usedIntBuffers.add(buffer);
		}
		
		return buffer;	
	}
	
	public void releaseFLoatBuffer(FloatBuffer buffer) {
		
		if(tempFloatBuffers.contains(buffer)) {
			tempFloatBuffers.remove(buffer);
			destroyBuffer(buffer);
			buffer=null;
		} else {
			usedFloatBuffers.remove(usedFloatBuffers.indexOf(buffer));
			buffer.clear();
			availableFloatBuffers.add(buffer);
		}
		
	}
	
	public void releaseIntBuffer(IntBuffer buffer) {
		
		if(tempIntBuffers.contains(buffer)) {
			destroyBuffer(buffer);
		} else {
			usedIntBuffers.remove(usedIntBuffers.indexOf(buffer));
			buffer.clear();
			availableIntBuffers.add(buffer);
		}
		
	}	
	
	private void destroyBuffer(Buffer buffer) {
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
