package game.world;

import static org.lwjgl.stb.STBPerlin.stb_perlin_noise3;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WorldGenerator {

	public void makeWorld(int dimensions) {
		
		System.out.println("Generatign World!");
		
		int width = 3000;
		int chunksPerSide = dimensions;
		int height = 20;
		float minAlt = height/1.5f;
		
		float randomNum = (float) Math.random()*0.01f;
		float value = 0.01f+randomNum;
				
		SoftReference<byte[]> weakVoxelMap = new SoftReference<byte[]>(new byte[width*height*width]); 
		
		int i = 0;
		for(int x = -width/2; x < width/2; x++) {
			for(int y = 0; y < height; y++) {
				for(int z = -width/2; z < width/2; z++) {	
					
					if(y+((stb_perlin_noise3(x*value,0,z*value,0,0,0))*minAlt/2) < minAlt) {
						if(y>=minAlt/1.45) {
							weakVoxelMap.get()[i] = 1;
						} else {
							weakVoxelMap.get()[i] = 2;
						}
					} else {
						weakVoxelMap.get()[i] = 0;
					}
						
					i++;
				}
			}
		}
						
		byte[] chunkMap;
		
		int m = 0;
								
		for(int chunkCounter = 0 ; chunkCounter < chunksPerSide*chunksPerSide ; chunkCounter++) {
			
			chunkMap = new byte[(width/chunksPerSide)*height*(width/chunksPerSide)];
			int c = 0;
			
			if(chunkCounter%chunksPerSide==0) m = (width/chunksPerSide)*(chunkCounter/chunksPerSide);
			
			for (i=0 ; i < ((width/chunksPerSide)*height*(width/chunksPerSide)) ; i++) {
				chunkMap[i] = weakVoxelMap.get()[m];
				m++;
				c++;
				if(c%(width/chunksPerSide)==0) {
					c = 0;
					m += (width/chunksPerSide)*(chunksPerSide-1);
				}
			}
			
			Path path = Paths.get("src/main/resources/maps/"+chunkCounter+".map");
			
			try {
				Files.write(path, chunkMap);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		weakVoxelMap.clear();
		
		System.out.println("Generated World!");

	}
	
}
