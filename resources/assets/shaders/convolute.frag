#version 120

uniform sampler2D texUnit;
uniform float[9] conMatrix;
uniform float conWeight;
uniform vec2 conPixel;

void main(void)
{
	vec4 color = vec4(0.0);
	vec2 texCoord = gl_TexCoord[0].st;
	vec2 offset = conPixel * 1.5;
	vec2 start = texCoord - offset;
	vec2 current = start;
	
	for (int i = 0; i < 9; i++)
	{
		color += texture2D( texUnit, current ) * conMatrix[i]; 
		
		current.x += conPixel.x;
		if (i == 2 || i == 5) {
			current.x = start.x;
			current.y += conPixel.y; 
		}
	}
	
    gl_FragColor = color * conWeight;
}