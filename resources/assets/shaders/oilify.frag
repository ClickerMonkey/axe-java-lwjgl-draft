uniform sampler2D texUnit;
uniform vec2 pixel = vec2(0.001953125, 0.001953125);
uniform vec2 radius = vec2(0.0078125, 0.0078125);
uniform bool clamp = false;

const int LEVELS = 20;

void main(void)
{
	vec2 texCoord = gl_TexCoord[0].st;
	vec2 start = texCoord - radius;
	vec2 end = texCoord + radius;
	vec2 current = start;
	
	int max = 0;
	int count[LEVELS+1];
	vec4 sum[LEVELS+1];	
	
	for (int i = 0; i <= LEVELS; i++)
	{
		count[i] = 0;
		sum[i] = vec4(0.0);
	}
	
	while (current.y <= end.y) 
	{
		if (!clamp || (current.x >= 0.0 && current.x <= 1.0 && current.y >= 0.0 && current.y <= 1.0)) {
			float dx = abs(current.x - texCoord.x);
			float dy = abs(current.y - texCoord.y);

			if (dx <= radius.x && dy <= radius.y) 
			{
				vec4 color = texture2D( texUnit, current );
	
				if (color.a != 0.0)
				{
					int i = floor((color.r + color.g + color.b) / 3.0 * LEVELS);
					
					count[i]++;
					sum[i] += color;
					
					if (count[i] > count[max]) 
					{
						max = i;
					}
				}
			}
		}
	
		current.x += pixel.x;
		if (current.x >= end.x) 
		{
			current.x = start.x;
			current.y += pixel.y;
		}
	}

    gl_FragColor = sum[max] / count[max]; 
}