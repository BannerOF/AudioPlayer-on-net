import java.util.concurrent.PriorityBlockingQueue;
import java.net.*;

public class ASoNProtocol
{
	public final static int HEADLENGTH = 6;
	/**
	 
	ASoNPacket
	+-----------------+-----------------+
	|  32 bit serial  |  16 bit length  |
	+-----------------+-----------------+
	|                                   |
	|               data                |
	|            0~32767 bit            |
	|                                   |
	+-----------------------------------+

	*/
	protected int sendPort;
	protected InetAddress sendAddress;
	protected ASoNNetWorking netWorking;
	protected int serial = 0;
	protected PriorityBlockingQueue<ASoNPacket> inputList = new PriorityBlockingQueue<ASoNPacket>();
	protected PriorityBlockingQueue<ASoNPacket> outputList = new PriorityBlockingQueue<ASoNPacket>();
	
	protected Thread writeSend = new Thread(//{{{
		new Runnable()
		{
			public void run()
			{
				try{
					while(true)
					{
						ASoNPacket tempASoNPacket = inputList.take();
						byte[] data = ASoNPacket2byte(tempASoNPacket);					
						DatagramPacket tempDatagramPacket =
							new DatagramPacket(data, data.length, tempASoNPacket.getAddress(), tempASoNPacket.getPort());
						netWorking.putSendPacket(tempDatagramPacket);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	);//}}}
	protected Thread readReceive = new Thread(//{{{
		new Runnable()
		{
			public void run()
			{
				try{
					while(true)
					{
						DatagramPacket tempPacket = netWorking.getReceivePacket();
						ASoNPacket tempASoNPacket = byte2ASoNPacket(tempPacket.getData());
						tempASoNPacket.setAddress(tempPacket.getAddress());
						tempASoNPacket.setPort(tempPacket.getPort());
						outputList.put(tempASoNPacket);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	);//}}}
	protected byte[] ASoNPacket2byte(ASoNPacket tempPacket)//{{{
	{
		byte[] buf = tempPacket.getData();
		int tempserial = tempPacket.getHeader_serial();
		short templength = tempPacket.getHeader_length();
		byte[] data = new byte[buf.length+HEADLENGTH];	

		data[0] = (byte) (tempserial & 0xff);    
		data[1] = (byte) (tempserial >> 8 & 0xff);    
		data[2] = (byte) (tempserial >> 16 & 0xff);    
		data[3] = (byte) (tempserial >> 24 & 0xff);    

		data[4] = (byte) (templength >> 0);
		data[5] = (byte) (templength >> 8);
		System.arraycopy(buf, 0, data, HEADLENGTH, buf.length);
		return data;
	}//}}}
	protected ASoNPacket byte2ASoNPacket(byte[] data)//{{{
	{
		int tempserial = (((data[3] & 0xff) << 24)      
			| ((data[2] & 0xff) << 16)      
			| ((data[1] & 0xff) << 8)  
			| ((data[0] & 0xff) << 0));
		short templength = (short)((data[5]<<8) | data[4] & 0xff);
		byte[] tempdata = new byte[templength];	
		System.arraycopy(data, HEADLENGTH, tempdata, 0, tempdata.length);
		ASoNPacket tempPacket = new ASoNPacket(tempserial, templength, tempdata);
		return tempPacket;
	}//}}}
	public ASoNProtocol(int sendPort, int receivePort, InetAddress sendAddress)//{{{
	{
		netWorking = new ASoNNetWorking(receivePort);
		this.sendPort = sendPort;
		this.sendAddress = sendAddress;
	}//}}}
	public void startWorking()//{{{
	{
		netWorking.initSend();
		netWorking.initReceive();
		writeSend.start();
		readReceive.start();
	}//}}}
	public void sendData(byte[] data)//{{{
	{
		ASoNPacket tempASoNPacket = new ASoNPacket(serial, (short)data.length, data);		
		tempASoNPacket.setPort(sendPort);
		tempASoNPacket.setAddress(sendAddress);
		inputList.put(tempASoNPacket);
		serial++;
	}//}}}
	public ASoNPacket getData() //{{{
	{
		try{
			//System.out.println(outputList.size());
			return outputList.take(); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}//}}}
	public int getSerial() //{{{
	{ return serial; }//}}}
}
