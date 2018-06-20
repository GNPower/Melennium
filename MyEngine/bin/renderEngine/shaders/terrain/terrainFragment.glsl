#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Colour;

uniform sampler2D backgroundTexture;
uniform sampler2D aTexture;
uniform sampler2D bTexture;
uniform sampler2D cTexture;
uniform sampler2D dTexture;
uniform sampler2D blendMap;

uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientLightFactor;
uniform vec3 skyColour;

vec4 diffuseLighting(vec3 normal, vec3 light){		
	
	float nDotl = dot(normal, light);
	float brightness = max(nDotl, ambientLightFactor);
	vec4 diffuse = vec4(brightness * lightColour, 0.2);
	
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

vec4 textureColour(){
	
	vec4 blendMapColour = texture(blendMap, pass_textureCoords);
	
	float backTextureAmout = 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);		
	vec2 tiledCoords = pass_textureCoords * 40.0;
	
	vec4 backgroundTextureColour = texture(backgroundTexture, tiledCoords) * backTextureAmout;
	vec4 aTextureColour = texture(aTexture, tiledCoords) * blendMapColour.r;
	vec4 bTextureColour = texture(bTexture, tiledCoords) * blendMapColour.g;
	vec4 cTextureColour = texture(cTexture, tiledCoords) * blendMapColour.b;
	vec4 dTextureColour = texture(dTexture, tiledCoords) * blendMapColour.b;
	
	vec4 totalColour = backgroundTextureColour + aTextureColour + bTextureColour + cTextureColour;// + dTextureColour;
	
	return totalColour;
}

void main(){			
		
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);	
	
	out_Colour = diffuseLighting(unitNormal, unitLightVector) * textureColour() + specularLighting(unitNormal, unitLightVector);
	out_Colour = mix(vec4(skyColour, 1.0), out_Colour, visibility);
}