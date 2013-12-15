package main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import model.Model;
import model.Rectangle;
import model.TextModel;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;


public class BitmapFont {
	public int texture;
	Font font;
	FontRenderContext rendCxt;
	final int texCharW = 25;
	final int texCharH = 25;
	BitmapFont(Font fnt) {
		rendCxt = new FontRenderContext(null, false, false);
		BufferedImage texBuf = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
		Graphics2D texGfx = (Graphics2D)texBuf.getGraphics();
		ByteBuffer pixels;
		char[] curCh = new char[1];
		int ch = 32;
		int x,y;
		font = fnt;
		texGfx.setColor(Color.black);
		texGfx.fillRect(0, 0, 256, 256);
		texGfx.setColor(Color.white);
		//texGfx.fillRect(100, 100, 206, 206);
		for (y = 0;y < 10;y++) {
			for (x = 0;x < 10;x++) {
				curCh[0] = (char)ch;
				texGfx.fill(font.layoutGlyphVector(rendCxt, curCh, 0, 1, 0).getGlyphOutline(0,x*texCharW,(y+1)*texCharH-texCharH/4));
				ch++;
			}
		}
		pixels = TexUtils.imageToBytes(texBuf,256);
		texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 256, 256, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
	}
	Float[] getCharTex(char ch,double chW) {
		int gridX = (ch-32)%10;
		int gridY = (ch-32)/10;
		float texX=(float)gridX*texCharW/256.0f;
		float texY=1.0f-(float)gridY*texCharH/256.0f;
		float texW=(float)chW*texCharW/256.0f;
		float texH=(float)texCharH/256.0f;
		Float[] retval = {texX,texY,texX+texW,texY,texX,texY-texH,
						texX+texW,texY,texX,texY-texH,texX+texW,texY-texH};
		return retval;
	}
	public TextModel getStringModel(String str) {
		int i;
		TextModel strModel = new TextModel(0.0,0.05);
		double x = 0.0;
		double chW;
		char[] curCh = new char[1];
		for (i=0;i < str.length();i++) {
			curCh[0]=str.charAt(i);
			chW = font.getStringBounds(curCh, 0, 1, rendCxt).getWidth()/texCharW;
			Model ch = new Rectangle(chW*0.1,0.1);
			ch.texVectRaw.addAll(Arrays.asList(getCharTex(str.charAt(i),chW)));
			ch.move(x,0.0,0.0);
			strModel.add(ch);
			x+=chW*0.1;
		}
		strModel.centerX=x/2.0;
		return strModel;
	}
}
