#version 430

layout (location = 0) out vec4 outputColour;

in vec2 mapCoord_FS;
in vec3 position_FS;
in vec3 tangent_FS;

struct Material{
	
	sampler2D diffusemap;
	sampler2D normalmap;
	sampler2D heightmap;
	float heightScaling;
	float horizontalScaling;
};

uniform sampler2D normalmap;
uniform sampler2D splatmap;
uniform Material materials[3];
uniform int tbn_range;
uniform vec3 cameraPosition;

const vec3 lightDirection = vec3(0.1,-1.0,0.1);
const float intensity = 1.2;
const float zFar = 10000;
const float zNear = 0.1;
const vec3 fogColour = vec3(0.65, 0.85, 0.9);
const float sightRange = 0.6;

float diffuse(vec3 direction, vec3 normal, float intensity){
	return max(0.01, dot(normal, -direction) * intensity);
}

float getFogFactor(float dist){
	return -0.0002 / sightRange * (dist - zFar / 10 * sightRange) + 1;
}

vec3 applyFog(vec3 rgb, float dist){
	float fogAmouont = 1.0 - exp(-dist*0.0005);
	//return mix(rgb, fogColour, fogAmouont);
	vec3 finalColour = rgb * (fogAmouont) + fogColour * fogAmouont;
	return finalColour;
}

/*
vec3 applyFog(vec3 rgb, float dist, vec3 rayDir, vec3 sunDir){
	float fogAmouont = 1.0 - exp(-dist * 0.0005);
	float sunAmount = max( dot(rayDir, sunDir), 0.0);
	vec3 fogColour = mix( vec3(0.5, 0.6, 0.7), vec3(1.0, 0.9, 0.7), pow(sunAmount, 8.0));
	return mix(rgb, fogColour, fogAmouont);
}
*/
void main(){
	
	float dist = length(cameraPosition - position_FS);
	float height = position_FS.y;
	
	vec3 normal = normalize(texture(normalmap, mapCoord_FS).rbg);
	
	vec4 blendValues = texture(splatmap, mapCoord_FS).rgba;
			
	float[4] blendValuesArray = float[](blendValues.r,blendValues.g,blendValues.b,blendValues.a);		
	
	if(dist < tbn_range - 50){
		float attenuation = clamp(-dist/(tbn_range-50) + 1, 0.0, 1.0);
		
		vec3 bitangent = normalize(cross(tangent_FS, normal));
		mat3 TBN = mat3(tangent_FS, normal, bitangent);
		
		vec3 bumpNormal;
		for(int i = 0; i < 3; i++){
			bumpNormal += (2*(texture(materials[i].normalmap, mapCoord_FS * materials[i].horizontalScaling).rbg) - 1) * blendValuesArray[i];
		}
		
		bumpNormal = normalize(bumpNormal);
		
		bumpNormal.xz *= attenuation;
		
		normal = normalize(TBN * bumpNormal);
	}
	
	vec3 fragColour = vec3(0,0,0);
	for(int i = 0; i < 3; i++){
		fragColour += texture(materials[i].diffusemap, mapCoord_FS * materials[i].horizontalScaling).rgb
					* blendValuesArray[i];
	}
	
	float diff = diffuse(lightDirection, normal, intensity);
	fragColour *= diff;
	
	float fogFactor = getFogFactor(dist);
	fragColour = mix(fogColour, fragColour, clamp(fogFactor, 0, 1));
//	vec3 rayDir = (position_FS - cameraPosition);
//	fragColour = applyFog(fragColour, dist);
	
	outputColour = vec4(fragColour, 1.0);
}