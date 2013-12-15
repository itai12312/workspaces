package sound;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundMgr {
	public static SoundEffect bite;
	public static void init() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		bite = new SoundEffect("sound/bite.wav");
	}
}
