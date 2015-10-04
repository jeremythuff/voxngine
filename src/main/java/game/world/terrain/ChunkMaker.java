package game.world.terrain;

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
                		
            			float[] newArray = voxGeo.getVertices(voxel, type,"front");
            	
            			for(int v=0; v<newArray.length; v++) {
            				mesh.addVert(newArray[v]);
            			}	
            			
            			faceCount++;
            		}
            		
            		if(hiddenFaces(voxel, "right")) {
            			float[] newArray = voxGeo.getVertices(voxel, type,"right");
                    	
            			for(int v=0; v<newArray.length; v++) {
            				mesh.addVert(newArray[v]);
            			}	
            			
            			faceCount++;
            		}
            		
            		if(hiddenFaces(voxel, "back")) {
            			float[] newArray = voxGeo.getVertices(voxel, type,"back");
                    	
            			for(int v=0; v<newArray.length; v++) {
            				mesh.addVert(newArray[v]);
            			}	
            			
            			faceCount++;
            		}
            		
            		if(hiddenFaces(voxel, "left")) {
            			float[] newArray = voxGeo.getVertices(voxel, type,"left");
                    	
            			for(int v=0; v<newArray.length; v++) {
            				mesh.addVert(newArray[v]);
            			}	
            			
            			faceCount++;
            		}
            		
            		if(hiddenFaces(voxel, "bottom")) {
            			float[] newArray = voxGeo.getVertices(voxel, type,"bottom");
                    	
            			for(int v=0; v<newArray.length; v++) {
            				mesh.addVert(newArray[v]);
            			}	
            			
            			faceCount++;
            		}
            		
            		if(hiddenFaces(voxel, "top")) {
            			float[] newArray = voxGeo.getVertices(voxel, type,"top");
                    	
            			for(int v=0; v<newArray.length; v++) {
            				mesh.addVert(newArray[v]);
            			}	
            			
            			faceCount++;
            		}
            		
            		for(int i = 0; i<faceCount ; i++) {
            			
            			Integer[] newArray = voxGeo.getIndices(index, i);
            			
            			for(int ind=0; ind<newArray.length; ind++) {
            				mesh.addInd(newArray[ind]);
            			}
            			
            		}
                    
            		m++;
            		index += faceCount*4;
        		}
        	}
        }
        
        chunkMap.clear();
                                
        return mesh;
	}
	
	private boolean hiddenFaces(int[] voxel, String string) {
		return true;
	}
	
}
