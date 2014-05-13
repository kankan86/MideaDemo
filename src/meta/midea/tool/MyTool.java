package meta.midea.tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceTypeListener;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

public class MyTool {
	public static ProgressDialog mypDialog = null;
	private static Activity dialogAct = null;
	private static AlertDialog.Builder dialogClose;

	// 读文件方法
	public String read(String fileName, Activity act) {
		try {
			FileInputStream inputStream = act.openFileInput(fileName);
			byte[] b = new byte[inputStream.available()];
			inputStream.read(b);
			return new String(b);
		} catch (Exception e) {
		}
		return null;
	}

	// 写文件
	public void write(String content, String filename, Activity act) {
		try {
			FileOutputStream fos = act.openFileOutput(filename,
					Context.MODE_APPEND);
			fos.write(content.getBytes());
			fos.close();
		} catch (Exception e) {
		}
	}

	public static String getRunningServicesInfo(Context context) {
		StringBuffer serviceInfo = new StringBuffer();
		final ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> services = activityManager
				.getRunningServices(100);

		Iterator<RunningServiceInfo> l = services.iterator();
		while (l.hasNext()) {
			RunningServiceInfo si = (RunningServiceInfo) l.next();
			serviceInfo.append("pid: ").append(si.pid);
			serviceInfo.append("\nprocess: ").append(si.process);
			serviceInfo.append("\nservice: ").append(si.service);
			serviceInfo.append("\ncrashCount: ").append(si.crashCount);
			serviceInfo.append("\nclientCount: ").append(si.clientCount);
			serviceInfo.append("\nactiveSince: ").append(si.activeSince);
			serviceInfo.append("\nlastActivityTime: ").append(
					si.lastActivityTime);
			serviceInfo.append("\n\n");
		}
		return serviceInfo.toString();
	}

