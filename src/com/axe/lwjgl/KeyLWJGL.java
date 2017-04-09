package com.axe.lwjgl;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallbackI;

import com.axe.input.AbstractKeyEngine;
import com.axe.input.Key;
import com.axe.input.KeyState;
import com.axe.util.EnumIntBiMap;
import com.axe.window.Window;

public class KeyLWJGL extends AbstractKeyEngine 
{
		
	public EnumIntBiMap<Key> keyMap;
	
	public KeyLWJGL() 
	{
		keyMap = new EnumIntBiMap( Key.class, GLFW_KEY_LAST + 1 );
		keyMap.add( Key.NONE, 0 );
		keyMap.add( Key.ESCAPE, GLFW_KEY_ESCAPE );
		keyMap.add( Key.N1, GLFW_KEY_1 );
		keyMap.add( Key.N2, GLFW_KEY_2 );
		keyMap.add( Key.N3, GLFW_KEY_3 );
		keyMap.add( Key.N4, GLFW_KEY_4 );
		keyMap.add( Key.N5, GLFW_KEY_5 );
		keyMap.add( Key.N6, GLFW_KEY_6 );
		keyMap.add( Key.N7, GLFW_KEY_7 );
		keyMap.add( Key.N8, GLFW_KEY_8 );
		keyMap.add( Key.N9, GLFW_KEY_9 );
		keyMap.add( Key.N0, GLFW_KEY_0 );
		keyMap.add( Key.MINUS, GLFW_KEY_MINUS );
		keyMap.add( Key.EQUALS, GLFW_KEY_EQUAL );
		keyMap.add( Key.BACK, GLFW_KEY_BACKSPACE );
		keyMap.add( Key.TAB, GLFW_KEY_TAB );
		keyMap.add( Key.Q, GLFW_KEY_Q );
		keyMap.add( Key.W, GLFW_KEY_W );
		keyMap.add( Key.E, GLFW_KEY_E );
		keyMap.add( Key.R, GLFW_KEY_R );
		keyMap.add( Key.T, GLFW_KEY_T );
		keyMap.add( Key.Y, GLFW_KEY_Y );
		keyMap.add( Key.U, GLFW_KEY_U );
		keyMap.add( Key.I, GLFW_KEY_I );
		keyMap.add( Key.O, GLFW_KEY_O );
		keyMap.add( Key.P, GLFW_KEY_P );
		keyMap.add( Key.LEFT_BRACKET, GLFW_KEY_LEFT_BRACKET );
		keyMap.add( Key.RIGHT_BRACKET, GLFW_KEY_RIGHT_BRACKET );
		keyMap.add( Key.RETURN, GLFW_KEY_ENTER );
		keyMap.add( Key.LEFT_CONTROL, GLFW_KEY_LEFT_CONTROL );
		keyMap.add( Key.A, GLFW_KEY_A );
		keyMap.add( Key.S, GLFW_KEY_S );
		keyMap.add( Key.D, GLFW_KEY_D );
		keyMap.add( Key.F, GLFW_KEY_F );
		keyMap.add( Key.G, GLFW_KEY_G );
		keyMap.add( Key.H, GLFW_KEY_H );
		keyMap.add( Key.J, GLFW_KEY_J );
		keyMap.add( Key.K, GLFW_KEY_K );
		keyMap.add( Key.L, GLFW_KEY_L );
		keyMap.add( Key.SEMICOLON, GLFW_KEY_SEMICOLON );
		keyMap.add( Key.APOSTROPHE, GLFW_KEY_APOSTROPHE );
		keyMap.add( Key.GRAVE, GLFW_KEY_GRAVE_ACCENT );
		keyMap.add( Key.LEFT_SHIFT, GLFW_KEY_LEFT_SHIFT );
		keyMap.add( Key.BACKSLASH, GLFW_KEY_BACKSLASH );
		keyMap.add( Key.Z, GLFW_KEY_Z );
		keyMap.add( Key.X, GLFW_KEY_X );
		keyMap.add( Key.C, GLFW_KEY_C );
		keyMap.add( Key.V, GLFW_KEY_V );
		keyMap.add( Key.B, GLFW_KEY_B );
		keyMap.add( Key.N, GLFW_KEY_N );
		keyMap.add( Key.M, GLFW_KEY_M );
		keyMap.add( Key.COMMA, GLFW_KEY_COMMA );
		keyMap.add( Key.PERIOD, GLFW_KEY_PERIOD );
		keyMap.add( Key.SLASH, GLFW_KEY_SLASH );
		keyMap.add( Key.RIGHT_SHIFT, GLFW_KEY_RIGHT_SHIFT );
		keyMap.add( Key.MULTIPLY, GLFW_KEY_KP_MULTIPLY );
		keyMap.add( Key.LEFT_ALT, GLFW_KEY_LEFT_ALT );
		keyMap.add( Key.SPACE, GLFW_KEY_SPACE );
		keyMap.add( Key.CAPITAL, GLFW_KEY_CAPS_LOCK );
		keyMap.add( Key.F1, GLFW_KEY_F1 );
		keyMap.add( Key.F2, GLFW_KEY_F2 );
		keyMap.add( Key.F3, GLFW_KEY_F3 );
		keyMap.add( Key.F4, GLFW_KEY_F4 );
		keyMap.add( Key.F5, GLFW_KEY_F5 );
		keyMap.add( Key.F6, GLFW_KEY_F6 );
		keyMap.add( Key.F7, GLFW_KEY_F7 );
		keyMap.add( Key.F8, GLFW_KEY_F8 );
		keyMap.add( Key.F9, GLFW_KEY_F9 );
		keyMap.add( Key.F10, GLFW_KEY_F10 );
		keyMap.add( Key.NUMLOCK, GLFW_KEY_NUM_LOCK );
		keyMap.add( Key.SCROLL, GLFW_KEY_SCROLL_LOCK );
		keyMap.add( Key.NUMPAD7, GLFW_KEY_KP_7 );
		keyMap.add( Key.NUMPAD8, GLFW_KEY_KP_8 );
		keyMap.add( Key.NUMPAD9, GLFW_KEY_KP_9 );
		keyMap.add( Key.SUBTRACT, GLFW_KEY_KP_SUBTRACT );
		keyMap.add( Key.NUMPAD4, GLFW_KEY_KP_4 );
		keyMap.add( Key.NUMPAD5, GLFW_KEY_KP_5 );
		keyMap.add( Key.NUMPAD6, GLFW_KEY_KP_6 );
		keyMap.add( Key.ADD, GLFW_KEY_KP_ADD );
		keyMap.add( Key.NUMPAD1, GLFW_KEY_KP_1 );
		keyMap.add( Key.NUMPAD2, GLFW_KEY_KP_2 );
		keyMap.add( Key.NUMPAD3, GLFW_KEY_KP_3 );
		keyMap.add( Key.NUMPAD0, GLFW_KEY_KP_0 );
		keyMap.add( Key.DECIMAL, GLFW_KEY_KP_DECIMAL );
		keyMap.add( Key.F11, GLFW_KEY_F11 );
		keyMap.add( Key.F12, GLFW_KEY_F12 );
		keyMap.add( Key.F13, GLFW_KEY_F13 );
		keyMap.add( Key.F14, GLFW_KEY_F14 );
		keyMap.add( Key.F15, GLFW_KEY_F15 );
		keyMap.add( Key.KANA, 0 ); // ?
		keyMap.add( Key.CONVERT, 0 );
		keyMap.add( Key.NOCONVERT, 0 );
		keyMap.add( Key.YEN, 0 );
		keyMap.add( Key.NUMPADEQUALS, GLFW_KEY_KP_EQUAL );
		keyMap.add( Key.CIRCUMFLEX, 0);
		keyMap.add( Key.AT, GLFW_KEY_2 ); // +shift
		keyMap.add( Key.COLON, GLFW_KEY_SEMICOLON ); // +shift
		keyMap.add( Key.UNDERLINE, GLFW_KEY_MINUS ); // +shift
		keyMap.add( Key.KANJI, 0 );
		keyMap.add( Key.STOP, 0);
		keyMap.add( Key.AX, 0 );
		keyMap.add( Key.UNLABELED, 0 );
		keyMap.add( Key.NUMPADENTER, GLFW_KEY_KP_ENTER );
		keyMap.add( Key.RIGHT_CONTROL, GLFW_KEY_RIGHT_CONTROL );
		keyMap.add( Key.NUMPADCOMMA, 0 );
		keyMap.add( Key.DIVIDE, GLFW_KEY_KP_DIVIDE );
		keyMap.add( Key.SYSRQ, 0 );
		keyMap.add( Key.RIGHT_ALT, GLFW_KEY_RIGHT_ALT );
		keyMap.add( Key.PAUSE, GLFW_KEY_PAUSE );
		keyMap.add( Key.HOME, GLFW_KEY_HOME );
		keyMap.add( Key.UP, GLFW_KEY_UP );
		keyMap.add( Key.PAGE_UP, GLFW_KEY_PAGE_UP );
		keyMap.add( Key.LEFT, GLFW_KEY_LEFT );
		keyMap.add( Key.RIGHT, GLFW_KEY_RIGHT );
		keyMap.add( Key.END, GLFW_KEY_END );
		keyMap.add( Key.DOWN, GLFW_KEY_DOWN );
		keyMap.add( Key.PAGE_DOWN, GLFW_KEY_PAGE_DOWN );
		keyMap.add( Key.INSERT, GLFW_KEY_INSERT );
		keyMap.add( Key.DELETE, GLFW_KEY_DELETE );
		keyMap.add( Key.LEFT_COMMAND, GLFW_KEY_LEFT_CONTROL );
		keyMap.add( Key.RIGHT_COMMAND, GLFW_KEY_RIGHT_CONTROL );
		keyMap.add( Key.APPS, 0 );
		keyMap.add( Key.POWER, 0 );
		keyMap.add( Key.SLEEP, 0 );
	}
	
	public void listen(long id, Window window)
	{
		int start = GLFW_KEY_SPACE;
		int end = GLFW_KEY_LAST + 1;
		
		for (int i = start; i < end; i++)
		{
			boolean down = glfwGetKey( id, i ) == GLFW_PRESS;
			Key key = keyMap.get( i );
			
			if (key != null)
			{
				KeyState state = getKey( key );
				
				state.down = down;	
				state.window = window;
			}
		}
		
		glfwSetKeyCallback( id, new GLFWKeyCallbackI() 
		{
			public void invoke(long keyWindow, int keyCode, int scancode, int action, int mods) 
			{
				Key key = keyMap.get( keyCode );
				
				if (key != null) 
				{
					long inputTime = AxeLWJGL.currentTime;
					
					if ( action == GLFW_PRESS ) 
					{
						getKey( key ).setDown( true, inputTime, window );
						
						queueKeyState( key, true, inputTime, window );
					}
					
					if ( action == GLFW_RELEASE ) 
					{
						getKey( key ).setDown( false, inputTime, window );
						
						queueKeyState( key, true, inputTime, window );
					}
				}
			}
		});
	}
	
	public void flush()
	{
		queue.clear();
	}
	
	public void destroy()
	{
		
	}

}
