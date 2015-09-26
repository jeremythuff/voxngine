package game.world.terrain;

import java.util.concurrent.Callable;

import voxngine.graphics.Mesh;

class ChunkMaker implements Callable<Mesh> {
	
	private Mesh mesh;
	private int[][] voxMap;
	private VoxelGeometry voxGeo;
	
	private boolean rebuildEvent;
	private boolean activeChunk;	
		
	ChunkMaker(Mesh mesh, int[][] voxelMap, boolean rebuildEvent) {
		this.mesh = mesh;
		this.voxMap = voxelMap;
		this.voxGeo = new VoxelGeometry();
				
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
        
        for(int[] voxel : voxMap) {
        	
        	if(voxel[3]==-1) continue;
        	        	
        	int faceCount = 0;
        	
        	type = VoxelType.values()[(int) voxel[3]];
        	
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
            	
            index += faceCount*4;
        }
        
        mesh.getVertBuffer().flip();        
        mesh.getIndecesBuffer().flip();
                
        return mesh;
	}
	
	private boolean hiddenFaces(int[] voxel, String string) {
		
		
		
		return true;
	}
	
}
