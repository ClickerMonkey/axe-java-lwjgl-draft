uniform sampler2D texUnit;
uniform float amount;

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );
	float gray = dot( c_in.rgb, vec3( 0.299, 0.587, 0.114 ) );
	vec4 c_out = vec4( gray, gray, gray, c_in.a );
	
    gl_FragColor = mix(c_in, c_out, amount);
}