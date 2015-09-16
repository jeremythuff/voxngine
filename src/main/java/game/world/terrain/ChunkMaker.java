package game.world.terrain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.Callable;

import org.joml.Vector3f;

import voxngine.graphics.Mesh;

class ChunkMaker implements Callable<Mesh> {
	
	private Mesh mesh;
	
	private Vector3f voxCount;
	private Vector3f positionOffset;
	
	private VoxelGeometry voxGeo;
	
	private boolean rebuildEvent;
	
	
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
	
	@Override
	public Mesh call() throws Exception {
		Map<String, Integer> cullables = new HashMap<String, Integer>();
		Vector3f vector = new Vector3f();
		
		HashSet<String> workingCulledCoords = mesh.getCulledCoords();
		if(rebuildEvent) {
			mesh.setCulledCoords(new HashSet<String>());
		}
				
        int index = 0;
        for(float x=0 ; x < voxCount.x ; x++) {

        	for (float y=0 ; y < voxCount.y ; y++) {

        		for (float z=0; z < voxCount.z ; z++) {
        			
        			vector.set((x-positionOffset.x),(y-positionOffset.y),(z-positionOffset.z));
        			
        			if(!workingCulledCoords.contains(vector.x+"-"+vector.y+"-"+vector.z)) {
        				
        				
        				float[] vertices = null;
        				if(y == voxCount.y-1) {
        					vertices = voxGeo.getVertices(vector);
        				} else {
        					vertices = voxGeo.getVertices(vector, VoxelType.DIRT);
        				}
        					
        				
        				mesh.getVertBuffer().put(vertices);
                		
            	        int[] indeces = voxGeo.getIndices(index);
            	        mesh.getIndecesBuffer().put(indeces);
            	        index++;
                	} else if(!rebuildEvent) {
                		continue;
                	}
        			
        			cullTest(cullables, vector.set((x-positionOffset.x),(y-positionOffset.y)-2,(z-positionOffset.z)));        			
        			cullTest(cullables, vector.set((x-positionOffset.x)+1,(y-positionOffset.y)-2,(z-positionOffset.z)+1));
        			cullTest(cullables, vector.set((x-positionOffset.x)+1,(y-positionOffset.y)-2,(z-positionOffset.z)-1));
        			
        			cullTest(cullables, vector.set((x-positionOffset.x)-1,(y-positionOffset.y)-2,(z-positionOffset.z)+1));
        			cullTest(cullables, vector.set((x-positionOffset.x)-1,(y-positionOffset.y)-2,(z-positionOffset.z)-1));
        			cullTest(cullables, vector.set((x-positionOffset.x)-1,(y-positionOffset.y)-2,(z-positionOffset.z)));
        			
        			cullTest(cullables, vector.set((x-positionOffset.x)+1,(y-positionOffset.y)-2,(z-positionOffset.z)));
        			cullTest(cullables, vector.set((x-positionOffset.x),(y-positionOffset.y)-2,(z-positionOffset.z)-1));
        			cullTest(cullables, vector.set((x-positionOffset.x),(y-positionOffset.y)-2,(z-positionOffset.z)+1));
        			
        		}
        	}
        }
        
        vector = null;
        cullables = null;
        
        mesh.getVertBuffer().flip();        
        mesh.getIndecesBuffer().flip();
        
        return mesh;
	}
	
	private Map<String, Integer> cullTest(Map<String, Integer> cullables, Vector3f vector) {
		
		String coords = vector.x+"-"+vector.y+"-"+vector.z;
		
		if(cullables.get(coords) == null) {
			cullables.put(coords, 1);
		} else {
			int curentValue = cullables.get(coords);			
			if(curentValue > 7) {
				mesh.getCulledCoords().add(coords);
			} else {
				cullables.replace(coords, curentValue+=1);
			}
		}
		
		return cullables;
		
	}

}
