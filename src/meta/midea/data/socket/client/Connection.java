package meta.midea.data.socket.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import meta.midea.data.socket.messages.AbstractMessage;
import meta.midea.data.socket.messages.Message;
import meta.midea.data.socket.messages.MessageFactory;
import meta.midea.data.socket.messages.ParseException;
import meta.midea.data.socket.utils.ByteArrayReader;

/**
 * �ͻ��˳�l�ӵķ�װ�ࡣ
 * <p>
 * ����ʱ�䣺2009-10-28 ����04:17:34
 * @author ����
 * @since 1.0
 */
public class Connection {
	private Socket socket;
	private OutputStream out;
	private InputStream in ;
	private long lastActTime = 0;
	Connection(String host,int port) throws IOException{
		socket = new Socket();
		socket.connect(new InetSocketAddress(host,port));
		in =socket.getInputStream();
		out = socket.getOutputStream();
	}
	Connection(Socket socket) throws IOException{
		this.socket=socket;
		in =socket.getInputStream();
		out = socket.getOutputStream();
	}
	
	private void send0(Message m) throws IOException{
		lastActTime = System.currentTimeMillis();
		out.write(m.getBytes());
		out.flush();
	}
	
	private Message readWithBlock0() throws IOException, ParseException{
		lastActTime = System.currentTimeMillis();
		byte [] header = new byte[AbstractMessage.MessageHeaderLength];
		if(in.read(header)!=AbstractMessage.MessageHeaderLength)
			throw new IOException("δ�ܶ�ȡ����İ�ͷ����");
		ByteArrayReader bar = new ByteArrayReader(header);
		int len = bar.readInt();
		if(len<0)throw new ParseException("����İ����Ϣ");
		int type = bar.readInt();
		byte [] cache = new byte [len];
		System.arraycopy(header, 0, cache, 0, header.length);
		if(in.read(cache, header.length, len-header.length)!=len-header.length)
			throw new IOException("δ�ܶ�ȡ����İ��岿��");
		Message m = MessageFactory.getInstance(type);
		m.parse(cache);
		return m;
	}
	/**
	 * ���ڷ�����ݰ����ڰ�ͷȱ�����кţ����ԣ�������̣�ÿһ�׶α���ȵ���һ�׶�Ӧ����յ����ܷ����´ε������
	 * @param m ���͵���Ϣ��
	 * @return  ��Ӧ��Ӧ���
	 * @throws IOException
	 * @throws ParseException
	 */
	public synchronized Message send(Message m) throws IOException, ParseException{
		send0(m);
		return readWithBlock0();
	}
	public synchronized void close() throws IOException{
		lastActTime = System.currentTimeMillis();
		ConnectionManager.removeConnection(this);
		if(socket!=null)socket.close();
		if(in!=null)in.close();
		if(out!=null)out.close();
	}
	
	public synchronized long getLastActTime(){
		return lastActTime;
	}
}