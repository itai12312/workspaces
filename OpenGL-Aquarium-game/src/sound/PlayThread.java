package sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;

public class PlayThread extends Thread {
	SourceDataLine dataLine;
	AudioFormat format;
	AudioInputStream inputStream;
	SoundEffect effect;
	byte[] buf;
	public PlayThread(SourceDataLine dl,AudioInputStream is,SoundEffect eff) {
		super();
		System.out.println("creat");
		dataLine = dl;
		inputStream = is;
		format = is.getFormat();
		buf = new byte[10000];
		effect = eff;
	}
	public void run() {
		try {
			dataLine.open(format);
			dataLine.start();
			int cnt;

		      while((cnt = inputStream.read(
		           buf,0,buf.length)) != -1){
		        if(cnt > 0){
		          dataLine.write(
		                             buf, 0, cnt);
		        }
		      }
		      dataLine.drain();
		      dataLine.close();
		      effect.playCount--;
		      
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
