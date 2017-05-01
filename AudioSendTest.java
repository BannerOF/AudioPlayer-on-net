import java.io.File;
import java.net.InetAddress;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class AudioSendTest implements Runnable
{
	private ASoNProtocol NetSend; 
	private AudioInputStream audioInputStream;
	private AudioFormat audioFormat;

	public void run()//{{{
	{
		try{
			while(true)
			{
				byte[] readbuf = new byte[2000];
				audioInputStream.read(readbuf, 0, readbuf.length);		
				NetSend.sendData(readbuf);	
				Thread.sleep(0,10);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}//}}}
	public AudioSendTest(int tPort, int Port, String tAddress)//{{{
	{
		try{
			NetSend = new ASoNProtocol(tPort, Port, InetAddress.getByName(tAddress));
			File file = new File("/liangcheng.mp3");
			audioInputStream = AudioSystem.getAudioInputStream(file);
			audioFormat = audioInputStream.getFormat(); 
			if(audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED)
			{
				audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,audioFormat.getSampleRate(),
						16,audioFormat.getChannels(),audioFormat.getChannels()*2,audioFormat.getSampleRate(),false);
				audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
			}
			NetSend.startWorking();
		}catch(Exception e) { e.printStackTrace(); }
	}//}}}
	public static void main(String args[])//{{{
	{
		AudioSendTest AST = new AudioSendTest(10011, 10010, "192.168.0.29");
		Thread thread = new Thread(AST);
		thread.start();	
	}//}}}
}
