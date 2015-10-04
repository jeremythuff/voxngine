package voxngine.graphics;

import java.lang.ref.SoftReference;

public class Mesh {
	
	//private SoftReference<FloatBuffer> vertBuffer;
	
	private int vertArrayMark = 0;
	private SoftReference<float[]> vertArray;
	
	private int intArrayMark = 0;
    private SoftReference<int[]> indecesArray;
    
    private int entityCount;

	public Mesh(){}
    
    public Mesh(float[] vertArray, int[] indecesArray) {
    	this.vertArray = new SoftReference<float[]>(vertArray);
    	this.indecesArray = new SoftReference<int[]>(indecesArray);
    }

	public float[] getVertArray() {
		return vertArray.get();
	}

	public void setVertArray(int i) {
		if(vertArray != null) {
			vertArrayMark = 0;
		} 
		vertArray = new SoftReference<float[]>(new float[i]);
	}
	
	public void addVert(float f) {
		vertArray.get()[vertArrayMark] = f;
		vertArrayMark++;
	}
	
	public int[] getIndecesArray() {
		return indecesArray.get();
	}

	public void setIndecesArray(int i) {
		if(indecesArray != null) {
			intArrayMark = 0;
		}
		indecesArray = new SoftReference<int[]>(new int[i]);	
	}
	
	public void addInd(int i) {
		indecesArray.get()[intArrayMark] = i;
		intArrayMark++;
	}
	
	public int getEntityCount() {
		return entityCount;
	}

	public void setEntityCount(int entityCount) {
		this.entityCount = entityCount;
	}

	public void clear() {		
		vertArrayMark = 0;
		vertArray.clear();
		
		intArrayMark = 0;
		indecesArray.clear();
	}
    
}
