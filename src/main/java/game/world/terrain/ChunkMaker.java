package game.world.terrain;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
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
				
        int index = 0;
        int culled = 0;
        for(float x=0 ; x < voxCount.x ; x++) {

        	for (float y=0 ; y < voxCount.y ; y++) {

        		for (float z=0; z < voxCount.z ; z++) {
        			
        			vector.set((x-positionOffset.x),(y-positionOffset.y),(z-positionOffset.z));
        			
        			if(!softWorkingCulledCoords.get().contains(vector.x+"-"+vector.y+"-"+vector.z)) {
        				
        				float[] vertices = null;
        				if(y == voxCount.y-1) {
        					if(activeChunk) {
            					vertices = voxGeo.getVertices(vector, VoxelType.ACTIVE);
            				} else {
            					vertices = voxGeo.getVertices(vector);
            				}
        				} else {
        					vertices = voxGeo.getVertices(vector, VoxelType.DIRT);
        				}
        					
        				
        				mesh.getVertBuffer().put(vertices);
                		
            	        int[] indeces = voxGeo.getIndices(index);
            	        mesh.getIndecesBuffer().put(indeces);
            	        index++;
                	} else if(!rebuildEvent) {
                		//culled++;
                		continue;
                	}
        			
        			cullTest(weakCullablesMap.get(), vector.set((x-positionOffset.x),(y-positionOffset.y)-1,(z-positionOffset.z)));        			
        			cullTest(weakCullablesMap.get(), vector.set((x-positionOffset.x)+1,(y-positionOffset.y)-1,(z-positionOffset.z)+1));
        			cullTest(weakCullablesMap.get(), vector.set((x-positionOffset.x)+1,(y-positionOffset.y)-1,(z-positionOffset.z)-1));
        			
        			cullTest(weakCullablesMap.get(), vector.set((x-positionOffset.x)-1,(y-positionOffset.y)-1,(z-positionOffset.z)+1));
        			cullTest(weakCullablesMap.get(), vector.set((x-positionOffset.x)-1,(y-positionOffset.y)-1,(z-positionOffset.z)-1));
        			cullTest(weakCullablesMap.get(), vector.set((x-positionOffset.x)-1,(y-positionOffset.y)-1,(z-positionOffset.z)));
        			
        			cullTest(weakCullablesMap.get(), vector.set((x-positionOffset.x)+1,(y-positionOffset.y)-1,(z-positionOffset.z)));
        			cullTest(weakCullablesMap.get(), vector.set((x-positionOffset.x),(y-positionOffset.y)-1,(z-positionOffset.z)-1));
        			cullTest(weakCullablesMap.get(), vector.set((x-positionOffset.x),(y-positionOffset.y)-1,(z-positionOffset.z)+1));
        			
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
	
	
	private void cullTest(Map<String, Integer> cullables, Vector3f vector) {
		
		String coords = vector.x+"-"+vector.y+"-"+vector.z;
		
		if(cullables.get(coords) == null) {
			cullables.put(coords, 129);
		} else {
			int curentValue = cullables.get(coords);			
			if(curentValue > 135) {
				softCulledCoords.get().add(coords);
			} else {
				cullables.replace(coords, curentValue+=1);
			}
		}
				
	}

}
