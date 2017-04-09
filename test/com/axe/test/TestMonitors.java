package com.axe.test;

import java.util.Arrays;

import com.axe.Axe;
import com.axe.lwjgl.AxeLWJGL;
import com.axe.lwjgl.SettingsLWJGL;

public class TestMonitors 
{

	public static void main(String[] args)
	{
		SettingsLWJGL settings = new SettingsLWJGL();
		
		AxeLWJGL.init(settings);
		
		System.out.println( Arrays.toString( Axe.monitors.getMonitors() ) );
	}
	
}
