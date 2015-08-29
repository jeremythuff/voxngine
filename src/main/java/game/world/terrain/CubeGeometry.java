package game.world.terrain;

import org.joml.Vector3f;

public class CubeGeometry {
	
	public float[] getGeometry(Vector3f coords) {
		
		float cubeSize = 1f;
	    float topColor[] = {0.0f, 0.6f, 0.0f, 1.0f};
	    float bottomColor[] = {0.5f, 0.35f, 0.1f, 1.0f};
		
		float vertices[] = {
     		   //Back
     		  -cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3], //top right
     		  -cubeSize+coords.x, -cubeSize+coords.y, -cubeSize+coords.z,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3], //bottom right
     		  cubeSize+coords.x, -cubeSize+coords.y, -cubeSize+coords.z,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3], //bottom left
     		  cubeSize+coords.x, -cubeSize+coords.y, -cubeSize+coords.z,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3], //bottom middle
     		  cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,			topColor[0], topColor[1], topColor[2], topColor[3], //top middle
     		  -cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3],//top left
     		  
     		  //left
     		  -cubeSize+coords.x, -cubeSize+coords.y,  cubeSize+coords.z,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3],//bottom right
     		  -cubeSize+coords.x, -cubeSize+coords.y, -cubeSize+coords.z,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3], //bottom left
     		  -cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3], //top left 
     		  -cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3], //top left
     		  -cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3], //top right
     		  -cubeSize+coords.x, -cubeSize+coords.y,  cubeSize+coords.z,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3], //bottom right
     		  
     		  //right
     		  cubeSize+coords.x, -cubeSize+coords.y, -cubeSize+coords.z,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3],
     		  cubeSize+coords.x, -cubeSize+coords.y,  cubeSize+coords.z,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3],
     		  cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3],
     		  cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3],
     		  cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3],
     		  cubeSize+coords.x, -cubeSize+coords.y, -cubeSize+coords.z,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3],
     		   
     		   //front
     		  -cubeSize+coords.x, -cubeSize+coords.y, cubeSize+coords.z,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3], //bottom left
     		  -cubeSize+coords.x,  cubeSize+coords.y, cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3], //top left
     		  cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3], //top middle 
     		  cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3], //top right
     		  cubeSize+coords.x, -cubeSize+coords.y,  cubeSize+coords.z,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3], //bottom right
     		  -cubeSize+coords.x, -cubeSize+coords.y, cubeSize+coords.z,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3], //bottom middle
     		  
     		  //top
     		  -cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3],
     		  cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3],
     		  cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3],
     		  cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3],
     		  -cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3],
     		  -cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor[0], topColor[1], topColor[2], topColor[3],
     		  
     		  //bottom
     		  -cubeSize+coords.x, -cubeSize+coords.x, -cubeSize+coords.x,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3],
     		  -cubeSize+coords.x, -cubeSize+coords.x, cubeSize+coords.x,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3],
     		  cubeSize+coords.x, -cubeSize+coords.x, -cubeSize+coords.x,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3],
     		  cubeSize+coords.x, -cubeSize+coords.x, -cubeSize+coords.x,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3],
     		  -cubeSize+coords.x, -cubeSize+coords.x, cubeSize+coords.x,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3],
     		  cubeSize+coords.x, -cubeSize+coords.x,  cubeSize+coords.x,		bottomColor[0], bottomColor[1], bottomColor[2], bottomColor[3]
     		};
		
		return vertices;
		
	}

}
