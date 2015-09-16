#version 330 core

in vec4 vColor;

void main()
{
	if(vColor.w < 1.0) discard;
    gl_FragColor = vColor;
}