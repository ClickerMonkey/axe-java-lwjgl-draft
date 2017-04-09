package com.axe.lwjgl.gfx;

import static org.lwjgl.opengl.ARBVertexBufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL40.*;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.magnos.asset.Assets;

import com.axe.gfx.Blend;
import com.axe.gfx.ColorOperation;
import com.axe.gfx.DataType;
import com.axe.gfx.FogMode;
import com.axe.gfx.GraphicsEngine;
import com.axe.gfx.Magnify;
import com.axe.gfx.Minify;
import com.axe.gfx.Mode;
import com.axe.gfx.Primitive;
import com.axe.gfx.Shader;
import com.axe.gfx.ShaderObject;
import com.axe.gfx.ShaderObjectType;
import com.axe.gfx.Texture;
import com.axe.gfx.VertexBuffer;
import com.axe.gfx.VertexFormat;
import com.axe.gfx.VertexMesh;
import com.axe.gfx.VertexMeshType;
import com.axe.gfx.Wrap;
import com.axe.tile.Tile;
import com.axe.util.EnumIntMap;

public class GraphicsLWJGL implements GraphicsEngine 
{

	public EnumIntMap<Mode> modes;
	public EnumIntMap<Primitive> primitives;
	public EnumIntMap<Wrap> wraps;
	public EnumIntMap<Blend> blendSrc;
	public EnumIntMap<Blend> blendDst;
	public EnumIntMap<ColorOperation> colorOps;
	public EnumIntMap<Magnify> magnify;
	public EnumIntMap<Minify> minify;
	public EnumIntMap<FogMode> fog;
	public EnumIntMap<DataType> dataTypes;
	public EnumIntMap<VertexMeshType> vertexTypes;
	public EnumIntMap<ShaderObjectType> shaderTypes;
	
	public boolean[] modeEnabled;
	public boolean[] modeLast;
	
	public GraphicsLWJGL()
	{
		
	}
	
	private EnumIntMap<Mode> loadModes()
	{
		EnumIntMap<Mode> map = new EnumIntMap<>( Mode.class );
		
		map.add( Mode.AlphaTest, GL_ALPHA_TEST );
		map.add( Mode.Blend, GL_BLEND );
		map.add( Mode.Texture, GL_TEXTURE_2D );
		map.add( Mode.Culling, GL_CULL_FACE );
		map.add( Mode.Depth, GL_DEPTH_TEST );
		map.add( Mode.Lighting, GL_LIGHTING );
		map.add( Mode.Normalize, GL_NORMALIZE );
		map.add( Mode.Smooth, GL_POLYGON_SMOOTH );
		map.add( Mode.Clipping, GL_SCISSOR_TEST );
		map.add( Mode.LineSmooth, GL_LINE_SMOOTH );
		map.add( Mode.LogicalOperation, GL_COLOR_LOGIC_OP );
		map.add( Mode.Fog, GL_FOG );
		map.add( Mode.DepthMask, GL_DEPTH_WRITEMASK );
		
		return map;
	}
	
	private EnumIntMap<Primitive> loadPrimitives()
	{
		EnumIntMap<Primitive> map = new EnumIntMap<>( Primitive.class );
		
		map.add( Primitive.Point, GL_POINTS );
		map.add( Primitive.Line, GL_LINES );
		map.add( Primitive.LineStrip, GL_LINE_STRIP );
		map.add( Primitive.LineLoop, GL_LINE_LOOP );
		map.add( Primitive.Triangle, GL_TRIANGLES );
		map.add( Primitive.TriangleStrip, GL_TRIANGLE_STRIP );
		map.add( Primitive.TriangleFan, GL_TRIANGLE_FAN );
		map.add( Primitive.Quad, GL_QUADS );
		map.add( Primitive.QuadStrip, GL_QUAD_STRIP );
		map.add( Primitive.Polygon, GL_POLYGON );
		
		return map;
	}
	
