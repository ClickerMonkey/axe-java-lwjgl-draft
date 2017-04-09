package com.axe.test;

import com.axe.Axe;
import com.axe.game.Game;
import com.axe.game.GameEvent;
import com.axe.input.Key;
import com.axe.input.MouseState;
import com.axe.lwjgl.AxeLWJGL;
import com.axe.lwjgl.SettingsLWJGL;
import com.axe.node.NodeList;
import com.axe.window.Window;
import com.axe.window.WindowEvent;
import com.axe2d.Scene2;

public class TestRelativeMouse 
{

	public static void main(String[] args)
	{
		SettingsLWJGL settings = new SettingsLWJGL();
		
		AxeLWJGL.init(settings);
				
		Debug.listen(Axe.monitors);
		
		Game game = Axe.games.create();
		
		Window window1 = game.newWindow();
		window1.getPlacement().attach(0.0f, 0.5f, 640f, 480f);
		
		Window window2 = game.newWindow();
		window2.getPlacement().attach(1.0f, 0.5f, 640f, 480f);
		
		Debug.listen(game);
		Debug.listen(window1);
		Debug.listen(window2);
		
		Scene2 scene = game.newScene(2);
		scene.setRoot(NodeList.array());
		
		window1.newView(scene);
		window2.newView(scene);
		
		window1.on( WindowEvent.DrawStart, TestRelativeMouse::updateWindowTitle );
		window2.on( WindowEvent.DrawStart, TestRelativeMouse::updateWindowTitle );
		
		game.on( GameEvent.UpdateStart, (g) -> {
			if ( Axe.keys.getKey( Key.ESCAPE ).down ) {
				g.stop();
			}
		});
		
		game.run();
	}
	
	public static void updateWindowTitle(Window w)
	{
		MouseState mouse = w.getMouse();
		
		w.setTitle( mouse.position.toString() + (mouse.inside ? " (inside)" : " (outside)") );
	}
	
	
}
