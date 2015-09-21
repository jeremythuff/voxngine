#version 330 core

in vec4 vColor;
out vec4 FragColor;

void main()
{
	//if(vColor.w < 1.0) discard;
    FragColor = vColor;
}