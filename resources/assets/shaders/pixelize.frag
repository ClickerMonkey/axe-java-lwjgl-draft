uniform sampler2D texUnit;
uniform vec2 pixel;

void main()
{
	vec2 texCoord = gl_TexCoord[0].st;
	
    gl_FragColor = texture2D( texUnit, texCoord - mod( texCoord, pixel ) + (pixel * 0.5) );
}