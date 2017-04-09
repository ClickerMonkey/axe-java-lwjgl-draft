package com.axe.lwjgl;

import static org.lwjgl.glfw.GLFW.*;

import org.magnos.asset.Assets;

import com.axe.core.Factory;
import com.axe.game.AbstractGame;
import com.axe.game.Game;
import com.axe.window.Window;

public class GameLWJGL extends AbstractGame 
{

	public static final Factory<Game> FACTORY = () -> new GameLWJGL();
	
	@Override
	public Window newWindow() 
	{
		WindowLWJGL window = new WindowLWJGL( this );
		
		windows.add(window);
		
		return window;
	}

	@Override
	protected void prepare() 
	{
		AxeLWJGL.keys.init( state.currentTime );
		AxeLWJGL.mouse.init( state.currentTime );
		
		AxeLWJGL.graphics.init();
	}

	@Override
	protected void processInput() 
	{
		AxeLWJGL.currentTime = state.currentTime;
		
		glfwPollEvents();
		
		AxeLWJGL.keys.tick( state.currentTime );
		AxeLWJGL.mouse.tick( state.currentTime );
	}

	@Override
	protected void flushInput() 
	{
		AxeLWJGL.keys.flush();
		AxeLWJGL.mouse.flush();
	}
	
	@Override
	protected void destroy() 
	{
		scenes.forAll( s -> s.getRoot().destroy() );
		
		AxeLWJGL.keys.destroy();
		AxeLWJGL.mouse.destroy();
		
		if (Assets.getFutureAssetService() != null)
		{
			Assets.getFutureAssetService().shutdown();	
		}
		
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

}
