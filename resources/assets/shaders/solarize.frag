uniform sampler2D texUnit;

void transfer(out float v);

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );

	transfer(c_in.r);
	transfer(c_in.g);
	transfer(c_in.b);

	gl_FragColor = c_in;
}

void transfer(out float v)
{
	v = (v > 0.5 ? 2 * (v - 0.5) : 2 * (0.5 - v) ); 
}