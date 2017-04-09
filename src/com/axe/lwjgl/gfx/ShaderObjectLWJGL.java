package com.axe.lwjgl.gfx;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.magnos.asset.AssetInfo;

import com.axe.Axe;
import com.axe.gfx.ShaderObject;
import com.axe.gfx.ShaderObjectType;
import com.axe.lwjgl.AxeLWJGL;

public class ShaderObjectLWJGL implements ShaderObject
{
	
	public int id = 0;
	public final AssetInfo info;
	public final CharSequence code;
	public final ShaderObjectType type;
	
	public ShaderObjectLWJGL( AssetInfo info, ShaderObjectType type, CharSequence code )
	{
		this.info = info;
		this.type = type;
		this.code = code;
	}
	
	@Override
	public CharSequence getCode()
	{
		return code;
	}
	
	@Override
	public ShaderObjectType getType()
	{
		return type;
	}
	
	@Override
	public AssetInfo getInfo()
	{
		return info;
	}
	
	@Override
	public boolean isActivated()
	{
		return (id != 0);
	}

	@Override
	public void activate()
	{
		if (id == 0)
		{
			id = GL20.glCreateShader( AxeLWJGL.graphics.shaderTypes.get( type ) );
			GL20.glShaderSource( id, code );
			GL20.glCompileShader( id );

			if (GL20.glGetShaderi( id, GL20.GL_COMPILE_STATUS ) == GL11.GL_FALSE) 
			{
				Axe.logger.log( GL20.glGetShaderInfoLog(id) + "\n" + code );
				
				ShaderLWJGL.log( id );
				id = 0;
			}
		}
	}

	@Override
	public void destroy() 
	{
		if (id != 0)
		{
			GL20.glDeleteShader( id );
			
			id = 0;
		}
	}

}
