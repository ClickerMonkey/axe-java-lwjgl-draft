uniform sampler2D texUnit;
uniform float hue;						// 0.0
uniform float saturation;				// 0.0
uniform float brightness;				// 0.0

vec3 RGBtoHSB( vec3 color )
{
	float r = color.r;
	float g = color.g;
	float b = color.b;
	float cmax = max(r, max(g, b));
	float cmin = min(r, min(g, b));
	float cgap = cmax - cmin;
	float brightness = cmax;
	float saturation = (cmax == 0.0 ? 0.0 : cgap / cmax);
	float hue = 0.0;
	
	if (cmax != 0.0) 
	{
		float rc = (cmax - r) / cgap;
		float gc = (cmax - g) / cgap;
		float bc = (cmax - b) / cgap;
		
		if (r == cmax) {
			hue = bc - gc;
		} else if (g == cmax) {
			hue = 2.0 + rc - bc;
		} else {
			hue = 4.0 + gc - rc;
		}
		hue = hue / 6.0;
		if (hue < 0.0) {
			hue += 1.0;
		}
	}
	
	return vec3(hue, saturation, brightness);
}

vec3 HSBtoRGB( vec3 color )
{
	if (color.g != 0.0)
	{
		float h = (color.r - floor(color.r)) * 6.0;
		float f = h - floor(h);
		float p = color.b * (1.0 - color.g);
		float q = color.b * (1.0 - color.g * f);
		float t = color.b * (1.0 - color.g * (1.0 - f));
		
		switch (int(h)) 
		{
		case 0: return vec3(color.b, t, p);
		case 1: return vec3(q, color.b, p);
		case 2: return vec3(p, color.b, t);
		case 3: return vec3(p, q, color.b);
		case 4: return vec3(t, p, color.b);
		case 5: return vec3(color.b, p, q);
		}
	}
	return vec3(color.b);
}

void main(void)
{
	vec4 c_in = texture2D( texUnit, gl_TexCoord[0].st );
	
	vec3 c_out = RGBtoHSB( c_in.rgb );
	c_out.x = mod( c_out.x + hue, 1 );
	c_out.y = clamp( c_out.y + saturation, 0, 1 );
	c_out.z = clamp( c_out.z + brightness, 0, 1 );
	c_out = HSBtoRGB( c_out );
	
	gl_FragColor = vec4(c_out, c_in.a);
}