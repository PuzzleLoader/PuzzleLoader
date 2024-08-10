#version 150
#ifdef GL_ES
precision mediump float;
#endif

in vec2 v_texCoord0;

uniform sampler2D texDiffuse;
uniform vec4 tintColor;

out vec4 outColor;

void main()
{
    vec4 texColor = texture(texDiffuse, v_texCoord0);

    if(texColor.a == 0)
    {
        discard;
    }

    outColor = texColor * tintColor;
}