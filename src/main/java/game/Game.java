package game;

import voxngine.Engine;
import voxngine.graphics.RenderEngine;
import voxngine.graphics.shaders.Shader;
import voxngine.graphics.shaders.ShaderProgram;
import voxngine.io.Window;

import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import game.gui.Gui;
import game.world.World;

public class Game extends Engine {
	
	private List<GameObject> gameObjecs = new ArrayList<GameObject>();
				
	public static void main(String[] args) {
        new Game().start();
    }

	@Override
	public void init() {
		
		glfwSetWindowTitle(Window.id, "Voxel Game");
		
		System.out.println("Game Started...");
		
		gameObjecs.add(new World());
		gameObjecs.add(new Gui());
		
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.init();
		}
		
		System.out.println("Initialization Done!\n");
	}
	
	@Override
	public void input() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.update(delta);
		}
	}

	@Override
	public void render(RenderEngine renderEngine) {
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.render(renderEngine);
		}
	}

	@Override
	public void dispose() {
		System.out.println("Shutting down...");
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.dispose();
		}
		System.out.println("Game Ended!");
	}
	
}
