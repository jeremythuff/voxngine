package game.world.terrain;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.stb.STBPerlin.stb_perlin_noise3;

import java.util.ArrayList;
import java.util.List;

import game.world.WorldObject;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;

public class Zone implements WorldObject {
	
	List<WorldObject> worldObjects = new ArrayList<WorldObject>();
	
	int activeChunk;
	int lastActive;
	
	int i;
	int u;

	private boolean setLastActive;

	@Override
	public void init(RenderEngine renderer) {

		int width = 250;
		int height = 20;
		float minAlt = height/1.5f;
		
		float randomNum = (float) Math.random()*0.02f;
		float value = 0.03f+randomNum;
				
		int[][] voxelMap = new int[width*height*width][4]; 
		
		int i = 0;
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				for(int z = 0; z < width; z++) {	
					if(y+((stb_perlin_noise3(x*value,0,z*value,0,0,0))*minAlt/2) < minAlt) {
						if(y>=minAlt/1.35) {
							voxelMap[i] = new int[] {x,y,z,0};
						} else {
							voxelMap[i] = new int[] {x,y,z,1};
						}
					} else {
						voxelMap[i] = new int[] {x,y,z,-1};
					}
						
					i++;
				}
			}
		}
		
		System.out.println(i);
		
		int[][] chunkMap1 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap2 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap3 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap4 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap5 = new int[(width/5)*height*(width/5)][4];

		int c = 0;
		int m = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap1[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		 c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap2[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap3[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap4[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap5[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		System.out.println(m);
		
		worldObjects.add(new Chunk(chunkMap1));
		worldObjects.add(new Chunk(chunkMap2));
		worldObjects.add(new Chunk(chunkMap3));
		worldObjects.add(new Chunk(chunkMap4));
		worldObjects.add(new Chunk(chunkMap5));
		
		activeChunk = worldObjects.size() - 1;
		
		worldObjects.stream().forEach(worldObject -> {		
			System.out.println("Initialiazing the "+ worldObject.getClass().getSimpleName() +" Object...");
			worldObject.init(renderer);
		});
		
	}
	
	@Override
	public void input(Controlls controlls) {
		
		if(controlls.getKeyboad().activeKeyEvent()) {
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_LEFT)) {
				setLastActive = true;
				lastActive = activeChunk;
				activeChunk++;
				if(activeChunk > worldObjects.size() - 1) {
					lastActive = worldObjects.size() - 1;
					activeChunk = 0;
				}
			}
			
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_RIGHT)) {
				setLastActive = true;
				lastActive = activeChunk;
				activeChunk--;
				if(activeChunk < 0) {
					lastActive = 0;
					activeChunk = worldObjects.size() - 1; 
				}
			}
			
		}
		
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Last Active: "+lastActive));
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Active: "+activeChunk));
		
		worldObjects.stream().forEach(worldObject -> {
			worldObject.input(controlls);
		});
	}

	@Override
	public void update(float delta) {
		u = 0;
		worldObjects.stream().forEach(worldObject -> {
			
			if(u == lastActive && setLastActive) {
				setLastActive = false;
				((Chunk)worldObject).setLastActive(true);
				((Chunk)worldObject).setActive(false);
			} else if(u == activeChunk)  {
				((Chunk)worldObject).setActive(true);
				((Chunk)worldObject).setLastActive(false);
			} else {
				((Chunk)worldObject).setActive(false);
				((Chunk)worldObject).setLastActive(false);
			}
			
			worldObject.update(delta);
			u++;
		});
	}

	@Override
	public void render(RenderEngine renderer) {
		
		i = 0;
		worldObjects.stream().forEach(worldObject -> {
			i++;
			worldObject.render(renderer);
		});
		
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Chunks: "+ i));			
		
	}

	public void dispose() {
		worldObjects.stream().forEach(worldObject -> {
			worldObject.dispose();
		});
	}

}
