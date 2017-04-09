package com.axe.test;

import com.axe.Axe;
import com.axe.View;
import com.axe.game.Game;
import com.axe.game.GameState;
import com.axe.input.Key;
import com.axe.input.KeyState;
import com.axe.lwjgl.AxeLWJGL;
import com.axe.lwjgl.SettingsLWJGL;
import com.axe.math.Vec2f;
import com.axe.monitor.Monitor;
import com.axe.node.Node;
import com.axe.window.Window;
import com.axe2d.Scene2;

public class TestMultipleMonitors 
{

	public static void main(String[] args)
	{
		SettingsLWJGL settings = new SettingsLWJGL();
		
		AxeLWJGL.init(settings);
		
		Debug.listen(Axe.monitors);
		
		Game game = Axe.games.create();
		
		Debug.listen(game);
		
		Scene2 scene = game.newScene(2);
		scene.setRoot(new TimedNode(game, 5000000000L));
		
		Monitor[] monitors = Axe.monitors.getMonitors();
		
		for (int i = 0; i < monitors.length; i++)
		{
			Window window = game.newWindow();
			window.getPlacement().center(640, 480);
			window.setTitle("TestMultipleMonitors");
			window.newView(scene);
			
			Debug.listen(window);
		}
		
		game.run();
	}
	
	private static class TimedNode implements Node<Vec2f>
	{
		
		public Node<Vec2f> parent;
		public Game game;
		public long countdown;
		public KeyState esc;
		
		public TimedNode(Game game, long countdown)
		{
			this.game = game;
			this.countdown = countdown;
			this.esc = Axe.keys.getKey( Key.ESCAPE );
		}
		
		@Override
		public void update(GameState state, View<Vec2f> view) 
		{
			if (state.currentTime - state.startTime > countdown || esc.down)
			{
				game.stop();
			}
		}

		@Override
		public Node<Vec2f> getParent() 
		{
			return parent;
		}

		@Override
		public void setParent(Node<Vec2f> parent) 
		{
			this.parent = parent;
		}
		
	}
	
}
