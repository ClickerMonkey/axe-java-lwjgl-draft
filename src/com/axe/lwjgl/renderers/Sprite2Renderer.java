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
import com.axe.lwjgl.vertex.SpriteVertex;
import com.axe.math.Numbers;
import com.axe.math.Vec2f;
import com.axe.render.Renderer;
import com.axe.tile.Tile;
import com.axe.util.Buffers;
import com.axe2d.sprite.Sprite2;

public class Sprite2Renderer implements Renderer<Sprite2> 
{
	
	public static final VertexFormat VERTEX_FORMAT = 
		new VertexFormatBuilder()
			.setVertex( 2, DataType.Float )	
			.setColor( 4, DataType.Ubyte )	
			.addTexture( 2, DataType.Float )
			.setPrimitive( Primitive.Triangle )
			.create();
	
	public byte[] INDEX = {0, 1, 2, 2, 3, 0};
	public VertexBuffer buffer;
	public VertexMesh mesh;
	public ByteBuffer indices;
	public int hash = 0;
	
	public boolean isActivated(Sprite2 sprite)
	{
		return buffer != null;
	}
	
	public void activate(Sprite2 sprite)
	{
		buffer = Axe.graphics.newVertexBufferFor( VertexMeshType.Static, VERTEX_FORMAT, 6, false );
		mesh = buffer.mesh();
		indices = Buffers.bytes( INDEX );
		hash = -1;
	}
	
	@Override
	public void begin(Sprite2 entity, GameState state, View view) 
	{
		final Tile tile = entity.tile;
		
		int code = entity.hashCode();
		
		if ( hash != code )
		{
			final SpriteVertex vertex = SpriteVertex.INSTANCE;
			final Vec2f pos = entity.position;
			final Vec2f size = entity.size;
			final Vec2f scale = entity.scale;
			final Vec2f anchor = entity.anchor;
			final float rotation = entity.rotation.v;
			final float w = size.x * scale.x;
			final float h = size.y * scale.y;
			final float l = anchor.x * w;
			final float r = (1 - anchor.x) * w;
			final float b = anchor.y * h;
			final float t = (1 - anchor.y) * h;
			
			vertex.setColor( entity.color );
			
			buffer.reset( mesh );
			
			if ( rotation == 0 )
			{
				vertex.x = pos.x - l;
				vertex.y = pos.y + t;
				vertex.s = tile.s0;
				vertex.t = tile.t0;
				buffer.write(vertex);
				
				vertex.x = pos.x - l;
				vertex.y = pos.y - b;
				vertex.s = tile.s0;
				vertex.t = tile.t1;
				buffer.write(vertex);
				
				vertex.x = pos.x + r;
				vertex.y = pos.y - b;
				vertex.s = tile.s1;
				vertex.t = tile.t1;
				buffer.write(vertex);
				
				vertex.x = pos.x + r;
				vertex.y = pos.y + t;
				vertex.s = tile.s1;
				vertex.t = tile.t0;
				buffer.write(vertex);
			}
			else
			{
				float sin = Numbers.sin( rotation );
				float cos = Numbers.cos( rotation );
				
				vertex.setRotated( -l, t, pos.x, pos.y, cos, sin );
				vertex.s = tile.s0;
				vertex.t = tile.t0;
				buffer.write(vertex);

				vertex.setRotated( -l, -b, pos.x, pos.y, cos, sin );
				vertex.s = tile.s0;
				vertex.t = tile.t1;
				buffer.write(vertex);
				
				vertex.setRotated( r, -b, pos.x, pos.y, cos, sin );
				vertex.s = tile.s1;
				vertex.t = tile.t1;
				buffer.write(vertex);

				vertex.setRotated( r, t, pos.x, pos.y, cos, sin );
				vertex.s = tile.s1;
				vertex.t = tile.t0;
				buffer.write(vertex);
			}
			
			buffer.save();
			hash = code;
		}
		
		tile.bind();
		
		mesh.draw( indices );
	}

}
