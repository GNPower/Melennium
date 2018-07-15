#version 430

in vec3 worldPosition;

layout (location = 0) out vec4 outputColour;

void main(){
	
	outputColour = vec4(0.1,0.1,1.0,1.0);
}