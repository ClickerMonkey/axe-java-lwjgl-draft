package com.axe.lwjgl;

import static org.lwjgl.opengl.GL11.*;

import com.axe.game.GameState;
import com.axe.math.Bound2i;
import com.axe.math.Scalarf;
import com.axe.math.Vec2f;
import com.axe2d.Scene2;
import com.axe2d.View2;

public class View2LWJGL extends View2 
{
		
	public View2LWJGL(WindowLWJGL window, Scene2 scene)
	{
		super( window, scene );
	}
		
	@Override
	public void draw(GameState state) 
	{		
		final Bound2i view = placement.getBounds( window.getWidth(), window.getHeight(), BOUNDS );
		final float resolution = window.getResolution();
		final Vec2f size = camera.size;
		final Vec2f halfSizeScaled = camera.halfSizeScaled;
		final Vec2f scale = camera.scale;
		final Vec2f center = camera.center;
		final Scalarf roll = camera.roll;
		final Scalarf near = camera.near;
		final Scalarf far = camera.far;
		
		glViewport( 
			(int)(view.l * resolution), 
			(int)(view.b * resolution), 
			(int)(view.width() * resolution), 
			(int)(view.height() * resolution) 
		);
		
		glMatrixMode( GL_PROJECTION );
		glLoadIdentity();
		glOrtho( 0, size.x, size.y, 0, near.v, far.v );
		
		glMatrixMode( GL_MODELVIEW );
		glLoadIdentity();
		glPushMatrix();
		
		glTranslatef( halfSizeScaled.x, halfSizeScaled.y, 0 );
		glRotatef( roll.degrees(), 0, 0, 1 );
		glScalef( scale.x, scale.y, 1 );
		glTranslatef( -center.x, -center.y, 0 );
		
		scene.draw(state, this);
		
		glPopMatrix();
	}

}
