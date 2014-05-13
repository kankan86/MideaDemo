package meta.midea;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import meta.midea.data.LoadData;
import meta.midea.service.MyService;
import meta.midea.tool.MyExceptionHandler;
import meta.midea.tool.WifiAdmin;

import org.jboss.netty.channel.Channel;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {
	private List<Activity> activityList = new LinkedList<Activity>();
	private static MyApplication instance;
	private WifiAdmin wifiAdmin;
	private Channel channel;

	private String stateStr; // 热水器状态字符串

	private String msStateMark = "00000001"; // 广播返回状态信息

	private String refStateMark = "00000100"; // 刷新返回状态信息
	private String refErrMark = "00000000"; // 刷新返回故障信息
	private String refRSLMark = "80"; // 刷新返回热水量信息
	private String refSJWDMark = "26"; // 刷新返回实际温度信息
	private String refSLLMark = "00000000"; // 刷新返回水流量信息
	private String refModeMark = "0000000010000000"; // 刷新返回模式开启信息
	private String refHeatingTimeHMark = "00";// 刷新返回加热剩余小时
	private String refHeatingTimeMMark = "30";// 刷新返回加热剩余分钟

	private String panelMark = "00001111";// 面板标示
	private String setMark = "0000001111111111"; // 设置标示
	private String mode1Mark = "00111111"; // 速热模式标示(新版b4=夏季 b5=冬季)
	private String mode2Mark = "00000011"; // 节能模式标示
	private String mode3Mark = "00111111"; // 分人洗模式标示
	private String mode4Mark = "00000111"; // 健康模式标示
	private String state1Mark = "00111110"; // 状态一标示
	private String state2Mark = "00000010"; // 状态二标示
	private String errorMark = "00000000"; // 故障标示
	private String ltypeMark = "00000101"; // 机型升数标示

	private List<Map<String, Object>> reserveList;
	private String reserve1Mark = "0075200011111111"; // 预约一标示
	private String reserve2Mark = "0075080010111110"; // 预约二标示
	private String reserve3Mark = "0075140010111110"; // 预约三标示

	private String tempMark = "75"; // 设置温度
	private boolean reserve1Change = false; // 预约一有改变
	private boolean reserve2Change = false; // 预约二有改变
	private boolean reserve3Change = false; // 预约三有改变

	private String msModeMark = "000000000000000000000000";

	private boolean isScouring = false; // 是否是洗浴状态，洗浴状态下不可发送任何指令

	private boolean isFirstLoadMain = true;// 是否是第一次加载主页面
	private boolean isDemoMode = false; // 是否是Demo演示进入系统
	private boolean isTestMode = false; // 是否是检验进入系统
	private boolean isReserveToMain = false; // 是否预约设置返回主面板
	private boolean isReserveToMainAndClose = false; // 是否预约设置返回主面板，并且是取消操作
	private String reserveToMainCloseName = "1";// 记录预约页面“取消”的是哪个预约
	private boolean isHasErr = false;// 记录有故障信息

	private String curReserveSet;// 当前编辑的预约(1,2,3)
	private String curModeName = "";// 当前选中的及时洗模式

	private String wifiName = "muRataAP";// 热水器设备wifi名前缀
	private String serverType = "_HostlessWiFi._tcp.local.";// 设备类型
	private String serverName = "TypeAAF-E149C2";// 设备名称
	private String serverIP = "192.168.254.23";// 设备IP
	private int port = 3000;// udp监听端口
	private String group = "239.255.255.250"; // udp监听groupIP
	private String cloudUrl;// 云端服务地址
	private String sockHost;// socket监听地址
	private int socketPort;// socket监听端口
	private String cloudUrlForWebSocket;// 云端服务地址WebService
	private String sockHostForWebSocket;// socket监听地址WebService
	private int socketPortForWebSocket;// socket监听端口WebService

	private String staticIp;// 静态IP
	private String ipMask;// 子网掩码
	private String ipGW;// 默认网关
	private String ipDns1;
	private String ipDns2;
	private String localIp;// 本地IP

	private String textCity;// 城市代码Json字符串

	private String isSearchAll;// 是否搜索全部jmdns
	private boolean isAct = false;// 是激活还是跳转主面板
	private boolean isBindToReg = false;// 是绑定页面跳转到注册页面，还是登陆页面跳转到注册页面
	private boolean isIntLogToGetServers = false; // 是否是室外登陆账号，是则去加载设备信息，否不去加载跳转主页面
	private boolean isApMode = false;// 是否是AP下的配置(针对wifi的操作：true，AP配置；false，wifi网络及云端修改)

	private JSONArray userArr;// 本地保存账号列表
	private String username;// 云账号名称
	private String userpass;// 云账号密码
	private String sessionID;// 云账号sessionID
	private String curWifiIP; // 当前热水器IP
	private String curServerUUID;// 当前热水器UUID
	private String curServerBaseName;// 当前热水器名称(是指热水器本身的出厂名字)
	private String curServerName;// 当前设备本地保存名称(是指用户给设备起的名字)
	private String curServerActCode;// 当前设备激活码
	private String curServerAuthKey;// 当前设备authKey

	private String curServerWeather = "0";// 当前设备所在地实时温度
	private String curServerCloudTemp;// 当前设备所在地匹配的云智能温度
	private String curServerIP;// 当前设备所在地IP
	private String curServerCity;// 当前设备所在地城市名
	private String curServerCityCode;// 当前设备所在地城市代码

	private String curProType;// 当前设备类型

	private JSONArray cloudServersList; // 从云端拿到的设备列表
	private String cloudAddServerStr;// 新增设备返回信息

	private String newVersionUrl;// 新版本更新地址

	private int faceLevel = 0; // 皮肤码

	private boolean isNoNetwork = false;// 记录是否有网络可用

	private int refreshTimer = 60;// 每隔多少秒刷新指令，默认120秒

	private MyService myService;

	private String loadErr = "";// 请求返回的错误信息(针对室外登陆注册返回信息)

	// private List<String> modellist = new ArrayList<String>() {
	// {
	// add("C8650");
	// }
	// };
	// private static final String FILENAME = "temp_file.txt";

	@Override
	public void onCreate() {
		super.onCreate();
		// 捕获异常
		Thread.setDefaultUncaughtExceptionHandler(MyExceptionHandler
				.getInstance(getApplicationContext()));
	}

	// 单例模式中获取唯一的MyApplication实例
	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public void exit(boolean isCallCloud) {
		// if (getChannel() != null)
		// getChannel().write(
		// "[\"logout\":\"" + getUsername() + "\"]" + "\r\n");

		if (!LoadData.isWifi && isCallCloud) {
			try {
				LoadData.exit();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (Activity activity : activityList) {
			activity.finish();
			if (getMyService() != null) {
				getMyService().closeNotification();
				getMyService().stopSelf();
			}
		}
		System.exit(0);
	}

	public boolean isHasErr() {
		return isHasErr;
	}

	public void setHasErr(boolean isHasErr) {
		this.isHasErr = isHasErr;
	}

	public String getReserveToMainCloseName() {
		return reserveToMainCloseName;
	}

	public void setReserveToMainCloseName(String reserveToMainCloseName) {
		this.reserveToMainCloseName = reserveToMainCloseName;
	}

	public boolean isReserveToMainAndClose() {
		return isReserveToMainAndClose;
	}

	public void setReserveToMainAndClose(boolean isReserveToMainAndClose) {
		this.isReserveToMainAndClose = isReserveToMainAndClose;
	}

	public boolean isNoNetwork() {
		return isNoNetwork;
	}

	public void setNoNetwork(boolean isNoNetwork) {
		this.isNoNetwork = isNoNetwork;
	}

	public String getCurServerUUID() {
		return curServerUUID;
	}

	public void setCurServerUUID(String curServerUUID) {
		this.curServerUUID = curServerUUID;
	}

	public String getIpDns1() {
		return ipDns1;
	}

	public void setIpDns1(String ipDns1) {
		this.ipDns1 = ipDns1;
	}

	public String getIpDns2() {
		return ipDns2;
	}

	public void setIpDns2(String ipDns2) {
		this.ipDns2 = ipDns2;
	}

	public String getLoadErr() {
		return loadErr;
	}

	public void setLoadErr(String loadErr) {
		this.loadErr = loadErr;
	}

	public boolean isApMode() {
		return isApMode;
	}

	public void setApMode(boolean isApMode) {
		this.isApMode = isApMode;
	}

	public String getNewVersionUrl() {
		return newVersionUrl;
	}

	public void setNewVersionUrl(String newVersionUrl) {
		this.newVersionUrl = newVersionUrl;
	}

	public String getCurServerCityCode() {
		return curServerCityCode;
	}

	public void setCurServerCityCode(String curServerCityCode) {
		this.curServerCityCode = curServerCityCode;
	}

	public boolean isFirstLoadMain() {
		return isFirstLoadMain;
	}

	public void setFirstLoadMain(boolean isFirstLoadMain) {
		this.isFirstLoadMain = isFirstLoadMain;
	}

	public String getCurServerCloudTemp() {
		return curServerCloudTemp;
	}

	public void setCurServerCloudTemp(String curServerCloudTemp) {
		this.curServerCloudTemp = curServerCloudTemp;
	}

	public String getCurServerIP() {
		return curServerIP;
	}

	public void setCurServerIP(String curServerIP) {
		this.curServerIP = curServerIP;
	}

	public String getCurServerCity() {
		return curServerCity;
	}

	public void setCurServerCity(String curServerCity) {
		this.curServerCity = curServerCity;
	}

	public String getCurServerWeather() {
		return curServerWeather;
	}

	public void setCurServerWeather(String curServerWeather) {
		this.curServerWeather = curServerWeather;
	}

	public boolean isReserveToMain() {
		return isReserveToMain;
	}

	public void setReserveToMain(boolean isReserveToMain) {
		this.isReserveToMain = isReserveToMain;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getCloudUrlForWebSocket() {
		return cloudUrlForWebSocket;
	}

	public void setCloudUrlForWebSocket(String cloudUrlForWebSocket) {
		this.cloudUrlForWebSocket = cloudUrlForWebSocket;
	}

	public String getSockHostForWebSocket() {
		return sockHostForWebSocket;
	}

	public void setSockHostForWebSocket(String sockHostForWebSocket) {
		this.sockHostForWebSocket = sockHostForWebSocket;
	}

	public int getSocketPortForWebSocket() {
		return socketPortForWebSocket;
	}

	public void setSocketPortForWebSocket(int socketPortForWebSocket) {
		this.socketPortForWebSocket = socketPortForWebSocket;
	}

	public String getRefHeatingTimeHMark() {
		return refHeatingTimeHMark;
	}

	public void setRefHeatingTimeHMark(String refHeatingTimeHMark) {
		this.refHeatingTimeHMark = refHeatingTimeHMark;
	}

	public String getRefHeatingTimeMMark() {
		return refHeatingTimeMMark;
	}

	public void setRefHeatingTimeMMark(String refHeatingTimeMMark) {
		this.refHeatingTimeMMark = refHeatingTimeMMark;
	}

	public boolean isTestMode() {
		return isTestMode;
	}

	public void setTestMode(boolean isTestMode) {
		this.isTestMode = isTestMode;
	}

	public boolean isDemoMode() {
		return isDemoMode;
	}

	public void setDemoMode(boolean isDemoMode) {
		this.isDemoMode = isDemoMode;
	}

	public String getCurModeName() {
		return curModeName;
	}

	public void setCurModeName(String curModeName) {
		this.curModeName = curModeName;
	}

	public String getCurReserveSet() {
		return curReserveSet;
	}

	public void setCurReserveSet(String curReserveSet) {
		this.curReserveSet = curReserveSet;
	}

	public String getCurServerBaseName() {
		return curServerBaseName;
	}

	public void setCurServerBaseName(String curServerBaseName) {
		this.curServerBaseName = curServerBaseName;
	}

	public JSONArray getUserArr() {
		return userArr;
	}

	public void setUserArr(JSONArray userArr) {
		this.userArr = userArr;
	}

	public MyService getMyService() {
		return myService;
	}

	public void setMyService(MyService myService) {
		this.myService = myService;
	}

	public String getCloudAddServerStr() {
		return cloudAddServerStr;
	}

	public void setCloudAddServerStr(String cloudAddServerStr) {
		this.cloudAddServerStr = cloudAddServerStr;
	}

	public int getRefreshTimer() {
		return refreshTimer;
	}

	public void setRefreshTimer(int refreshTimer) {
		this.refreshTimer = refreshTimer;
	}

	public String getCurProType() {
		return curProType;
	}

	public void setCurProType(String curProType) {
		this.curProType = curProType;
	}

	public JSONArray getCloudServersList() {
		return cloudServersList;
	}

	public void setCloudServersList(JSONArray cloudServersList) {
		this.cloudServersList = cloudServersList;
	}

	public boolean isIntLogToGetServers() {
		return isIntLogToGetServers;
	}

	public void setIntLogToGetServers(boolean isIntLogToGetServers) {
		this.isIntLogToGetServers = isIntLogToGetServers;
	}

	public boolean isBindToReg() {
		return isBindToReg;
	}

	public void setBindToReg(boolean isBindToReg) {
		this.isBindToReg = isBindToReg;
	}

	public String getPanelMark() {
		return panelMark;
	}

	public void setPanelMark(String panelMark) {
		this.panelMark = panelMark;
	}

	public int getFaceLevel() {
		return faceLevel;
	}

	public void setFaceLevel(int faceLevel) {
		this.faceLevel = faceLevel;
	}

	public boolean isScouring() {
		return isScouring;
	}

	public void setScouring(boolean isScouring) {
		this.isScouring = isScouring;
	}

	public List<Activity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<Activity> activityList) {
		this.activityList = activityList;
	}

	public String getCurServerAuthKey() {
		return curServerAuthKey;
	}

	public void setCurServerAuthKey(String curServerAuthKey) {
		this.curServerAuthKey = curServerAuthKey;
	}

	public String getCurServerActCode() {
		return curServerActCode;
	}

	public void setCurServerActCode(String curServerActCode) {
		this.curServerActCode = curServerActCode;
	}

	public String getCurServerName() {
		return curServerName;
	}

	public void setCurServerName(String curServerName) {
		this.curServerName = curServerName;
	}

	public String getCurWifiIP() {
		return curWifiIP;
	}

	public void setCurWifiIP(String curWifiIP) {
		this.curWifiIP = curWifiIP;
	}

	public boolean isAct() {
		return isAct;
	}

	public void setAct(boolean isAct) {
		this.isAct = isAct;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpass() {
		return userpass;
	}

	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}

	public String getIpMask() {
		return ipMask;
	}

	public void setIpMask(String ipMask) {
		this.ipMask = ipMask;
	}

	public String getIpGW() {
		return ipGW;
	}

	public void setIpGW(String ipGW) {
		this.ipGW = ipGW;
	}

	public WifiAdmin getWifiAdmin() {
		return wifiAdmin;
	}

	public void setWifiAdmin(WifiAdmin wifiAdmin) {
		this.wifiAdmin = wifiAdmin;
	}

	public String getCloudUrl() {
		return cloudUrl;
	}

	public void setCloudUrl(String cloudUrl) {
		this.cloudUrl = cloudUrl;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public String getStaticIp() {
		return staticIp;
	}

	public void setStaticIp(String staticIp) {
		this.staticIp = staticIp;
	}

	public String getSockHost() {
		return sockHost;
	}

	public void setSockHost(String sockHost) {
		this.sockHost = sockHost;
	}

	public int getSocketPort() {
		return socketPort;
	}

	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}

	public String getIsSearchAll() {
		return isSearchAll;
	}

	public void setIsSearchAll(String issearchall) {
		this.isSearchAll = issearchall;
	}

	public String getWifiName() {
		return wifiName;
	}

	public void setWifiName(String wifiName) {
		this.wifiName = wifiName;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	// public List<String> getModellist() {
	// return modellist;
	// }
	//
	// public void setModellist(List<String> modellist) {
	// this.modellist = modellist;
	// }

	public String getTextCity() {
		return textCity;
	}

	public void setTextCity(String textCity) {
		this.textCity = textCity;
	}

	public String getStateStr() {
		return stateStr;
	}

	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}

	public String getMsStateMark() {
		return msStateMark;
	}

	public void setMsStateMark(String msStateMark) {
		this.msStateMark = msStateMark;
	}

	public String getRefStateMark() {
		return refStateMark;
	}

	public void setRefStateMark(String refStateMark) {
		this.refStateMark = refStateMark;
	}

	public String getRefErrMark() {
		return refErrMark;
	}

	public void setRefErrMark(String refErrMark) {
		this.refErrMark = refErrMark;
	}

	public String getRefRSLMark() {
		return refRSLMark;
	}

	public void setRefRSLMark(String refRSLMark) {
		this.refRSLMark = refRSLMark;
	}

	public String getRefSJWDMark() {
		return refSJWDMark;
	}

	public void setRefSJWDMark(String refSJWDMark) {
		this.refSJWDMark = refSJWDMark;
	}

	public String getRefSLLMark() {
		return refSLLMark;
	}

	public void setRefSLLMark(String refSLLMark) {
		this.refSLLMark = refSLLMark;
	}

	public String getRefModeMark() {
		return refModeMark;
	}

	public void setRefModeMark(String refModeMark) {
		this.refModeMark = refModeMark;
	}

	public String getSetMark() {
		return setMark;
	}

	public void setSetMark(String setMark) {
		this.setMark = setMark;
	}

	public String getMode1Mark() {
		return mode1Mark;
	}

	public void setMode1Mark(String mode1Mark) {
		this.mode1Mark = mode1Mark;
	}

	public String getMode2Mark() {
		return mode2Mark;
	}

	public void setMode2Mark(String mode2Mark) {
		this.mode2Mark = mode2Mark;
	}

	public String getMode3Mark() {
		return mode3Mark;
	}

	public void setMode3Mark(String mode3Mark) {
		this.mode3Mark = mode3Mark;
	}

	public String getMode4Mark() {
		return mode4Mark;
	}

	public void setMode4Mark(String mode4Mark) {
		this.mode4Mark = mode4Mark;
	}

	public String getState1Mark() {
		return state1Mark;
	}

	public void setState1Mark(String state1Mark) {
		this.state1Mark = state1Mark;
	}

	public String getState2Mark() {
		return state2Mark;
	}

	public void setState2Mark(String state2Mark) {
		this.state2Mark = state2Mark;
	}

	public String getErrorMark() {
		return errorMark;
	}

	public void setErrorMark(String errorMark) {
		this.errorMark = errorMark;
	}

	public String getLtypeMark() {
		return ltypeMark;
	}

	public void setLtypeMark(String ltypeMark) {
		this.ltypeMark = ltypeMark;
	}

	public List<Map<String, Object>> getReserveList() {
		return reserveList;
	}

	public void setReserveList(List<Map<String, Object>> reserveList) {
		this.reserveList = reserveList;
	}

	public String getReserve1Mark() {
		return reserve1Mark;
	}

	public void setReserve1Mark(String reserve1Mark) {
		this.reserve1Mark = reserve1Mark;
	}

	public String getReserve2Mark() {
		return reserve2Mark;
	}

	public void setReserve2Mark(String reserve2Mark) {
		this.reserve2Mark = reserve2Mark;
	}

	public String getReserve3Mark() {
		return reserve3Mark;
	}

	public void setReserve3Mark(String reserve3Mark) {
		this.reserve3Mark = reserve3Mark;
	}

	public String getTempMark() {
		return tempMark;
	}

	public void setTempMark(String tempMark) {
		this.tempMark = tempMark;
	}

	public boolean isReserve1Change() {
		return reserve1Change;
	}

	public void setReserve1Change(boolean reserve1Change) {
		this.reserve1Change = reserve1Change;
	}

	public boolean isReserve2Change() {
		return reserve2Change;
	}

	public void setReserve2Change(boolean reserve2Change) {
		this.reserve2Change = reserve2Change;
	}

	public boolean isReserve3Change() {
		return reserve3Change;
	}

	public void setReserve3Change(boolean reserve3Change) {
		this.reserve3Change = reserve3Change;
	}

	public String getMsModeMark() {
		return msModeMark;
	}

	public void setMsModeMark(String msModeMark) {
		this.msModeMark = msModeMark;
	}
}
