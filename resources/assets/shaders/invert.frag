uniform sampler2D texUnit;
uniform float amount;

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );
	vec4 c_out = vec4( 1 - c_in.rgb, c_in.a );
	
    gl_FragColor = mix(c_in, c_out, amount);
}