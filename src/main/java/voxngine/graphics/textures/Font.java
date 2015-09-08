package voxngine.graphics.textures;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.stb.STBTruetype.stbtt_PackBegin;
import static org.lwjgl.stb.STBTruetype.stbtt_PackEnd;
import static org.lwjgl.stb.STBTruetype.stbtt_PackFontRange;
import static org.lwjgl.stb.STBTruetype.stbtt_PackSetOversampling;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;

import voxngine.utils.FileUtils;

public class Font {
	
	private Texture fontTexture;
	
	private ByteBuffer chardata;
	
	private final float[] scale = {
		24.0f,
		14.0f
	};
	
	private final int BITMAP_W = 512;
	private final int BITMAP_H = 512;
	
	public Font(String fontPath) {
		
		chardata = BufferUtils.createByteBuffer(6 * 128 * STBTTPackedchar.SIZEOF);

		try {
			
			ByteBuffer ttf = FileUtils.ioResourceToByteBuffer(fontPath, 160 * 1024);

			ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);

			ByteBuffer pc = BufferUtils.createByteBuffer(STBTTPackContext.SIZEOF);
			stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, null);
			for ( int i = 0; i < 2; i++ ) {
				chardata.position(((i * 3 + 0) * 128 + 32) * STBTTPackedchar.SIZEOF);
				stbtt_PackSetOversampling(pc, 1, 1);
				stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, 95, chardata);

				chardata.position(((i * 3 + 1) * 128 + 32) * STBTTPackedchar.SIZEOF);
				stbtt_PackSetOversampling(pc, 2, 2);
				stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, 95, chardata);

				chardata.position(((i * 3 + 2) * 128 + 32) * STBTTPackedchar.SIZEOF);
				stbtt_PackSetOversampling(pc, 3, 1);
				stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, 95, chardata);
			}
			stbtt_PackEnd(pc);

			fontTexture = new Texture(BITMAP_W, BITMAP_H, GL_RED, bitmap);
			fontTexture.bind();
			
			glEnable(GL_BLEND);
	        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	public Texture getFontTexture() {
		return fontTexture;
	}

	public int getBitMapW() {
		return BITMAP_W;
	}

	public int getBitMapH() {
		return BITMAP_H;
	}

	public ByteBuffer getChardata() {
		return chardata;
	}

}
