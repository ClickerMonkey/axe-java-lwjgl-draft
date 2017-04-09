package com.axe.test;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.magnos.asset.Assets;

import com.axe.Axe;
import com.axe.View;
import com.axe.color.Colors;
import com.axe.game.Game;
import com.axe.game.GameEvent;
import com.axe.game.GameState;
import com.axe.gfx.Blend;
import com.axe.gfx.Mode;
import com.axe.gfx.Texture;
import com.axe.input.Key;
import com.axe.input.KeyState;
import com.axe.lwjgl.AxeLWJGL;
import com.axe.lwjgl.SettingsLWJGL;
import com.axe.lwjgl.gfx.GraphicsLWJGL;
import com.axe.math.Vec2f;
import com.axe.node.RenderedNode;
import com.axe.render.Renderer;
import com.axe.tile.Tile;
import com.axe.window.Window;
import com.axe2d.Camera2;
import com.axe2d.Scene2;

public class TestTexture 
{

	public static void main(String[] args)
	{
		SettingsLWJGL settings = new SettingsLWJGL( 2, 1 );
		
		AxeLWJGL.init(settings);
		
		Axe.renderers.register(TestNode.VIEW, new TestNodeRenderer());
		
		Debug.listen(Axe.monitors);
		
		Game game = Axe.games.create();
		
		Window window = game.newWindow();
		window.getPlacement().center(640, 480);
		window.setTitle("TestTexture");
		window.setBackground(Colors.CornflowerBlue);
		
		Debug.listen(game);
		Debug.listen(window);
		
		Scene2 scene = game.newScene(2);
		scene.setRoot(new TestNode());
		
		View<Vec2f> view = window.newView(scene);
		
		Camera2 camera = view.getCamera();
		camera.size.set(640, 480);
		camera.center.set(0, 0);
		camera.update();
		
		game.on( GameEvent.Start, g -> {
			Axe.logger.log( Axe.graphics.getGraphicsCard() );
		});
		
		game.run();
	}
	
	private static class TestNode extends RenderedNode<Vec2f>
	{
		public static final int VIEW = Axe.renderers.create();
		
		public KeyState esc;
		public Vec2f center = new Vec2f();
		public Vec2f size = new Vec2f(320, 240);
		public Texture texture;
		
		public TestNode()
		{
			super( VIEW );
			
			this.esc = Axe.keys.getKey( Key.ESCAPE );
			this.texture = Assets.load("assets/monkey.png");
			this.size.set( this.texture.getImageWidth(), this.texture.getImageHeight() );
		}
		
		@Override
		public void update(GameState state, View<Vec2f> view) 
		{
			if (esc.down)
			{
				view.getGame().stop();
			}
			
			texture.activate();
		}
	}
	
	private static class TestNodeRenderer implements Renderer<TestNode>
	{
		@Override
		public void begin(TestNode entity, GameState state, View view) 
		{
			GraphicsLWJGL gr = AxeLWJGL.graphics;
			Texture tex = entity.texture;
			float x = entity.center.x;
			float y = entity.center.y;
			float w = entity.size.x * 0.5f;
			float h = entity.size.y * 0.5f;
			
			gr.enable( Mode.Texture );
			gr.enable( Mode.Blend );
			gr.setBlend( Blend.Alpha );
			
			tex.bind();
			
			Tile tile = tex.tile();
			
			glBegin( GL_QUADS );
			
			glTexCoord2f(tile.s0, tile.t0);
			glVertex2f(x - w, y - h);
			glTexCoord2f(tile.s0, tile.t1);
			glVertex2f(x - w, y + h);
			glTexCoord2f(tile.s1, tile.t1);
			glVertex2f(x + w, y + h);
			glTexCoord2f(tile.s1, tile.t0);
			glVertex2f(x + w, y - h);
			
			glEnd();
			
			gr.disable( Mode.Texture );
		}	
	}
	
}
