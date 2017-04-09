uniform sampler2D texUnit;
uniform vec3 into = vec3(0.0);
uniform vec3 mixed = vec3(0.0);

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );
	vec3 rgb = c_in.rgb;
	vec3 c_out = vec3(into * (mixed * rgb + (1 - mixed) * rgb) + (1 - into) * rgb);
	
	gl_FragColor = vec4( clamp(c_out, 0, 1), c_in.a );
}