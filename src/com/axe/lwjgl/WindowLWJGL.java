package com.axe.lwjgl;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_FOCUSED;
import static org.lwjgl.glfw.GLFW.GLFW_ICONIFIED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwFocusWindow;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwIconifyWindow;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.opengl.GL;

import com.axe.Axe;
import com.axe.Scene;
import com.axe.View;
import com.axe.game.GameState;
import com.axe.math.Bound2i;
import com.axe.math.Vec;
import com.axe.monitor.Monitor;
import com.axe.ui.Placement;
import com.axe.window.AbstractWindow;
import com.axe.window.WindowEvent;
import com.axe2d.Scene2;
import com.axe3d.Scene3;

public class WindowLWJGL extends AbstractWindow 
{
	private static final int[] WIDTH = {0};
	private static final int[] HEIGHT = {0};

	public GameLWJGL game;
	public long id;
	public boolean initialized;
	public boolean focused;
	public boolean hidden;
	public int frameWidth;
	public int frameHeight;
	
	public WindowLWJGL(GameLWJGL game) 
	{
		this.game = game;
	}

	@Override
	public void update()
	{
		if (!initialized)
		{
			return;
		}
		
		boolean newFocused = glfwGetWindowAttrib(id, GLFW_FOCUSED) == GL_TRUE;
		
		if (newFocused != focused)
		{
			focused = newFocused;
			
			listeners(focused ? WindowEvent.Focus : WindowEvent.Blur).onEvent(this);
		}
		
		boolean newHidden = glfwGetWindowAttrib( id, GLFW_ICONIFIED ) == GL_TRUE;
		
		if (newHidden != hidden)
		{
			hidden = newHidden;
			
			listeners(hidden ? WindowEvent.Minimized : WindowEvent.Maximized).onEvent(this);
		}
	}
	
	@Override
	public void draw(GameState state)
	{
		if (!initialized)
		{
			return;
		}
		
		glfwMakeContextCurrent( id );
		
		listeners( WindowEvent.DrawStart ).onEvent( this );
		
		boolean has3d = views.exists( v -> v.getScene() instanceof Scene3 );
		
		glClearColor( backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a );
		glClear( has3d ? GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT : GL_COLOR_BUFFER_BIT );
		
		views.forAll( view -> view.draw( state ) );
		
		listeners( WindowEvent.DrawEnd ).onEvent( this );
		
		glfwSwapBuffers( id );
	}
	
	@Override
	public boolean isFocused() 
	{
		return focused;
	}

	@Override
	public boolean isHidden() 
	{
		return hidden;
	}
	
	@Override
	public GameLWJGL getGame()
	{
		return game;
	}

	@Override
	public boolean isCloseRequest() 
	{
		return glfwWindowShouldClose( id );
	}

	@Override
	public <V extends Vec<V>, W extends View<V>> W newView(Scene<V> scene) 
	{
		View view = null;
		
		if (scene instanceof Scene2)
		{
			view = new View2LWJGL(this, (Scene2)scene);
		}
		
		if (scene instanceof Scene3)
		{
			view = new View3LWJGL(this, (Scene3)scene);
		}
		
		if (view != null)
		{
			if (scene.getView() == null)
			{
				scene.setView( view );
			}
			
			views.add(view);
		}
		
		return (W)view;
	}

	@Override
	public int getFrameWidth() 
	{
		return frameWidth;
	}

	@Override
	public int getFrameHeight() 
	{
		return frameHeight;
	}

	@Override
	public void close() 
	{
		if (initialized)
		{
			glfwFreeCallbacks(id);
			glfwDestroyWindow(id);
			
			initialized = false;
		
			listItem.remove(this);
		}
	}

	@Override
	public void hide() 
	{
		if (initialized && !hidden)
		{
			glfwHideWindow(id);
			
			hidden = true;
		}
	}

