package meta.midea.data;

import java.io.IOException;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import meta.midea.MyApplication;
import meta.midea.SelModeActivity;
import meta.midea.login.IntLoginActivity;
import meta.midea.login.MyServerListActivity;
import meta.midea.main.MainActivity;
import meta.midea.tool.MyTool;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.ParseException;
import android.os.Handler;
import android.os.Message;

public class LoadData {
	public static boolean isWifi = true;
	public static boolean isAddAuthKey = false;

	public static String BASE_URLFor3G = "http://192.168.254.31:8088/";
	// public static String BASE_URLFor3G = "http://119.147.213.171/";
	public static String BASE_URLForWIFI = "http://192.168.1.1/";
	public static String KEYFor3G = "customize/control/setServicesOfCloudMothod";
	public static String Login = "customize/control/flexLogin";
	public static String Update = "customize/control/updateUser";
	public static String Register = "customize/control/getRegisterUser";
	public static String CheckLoginName = "customize/control/checkLoginUserName";
	public static String GetActivation = "customize/control/getActivation";
	public static String Bind = "customize/control/getTogetherResult";
	public static String ErrLog = "customize/control/errorLog";
	public static String GetServers = "customize/control/getAllDeviceList";
	public static String SaveServer = "customize/control/insertNewAppliance";
	public static String WifiToCloud = "customize/control/getServicesOfCloudMothod";
	public static String Exit = "customize/control/clearWaitCommand";
	public static String SaveCloudTemp = "customize/control/setTempUpload";
	public static String GetServerCity = "customize/control/getCityAndIPAddr";
	public static String CheckVersion = "customize/control/checkVersion";
	public static String KEYForWIFI = "uart/ioctl";
	public static String SYSMODE = "sys/mode";
	public static String SYSSCAN = "sys/scan";
	public static String SYSNETWORK = "sys/network";
	public static String SYSNETCLOUD = "sys/cloud";
	public static String SYSREBOOT = "sys/command";
	public static String SYS = "sys";
	public static String authkey = "";

	public static int TIME = 1; // 热水器向云端的刷新速率
	private static MyApplication myApp = MyApplication.getInstance();

	// JSON包结构就是请求包是 {cmd_req: AA55015A020F0001}， 应答包是{cmd_rsp:AA550123020F0001}
	private static String startCmd = "AA55"; // 起始码

	private static String modelNum1Cmd = "01"; // 电热水器
	// private static String modelNum2Cmd = "02"; // 燃气热水器
	// private static String modelNum3Cmd = "03"; // 太阳能热水器
	// private static String modelNum4Cmd = "04"; // 空气能热水器

	private static String reqCmd = "5A"; // 命令请求
	// private static String rspCmd = "23"; // 命令应答
	// private static String evtReqCmd = "40"; // 事件请求
	// private static String evtRspCmd = "21"; // 事件应答

	// 三个命令码(第一个类型，第二个名称，第三个预留，第二三放在一起)
	// private static String setCmd = "01"; // 命令码1设置指令
	// private static String selCmd = "02"; // 命令码1功能选择指令
	// private static String searchCmd = "03"; // 命令码1状态查询指令

	// private static String actCmd = "010000"; // 激活指令(后加六位激活码)
	private static String openCmd = "010100";// 开机指令
	private static String closeCmd = "010200";// 关机指令
	// private static String timeCmd = "010300";// 设置时间指令
	private static String reserve1Cmd = "010500";// 设置预约一指令
	private static String reserve2Cmd = "010600";// 设置预约二指令
	private static String reserve3Cmd = "010700";// 设置预约三指令
	// private static String tempCmd = "010400";// 温度设置指令
	private static String getStateCmd = "030100";// 刷新状态
	private static String getReserve1Cmd = "030200";// 预约一刷新指令
	private static String getReserve2Cmd = "030300";// 预约二刷新指令
	private static String getReserve3Cmd = "030400";// 预约三刷新指令
	private static String getServerTypeCmd = "030500";// 刷新机型指令
	private static String setModeCmd = "020100"; // 设置模式
	private static String setDraCmd = "020200"; // 智洁排水
	private static String setWarCmd = "020400"; // 暖风

	public static String refreshCmd = "010100";// 状态刷新广播
	public static String reserveCmd = "010400";// 预约刷新广播

	// 设置配置模式
	public static boolean setMode(boolean isNormal) {
		String jsonStr = "{\"mode\":" + (isNormal ? "0" : "1") + "}";

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("", jsonStr));

