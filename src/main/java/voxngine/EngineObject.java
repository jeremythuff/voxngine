package voxngine;

public interface EngineObject {
	public void init();
	public void input();
	public void update(float delta);
	public void render(float delta);
	public void dispose();
}
