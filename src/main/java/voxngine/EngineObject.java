package voxngine;

public interface EngineObject {
	public void init();
	public void update(float delta);
	public void render(float delta);
	public void dispose();
}
