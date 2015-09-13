package game.world.terrain;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class VoxelGeometry {
	
	float cubeSize = 0.5f;
	
	public int[] getIndices(int index) {
		
		int stride = 8*index;
		 
		int indices[] = { 
			stride+0, stride+1, stride+3, stride+0, stride+3, stride+2, // Front face
			stride+1, stride+5, stride+7, stride+1, stride+7, stride+3, // Right face
			stride+5, stride+4, stride+6, stride+5, stride+6, stride+7, // Back face
			stride+4, stride+0, stride+2, stride+4, stride+2, stride+6, // Left face
			stride+4, stride+5, stride+1, stride+4, stride+1, stride+0,  // Bottom face,
			stride+2, stride+3, stride+7, stride+2, stride+7, stride+6 // Top face
			
		};
		 
		return indices;
	}
	
	public float[] getVertices(Vector3f coords) {
		
	    Vector4f topColor = new Vector4f(0.0f, 0.6f, 0.0f, 1.0f);
	    Vector4f bottomColor = new Vector4f(0.5f, 0.35f, 0.1f, 1.0f);
	    
	    float negX = -cubeSize+coords.x;
	    float posX = cubeSize+coords.x;
	    
	    float negY = -cubeSize+coords.y;
	    float posY = cubeSize+coords.y;
	    
	    float negZ = -cubeSize+coords.z;
	    float posZ = cubeSize+coords.z;
		
//		Random rand = new Random();

//		int  n = rand.nextInt(6) + 1;
	    
//	    Vector4f topColor = new Vector4f(0.0f, 0.6f, 0.0f, 1.0f);
//	    
//	    switch(n) {
//	    	case 1: topColor = new Vector4f(0.15f, 0.7f, 0.1f, 1.0f);
//	    		break;
//	    	case 2: topColor = new Vector4f(0.25f, 0.8f, 0.1f, 1.0f);
//	    		break;
//	    	case 3: topColor = new Vector4f(0.3f, 0.9f, 0.1f, 1.0f);
//	    		break;
//	    	case 4: topColor = new Vector4f(0.35f, 0.9f, 0.1f, 1.0f);
//	    		break;
//	    	case 5: topColor = new Vector4f(0.4f, 0.8f, 0.1f, 1.0f);
//	    		break;
//	    	case 6: topColor = new Vector4f(0.45f, 0.7f, 0.1f, 1.0f);
//    			break;
//	    }
//	    
//	    Vector4f bottomColor = topColor;
		
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
