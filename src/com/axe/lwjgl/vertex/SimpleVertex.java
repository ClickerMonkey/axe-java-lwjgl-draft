package com.axe.lwjgl.vertex;

import java.nio.ByteBuffer;

import com.axe.gfx.WritableVertex;

public class SimpleVertex implements WritableVertex 
{
	
	public static SimpleVertex INSTANCE = new SimpleVertex();

	public float x, y, s, t;
	
	@Override
	public void write(ByteBuffer buffer) 
	{
		buffer.putFloat( x );
		buffer.putFloat( y );
		buffer.putFloat( s );
		buffer.putFloat( t );
	}

	@Override
	public String toString() 
	{
		return "SimpleVertex [x=" + x + ", y=" + y + ", s=" + s + ", t=" + t + "]";
	}

}
