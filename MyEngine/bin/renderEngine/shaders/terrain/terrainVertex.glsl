#version 400 core

const int maxLights = 4;

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector[maxLights + 1];
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[maxLights + 1];
uniform float syncNormals;
uniform int renderFog;

//determines the thickness of the fog and the general visibility of the scene
const float density = 0.0035;
//determines the strength of the transition from no fog to full fog
const float gradient = 5.0;

const float fogStart = 10.0;
const float fogEnd = 100.0;

void expFog(float camD){
	visibility = exp(-pow((camD * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}

void linFog(float camD){
	visibility = (fogEnd - camD)/(fogEnd - fogStart);
	visibility = clamp(visibility, 0.0, 1.0);
}

void expSqFog(float camD){
	float exponent = pow((camD * density), gradient);
	visibility = exp(-1*(exponent * exponent));
	visibility = clamp(visibility, 0.0, 1.0);
}

void main(){
	
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	
	gl_Position = projectionMatrix * positionRelativeToCamera;
	pass_textureCoords = textureCoords;
	
	vec3 adjustedNormal = normal;
	if(syncNormals > 0.5){
		adjustedNormal = vec3(0, 1, 0);
	}
	
	surfaceNormal = (transformationMatrix * vec4(adjustedNormal, 0.0)).xyz;

	for(int i = 0; i < maxLights + 1; i++){
		toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	
	float camDistance = length(positionRelativeToCamera.xyz);
	if(renderFog == 1){
		expFog(camDistance);
		//linFog(camDistance);
		//expSqFog(camDistance);
	}else{
		visibility = 1.0;
	}
}