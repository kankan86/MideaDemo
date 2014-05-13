package  meta.midea.data.socket.messages;

/**
 *	������ݰ�����쳣ʱ�׳�
 * <p>
 * ����ʱ�䣺2009-10-28 ����02:42:16
 * @author ����
 * @since 1.0
 */
public class ParseException extends Exception {

	private static final long serialVersionUID = 1L;
	public ParseException() {
	}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(Throwable cause) {
		super(cause);
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

}