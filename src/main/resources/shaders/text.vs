#version 150 core

in vec2 fontPos;
in vec3 fontColor;
in vec2 fontTexcoord;

out vec3 vertexColor;
out vec2 textureCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vertexColor = fontColor;
    textureCoord = fontTexcoord;
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(fontPos, 0.0, 1.0);
}
