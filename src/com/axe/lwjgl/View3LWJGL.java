package com.axe.lwjgl;

import com.axe.game.GameState;
import com.axe3d.Scene3;
import com.axe3d.View3;

public class View3LWJGL extends View3 
{

	public View3LWJGL(WindowLWJGL window, Scene3 scene)
	{
		super( window, scene );
	}

	
	@Override
	public void draw(GameState state) 
	{
		// TODO
		
		// scene.getRoot().draw(state, this);
	}
	
}
