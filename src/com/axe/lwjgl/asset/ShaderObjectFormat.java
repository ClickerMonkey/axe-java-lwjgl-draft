package com.axe.lwjgl.asset;

import java.io.InputStream;
import java.util.Scanner;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetFormat;

import com.axe.gfx.ShaderObject;
import com.axe.gfx.ShaderObjectType;
import com.axe.lwjgl.gfx.ShaderObjectLWJGL;

public class ShaderObjectFormat extends BaseAssetFormat 
{

	private final ShaderObjectType type;
	
	public ShaderObjectFormat(ShaderObjectType type, String ... extensions)
	{
		super( extensions, ShaderObject.class );
		
		this.type = type;
	}
	
	public Object loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		StringBuilder code = new StringBuilder();
		Scanner scanner = new Scanner( input );
		
		try
		{
			while (scanner.hasNextLine()) 
			{
				code.append( scanner.nextLine() ).append( '\n' );
			}
		}
		finally
		{
			scanner.close();
		}
		
		return new ShaderObjectLWJGL( assetInfo, type, code );
	}

}
