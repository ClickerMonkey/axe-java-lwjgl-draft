package com.axe.lwjgl;

import static org.lwjgl.glfw.GLFW.*;

import java.io.PrintStream;

public class SettingsLWJGL 
{
	
	public static final int DEFAULT_MAJOR = 2;
	public static final int DEFAULT_MINOR = 1;
		
	public int major;
	public int minor;
	public boolean core;
	public boolean forward;
	public PrintStream out;
	
	public SettingsLWJGL()
	{
		this( DEFAULT_MAJOR, DEFAULT_MINOR, false, false );
	}
	
	public SettingsLWJGL(int major, int minor)
	{
		this( major, minor, false, false );
	}
	
	public SettingsLWJGL(int major, int minor, boolean core, boolean forward)
	{
		this.major = major;
		this.minor = minor;
		this.core = core;
		this.forward = forward;
		this.out = System.out;
	}
	
	public boolean hasCoreProfile()
	{
		return major > 3 || (major == 3 && minor >= 2);
	}
	
	protected void apply()
	{
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, major);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, minor);
		
		if (core && hasCoreProfile())
		{
			glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
			
			if (forward)
			{
				glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
			}
		}
	}
	
}