	// 获取当前经纬度
	public static Map<String, String> getGSP(Context context) {
		// 声明LocationManager对象
		LocationManager loctionManager;
		String contextService = Context.LOCATION_SERVICE;
		// 通过系统服务，取得LocationManager对象
		loctionManager = (LocationManager) context
				.getSystemService(contextService);

		// 通过GPS位置提供器获得位置
		String provider = LocationManager.GPS_PROVIDER;
		Location location = loctionManager.getLastKnownLocation(provider);

		if (location == null) {
			// 通过网络位置提供器获得位置
			provider = LocationManager.NETWORK_PROVIDER;
			location = loctionManager.getLastKnownLocation(provider);
		}

		String latStr = "";
		String lngStr = "";
		if (location != null) {
			latStr = location.getLatitude() + "";// 纬度
			lngStr = location.getLongitude() + "";// 经度
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("lat", latStr);
		map.put("lng", lngStr);

		return map;
	}

	// 获取当前地域信息
	public static Map<String, String> getCurCityInfo(String latitude,
			String longitude) {
		// 谷歌
		String str = "http://maps.google.com/maps/api/geocode/json?latlng=22.537809,114.105656&language=zh-CN&sensor=true";
		// 百度
		String str1 = "http://api.map.baidu.com/geocoder?location=" + latitude
				+ "," + longitude
				+ "&output=json&key=d903c06b0d7c3b92e3cc50c2a44bddea";
		Map map = new HashMap();
		try {
			URL url = new URL(str1);
			BufferedReader br = null;
			String brLine = "";
			String getStr = "";
			String rstr = "";
			String msg = "Success";

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setRequestMethod("GET"); // request method, default GET
			connection.setUseCaches(false); // Post can not user cache
			connection.setDoOutput(true); // set output from urlconn
			connection.setDoInput(true); // set input from urlconn

			br = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
			while ((brLine = br.readLine()) != null) {
				getStr = (new StringBuilder(String.valueOf(getStr))).append(
						brLine).toString();
			}
			// System.out.println("str:"+getStr);

			JSONObject jsob = new JSONObject(getStr);

			// 百度解析地址
			String city = "";
			String district = "";
			String province = "";
			JSONObject jsob2 = jsob.getJSONObject("result");
			JSONObject jsob3 = jsob2.getJSONObject("addressComponent");
			if (jsob3 != null) {
				city = (String) jsob3.get("city");// 市
				district = (String) jsob3.get("district");// 区
				province = (String) jsob3.get("province");// 省
				String street = (String) jsob3.get("street");// 路
				String street_number = (String) jsob3.get("street_number");// 号
			}
			map.put("city",
					city.replace("市", "").replace("区", "").replace("县", "")
							.replace("省", ""));
			map.put("district", district.replace("市", "").replace("区", "")
					.replace("县", "").replace("省", ""));
			map.put("province", province.replace("市", "").replace("区", "")
					.replace("县", "").replace("省", ""));

		} catch (Exception e) {
			return null;
		}

		return map;
	}

	// 获取版本号
	public static String getVersion(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		if (info != null) {
			// 当前应用的版本名称
			String versionName = info.versionName;

			// 当前版本的版本号
			int versionCode = info.versionCode;

			// 当前版本的包名
			String packageNames = info.packageName;

			return versionName;
		}

		return null;
	}

	// 获取系统当前时间
	public static String getCurTime() {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		// DateFormat f = DateFormat.getDateTimeInstance();
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		String str = sDateFormat.format(now);

		return str;
	}

	// 获取系统当前时间（24小时制）
	public static ArrayList<String> getCurTime24() {
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。

		int year = t.year;
		int month = t.month;
		int date = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;

		String yearStr = Integer.toString(year);
		String monthStr = Integer.toString(month);
		String dateStr = Integer.toString(date);
		String hourStr = Integer.toString(hour); // 0-23
		String minuteStr = Integer.toString(minute);
		String secondStr = Integer.toString(second);

		ArrayList<String> arr = new ArrayList<String>();
		arr.add(yearStr);
		arr.add(monthStr);
		arr.add(dateStr);
		arr.add(hourStr);
		arr.add(minuteStr);
		arr.add(secondStr);

		return arr;
	}

	// 读取文件
	public static String readText(Context context, String textName) {
		String returnStr = "";
		InputStream is;
		try {
			is = context.getResources().getAssets().open(textName);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			returnStr = new String(buffer, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("读取文件 " + textName + " 返回==" + returnStr);
		return returnStr;
	}

	// 判断是wifi还是sim卡上网
	public static boolean checkWifi(Context context) {
		boolean isWifiConnect = true;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
		for (int i = 0; i < networkInfos.length; i++) {
			if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
				if (networkInfos[i].getType() == cm.TYPE_MOBILE) {
					isWifiConnect = false;
				}
				if (networkInfos[i].getType() == cm.TYPE_WIFI) {
					isWifiConnect = true;
				}
			}
		}
		return isWifiConnect;
	}

	// 判断wifi网络是否连接
	public static boolean isWiFiActive(Context inContext) {
		WifiManager mWifiManager = (WifiManager) inContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
		int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
		if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
			System.out.println("**** WIFI is on");
			return true;
		} else {
			System.out.println("**** WIFI is off");
			return false;
		}
	}

	// 判断3G网络是否连接
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			System.out.println("**** newwork is off");
			return false;
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info == null) {
				System.out.println("**** newwork is off");
				return false;
			} else {
				if (info.isAvailable()) {
					System.out.println("**** newwork is on");
					return true;
				}

			}
		}
		System.out.println("**** newwork is off");
		return false;
	}

	// 获取手机IP
	public static String getCurIP(Context context) {
		String wserviceName = Context.WIFI_SERVICE;
		WifiManager wm = (WifiManager) context.getSystemService(wserviceName);
		WifiInfo info = wm.getConnectionInfo();

		String ip = intToIp(info.getIpAddress());
		System.out.println("手机IP==" + ip);
		return ip;
	}

	// 将获取的int转为真正的ip地址
	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + ((i >> 24) & 0xFF);
	}

	// 扫描jmDNS设备
	public static List getJmDNS(String type, boolean isSearchAll)
			throws IOException, InterruptedException {
		JmDNS jmDNS = JmDNS.create();
		List<ServiceInfo[]> sInfoList = new ArrayList<ServiceInfo[]>();

		if (!isSearchAll) {
			ServiceInfo[] si = jmDNS.list(type);
			sInfoList.add(si);
			return sInfoList;
		} else {
			SampleListener sl = new SampleListener();
			jmDNS.addServiceTypeListener(sl);

			int len = sl.typeList.size();
			while (true) {
				Thread.sleep(10000);
				System.out.println("len==" + len);
				System.out.println("sl.typeList.size()==" + sl.typeList.size());
				if (len == sl.typeList.size()) {
					System.out.println(" suc");
					List<String> typeList = sl.typeList;

					for (int i = 0; i < typeList.size(); i++) {
						String tempStr = typeList.get(i);
						System.out.println("jmdns name==" + tempStr);
						ServiceInfo[] si = jmDNS.list(tempStr);
						sInfoList.add(si);
					}

					return sInfoList;
				} else {
					len = sl.typeList.size();
				}
			}
		}
	}

	// 打开短暂提示框
	public static void makeText(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}

	// 打开确定对话框
	public static void openSureAD(Context context, String title, String msg) {
		if (dialogClose != null)
			return;

		dialogClose = new AlertDialog.Builder(context);
		dialogClose.setTitle(title).setIcon(android.R.drawable.ic_dialog_info)
				.setMessage(msg)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						MyTool.closeSureAD();
						dialog.cancel();
					}
				}).create().show();
	}

	// 打开确认取消对话框
	public static void openAlertDialog(Context context, String title,
			String msg, String positiveName, String negativeName,
			android.content.DialogInterface.OnClickListener positiveListener,
			android.content.DialogInterface.OnClickListener negativeListener,
			boolean isCloseCancel) {
		if (dialogClose != null)
			return;

		dialogClose = new AlertDialog.Builder(context);
		dialogClose.setTitle(title);
		dialogClose.setIcon(android.R.drawable.ic_dialog_info);
		dialogClose.setMessage(msg);
		if (isCloseCancel)
			dialogClose.setCancelable(false);
		dialogClose.setPositiveButton(positiveName, positiveListener);
		if (negativeName != null && negativeListener != null)
			dialogClose.setNegativeButton(negativeName, negativeListener);
		dialogClose.create();
		dialogClose.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getRepeatCount() == 0) {
					closeSureAD();
				}
				return false;
			}
		});
		dialogClose.show();
	}

	// 关闭对话框
	public static void closeSureAD() {
		if (dialogClose != null) {
			dialogClose = null;
		} else {
			return;
		}
	}

	// 打开加载对话框
	public static void showProgressDialog(String msg, Context context,
			Activity act) {
		dialogAct = act;
		if (mypDialog != null)
			return;

		try {
			mypDialog = new ProgressDialog(context);
			mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mypDialog.setMessage(msg);
			mypDialog.setIndeterminate(true);
			mypDialog.setCancelable(false);
			mypDialog.setButton("取消", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					closeProgressDialog();
				}
			});
			mypDialog.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 关闭加载对话框
	public static void closeProgressDialog() {
		if (dialogAct != null && !dialogAct.isFinishing() && mypDialog != null) {
			mypDialog.dismiss();
			mypDialog = null;
		}
	}

	/**
	 * 
	 * @param bytes
	 * @return 将二进制字符串转换为十六进制字符串
	 */
	public static StringBuffer BinaryToHexString(String binString) {
		StringBuffer sbuf = new StringBuffer();
		int blength = binString.length() % 4;
		if (blength != 0) {
			String first = binString.substring(0, blength);
			sbuf.append(Integer.toHexString(Integer.parseInt(first, 2)));
			binString = binString.substring(blength);
		}
		int cnum = binString.length() / 4;
		for (int i = 0; i < cnum; i++) {
			sbuf.append(Integer.toHexString(Integer.parseInt(
					binString.substring(i * 4, 4 * (i + 1)), 2)));
		}

		return sbuf;
	}

	/**
	 * 
	 * @param hexString
	 * @return 将十六进制转换为二进制字符串
	 */
	public static StringBuffer HexStringToBinary(String hexString) {
		StringBuffer sbuf = new StringBuffer();
		for (int i = 0; i < hexString.length(); i++) {
			String bin = Integer.toBinaryString(Integer.parseInt(
					hexString.substring(i, i + 1), 16));
			// System.out.println("转换十六进制bin==" + bin);
			if (i >= 0) {
				bin = "0000" + bin;
				bin = bin.substring(bin.length() - 4);
			}
			// System.out.println("+++++++++" + bin);
			sbuf.append(bin);
		}

		return sbuf;
	}

	/**
	 * 定义从右侧进入的动画效果
	 * 
	 * @return
	 */
	public static Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(300);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	/**
	 * 定义从左侧退出的动画效果
	 * 
	 * @return
	 */
	public static Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(5);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	/**
	 * 定义从左侧进入的动画效果
	 * 
	 * @return
	 */
	public static Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(5);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}

	/**
	 * 定义从右侧退出时的动画效果
	 * 
	 * @return
	 */
	public static Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(30);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}

	static class SampleListener implements ServiceTypeListener {

		public List<String> typeList = new ArrayList<String>();

		public void serviceTypeAdded(ServiceEvent event) {
			System.out.println("Service type added: " + event.getType());
			typeList.add(event.getType());
		}

		public void subTypeForServiceTypeAdded(ServiceEvent event) {
			System.out.println("SubType for service type added: "
					+ event.getType());
		}
	}
}
