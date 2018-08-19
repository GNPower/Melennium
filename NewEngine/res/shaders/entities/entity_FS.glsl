#version 430

in vec3 worldPosition;
in vec2 texCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;

layout (location = 0) out vec4 outputColour;

uniform vec3 lightColour;

uniform sampler2D tex;

void main(){

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDotl = dot(unitNormal, unitLightVector);
	float brightness = max(nDotl, 0.0);
	vec3 diffuse = brightness * lightColour;
	
	outputColour = vec4(diffuse,1.0) * texture(tex, texCoords);
}