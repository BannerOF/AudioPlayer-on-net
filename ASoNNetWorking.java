import java.net.*;
import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;

public class ASoNNetWorking
{
	private int receivePort; 
	private LinkedBlockingQueue<DatagramPacket> dataBuffer;
	private LinkedBlockingQueue<DatagramPacket> sendBuffer;
	private DatagramSocket receiveSocket;  
	private DatagramSocket sendSocket;  
	private boolean sendFlag;
	private boolean receiveFlag;

	private Thread receive = new Thread(//{{{
		new Runnable()
		{
			public void run()	
			{
				try{
					while(receiveFlag)
					{
						byte[] buf = new byte[1024];
						DatagramPacket temppacket = new DatagramPacket(buf, buf.length);
						receiveSocket.receive(temppacket);				
						dataBuffer.put(temppacket);	
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	); //}}}
	private Thread send = new Thread(//{{{
		new Runnable()
		{
			public void run()	
			{
				try{
					while(sendFlag)
					{
						DatagramPacket tamppacket = sendBuffer.take();
						sendSocket.send(tamppacket);				
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	); //}}}
	public ASoNNetWorking(int receivePort)//{{{
	{
		this.receivePort = receivePort;
		sendFlag = true;
		receiveFlag = true;
		dataBuffer = new LinkedBlockingQueue<DatagramPacket>();
		sendBuffer = new LinkedBlockingQueue<DatagramPacket>();
		try{
			receiveSocket = new DatagramSocket(receivePort);
			sendSocket = new DatagramSocket();
		}catch(Exception e){
			e.printStackTrace();
		}
	}//}}}
	public DatagramPacket getReceivePacket()//{{{
	{ 
		try{
			DatagramPacket temp = dataBuffer.take();
			return temp;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}//}}}
	public void putSendPacket(DatagramPacket packet)//{{{
	{
		try{
			sendBuffer.put(packet);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}//}}}
	public void initReceive()//{{{
	{ receive.start(); }//}}}
	public void initSend()//{{{
	{ send.start(); }//}}}
	public void pauseReceive()//{{{
	{
		try{
			receive.wait();
		}catch(Exception e){
			e.printStackTrace();
		}
	}//}}}
	public void pauseSend()//{{{
	{
		try{
			send.wait();
		}catch(Exception e){
			e.printStackTrace();
		}
	}//}}}
	public void startReceive()//{{{
	{
		try{
			receive.notify();
		}catch(Exception e){
			e.printStackTrace();
		}
	}//}}}
	public void startSend()//{{{
	{
		try{
			send.notify();
		}catch(Exception e){
			e.printStackTrace();
		}
	}//}}}
	public void stopReceive()//{{{
	{
		try{
			receiveFlag = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}//}}}
	public void stopSend()//{{{
	{
		try{
			sendFlag = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}//}}}
	public void clearDataBuf()//{{{
	{ dataBuffer.clear(); }//}}}
	public void clearSendBuffer()//{{{
	{ sendBuffer.clear(); }//}}}
	public int getReceiveBufSize()//{{{
	{ return dataBuffer.size(); }//}}}
	public int getSendBufSize()//{{{
	{ return sendBuffer.size(); }//}}}
}
