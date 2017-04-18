import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.net.*;

public class ASoNProtocol
{
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
		byte[] data = new byte[buf.length+4];	
		data[0] = (byte) (tempserial & 0xff);    
		data[1] = (byte) (tempserial >> 8 & 0xff);    
		data[2] = (byte) (tempserial >> 16 & 0xff);    
		data[3] = (byte) (tempserial >> 24 & 0xff);    
		System.arraycopy(buf, 0, data, 4, buf.length);
		return data;
	}//}}}
	protected ASoNPacket byte2ASoNPacket(byte[] data)//{{{
	{
		int tempserial = (((data[3] & 0xff) << 24)      
			| ((data[2] & 0xff) << 16)      
			| ((data[1] & 0xff) << 8)  
			| ((data[0] & 0xff) << 0));  
		byte[] tempdata = new byte[data.length-4];	
		System.arraycopy(data, 4, tempdata, 0, tempdata.length);
		ASoNPacket tempPacket = new ASoNPacket(tempserial, tempdata);
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
	}//}}}
	public void sendData(byte[] data)//{{{
	{
		ASoNPacket tempASoNPacket = new ASoNPacket(serial, data);		
		inputList.put(tempASoNPacket);
		serial++;
	}//}}}
	public ASoNPacket getData() //{{{
	{
		try{
			return outputList.take(); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}//}}}
}
