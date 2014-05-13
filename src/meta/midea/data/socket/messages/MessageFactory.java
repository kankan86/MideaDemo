package meta.midea.data.socket.messages;

/**
 *
 * <p>
 * ����ʱ�䣺2009-10-28 ����04:37:53
 * @author ����
 * @since 1.0
 */
public class MessageFactory {
	public static Message getInstance(int messageType){
		Message m = null;
		switch(messageType){
		case Message.ActiveTestRequest : m= new ActiveTestRequest();break;
		case Message.ActiveTestResponse : m= new ActiveTestResponse();break;
		default:throw new RuntimeException("���������ݰ���δ���ṩ");
		}
		return m;
	}
}