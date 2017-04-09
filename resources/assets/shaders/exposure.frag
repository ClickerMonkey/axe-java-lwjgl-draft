uniform sampler2D texUnit;
uniform float brightness = 1.0;
uniform float contrast = 1.0;

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );

	c_in.r = ((c_in.r * brightness) - 0.5) * contrast + 0.5;
	c_in.g = ((c_in.g * brightness) - 0.5) * contrast + 0.5;
	c_in.b = ((c_in.b * brightness) - 0.5) * contrast + 0.5;

	gl_FragColor = c_in;
}