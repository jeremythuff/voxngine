package game.world;

public interface WorldObject {
	
	public void init();
	public void update(float delta);
	public void render(float delta);
	public void dispose();

}
