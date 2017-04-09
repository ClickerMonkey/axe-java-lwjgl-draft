uniform sampler2D texUnit;
uniform float period;
uniform float amplitude;
uniform float shift;
uniform int orientation;		//0=horizontal, 1=vertical

void main(void)
{
	vec2 texCoord = gl_TexCoord[0].st;
	float x = 6.28318531 / period + shift;
	
	if (orientation == 0) {
		texCoord.x -= cos( mod( texCoord.y, period ) * x ) * amplitude;
	} else {
		texCoord.y -= cos( mod( texCoord.x, period ) * x ) * amplitude;
	}
	
    gl_FragColor = texture2D( texUnit, texCoord );
}