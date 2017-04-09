uniform sampler2D texUnit;
uniform int levels;

float transfer(float v)
{
	return floor(v * levels) / (levels - 1);
}

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );

	c_in.r = transfer(c_in.r);
	c_in.g = transfer(c_in.g);
	c_in.b = transfer(c_in.b);

	gl_FragColor = c_in;
}