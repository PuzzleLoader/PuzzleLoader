#version 150

in vec3 a_position;
in vec2 a_texCoord0;
in vec3 a_normal;
out vec2 v_texCoord0;

uniform mat4 u_projViewTrans;
uniform mat4 u_modelMat;

out vec3 v_normal;

void main()
{

	v_normal = normalize(a_normal); // Mult by u_modelMat for world space vectors
	v_texCoord0 = a_texCoord0;
	gl_Position = u_projViewTrans * u_modelMat * vec4(a_position, 1.0);
}
