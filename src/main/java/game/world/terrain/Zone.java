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
		float value = 0.02f+randomNum;
				
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
		
		int[][] chunkMap6 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap7 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap8 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap9 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap10 = new int[(width/5)*height*(width/5)][4];
		
		int[][] chunkMap11 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap12 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap13 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap14 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap15 = new int[(width/5)*height*(width/5)][4];
		
		int[][] chunkMap16 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap17 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap18 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap19 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap20 = new int[(width/5)*height*(width/5)][4];
		
		int[][] chunkMap21 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap22 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap23 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap24 = new int[(width/5)*height*(width/5)][4];
		int[][] chunkMap25 = new int[(width/5)*height*(width/5)][4];

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
		
		c = 0;
		m= (width/5);
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap6[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap7[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap8[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap9[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap10[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		m= (width/5)*2;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap11[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap12[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap13[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap14[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap15[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		m= (width/5)*3;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap16[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap17[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap18[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap19[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap20[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		m= (width/5)*4;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap21[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap22[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap23[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap24[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
		
		c = 0;
		for (i=0 ; i < ((width/5)*height*(width/5)) ; i++) {
			chunkMap25[i] = voxelMap[m];
			m++;
			c++;
			if(c%(width/5)==0) {
				c = 0;
				m += (width/5)*4;
			}
		}
				
		worldObjects.add(new Chunk(chunkMap1));
		worldObjects.add(new Chunk(chunkMap2));
		worldObjects.add(new Chunk(chunkMap3));
		worldObjects.add(new Chunk(chunkMap4));
		worldObjects.add(new Chunk(chunkMap5));
		
		worldObjects.add(new Chunk(chunkMap6));
		worldObjects.add(new Chunk(chunkMap7));
		worldObjects.add(new Chunk(chunkMap8));
		worldObjects.add(new Chunk(chunkMap9));
		worldObjects.add(new Chunk(chunkMap10));
		
		worldObjects.add(new Chunk(chunkMap11));
		worldObjects.add(new Chunk(chunkMap12));
		worldObjects.add(new Chunk(chunkMap13));
		worldObjects.add(new Chunk(chunkMap14));
		worldObjects.add(new Chunk(chunkMap15));
		
		worldObjects.add(new Chunk(chunkMap16));
		worldObjects.add(new Chunk(chunkMap17));
		worldObjects.add(new Chunk(chunkMap18));
		worldObjects.add(new Chunk(chunkMap19));
		worldObjects.add(new Chunk(chunkMap20));
		
		worldObjects.add(new Chunk(chunkMap21));
		worldObjects.add(new Chunk(chunkMap22));
		worldObjects.add(new Chunk(chunkMap23));
		worldObjects.add(new Chunk(chunkMap24));
		worldObjects.add(new Chunk(chunkMap25));
		
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
