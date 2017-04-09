uniform sampler2D texUnit;
uniform ivec4 mask;

float maskComponent(int m, float v)
{
	return (int(v * 255.0 + 0.5) & m) / 255.0;
}

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );

	c_in.r = maskComponent(mask.r, c_in.r);
	c_in.g = maskComponent(mask.g, c_in.g);
	c_in.b = maskComponent(mask.b, c_in.b);
	c_in.a = maskComponent(mask.a, c_in.a);

	gl_FragColor = c_in;
}

