#version 430

in vec3 worldPosition;

layout (location = 0) out vec4 outputColour;

const vec3 baseColour = vec3(0.18, 0.27, 0.47);

void main(){
	
	float red = -0.00022 * (worldPosition.y - 2800) + baseColour.x;
	float green = -0.00025 * (worldPosition.y - 2800) + baseColour.y;
	float blue = -0.00019 * (worldPosition.y - 2800) + baseColour.z;
	
	outputColour = vec4(red, green, blue, 1);
}