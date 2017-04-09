package com.axe.lwjgl.renderers;

import java.nio.ByteBuffer;

import com.axe.Axe;
import com.axe.View;
import com.axe.game.GameState;
import com.axe.gfx.DataType;
import com.axe.gfx.Primitive;
import com.axe.gfx.VertexBuffer;
import com.axe.gfx.VertexFormat;
import com.axe.gfx.VertexFormatBuilder;
import com.axe.gfx.VertexMesh;
import com.axe.gfx.VertexMeshType;
import com.axe.lwjgl.vertex.SimpleVertex;
import com.axe.math.Vec2f;
import com.axe.render.Renderer;
import com.axe.tile.Tile;
import com.axe.util.Buffers;
import com.axe2d.sprite.SimpleSprite2;

public class SimpleSprite2Renderer implements Renderer<SimpleSprite2> 
{
	
	public static final VertexFormat VERTEX_FORMAT = 
		new VertexFormatBuilder()
			.setVertex( 2, DataType.Float )		
			.addTexture( 2, DataType.Float )
			.setPrimitive( Primitive.Triangle )
			.create();
	
	public byte[] INDEX = {0, 1, 2, 2, 3, 0};
	public VertexBuffer buffer;
	public VertexMesh mesh;
	public ByteBuffer indices;
	public int hash = 0;
	
	public boolean isActivated(SimpleSprite2 sprite)
	{
		return buffer != null;
	}
	
	public void activate(SimpleSprite2 sprite)
	{
		buffer = Axe.graphics.newVertexBufferFor( VertexMeshType.Static, VERTEX_FORMAT, 6, false );
		mesh = buffer.mesh();
		indices = Buffers.bytes( INDEX );
		hash = -1;
	}
	
	@Override
	public void begin(SimpleSprite2 entity, GameState state, View view) 
	{
		final Tile tile = entity.tile;
		
		int code = entity.hashCode();
		
		if ( hash != code )
		{
			final SimpleVertex vertex = SimpleVertex.INSTANCE;
			final Vec2f pos = entity.position;
			final Vec2f size = entity.size;
			
			buffer.reset( mesh );
			
			vertex.x = pos.x;
			vertex.y = pos.y + size.y;
			vertex.s = tile.s0;
			vertex.t = tile.t0;
			buffer.write(vertex);
			
			vertex.x = pos.x;
			vertex.y = pos.y;
			vertex.s = tile.s0;
			vertex.t = tile.t1;
			buffer.write(vertex);
			
			vertex.x = pos.x + size.x;
			vertex.y = pos.y;
			vertex.s = tile.s1;
			vertex.t = tile.t1;
			buffer.write(vertex);
			
			vertex.x = pos.x + size.x;
			vertex.y = pos.y + size.y;
			vertex.s = tile.s1;
			vertex.t = tile.t0;
			buffer.write(vertex);
			
			buffer.save();
			hash = code;
		}
		
		tile.bind();
		// mesh.draw();
		
		mesh.draw( indices );
	}

}
