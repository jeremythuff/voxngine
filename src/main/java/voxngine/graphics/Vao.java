package voxngine.graphics;

import static org.lwjgl.opengl.GL30.*;

public class Vao {

	private int id;
	
	public Vao() {
		id = glGenVertexArrays();
	}
	
	public void bind() {
		glBindVertexArray(id);
	}
	
	public void unbind() {
		glBindVertexArray(0);
	}
	
	 public void delete() {
	    glDeleteVertexArrays(id);
	 }
	
	public int getID() {
		return id;
	}

}
