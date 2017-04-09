package com.axe.lwjgl;

import com.axe.monitor.Monitor;

public class MonitorLWJGL implements Monitor, Comparable<MonitorLWJGL> 
{
	
	public final long id;
	public boolean primary;
	public boolean valid;
	public final int x;
	public final int y;
	public final int width;
	public final int height;
	public final int refreshRate;
	public final String name;	
	public Object data;

	public MonitorLWJGL(long id, boolean primary, int x, int y, int width, int height, int refreshRate, String name) 
	{
		this.id = id;
		this.primary = primary;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.refreshRate = refreshRate;
		this.name = name;
		this.valid = true;
	}

	@Override
	public int getWidth() 
	{
		return width;
	}

	@Override
	public int getHeight() 
	{
		return height;
	}

	@Override
	public int getRefreshRate() 
	{
		return refreshRate;
	}

	@Override
	public boolean isPrimary() 
	{
		return primary;
	}
	
	@Override
	public int getX()
	{
		return x;
	}
	
	@Override
	public int getY()
	{
		return y;
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public int compareTo(MonitorLWJGL o) 
	{
		return x - o.x;
	}
	
	@Override
	public boolean isValid()
	{
		return valid;
	}

	@Override
	public <T> T getData() 
	{
		return (T) data;
	}

	@Override
	public void setData(Object data) 
	{
		this.data = data;
	}

	@Override
	public String toString() 
	{
		return "MonitorLWJGL [id=" + id + ", primary=" + primary + ", x=" + x
				+ ", y=" + y + ", width=" + width + ", height=" + height
				+ ", refreshRate=" + refreshRate + ", name=" + name + "]";
	}

}
