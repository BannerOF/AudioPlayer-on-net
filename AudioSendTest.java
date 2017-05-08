import java.io.File;
import java.net.InetAddress;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class AudioSendTest implements Runnable,ACoNProtocol.cmdListener
{
	private ASoNProtocol NetSend; 
	private ACoNProtocol NetControl;
	private SourceDataLine sourceDataLine;
	private DataLine.Info dataLineInfo;
	private AudioInputStream audioInputStream;
	private AudioFormat audioFormat;

	public void run()//{{{
	{
		try{
			NetControl.sendCMD_AudioFormat(audioFormat);
			while(true)
			{
				byte[] readbuf = new byte[1000];//this for long range transmitting
				//byte[] readbuf = new byte[320];// this for short range transmitting
				if (audioInputStream.read(readbuf, 0, readbuf.length) == -1)
				{
					System.out.println("Sending Over");
					//TODO
					//change to real endCMD
					NetControl.sendCMD_Common(new byte[0]);//send end CMD;
					break;
				}
				sourceDataLine.write(readbuf, 0, readbuf.length);
				NetSend.sendData(readbuf);	
				Thread.sleep(0,90);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}//}}}
	public AudioSendTest(int sPort, int cPort, String tAddress)//{{{
	{
		try{
			NetSend = new ASoNProtocol(sPort, sPort+10, InetAddress.getByName(tAddress));
			NetControl = new ACoNProtocol(cPort, cPort+10, InetAddress.getByName(tAddress), this);

			File file = new File("/liangcheng.mp3");
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
			NetSend.startWorking();
			NetControl.startWorking();
		}catch(Exception e) { e.printStackTrace(); }
	}//}}}
public static void main(String args[])//{{{
{
	//AudioSendTest AST = new AudioSendTest(10010, 10011, "27.18.115.207");
	AudioSendTest AST = new AudioSendTest(10010, 10011, "127.0.0.1");
	Thread thread = new Thread(AST);
	thread.start();	
}//}}}
	public void onReceiveCMD_AudioFormat(AudioFormat audioFormat) { }
	public void onReceiveCMD_Common(byte[] param) {//{{{
		int lostnum = (((param[3] & 0xff) << 24)      
			| ((param[2] & 0xff) << 16)      
			| ((param[1] & 0xff) << 8)  
			| ((param[0] & 0xff) << 0));  
		System.out.println("lost rate: "+lostnum*100/NetSend.getSerial()+"%");
	}//}}}
}
