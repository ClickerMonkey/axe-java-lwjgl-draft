package com.axe.lwjgl.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLCapabilities;

import com.axe.gfx.AbstractTexture;
import com.axe.gfx.Blend;
import com.axe.gfx.Mode;
import com.axe.gfx.TextureInfo;
import com.axe.lwjgl.AxeLWJGL;

public class TextureLWJGL extends AbstractTexture 
{
	
	public static int currentID = -1;
	
	private ByteBuffer image;

	public int id = -1;
	public int format = -1;

	public TextureLWJGL(TextureInfo info, int imageWidth, int imageHeight, int textureWidth, int textureHeight, ByteBuffer image, int format) 
	{
		super( info, imageWidth, imageHeight, textureWidth, textureHeight );
		
		this.image = image;
		this.format = format;
	}
	
	public boolean isBound() 
	{
		return (id == currentID);
	}

	@Override
	public boolean hasAlpha()
	{
		return format == GL_RGBA;
	}
	
	@Override
	public void bind()
	{
		if (currentID != id && id != -1) 
		{
			GraphicsLWJGL gr = AxeLWJGL.graphics;
			
			gr.enable( Mode.Texture );
			
			glBindTexture(GL_TEXTURE_2D, id);
			
			currentID = id;
			
			if (format == GL_RGBA)
			{
				gr.enable( Mode.Blend );
				gr.setBlend( Blend.Alpha );
			}
		}
	}
	
	@Override
	public void unbind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
		
		currentID = -1;
	}
	
	@Override
	public boolean isActivated()
	{
		return ( id != -1 );
	}
	
	@Override
	public void activate()
	{
		if ( id == -1 )
		{
			final GLCapabilities caps = GL.getCapabilities();
			final TextureInfo info = (TextureInfo)getInfo();
			
			glGetError();
			id = glGenTextures();
						
			glBindTexture( GL_TEXTURE_2D, id );
			
			if (format == GL_RGB)
			{
				if ( (width & 3) != 0 )
				{
					glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (width & 1));
				}
			}
			
			glTexImage2D( GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, image );
			
			AxeLWJGL.graphics.setWrap( wrap );
			AxeLWJGL.graphics.setMagnify( mag );
			AxeLWJGL.graphics.setMinify( min );
			
			if (anisotrophy > 1 && caps.GL_EXT_texture_filter_anisotropic) 
			{
				glTexParameterf(GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisotrophy);				
			}
			
			if ( info.isMipMap() )
			{
				if (caps.OpenGL30 || caps.GL_ARB_framebuffer_object)
				{
					glEnable( GL_TEXTURE_2D );
					glGenerateMipmap( GL_TEXTURE_2D );
				}
				else if (caps.OpenGL14)
				{
					glTexParameteri(GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL_TRUE);
				}
				else
				{
					
				}
			}
			
			AxeLWJGL.graphics.enable( Mode.Texture );
			
			stbi_image_free( image );
			image = null;
		}
	}

	@Override
	public void destroy() 
	{
		if ( id != -1 ) 
		{
			glDeleteTextures( id );
			
			id = -1;
			format = -1;
			image = null;
		}
	}
	
	@Override
	public int hashCode()
	{
		return id;
	}

}
