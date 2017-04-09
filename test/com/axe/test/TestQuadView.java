package com.axe.test;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.axe.Axe;
import com.axe.View;
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

public class TestQuadView 
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
		window.setTitle("TestCentered");
		window.setBackground(Colors.CornflowerBlue);
		
		Debug.listen(game);
		Debug.listen(window);
		
		Scene2 scene = game.newScene(2);
		scene.setRoot(new TestNode());
		
		View<Vec2f> view1 = window.newView(scene);
		view1.getPlacement().attach(0, 1, 320, 240);
		view1.setData("Top Left");
		Camera2 camera1 = view1.getCamera();
		camera1.size.set(320, 240);
		camera1.update();
		
		View<Vec2f> view2 = window.newView(scene);
		view2.getPlacement().attach(1, 1, 320, 240);
		view2.setData("Top Right");
		Camera2 camera2 = view2.getCamera();
		camera2.size.set(320, 240);
		camera2.roll.v = 0.4f;
		camera2.update();
		
		View<Vec2f> view3 = window.newView(scene);
		view3.getPlacement().attach(1, 0, 320, 240);
		view3.setData("Bottom Right");
		Camera2 camera3 = view3.getCamera();
		camera3.size.set(320, 240);
		camera3.roll.v = 0.8f;
		camera3.update();
		
		View<Vec2f> view4 = window.newView(scene);
		view4.getPlacement().attach(0, 0, 320, 240);
		view4.setData("Bottom Left");
		Camera2 camera4 = view4.getCamera();
		camera4.size.set(320, 240);
		camera4.center.set(100, 100);
		camera4.roll.v = 1.2f;
		camera4.update();
		
		game.run();
	}
	
	private static class TestNode extends RenderedNode<Vec2f>
	{
		public static final int VIEW = Axe.renderers.create();
		
		public KeyState esc;
		public Vec2f center = new Vec2f();
		public Vec2f size = new Vec2f(160, 120);
		
		public TestNode()
		{
			super( VIEW );
			
			this.esc = Axe.keys.getKey( Key.ESCAPE );
		}
		
		@Override
		public void update(GameState state, View<Vec2f> view) 
		{
			Vec2f projected = new Vec2f();
			
			view.getWindow().getViews().forEach((v) -> 
			{
				boolean hit = v.project( projected ) != null;
				
				if ( hit )
				{
					v.getWindow().setTitle( v.getData() + " " + projected );
				}
				
				return !hit;
			});
			
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
			
			glBegin( GL_QUADS );
			glColor3f(1, 1, 1);
			glVertex2f(cx-hx, cy-hy);
			glVertex2f(cx+hx, cy-hy);
			glVertex2f(cx+hx, cy+hy);
			glVertex2f(cx-hx, cy+hy);
			glEnd();
		}	
	}
	
}
