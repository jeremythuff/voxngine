package game.world.terrain;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.stb.STBPerlin.stb_perlin_noise3;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

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

		int width = 3000;
		int chunksPerSide = 60;
		int height = 20;
		float minAlt = height/1.5f;
		
		float randomNum = (float) Math.random()*0.01f;
		float value = 0.01f+randomNum;
				
		SoftReference<byte[]> weakVoxelMap = new SoftReference<byte[]>(new byte[width*height*width]); 
		
		int i = 0;
//		for(int x = -width/2; x < width/2; x++) {
//			for(int y = 0; y < height; y++) {
//				for(int z = -width/2; z < width/2; z++) {	
//					
//					if(y+((stb_perlin_noise3(x*value,0,z*value,0,0,0))*minAlt/2) < minAlt) {
//						if(y>=minAlt/1.45) {
//							weakVoxelMap.get()[i] = 1;
//						} else {
//							weakVoxelMap.get()[i] = 2;
//						}
//					} else {
//						weakVoxelMap.get()[i] = 0;
//					}
//						
//					i++;
//				}
//			}
//		}
						
		WeakReference<byte[]> chunkMap;
		
		int m = 0;
								
//		for(int chunkCounter = 0 ; chunkCounter < chunksPerSide*chunksPerSide ; chunkCounter++) {
//			
//			chunkMap = new WeakReference<byte[]>(new byte[(width/chunksPerSide)*height*(width/chunksPerSide)]);
//			int c = 0;
//			
//			if(chunkCounter%chunksPerSide==0) m = (width/chunksPerSide)*(chunkCounter/chunksPerSide);
//			
//			for (i=0 ; i < ((width/chunksPerSide)*height*(width/chunksPerSide)) ; i++) {
//				chunkMap.get()[i] = weakVoxelMap.get()[m];
//				m++;
//				c++;
//				if(c%(width/chunksPerSide)==0) {
//					c = 0;
//					m += (width/chunksPerSide)*(chunksPerSide-1);
//				}
//			}
//			
//			Path path = Paths.get("src/main/resources/maps/"+chunkCounter+".map");
//			
//			try {
//				Files.write(path, chunkMap.get());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			chunkMap.clear();
//		}
		
		weakVoxelMap.clear();

		
		float XStart = 0;
		float YStart = 0;
		float ZStart = 0;
					
		int chunkCounter = 0;
		int startingMap = 500;
		for(int c = 0 ; c < 25 ; c++) {
			
			worldObjects.add(new Chunk(new Vector3f(XStart,YStart,ZStart), chunkCounter+startingMap));
			chunkCounter++;
			if(chunkCounter%5==0) {
				chunkCounter+=(chunksPerSide)-5;
			}
			
			
			
			XStart += 50;
			if((c+1)%5==0) {
				if(c != 0) {
					XStart = 0;
					ZStart += 50;
				}
			}
			
		}
		
		
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
