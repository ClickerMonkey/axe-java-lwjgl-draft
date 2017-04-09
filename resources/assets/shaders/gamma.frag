uniform sampler2D texUnit;
uniform vec4 gamma = vec4(1.0, 1.0, 1.0, 1.0);

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );

	gl_FragColor = pow( c_in, 1.0 / gamma );
}