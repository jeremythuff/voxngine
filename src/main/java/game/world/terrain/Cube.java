package game.world.terrain;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import game.world.WorldObject;
import voxngine.graphics.RenderEngine;

public class Cube implements WorldObject {
	
	Vector3f coords;
	
	public Cube(Vector3f coords) {
		this.coords = coords;
	}
	
	@Override
	public void init() {
		
        CubeGeometry cubeGeo = new CubeGeometry();
        
        float[] vertices = cubeGeo.getGeometry(coords);
               
        FloatBuffer interleavedBuffer = BufferUtils.createFloatBuffer(vertices.length);
        interleavedBuffer.put(vertices).flip();
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
