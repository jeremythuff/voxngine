#version 330 core

layout(location = 1) in vec4 vColor;

uniform mat4 viewProjMatrix;
out vec4 vColor;
  
void main(void) {
	gl_Position = viewProjMatrix * gl_Vertex;
}

