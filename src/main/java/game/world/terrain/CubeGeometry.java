package game.world.terrain;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class CubeGeometry {
	
	public float[] getGeometry(Vector3f coords) {
		
		float cubeSize = 1f;
	    Vector4f topColor = new Vector4f(0.0f, 0.6f, 0.0f, 1.0f);
	    Vector4f bottomColor = new Vector4f(0.5f, 0.35f, 0.1f, 1.0f);
		
		float vertices[] = {
     		   //Back
     		  -cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w, //top right
     		  -cubeSize+coords.x, -cubeSize+coords.y, -cubeSize+coords.z,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, //bottom right
     		  cubeSize+coords.x, -cubeSize+coords.y, -cubeSize+coords.z,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, //bottom left
     		  cubeSize+coords.x, -cubeSize+coords.y, -cubeSize+coords.z,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, //bottom middle
     		  cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,			topColor.x, topColor.y, topColor.z, topColor.w, //top middle
     		  -cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w,//top left
     		  
     		  //left
     		  -cubeSize+coords.x, -cubeSize+coords.y,  cubeSize+coords.z,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w,//bottom right
     		  -cubeSize+coords.x, -cubeSize+coords.y, -cubeSize+coords.z,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, //bottom left
     		  -cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w, //top left 
     		  -cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w, //top left
     		  -cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w, //top right
     		  -cubeSize+coords.x, -cubeSize+coords.y,  cubeSize+coords.z,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, //bottom right
     		  
     		  //right
     		  cubeSize+coords.x, -cubeSize+coords.y, -cubeSize+coords.z,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w,
     		  cubeSize+coords.x, -cubeSize+coords.y,  cubeSize+coords.z,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w,
     		  cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w,
     		  cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w,
     		  cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w,
     		  cubeSize+coords.x, -cubeSize+coords.y, -cubeSize+coords.z,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w,
     		   
     		   //front
     		  -cubeSize+coords.x, -cubeSize+coords.y, cubeSize+coords.z,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, //bottom left
     		  -cubeSize+coords.x,  cubeSize+coords.y, cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w, //top left
     		  cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w, //top middle 
     		  cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w, //top right
     		  cubeSize+coords.x, -cubeSize+coords.y,  cubeSize+coords.z,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, //bottom right
     		  -cubeSize+coords.x, -cubeSize+coords.y, cubeSize+coords.z,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, //bottom middle
     		  
     		  //top
     		  -cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w,
     		  cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w,
     		  cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w,
     		  cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w,
     		  -cubeSize+coords.x,  cubeSize+coords.y,  cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w,
     		  -cubeSize+coords.x,  cubeSize+coords.y, -cubeSize+coords.z,		topColor.x, topColor.y, topColor.z, topColor.w,
     		  
     		  //bottom
     		  -cubeSize+coords.x, -cubeSize+coords.x, -cubeSize+coords.x,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w,
     		  -cubeSize+coords.x, -cubeSize+coords.x, cubeSize+coords.x,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w,
     		  cubeSize+coords.x, -cubeSize+coords.x, -cubeSize+coords.x,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w,
     		  cubeSize+coords.x, -cubeSize+coords.x, -cubeSize+coords.x,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w,
     		  -cubeSize+coords.x, -cubeSize+coords.x, cubeSize+coords.x,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w,
     		  cubeSize+coords.x, -cubeSize+coords.x,  cubeSize+coords.x,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w
     		};
		
		return vertices;
		
	}

}
