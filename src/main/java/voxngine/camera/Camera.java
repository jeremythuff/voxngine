package voxngine.camera;

import java.nio.FloatBuffer;

import org.joml.Vector3f;

import voxngine.io.Controlls;

public interface Camera {

	public void init();

	public void input(Controlls controlls);

	public void update(float delta);

	public FloatBuffer getMVMatrix();

	boolean frustumCulled(Vector3f point);

}