		try {
			JSONObject jObj = getServiceActionStr(param, false, BASE_URLForWIFI
					+ SYSMODE, true);
			if (jObj != null)
				return true;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// 获取周边Wifi
	public static JSONArray getWifi() {
		try {
			JSONObject jObj = getServiceActionStr(null, true, BASE_URLForWIFI
					+ SYSSCAN, true);
			if (jObj == null || !jObj.has("networks")) {
				return null;
			}
			return jObj.getJSONArray("networks");
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 获取wifi的网络信息
	public static JSONObject getNetWork() {
		try {
			JSONObject jObj = getServiceActionStr(null, true, BASE_URLForWIFI
					+ SYSNETWORK, true);
			return jObj;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 获取wifi的云平台信息
	public static JSONObject getCloud() {
		try {
			JSONObject jObj = getServiceActionStr(null, true, BASE_URLForWIFI
					+ SYSNETCLOUD, true);
			return jObj;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 发送wifi
	public static boolean sendWifi(String ssidStr, String securityStr,
			String passphraseStr, String ip, String ipmask, String ipgw,
			String ipdns1, String ipdns2) {
		boolean isStaticIP = true;
		if (ip == null || ip.equals("")) {
			isStaticIP = false;
		}
		String jsonStr = "{\"ssid\":\""
				+ ssidStr
				+ "\",\"security\":\""
				+ securityStr
				+ "\",\"passphrase\":\""
				+ passphraseStr
				+ "\",\"dhcp\":"
				+ (!isStaticIP ? "1\"}" : ("0" + ", \"ipaddr\":\"" + ip
						+ "\", \"ipmask\":\"" + ipmask + "\", \"ipgw\":\""
						+ ipgw + "\", \"ipdns1\":\"" + ipdns1
						+ "\", \"ipdns2\":\"" + ipdns2 + "\"}"));

		// SSID: meta-links, BSSID: ec:88:8f:91:9b:fe, capabilities:
		// [WPA-PSK-CCMP][WPA2-PSK-CCMP], level: -37, frequency: 2437

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("", jsonStr));

		try {
			JSONObject jObj = getServiceActionStr(param, false, BASE_URLForWIFI
					+ SYSNETWORK, true);
			if (jObj != null)
				return true;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// 发送云服务
	public static boolean sendCloud(String enable, String interval, String url,
			String isAuthKey, String authKey, String ip) {
		url = url + WifiToCloud;
		String jsonStr = "{\"enable\":" + enable + ",\"interval\":" + interval
				+ ",\"url\":\"" + url + "\",\"isauthkey\":" + isAuthKey
				+ ", \"authkey\":\"" + authKey + "\", \"ipaddr\":\"" + ip
				+ "\"}";

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("", jsonStr));

		try {
			JSONObject jObj = getServiceActionStr(param, false, BASE_URLForWIFI
					+ SYSNETCLOUD, true);
			if (jObj != null)
				return true;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// 重启热水器
	public static boolean resetServer() {
		String jsonStr = "{\"command\":\"reboot\"}";

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("", jsonStr));

		try {
			JSONObject jObj = getServiceActionStr(param, false, BASE_URLForWIFI
					+ SYSREBOOT, true);
			if (jObj != null)
				return true;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// 获取设备基本信息
	public static String getServeBaseInfo() {
		String str = "";
		try {
			str = HttpServer.getGetRequest(BASE_URLForWIFI + SYS, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return str;
	}

	// 获取设备authKey
	public static String getSysCloud() {
		String str = "";
		try {
			// {"enable":0,"interval":60,
			// "url":"http://192.168.254.97:8088/customize/control/getServicesOfCloudMothod",
			// "isauthkey":1,"authkey":"test4;F50J;111112"}
			str = HttpServer.getGetRequest(BASE_URLForWIFI + SYSNETCLOUD, null);
		} catch (Exception e) {
			e.printStackTrace();
			return str;
		}

		return str;
	}

	// 云端服务：报错异常信息上传
	public static boolean saveErr(String errStr) throws JSONException,
			Exception {
		System.out.println("saveErr====errStr:" + errStr);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("errStr", errStr));

		// JSONObject jObj = getServiceActionStr(param, false, ErrLog);
		JSONObject jObj = getJSONObject(HttpServer.getPostRequest(BASE_URLFor3G
				+ ErrLog, param, false));
		if (jObj != null)
			return true;
		else
			return false;
	}

	// 云端服务：检查版本更新
	public static boolean checkVersion(String curVersion) throws JSONException,
			Exception {
		// MideaAndroid1219V1.2.apk
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("curversion", curVersion));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ CheckVersion + ";jsessionid=" + myApp.getSessionID(), false);
		if (jObj != null && jObj.has("tag")) {
			myApp.setNewVersionUrl(jObj.getString("tag"));
			return true;
		} else
			return false;
	}

	// 云端服务：应用关闭
	public static boolean exit() throws JSONException, Exception {
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("uApiKey", myApp.getCurServerAuthKey()));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ Exit + ";jsessionid=" + myApp.getSessionID(), false);
		if (jObj != null)
			return true;
		else
			return false;
	}

	// 云端服务：获取设备所在地
	public static boolean getServerCity() throws JSONException, Exception {
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("authKey", myApp.getCurServerAuthKey()));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ GetServerCity, false);
		if (jObj != null) {
			if (jObj.has("ipAddr")) {
				myApp.setCurServerIP(jObj.getString("ipAddr"));
			}
			if (jObj.has("city") && jObj.get("city") != null
					&& !jObj.getString("city").equals("null")) {
				String cityStr = jObj.getString("city");
				String[] cityArr = cityStr.split(",");
				myApp.setCurServerCity(cityArr[cityArr.length - 1]);
			}
			return true;
		} else
			return false;
	}

	// 云端服务：保存云智能匹配温度
	public static boolean saveCloudTemp() throws JSONException, Exception {
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("authKey", myApp.getCurServerAuthKey()));
		param.add(new BasicNameValuePair("cloudTemp", myApp
				.getCurServerCloudTemp()));

		System.out.println("保存云平台城市：" + myApp.getCurServerCity());
		System.out.println("保存云平台温度：" + myApp.getCurServerCloudTemp());

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ SaveCloudTemp, false);
		if (jObj != null)
			return true;
		else
			return false;
	}

	// 云端服务：注册
	public static boolean register(String username, String password,
			String cName, String eName, String phoneNum, String telNum,
			String email, String homeAddress, String officeAddress)
			throws Exception {
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("username", username));
		param.add(new BasicNameValuePair("password", password));
		param.add(new BasicNameValuePair("chineseName", cName));
		param.add(new BasicNameValuePair("englishName", eName));
		param.add(new BasicNameValuePair("telephone", phoneNum));
		param.add(new BasicNameValuePair("telephone2", telNum));
		param.add(new BasicNameValuePair("email", email));
		param.add(new BasicNameValuePair("homeAdress", homeAddress));
		param.add(new BasicNameValuePair("officeAdress", officeAddress));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ Register, false);
		if (jObj != null)
			return true;
		else
			return false;
	}

	// 云端服务：检查登陆名是否可用
	public static boolean checkLoginName(String username) throws Exception {
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("username", username));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ CheckLoginName, false);
		if (jObj != null)
			return true;
		else
			return false;
	}

	// 云端服务：登陆
	public static boolean login(String username, String password)
			throws Exception {
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("username", username));
		param.add(new BasicNameValuePair("password", password));

		// {"tag":"Success","sessionid":"68804368F3F5DA715152BE83AF48DF28.jvm1"}
		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ Login, false);
		if (jObj != null) {
			String sessionID = jObj.getString("sessionid");
			myApp.setSessionID(sessionID);
			myApp.setUsername(username);
			myApp.setUserpass(password);
			return true;
		} else
			return false;
	}

	// 云端服务：修改密码
	public static boolean update(String username, String newPassword,
			String sessionID) throws Exception {
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("username", username));
		param.add(new BasicNameValuePair("userpassword", newPassword));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ Update + ";jsessionid=" + sessionID, false);
		if (jObj != null) {
			return true;
		} else
			return false;
	}

	// 云端服务：激活
	public static boolean activate(String actCode, String sessionid,
			String username, String password, String authkey)
			throws JSONException, Exception {
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("activeCode", actCode));
		param.add(new BasicNameValuePair("jsessionid", sessionid));
		param.add(new BasicNameValuePair("username", username));
		param.add(new BasicNameValuePair("password", password));
		param.add(new BasicNameValuePair("authkey", authkey));

		// {"tag":"Success","sessionid":"68804368F3F5DA715152BE83AF48DF28.jvm1"}
		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ Login, false);
		if (jObj != null) {

			return true;
		} else
			return false;
	}

	// 云端服务：绑定
	public static boolean bind(String actCode, String username,
			String userpass, String sessionID, String authkey, String serverName)
			throws JSONException, Exception {
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("activeCode", actCode));
		// param.add(new BasicNameValuePair("sessionid", sessionID));
		param.add(new BasicNameValuePair("username", username));
		param.add(new BasicNameValuePair("password", userpass));
		param.add(new BasicNameValuePair("authKey", authkey));
		param.add(new BasicNameValuePair("otherName", serverName));

		// {"tag":"Success","sessionid":"68804368F3F5DA715152BE83AF48DF28.jvm1"}
		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ Bind + ";jsessionid=" + sessionID, false);
		if (jObj != null) {

			return true;
		} else {
			return false;
		}
	}

	// 云端服务：获取账号下设备信息
	public static boolean getServers(String username, String sessionID)
			throws Exception {
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("username", username));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ GetServers + ";jsessionid=" + sessionID, false);
		if (jObj != null) {
			// {"total":1,"data":[{"deviceName":"美的电热水器"}]}
			if (jObj.has("data")) {
				JSONArray jsonArr = (JSONArray) jObj.get("data");
				myApp.setCloudServersList(jsonArr);

				// 展示用
				// "authKey":"midea;jbh101"
				// JSONArray jsonArrNew = new JSONArray();
				// for (int i = 0; i < jsonArr.length(); i++) {
				// JSONObject jobj = (JSONObject) jsonArr.get(i);
				// if (!jobj.has("authKey")
				// || !jobj.getString("authKey")
				// .equals("midea;111117")) {
				// continue;
				// }
				// jsonArrNew.put(jobj);
				// }
				// myApp.setCloudServersList(jsonArrNew);
			}

			return true;
		} else
			return false;
	}

	// 云端服务：保存设备到账号
	public static boolean saveServers(String serverName, String actCode,
			String userName, String sessionID) throws Exception {

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("otherName", serverName));
		param.add(new BasicNameValuePair("activeCode", actCode));
		param.add(new BasicNameValuePair("username", userName));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ SaveServer + ";jsessionid=" + sessionID, false);
		if (jObj != null) {
			if (jObj.has("total") && jObj.getString("total").equals("0")) {
				myApp.setCloudServersList(null);
				myApp.setCloudAddServerStr("激活码不可用，请联系美的售后人员索取热水器激活码");
			} else if (jObj.has("total")
					&& jObj.getString("total").equals("-1")) {
				myApp.setCloudServersList(null);
				myApp.setCloudAddServerStr("该激活码已被激活，不可重复激活");
			} else {
				if (jObj.has("data")) {
					JSONArray jsonArr = (JSONArray) jObj.get("data");
					myApp.setCloudServersList(jsonArr);
				}
			}
			return true;
		} else
			return false;
	}

	// 室外刷新机型
	public static boolean getServerTypeFor3G() throws JSONException, Exception {
		String cmdV = initGetTypeCmd(false);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("cmdName", "获取机型"));
		param.add(new BasicNameValuePair("cmdValue", cmdV));
		param.add(new BasicNameValuePair("interval", Integer.toString(TIME))); // 下一次访问的时间差
		param.add(new BasicNameValuePair("username", myApp.getUsername()));
		param.add(new BasicNameValuePair("proType", myApp.getCurProType()));
		param.add(new BasicNameValuePair("authKey", myApp.getCurServerAuthKey()));
		param.add(new BasicNameValuePair("actCode", myApp.getCurServerActCode()));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ KEYFor3G + ";jsessionid=" + myApp.getSessionID(), false);

		if (jObj != null && jObj.has("success")
				&& jObj.getString("success").equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	// 室内刷新机型
	public static boolean getServerTypeForWifi() throws JSONException,
			Exception {
		String jsonStr = initGetTypeCmd(true);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("", jsonStr));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLForWIFI
				+ KEYForWIFI, true);

		if (jObj != null) {
			String reStr = jObj.getString("cmd_rsp");
			if (checkCmd(reStr)) {
				return setGetServeType(reStr);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// 处理获取机型返回指令
	public static boolean setGetServeType(String reStr) {
		// 解析处理应答命令
		String conStr = reStr.substring(14, reStr.length());
		String con1Str = conStr.substring(0, 2);
		if (con1Str.equals("10")) {
			return false;
		} else if (con1Str.equals("01")) {
			String con2Str = conStr.substring(2, conStr.length());
			String sbuf = MyTool.HexStringToBinary(con2Str).toString();

			myApp.setPanelMark(sbuf.substring(0, 8));
			myApp.setMode1Mark(sbuf.substring(8, 16));
			myApp.setMode3Mark(sbuf.substring(16, 24));
			myApp.setMode2Mark(sbuf.substring(24, 32));
			myApp.setMode4Mark(sbuf.substring(32, 40));
			myApp.setSetMark(sbuf.substring(40, 56));

			return true;
		}

		return false;
	}

	// 室外刷新状态
	public static boolean refreshStateFor3G() throws JSONException, Exception {
		String cmdV = initRefreshCmd(false);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("cmdName", "刷新"));
		param.add(new BasicNameValuePair("cmdValue", cmdV));
		param.add(new BasicNameValuePair("interval", Integer.toString(TIME))); // 下一次访问的时间差
		param.add(new BasicNameValuePair("username", myApp.getUsername()));
		param.add(new BasicNameValuePair("proType", myApp.getCurProType()));
		param.add(new BasicNameValuePair("authKey", myApp.getCurServerAuthKey()));
		param.add(new BasicNameValuePair("actCode", myApp.getCurServerActCode()));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ KEYFor3G + ";jsessionid=" + myApp.getSessionID(), false);

		if (jObj != null && jObj.has("success")
				&& jObj.getString("success").equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	// 室内刷新状态
	public static boolean refreshStateForWifi() throws JSONException, Exception {
		String jsonStr = initRefreshCmd(true);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("", jsonStr));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLForWIFI
				+ KEYForWIFI, true);

		if (jObj != null) {
			String reStr = jObj.getString("cmd_rsp");
			if (checkCmd(reStr)) {
				String conStr = reStr.substring(14, reStr.length());
				String con1Str = conStr.substring(0, 2);
				if (con1Str.equals("10")) {
					return false;
				} else if (con1Str.equals("01")) {
					setRefresh(conStr);
					return true;
				}
			} else {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	// 处理刷新状态返回指令
	public static void setRefresh(String conStr) {
		// AA 55 01 23 03 01 00 01 ;;01 00 46 00 00 02 00 00 00 00 008E
		// 第九位：01代表已经开机
		// 第十位：00代表保护功能全关（包含漏电保护、超温保护等）
		// 第十一位：46代表温度，16进制转换为10进制为70℃
		// 第十二位：00代表热水量为0，如果有值的话16进制转换为10进制显示热水量
		// 第十三位：00代表水流量，如果有值 同上一条一样
		// 第十四位：02代表生活用水，01代表E+增容等
		// 第十五位：01代表一人洗，02代表二人洗等
		// 第十六位：加热剩余小时
		// 第十七位：加热剩余分钟
		// 第十八位：当前目标温度
		// 第19位：预留
		// 第二十位：校验和

		String con2Str = conStr.substring(2, conStr.length());
		String sbuf = MyTool.HexStringToBinary(con2Str.substring(0, 4))
				.toString();

		myApp.setRefStateMark(sbuf.substring(0, 8));
		myApp.setRefErrMark(sbuf.substring(8, sbuf.length()));
		myApp.setRefSJWDMark(Integer.toString(
				Integer.parseInt(con2Str.substring(4, 6), 16), 10));
		myApp.setRefRSLMark(Integer.toString(
				Integer.parseInt(con2Str.substring(6, 8), 16), 10));
		myApp.setRefSLLMark(Integer.toString(
				Integer.parseInt(con2Str.substring(8, 10), 16), 10));
		myApp.setRefModeMark(MyTool
				.HexStringToBinary(con2Str.substring(10, 14)).toString());
		myApp.setRefHeatingTimeHMark(Integer.toString(
				Integer.parseInt(con2Str.substring(14, 16), 16), 10));
		myApp.setRefHeatingTimeMMark(Integer.toString(
				Integer.parseInt(con2Str.substring(16, 18), 16), 10));
		myApp.setTempMark(Integer.toString(
				Integer.parseInt(con2Str.substring(18, 20), 16), 10));
	}

	// 室外刷新预约
	public static boolean refreshReserveFor3G(String reserveNum)
			throws JSONException, Exception {
		String[] str = initRefreshRerserveCmd(false, reserveNum);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("cmdName", str[0]));
		param.add(new BasicNameValuePair("cmdValue", str[1]));
		param.add(new BasicNameValuePair("interval", Integer.toString(TIME))); // 下一次访问的时间差
		param.add(new BasicNameValuePair("username", myApp.getUsername()));
		param.add(new BasicNameValuePair("proType", myApp.getCurProType()));
		param.add(new BasicNameValuePair("authKey", myApp.getCurServerAuthKey()));
		param.add(new BasicNameValuePair("actCode", myApp.getCurServerActCode()));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ KEYFor3G + ";jsessionid=" + myApp.getSessionID(), false);

		if (jObj != null && jObj.has("success")
				&& jObj.getString("success").equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	// 室内刷新预约
	public static boolean refreshReserveForWifi(String reserveNum)
			throws JSONException, Exception {
		String[] str = initRefreshRerserveCmd(true, reserveNum);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("", str[1]));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLForWIFI
				+ KEYForWIFI, true);

		if (jObj != null) {
			String reStr = jObj.getString("cmd_rsp");
			if (checkCmd(reStr)) {
				setReserveTotal(reStr, reserveNum);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// 处理刷新预约返回指令
	public static void setReserveTotal(String reStr, String reserveNum) {
		// AA550123 0302
		// 00(00000000 预约一关(开)\预约二关(开)\预约三关(开) )
		// 20(温度 10进制) 21(小时 10进制) 20(分钟10进制) 00(loop0关1开、周日~周一0关1开)
		// 20(温度 10进制) 21(小时 10进制) 20(分钟10进制) 00(loop0关1开、周日~周一0关1开)
		// 20(温度 10进制) 21(小时 10进制) 20(分钟10进制) 00(loop0关1开、周日~周一0关1开)
		// 8E校验和

		String reserve1Open = "";
		String reserve2Open = "";
		String reserve3Open = "";
		String conStr = reStr.substring(12, reStr.length());
		char[] openCharArr = MyTool.HexStringToBinary(conStr.substring(0, 2))
				.toString().toCharArray();
		if (openCharArr[7] == '1') {
			reserve1Open = "FF";
		} else if (openCharArr[7] == '0') {
			reserve1Open = "00";
		}
		if (openCharArr[6] == '1') {
			reserve2Open = "FF";
		} else if (openCharArr[6] == '0') {
			reserve2Open = "00";
		}
		if (openCharArr[5] == '1') {
			reserve3Open = "FF";
		} else if (openCharArr[5] == '0') {
			reserve3Open = "00";
		}

		// 预约一
		String temp1Str = Integer.toString(
				Integer.parseInt(conStr.substring(2, 4), 16), 10);
		if (temp1Str.length() == 1)
			temp1Str = "0" + temp1Str;

		String h1Str = Integer.toString(
				Integer.parseInt(conStr.substring(4, 6), 16), 10);
		if (h1Str.length() == 1)
			h1Str = "0" + h1Str;

		String m1Str = Integer.toString(
				Integer.parseInt(conStr.substring(6, 8), 16), 10);
		if (m1Str.length() == 1)
			m1Str = "0" + m1Str;

		String week1Str = MyTool.HexStringToBinary(conStr.substring(8, 10))
				.toString();

		// 预约二
		String temp2Str = Integer.toString(
				Integer.parseInt(conStr.substring(10, 12), 16), 10);
		if (temp2Str.length() == 1)
			temp2Str = "0" + temp2Str;

		String h2Str = Integer.toString(
				Integer.parseInt(conStr.substring(12, 14), 16), 10);
		if (h2Str.length() == 1)
			h2Str = "0" + h2Str;

		String m2Str = Integer.toString(
				Integer.parseInt(conStr.substring(14, 16), 16), 10);
		if (m2Str.length() == 1)
			m2Str = "0" + m2Str;

		String week2Str = MyTool.HexStringToBinary(conStr.substring(16, 18))
				.toString();

		// 预约三
		String temp3Str = Integer.toString(
				Integer.parseInt(conStr.substring(18, 20), 16), 10);
		if (temp3Str.length() == 1)
			temp3Str = "0" + temp3Str;

		String h3Str = Integer.toString(
				Integer.parseInt(conStr.substring(20, 22), 16), 10);
		if (h3Str.length() == 1)
			h3Str = "0" + h3Str;

		String m3Str = Integer.toString(
				Integer.parseInt(conStr.substring(22, 24), 16), 10);
		if (m3Str.length() == 1)
			m3Str = "0" + m3Str;

		String week3Str = MyTool.HexStringToBinary(conStr.substring(24, 26))
				.toString();

		if (reserve1Open.toLowerCase().equals("ff"))
			myApp.setReserve1Mark(reserve1Open + temp1Str + h1Str + m1Str
					+ week1Str);
		if (reserve2Open.toLowerCase().equals("ff"))
			myApp.setReserve2Mark(reserve2Open + temp2Str + h2Str + m2Str
					+ week2Str);
		if (reserve3Open.toLowerCase().equals("ff"))
			myApp.setReserve3Mark(reserve3Open + temp3Str + h3Str + m3Str
					+ week3Str);
	}

	// 室外设置预约
	public static boolean sendReserveFor3G(String reserveNum,
			String rerserveCMD, boolean isOpen) throws JSONException, Exception {
		String[] str = initSetRerserveCmd(false, reserveNum, isOpen,
				rerserveCMD);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("cmdName", str[0]));
		param.add(new BasicNameValuePair("cmdValue", str[1]));
		param.add(new BasicNameValuePair("interval", Integer.toString(TIME))); // 下一次访问的时间差
		param.add(new BasicNameValuePair("username", myApp.getUsername()));
		param.add(new BasicNameValuePair("proType", myApp.getCurProType()));
		param.add(new BasicNameValuePair("authKey", myApp.getCurServerAuthKey()));
		param.add(new BasicNameValuePair("actCode", myApp.getCurServerActCode()));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ KEYFor3G + ";jsessionid=" + myApp.getSessionID(), false);

		if (jObj != null && jObj.has("success")
				&& jObj.getString("success").equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	// 室内设置预约
	public static boolean sendReserveForWifi(String reserveNum,
			String rerserveCMD, boolean isOpen) throws JSONException, Exception {
		String[] str = initSetRerserveCmd(true, reserveNum, isOpen, rerserveCMD);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("", str[1]));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLForWIFI
				+ KEYForWIFI, true);

		if (jObj != null) {
			String reStr = jObj.getString("cmd_rsp");
			if (checkCmd(reStr)) {
				if (reserveNum.equals("1")) {
					myApp.setReserve1Change(false);
				} else if (reserveNum.equals("2")) {
					myApp.setReserve2Change(false);
				} else if (reserveNum.equals("3")) {
					myApp.setReserve3Change(false);
				}
				setReserveTotal(reStr, reserveNum);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// 室外开机
	public static boolean openFor3G() throws JSONException, Exception {
		String cmdV = initOpenCmd(false);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("cmdName", "开机"));
		param.add(new BasicNameValuePair("cmdValue", cmdV));
		param.add(new BasicNameValuePair("interval", Integer.toString(TIME))); // 下一次访问的时间差
		param.add(new BasicNameValuePair("username", myApp.getUsername()));
		param.add(new BasicNameValuePair("proType", myApp.getCurProType()));
		param.add(new BasicNameValuePair("authKey", myApp.getCurServerAuthKey()));
		param.add(new BasicNameValuePair("actCode", myApp.getCurServerActCode()));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ KEYFor3G + ";jsessionid=" + myApp.getSessionID(), false);

		if (jObj != null && jObj.has("success")
				&& jObj.getString("success").equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	// 室内开机
	public static boolean openForWifi() throws JSONException, Exception {
		String jsonStr = initOpenCmd(true);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("", jsonStr));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLForWIFI
				+ KEYForWIFI, true);

		if (jObj != null) {
			String reStr = jObj.getString("cmd_rsp");
			if (checkCmd(reStr)) {
				String conStr = reStr.substring(14, reStr.length());
				String con1Str = conStr.substring(0, 2);
				if (con1Str.equals("10")) {
					return false;
				} else if (con1Str.equals("01")) {
					setRefresh(conStr);
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}

		return false;
	}

	// 室外关机
	public static boolean closeFor3G() throws JSONException, Exception {
		String cmdV = initCloseCmd(false);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("cmdName", "关机"));
		param.add(new BasicNameValuePair("cmdValue", cmdV));
		param.add(new BasicNameValuePair("interval", Integer.toString(TIME))); // 下一次访问的时间差
		param.add(new BasicNameValuePair("username", myApp.getUsername()));
		param.add(new BasicNameValuePair("proType", myApp.getCurProType()));
		param.add(new BasicNameValuePair("authKey", myApp.getCurServerAuthKey()));
		param.add(new BasicNameValuePair("actCode", myApp.getCurServerActCode()));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ KEYFor3G + ";jsessionid=" + myApp.getSessionID(), false);

		if (jObj != null && jObj.has("success")
				&& jObj.getString("success").equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	// 室内关机
	public static boolean closeForWifi() throws JSONException, Exception {
		String jsonStr = initCloseCmd(true);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("", jsonStr));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLForWIFI
				+ KEYForWIFI, true);

		if (jObj != null) {
			String reStr = jObj.getString("cmd_rsp");
			if (checkCmd(reStr)) {
				String conStr = reStr.substring(14, reStr.length());
				String con1Str = conStr.substring(0, 2);
				if (con1Str.equals("10")) {
					return false;
				} else if (con1Str.equals("01")) {
					setRefresh(conStr);
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}

		return false;
	}

	// 室外模式开启
	public static boolean openModeFor3G(String type, String modeCmd,
			String modeTemp, String cmdN) throws JSONException, Exception {
		String cmdV = initSetModeCmd(false, type, modeCmd, modeTemp);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("cmdName", cmdN));
		param.add(new BasicNameValuePair("cmdValue", cmdV));
		param.add(new BasicNameValuePair("interval", Integer.toString(TIME))); // 下一次访问的时间差
		param.add(new BasicNameValuePair("username", myApp.getUsername()));
		param.add(new BasicNameValuePair("proType", myApp.getCurProType()));
		param.add(new BasicNameValuePair("authKey", myApp.getCurServerAuthKey()));
		param.add(new BasicNameValuePair("actCode", myApp.getCurServerActCode()));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLFor3G
				+ KEYFor3G + ";jsessionid=" + myApp.getSessionID(), false);

		if (jObj != null && jObj.has("success")
				&& jObj.getString("success").equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	// 室内模式开启
	public static boolean openModeForWifi(String type, String modeCmd,
			String modeTemp, String cmdN) throws JSONException, Exception {
		String jsonStr = initSetModeCmd(true, type, modeCmd, modeTemp);

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("", jsonStr));

		JSONObject jObj = getServiceActionStr(param, false, BASE_URLForWIFI
				+ KEYForWIFI, true);

		if (jObj != null) {
			String reStr = jObj.getString("cmd_rsp");
			reStr = jObj.getString("cmd_rsp");
			if (checkCmd(reStr)) {
				String conStr = reStr.substring(14, reStr.length());
				String con1Str = conStr.substring(0, 2);
				String modeTempStr = Integer.toString(
						Integer.parseInt(conStr.substring(2, 4), 16), 10);
				myApp.setTempMark(modeTempStr);
				if (con1Str.equals("10")) {
					return false;
				} else if (con1Str.equals("01")) {
					setRefresh(conStr);
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}

		return false;
	}

	// 室内播报数据处理
	// private String refreshCmd = "AA5501 40 010100 01 ...";// 状态刷新广播
	// private String reserveCmd = "AA5501 40 010400 ...";// 预约刷新广播
	// {"ip":" 192.168.10.103","event":"AA550140020100010000000000000000000000BB"}
	// {"ip":"192.168.0.102","event":"AA5501 40 010100 0105001A00000880002F3C00AA"}


	public static void setMSData(String jsonStr) throws JSONException {
		JSONObject jObj = new JSONObject(jsonStr);

		if (LoadData.isWifi) {
			if (!jObj.has("ip")
					|| !jObj.getString("ip").equals(myApp.getCurWifiIP())) {
				return;
			}

			String msCmd = jObj.getString("event");
			String cmdName = msCmd.substring(8, 14);

			if (cmdName.equals(LoadData.refreshCmd)) {// 状态刷新
				String conStr = msCmd.substring(14, msCmd.length());
				LoadData.setRefresh(conStr);

				// 判断如果有故障则发消息提示
				boolean hasErr = false;
				String errStr = myApp.getRefErrMark();
				if (errStr.equals("00000000")) {
					hasErr = false;
				} else {
					hasErr = true;
				}

				Intent intent = new Intent();
				intent.putExtra("tag", "refreshEvent");
				// intent.putExtra("error", true);
				intent.putExtra("activity", "main");
				intent.setAction("meta.midea.MainActivity");
				myApp.getMyService().sendBroadcast(intent);

				if (hasErr && !myApp.isHasErr()) {
					myApp.getMyService().openNotification("热水器有故障", "1",
							MainActivity.class);
					myApp.setHasErr(true);
				}
			} else if (cmdName.equals(LoadData.reserveCmd)) {// 预约刷新
				LoadData.setReserveTotal(msCmd, "1");

				Intent intent = new Intent();
				intent.putExtra("tag", "refreshReserveEvent");
				intent.putExtra("activity", "main");
				intent.setAction("meta.midea.MainActivity");
				myApp.getMyService().sendBroadcast(intent);
			}
		}
	}

	// 室外播报数据处理
	public static void setMSDataForSocket(String jsonStr) throws JSONException {
		JSONObject jObj = new JSONObject(jsonStr);

		if (!LoadData.isWifi) {
			String msCmd = jObj.getString("event");
			String cmdName = msCmd.substring(8, 14);

			if (cmdName.equals(LoadData.refreshCmd)) {// 状态刷新
				String conStr = msCmd.substring(14, msCmd.length());
				LoadData.setRefresh(conStr);

				// 判断如果有故障则发消息提示
				boolean hasErr = false;
				String errStr = myApp.getRefErrMark();
				if (errStr.equals("00000000")) {
					hasErr = false;
				} else {
					hasErr = true;
				}

				Intent intent = new Intent();
				intent.putExtra("tag", "refreshEvent");
				// intent.putExtra("error", true);
				intent.putExtra("activity", "main");
				intent.setAction("meta.midea.MainActivity");
				myApp.getMyService().sendBroadcast(intent);

				if (hasErr && !myApp.isHasErr()) {
					myApp.getMyService().openNotification("热水器有故障", "1",
							MainActivity.class);
					myApp.setHasErr(true);
				}
			} else if (cmdName.equals(LoadData.reserveCmd)) {// 预约刷新
				LoadData.setReserveTotal(msCmd, "1");

				Intent intent = new Intent();
				intent.putExtra("tag", "refreshReserveEvent");
				intent.putExtra("activity", "main");
				intent.setAction("meta.midea.MainActivity");
				myApp.getMyService().sendBroadcast(intent);
			}
		}
	}

	public static boolean checkCmd(String cmdStr) {
		// 判断命令是否正确
		String endStr = getEndChar(cmdStr.substring(0, 38));
		if (!endStr.equals(cmdStr.substring(38, 40))) {
			return false;
		}

		return true;
	}

	// 尾部补足0
	private static String addZero(String cmd) {
		char[] cmdCArr = cmd.toCharArray();
		char[] reCArr = new char[38];
		for (int i = 0; i < 38; i++) {
			if (i < cmdCArr.length) {
				reCArr[i] = cmdCArr[i];
			} else {
				reCArr[i] = '0';
			}
		}
		return String.valueOf(reCArr);
	}

	// 求和计算第二十个字节
	private static String getEndChar(String cmd) {
		String s1 = cmd.substring(4, cmd.length());
		char[] s1Arr = s1.toCharArray();
		int total = 0;
		for (int i = 0; i < s1Arr.length; i = i + 2) {
			String s2 = "";
			s2 = s2 + s1Arr[i];
			s2 = s2 + s1Arr[i + 1];
			int s2int = Integer.parseInt(s2, 16);

			total = total + s2int;
		}
		int endInt = (total ^ 0xFF) + 1;
		String returnStr = Integer.toHexString(endInt).toUpperCase();
		if (returnStr.length() > 2) {
			returnStr = returnStr.substring(returnStr.length() - 2,
					returnStr.length());
		}

		if (returnStr.length() == 1) {
			returnStr = "0" + returnStr;
		}

		return returnStr;
	}

	// 发送请求
	public static JSONObject getServiceActionStr(List<NameValuePair> params,
			boolean isGet, String url, boolean isWifi) throws JSONException,
			Exception {
		if (!getSocketState()) {
			getCloudContent();
		}

		JSONObject jsonObj = null;
		if (isGet) {
			jsonObj = getJSONObject(HttpServer.getGetRequest(url, params));
		} else {
			jsonObj = getJSONObject(HttpServer.getPostRequest(url, params,
					isWifi));
		}
		return jsonObj;
	}

	public static JSONObject getJSONObject(String str) {
		JSONObject job = null;
		try {
			if (!str.equals("{}") && !str.equals("timeout")) {
				job = new JSONObject(str);
				if (job.has("error") && job.getString("error").equals("-1")) {
					return null;
				} else if (job.has("tag")
						&& job.getString("tag").equals("Error")) {
					myApp.setLoadErr("用户名或密码错误，请检查");
					return null;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return job;
	}

	/************* 开启socket *************/
	// 判断socket是否断开
	private static boolean getSocketState() {
		if (myApp.getMyService().mySocket == null
				|| !myApp.getMyService().mySocket.isConnect) {
			return false;
		}

		try {
			myApp.getMyService().mySocket.write("");
		} catch (Exception e) {
			System.out.println("socket不通了");
			return false;
		}

		return true;
	}

	// 获取同云端的连接
	public static void getCloudContent() {
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					msg.obj = myApp.getMyService().openSocket();
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForGetCloudContent.sendMessage(msg);
			}
		}.start();
	}

	private static Handler handlerForGetCloudContent = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null && (Boolean) msg.obj) {
					System.out.println("socket重新连接了");
				} else {
					System.out.println("socket连接失败，请检查网络连接是否正常！");
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	/********************* 封装指令 *********************/
	// 封装获取机型指令
	private static String initGetTypeCmd(boolean iswifi) {
		ArrayList<String> curTimeArr = MyTool.getCurTime24();
		String hourStr = Integer.toString(
				Integer.parseInt(curTimeArr.get(3), 10), 16);
		String minuteStr = Integer.toString(
				Integer.parseInt(curTimeArr.get(4), 10), 16);
		Calendar calendar = Calendar.getInstance();
		String curDayIndex = Integer.toString(
				calendar.get(Calendar.DAY_OF_WEEK), 16);
		String cloudTemp = Integer.toString(
				Integer.parseInt(myApp.getTempMark(), 10), 16);

		// 处理请求命令
		String getServeTypeCmdStr = startCmd + modelNum1Cmd + reqCmd
				+ getServerTypeCmd
				+ (hourStr.length() == 1 ? "0" + hourStr : hourStr)
				+ (minuteStr.length() == 1 ? "0" + minuteStr : minuteStr)
				+ (curDayIndex.length() == 1 ? "0" + curDayIndex : curDayIndex)
				+ (cloudTemp.length() == 1 ? "0" + cloudTemp : cloudTemp);
		String totalActCmd = addZero(getServeTypeCmdStr);
		String endChar = getEndChar(totalActCmd);
		String jsonStr = "{\"cmd_req\":\"" + totalActCmd + endChar + "\""
				+ (isAddAuthKey ? ", \"authkey\":\"" + authkey + "\"" : "")
				+ "}";

		String cmdV = "cmd_req:" + totalActCmd + endChar;

		if (iswifi) {
			return jsonStr;
		} else {
			return cmdV;
		}
	}

	// 封装刷新状态指令
	private static String initRefreshCmd(boolean iswifi) {
		String getStateCmdStr = startCmd + modelNum1Cmd + reqCmd + getStateCmd
				+ "01";
		String totalActCmd = addZero(getStateCmdStr);
		String endChar = getEndChar(totalActCmd);
		String jsonStr = "{\"cmd_req\":\"" + totalActCmd + endChar + "\""
				+ (isAddAuthKey ? ", \"authkey\":\"" + authkey + "\"" : "")
				+ "}";
		String cmdV = "cmd_req:" + totalActCmd + endChar;

		if (iswifi) {
			return jsonStr;
		} else {
			return cmdV;
		}
	}

	// 封装预约刷新指令
	private static String[] initRefreshRerserveCmd(boolean iswifi,
			String reserveNum) {
		String[] returnStr = new String[2];

		String cmd = "";
		String cmdN = "";
		if (reserveNum.equals("1")) {
			cmd = getReserve1Cmd;
			cmdN = "刷新预约一";
		} else if (reserveNum.equals("2")) {
			cmd = getReserve2Cmd;
			cmdN = "刷新预约二";
		} else if (reserveNum.equals("3")) {
			cmd = getReserve3Cmd;
			cmdN = "刷新预约三";
		}
		returnStr[0] = cmdN;

		String getStateCmdStr = startCmd + modelNum1Cmd + reqCmd + cmd + "01";
		String totalActCmd = addZero(getStateCmdStr);
		String endChar = getEndChar(totalActCmd);
		String jsonStr = "{\"cmd_req\":\"" + totalActCmd + endChar + "\""
				+ (isAddAuthKey ? ", \"authkey\":\"" + authkey + "\"" : "")
				+ "}";
		String cmdV = "cmd_req:" + totalActCmd + endChar;

		if (iswifi) {
			returnStr[1] = jsonStr;
		} else {
			returnStr[1] = cmdV;
		}

		return returnStr;
	}

	// 封装设置预约指令
	private static String[] initSetRerserveCmd(boolean iswifi,
			String reserveNum, boolean isOpen, String rerserveCMD) {
		String[] returnStr = new String[2];

		String cmd = "";
		String cmdN = "";
		if (reserveNum.equals("1")) {
			cmd = reserve1Cmd;
			cmdN = (isOpen ? "设置" : "取消") + "预约一";
		} else if (reserveNum.equals("2")) {
			cmd = reserve2Cmd;
			cmdN = (isOpen ? "设置" : "取消") + "预约二";
		} else if (reserveNum.equals("3")) {
			cmd = reserve3Cmd;
			cmdN = (isOpen ? "设置" : "取消") + "预约三";
		}
		returnStr[0] = cmdN;

		String tempStr = Integer.toString(
				Integer.parseInt(rerserveCMD.substring(2, 4), 10), 16);
		String setHourStr = Integer.toString(
				Integer.parseInt(rerserveCMD.substring(4, 6), 10), 16);
		String setMinuteStr = Integer.toString(
				Integer.parseInt(rerserveCMD.substring(6, 8), 10), 16);

		String rerserveCMDStr = rerserveCMD.substring(0, 2)
				+ ((tempStr.length() == 1) ? ("0" + tempStr) : tempStr)
				+ ((setHourStr.length() == 1) ? ("0" + setHourStr) : setHourStr)
				+ ((setMinuteStr.length() == 1) ? ("0" + setMinuteStr)
						: setMinuteStr)
				+ MyTool.BinaryToHexString(rerserveCMD.substring(8)).toString();

		ArrayList<String> curTimeArr = MyTool.getCurTime24();
		String hourStr = Integer.toString(
				Integer.parseInt(curTimeArr.get(3), 10), 16);
		String minuteStr = Integer.toString(
				Integer.parseInt(curTimeArr.get(4), 10), 16);
		Calendar calendar = Calendar.getInstance();
		String curDayIndex = Integer.toString(
				calendar.get(Calendar.DAY_OF_WEEK), 16);

		String getStateCmdStr = startCmd + modelNum1Cmd + reqCmd + cmd + "01"
				+ rerserveCMDStr
				+ (hourStr.length() == 1 ? "0" + hourStr : hourStr)
				+ (minuteStr.length() == 1 ? "0" + minuteStr : minuteStr)
				+ (curDayIndex.length() == 1 ? "0" + curDayIndex : curDayIndex);
		String totalActCmd = addZero(getStateCmdStr);
		String endChar = getEndChar(totalActCmd);
		String jsonStr = "{\"cmd_req\":\"" + totalActCmd + endChar + "\""
				+ (isAddAuthKey ? ", \"authkey\":\"" + authkey + "\"" : "")
				+ "}";
		String cmdV = "cmd_req:" + totalActCmd + endChar;

		if (iswifi) {
			returnStr[1] = jsonStr;
		} else {
			returnStr[1] = cmdV;
		}

		return returnStr;
	}

	// 封装开机指令
	private static String initOpenCmd(boolean iswifi) {
		String openCmdStr = startCmd + modelNum1Cmd + reqCmd + openCmd + "01";
		String totalActCmd = addZero(openCmdStr);
		String endChar = getEndChar(totalActCmd);
		String jsonStr = "{\"cmd_req\":\"" + totalActCmd + endChar + "\""
				+ (isAddAuthKey ? ", \"authkey\":\"" + authkey + "\"" : "")
				+ "}";
		String cmdV = "cmd_req:" + totalActCmd + endChar;

		if (iswifi) {
			return jsonStr;
		} else {
			return cmdV;
		}
	}

	// 封装关机指令
	private static String initCloseCmd(boolean iswifi) {
		String closeCmdStr = startCmd + modelNum1Cmd + reqCmd + closeCmd + "01";
		String totalActCmd = addZero(closeCmdStr);
		String endChar = getEndChar(totalActCmd);
		String jsonStr = "{\"cmd_req\":\"" + totalActCmd + endChar + "\""
				+ (isAddAuthKey ? ", \"authkey\":\"" + authkey + "\"" : "")
				+ "}";
		String cmdV = "cmd_req:" + totalActCmd + endChar;

		if (iswifi) {
			return jsonStr;
		} else {
			return cmdV;
		}
	}

	// 封装模式设置指令
	private static String initSetModeCmd(boolean iswifi, String type,
			String modeCmd, String modeTemp) {
		String modeCmdStr = "";
		if (type.equals("mode")) {
			String modeCmd16 = MyTool.BinaryToHexString(modeCmd).toString();
			modeCmdStr = startCmd + modelNum1Cmd + reqCmd + setModeCmd + "01"
					+ modeCmd16 + modeTemp;
		} else if (type.equals("dra")) {
			modeCmdStr = startCmd + modelNum1Cmd + reqCmd + setDraCmd + "0100";
		} else if (type.equals("war")) {
			modeCmdStr = startCmd + modelNum1Cmd + reqCmd + setWarCmd + "0100";
		}

		String totalActCmd = addZero(modeCmdStr);
		String endChar = getEndChar(totalActCmd);
		String jsonStr = "{\"cmd_req\":\"" + totalActCmd + endChar + "\""
				+ (isAddAuthKey ? ", \"authkey\":\"" + authkey + "\"" : "")
				+ "}";
		String cmdV = "cmd_req:" + totalActCmd + endChar;

		if (iswifi) {
			return jsonStr;
		} else {
			return cmdV;
		}
	}
}
