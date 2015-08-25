package voxngine;

import voxngine.graphics.RenderEngine;

public interface EngineObject {
	public void init();
	public void input();
	public void update(float delta);
	public void render(RenderEngine renderEngine);
	public void dispose();
}
