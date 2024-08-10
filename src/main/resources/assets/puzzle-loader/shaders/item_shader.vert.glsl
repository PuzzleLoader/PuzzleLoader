#version 150

in vec3 a_position;
in vec2 a_texCoord0;

out vec2 v_texCoord0;

uniform mat4 u_projViewTrans;
uniform mat4 u_modelMat;

void main()
{
	v_texCoord0 = a_texCoord0;
	gl_Position = u_projViewTrans * u_modelMat * vec4(a_position, 1.0);
}
