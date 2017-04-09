package com.axe.test;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.axe.Axe;
import com.axe.View;
import com.axe.color.ColorInterface;
import com.axe.color.Colors;
import com.axe.game.Game;
import com.axe.game.GameState;
import com.axe.input.Key;
import com.axe.input.KeyState;
import com.axe.lwjgl.AxeLWJGL;
import com.axe.lwjgl.SettingsLWJGL;
import com.axe.math.Vec2f;
import com.axe.node.RenderedNode;
import com.axe.render.Renderer;
import com.axe.window.Window;
import com.axe2d.Camera2;
import com.axe2d.Scene2;
import com.axe2d.View2;

public class TestDoubleWindow 
{

	public static void main(String[] args)
	{
		SettingsLWJGL settings = new SettingsLWJGL();
		
		AxeLWJGL.init(settings);
		
		Axe.renderers.register(TestNode.VIEW, new TestNodeRenderer());
		
		Debug.listen(Axe.monitors);
		
		Game game = Axe.games.create();
		
		Window window1 = game.newWindow();
		window1.getPlacement().attach(0.0f, 0.5f, 640f, 480f);
		window1.setTitle("Left");
		
		Window window2 = game.newWindow();
		window2.getPlacement().attach(1.0f, 0.5f, 640f, 480f);
		window2.setTitle("Right");
		
		Debug.listen(game);
		Debug.listen(window1);
		Debug.listen(window2);
		
		Scene2 scene = game.newScene(2);
		scene.setRoot(new TestNode());
		
		View2 view1 = window1.newView(scene);
		Camera2 camera1 = view1.getCamera();
		camera1.size.set(640, 480);
		camera1.center.set(0, 0);
		camera1.update();
		
		View2 view2 = window2.newView(scene);
		Camera2 camera2 = view2.getCamera();
		camera2.size.set(640, 480);
		camera2.center.set(0, 0);
		camera2.roll.v = 0.4f;
		camera2.update();
		
		game.run();
	}
	
	private static class TestNode extends RenderedNode<Vec2f>
	{
		public static final int VIEW = Axe.renderers.create();
		
		public KeyState esc;
		public Vec2f center = new Vec2f();
		public Vec2f size = new Vec2f(320, 240);
		
		public TestNode()
		{
			super( VIEW );
			this.esc = Axe.keys.getKey( Key.ESCAPE );
		}
		
		@Override
		public void update(GameState state, View<Vec2f> view) 
		{
			if (esc.down)
			{
				view.getGame().stop();
			}
		}
	}

	private static class TestNodeRenderer implements Renderer<TestNode>
	{
		@Override
		public void begin(TestNode entity, GameState state, View view) 
		{
			float cx = entity.center.x;
			float cy = entity.center.y;
			float hx = entity.size.x * 0.5f;
			float hy = entity.size.y * 0.5f;
			
			ColorInterface color = view.getWindow().getTitle().equals( "Left" ) ? Colors.Blue : Colors.Magenta;
			
			glBegin( GL_QUADS );
			glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getGreen());
			glVertex2f(cx-hx, cy-hy);
			glVertex2f(cx+hx, cy-hy);
			glVertex2f(cx+hx, cy+hy);
			glVertex2f(cx-hx, cy+hy);
			glEnd();
		}	
	}
	
}
