import java.io.File;
import java.net.InetAddress;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class AudioReceiveTest implements Runnable, ACoNProtocol.cmdListener
{
	private AudioFormat audioFormat;
	private SourceDataLine sourceDataLine;
	private DataLine.Info dataLineInfo;
	private ASoNProtocol NetReceive;
	private ACoNProtocol NetControl;

	public void run()//{{{
	{
		try{
			//int temp = -1;
			while(true)
			{
				ASoNPacket packet = NetReceive.getData();
				byte[] data = packet.getData();
				//int serial = packet.getHeader_serial();
				//if(serial != temp + 1)
				//{
					//System.out.println("====");
					//System.out.println("serial:"+serial);
					//System.out.println("drop:"+(serial-temp));
				//}
				//temp = serial;
				sourceDataLine.write(data, 0, data.length);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}//}}}
	public AudioReceiveTest(int sPort, int cPort, String tAddress)//{{{
	{
		try{
			NetReceive = new ASoNProtocol(sPort+10, sPort, InetAddress.getByName(tAddress));
			NetControl = new ACoNProtocol(cPort+10, cPort, InetAddress.getByName(tAddress), this);
			NetReceive.startWorking();
			NetControl.startWorking();
		}catch(Exception e){ e.printStackTrace(); }
	}//}}}
	public static void main(String args[])//{{{
	{
		//AudioReceiveTest ART = new AudioReceiveTest(10010, 10011, "171.113.100.102");
		AudioReceiveTest ART = new AudioReceiveTest(10010, 10011, "127.0.0.1");
	}//}}}
	public void onReceiveCMD_AudioFormat(AudioFormat audioFormat) {//{{{
		try {
			this.audioFormat = audioFormat;
			dataLineInfo = new DataLine.Info(
				SourceDataLine.class, audioFormat,
				AudioSystem.NOT_SPECIFIED);
			sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();
			Thread player = new Thread(this);
			player.start();
		} catch(Exception e){
			e.printStackTrace();
		}
	}//}}}
	public void onReceiveCMD_Common(byte[] param) { }
}
