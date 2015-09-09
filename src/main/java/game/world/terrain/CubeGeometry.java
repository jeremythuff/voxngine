package game.world.terrain;

import java.util.Random;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class CubeGeometry {
	
	float cubeSize = 0.5f;
	
	public int[] getIndices(int index) {
		
		int stride = 8*index;
		 
		int indices[] = { 
			stride+0, stride+1, stride+2, stride+3, stride+2, stride+1, // Front face
			stride+1, stride+4, stride+3, stride+3, stride+5, stride+4, // Right face
			stride+4, stride+6, stride+5, stride+5, stride+7, stride+6, // Back face
			stride+6, stride+0, stride+7, stride+7, stride+2, stride+0, // Left face
			stride+6, stride+4, stride+0, stride+0, stride+1, stride+4, // Top face
			stride+7, stride+5, stride+2, stride+2, stride+3, stride+5  // Bottom face
		};
		 
		return indices;
	}
	
	public float[] getVertices(Vector3f coords) {
		
	    //Vector4f topColor = new Vector4f(0.0f, 0.6f, 0.0f, 1.0f);
	    //Vector4f bottomColor = new Vector4f(0.5f, 0.35f, 0.1f, 1.0f);
	    
	    float negX = -cubeSize+coords.x;
	    float posX = cubeSize+coords.x;
	    
	    float negY = -cubeSize+coords.y;
	    float posY = cubeSize+coords.y;
	    
	    float negZ = -cubeSize+coords.z;
	    float posZ = cubeSize+coords.z;
		
		Random rand = new Random();

		int  n = rand.nextInt(6) + 1;
	    
	    Vector4f topColor = new Vector4f(0.0f, 0.6f, 0.0f, 1.0f);
	    
	    switch(n) {
	    	case 1: topColor = new Vector4f(0.15f, 0.7f, 0.1f, 1.0f);
	    		break;
	    	case 2: topColor = new Vector4f(0.25f, 0.8f, 0.1f, 1.0f);
	    		break;
	    	case 3: topColor = new Vector4f(0.3f, 0.9f, 0.1f, 1.0f);
	    		break;
	    	case 4: topColor = new Vector4f(0.35f, 0.9f, 0.1f, 1.0f);
	    		break;
	    	case 5: topColor = new Vector4f(0.4f, 0.8f, 0.1f, 1.0f);
	    		break;
	    	case 6: topColor = new Vector4f(0.45f, 0.7f, 0.1f, 1.0f);
    			break;
	    }
	    
	    Vector4f bottomColor = topColor;
	    
	    float topRed = topColor.x;
	    float topGreen = topColor.y;
	    float topBlue = topColor.z;
	    float topAlpha = topColor.w;
	    
	    float botRed = bottomColor.x;
	    float botGreen = bottomColor.y;
	    float botBlue = bottomColor.z;
	    float botAlpha = bottomColor.w;

		
		
	    
	    
	    float vertices[] = {
	    	negX, posY, posZ,		topRed, topGreen, topBlue, topAlpha, //top front left
	    	posX, posY, posZ,		topRed, topGreen, topBlue, topAlpha, //top front right
			negX, negY, posZ,		botRed, botGreen, botBlue, botAlpha, //bottom front left
			posX, negY, posZ,		botRed, botGreen, botBlue, botAlpha, // bottom front right
			posX, posY, negZ,		topRed, topGreen, topBlue, topAlpha, // top back right
			posX, negY, negZ,		botRed, botGreen, botBlue, botAlpha, // bottom back right
			negX, posY, negZ,		topRed, topGreen, topBlue, topAlpha, // top back left
			negX, negY, negZ,		botRed, botGreen, botBlue, botAlpha, // bottom back right
		};
	    
		return vertices;
		
	}

}
