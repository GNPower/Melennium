#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Colour;

uniform sampler2D textureSampler;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientLightFactor;
uniform vec3 skyColour;

vec4 diffuseLighting(vec3 normal, vec3 light){		
	
	float nDotl = dot(normal, light);
	float brightness = max(nDotl, ambientLightFactor);
	vec4 diffuse = vec4(brightness * lightColour, 1.0);
	
	return diffuse;
}

vec4 specularLighting(vec3 normal, vec3 light){
	
	vec3 unitCameraVector = normalize(toCameraVector);
	vec3 lightDirection = -light;
	
	vec3 reflectedLight = reflect(lightDirection, normal);
	
	float specularFactor = dot(reflectedLight, unitCameraVector);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	
	vec4 finalSpecular = vec4(dampedFactor * reflectivity * lightColour, 1.0);
	
	return finalSpecular;
}

void main(){
		
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);	
	
	vec4 textureColour = texture(textureSampler, pass_textureCoords);
	if(textureColour.a < 0.5){
		discard;
	}
	
	out_Colour = diffuseLighting(unitNormal, unitLightVector) * textureColour + specularLighting(unitNormal, unitLightVector);
	out_Colour = mix(vec4(skyColour, 1.0), out_Colour, visibility);
}