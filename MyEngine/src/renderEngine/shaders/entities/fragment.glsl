#version 400 core

const int maxLights = 4;

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[maxLights + 1];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Colour;

uniform sampler2D textureSampler;

uniform vec3 lightColour[maxLights + 1];
uniform float lightBrightnessFactor[maxLights + 1];
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientLightFactor;
uniform vec3 skyColour;
uniform vec3 attenuation[maxLights];

vec3 diffuseLighting(vec3 normal, vec3 light, int index){		
	
	float nDotl = dot(normal, light);
	float brightness = max(nDotl, 0.0);
	vec3 diffuse = vec3(brightness * lightColour[index]);
	diffuse = diffuse * lightBrightnessFactor[index];
	
	return diffuse;
}

vec3 specularLighting(vec3 normal, vec3 light, vec3 unitCameraVector, int index){
	
	vec3 lightDirection = -light;
	
	vec3 reflectedLight = reflect(lightDirection, normal);
	
	float specularFactor = dot(reflectedLight, unitCameraVector);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	
	vec3 finalSpecular = vec3(dampedFactor * reflectivity * lightColour[index]);
	finalSpecular = finalSpecular * lightBrightnessFactor[index];
	
	return finalSpecular;
}

void main(){
		
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitCameraVector = normalize(toCameraVector);
	vec3 unitSunVector = normalize(toLightVector[maxLights]);
	
	vec3 totalDiffuse = diffuseLighting(unitNormal, unitSunVector, maxLights);
	vec3 totalSpecular = specularLighting(unitNormal, unitSunVector, unitCameraVector, maxLights);
	
	for(int i = 0; i < maxLights; i++){
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);	
		totalDiffuse = totalDiffuse + (diffuseLighting(unitNormal, unitLightVector, i) / attFactor);
		totalSpecular = totalSpecular + (specularLighting(unitNormal, unitLightVector, unitCameraVector, i) / attFactor);
	}
	totalDiffuse = max(totalDiffuse, ambientLightFactor);
	
	vec4 textureColour = texture(textureSampler, pass_textureCoords);
	if(textureColour.a < 0.5){
		discard;
	}
	
	out_Colour = vec4(totalDiffuse, 1.0) * textureColour + vec4(totalSpecular, 1.0);
	out_Colour = mix(vec4(skyColour, 1.0), out_Colour, visibility);
}