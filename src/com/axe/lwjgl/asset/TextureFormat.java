package com.axe.lwjgl.asset;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetFormat;

import com.axe.Axe;
import com.axe.gfx.Texture;
import com.axe.gfx.TextureInfo;
import com.axe.lwjgl.gfx.TextureLWJGL;
import com.axe.util.Buffers;

public class TextureFormat extends BaseAssetFormat
{
	
	public static final String[] EXTENSIONS = {"png", "bmp", "jpeg", "jpg", "tga"};

	private static TextureFormat instance = new TextureFormat();
	
	public static TextureFormat get()
	{
		return instance;
	}
	
	public TextureFormat()
	{
		super( EXTENSIONS, Texture.class );
	}

	@Override
	public AssetInfo getInfo( Class<?> type ) 
	{
		return new TextureInfo();
	}
	
	@Override
	public Object loadAsset(InputStream input, AssetInfo assetInfo) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] chunk = new byte[ 4096 ];
		int read = 0;
		while ((read = input.read(chunk)) > 0) {
			out.write(chunk, 0, read);
		}
		
		ByteBuffer imageBuffer = Buffers.bytes( out.size() );
		imageBuffer.put( out.toByteArray() );
		imageBuffer.flip();
		
		return load( imageBuffer, assetInfo );
	}
	
	public Texture load(ByteBuffer imageBuffer, AssetInfo assetInfo)
	{
		TextureInfo info = (TextureInfo)assetInfo;
				
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		
		stbi_set_flip_vertically_on_load(true);
		
		boolean result = stbi_info_from_memory(imageBuffer, w, h, comp); 
		if ( !result )
		{
			Axe.logger.log( "Failed to fetch the image info" );
			
			throw new RuntimeException("Failed to fetch image info: " + result);
		}
		
		int imageWidth = w.get(0);
		int imageHeight = h.get(0);
		
		ByteBuffer image = stbi_load_from_memory(imageBuffer, w, h, comp, 4);
		
		if ( image == null )
		{
			Axe.logger.log( "Failed to load image data" );
			
			throw new RuntimeException("Failed to load image data");
		}
		
		int width = w.get(0);
		int height = h.get(0);
		int components = comp.get(0);
		int sourceFormat = components == 3 ? GL_RGB : GL_RGBA;
		
		TextureLWJGL tex = new TextureLWJGL( info, imageWidth, imageHeight, width, height, image, sourceFormat );
		tex.anisotrophy( info.getAnisotrophy() );
		tex.minify( info.getMinify() );
		tex.magnify( info.getMagnify() );
		tex.wrap( info.getWrap() );
		
		return tex;
	}
	
	public Texture load(BufferedImage source, AssetInfo assetInfo) 
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try 
		{
			ImageIO.write(source, "png", out);
			
			ByteArrayInputStream input = new ByteArrayInputStream( out.toByteArray() );
			
			return (Texture) loadAsset( input, assetInfo );
		} 
		catch (IOException e) 
		{
			return null;
		}
	}

}