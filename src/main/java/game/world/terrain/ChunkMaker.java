package game.world.terrain;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import org.joml.Vector3f;

import voxngine.graphics.Mesh;

class ChunkMaker implements Callable<Mesh> {
	
	private Mesh mesh;
	
	private Vector3f voxCount;
	private Vector3f positionOffset;
	
	private VoxelGeometry voxGeo;
	
	private boolean rebuildEvent;
	private boolean activeChunk;
		
	ChunkMaker(Mesh mesh, Vector3f voxCount, Vector3f positionOffset, boolean rebuildEvent) {
		this.mesh = mesh;
		
		this.voxCount = voxCount;
		this.positionOffset = positionOffset;
		
		voxGeo = new VoxelGeometry();
				
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
				
		SoftReference<Map<String, Integer>> weakCullablesMap = new SoftReference<Map<String, Integer>>(new HashMap<String, Integer>());
		Vector3f vector = new Vector3f();
		
        int index = 0;
        int culled = 0;
        for(float x=0 ; x < voxCount.x ; x++) {

        	for (float y=0 ; y < voxCount.y ; y++) {

        		for (float z=0; z < voxCount.z ; z++) {
        			
        			vector.set((x-positionOffset.x),(y-positionOffset.y),(z-positionOffset.z));
        			
        				
    				int faceCount = 0;
    				
    				if(hiddenFaces(vector, "front")) {
    					mesh.getVertBuffer().put(voxGeo.getVertices(vector, "front"));
    					faceCount++;
    				}
    				
    				if(hiddenFaces(vector,"right")) {
    					mesh.getVertBuffer().put(voxGeo.getVertices(vector, "right"));
    					faceCount++;
    				}
    				
    				if(hiddenFaces(vector, "back")) {
    					mesh.getVertBuffer().put(voxGeo.getVertices(vector, "back"));
    					faceCount++;
    				}
    				
    				if(hiddenFaces(vector, "left")) {
    					mesh.getVertBuffer().put(voxGeo.getVertices(vector, "left"));
    					faceCount++;
    				}
    				
    				if(hiddenFaces(vector, "bottom")) {
    					mesh.getVertBuffer().put(voxGeo.getVertices(vector, "bottom"));
    					faceCount++;
    				}
    				
    				if(hiddenFaces(vector, "top")) {
    					mesh.getVertBuffer().put(voxGeo.getVertices(vector, "top"));
    					faceCount++;
    				}
    				
    				
    				for(int i = 0; i<faceCount ; i++) {
    					 mesh.getIndecesBuffer().put(voxGeo.getIndices(index, i));
    				}
        	        	
        	        index += faceCount*4;
                	
        			 			
        		}
        	}
        }
        
        mesh.getVertBuffer().flip();        
        mesh.getIndecesBuffer().flip();
        mesh.setEntityCount(mesh.getEntityCount()-culled);
        
        vector = null;
        weakCullablesMap.clear();
        
        return mesh;
	}
	
	private boolean hiddenFaces(Vector3f vector, String face) {
		
		boolean visible;
		
		switch(face) {
		
			case "front":
				if(vector.z+1 == voxCount.z) {
					visible = true;
				} else {
					visible = false;
				}
			break;
			
			case "right":
				if(vector.x+1 == voxCount.x) {
					visible = true;
				} else {
					visible = false;
				}
			break;
				
			case "back":
				if(vector.z == 0) {
					visible = true;
				} else {
					visible = false;
				}
			break;
				
			case "left":
				if(vector.x == 0) {
					visible = true;
				} else {
					visible = false;
				}
			break;
				
			case "bottom":
				if(vector.y == 0) {
					visible = true;
				} else {
					visible = false;
				}
			break;
				
			case "top":
				if(vector.y+1 == voxCount.y) {
					visible = true;
				} else {
					visible = false;
				}
			break;
			
			default: visible = true;
		
		}
		
		
		return visible;
	}
	
}
