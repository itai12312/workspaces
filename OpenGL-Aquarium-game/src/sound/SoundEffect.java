package sound;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundEffect {
	String path;
	public SourceDataLine dataLine;
	public AudioInputStream audioStream;
	public int playCount;
	public int playLimit = 2;
	public SoundEffect(String p) {
		path = p;
		playCount = 0;
	}
	public void play() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		byte[] buffer = new byte[500000];
		if (playCount < playLimit) {
			playCount++;
			InputStream s = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			s.read(buffer);
			ByteArrayInputStream is = new ByteArrayInputStream(buffer);
			audioStream = AudioSystem.getAudioInputStream(is);//Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
			AudioFormat fmt = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, fmt);
			dataLine = (SourceDataLine)AudioSystem.getLine(info);
			new PlayThread(dataLine,audioStream,this).start();
		}
	}
}
