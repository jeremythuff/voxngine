package voxngine.graphics;

import static org.lwjgl.opengl.GL30.*;

public class Vao {

	private int id;
	
	public Vao() {
		@SuppressWarnings("unused")
		int id = glGenVertexArrays();
	}
	
	public void bind() {
		glBindVertexArray(id);
	}
	
	 public void delete() {
	    glDeleteVertexArrays(id);
	 }
	
	public int getId() {
		return id;
	}

}
