package com.axe.lwjgl.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.axe.gfx.DataType;
import com.axe.gfx.VertexFormat;
import com.axe.gfx.VertexMesh;
import com.axe.gfx.VertexMeshType;
import com.axe.lwjgl.AxeLWJGL;
import com.axe.util.EnumIntMap;

public class VertexMeshLWJGL implements VertexMesh 
{

	public final VertexMeshType type;
	public final VertexFormat format;
	public int vertices;
	public int id;
	public int updates;
	
	protected int primitive;
	
	public VertexMeshLWJGL(VertexMeshType type, VertexFormat format) 
	{
		this.type = type;
		this.format = format;
	}
	
	public boolean bind()
	{
		boolean bindable = isValid();
		
		if (bindable)
		{
			glBindBuffer( GL_ARRAY_BUFFER, id );
		}
		
		return bindable;
	}
	
	public boolean create()
	{
		boolean ready = id == 0;
		
		if (ready)
		{
			id = glGenBuffers();
			primitive = AxeLWJGL.graphics.primitives.get( format.primitive );
		}
		
		return ready;
	}
	
	@Override
	public VertexFormat format() 
	{
		return format;
	}

	@Override
	public VertexMeshType type() 
	{
		return type;
	}

	@Override
	public int vertices() 
	{
		return vertices;
	}

	@Override
	public int size() 
	{
		return vertices * format.stride;
	}

	@Override
	public void destroy() 
	{
		if (id != 0)
		{
			glDeleteBuffers( id );
			id = 0;
		}
	}

	@Override
	public boolean isValid() 
	{
		return id != 0;
	}

	@Override
	public void draw(int offset, int vertices) 
	{
		if (bind())
		{
			startDraw();
			glDrawArrays( primitive, offset, vertices );
			endDraw();
		}
	}

	@Override
	public void draw(ShortBuffer indices) 
	{
		if (bind())
		{
			startDraw();
			glDrawElements( primitive, indices );
			endDraw();
		}
	}

	@Override
	public void draw(IntBuffer indices) 
	{		
		if (bind())
		{
			startDraw();
			glDrawElements( primitive, indices );
			endDraw();
		}
	}

	@Override
	public void draw(ByteBuffer indices) 
	{
		final EnumIntMap<DataType> types = AxeLWJGL.graphics.dataTypes;
		
		if (bind())
		{
			startDraw();
			glDrawElements( primitive, types.get( DataType.Ubyte ), indices );
			endDraw();
		}
	}

	@Override
	public void draw(ByteBuffer indices, DataType indexType) 
	{
		final EnumIntMap<DataType> types = AxeLWJGL.graphics.dataTypes;
		
		if (bind())
		{
			startDraw();
			glDrawElements( primitive, types.get( indexType ), indices );
			endDraw();
		}
	}
	
	public void startDraw()
	{
		final EnumIntMap<DataType> types = AxeLWJGL.graphics.dataTypes;
		
		glEnableClientState( GL_VERTEX_ARRAY );
		glVertexPointer( format.vertexDimension, types.get( format.vertexType ), format.stride, format.vertexOffset );
		
		if ( format.normalDimension != 0 ) 
		{
			glEnableClientState( GL_NORMAL_ARRAY );
			glNormalPointer( types.get( format.normalType ), format.stride, format.normalOffset );
		}
		
		if ( format.colorComponents != 0 ) 
		{
			glEnableClientState( GL_COLOR_ARRAY );
			glColorPointer( format.colorComponents, types.get( format.colorType ), format.stride, format.colorOffset );
		}
		
		if ( format.secondaryColorComponents != 0 )
		{
			glEnableClientState( GL_SECONDARY_COLOR_ARRAY );
			glSecondaryColorPointer( format.secondaryColorComponents, types.get( format.secondaryColorType ), format.stride, format.secondaryColorOffset );
		}
		
		for ( int i = 0; i < format.textures; i++ ) 
		{
			glClientActiveTexture( GL_TEXTURE0 + i );
			glEnableClientState( GL_TEXTURE_COORD_ARRAY );
			glTexCoordPointer( format.textureDimension[i], types.get( format.textureType[i] ), format.stride, format.textureOffsets[i] );
		}
		
		for (int i = 0; i < format.attributes; i++)
		{
			glEnableVertexAttribArray( format.attributeIndex[i] );
			glVertexAttribPointer( format.attributeIndex[i], format.attributeSize[i], types.get( format.attributeType[i] ), format.attributeNormalize[i], format.stride, format.attributeOffset[i] );
		}
	}
	
	public void endDraw()
	{
		glDisableClientState( GL_VERTEX_ARRAY );
		
		if ( format.normalDimension != 0 ) 
		{
			glDisableClientState( GL_NORMAL_ARRAY );
		}
		
		if ( format.colorComponents != 0 ) 
		{
			glDisableClientState( GL_COLOR_ARRAY );
		}
		
		if ( format.secondaryColorComponents != 0 )
		{
			glDisableClientState( GL_SECONDARY_COLOR_ARRAY );
		}
		
		for ( int i = 0; i < format.textures; i++ ) 
		{
			glClientActiveTexture( GL_TEXTURE0 + i );
			glDisableClientState( GL_TEXTURE_COORD_ARRAY );
		}
		
		for ( int i = 0; i < format.attributes; i++ )
		{
			glDisableVertexAttribArray( format.attributeIndex[i] );
		}
	}
	
}