	private EnumIntMap<Wrap> loadWraps()
	{
		EnumIntMap<Wrap> map = new EnumIntMap<>( Wrap.class );
		
		GLCapabilities caps = GL.getCapabilities();
		
		map.add( Wrap.Edge, caps.OpenGL12 ? GL_CLAMP_TO_EDGE : GL_CLAMP );
		map.add( Wrap.Border, caps.OpenGL13 ? GL_CLAMP_TO_BORDER : GL_CLAMP );
		map.add( Wrap.Default, caps.OpenGL11 ? GL_CLAMP : GL_CLAMP );
		map.add( Wrap.Repeat, caps.OpenGL11 ? GL_REPEAT : GL_REPEAT );
		map.add( Wrap.Mirror, caps.OpenGL14 ? GL_MIRRORED_REPEAT : GL_REPEAT);
		
		return map;
	}
	
	private EnumIntMap<Blend> loadBlendSources()
	{
		EnumIntMap<Blend> map = new EnumIntMap<>( Blend.class );
		
		map.add( Blend.Add, GL_ONE );
		map.add( Blend.AlphaAdd, GL_SRC_ALPHA );
		map.add( Blend.Alpha, GL_SRC_ALPHA );
		map.add( Blend.Color, GL_ONE );
		map.add( Blend.Minus, GL_ONE_MINUS_DST_ALPHA );
		map.add( Blend.PremultAlpha, GL_ONE );
		map.add( Blend.Modulate, GL_DST_COLOR );
		map.add( Blend.Xor, GL_ONE_MINUS_DST_COLOR );
		map.add( Blend.None, GL_ZERO );
		
		return map;
	}
	
	private EnumIntMap<Blend> loadBlendDestinations()
	{
		EnumIntMap<Blend> map = new EnumIntMap<>( Blend.class );
		
		map.add( Blend.Add, GL_ONE );
		map.add( Blend.AlphaAdd, GL_ONE );
		map.add( Blend.Alpha, GL_ONE_MINUS_SRC_ALPHA );
		map.add( Blend.Color, GL_ONE_MINUS_SRC_COLOR );
		map.add( Blend.Minus, GL_DST_ALPHA );
		map.add( Blend.PremultAlpha, GL_ONE_MINUS_SRC_ALPHA );
		map.add( Blend.Modulate, GL_ZERO );
		map.add( Blend.Xor, GL_ZERO );
		map.add( Blend.None, GL_ONE ); 
	
		return map;
	}
	
	private EnumIntMap<ColorOperation> loadColorOperations()
	{
		EnumIntMap<ColorOperation> map = new EnumIntMap<>( ColorOperation.class );
		
		map.add( ColorOperation.Clear, GL_CLEAR );			
		map.add( ColorOperation.Set, GL_SET ); 
		map.add( ColorOperation.Copy, GL_COPY ); 
		map.add( ColorOperation.CopyInverted, GL_COPY_INVERTED );
		map.add( ColorOperation.Noop, GL_NOOP );
		map.add( ColorOperation.Inverted, GL_INVERT );
		map.add( ColorOperation.And, GL_AND );
		map.add( ColorOperation.NAnd, GL_NAND );
		map.add( ColorOperation.Or, GL_OR );
		map.add( ColorOperation.NOr, GL_NOR );
		map.add( ColorOperation.XOr, GL_XOR ); 
		map.add( ColorOperation.Equiv, GL_EQUIV );
		map.add( ColorOperation.AndReverse, GL_AND_REVERSE );
		map.add( ColorOperation.AndInverted, GL_AND_INVERTED );
		map.add( ColorOperation.OrReverse, GL_OR_REVERSE );
		map.add( ColorOperation.OrInverted, GL_OR_INVERTED );
	
		return map;
	}
	
	private EnumIntMap<Magnify> loadMagnify()
	{
		EnumIntMap<Magnify> map = new EnumIntMap<>( Magnify.class );
		
		map.add( Magnify.Nearest, GL_NEAREST );
		map.add( Magnify.Linear, GL_LINEAR );

		return map;
	}
	
	private EnumIntMap<Minify> loadMinify()
	{
		EnumIntMap<Minify> map = new EnumIntMap<>( Minify.class );
		
		map.add( Minify.Nearest, GL_NEAREST );
		map.add( Minify.Linear, GL_LINEAR );
		map.add( Minify.NearestNearest, GL_NEAREST_MIPMAP_NEAREST );
		map.add( Minify.LinearNearest, GL_LINEAR_MIPMAP_NEAREST );
		map.add( Minify.NearestLinear, GL_NEAREST_MIPMAP_LINEAR );
		map.add( Minify.LinearLinear, GL_LINEAR_MIPMAP_LINEAR );
		
		return map;
	}
	
