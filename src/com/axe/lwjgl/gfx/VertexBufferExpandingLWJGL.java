package com.axe.lwjgl.gfx;

import static org.lwjgl.opengl.GL15.*;

import java.nio.ByteBuffer;

import com.axe.gfx.AbstractVertexBuffer;
import com.axe.gfx.VertexMesh;
import com.axe.lwjgl.AxeLWJGL;
import com.axe.util.Array;
import com.axe.util.Buffers;

public class VertexBufferExpandingLWJGL extends AbstractVertexBuffer
{

	public final int chunkSize;
	public ByteBuffer data[];
	public int cursor;
	public int vertices;
	public int verticesPerBuffer;
	public int stride;
	
	public VertexBufferExpandingLWJGL(int chunkSize)
	{
		this.chunkSize = chunkSize; 
		this.data = new ByteBuffer[] { Buffers.bytes( chunkSize ) };
		this.cursor = 0;
	}
	
	protected void ensureIndex(int i)
	{
		while (data.length <= i)
		{
			data = Array.add( Buffers.bytes( chunkSize ), data );
		}
	}
	
	@Override
	public void reset(VertexMesh mesh)
	{
		super.reset( mesh );
		
		cursor = 0;
		stride = mesh.format().stride;
		vertices = 0;
		verticesPerBuffer = chunkSize / stride;
	}
	
	@Override
	public boolean load(VertexMesh vertexMesh, int offset, int count) 
	{
		final VertexMeshLWJGL mesh = (VertexMeshLWJGL)vertexMesh;
		
		if ( !mesh.bind() )
		{
			throw new IllegalArgumentException( "Unable to load an invalid mesh" );
		}
		
		reset( vertexMesh );
		
		int bufferIndex = 0;
		
		vertices = 0;
		
		while (count > 0)
		{
			ensureIndex( bufferIndex );
			
			ByteBuffer chunk = data[ bufferIndex ];
			int desired = Math.min( verticesPerBuffer, count );
			int desiredBytes = desired * stride;
			
			chunk.position( 0 ).limit( desiredBytes );
			
			glGetBufferSubData( GL_ARRAY_BUFFER, offset, chunk );
			
			chunk.clear();
			count -= desired;
			offset += desiredBytes;
			vertices += desired;
			bufferIndex++;
		}
		
		return (vertices == count);
	}
	
	@Override
	public void next()
	{
		int nextPosition = position + stride;
		
		if ( nextPosition > data[ cursor ].capacity() )
		{
			ensureIndex( ++cursor );
			nextPosition = 0;
		}
		
		vertices++;
		data[ cursor ].position( nextPosition );
	}
	
	@Override
	public void vertex(int index)
	{
		cursor = index / verticesPerBuffer;
		
		ensureIndex( cursor );
		
		position = index % verticesPerBuffer; 
		
		vertices = Math.max( vertices, index );
		data[ cursor ].position( position );
	}
	
	@Override
	public int capacity()
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public int vertices()
	{
		return vertices;
	}
	
	@Override
	public int remaining()
	{
		return Integer.MAX_VALUE - vertices;
	}

	@Override
	public ByteBuffer data() 
	{
		return data[ cursor ];
	}

	@Override
	public boolean save(int offset) 
	{
		final VertexMeshLWJGL mesh = (VertexMeshLWJGL)mesh();
		final int total = offset + vertices;
		final boolean canSave = vertices > 0;
		
		if (canSave)
		{
			mesh.create();
			mesh.bind();
			
			if (mesh.updates == 0 || total > mesh.vertices)
			{
				// TODO if VBO already exists and offset > 0 - we need to copy contents to a new buffer before updating it.
				
				glBufferData( GL_ARRAY_BUFFER, vertices * stride, AxeLWJGL.graphics.vertexTypes.get( mesh.type ) );
				
				mesh.vertices = total;
			}
			
			int chunkIndex = 0;
			
			while (vertices > 0)
			{
				ByteBuffer chunk = data[ chunkIndex++ ];
				int count = Math.min( vertices, verticesPerBuffer );
				int countBytes = count * stride;
				
				chunk.position( 0 ).limit( countBytes );
				
				glBufferSubData( GL_ARRAY_BUFFER, offset, chunk );
				
				chunk.clear();
				
				offset += countBytes;
				vertices -= count;
			}
			
			mesh.updates++;
			
			cursor = 0;
			position = 0;
		}
		
		return canSave;
	}

}
