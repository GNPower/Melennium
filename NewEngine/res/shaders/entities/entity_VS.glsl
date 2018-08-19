#version 430

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoords;
layout(location = 2) in vec3 normals;

out vec3 worldPosition;
out vec2 texCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;

uniform vec3 lightPosition;
uniform mat4 m_MVP;
uniform mat4 m_World;

void main(){
	
	texCoords = textureCoords;
	gl_Position = m_MVP * vec4(position, 1);
	worldPosition = (m_World * vec4(position, 1)).xyz;
	
	surfaceNormal = (m_World * vec4(normals, 0.0)).xyz;
	toLightVector = lightPosition - worldPosition;
}