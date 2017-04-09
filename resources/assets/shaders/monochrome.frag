uniform sampler2D texUnit;
uniform float amount;

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );
	float rgb = step(1.5, c_in.r + c_in.g + c_in.b);
	vec4 c_out = vec4( rgb, rgb, rgb, c_in.a );
	
    gl_FragColor = mix(c_in, c_out, amount);
}