package com.axe.lwjgl;

import static org.lwjgl.glfw.GLFW.glfwInit;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.magnos.asset.Assets;

import com.axe.Axe;
import com.axe.core.Factory;
import com.axe.game.Game;
import com.axe.gfx.ShaderObject;
import com.axe.gfx.ShaderObjectType;
import com.axe.gfx.Texture;
import com.axe.lwjgl.asset.ShaderObjectFormat;
import com.axe.lwjgl.asset.TextureFormat;
import com.axe.lwjgl.gfx.GraphicsLWJGL;
import com.axe.lwjgl.gfx.ShaderObjectLWJGL;
import com.axe.lwjgl.gfx.TextureLWJGL;
import com.axe.lwjgl.renderers.SimpleSprite2Renderer;
import com.axe.lwjgl.renderers.Sprite2Renderer;
import com.axe.resource.ResourceFutureAsset;
import com.axe.util.DefaultLogger;
import com.axe2d.sprite.SimpleSprite2;
import com.axe2d.sprite.Sprite2;

public class AxeLWJGL 
{
	
	public static Factory<Game> games;
	public static KeyLWJGL keys;
	public static MouseLWJGL mouse;
	public static SettingsLWJGL settings;
	public static MonitorSystemLWJGL monitors;
	public static GraphicsLWJGL graphics;
	
	protected static long mainWindow;
	protected static long currentTime;

	public static void init(SettingsLWJGL settings)
	{
		if (Axe.logger instanceof DefaultLogger)
		{
			Axe.logger = new DefaultLogger( settings.out );
		}
		
		Axe.load();
		
		Assets.addFormats(
			new TextureFormat(),
			new ShaderObjectFormat( ShaderObjectType.Fragment, "frag", "fs" ),
			new ShaderObjectFormat( ShaderObjectType.Vertex, "vert", "vs" ),
			new ShaderObjectFormat( ShaderObjectType.Geometry, "geom", "gs" )
		); 
		
		Assets.addFutureAssetFactory( ResourceFutureAsset.FACTORY, 
			ShaderObject.class, ShaderObjectLWJGL.class,
			Texture.class, TextureLWJGL.class
		);
		
		AxeLWJGL.settings = settings;
		Axe.games = games = GameLWJGL.FACTORY;
		Axe.keys = keys = new KeyLWJGL();
		Axe.mouse = mouse = new MouseLWJGL();
		Axe.monitors = monitors = new MonitorSystemLWJGL();
		Axe.graphics = graphics = new GraphicsLWJGL();
		
		GLFWErrorCallback.createPrint( settings.out ).set();
		
		if ( !glfwInit() ) 
		{
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		monitors.init();

		Axe.renderers.register( SimpleSprite2.VIEW, new SimpleSprite2Renderer() );
		Axe.renderers.register( Sprite2.VIEW, new Sprite2Renderer() );
	}
	
}
