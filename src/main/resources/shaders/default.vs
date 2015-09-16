#version 330 core

layout(location = 0) in vec4 inVert;
layout(location = 1) in vec4 inColor;

uniform mat4 viewProjMatrix;
out vec4 vColor;
  
void main(void) {
	vColor = inColor;
	gl_Position = viewProjMatrix * normalize(inVert);
}

