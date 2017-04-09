package com.axe.test;

import org.magnos.asset.Assets;

import com.axe.Axe;
import com.axe.View;
import com.axe.color.Colors;
import com.axe.game.Game;
import com.axe.game.GameEvent;
import com.axe.game.GameState;
import com.axe.input.Key;
import com.axe.input.KeyState;
import com.axe.lwjgl.AxeLWJGL;
import com.axe.lwjgl.SettingsLWJGL;
import com.axe.math.Vec2f;
import com.axe.node.state.StateManagerNode;
import com.axe.node.state.StateNode;
import com.axe.window.Window;
import com.axe2d.Camera2;
import com.axe2d.Scene2;
import com.axe2d.sprite.Sprite2;

public class TestStateMachine 
{
	
	public static enum GameStates {
		Loading,
		Red,
		Green
	}

	public static void main(String[] args)
	{
		SettingsLWJGL settings = new SettingsLWJGL( 2, 1 );
		
		AxeLWJGL.init(settings);
				
		Debug.listen(Axe.monitors);
		
		Game game = Axe.games.create();
		
		Window window = game.newWindow();
		window.getPlacement().center(640, 480);
		window.setTitle("TestStateMachine");
		window.setBackground(Colors.CornflowerBlue);
		
		Debug.listen(game);
		Debug.listen(window);

		Sprite2 spriteRed = new Sprite2();
		spriteRed.setPosition( Vec2f.ZERO );
		spriteRed.setTexture( Assets.load("assets/monkey.png") );
		spriteRed.setColor( Colors.Red );
		
		Sprite2 spriteGreen = new Sprite2();
		spriteGreen.setPosition( Vec2f.ZERO );
		spriteGreen.setTexture( Assets.load("assets/monkey.png") );
		spriteGreen.setColor( Colors.Green );
		
		StateManagerNode<Vec2f, GameStates> manager = new StateManagerNode<>( GameStates.class );
		manager.add( GameStates.Loading, new FakeLoadingState() );
		manager.add( GameStates.Red, new SimpleState( spriteRed ) );
		manager.add( GameStates.Green, new SimpleState( spriteGreen ) );
		manager.set( GameStates.Loading );
		
		Scene2 scene = game.newScene(2);
		scene.setRoot( manager );
		
		View<Vec2f> view = window.newView(scene);
		
		Camera2 camera = view.getCamera();
		camera.size.set(640, 480);
		camera.center.set(0, 0);
		camera.update();
		
		game.on( GameEvent.Start, g -> {
			Axe.logger.log( Axe.graphics.getGraphicsCard() );
		});

		final KeyState RED = Axe.keys.getKey(Key.R);
		final KeyState GREEN = Axe.keys.getKey(Key.G);
		final KeyState ESC = Axe.keys.getKey(Key.ESCAPE);
		
		game.on( GameEvent.UpdateStart, g -> {
			if (RED.isDown()) {
				manager.set( GameStates.Red );
			}
			if (GREEN.isDown()) {
				manager.set( GameStates.Green );
			}
			if (ESC.isDown()) {
				game.stop();
			}
		});
		
		game.run();
	}
	
	private static class FakeLoadingState extends StateNode<Vec2f, GameStates> {
		public float time;
		public FakeLoadingState() {
			super(null);
		}
		public void update(GameState state, View<Vec2f> firstView) {
			time += state.seconds;
			if (time > 3.0f) {
				manager.set(GameStates.Red);
			}
		}
	}
	
	private static class SimpleState extends StateNode<Vec2f, GameStates> {
		public Sprite2 sprite;
		public float transitionTime = 0;
		public SimpleState(Sprite2 sprite) {
			super(sprite);
			this.sprite = sprite;
		}
		public void entering(GameState state, View<Vec2f> view, StateManagerNode<Vec2f, GameStates> manager) {
			transitionTime += state.seconds;
			if (transitionTime > 1) {
				manager.proceed();	
				sprite.color.a = 1;
			} else {
				sprite.color.a = transitionTime;
			}
		}
		public void exiting(GameState state, View<Vec2f> view, StateManagerNode<Vec2f, GameStates> manager) {
			transitionTime -= state.seconds;
			if (transitionTime < 0) {
				manager.proceed();	
				sprite.color.a = 0;
			} else {
				sprite.color.a = transitionTime;
			}
		}
		public void onEntering() {
			sprite.color.a = 0;
			transitionTime = 0;
		}
		public void onExiting() {
			transitionTime = 1;
		}
	}

	
}
