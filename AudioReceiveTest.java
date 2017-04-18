import java.io.File;
import java.net.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AudioReceiveTest implements Runnable
{
	private AudioInputStream audioInputStream;
	private AudioFormat audioFormat;
	private SourceDataLine sourceDataLine;
	private DataLine.Info dataLineInfo;
	private ASoNProtocol NetReceive;

	public void run()
	{
		byte[] readbuf = new byte[320];
		try{
			while(true)
			{
				ASoNPacket packet = NetReceive.getData();
				byte[] data = packet.getData();
				sourceDataLine.write(data, 0, data.length);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public AudioReceiveTest(int tPort, int Port, String tAddress)
	{
		try{
			File file = new File("/test.wav");
			audioInputStream = AudioSystem.getAudioInputStream(file);
			audioFormat = audioInputStream.getFormat();
			if(audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED)
			{
				audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,audioFormat.getSampleRate(),
						16,audioFormat.getChannels(),audioFormat.getChannels()*2,audioFormat.getSampleRate(),false);
				audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
			}
			dataLineInfo = new DataLine.Info(
				SourceDataLine.class, audioFormat,
				AudioSystem.NOT_SPECIFIED);
			sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();
			NetReceive.startWorking();
		}catch(Exception e){ e.printStackTrace(); }
	}
	public static void main(String args[])
	{
		AudioReceiveTest ART = new AudioReceiveTest(10010, 10010, "192.168.0.2");
		Thread thread = new Thread(ART);
		thread.start();
	}
}
