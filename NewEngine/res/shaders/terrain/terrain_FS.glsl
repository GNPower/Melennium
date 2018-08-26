#version 430

layout (location = 0) out vec4 outputColour;

in vec2 mapCoord_FS;
in vec3 position_FS;
in vec3 tangent_FS;
/**/in vec3 toLightVector_FS;

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
/**/uniform vec3 lightColour;

const vec3 lightDirection = vec3(0.1,-1.0,0.1);
const float intensity = 1.2;
const float zFar = 10000;
const float zNear = 0.1;
//const vec3 fogColour = vec3(0.65, 0.85, 0.9);
const float sightRange = 0.6;

vec3 diffuse(vec3 lightDirection, vec3 normal){
		vec3 unitLightDir = normalize(lightDirection);
		float nDotl = dot(normal, unitLightDir);
		float brightness = max(nDotl, 0.1);
		vec3 diff = brightness * lightColour;
		return diff;
}

vec3 applyFog(vec3 rgb, float dist, vec3 rayDir, vec3 sunDir){
	//float fogAmouont = 1.0 - exp(-dist*0.0005);
	vec3 unitRay = normalize(rayDir);
	vec3 unitSun = normalize(sunDir);
	float sunAmount = max( dot(unitRay, unitSun), 0.0);
	vec3 fogColour = mix(vec3(0.5, 0.6, 0.7),
						vec3(1.0,0.9,0.7),
						pow(sunAmount, 8.0) );
	//return mix(rgb, fogColour, fogAmouont);
}

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
	
	fragColour *= diffuse(toLightVector_FS, normal);
	
	vec3 rayDir = (position_FS - cameraPosition);
	fragColour = applyFog(fragColour, dist, rayDir, -toLightVector_FS);
	
	outputColour = vec4(fragColour, 1.0);
}