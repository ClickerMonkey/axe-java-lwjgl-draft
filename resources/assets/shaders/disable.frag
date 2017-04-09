uniform sampler2D texUnit;
uniform float add = 1;
uniform float weight = 0.5;

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );
	
	c_in.r = (c_in.r + add) * weight;
	c_in.g = (c_in.g + add) * weight;
	c_in.b = (c_in.b + add) * weight;

	gl_FragColor = c_in;
}