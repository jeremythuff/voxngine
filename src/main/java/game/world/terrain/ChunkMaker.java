package game.world.terrain;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import org.joml.Vector3f;

import voxngine.graphics.Mesh;

class ChunkMaker implements Callable<Mesh> {
	
	private Mesh mesh;
	private WeakReference<byte[]> chunkMap;
	private VoxelGeometry voxGeo;
	private Vector3f startCoords;
	
	private boolean rebuildEvent;
	private boolean activeChunk;	
		
	ChunkMaker(Mesh mesh, byte[] chunkMap, Vector3f startCoords, boolean rebuildEvent) {
		this.mesh = mesh;
		this.chunkMap = new WeakReference<byte[]>(chunkMap);
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
        
        mesh.getVertBuffer().flip();        
        mesh.getIndecesBuffer().flip();
                
        return mesh;
	}
	
	private boolean hiddenFaces(int[] voxel, String string) {
		return true;
	}
	
}
