package game.world.terrain;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import game.world.WorldObject;
import voxngine.graphics.RenderEngine;

public class Cube implements WorldObject {
	
	int xCubes = 30;
	int yCubes = 10;
	int zCubes = 30;
	int totalCubes = xCubes*yCubes*zCubes;
			
	@Override
	public void init() {
		
        CubeGeometry cubeGeo = new CubeGeometry();
        int geoLength = cubeGeo.getGeometry(new Vector3f(0,0,0)).length;
        
        FloatBuffer interleavedBuffer = BufferUtils.createFloatBuffer(totalCubes*geoLength*5);
        int xCount = 0;
        int yCount = 0;
        int zCount = 0;
        
        for(float x=0 ; x < (float)xCubes ; x++) {
        	xCount++;
        	for (float y=0 ; y < (float)yCubes ; y++) {
        		yCount++;
        		for (float z=0; z < (float)zCubes ; z++) {
        			zCount++;
        			float[] vertices = cubeGeo.getGeometry(new Vector3f(x-(xCubes/2),y-yCubes,z-(zCubes/2)));
        	        interleavedBuffer.put(vertices);
        		}
        	}
        }
        
        System.out.println(xCount);
        System.out.println(yCount);
        System.out.println(zCount);

        System.out.println(totalCubes);
        
        interleavedBuffer.flip();
        
        RenderEngine.queBuffer(interleavedBuffer);
        
	}
		
	@Override
	public void input() {
	}

	@Override
	public void update(float delta) {
	}
 
	@Override
	public void render() {
	}

	@Override
	public void dispose() {
		
	}

}
