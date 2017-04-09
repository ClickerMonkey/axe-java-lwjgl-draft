package com.axe.lwjgl.gfx;

import static org.lwjgl.opengl.GL15.*;

import java.nio.ByteBuffer;

import com.axe.gfx.AbstractVertexBuffer;
import com.axe.gfx.VertexMesh;
import com.axe.lwjgl.AxeLWJGL;
import com.axe.util.Buffers;

public class VertexBufferLWJGL extends AbstractVertexBuffer
{

	public ByteBuffer data;
	
	public VertexBufferLWJGL(int bufferSize)
	{
		this.data = Buffers.bytes( bufferSize );
	}
	
	@Override
	public boolean load(VertexMesh vertexMesh, int offset, int count) 
	{
		VertexMeshLWJGL mesh = (VertexMeshLWJGL)vertexMesh;
		
		if ( !mesh.bind() )
		{
			throw new IllegalArgumentException( "Unable to load an invalid mesh" );
		}
		
		reset( vertexMesh );
		
		final int stride = mesh.format.stride;
		final int byteOffset = offset * stride;
		final int byteCount = count * stride;
		final int byteLimit = Math.min( byteCount, data.capacity() );
		
		data.limit( byteLimit );
		
		glGetBufferSubData( GL_ARRAY_BUFFER, byteOffset, data );
		
		return (byteCount == byteLimit);
	}

	@Override
	public ByteBuffer data() 
	{
		return data;
	}

	@Override
	public boolean save(int offset) 
	{
		final VertexMeshLWJGL mesh = (VertexMeshLWJGL)mesh();
		final int stride = mesh.format.stride;
		final int vertices = this.vertices();
		final int total = offset + vertices;
		final boolean canSave = vertices > 0;
		
		if (canSave)
		{
			mesh.create();
			mesh.bind();
			
			data.position( 0 ).limit( vertices * stride );
			
			if (mesh.updates == 0 || total > mesh.vertices)
			{
				// TODO if VBO already exists and offset > 0 - we need to copy contents to a new buffer before updating it.
				
				glBufferData( GL_ARRAY_BUFFER, data, AxeLWJGL.graphics.vertexTypes.get( mesh.type ) );
				
				mesh.vertices = total;
			}
			else
			{
				glBufferSubData( GL_ARRAY_BUFFER, offset * stride, data );
			}
			
			mesh.updates++;
			
			data.clear();
			position = 0;
		}
		
		return false;
	}

}
