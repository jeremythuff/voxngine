package voxngine.graphics;

import static org.lwjgl.opengl.GL30.*;

public class Vao implements RenderObject {

	private int id;
	private int count;
	
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
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

}
