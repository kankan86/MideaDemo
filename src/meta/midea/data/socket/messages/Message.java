package  meta.midea.data.socket.messages;

/**
 *	������ݰ��Ӧ�����߳���<br/>
 *  һ����ݰ�ְ�ͷ�Ͱ���}��֣���ͷһ�㺬��ȺͰ����͵���Ϣ�������Ǳ�������ص����ݡ�<br/>
 *  �����У���ͷ�Ͱ�Ⱥ�����}����Ϣ���ֱ�ռ4�ֽڿռ䡣
 * <p>
 * ����ʱ�䣺2009-10-28 ����02:12:43
 * @author ����
 * @since 1.0
 */
public interface Message {
	/**
	 * ������ݰ����ͣ�����������
	 */
	public static final int ActiveTestRequest		= 0x00000001;
	/**
	 * ������ݰ����ͣ������Ӧ���
	 */
	public static final int ActiveTestResponse	= 0x80000001;
	/**
	 *  ������ݰ����ͣ���½�������
	 */
	public static final int LoginRequest		= 0x00000002;
	/**
	 * ������ݰ����ͣ���½��Ӧ���
	 */
	public static final int LoginResponse		= 0x80000002;
	/**
	 *  ������ݰ����ͣ��ǳ�������
	 */
	public static final int LogoutRequest		= 0x00000003;
	/**
	 * ������ݰ����ͣ��ǳ��Ӧ���
	 */
	public static final int LogoutResponse	= 0x80000003;
	/*
	 * ���ﻹ�������������Ϣ�������
	 * */
	/**
	 * ��ȡ��ݰ���ܳ��ȣ���(��ͷ�Ӱ���ĳ��ȣ�
	 * @return��ݰ���ܳ���
	 */
	public abstract int getMessageLength();
	/**
	 * ��ȡ��ݰ������
	 * @return��ݰ������
	 */
	public abstract int getMessageType();
	/**
	 * ������ݰ�����ݡ�ʹ�ֽ���ת��������󡣸÷�����Ҫ��ɶ԰���Ľ����̡�
	 * @param b �ֽ����飨�ֽ���
	 * @throws ParseException ������ʱ�����쳣ʱ�׳�
	 */
	public abstract void parse(byte [] b) throws ParseException;
	/**
	 * ������������ת�����ֽ�����
	 * @return ������Ӧ���ֽ�����
	 */
	public abstract byte [] getBytes();
}