import java.net.*;
import java.io.*;
import java.lang.Comparable;


public class ASoNPacket implements Comparable<ASoNPacket>
{
	private int serial;
	private byte[] data;
	private InetAddress address;
	private int Port;

	public ASoNPacket(int serial, byte[] buf)//{{{
	{
		this.serial = serial;
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
