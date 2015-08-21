package game;

import voxngine.Engine;
import game.world.World;

public class Main extends Engine {
	
	private World world;
			
	public static void main(String[] args) {
        new Main().start();
    }

	@Override
	public void init() {
		System.out.println("Game Started!");
		
		world = new World();
		world.init();
		
		//gui
		
		System.out.println("Initialization Done!");
	}

	@Override
	public void update(float delta) {
		world.update(delta);
	}

	@Override
	public void render(float delta) {
		world.render(delta);
	}

	@Override
	public void dispose() {
		System.out.println("Shutting down...");
		world.dispose();
		System.out.println("Game Ended!");
	}
	
}
