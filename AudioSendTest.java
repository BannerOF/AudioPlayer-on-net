import java.io.File;
import java.net.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AudioSendTest
{
	private ASoNProtocol NetSend; 

	public AudioSendTest(int tPort, int Port, String tAddress)
	{
		try{
			NetSend = new ASoNProtocol(tPort, Port, InetAddress.getByName(tAddress));
		}catch(Exception e) { e.printStackTrace(); }
	}
	public static void main(String args[])
	{

	}
}
