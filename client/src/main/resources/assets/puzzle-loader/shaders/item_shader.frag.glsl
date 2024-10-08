#version 150
#ifdef GL_ES
precision mediump float;
#endif

in vec2 v_texCoord0;
in vec3 v_normal;

uniform sampler2D texDiffuse;
uniform vec4 tintColor;
uniform int isInSlot;

out vec4 outColor;

void main()
{
    //bs numbers might want to mess around with
    float faceShade = abs(dot(vec3(0,0,1), v_normal) ) + 0.6;
    faceShade *= abs(dot(vec3(0,1,0), v_normal) + 0.8);
    faceShade *= 1.0;
    vec4 texColor = texture(texDiffuse, v_texCoord0);

    if(texColor.a == 0)
    {
        discard;
    }

    if (isInSlot == 1) {
        outColor = texColor;
        return;
    } else outColor = vec4(texColor.rgb * faceShade , texColor.a) * tintColor;
}