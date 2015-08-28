package voxngine.graphics;

import java.util.ArrayList;
import java.util.List;

public class RenderEngine {
	
	static List<float[]> verts = new ArrayList<float[]>();
	
	public static void queVbo(float[] vert) {
		verts.add(vert);
	}
	
	public static void render() {
		for(float[] vert : verts) {
			System.out.println(vert.length);
		}
	}
	
	public static void init() {
		new RenderEngine();
	}

}
