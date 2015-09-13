package voxngine;

import java.lang.reflect.InvocationTargetException;

import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;

public interface EngineObject {
	public void init(RenderEngine renderer);
	public void input(Controlls controlls);
	public void update(float delta);
	public void render(RenderEngine renderer);
	public void dispose();
}