	@Override
	public void show() 
	{
		if (initialized && hidden)
		{
			glfwShowWindow(id);
			
			hidden = false;
		}
		else if (!initialized)
		{
			MonitorLWJGL monitor = getMonitor();
			int parentWidth = monitor.getWidth();
			int parentHeight = monitor.getHeight();
			
			glfwDefaultWindowHints();
			glfwWindowHint( GLFW_VISIBLE, GLFW_FALSE ); 
			glfwWindowHint( GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE );
			
			AxeLWJGL.settings.apply();
			
			if (width == 0)
			{
				width = placement.getWidth( parentWidth );
			}
			
			if (height == 0)
			{
				height = placement.getHeight( parentHeight );
			}
			
			int actualWidth = fullscreen ? parentWidth : width;
			int actualHeight = fullscreen ? parentHeight : height;
			long fullscreenMonitor = fullscreen ? monitor.id : NULL;
			
			id = glfwCreateWindow( actualWidth, actualHeight, title, fullscreenMonitor, AxeLWJGL.mainWindow );
			
			if ( id == NULL ) 
			{
				return;
			}
			
			if ( AxeLWJGL.mainWindow == NULL )
			{
				AxeLWJGL.mainWindow = id;
			}

			int x = placement.getLeft( parentWidth );
			int y = parentHeight - height - placement.getBottom( parentHeight );
						
			glfwSetWindowPos( id, x, y );

			glfwMakeContextCurrent( id );
			
			GL.createCapabilities();
			
			if ( vsync )
			{
				glfwSwapInterval(1);
			}
			
			glfwGetFramebufferSize( id, WIDTH, HEIGHT );
			
			frameWidth = WIDTH[ 0 ];
			frameHeight = HEIGHT[ 0 ];
			
			glfwSetFramebufferSizeCallback(id, (resizeWindow, w, h) -> 
			{
				if (resizeWindow == id)
				{
					frameWidth = w;
					frameHeight = h;	
				}
			});
			
			AxeLWJGL.keys.listen( id, this );
			AxeLWJGL.mouse.listen( id, this );
			
			glfwShowWindow( id );
			
			initialized = true;
		}
	}
	
	@Override
	public MonitorLWJGL getMonitor()
	{
		if (monitor == null || !monitor.isValid())
		{
			monitor = Axe.monitors.getPrimary();
		}
		
		return (MonitorLWJGL)monitor;
	}

	@Override
	public void minimize() 
	{
		if (initialized && !hidden)
		{
			glfwIconifyWindow( id );
			
			hidden = true;
		}
	}

	@Override
	public void maximize() 
	{
		if (initialized)
		{
			glfwMaximizeWindow( id );
			
			hidden = false;
		}
	}

	@Override
	public void focus() 
	{
		if (initialized && !focused)
		{
			glfwFocusWindow( id );
			
			focused = true;
		}
	}

	@Override
	protected boolean isInitialized() 
	{
		return initialized;
	}

	@Override
	protected void applyTitle(String title) 
	{
		glfwSetWindowTitle( id, title );
	}

	@Override
	protected void applyFullscreen(boolean fullscreen) 
	{
		applyWindowState();
	}

	@Override
	protected void applyVsync(boolean vsync) 
	{
		if (vsync)
		{
			glfwSwapInterval(1);
		}
	}

	@Override
	protected void applyFrameRate(int frameRate) 
	{
		applyWindowState();
	}

	@Override
	protected void applyPlacement(Placement placement, Monitor monitor) 
	{
		applyWindowState();
	}

	@Override
	protected void applyResizable(boolean resizable) 
	{
		glfwWindowHint( GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE );
	}
	
	protected void applyWindowState()
	{
		long monitorPointer = getMonitor().id;
		int refreshRate = getRefreshRate();
		
		if ( fullscreen )
		{
			glfwSetWindowMonitor( id, monitorPointer, 0, 0, monitor.getWidth(), monitor.getHeight(), refreshRate );
		}
		else
		{
			Bound2i bounds = placement.getBounds( monitor.getWidth(), monitor.getHeight(), new Bound2i() );
			
			int x = monitor.getX() + bounds.l;
			int y = monitor.getY() + bounds.t;
			int w = bounds.width();
			int h = bounds.height();
			
			glfwSetWindowMonitor( id, NULL, x, y, w, h, refreshRate );
		}
	}
	
	protected int getRefreshRate()
	{
		return Math.min( getMonitor().getRefreshRate(), vsync ? 0 : frameRate );
	}

}
