package com.axe.lwjgl.gfx;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.axe.Axe;
import com.axe.color.Color;
import com.axe.gfx.Shader;
import com.axe.gfx.ShaderObject;
import com.axe.math.Scalarf;
import com.axe.math.Scalari;
import com.axe.math.Vec2f;
import com.axe.math.Vec2i;
import com.axe.math.Vec3f;
import com.axe.math.Vec3i;
import com.axe.math.Vec4f;
import com.axe.util.Buffers;

public class ShaderLWJGL implements Shader
{
	
	private static FloatBuffer bufferf = Buffers.floats( 64 );
	private static IntBuffer bufferi = Buffers.ints( 64 );
	
	public int id = 0;
	private Class<Enum<?>> variableClass;
	private int[] locations;
	private ShaderObjectLWJGL[] objects;

	public ShaderLWJGL(ShaderObject ... shaderObjects)
	{
		this. objects = validateObjects( shaderObjects );
	}
	
	public ShaderLWJGL(Class<Enum<?>> variableClass, ShaderObject ... shaderObjects)
	{
		this( shaderObjects );
		
		this.variableClass = variableClass;
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
			int activated = 0;
			
			for (ShaderObjectLWJGL so : objects)
			{				
				if (!so.isActivated())
				{
					so.activate();
				}
				
				if (!so.isActivated())
				{
					Axe.logger.log( "Shader Object wasn't successfully activated: " + so.getInfo().getPath() );
				}
				else
				{
					activated++;
				}
			}
			
			if (activated == objects.length)
			{
				id = GL20.glCreateProgram();

				for (ShaderObjectLWJGL so : objects)
				{
					GL20.glAttachShader( id, so.id );
				}

				if (ARBShaderObjects.glGetObjectParameteriARB( id, ARBShaderObjects.GL_OBJECT_ATTACHED_OBJECTS_ARB ) == GL11.GL_FALSE) {
					log( id );
					id = 0;
					return;
				}
				
				GL20.glLinkProgram( id );

				if (ARBShaderObjects.glGetObjectParameteriARB( id, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB ) == GL11.GL_FALSE) {
					log( id );
					id = 0;
					return;
				}

				GL20.glValidateProgram( id );

				if (ARBShaderObjects.glGetObjectParameteriARB( id, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB ) == GL11.GL_FALSE) {
					log( id );
					id = 0;
					return;
				}

				GL20.glUseProgram( id );

				locations = getLocations( variableClass, id );
				
				GL20.glUseProgram( 0 );
			}
		}
	}
	
	@Override
	public void destroy()
	{
		
	}

	private ShaderObjectLWJGL[] validateObjects( ShaderObject[] objects )
	{
		ShaderObjectLWJGL[] out = new ShaderObjectLWJGL[ objects.length ];
		
		for (int i = 0; i < objects.length; i++)
		{			
			out[ i ] = (ShaderObjectLWJGL)objects[ i ];
		}
		
		return out;
	}

	private int[] getLocations(Class<Enum<?>> variableClass, int program)
	{
		Enum<?>[] constants = variableClass.getEnumConstants();

		int[] locations = new int[ constants.length ];

		for (Enum<?> var : constants) 
		{
			locations[var.ordinal()] = GL20.glGetUniformLocation( program, var.name() ); 
		}

		return locations;
	}
	
	public ShaderObjectLWJGL[] getObjects()
	{
		return objects;
	}

	public void bind()
	{
		GL20.glUseProgram( id );
	}

	
	public void unbind()
	{
		GL20.glUseProgram( 0 );
	}
	
	public void set( int location, boolean x )
	{
		GL20.glUniform1i( location, x ? GL11.GL_TRUE : GL11.GL_FALSE );
	}
	
	public void set( int location, float x )
	{
		GL20.glUniform1f( location, x );
	}

	public void set( int location, float[] x )
	{
		readyFloatBuffer( x.length );
		bufferf.put( x );
		bufferf.flip();

		GL20.glUniform1fv( location, bufferf );
	}

	public void set( int location, Scalarf v )
	{
		GL20.glUniform1f( location, v.v );
	}

	public void set( int location, float x, float y )
	{
		GL20.glUniform2f( location, x, y );
	}

	public void set( int location, Vec2f v )
	{
		GL20.glUniform2f( location, v.x, v.y );
	}

	public void set( int location, Vec2f[] v )
	{
		readyFloatBuffer( v.length * 2 );
		for (int i = 0; i < v.length; i++) {
			bufferf.put( v[i].x );
			bufferf.put( v[i].y );
		}
		bufferf.flip();

		GL20.glUniform2fv( location, bufferf );
	}

	public void set( int location, float x, float y, float z )
	{
		GL20.glUniform3f( location, x, y, z );
	}

	public void set( int location, Vec3f v )
	{
		GL20.glUniform3f( location, v.x, v.y, v.z );
	}

	public void set( int location, Vec3f[] v )
	{
		readyFloatBuffer( v.length * 3 );
		for (int i = 0; i < v.length; i++) {
			bufferf.put( v[i].x );
			bufferf.put( v[i].y );
			bufferf.put( v[i].z );
		}
		bufferf.flip();

		GL20.glUniform3fv( location, bufferf );
	}

	public void set( int location, float x, float y, float z, float w )
	{
		GL20.glUniform4f( location, x, y, z, w );
	}

	public void set( int location, Vec4f v )
	{
		GL20.glUniform4f( location, v.x, v.y, v.z, v.w );
	}

	public void set( int location, Color color )
	{
		GL20.glUniform4f( location, color.r, color.g, color.b, color.a );
	}

	public void set( int location, Vec4f[] v )
	{
		readyFloatBuffer( v.length * 4 );
		for (int i = 0; i < v.length; i++) {
			bufferf.put( v[i].x );
			bufferf.put( v[i].y );
			bufferf.put( v[i].z );
			bufferf.put( v[i].w );
		}
		bufferf.flip();

		GL20.glUniform4fv( location, bufferf );
	}

	public void set( int location, Color[] v )
	{
		readyFloatBuffer( v.length * 4 );
		for (int i = 0; i < v.length; i++) {
			bufferf.put( v[i].r );
			bufferf.put( v[i].g );
			bufferf.put( v[i].b );
			bufferf.put( v[i].a );
		}
		bufferf.flip();

		GL20.glUniform4fv( location, bufferf );
	}

	public void set( int location, int x )
	{
		GL20.glUniform1i( location, x );
	}

	public void set( int location, int[] x )
	{
		readyIntBuffer( x.length );
		bufferi.put( x );
		bufferi.flip();

		GL20.glUniform1iv( location, bufferi );
	}

	public void set( int location, Scalari v )
	{
		GL20.glUniform1i( location, v.v );
	}

	public void set( int location, int x, int y )
	{
		GL20.glUniform2i( location, x, y );
	}

	public void set( int location, Vec2i v )
	{
		GL20.glUniform2i( location, v.x, v.y );
	}

	public void set( int location, Vec2i[] v )
	{
		readyIntBuffer( v.length * 2 );
		for (int i = 0; i < v.length; i++) {
			bufferi.put( v[i].x );
			bufferi.put( v[i].y );
		}
		bufferi.flip();

		GL20.glUniform2iv( location, bufferi );
	}

	public void set( int location, int x, int y, int z )
	{
		GL20.glUniform3i( location, x, y, z );
	}

	public void set( int location, Vec3i v )
	{
		GL20.glUniform3i( location, v.x, v.y, v.z );
	}

	public void set( int location, Vec3i[] v )
	{
		readyIntBuffer( v.length * 3 );
		for (int i = 0; i < v.length; i++) {
			bufferi.put( v[i].x );
			bufferi.put( v[i].y );
			bufferi.put( v[i].z );
		}
		bufferi.flip();

		GL20.glUniform3iv( location, bufferi );
	}

	public void set( int location, int x, int y, int z, int w )
	{
		GL20.glUniform4i( location, x, y, z, w );
	}

	private void readyIntBuffer(int length ) 
	{
		if (bufferi.capacity() < length) 
		{
			bufferi = Buffers.ints( length );
		}

		bufferi.clear();
	}

	private void readyFloatBuffer(int length ) 
	{
		if (bufferf.capacity() < length) 
		{
			bufferf = Buffers.floats( length );
		}

		bufferf.clear();
	}

	public int getLocation(Enum<?> enumConstant)
	{
		int location = locations[enumConstant.ordinal()]; 

		if (location == -1)
		{
			location = locations[enumConstant.ordinal()] = GL20.glGetUniformLocation( id, enumConstant.name() );
			
			if (location == -1)
			{
				Axe.logger.log("Variable does not exist in the Shader: " + enumConstant );
				
				throw new RuntimeException( "Variable " + enumConstant + " does not exist in the Shader" );	
			}
		}

		return location;
	}
	
	public int getLocation( String name )
	{
		int location = GL20.glGetUniformLocation( id, name ); 
		
		if (location == -1)
		{
			Axe.logger.log( "Variable does not exist in the Shader: " + name );
			
			throw new RuntimeException( "Variable " + name + " does not exist in the Shader" );	
		}
		
		return location;
	}
	
	public int getAttributeLocation( String name )
	{
		return GL20.glGetAttribLocation( id, name );
	}
	
	public <E extends Enum<E>> int getAttributeLocation( E enumConstant )
	{
		return GL20.glGetAttribLocation( id, enumConstant.name() );
	}

	public static boolean log(int id)
	{
		IntBuffer iVal = Buffers.ints(1);
		
		ARBShaderObjects.glGetObjectParameterivARB( id, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal );

		int length = iVal.get();
		
		if (length > 1) 
		{
			ByteBuffer infoLog = Buffers.bytes(length);
			iVal.flip();
			
			ARBShaderObjects.glGetInfoLogARB(id, iVal, infoLog);
			
			byte[] infoBytes = new byte[length];
			infoLog.get(infoBytes);
			
			Axe.logger.log( "Info log:" + new String(infoBytes) );
		}
		else 
		{
			return true;
		}
		
		return false;
	}

	public static boolean isAvailable()
	{
		return GL.getCapabilities().OpenGL20;
	}

}
