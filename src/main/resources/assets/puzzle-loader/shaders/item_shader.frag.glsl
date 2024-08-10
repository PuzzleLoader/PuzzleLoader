#version 330

in vec2 v_texCoord0;
in vec3 worldPos;
in vec4 blocklight;

out vec4 outColor;
uniform sampler2D texDiffuse;
uniform vec3 skyAmbientColor;

void main()
{
    vec4 texColor = texture(texDiffuse, v_texCoord0);
    if(texColor.a == 0)
    {
        discard;
    }

    vec3 it =  pow(15*blocklight.rgb / 25.0, vec3(2));
    vec3 t = 30.0/(1.0 + exp(-15.0 * it)) - 15;
    vec3 lightTint = max(t/15, blocklight.a * skyAmbientColor);

//    lightTint = max(lightTint, vec3(0.1));

    outColor = vec4(texColor.rgb * lightTint.rgb, texColor.a);
//    outColor = vec4(blocklight.rgb, texColor.a);

}