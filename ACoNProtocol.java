import javax.sound.sampled.AudioFormat;
import java.net.InetAddress;

public class ACoNProtocol extends ASoNProtocol
{
	/**
	 *	ACoNProtocol - Header
	 *	+----------------------------------+
	 *	|	 6 byte ASoNProtocol Header    | 
	 *	+----------------------------------+
	 *	|   1 byte ACoNProtocol CmdCode    |
	 *	+----------------------------------+
	 *	|		      CmdParam             |
	 *	+----------------------------------+
	 *
	 *	ACoNProtocol - Cmd: AudioFormat
	 *	+---------------------------------------------+
	 *	|                 Header                      |
	 *	+-------------------+-------------------------+
	 *	| 4 byte sampleRate | 4 byte sampleSizeInBits |
	 *	+-------------------+-------------------------+
	 *	|  4 byte channels  |     4 byte frmeSize     | 
	 *	+-------------------+-------------------------+
	 *	| 4 byte frameRate  |     1 byte bigEndian    |
	 *	+-------------------+-------------------------+
	 *
	 */
	public static final byte CMD_AUDIOFORMAT = 0x01;
	public static final byte CMD_COMMON = 0x0f;
	public interface cmdListener
	{
		void onReceiveCMD_AudioFormat(AudioFormat AF);
		void onReceiveCMD_Common(byte[] param);
	}
	private cmdListener mycmdListener;

	private Thread readCMD = new Thread(//{{{
			new Runnable()
			{
				public void run()
				{
					try{
						while(true)
						{
							ASoNPacket tempASoNPacket = getData();
							byte[] tempdata = tempASoNPacket.getData();
							byte[] temp = new byte[tempdata.length-1];
							System.arraycopy(tempdata, 1, temp, 0, tempdata.length-1);
							Cmd2Action(tempdata[0], temp);
						}
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		);//}}}
	public ACoNProtocol(int sendPort, int receivePort, InetAddress sendAddress, cmdListener Listener)//{{{
	{
		super(sendPort, receivePort, sendAddress);
		mycmdListener = Listener;
	}//}}}
	private void Cmd2Action(byte Cmd, byte[] param)//{{{
	{
		switch(Cmd)
		{
			case CMD_AUDIOFORMAT : 
				mycmdListener.onReceiveCMD_AudioFormat(byte2AudioFormat(param));
				break;
			case CMD_COMMON :
				mycmdListener.onReceiveCMD_Common(param);
				break;
		}
	}//}}}
	private AudioFormat byte2AudioFormat(byte[] data)//{{{
	{
		float sampleRate = byte2float(data, 0);
		int sampleSizeInBits = byte2int(data, 4);
		int channels = byte2int(data, 8);
		int frmeSize = byte2int(data, 12);
		float frameRate = byte2float(data, 16);
		boolean bigEndian = data[20] == (byte)0x01;
		byte[] temp = new byte[data.length-21];
		System.arraycopy(data, 21, temp, 0, data.length-21);
		String encoding = new String(temp);
		//System.out.println(sampleRate);
		//System.out.println(sampleSizeInBits);
		//System.out.println(channels);
		//System.out.println(frmeSize);
		//System.out.println(frameRate);
		//System.out.println(bigEndian);
		//System.out.println(encoding);
		return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
							   sampleRate,
							   sampleSizeInBits,
							   channels,
							   frmeSize,
							   frameRate,
							   bigEndian);
		//return new AudioFormat(new AudioFormat.Encoding(encoding),
							   //sampleRate,
							   //sampleSizeInBits,
							   //channels,
							   //frmeSize,
							   //frameRate,
							   //bigEndian);
	}//}}}
	public void sendCMD_AudioFormat(AudioFormat AF)//{{{
	{	
		String encoding = AF.getEncoding().toString();
		float sampleRate = AF.getSampleRate();
		int sampleSizeInBits = AF.getSampleSizeInBits();
		int channels = AF.getChannels();
		int frmeSize = AF.getFrameSize(); 
		float frameRate = AF.getFrameRate();
		boolean bigEndian = AF.isBigEndian();
		byte[] data = new byte[100];
		data[0] = CMD_AUDIOFORMAT;
		float2byte(sampleRate, data, 1);
		int2byte(sampleSizeInBits, data, 5);
		int2byte(channels, data, 9);
		int2byte(frmeSize, data, 13);
		float2byte(frameRate, data, 17);
		data[21] = bigEndian ? (byte)0x01 : (byte)0x00;
		byte[] temp = encoding.getBytes();
		System.arraycopy(temp, 0, data, 22, temp.length);
		//System.out.println(encoding);
		//System.out.println(sampleRate);
		//System.out.println(sampleSizeInBits);
		//System.out.println(channels);
		//System.out.println(frmeSize);
		//System.out.println(bigEndian);
		sendData(data);
	}//}}}
	public void startWorking()//{{{
	{
		super.startWorking();
		readCMD.start();
	}//}}}
	private void float2byte(float f, byte[] dest, int index)//{{{
	{
		int fbit = Float.floatToIntBits(f);
		byte[] b = new byte[4];
		for(int i = 0; i<4; i++)
			b[i] = (byte)(fbit >> (24-i*8));
		System.arraycopy(b, 0, dest, index, 4);
		byte temp;
		temp = dest[index+0];
		dest[index+0] = dest[index+3];
		dest[index+3] = temp;
		temp = dest[index+1];
		dest[index+1] = dest[index+2];
		dest[index+2] = temp;
	}//}}}
	private float byte2float(byte[] b, int index)//{{{
	{
		int I;
		I = b[index+0];
		I &= 0xff;
		I |= ((long)b[index+1]<<8);
		I &= 0xffff;
		I |= ((long)b[index+2]<<16);
		I &= 0xffffff;
		I |= ((long)b[index+3]<<24);
		return Float.intBitsToFloat(I);
	}//}}}
	private void int2byte(int i, byte[] data, int index)//{{{
	{
		data[index+0] = (byte) (i & 0xff);    
		data[index+1] = (byte) (i >> 8 & 0xff);    
		data[index+2] = (byte) (i >> 16 & 0xff);    
		data[index+3] = (byte) (i >> 24 & 0xff);    
	}//}}}
	private int byte2int(byte[] data, int index)//{{{
	{
		int i = (((data[3+index] & 0xff) << 24)      
			| ((data[2+index] & 0xff) << 16)      
			| ((data[1+index] & 0xff) << 8)  
			| ((data[0+index] & 0xff) << 0));  
		return i;
	}//}}}
}
