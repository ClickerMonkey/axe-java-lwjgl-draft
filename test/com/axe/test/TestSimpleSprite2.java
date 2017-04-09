package com.axe.test;

import org.magnos.asset.Assets;

import com.axe.Axe;
import com.axe.View;
import com.axe.color.Colors;
import com.axe.game.Game;
import com.axe.game.GameEvent;
import com.axe.input.Key;
import com.axe.input.KeyState;
import com.axe.lwjgl.AxeLWJGL;
import com.axe.lwjgl.SettingsLWJGL;
import com.axe.math.Vec2f;
import com.axe.window.Window;
import com.axe2d.Camera2;
import com.axe2d.Scene2;
import com.axe2d.sprite.SimpleSprite2;

public class TestSimpleSprite2 
{

	public static void main(String[] args)
	{
		SettingsLWJGL settings = new SettingsLWJGL( 2, 1 );
		
		AxeLWJGL.init(settings);
				
		Debug.listen(Axe.monitors);
		
		Game game = Axe.games.create();
		
		Window window = game.newWindow();
		window.getPlacement().center(640, 480);
		window.setTitle("TestSimpleSprite2");
		window.setBackground(Colors.CornflowerBlue);
		
		Debug.listen(game);
		Debug.listen(window);
		
		SimpleSprite2 sprite = new SimpleSprite2();
		sprite.setPosition( Vec2f.ZERO );
		sprite.setTexture( Assets.load("assets/monkey.png") );
		
		Scene2 scene = game.newScene(2);
		scene.setRoot(sprite);
		
		View<Vec2f> view = window.newView(scene);
		
		Camera2 camera = view.getCamera();
		camera.size.set(640, 480);
		camera.center.set(0, 0);
		camera.update();
		
		game.on( GameEvent.Start, g -> {
			Axe.logger.log( Axe.graphics.getGraphicsCard() );
		});
		
		final KeyState escape = Axe.keys.getKey(Key.ESCAPE);
		
		game.on( GameEvent.UpdateStart, g -> {
			if (escape.down) {
				game.stop();
			}
		});
		
		game.run();
	}

	
}
