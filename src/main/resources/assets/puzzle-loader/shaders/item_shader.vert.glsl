#version 330

//layout (location = 0) in vec2 aTexCoord;
in vec3 a_position;
attribute vec2 a_texCoord0;
in vec4 a_lighting;
uniform mat4 u_projViewTrans;
uniform mat4 u_modelMat;
out vec2 v_texCoord0;
out vec3 worldPos;
out vec4 blocklight;
void main()
{
	blocklight = a_lighting;
	v_texCoord0 = a_texCoord0;
	worldPos = a_position;
	gl_Position = (u_projViewTrans * u_modelMat * vec4(worldPos, 1.0));
}