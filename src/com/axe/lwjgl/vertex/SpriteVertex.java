package com.axe.lwjgl.vertex;

import java.nio.ByteBuffer;

import com.axe.color.Color;
import com.axe.color.Colors;
import com.axe.gfx.WritableVertex;

public class SpriteVertex implements WritableVertex 
{
	
	public static SpriteVertex INSTANCE = new SpriteVertex();
	public static Color DEFAULT_COLOR = Colors.White.mutable(); 

	public float x, y, s, t;
	public Color color = DEFAULT_COLOR;
	
	@Override
	public void write(ByteBuffer buffer) 
	{
		buffer.putFloat( x );
		buffer.putFloat( y );
		buffer.putInt( color.abgr() );
		buffer.putFloat( s );
		buffer.putFloat( t );
	}
	
	public void setColor(Color color)
	{
		this.color = color == null ? DEFAULT_COLOR : color;
	}
	
	public void setRotated(float x, float y, float originx, float originy, float cos, float sin)
	{
		this.x = x * cos - y * sin + originx; 
		this.y = x * sin + y * cos + originy;
	}

	@Override
	public String toString() 
	{
		return "SpriteVertex [x=" + x + ", y=" + y + ", s=" + s + ", t=" + t + ", color=" + color + "]";
	}

}
