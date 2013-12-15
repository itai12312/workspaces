package main;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TexUtils {
	public static int radioactive;
	public static void init() throws IOException {
		radioactive = loadTexture("texture/radio.png");
	}
	static ByteBuffer imageToBytes(BufferedImage img,int side) {
		BufferedImage rgbImage = new BufferedImage(side, side, BufferedImage.TYPE_INT_ARGB);
		ByteBuffer bytes = BufferUtils.createByteBuffer(side*side*4);
		IntBuffer ints = bytes.asIntBuffer();
		int[] line = new int[side];
		int[] samples = new int[side*side];
		img.getRGB(0, 0, side, side, samples, 0, img.getWidth());
		rgbImage.setRGB(0, 0, side, side, samples, 0, img.getWidth());
		int i;
		for (i=side-1;i >= 0;i--) {
			rgbImage.getData().getDataElements(0, i, side, 1, line);
			ints.put(line);
		}
		return bytes;
	}
	public static int loadTexture(String path) throws IOException {
		BufferedImage tex = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
		int texNum = GL11.glGenTextures();
		ByteBuffer pixels = imageToBytes(tex,tex.getWidth());
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texNum);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tex.getWidth(), tex.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
		return texNum;
	}
}
