package meta.midea.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import meta.midea.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

public class MySocketClient implements Runnable {
	private String HOST = "192.168.254.34";
	private int PORT = 9999;
	public Socket socket = null;
	public BufferedReader in = null;
	public PrintWriter out = null;
	private String content = "";
	private MyApplication myApp;
	public boolean isConnect = false;

	public MySocketClient(String host, int port, MyApplication myapp) {
		HOST = host;
		PORT = port;
		myApp = myapp;
		try {
			socket = new Socket();
			InetSocketAddress isa = new InetSocketAddress(HOST, PORT);
			socket.connect(isa, 5000);

			// socket = new Socket(HOST, PORT);

			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);

			isConnect = true;
			System.out.println("socket==" + socket);
		} catch (IOException ex) {
			isConnect = false;
			// ex.printStackTrace();
			System.out.println("socket断开了");
		}
	}

	public void run() {
		while (true) {
			read();
		}
	}

	private void read() {
		if (socket != null && socket.isConnected()) {
			if (!socket.isInputShutdown()) {
				try {
					if ((content = in.readLine()) != null) {
						System.out.println("socket接收到的数据：" + content);

						try {
							JSONObject jObj = LoadData.getJSONObject(content);
							// {"tagType":"","tagValue":{"id":46,"error":-1},"key":"wificode",
							// cmdName:"", cmd_rsp:""}

							// socket 返回 jObj=={
							// "cmdName":"参数刷新",
							// "tagValue":{"event":"AA55014001010001010118000008401A344B00C1"},
							// "tagType":"",
							// "key":"wificode",
							// "event":"AA55014001010001010118000008401A344B00C1"}
							if (jObj != null) {
								if (jObj.has("key")
										&& jObj.getString("key").equals(
												"wificode")) {
									// 判断是此authKey的socket返回信息
									if (jObj.has("authKey")
											&& jObj.get("authKey")
													.equals(myApp
															.getCurServerAuthKey())) {
										if (jObj.has("tagValue")) {
											JSONObject tagJObj = jObj
													.getJSONObject("tagValue");
											if (tagJObj.has("error")
													&& tagJObj.getString(
															"error").equals(
															"-1")) {
												setSocketFalse();
											} else {
												if (tagJObj.has("event")) {
													LoadData.setMSDataForSocket(jObj
															.getString("tagValue"));
												} else {
													socketSetData(jObj);
												}
											}
										} else {
											setSocketFalse();
										}
									}
								}
							}
						} catch (JSONException e) {
							setSocketFalse();
							// e.printStackTrace();
						} catch (Exception e) {
							setSocketFalse();
							// e.printStackTrace();
						}
					}
				} catch (IOException e) {
					// e.printStackTrace();
					isConnect = false;
					myApp.getMyService().closeSocket();
					System.out.println("socket断开了");
				}
			}
		} else {
			isConnect = false;
			myApp.getMyService().closeSocket();
			System.out.println("socket断开了");
		}
	}

	private void setSocketFalse() {
		Intent intent;
		intent = new Intent();
		intent.putExtra("tag", "socketError");
		intent.putExtra("activity", "main");
		intent.setAction("meta.midea.MainActivity");
		myApp.getMyService().sendBroadcast(intent);

		intent = new Intent();
		intent.putExtra("tag", "socketError");
		intent.putExtra("activity", "setReserve");
		intent.setAction("meta.midea.ReserveSetActivity");
		myApp.getMyService().sendBroadcast(intent);
	}

	private void socketSetData(JSONObject jObj) throws JSONException {
		String reCmd;
		String reStr;
		Intent intent;
		if (!jObj.has("cmdName") || !jObj.has("cmd_rsp"))
			return;

		reCmd = jObj.getString("cmdName");
		reStr = jObj.getString("cmd_rsp");

		// 指令不合法
		if (!LoadData.checkCmd(reStr)) {
			return;
		}

		if (reCmd.equals("获取机型")) {
			if (jObj.has("ipAddr")) {
				myApp.setCurServerIP(jObj.getString("ipAddr"));
			}
			if (jObj.has("city") && jObj.get("city") != null
					&& !jObj.getString("city").equals("null")) {
				String cityStr = jObj.getString("city");
				String[] cityArr = cityStr.split(",");
				myApp.setCurServerCity(cityArr[cityArr.length - 1]);
			}

			boolean tag = LoadData.setGetServeType(reStr);

			intent = new Intent();
			intent.putExtra("tag", "getType");
			intent.putExtra("activity", "main");
			intent.putExtra("result", tag);
			intent.setAction("meta.midea.MainActivity");
			myApp.getMyService().sendBroadcast(intent);
		} else if (reCmd.equals("刷新")) {
			String conStr = reStr.substring(14, reStr.length());
			String con1Str = conStr.substring(0, 2);
			boolean tag = false;
			if (con1Str.equals("10")) {
				tag = false;
			} else if (con1Str.equals("01")) {
				LoadData.setRefresh(conStr);
				tag = true;
			}

			intent = new Intent();
			intent.putExtra("tag", "refresh");
			intent.putExtra("activity", "main");
			intent.putExtra("result", tag);
			intent.setAction("meta.midea.MainActivity");
			myApp.getMyService().sendBroadcast(intent);
		} else if (reCmd.equals("刷新预约一")) {
			LoadData.setReserveTotal(reStr, "1");

			intent = new Intent();
			intent.putExtra("tag", "refreshReserve1");
			intent.putExtra("activity", "main");
			intent.putExtra("result", true);
			intent.setAction("meta.midea.MainActivity");
			myApp.getMyService().sendBroadcast(intent);
		} else if (reCmd.equals("设置预约一")) {
			myApp.setReserve1Change(false);
			LoadData.setReserveTotal(reStr, "1");

			intent = new Intent();
			intent.putExtra("tag", "setReserve1");
			intent.putExtra("activity", "setReserve");
			intent.putExtra("result", true);
			intent.setAction("meta.midea.ReserveSetActivity");
			myApp.getMyService().sendBroadcast(intent);
		} else if (reCmd.equals("设置预约二")) {
			myApp.setReserve2Change(false);
			LoadData.setReserveTotal(reStr, "2");

			intent = new Intent();
			intent.putExtra("tag", "setReserve2");
			intent.putExtra("activity", "setReserve");
			intent.putExtra("result", true);
			intent.setAction("meta.midea.ReserveSetActivity");
			myApp.getMyService().sendBroadcast(intent);
		} else if (reCmd.equals("设置预约三")) {
			myApp.setReserve3Change(false);
			LoadData.setReserveTotal(reStr, "3");

			intent = new Intent();
			intent.putExtra("tag", "setReserve3");
			intent.putExtra("activity", "setReserve");
			intent.putExtra("result", true);
			intent.setAction("meta.midea.ReserveSetActivity");
			myApp.getMyService().sendBroadcast(intent);
		} else if (reCmd.equals("取消预约一")) {
			myApp.setReserve1Change(false);
			LoadData.setReserveTotal(reStr, "1");

			intent = new Intent();
			intent.putExtra("tag", "closeReserve1");
			intent.putExtra("activity", "main");
			intent.putExtra("result", true);
			intent.putExtra("name", "1");
			intent.setAction("meta.midea.MainActivity");
			myApp.getMyService().sendBroadcast(intent);

			intent = new Intent();
			intent.putExtra("tag", "closeReserve1");
			intent.putExtra("activity", "setReserve");
			intent.putExtra("result", true);
			intent.setAction("meta.midea.ReserveSetActivity");
			myApp.getMyService().sendBroadcast(intent);
		} else if (reCmd.equals("取消预约二")) {
			myApp.setReserve2Change(false);
			LoadData.setReserveTotal(reStr, "2");

			intent = new Intent();
			intent.putExtra("tag", "closeReserve2");
			intent.putExtra("activity", "main");
			intent.putExtra("result", true);
			intent.putExtra("name", "2");
			intent.setAction("meta.midea.MainActivity");
			myApp.getMyService().sendBroadcast(intent);

			intent = new Intent();
			intent.putExtra("tag", "closeReserve2");
			intent.putExtra("activity", "setReserve");
			intent.putExtra("result", true);
			intent.setAction("meta.midea.ReserveSetActivity");
			myApp.getMyService().sendBroadcast(intent);
		} else if (reCmd.equals("取消预约三")) {
			myApp.setReserve3Change(false);
			LoadData.setReserveTotal(reStr, "3");

			intent = new Intent();
			intent.putExtra("tag", "closeReserve3");
			intent.putExtra("activity", "main");
			intent.putExtra("result", true);
			intent.putExtra("name", "3");
			intent.setAction("meta.midea.MainActivity");
			myApp.getMyService().sendBroadcast(intent);

			intent = new Intent();
			intent.putExtra("tag", "closeReserve3");
			intent.putExtra("activity", "setReserve");
			intent.putExtra("result", true);
			intent.setAction("meta.midea.ReserveSetActivity");
			myApp.getMyService().sendBroadcast(intent);
		} else if (reCmd.equals("开机")) {
			String conStr = reStr.substring(14, reStr.length());
			String con1Str = conStr.substring(0, 2);
			boolean tag = false;
			if (con1Str.equals("10")) {
				tag = false;
			} else if (con1Str.equals("01")) {
				LoadData.setRefresh(conStr);
				tag = true;
			}

			intent = new Intent();
			intent.putExtra("tag", "open");
			intent.putExtra("activity", "main");
			intent.putExtra("result", tag);
			intent.setAction("meta.midea.MainActivity");
			myApp.getMyService().sendBroadcast(intent);
		} else if (reCmd.equals("关机")) {
			String conStr = reStr.substring(14, reStr.length());
			String con1Str = conStr.substring(0, 2);
			// String con2Str = conStr.substring(6, conStr.length());
			boolean tag = false;
			if (con1Str.equals("10")) {
				tag = false;
			} else if (con1Str.equals("01")) {
				LoadData.setRefresh(conStr);
				tag = true;
			}
			intent = new Intent();
			intent.putExtra("tag", "close");
			intent.putExtra("activity", "main");
			intent.putExtra("result", tag);
			intent.setAction("meta.midea.MainActivity");
			myApp.getMyService().sendBroadcast(intent);
		} else if (reCmd.equals("E+增容洗浴模式") || reCmd.equals("生活用水洗浴模式")
				|| reCmd.equals("半胆速热洗浴模式") || reCmd.equals("整胆速热洗浴模式")
				|| reCmd.equals("峰谷夜电洗浴模式") || reCmd.equals("节能洗浴模式")
				|| reCmd.equals("1人洗洗浴模式") || reCmd.equals("2人洗洗浴模式")
				|| reCmd.equals("3人洗洗浴模式") || reCmd.equals("老人洗洗浴模式")
				|| reCmd.equals("成人洗洗浴模式") || reCmd.equals("儿童洗洗浴模式")
				|| reCmd.equals("设置暖风") || reCmd.equals("设置智洁排水")
				|| reCmd.equals("云智能") || reCmd.equals("自设温度")
				|| reCmd.equals("夏季模式洗浴模式") || reCmd.equals("冬季模式洗浴模式")) {
			String conStr = reStr.substring(14, reStr.length());
			String con1Str = conStr.substring(0, 2);
			boolean tag = false;
			if (con1Str.equals("10")) {
				tag = false;
			} else if (con1Str.equals("01")) {
				LoadData.setRefresh(conStr);
				tag = true;
			}

			intent = new Intent();
			intent.putExtra("tag", "setMode");
			intent.putExtra("activity", "main");
			intent.putExtra("result", tag);
			intent.setAction("meta.midea.MainActivity");
			myApp.getMyService().sendBroadcast(intent);
		}
	}

	public void write(String msg) {
		if (socket.isConnected()) {
			if (!socket.isOutputShutdown()) {
				out.println(msg);
			}
		}
	}
}
