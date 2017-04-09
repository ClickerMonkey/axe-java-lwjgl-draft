uniform sampler2D texUnit;
uniform float gain = 0.5;
uniform float bias = 0.5;

void transfer(out float v);

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );

	transfer( c_in.r );
	transfer( c_in.g );
	transfer( c_in.b );

	gl_FragColor = c_in;
}

void transfer(out float v)
{
	float c = (1.0 / gain - 2.0) * (1.0 - 2.0 * v);
	if (a < 0.5) {
		v = v / (c + 1.0);
	} else {
		v = (c - v) / (c - 1.0);
	}
	
	v = v / ((1.0 / bias - 2.0) * (1.0 - v) + 1);
}