	private EnumIntMap<FogMode> loadFog()
	{
		EnumIntMap<FogMode> map = new EnumIntMap<>( FogMode.class );
		
		map.add( FogMode.Exp, GL_EXP );
		map.add( FogMode.Exp2, GL_EXP2 );
		map.add( FogMode.Linear, GL_LINEAR );
		
		return map;
	}
	
	private EnumIntMap<DataType> loadDataTypes()
	{
		EnumIntMap<DataType> map = new EnumIntMap<>( DataType.class );
		
		map.add( DataType.Byte, GL_BYTE ); 
		map.add( DataType.Ubyte, GL_UNSIGNED_BYTE ); 
		map.add( DataType.Short, GL_SHORT );
		map.add( DataType.Ushort, GL_UNSIGNED_SHORT );
		map.add( DataType.Integer, GL_INT );
		map.add( DataType.Uint, GL_UNSIGNED_INT );
		map.add( DataType.Float, GL_FLOAT );
		map.add( DataType.Double, GL_DOUBLE );
		
		return map;
	}
	
	private EnumIntMap<VertexMeshType> loadVertexBufferTypes()
	{
		EnumIntMap<VertexMeshType> map = new EnumIntMap<>( VertexMeshType.class );
		
		map.add( VertexMeshType.Static, GL_STATIC_DRAW_ARB );
		map.add( VertexMeshType.StaticRead, GL_STATIC_READ_ARB );
		map.add( VertexMeshType.StaticCopy, GL_STATIC_COPY_ARB );
		map.add( VertexMeshType.Dynamic, GL_DYNAMIC_DRAW_ARB );
		map.add( VertexMeshType.DynamicRead, GL_DYNAMIC_READ_ARB );
		map.add( VertexMeshType.DynamicCopy, GL_DYNAMIC_COPY_ARB );
		map.add( VertexMeshType.Stream, GL_STREAM_DRAW_ARB );
		map.add( VertexMeshType.StreamRead, GL_STREAM_READ_ARB ); 
		map.add( VertexMeshType.StreamCopy, GL_STREAM_COPY_ARB );
		
		return map;
	}
	
	private EnumIntMap<ShaderObjectType> loadShaderTypes()
	{
		EnumIntMap<ShaderObjectType> map = new EnumIntMap<>( ShaderObjectType.class );
		
		map.add( ShaderObjectType.Fragment, GL_FRAGMENT_SHADER );
		map.add( ShaderObjectType.Vertex, GL_VERTEX_SHADER );
		map.add( ShaderObjectType.Geometry, GL_GEOMETRY_SHADER );
		map.add( ShaderObjectType.TesselationControl, GL_TESS_CONTROL_SHADER );
		map.add( ShaderObjectType.TesselationEvaluation, GL_TESS_EVALUATION_SHADER );
		
		return map;
	}
	
	public void init()
	{
		this.modes = loadModes();
		this.primitives = loadPrimitives();
		this.wraps = loadWraps();
		this.blendSrc = loadBlendSources();
		this.blendDst = loadBlendDestinations();
		this.colorOps = loadColorOperations();
		this.magnify = loadMagnify();
		this.minify = loadMinify();
		this.fog = loadFog();
		this.dataTypes = loadDataTypes();
		this.vertexTypes = loadVertexBufferTypes();
		this.shaderTypes = loadShaderTypes();
		
		this.initializeModes();
	}
	
	public void initializeModes()
	{
		Mode[] values = Mode.values();
		int count = values.length;
		
		modeEnabled = new boolean[ count ];
		modeLast = new boolean[ count ];
		
		for (int i = 0; i < count; i++)
		{
			Mode mode = values[ i ];
			int cap = this.modes.get( mode );
			
			switch (mode) {
			case DepthMask:
				modeEnabled[ i ] = modeLast[ i ] = glGetBoolean( cap );
				break;
			default: 
				modeEnabled[ i ] = modeLast[ i ] = glIsEnabled( cap );
				break;
			}
		}
	}
	
