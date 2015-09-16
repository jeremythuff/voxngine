package game.world.terrain;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class VoxelGeometry {	
	
	float cubeSize = 0.475f;
	
	public int[] getIndices(int index) {
		
		int stride = 8*index;
		 
		int indices[] = { 
			stride+0, stride+1, stride+3, stride+0, stride+3, stride+2, // Front face
			stride+1, stride+5, stride+7, stride+1, stride+7, stride+3, // Right face
			stride+5, stride+4, stride+6, stride+5, stride+6, stride+7, // Back face
			stride+4, stride+0, stride+2, stride+4, stride+2, stride+6, // Left face
			stride+4, stride+5, stride+1, stride+4, stride+1, stride+0, // Bottom face,
			stride+2, stride+3, stride+7, stride+2, stride+7, stride+6  // Top face
			
		};
		 
		return indices;
	}
	
	public float[] getVertices(Vector3f coords) {
		return getVertices(coords, VoxelType.GRASS);
	}
	
	public float[] getVertices(Vector3f coords, VoxelType type) {
		
	    Vector4f topColor =  new Vector4f();
	    Vector4f bottomColor = new Vector4f();
	    
	    if(type == VoxelType.GRASS) {
	    	topColor.set(0.0f, 0.6f, 0.0f, 1.0f);
		    bottomColor.set(0.5f, 0.35f, 0.1f, 1.0f);
	    }
	    
	    if(type == VoxelType.DIRT) {
	    	topColor.set(0.5f, 0.35f, 0.1f, 1.0f);
	    	bottomColor.set(0.4f, 0.35f, 0.3f, 1.0f);
	    }
	    
	    float negX = -cubeSize+coords.x;
	    float posX = cubeSize+coords.x;
	    
	    float negY = -cubeSize+coords.y;
	    float posY = cubeSize+coords.y;
	    
	    float negZ = -cubeSize+coords.z;
	    float posZ = cubeSize+coords.z;
		
		
	    float vertices[] = {
		    posX, posY, posZ,		topColor.x, topColor.y, topColor.z, topColor.w, //top front right
	    	negX, posY, posZ,		topColor.x, topColor.y, topColor.z, topColor.w, //top front left
			posX, negY, posZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, // bottom front right
	    	negX, negY, posZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, //bottom front left
			posX, posY, negZ,		topColor.x, topColor.y, topColor.z, topColor.w, // top back right
	    	negX, posY, negZ,		topColor.x, topColor.y, topColor.z, topColor.w, // top back left
			posX, negY, negZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, // bottom back right
			negX, negY, negZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, // bottom back left
		};
	    
		return vertices;
		
	}

}
