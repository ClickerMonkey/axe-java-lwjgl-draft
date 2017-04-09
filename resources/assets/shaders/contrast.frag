uniform sampler2D texUnit;
uniform float exposure = 1.0;

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );

	c_in.r = 1.0 - exp(-c_in.r * exposure);
	c_in.g = 1.0 - exp(-c_in.g * exposure);
	c_in.b = 1.0 - exp(-c_in.b * exposure);

	gl_FragColor = c_in;
}