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
	
    private SoftReference<TreeSet<String>> softCulledCoords;
	
	ChunkMaker(Mesh mesh, Vector3f voxCount, Vector3f positionOffset, boolean rebuildEvent) {
		this.mesh = mesh;
		
		this.voxCount = voxCount;
		this.positionOffset = positionOffset;
		
		voxGeo = new VoxelGeometry();
		
		softCulledCoords = new SoftReference<TreeSet<String>>(new TreeSet<String>());	
		
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
		
		SoftReference<TreeSet<String>> softWorkingCulledCoords = softCulledCoords;//new SoftReference<TreeSet<String>>(mesh.getCulledCoords());
		if(rebuildEvent) {
			mesh.setCulledCoords(new TreeSet<String>());
		}		
		
		boolean[] faces = new boolean[6];
        int index = 0;
        int culled = 0;
        for(float x=0 ; x < voxCount.x ; x++) {

        	for (float y=0 ; y < voxCount.y ; y++) {

        		for (float z=0; z < voxCount.z ; z++) {
        			
        			vector.set((x-positionOffset.x),(y-positionOffset.y),(z-positionOffset.z));
        			
        			if(!softWorkingCulledCoords.get().contains(vector.x+"-"+vector.y+"-"+vector.z)) {
        				 
        				hiddenFaces(vector, faces);
        				int faceCount = 0;
        				if(faces[0]) {
        					mesh.getVertBuffer().put(voxGeo.getVertices(vector, "front"));
        					faceCount++;
        				}
        				
        				if(faces[1]) {
        					mesh.getVertBuffer().put(voxGeo.getVertices(vector, "right"));
        					faceCount++;
        				}
        				
        				if(faces[2]) {
        					mesh.getVertBuffer().put(voxGeo.getVertices(vector, "back"));
        					faceCount++;
        				}
        				
        				if(faces[3]) {
        					mesh.getVertBuffer().put(voxGeo.getVertices(vector, "left"));
        					faceCount++;
        				}
        				
        				if(faces[4]) {
        					mesh.getVertBuffer().put(voxGeo.getVertices(vector, "bottom"));
        					faceCount++;
        				}
        				
        				if(faces[5]) {
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
        }
        
        mesh.getVertBuffer().flip();        
        mesh.getIndecesBuffer().flip();
        mesh.setEntityCount(mesh.getEntityCount()-culled);
        
        vector = null;
        weakCullablesMap.clear();
        
        return mesh;
	}
	
	private void hiddenFaces(Vector3f vector, boolean[] faces) {

		faces[0] = true;
		faces[1] = true;
		faces[2] = true;
		faces[3] = true;
		faces[4] = true;
		faces[5] = true;
	}
	
}
