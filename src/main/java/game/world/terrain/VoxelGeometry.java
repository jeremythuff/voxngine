package game.world.terrain;

import org.joml.Vector4f;

public class VoxelGeometry {	
	
	float cubeSize = 0.5f;
	
	public int[] getIndices(int index, int i) {
		
		int stride = 4*i+index;//8*index+i;
		int indices[] = { 
			stride+0, stride+1, stride+3, stride+0, stride+3, stride+2, // Front face
		};
			 
		return indices;
	}
	
	public float[] getVertices(int[] coords, String face) {
		return getVertices(coords, VoxelType.GRASS, face);
	}
	
	public float[] getVertices(int[] voxel, VoxelType type, String face) {
		
	    Vector4f topColor =  new Vector4f();
	    Vector4f bottomColor = new Vector4f();
	    
	    if(type == VoxelType.GRASS) {
	    	topColor.set(0.0f, 0.6f, 0.0f, 1.0f);
		    bottomColor.set(0.5f, 0.35f, 0.1f, 1.0f);
	    }
	    
	    if(type == VoxelType.ACTIVE) {
	    	topColor.set(0.5f, 0.0f, 0.0f, 1.0f);
		    bottomColor.set(0.5f, 0.35f, 0.1f, 1.0f);
	    }
	    
	    if(type == VoxelType.DIRT) {
	    	topColor.set(0.5f, 0.35f, 0.1f, 1.0f);
	    	bottomColor.set(0.4f, 0.35f, 0.3f, 1.0f);
	    }
	    
	    float negX = -cubeSize+voxel[0];
	    float posX = cubeSize+voxel[0];
	    
	    float negY = -cubeSize+voxel[1];
	    float posY = cubeSize+voxel[1];
	    
	    float negZ = -cubeSize+voxel[2];
	    float posZ = cubeSize+voxel[2];
		
	    float vertices[];
	    
	    switch(face) {
			case "front":
				vertices = new float[] {
				    posX, posY, posZ,		topColor.x, topColor.y, topColor.z, topColor.w, //top front right
			    	negX, posY, posZ,		topColor.x, topColor.y, topColor.z, topColor.w, //top front left   	
					posX, negY, posZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, // bottom front right
			    	negX, negY, posZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, //bottom front left
				};
			break;
			case "right":
				vertices = new float[] { 
					posX, posY, posZ,		topColor.x, topColor.y, topColor.z, topColor.w, //top front right
					posX, negY, posZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, // bottom front right
			    	posX, posY, negZ,		topColor.x, topColor.y, topColor.z, topColor.w, // top back right
			    	posX, negY, negZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, // bottom back right
				};
			break;
			case "left":
				vertices = new float[] {
				    negX, posY, posZ,		topColor.x, topColor.y, topColor.z, topColor.w, //top front left   		
			    	negX, posY, negZ,		topColor.x, topColor.y, topColor.z, topColor.w, // top back left
			    	negX, negY, posZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, //bottom front left
					negX, negY, negZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, // bottom back left
				};
			break;
			case "back":
				vertices = new float[] {
					negX, posY, negZ,		topColor.x, topColor.y, topColor.z, topColor.w, // top back left
					posX, posY, negZ,		topColor.x, topColor.y, topColor.z, topColor.w, // top back right
					negX, negY, negZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, // bottom back left
				    posX, negY, negZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, // bottom back right
				};
			break;
			case "bottom":
				vertices = new float[] {
					posX, negY, posZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, // bottom front right
			    	negX, negY, posZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, //bottom front left
			    	posX, negY, negZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, // bottom back right
					negX, negY, negZ,		bottomColor.x, bottomColor.y, bottomColor.z, bottomColor.w, // bottom back left
				};
			break;
			case "top":
				vertices = new float[] {
			    	negX, posY, posZ,		topColor.x, topColor.y, topColor.z, topColor.w, //top front left
				    posX, posY, posZ,		topColor.x, topColor.y, topColor.z, topColor.w, //top front right
			    	negX, posY, negZ,		topColor.x, topColor.y, topColor.z, topColor.w, // top back left
			    	posX, posY, negZ,		topColor.x, topColor.y, topColor.z, topColor.w, // top back right
				};
			break;
			default: vertices = null;
		}
	    
		return vertices;
		
	}

}
