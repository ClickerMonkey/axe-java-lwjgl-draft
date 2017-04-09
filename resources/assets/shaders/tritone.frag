uniform sampler2D texUnit;
uniform vec3 low;
uniform vec3 mid;
uniform vec3 high;

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );
	float brightness = (c_in.r + c_in.g + c_in.b) / 3;

	if (brightness < 0.5) {
		gl_FragColor = vec4( mix(low, mid, brightness * 2), c_in.a );
	} else {
		gl_FragColor = vec4( mix(mid, high, (brightness - 0.5) * 2), c_in.a );
	}
}