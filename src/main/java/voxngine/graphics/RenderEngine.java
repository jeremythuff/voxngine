package voxngine.graphics;

import java.util.ArrayList;
import java.util.List;

public class RenderEngine {
	
	List<Vbo> vbos = new ArrayList<Vbo>();
	
	public void queVbo(Vbo vbo) {
		vbos.add(vbo);
	}
	
	public void render() {
		
	}
	
	public static void init() {
		new RenderEngine();
	}

}
