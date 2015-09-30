package game.world.terrain;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import org.joml.Vector3f;

import voxngine.graphics.Mesh;

class ChunkMaker implements Callable<Mesh> {
	
	private Mesh mesh;
	private VoxelGeometry voxGeo;
	private Vector3f startCoords;
	
	private boolean rebuildEvent;
	private boolean activeChunk;
	private int chunkCounter;	
		
	ChunkMaker(Mesh mesh, int chunkCounter, Vector3f startCoords, boolean rebuildEvent) {
		this.mesh = mesh;
		this.chunkCounter = chunkCounter;
		this.voxGeo = new VoxelGeometry();
		this.startCoords =startCoords;		
		this.rebuildEvent = rebuildEvent;
	}
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public void setRebuildEvent(boolean rebuildEvent) {
		this.rebuildEvent = rebuildEvent;
	}
	
	public void setActiveChunk(boolean activeChunk) {
		this.activeChunk = activeChunk;
	}
	
	@Override
	public Mesh call() throws Exception {
								
        int index = 0;
       
        VoxelType type;
        
        int m = 0;
        
        Path path = Paths.get("src/main/resources/maps/"+chunkCounter+".map");
        SoftReference<byte[]> chunkMap = new SoftReference<byte[]>(Files.readAllBytes(path));
		
       
        for(int x=0; x<50; x++) {
        	for(int y=0; y<20; y++) {
        		voxelLoop:
        		for(int z=0; z<50; z++) {
        			
        			if(chunkMap.get()[m]==0)  {
        				m++;
        				continue voxelLoop;
        			}
    	        	
                	int faceCount = 0;
                	
                	type = VoxelType.values()[chunkMap.get()[m]];
                	
                	int[] voxel = new int[] {(int) (x+startCoords.x),(int) (y+startCoords.y),(int) (z+startCoords.z)};
                	
                	if(hiddenFaces(voxel, "front")) {
            			mesh.getVertBuffer().put(voxGeo.getVertices(voxel, type,"front"));
            			faceCount++;
            		}
            		
            		if(hiddenFaces(voxel, "right")) {
            			mesh.getVertBuffer().put(voxGeo.getVertices(voxel, type, "right"));
            			faceCount++;
            		}
            		
            		if(hiddenFaces(voxel, "back")) {
            			mesh.getVertBuffer().put(voxGeo.getVertices(voxel, type, "back"));
            			faceCount++;
            		}
            		
            		if(hiddenFaces(voxel, "left")) {
            			mesh.getVertBuffer().put(voxGeo.getVertices(voxel, type, "left"));
            			faceCount++;
            		}
            		
            		if(hiddenFaces(voxel, "bottom")) {
            			mesh.getVertBuffer().put(voxGeo.getVertices(voxel, type, "bottom"));
            			faceCount++;
            		}
            		
            		if(hiddenFaces(voxel, "top")) {
            			mesh.getVertBuffer().put(voxGeo.getVertices(voxel, type, "top"));
            			faceCount++;
            		}
            		
            		for(int i = 0; i<faceCount ; i++) {
            			mesh.getIndecesBuffer().put(voxGeo.getIndices(index, i));	 
            		}
                    
            		m++;
            		index += faceCount*4;
        		}
        	}
        }
        
        chunkMap.clear();
        
        mesh.getVertBuffer().flip();        
        mesh.getIndecesBuffer().flip();
                
        return mesh;
	}
	
	private boolean hiddenFaces(int[] voxel, String string) {
		return true;
	}
	
	/** Read the given binary file, and return its contents as a byte array.*/ 
	  byte[] read(String aInputFileName){
	    File file = new File(aInputFileName);
	    byte[] result = new byte[(int)file.length()];
	    try {
	      InputStream input = null;
	      try {
	        int totalBytesRead = 0;
	        input = new BufferedInputStream(new FileInputStream(file));
	        while(totalBytesRead < result.length){
	          int bytesRemaining = result.length - totalBytesRead;
	          //input.read() returns -1, 0, or more :
	          int bytesRead = input.read(result, totalBytesRead, bytesRemaining); 
	          if (bytesRead > 0){
	            totalBytesRead = totalBytesRead + bytesRead;
	          }
	        }
	        /*
	         the above style is a bit tricky: it places bytes into the 'result' array; 
	         'result' is an output parameter;
	         the while loop usually has a single iteration only.
	        */
	      }
	      finally {
	        input.close();
	      }
	    }
	    catch (FileNotFoundException ex) {
	    }
	    catch (IOException ex) {
	    }
	    return result;
	  }
	
}
