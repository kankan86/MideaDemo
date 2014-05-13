package meta.midea.tool;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class MultiCastSSDP {
	public final int RECEIVE_LENGTH = 1024;
	public final String ssdpgroup = "239.255.255.250";
	public final int ssdpPort = 2000;
	public final int TTLTime = 4;
	public boolean isValid = false;
	public MulticastSocket multiSocket;
	public InetAddress group;
	public boolean broadcast_valid = false; // false
	public boolean multicast_valid = true; // true
	public DatagramSocket udpSocket;
	public InetAddress broadcastAddr;
	public DatagramPacket dp;
	
	private Context myContent;

	// @SuppressLint("ParserError")
	public ArrayList<String> MultiCastSSDPClient(String ip, String mac)
			throws Exception {
		ArrayList<String> IpAddrList = new ArrayList<String>(0);
		String macAddr = mac;
		// Log.i("yqi", "MultiCastSSDPClient, ip :" + ip);
		if (multicast_valid) {
			group = InetAddress.getByName(ssdpgroup);
			multiSocket = new MulticastSocket(ssdpPort);
			multiSocket.setSoTimeout(5000);
			multiSocket.setLoopbackMode(true);
			multiSocket.joinGroup(group);
		}
		if (broadcast_valid) {
			broadcastAddr = InetAddress.getByName("255.255.255.255");
			udpSocket = new DatagramSocket(ssdpPort);
			udpSocket.setSoTimeout(5000);
			udpSocket.setBroadcast(true);
		}

		JSONObject info = new JSONObject();
		info.put("type", "SCS-DISCOVER");
		info.put("ip", ip);
		info.put("hostname", "Host-SCS");
		info.put("mac", macAddr);

		// {"type":"SCS-DISCOVER","ip":"10.3.1.53","hostname":"Host-D9B909","mac":"ABCDEFD9B909"}
		byte[] sendMSG = (info.toString()).getBytes();
		if (multicast_valid) {
			dp = new DatagramPacket(sendMSG, sendMSG.length, group, ssdpPort);
			String sendstr = new String(dp.getData()).trim();
			multiSocket.send(dp);
			System.out.println("yqi sendstr" + sendstr);
		}
		if (broadcast_valid) {
			dp = new DatagramPacket(sendMSG, sendMSG.length, broadcastAddr,
					ssdpPort);
			String sendstr = new String(dp.getData()).trim();
			udpSocket.send(dp);
			System.out.println("yqi sendstr" + sendstr);
		}

		while (true) {
			DatagramPacket rcvdp = new DatagramPacket(new byte[RECEIVE_LENGTH],
					RECEIVE_LENGTH);
			try {
				if (multicast_valid)
					multiSocket.receive(rcvdp);
				if (broadcast_valid)
					udpSocket.receive(rcvdp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (multicast_valid) {
					multiSocket.leaveGroup(group);
					multiSocket.close();
				}
				if (broadcast_valid)
					udpSocket.close();
				System.out.println("yqi IpAddrList size: " + IpAddrList.size());
				System.out.println("yqi Socket Receive Timeout!");
				return IpAddrList;
			}
			System.out.println("yqi ################# rcvdp.getLength = "
					+ rcvdp.getLength());
			String recvStr = new String(rcvdp.getData(), rcvdp.getOffset(),
					rcvdp.getLength());
			System.out.println("yqi recvStr" + recvStr);

			JSONObject infor = new JSONObject(recvStr);
			String type = infor.getString("type");
			if (type.equalsIgnoreCase("SCS-NOTIFY")) {
				IpAddrList.add(infor.getString("ip"));
				System.out
						.println("yqi IpAddrList[0] ip: " + IpAddrList.get(0));
			}
		}
	}

//	public static List<String> getAllMacAddresses() {
//		List<String> addresses = new ArrayList<String>();
//		
//		WifiInfo info = wifi.getConnectionInfo();
//		info.getMacAddress();
//
////		StringBuffer sb = new StringBuffer();
////		try {
////			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
////					.getNetworkInterfaces();
////			while (networkInterfaces.hasMoreElements()) {
////				NetworkInterface netInterface = networkInterfaces.nextElement();
////				byte[] mac = netInterface.getHardwareAddress();
////
////				if (mac != null && mac.length != 0) {
////					sb.delete(0, sb.length());
////					for (byte b : mac) {
////						String hexString = Integer.toHexString(b & 0xFF);
////						sb.append((hexString.length() == 1) ? "0" + hexString
////								: hexString);
////					}
////					addresses.add(sb.toString());
////				}
////			}
////		} catch (SocketException e) {
////			e.printStackTrace();
////		}
//
//		return addresses;
//	}

	public static String getLocalHostIPAddress() {
		String ip = "";
		try {
			InetAddress i = InetAddress.getLocalHost();
			System.out.println(i); // 计算机名称和IP
			System.out.println(i.getHostName()); // 名称
			System.out.println(i.getHostAddress()); // 只获得IP
			ip = i.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	private static String IP_intToString(int i) {
		return ((i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + ((i >> 24) & 0xFF));
	}

	private String HostipAddr = null;
	private String MacAddr = null;

	public String getLocalMacAddress(Context c) {
		myContent = c;
		
		WifiManager wifi = (WifiManager) c
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		HostipAddr = IP_intToString(info.getIpAddress());
		return info.getMacAddress();
	}

	// public static void main(String[] args) {
	// MultiCastSSDP ssdp = new MultiCastSSDP();
	// try {
	// // List<String> macs = ssdp.getAllMacAddresses();
	// // for(int i=0;i<macs.size();i++){
	// // String tempIp = macs.get(i);
	// ssdp.MultiCastSSDPClient(ssdp.getLocalHostIPAddress(),
	// ssdp.getLocalMacAddress());
	// // }
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
}
