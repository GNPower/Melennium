#version 430

layout (triangles) in;
layout (line_strip, max_vertices = 4) out;

void main(){
	
	for(int i = 0; i < gl_in.length(); i++){
		
		vec4 worldPos = gl_in[i].gl_Position;
		gl_Position = worldPos;
		EmitVertex();
	}
/*	
	vec4 worldPos = gl_in[0].gl_Position;
	gl_Position = worldPos;
	EmitVertex();
	*/
	EndPrimitive();
}