	public void enable(Mode mode)
	{
		int i = mode.ordinal();
		
		modeLast[ i ] = modeEnabled[ i ];
		
		switch (mode) {
		case DepthMask:
			if (!modeEnabled[ i ]) {
				glDepthMask( true );
				modeEnabled[ i ] = true;
			}
			break;
		default:
			if (!modeEnabled[ i ]) {
				glEnable( modes.get(mode) );
				modeEnabled[ i ] = true;
			}
			break;
		}
	}
	
	public void disable(Mode mode)
	{
		int i = mode.ordinal();
		
		modeLast[ i ] = modeEnabled[ i ];

		switch (mode) {
		case DepthMask:
			if (modeEnabled[ i ]) {
				glDepthMask( false );
				modeEnabled[ i ] = false;
			}
			break;
		case Texture:
			glBindTexture( GL_TEXTURE_2D, 0 );
			TextureLWJGL.currentID = -1;
			// fall through
		default:
			if (modeEnabled[ i ]) {
				glDisable( modes.get( mode ) );
				modeEnabled[ i ] = false;
			}
		}
	}
	
	public void restore( Mode mode )
	{
		int i = mode.ordinal();
		
		if (modeLast[i]) 
		{
			enable( mode );
		} 
		else 
		{
			disable( mode );
		}
	}
	
	public boolean isEnabled( Mode mode )
	{
		return modeEnabled[ mode.ordinal() ];
	}
	
	public void setWrap(Wrap wrap)
	{
		int param = wraps.get( wrap );
		
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, param );
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, param );
	}
	
	public void setBlend( Blend blend )
	{
		int src = blendSrc.get( blend );
		int dst = blendDst.get( blend );
		
		glBlendFunc( src, dst );
	}
	
	public void setColorOperation( ColorOperation coop )
	{
		int param = colorOps.get( coop );
		
		glLogicOp( param );
	}
	
	public void setMagnify( Magnify mag )
	{
		int filter = magnify.get( mag );
		
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter );
	}
	
	public void setMinify( Minify min )
	{
		int filter = minify.get( min );
		
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter );
	}
	
	public void setTile(Tile tile)
	{
		setTexture( tile.texture );
	}
	
	public void setTexture(Texture texture)
	{
		enable( Mode.Texture );
		
		
	}

	@Override
	public ShaderObject newShaderObject( ShaderObjectType type, String code )
	{
		return new ShaderObjectLWJGL( null, type, code );
	}
	
	@Override
	public Shader newShader( ShaderObject ... objects )
	{
		return new ShaderLWJGL( objects );
	}
	
	@Override
	public Shader newShader( String ... objectPaths )
	{
		ShaderObject[] objects = new ShaderObject[ objectPaths.length ];
		
		for (int i = 0; i < objectPaths.length; i++)
		{
			objects[ i ] = Assets.load( objectPaths[ i ] );
		}
		
		return new ShaderLWJGL( objects );
	}

	@Override
	public Shader newShader( Class<Enum<?>> enumClass, ShaderObject ... objects )
	{
		return new ShaderLWJGL( enumClass, objects );
	}
	
	@Override
	public Shader newShader( Class<Enum<?>> enumClass, String ... objectPaths )
	{
		ShaderObject[] objects = new ShaderObject[ objectPaths.length ];
		
		for (int i = 0; i < objectPaths.length; i++)
		{
			objects[ i ] = Assets.load( objectPaths[ i ] );
		}
		
		return new ShaderLWJGL( enumClass, objects );
	}

	@Override
	public VertexMesh newVertexMesh(VertexMeshType type, VertexFormat format) 
	{
		return new VertexMeshLWJGL(type, format);
	}

	@Override
	public VertexBuffer newVertexBuffer(int chunkSizeInBytes, boolean autoExpand) 
	{
		return autoExpand ? new VertexBufferExpandingLWJGL( chunkSizeInBytes ) : new VertexBufferLWJGL( chunkSizeInBytes ); 
	}

	@Override
	public String getVersion()
	{
		return "OpenGL " + glGetString( GL_VERSION ) + " - GLSL " + glGetString( GL_SHADING_LANGUAGE_VERSION );
	}
	
	@Override
	public String getGraphicsCard()
	{
		return glGetString( GL_VENDOR ) + " - " + glGetString( GL_RENDERER );
	}
	
}
