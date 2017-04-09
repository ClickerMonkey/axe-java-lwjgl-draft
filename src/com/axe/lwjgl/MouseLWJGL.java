package com.axe.lwjgl;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;

import com.axe.input.AbstractMouseEngine;
import com.axe.input.MouseState;
import com.axe.window.Window;

public class MouseLWJGL extends AbstractMouseEngine 
{
	private static final double[] X = {0.0};
	private static final double[] Y = {0.0};
	
	
	public MouseLWJGL()
	{
		
	}
	
	public void listen(long id, Window window)
	{
		MouseState relativeMouse = window.getMouse();
		
		for (int i = 0; i < states.length; i++)
		{
			states[ i ].down = glfwGetMouseButton( id, i ) == GLFW_PRESS;
		}
		
		glfwGetCursorPos( id, X, Y );
		
		int cx = (int)X[ 0 ];
		int cy = (int)Y[ 0 ];
		
		mouse.reset( cx, cy );
		relativeMouse.reset( cx, cy );
		
		glfwSetCursorPosCallback( id, (mouseWindow, mx, my) -> 
		{
			int ix = (int)mx;
			int iy = (int)my;
			
			mouse.accumulatePosition( ix, iy );
			
			relativeMouse.accumulatePosition( ix, iy );
		});
		
		glfwSetScrollCallback(id, (mouseWindow, x, y) -> 
		{
			int iy = (int)y;
			
			mouse.accumulateScroll( iy );
			
			relativeMouse.accumulateScroll( iy );
		});
		
		glfwSetMouseButtonCallback(id, (mouseWindow, button, action, mods) ->
		{
			long inputTime = AxeLWJGL.currentTime;
			boolean down = action == GLFW_PRESS;
			
			getButton( button ).setDown( down, inputTime, window );
			
			queueButtonState( button, down, inputTime, window );
		});
	}
	
	public void flush()
	{
		mouse.clear();
		queue.clear();
	}

	public void destroy()
	{
		
	}

	@Override
	public void setGrabbed(boolean grabbed) 
	{
		this.grabbed = grabbed;
		
		this.updateMouseMode();
	}
	
	@Override
	public void setHidden(boolean hidden)
	{
		this.hidden = hidden;
		
		this.updateMouseMode();
	}
	
	protected void updateMouseMode()
	{
		long window = glfwGetCurrentContext();
		int state = grabbed ? GLFW_CURSOR_DISABLED : (hidden ? GLFW_CURSOR_HIDDEN : GLFW_CURSOR_NORMAL);
		
		glfwSetInputMode( window, GLFW_CURSOR, state );
	}

}
