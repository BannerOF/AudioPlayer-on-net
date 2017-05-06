import java.net.InetAddress;
import java.lang.Comparable;


public class ASoNPacket implements Comparable<ASoNPacket>
{
	private int serial;
	private short length;
	private byte[] data;
	private InetAddress address;
	private int Port;

	public ASoNPacket(int serial, short length, byte[] buf)//{{{
	{
		this.serial = serial;
		this.length = length;
		this.data = buf;
	}//}}}
	public void setAddress(InetAddress address)//{{{
	{
		this.address = address;
	}//}}}
	public void setPort(int port)//{{{
	{
		this.Port = port;
	}//}}}
	public InetAddress getAddress()//{{{
	{ return address; }//}}}
	public int getPort()//{{{
	{ return Port; }//}}}
	public int getHeader_serial()	//{{{
	{ return serial; }		//}}}
	public short getHeader_length()	//{{{
	{ return length; }		//}}}
	public int compareTo(ASoNPacket a)//{{{
	{
		if(this.getHeader_serial() < a.getHeader_serial())		
			return -1;
		else
			return 1;
	}//}}}
	public byte[] getData()//{{{
	{ return data; }//}}}
}
