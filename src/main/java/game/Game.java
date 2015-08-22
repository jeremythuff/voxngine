package game;

import voxngine.Engine;
import game.gui.Gui;
import game.world.World;

public class Game extends Engine {
	
	private World world;
	private Gui gui;
			
	public static void main(String[] args) {
        new Game().start();
    }

	@Override
	public void init() {
		System.out.println("Game Started!");
		
		world = new World();
		world.init();
		
		gui = new Gui();
		gui.init();
		
		System.out.println("Initialization Done!");
	}

	@Override
	public void update(float delta) {
		world.update(delta);
		gui.update(delta);
	}

	@Override
	public void render(float delta) {
		world.render(delta);
		gui.render(delta);
	}

	@Override
	public void dispose() {
		System.out.println("Shutting down...");
		world.dispose();
		gui.dispose();
		System.out.println("Game Ended!");
	}
	
}
