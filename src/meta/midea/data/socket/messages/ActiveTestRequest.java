package  meta.midea.data.socket.messages;

import meta.midea.data.socket.utils.ByteArrayBuilder;

/**
 * ����������
 * <p>
 * ����ʱ�䣺2009-10-28 ����02:48:36
 * @author ����
 * @since 1.0
 */
public class ActiveTestRequest extends AbstractMessage implements Message {

	/* ���� Javadoc��
	 * @see houlei.net.keepconn.messages.Message#getBytes()
	 */
	public byte[] getBytes() {
		return new ByteArrayBuilder().write(MessageHeaderLength).write(ActiveTestRequest).toBytes();
	}

	/* ���� Javadoc��
	 * @see houlei.net.keepconn.messages.Message#getMessageLength()
	 */
	public int getMessageLength() {
		return MessageHeaderLength;
	}

	/* ���� Javadoc��
	 * @see houlei.net.keepconn.messages.Message#getMessageType()
	 */
	public int getMessageType() {
		return ActiveTestRequest;
	}

	/* ���� Javadoc��
	 * @see houlei.net.keepconn.messages.Message#parse(byte[])
	 */
	public void parse(byte[] b) throws ParseException {
		//�հ��壬���Կպ����塣
	}

}