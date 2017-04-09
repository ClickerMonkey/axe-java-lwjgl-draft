uniform sampler2D texUnit;
uniform float amount;

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );

	gl_FragColor = vec4( c_in.rgb * amount, c_in.a );
}