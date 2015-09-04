package game.world.terrain;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import game.world.WorldObject;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;

public class Cube implements WorldObject {
	
	int xCubes;
	int yCubes;
	int zCubes;
	
	int xOrigin;
	int yOrigin;
	int zOrigin;
	
	int totalCubes;
	
	public Cube(int xCubes, int yCubes, int zCubes, 
				int xOrigin, int yOrigin, int zOrigin) {
		
		this.xCubes = xCubes;
		this.yCubes = yCubes;
		this.zCubes = zCubes;
		
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
		this.zOrigin = zOrigin;
		
		totalCubes = xCubes*yCubes*zCubes;
		
	}
			
	@Override
	public void init(RenderEngine renderer) {
		
        CubeGeometry cubeGeo = new CubeGeometry();
        int geoLength = cubeGeo.getGeometry(new Vector3f(0,0,0)).length;
        
        FloatBuffer interleavedBuffer = BufferUtils.createFloatBuffer(totalCubes*geoLength);
        
        int i=0;
        for(float x=0 ; x < (float)xCubes ; x++) {
        	for (float y=0 ; y < (float)yCubes ; y++) {
        		for (float z=0; z < (float)zCubes ; z++) {
        			i++;
        			float[] vertices = cubeGeo.getGeometry(new Vector3f(x-xOrigin,y-yOrigin,z-zOrigin));
        	        interleavedBuffer.put(vertices);
        		}
        	}
        }
        
        System.out.println(i);
              
        interleavedBuffer.flip();
        
        renderer.queBuffer(totalCubes, interleavedBuffer);
        
	}
		
	@Override
	public void input(Controlls controlls) {
	}

	@Override
	public void update(float delta) {
	}
 
	@Override
	public void render(RenderEngine renderer) {
	}

	@Override
	public void dispose() {
		
	}

}
