package com.axe.lwjgl;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Arrays;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode;

import com.axe.event.EventMap;
import com.axe.monitor.MonitorEvent;
import com.axe.monitor.MonitorSystem;
import com.axe.util.Array;

public class MonitorSystemLWJGL implements MonitorSystem 
{
	private static final int[] X = {0};
	private static final int[] Y = {0};

	public EventMap events;
	public MonitorLWJGL primary;
	public MonitorLWJGL[] monitors = {};
	
	public MonitorSystemLWJGL()
	{
		this.events = new EventMap();
	}
	
	public void init()
	{
		PointerBuffer pointers = glfwGetMonitors();
		
		while (pointers.hasRemaining())
		{	
			monitors = Array.add(createMonitor(pointers.get()), monitors);
		}
				
		sortMonitors();
		
		determinePrimary();
		
		glfwSetMonitorCallback((monitor, event) -> 
		{
			if (event == GLFW_CONNECTED)
			{
				addMonitor( monitor );
			}
			else if (event == GLFW_DISCONNECTED)
			{
				removeMonitor( monitor );
			}
		});
	}
	
	protected MonitorLWJGL createMonitor(long id)
	{		
		String name = glfwGetMonitorName(id);
		
		glfwGetMonitorPos(id, X, Y);
		
		GLFWVidMode mode = glfwGetVideoMode(id);
		
		return new MonitorLWJGL(id, false, X[0], Y[0], mode.width(), mode.height(), mode.refreshRate(), name);
	}
	
	protected void addMonitor(long id)
	{
		MonitorLWJGL created = createMonitor( id );
		
		monitors = Array.add( created, monitors );
		
		sortMonitors();
		
		determinePrimary();
		
		listeners(MonitorEvent.Connected).onEvent( created );
	}
	
	protected void removeMonitor(long id)
	{
		int i = findMonitor( id );
		
		if (i != -1)
		{					
			MonitorLWJGL removed = Array.remove( i, monitors );
			
			removed.valid = false;
			
			determinePrimary();
			
			listeners(MonitorEvent.Disconnected).onEvent( removed );
		}
	}
	
	protected int findMonitor(long id)
	{
		for (int i = 0; i < monitors.length; i++)
		{
			if (monitors[i].id == id)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	protected void sortMonitors()
	{
		Arrays.sort(monitors);	
	}
	
	protected void determinePrimary()
	{
		long id = glfwGetPrimaryMonitor();
		
		for (int i = 0; i < monitors.length; i++)
		{
			MonitorLWJGL monitor = monitors[i];
			
			if (monitor.primary = (monitor.id == id))
			{
				primary = monitor;
			}
		}
	}
	
	@Override
	public EventMap getEvents() 
	{
		return events;
	}

	@Override
	public MonitorLWJGL getPrimary() 
	{
		return primary;
	}

	@Override
	public MonitorLWJGL[] getMonitors() 
	{
		return monitors;
	}

}
