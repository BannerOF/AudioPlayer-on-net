import java.io.File;
import java.net.InetAddress;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class AudioCMDSendTest implements Runnable, ACoNProtocol.cmdListener
{
	private ACoNProtocol CMDSender;
	private AudioFormat audioFormat;
	private AudioInputStream audioInputStream;

	public void run()//{{{
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
			CMDSender.sendCMD_AudioFormat(audioFormat);	
		}catch(Exception e) { e.printStackTrace(); }
	}//}}}
	public AudioCMDSendTest(int tPort, int Port, String tAddress)//{{{
	{
		try{
			CMDSender = new ACoNProtocol(tPort, Port, InetAddress.getByName(tAddress), this);
		}catch(Exception e) { e.printStackTrace(); }
	}//}}}
	public static void main(String args[])//{{{
	{
		AudioCMDSendTest ACMDST = new AudioCMDSendTest(10011, 10011, "192.168.0.20");
		Thread thread = new Thread(ACMDST);
		thread.start();
	}//}}}
	public void onReceiveCMD_Common(byte[] param) { }
	public void onReceiveCMD_AudioFormat(AudioFormat AF) { }
}
