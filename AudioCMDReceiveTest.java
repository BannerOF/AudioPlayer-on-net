import java.io.File;
import java.net.InetAddress;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class AudioCMDReceiveTest implements ACoNProtocol.cmdListener
{
	private ACoNProtocol CMDReceiver;

	public void AudioReceiveTest()//{{{
	{
		try{
			CMDReceiver = new ACoNProtocol(10011, 10011, InetAddress.getByName("192.168.0.2"), this);
			CMDReceiver.startWorking();
		}catch(Exception e) { e.printStackTrace(); }
	}//}}}
	public static void main(String args[])//{{{
	{
		AudioCMDReceiveTest test = new AudioCMDReceiveTest();
	}//}}}
	public void onReceiveCMD_Common(byte[] param) { }
	public void onReceiveCMD_AudioFormat(AudioFormat AF) { }
}
