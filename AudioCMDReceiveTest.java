import java.io.File;
import java.net.InetAddress;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class AudioCMDReceiveTest implements ACoNProtocol.cmdListener,Runnable
{
	private ACoNProtocol CMDReceiver;
	private SourceDataLine sourceDataLine;
	private AudioInputStream audioInputStream;

	public AudioCMDReceiveTest()//{{{
	{
		try{
			CMDReceiver = new ACoNProtocol(10010, 10011, InetAddress.getByName("127.0.0.1"), this);
			CMDReceiver.startWorking();
		}catch(Exception e) { e.printStackTrace(); }
	}//}}}
	public static void main(String args[])//{{{
	{
		AudioCMDReceiveTest test = new AudioCMDReceiveTest();
	}//}}}
	public void run() {//{{{
		try {
			while(true )
			{
				byte[] readbuf = new byte[2000];
				audioInputStream.read(readbuf, 0, readbuf.length);		
				sourceDataLine.write(readbuf, 0, readbuf.length);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}//}}}
	public void onReceiveCMD_AudioFormat(AudioFormat AF)//{{{
	{
		try {
			File file = new File("/test.wav");
			audioInputStream = AudioSystem.getAudioInputStream(file);
			DataLine.Info dataLineInfo;
			dataLineInfo = new DataLine.Info(
				SourceDataLine.class, AF,
				AudioSystem.NOT_SPECIFIED);
			sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(AF);
			sourceDataLine.start();
			Thread ACMDR = new Thread(this );
			ACMDR.start();
		} catch(Exception e){
			e.printStackTrace();
		}
	}//}}}
	public void onReceiveCMD_Common(byte[] param) { }
}
