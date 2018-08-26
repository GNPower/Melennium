#version 430

in vec3 worldPosition;
in vec2 texCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

layout (location = 0) out vec4 outputColour;

uniform vec3 lightColour;

uniform sampler2D tex;
uniform sampler2D specularmap;

uniform float reflectivity;

void main(){

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	vec3 unitCameraVector = normalize(toCameraVector);
	vec3 lightDir = -unitLightVector;
	vec3 reflectedDir = reflect(lightDir, unitNormal);
	
	float specularFactor = dot(reflectedDir, unitCameraVector);
	specularFactor = max(specularFactor, 0.0);
	
	float dampedFactor = pow(specularFactor, texture(specularmap, texCoords).x * 10);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColour;
	
	float nDotl = dot(unitNormal, unitLightVector);
	float brightness = max(nDotl, 0.1);
	vec3 diffuse = brightness * lightColour;
	
	outputColour = vec4(diffuse,1.0) * texture(tex, texCoords) + vec4(finalSpecular, 1.0);
}