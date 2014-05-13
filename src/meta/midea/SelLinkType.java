package meta.midea;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import meta.midea.data.HttpServer;
import meta.midea.data.LoadData;
import meta.midea.R;
import meta.midea.main.MainActivity;
import meta.midea.main.NoNetworkActivity;
import meta.midea.tool.MyTool;
import meta.midea.tool.WifiAdmin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

public class SelLinkType extends Activity {
	private MyApplication myApp;

	private Button autoSelWifiBtn;
	private LinearLayout autoLoadLL;
	// private TextView searchTV;

	private int index = 0;
	private int index2 = 25;
	private int index3 = 50;
	private int index4 = 75;
	private int index5 = 100;
	private int index6 = 125;
	private int index7 = 150;
	private int index8 = 175;
	private int index9 = 200;
	private int index10 = 225;
	private int index11;
	private boolean b1 = false;
	private boolean b2 = false;
	private boolean b3 = false;
	private boolean b4 = false;
	private boolean b5 = false;
	private boolean b6 = false;
	private boolean b7 = false;
	private boolean b8 = false;
	private boolean b9 = false;
	private boolean b10 = false;
	private boolean b11;
	private Thread th1;
	private Thread th2;
	private Thread th3;
	private Thread th4;
	private Thread th5;
	private Thread th6;
	private Thread th7;
	private Thread th8;
	private Thread th9;
	private Thread th10;
	private Thread th11;
	private ArrayList<String> wifiipList;
	private boolean isOpenNoFound = false;
	private boolean isDestroy = false; // 记录是否搜索期间点击列表进入主面板，是则终止所有搜索线程
	private int searchNum = 0;// 记录搜索到的个数，用来生成设备名称
	private boolean isHasTh11 = false;// 记录是否有第11个线程

	private ListView list;
	private ArrayList<HashMap<String, Object>> listItem;
	private SimpleAdapter listItemAdapter;
	private JSONArray jsonArr;

	private String wifiIPStr;// 用户选择连接的设备IP
	private String linkIP; // 点击链接的IP

	private static RelativeLayout noNetworkRL;
	private Button noNetworkBtn;

	private String log;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wifi_serverlist);
		myApp = MyApplication.getInstance();
		myApp.addActivity(this);
		myApp.setWifiAdmin(new WifiAdmin(SelLinkType.this));

		// 设置背景
		LinearLayout page = (LinearLayout) findViewById(R.id.sellink_page);
		page.getBackground().setLevel(myApp.getFaceLevel());

		initAutoPage();
		initListPage();
		init();
		initNoNetwork();
	}

	private void initNoNetwork() {
		noNetworkRL = (RelativeLayout) findViewById(R.id.wifiList_noNetwork);
		noNetworkBtn = (Button) findViewById(R.id.wifiList_noNetworkBtn);
		setNoNetwork(myApp.isNoNetwork());

		noNetworkBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SelLinkType.this, NoNetworkActivity.class);
				startActivity(intent);
			}
		});
	}

	public static void setNoNetwork(boolean isHas) {
		if (noNetworkRL == null)
			return;

		if (isHas) {
			noNetworkRL.setVisibility(View.GONE);
		} else {
			noNetworkRL.setVisibility(View.VISIBLE);
		}
	}

	private void initAutoPage() {
		// 自动搜索
		autoSelWifiBtn = (Button) findViewById(R.id.autoSelWifiBtn);
		autoLoadLL = (LinearLayout) findViewById(R.id.autoSelLoadLL);
		// autoLoadLL.addView(new CustomGifView(this, R.drawable.animation));
		// searchTV = (TextView) findViewById(R.id.searchTV);

		autoSelWifiBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (autoLoadLL.getVisibility() == View.VISIBLE) {
					MyTool.makeText(SelLinkType.this, "系统正在搜索您周边的设备中，请稍后再试...");
					return;
				} else {
					autoLoadLL.setVisibility(View.VISIBLE);
					searchIP();
				}

				// MultiCastSSDP ssdp = new MultiCastSSDP();
				// try {
				// System.out.println("myApp.getLocalIp()=="
				// + myApp.getLocalIp());
				// System.out.println("myApp.getWifiAdmin().getMacAddress()=="
				// + myApp.getWifiAdmin().getMacAddress());
				// ArrayList<String> arr = ssdp.MultiCastSSDPClient(myApp
				// .getLocalIp(), myApp.getWifiAdmin().getMacAddress());
				//
				// System.out.println("***********" + arr.size());
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
			}
		});
	}

	private void initListPage() {
		list = (ListView) findViewById(R.id.sellink_listView);

		listItem = new ArrayList<HashMap<String, Object>>();
		listItemAdapter = new SimpleAdapter(this, listItem,
				R.layout.list_items, new String[] { "ItemTitle", "ItemText" },
				new int[] { R.id.ItemTitle, R.id.ItemText });
		list.setAdapter(listItemAdapter);
		// 添加点击
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, Object> map = (HashMap<String, Object>) listItem
						.get(arg2);
				if (map != null) {
					linkIP = map.get("ip").toString();
					linkServer(linkIP);
				}
			}
		});
		// 添加长按点击
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				Dialog dialog = new AlertDialog.Builder(SelLinkType.this)
						.setIcon(android.R.drawable.btn_star)
						.setTitle("操作提示")
						// \n\n点击'配置'，可修改此设备的网络及云平台的配置。
						.setMessage(
								"点击'重命名',可以为您的设备重新取个名字。\n\n点击'删除'，则删除本地保存的此设备信息。")
						.setPositiveButton("重命名",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										saveServerName(arg2);
									}
								})
						.setNegativeButton("删除",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										MyTool.closeSureAD();
										listItem.remove(arg2);
										listItemAdapter.notifyDataSetChanged();
										if (listItem.size() <= 0) {
											list.setVisibility(View.INVISIBLE);
										}
										saveServerList();
										dialog.cancel();
									}
								}).create();
				// .setNeutralButton("配置",
				// new DialogInterface.OnClickListener() {
				// public void onClick(DialogInterface dialog,
				// int which) {
				// MyTool.closeSureAD();
				// dialog.cancel();
				//
				// HashMap<String, Object> map = (HashMap<String, Object>)
				// listItem
				// .get(arg2);
				// if (map != null) {
				// myApp.setStaticIp(map.get("ip")
				// .toString());
				// }
				//
				// myApp.setApMode(false);
				// Intent intent = new Intent();
				// intent.setClass(SelLinkType.this,
				// SelWifi.class);
				// startActivity(intent);
				// SelLinkType.this.finish();
				// }
				// }).create();
				dialog.show();
				return true;
			}
		});
	}

	private void addItem(String name, String ip, String servername,
			String uuid, boolean isSave) {
		if (list.getVisibility() == View.INVISIBLE) {
			list.setVisibility(View.VISIBLE);
		}

		String oldName = null;
		for (int i = 0; i < listItem.size(); i++) {
			HashMap<String, Object> map = listItem.get(i);
			if (map.get("uuid").toString().equals(uuid)) {
				oldName = listItem.get(i).get("name").toString();
				listItem.remove(listItem.get(i));
				listItemAdapter.notifyDataSetChanged();
			}
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemTitle", oldName == null ? name : oldName);
		map.put("ItemText", "设备IP：" + ip + "\n设备名称：" + servername);
		map.put("uuid", uuid);
		map.put("name", oldName == null ? name : oldName);
		map.put("ip", ip);
		map.put("servername", servername);
		listItem.add(map);
		listItemAdapter.notifyDataSetChanged();

		if (isSave && listItem.size() > 0) {
			saveServerList();
		}
	}

	private void init() {
		myApp.getWifiAdmin().startScan();
		List<ScanResult> list = myApp.getWifiAdmin().getWifiList();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				String ssid = ((ScanResult) list.get(i)).SSID;
				// 找到可配置wifi，跳转配置热水器Wifi页面
				if (ssid.indexOf(myApp.getWifiName()) > -1) {
					MyTool.openAlertDialog(SelLinkType.this, "提示",
							"发现未配置热水器，是否进行配置？", "配置", "取消",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									MyTool.closeSureAD();
									myApp.setApMode(true);
									Intent intent = new Intent();
									intent.setClass(SelLinkType.this,
											SelWifi.class);
									startActivity(intent);
									SelLinkType.this.finish();
									return;
								}
							}, new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									MyTool.closeSureAD();
									dialog.cancel();// 取消弹出框
									initData();
								}
							}, true);
					return;
				}
			}
		} else {
			MyTool.openSureAD(SelLinkType.this, "提示", "无可用Wifi");
			return;
		}

		initData();
	}

	private void initData() {
		String serverList = "";
		try {
			FileInputStream inputStream = openFileInput("server");
			if (inputStream != null) {
				byte[] b = new byte[inputStream.available()];
				inputStream.read(b);
				serverList = new String(b);
			}
		} catch (Exception e) {
		}
		if (serverList != null && !serverList.equals("")) {
			try {
				JSONObject jsonObj = new JSONObject(serverList);
				if (jsonObj != null) {
					jsonArr = jsonObj.getJSONArray("server");
				}

				// 多个设备，弹出列表给用户选择
				if (jsonArr.length() > 0) {
					list.setVisibility(View.VISIBLE);
				} else {
					list.setVisibility(View.INVISIBLE);
				}
				for (int i = 0; i < jsonArr.length(); i++) {
					JSONObject tempJsonObj = (JSONObject) jsonArr.get(i);
					String wifiip = tempJsonObj.getString("wifiip");
					String name = tempJsonObj.getString("name");
					String serverBaseName = tempJsonObj.getString("servername");
					String uuid = tempJsonObj.getString("uuid");

					addItem(name, wifiip, serverBaseName, uuid, false);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
		}
	}

	private void linkServer(String wifiIP) {
		LoadData.BASE_URLForWIFI = "http://" + wifiIP + "/";
		wifiIPStr = wifiIP;

		// 判断本地IP是否可用
		try {
			isIpUse();
		} catch (Exception ex) {
			reSearch(linkIP);
			return;
		}
	}

	// 判断ip是否连通
	public void isIpUse() {
		if (MyTool.mypDialog != null)
			return;
		MyTool.showProgressDialog("请稍等，正在连接热水器。。。", SelLinkType.this,
				SelLinkType.this);
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					msg.obj = LoadData.getSysCloud();
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForIsIpUse.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForIsIpUse = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				MyTool.closeProgressDialog();
				// {"enable":0,"interval":60,
				// "url":"http://192.168.254.97:8088/customize/control/getServicesOfCloudMothod",
				// "isauthkey":1,"authkey":"test4;F50J;111112"}
				if (msg.obj != null) {
					myApp.setCurWifiIP(wifiIPStr);
					try {
						JSONObject jObj = new JSONObject((String) msg.obj);
						if (jObj != null && jObj.has("authkey"))
							myApp.setCurServerAuthKey(jObj.getString("authkey"));
					} catch (JSONException e) {
						e.printStackTrace();
						MyTool.closeProgressDialog();
						reSearch(linkIP);
						return;
					}

					isDestroy = true;

					Intent intent = new Intent();
					intent.setClass(SelLinkType.this, MainActivity.class);
					startActivity(intent);
				} else {
					MyTool.closeProgressDialog();
					reSearch(linkIP);
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	// 重新搜索
	private void reSearch(String oldIp) {
		MyTool.closeSureAD();
		MyTool.makeText(this, "该设备无法连接，系统正为您重新搜索可用设备，请稍等...");
		if (autoLoadLL.getVisibility() == View.VISIBLE) {
			return;
		} else {
			autoLoadLL.setVisibility(View.VISIBLE);
			// searchIP();
			String[] arr = oldIp.split("\\.");
			searchIPMiddle(Integer.parseInt(arr[3]));
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	private void saveServerName(final int index) {
		LayoutInflater factory = LayoutInflater.from(SelLinkType.this);// 提示框
		final View view = factory.inflate(R.layout.alert_edit, null);// 这里必须是final的
		final EditText edit = (EditText) view.findViewById(R.id.editText1);// 获得输入框对象
		edit.setHint("请输入设备名称");// 输入框默认值

		AlertDialog.Builder dialogClose = new AlertDialog.Builder(
				SelLinkType.this);
		dialogClose.setTitle("提示");
		dialogClose.setView(view);
		dialogClose.setIcon(android.R.drawable.ic_dialog_info);
		dialogClose.setMessage("重新给设备取个名字吧");
		dialogClose.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						String newName = edit.getText().toString();
						listItem.get(index).put("name", newName);
						listItem.get(index).put("ItemTitle", newName);
						listItemAdapter.notifyDataSetChanged();
						saveServerList();
						dialog.cancel();
					}
				});
		dialogClose.create();
		dialogClose.show();
	}

	private void saveServerList() {
		int len = listItem.size();
		String serverStr = "";
		String serverList = "{\"server\":[";
		for (int i = 0; i < len; i++) {
			HashMap<String, Object> map = listItem.get(i);
			serverStr = "{\"wifiip\":\"" + map.get("ip") + "\", \"uuid\":\""
					+ map.get("uuid") + "\", \"servername\":\""
					+ map.get("servername") + "\"," + "\"isact\":\"0\","
					+ "\"name\":\"" + map.get("name") + "\", \"authkey\":\""
					+ myApp.getCurServerAuthKey() + "\", \"actcode\":\""
					+ myApp.getCurServerActCode() + "\", " + "\"base\":\"\"}";
			serverList = serverList + serverStr;

			if (i == (len - 1))
				break;
			else
				serverList = serverList + ",";
		}
		serverList = serverList + "]}";

		try {
			JSONObject jsonObj = new JSONObject(serverList);
			if (jsonObj != null) {
				jsonArr = jsonObj.getJSONArray("server");
			}

			FileOutputStream fos = openFileOutput("server",
					Context.MODE_PRIVATE);
			fos.write(serverList.getBytes());
			fos.close();
		} catch (Exception e) {
		}
	}

	@Override
	protected void onDestroy() {
		MyTool.closeProgressDialog();
		super.onDestroy();
		System.out.println("SelLinkType onDestroy~~~");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			MyTool.closeSureAD();
			Intent intent = new Intent();
			intent.setClass(SelLinkType.this, SelModeActivity.class);
			startActivity(intent);
			SelLinkType.this.finish();
			return false;
		}
		return false;
	}

	/*********** 多线程查找IP **********/
	private void searchIP() {
		// 清空设备列表
		for (int i = 0; i < listItem.size(); i++) {
			listItem.remove(i);
		}
		listItemAdapter.notifyDataSetChanged();
		list.setVisibility(View.INVISIBLE);

		// String newip = "192.168.0.103";
		// String url = "http://" + newip + "/sys";
		// try {
		// String str = HttpServer.getGetTestRequest(url);
		// JSONObject jObj = new JSONObject(str);
		// if (jObj != null && jObj.has("uuid") && jObj.has("name")) {
		// String ipAndId = searchNum + "#" + newip + "#"
		// + jObj.getString("name") + "#" + jObj.getString("uuid");
		// MyTool.makeText(SelLinkType.this, newip + "通");
		// } else {
		// MyTool.makeText(SelLinkType.this, newip + "不通\n" + str);
		// }
		// } catch (Exception ex) {
		// MyTool.makeText(SelLinkType.this,
		// newip + "不通\n发生异常" + ex.getMessage());
		// }

		// 静态IP
		String localIP = MyTool.getCurIP(SelLinkType.this);
		myApp.setLocalIp(localIP);

		if (MyTool.mypDialog != null)
			return;
		// MyTool.showProgressDialog("系统正在尝试连接您的热水器。。。", SelLinkType.this,
		// SelLinkType.this);
		index = 0;
		index2 = 25;
		index3 = 50;
		index4 = 75;
		index5 = 100;
		index6 = 125;
		index7 = 150;
		index8 = 175;
		index9 = 200;
		index10 = 225;
		b1 = false;
		b2 = false;
		b3 = false;
		b4 = false;
		b5 = false;
		b6 = false;
		b7 = false;
		b8 = false;
		b9 = false;
		b10 = false;
		isOpenNoFound = false;
		wifiipList = new ArrayList<String>();
		searchNum = 0;
		loadThreadInitView(true, 25);
		loadThreadInitView2(true, 50);
		loadThreadInitView3(true, 75);
		loadThreadInitView4(true, 100);
		loadThreadInitView5(true, 125);
		loadThreadInitView6(true, 150);
		loadThreadInitView7(true, 175);
		loadThreadInitView8(true, 200);
		loadThreadInitView9(true, 225);
		loadThreadInitView10(true, 250);
	}

	private void searchIPMiddle(int oldIP) {
		// 静态IP
		String localIP = MyTool.getCurIP(SelLinkType.this);
		myApp.setLocalIp(localIP);

		if (MyTool.mypDialog != null)
			return;

		ArrayList<HashMap<String, Object>> arr = new ArrayList<HashMap<String, Object>>();
		for (int i = oldIP; i > 0; i = i - 25) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("s", i + 1);
			map.put("e", ((i - 25) < 1) ? 1 : (i + 1 - 25));
			map.put("isAdd", false);
			arr.add(map);
		}
		for (int i = oldIP; i < 255; i = i + 25) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("s", i);
			map.put("e", ((i + 25) > 255) ? 255 : (i + 25));
			map.put("isAdd", true);
			arr.add(map);
		}

		for (int i = 0; i < arr.size(); i++) {
			HashMap<String, Object> map = arr.get(i);
			System.out.println("s" + i + "==" + map.get("s"));
			System.out.println("e" + i + "==" + map.get("e"));
		}
		if (arr.size() == 11) {
			isHasTh11 = true;
		} else {
			isHasTh11 = false;
		}

		index = (Integer) ((HashMap<String, Object>) arr.get(0)).get("s");
		index2 = (Integer) ((HashMap<String, Object>) arr.get(1)).get("s");
		index3 = (Integer) ((HashMap<String, Object>) arr.get(2)).get("s");
		index4 = (Integer) ((HashMap<String, Object>) arr.get(3)).get("s");
		index5 = (Integer) ((HashMap<String, Object>) arr.get(4)).get("s");
		index6 = (Integer) ((HashMap<String, Object>) arr.get(5)).get("s");
		index7 = (Integer) ((HashMap<String, Object>) arr.get(6)).get("s");
		index8 = (Integer) ((HashMap<String, Object>) arr.get(7)).get("s");
		index9 = (Integer) ((HashMap<String, Object>) arr.get(8)).get("s");
		index10 = (Integer) ((HashMap<String, Object>) arr.get(9)).get("s");
		if (isHasTh11) {
			index11 = (Integer) ((HashMap<String, Object>) arr.get(10))
					.get("s");
		}
		b1 = false;
		b2 = false;
		b3 = false;
		b4 = false;
		b5 = false;
		b6 = false;
		b7 = false;
		b8 = false;
		b9 = false;
		b10 = false;
		if (isHasTh11) {
			b11 = false;
		}
		isOpenNoFound = false;
		wifiipList = new ArrayList<String>();
		searchNum = 0;
		loadThreadInitView(
				(Boolean) ((HashMap<String, Object>) arr.get(0)).get("isAdd"),
				(Integer) ((HashMap<String, Object>) arr.get(0)).get("e"));
		loadThreadInitView2(
				(Boolean) ((HashMap<String, Object>) arr.get(1)).get("isAdd"),
				(Integer) ((HashMap<String, Object>) arr.get(1)).get("e"));
		loadThreadInitView3(
				(Boolean) ((HashMap<String, Object>) arr.get(2)).get("isAdd"),
				(Integer) ((HashMap<String, Object>) arr.get(2)).get("e"));
		loadThreadInitView4(
				(Boolean) ((HashMap<String, Object>) arr.get(3)).get("isAdd"),
				(Integer) ((HashMap<String, Object>) arr.get(3)).get("e"));
		loadThreadInitView5(
				(Boolean) ((HashMap<String, Object>) arr.get(4)).get("isAdd"),
				(Integer) ((HashMap<String, Object>) arr.get(4)).get("e"));
		loadThreadInitView6(
				(Boolean) ((HashMap<String, Object>) arr.get(5)).get("isAdd"),
				(Integer) ((HashMap<String, Object>) arr.get(5)).get("e"));
		loadThreadInitView7(
				(Boolean) ((HashMap<String, Object>) arr.get(6)).get("isAdd"),
				(Integer) ((HashMap<String, Object>) arr.get(6)).get("e"));
		loadThreadInitView8(
				(Boolean) ((HashMap<String, Object>) arr.get(7)).get("isAdd"),
				(Integer) ((HashMap<String, Object>) arr.get(7)).get("e"));
		loadThreadInitView9(
				(Boolean) ((HashMap<String, Object>) arr.get(8)).get("isAdd"),
				(Integer) ((HashMap<String, Object>) arr.get(8)).get("e"));
		loadThreadInitView10(
				(Boolean) ((HashMap<String, Object>) arr.get(9)).get("isAdd"),
				(Integer) ((HashMap<String, Object>) arr.get(9)).get("e"));
		if (isHasTh11) {
			loadThreadInitView11(
					(Boolean) ((HashMap<String, Object>) arr.get(10))
							.get("isAdd"),
					(Integer) ((HashMap<String, Object>) arr.get(10)).get("e"));
		}
	}

	private void loadThreadInitView(final boolean isAdd, final int end) {
		th1 = new Thread() {
			public void run() {
				try {
					String localip = myApp.getLocalIp();
					String[] strs = localip.split("\\.");
					boolean isnull = true;
					while (isnull) {
						if (isDestroy)
							return;

						index = isAdd ? (index + 1) : (index - 1);
						int nip = index;
						if (isAdd ? (nip > end) : (nip < end)) {
							break;
						} else {
							String newip = strs[0] + "." + strs[1] + "."
									+ strs[2] + "." + nip;

							String url = "http://" + newip + "/sys";
							System.out.println("扫描 ip==" + newip);
							log = log + "扫描 ip==" + newip + "\n";
							try {
								String str = HttpServer.getGetTestRequest(url);
								log = log + newip + ":" + str + "\n";
								JSONObject jObj = new JSONObject(str);
								if (jObj != null && jObj.has("uuid")
										&& jObj.has("name")) {
									searchNum++;
									String ipAndId = searchNum + "#" + newip
											+ "#" + jObj.getString("name")
											+ "#" + jObj.getString("uuid");
									wifiipList.add(newip);

									Message msg = new Message();
									msg.what = 0;
									msg.obj = ipAndId;
									handlerForAdd.sendMessage(msg);
								}
							} catch (Exception ex) {

							}
						}
					}
					b1 = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 0;
				msg.obj = true;
				handlerInitView.sendMessage(msg);
			}
		};
		th1.start();
	}

	private void loadThreadInitView2(final boolean isAdd, final int end) {
		th2 = new Thread() {
			public void run() {
				try {
					String localip = myApp.getLocalIp();
					String[] strs = localip.split("\\.");
					boolean isnull = true;
					while (isnull) {
						if (isDestroy)
							return;

						index2 = isAdd ? (index2 + 1) : (index2 - 1);
						int nip = index2;

						if (isAdd ? (nip > end) : (nip < end)) {
							break;
						} else {
							String newip = strs[0] + "." + strs[1] + "."
									+ strs[2] + "." + nip;

							String url = "http://" + newip + "/sys";
							System.out.println("扫描 ip==" + newip);
							log = log + "扫描 ip==" + newip + "\n";
							try {
								String str = HttpServer.getGetTestRequest(url);
								log = log + newip + ":" + str + "\n";
								JSONObject jObj = new JSONObject(str);
								if (jObj != null && jObj.has("uuid")
										&& jObj.has("name")) {
									searchNum++;
									String ipAndId = searchNum + "#" + newip
											+ "#" + jObj.getString("name")
											+ "#" + jObj.getString("uuid");
									wifiipList.add(newip);

									Message msg = new Message();
									msg.what = 0;
									msg.obj = ipAndId;
									handlerForAdd.sendMessage(msg);
								}
							} catch (Exception ex) {

							}
						}
					}
					b2 = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 0;
				msg.obj = true;
				handlerInitView.sendMessage(msg);
			}
		};
		th2.start();
	}

	private void loadThreadInitView3(final boolean isAdd, final int end) {
		th3 = new Thread() {
			public void run() {
				try {
					String localip = myApp.getLocalIp();
					String[] strs = localip.split("\\.");
					boolean isnull = true;
					while (isnull) {
						if (isDestroy)
							return;

						index3 = isAdd ? (index3 + 1) : (index3 - 1);
						int nip = index3;

						if (isAdd ? (nip > end) : (nip < end)) {
							break;
						} else {
							String newip = strs[0] + "." + strs[1] + "."
									+ strs[2] + "." + nip;

							String url = "http://" + newip + "/sys";
							System.out.println("扫描 ip==" + newip);
							log = log + "扫描 ip==" + newip + "\n";
							try {
								String str = HttpServer.getGetTestRequest(url);
								log = log + newip + ":" + str + "\n";
								JSONObject jObj = new JSONObject(str);
								if (jObj != null && jObj.has("uuid")
										&& jObj.has("name")) {
									searchNum++;
									String ipAndId = searchNum + "#" + newip
											+ "#" + jObj.getString("name")
											+ "#" + jObj.getString("uuid");
									wifiipList.add(newip);

									Message msg = new Message();
									msg.what = 0;
									msg.obj = ipAndId;
									handlerForAdd.sendMessage(msg);
								}
							} catch (Exception ex) {

							}
						}
					}
					b3 = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 0;
				msg.obj = true;
				handlerInitView.sendMessage(msg);
			}
		};
		th3.start();
	}

	private void loadThreadInitView4(final boolean isAdd, final int end) {
		th4 = new Thread() {
			public void run() {
				try {
					String localip = myApp.getLocalIp();
					String[] strs = localip.split("\\.");
					boolean isnull = true;
					while (isnull) {
						if (isDestroy)
							return;

						index4 = isAdd ? (index4 + 1) : (index4 - 1);
						int nip = index4;

						if (isAdd ? (nip > end) : (nip < end)) {
							break;
						} else {
							String newip = strs[0] + "." + strs[1] + "."
									+ strs[2] + "." + nip;

							String url = "http://" + newip + "/sys";
							System.out.println("扫描 ip==" + newip);
							log = log + "扫描 ip==" + newip + "\n";
							try {
								String str = HttpServer.getGetTestRequest(url);
								log = log + newip + ":" + str + "\n";
								JSONObject jObj = new JSONObject(str);
								if (jObj != null && jObj.has("uuid")
										&& jObj.has("name")) {
									searchNum++;
									String ipAndId = searchNum + "#" + newip
											+ "#" + jObj.getString("name")
											+ "#" + jObj.getString("uuid");
									wifiipList.add(newip);

									Message msg = new Message();
									msg.what = 0;
									msg.obj = ipAndId;
									handlerForAdd.sendMessage(msg);
								}
							} catch (Exception ex) {

							}
						}
					}
					b4 = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 0;
				msg.obj = true;
				handlerInitView.sendMessage(msg);
			}
		};
		th4.start();
	}

	private void loadThreadInitView5(final boolean isAdd, final int end) {
		th5 = new Thread() {
			public void run() {
				try {
					String localip = myApp.getLocalIp();
					String[] strs = localip.split("\\.");
					boolean isnull = true;
					while (isnull) {
						if (isDestroy)
							return;

						index5 = isAdd ? (index5 + 1) : (index5 - 1);
						int nip = index5;

						if (isAdd ? (nip > end) : (nip < end)) {
							break;
						} else {
							String newip = strs[0] + "." + strs[1] + "."
									+ strs[2] + "." + nip;

							String url = "http://" + newip + "/sys";
							System.out.println("扫描 ip==" + newip);
							log = log + "扫描 ip==" + newip + "\n";
							try {
								String str = HttpServer.getGetTestRequest(url);
								log = log + newip + ":" + str + "\n";
								JSONObject jObj = new JSONObject(str);
								if (jObj != null && jObj.has("uuid")
										&& jObj.has("name")) {
									searchNum++;
									String ipAndId = searchNum + "#" + newip
											+ "#" + jObj.getString("name")
											+ "#" + jObj.getString("uuid");
									wifiipList.add(newip);

									Message msg = new Message();
									msg.what = 0;
									msg.obj = ipAndId;
									handlerForAdd.sendMessage(msg);
								}
							} catch (Exception ex) {

							}
						}
					}
					b5 = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 0;
				msg.obj = true;
				handlerInitView.sendMessage(msg);
			}
		};
		th5.start();
	}

	private void loadThreadInitView6(final boolean isAdd, final int end) {
		th6 = new Thread() {
			public void run() {
				try {
					String localip = myApp.getLocalIp();
					String[] strs = localip.split("\\.");
					boolean isnull = true;
					while (isnull) {
						if (isDestroy)
							return;

						index6 = isAdd ? (index6 + 1) : (index6 - 1);
						int nip = index6;

						if (isAdd ? (nip > end) : (nip < end)) {
							break;
						} else {
							String newip = strs[0] + "." + strs[1] + "."
									+ strs[2] + "." + nip;

							String url = "http://" + newip + "/sys";
							System.out.println("扫描 ip==" + newip);
							log = log + "扫描 ip==" + newip + "\n";
							try {
								String str = HttpServer.getGetTestRequest(url);
								log = log + newip + ":" + str + "\n";
								JSONObject jObj = new JSONObject(str);
								if (jObj != null && jObj.has("uuid")
										&& jObj.has("name")) {
									searchNum++;
									String ipAndId = searchNum + "#" + newip
											+ "#" + jObj.getString("name")
											+ "#" + jObj.getString("uuid");
									wifiipList.add(newip);

									Message msg = new Message();
									msg.what = 0;
									msg.obj = ipAndId;
									handlerForAdd.sendMessage(msg);
								}
							} catch (Exception ex) {

							}
						}
					}
					b6 = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 0;
				msg.obj = true;
				handlerInitView.sendMessage(msg);
			}
		};
		th6.start();
	}

	private void loadThreadInitView7(final boolean isAdd, final int end) {
		th7 = new Thread() {
			public void run() {
				try {
					String localip = myApp.getLocalIp();
					String[] strs = localip.split("\\.");
					boolean isnull = true;
					while (isnull) {
						if (isDestroy)
							return;

						index7 = isAdd ? (index7 + 1) : (index7 - 1);
						int nip = index7;

						if (isAdd ? (nip > end) : (nip < end)) {
							break;
						} else {
							String newip = strs[0] + "." + strs[1] + "."
									+ strs[2] + "." + nip;

							String url = "http://" + newip + "/sys";
							System.out.println("扫描 ip==" + newip);
							log = log + "扫描 ip==" + newip + "\n";
							try {
								String str = HttpServer.getGetTestRequest(url);
								log = log + newip + ":" + str + "\n";
								JSONObject jObj = new JSONObject(str);
								if (jObj != null && jObj.has("uuid")
										&& jObj.has("name")) {
									searchNum++;
									String ipAndId = searchNum + "#" + newip
											+ "#" + jObj.getString("name")
											+ "#" + jObj.getString("uuid");
									wifiipList.add(newip);

									Message msg = new Message();
									msg.what = 0;
									msg.obj = ipAndId;
									handlerForAdd.sendMessage(msg);
								}
							} catch (Exception ex) {

							}
						}
					}
					b7 = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 0;
				msg.obj = true;
				handlerInitView.sendMessage(msg);
			}
		};
		th7.start();
	}

	private void loadThreadInitView8(final boolean isAdd, final int end) {
		th8 = new Thread() {
			public void run() {
				try {
					String localip = myApp.getLocalIp();
					String[] strs = localip.split("\\.");
					boolean isnull = true;
					while (isnull) {
						if (isDestroy)
							return;

						index8 = isAdd ? (index8 + 1) : (index8 - 1);
						int nip = index8;

						if (isAdd ? (nip > end) : (nip < end)) {
							break;
						} else {
							String newip = strs[0] + "." + strs[1] + "."
									+ strs[2] + "." + nip;

							String url = "http://" + newip + "/sys";
							System.out.println("扫描 ip==" + newip);
							log = log + "扫描 ip==" + newip + "\n";
							try {
								String str = HttpServer.getGetTestRequest(url);
								log = log + newip + ":" + str + "\n";
								JSONObject jObj = new JSONObject(str);
								if (jObj != null && jObj.has("uuid")
										&& jObj.has("name")) {
									searchNum++;
									String ipAndId = searchNum + "#" + newip
											+ "#" + jObj.getString("name")
											+ "#" + jObj.getString("uuid");
									wifiipList.add(newip);

									Message msg = new Message();
									msg.what = 0;
									msg.obj = ipAndId;
									handlerForAdd.sendMessage(msg);
								}
							} catch (Exception ex) {

							}
						}
					}
					b8 = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 0;
				msg.obj = true;
				handlerInitView.sendMessage(msg);
			}
		};
		th8.start();
	}

	private void loadThreadInitView9(final boolean isAdd, final int end) {
		th9 = new Thread() {
			public void run() {
				try {
					String localip = myApp.getLocalIp();
					String[] strs = localip.split("\\.");
					boolean isnull = true;
					while (isnull) {
						if (isDestroy)
							return;

						index9 = isAdd ? (index9 + 1) : (index9 - 1);
						int nip = index9;

						if (isAdd ? (nip > end) : (nip < end)) {
							break;
						} else {
							String newip = strs[0] + "." + strs[1] + "."
									+ strs[2] + "." + nip;

							String url = "http://" + newip + "/sys";
							System.out.println("扫描 ip==" + newip);
							log = log + "扫描 ip==" + newip + "\n";
							try {
								String str = HttpServer.getGetTestRequest(url);
								log = log + newip + ":" + str + "\n";
								JSONObject jObj = new JSONObject(str);
								if (jObj != null && jObj.has("uuid")
										&& jObj.has("name")) {
									searchNum++;
									String ipAndId = searchNum + "#" + newip
											+ "#" + jObj.getString("name")
											+ "#" + jObj.getString("uuid");
									wifiipList.add(newip);

									Message msg = new Message();
									msg.what = 0;
									msg.obj = ipAndId;
									handlerForAdd.sendMessage(msg);
								}
							} catch (Exception ex) {

							}
						}
					}
					b9 = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 0;
				msg.obj = true;
				handlerInitView.sendMessage(msg);
			}
		};
		th9.start();
	}

	private void loadThreadInitView10(final boolean isAdd, final int end) {
		th10 = new Thread() {
			public void run() {
				try {
					String localip = myApp.getLocalIp();
					String[] strs = localip.split("\\.");
					boolean isnull = true;
					while (isnull) {
						if (isDestroy)
							return;

						index10 = isAdd ? (index10 + 1) : (index10 - 1);
						int nip = index10;

						if (isAdd ? (nip > end) : (nip < end)) {
							break;
						} else {
							String newip = strs[0] + "." + strs[1] + "."
									+ strs[2] + "." + nip;

							String url = "http://" + newip + "/sys";
							System.out.println("扫描 ip==" + newip);
							log = log + "扫描 ip==" + newip + "\n";
							try {
								String str = HttpServer.getGetTestRequest(url);
								log = log + newip + ":" + str + "\n";
								JSONObject jObj = new JSONObject(str);
								if (jObj != null && jObj.has("uuid")
										&& jObj.has("name")) {
									searchNum++;
									String ipAndId = searchNum + "#" + newip
											+ "#" + jObj.getString("name")
											+ "#" + jObj.getString("uuid");
									wifiipList.add(newip);

									Message msg = new Message();
									msg.what = 0;
									msg.obj = ipAndId;
									handlerForAdd.sendMessage(msg);
								}
							} catch (Exception ex) {

							}
						}
					}
					b10 = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 0;
				msg.obj = true;
				handlerInitView.sendMessage(msg);
			}
		};
		th10.start();
	}

	private void loadThreadInitView11(final boolean isAdd, final int end) {
		th11 = new Thread() {
			public void run() {
				try {
					String localip = myApp.getLocalIp();
					String[] strs = localip.split("\\.");
					boolean isnull = true;
					while (isnull) {
						if (isDestroy)
							return;

						index11 = isAdd ? (index11 + 1) : (index11 - 1);
						int nip = index11;

						if (isAdd ? (nip > end) : (nip < end)) {
							break;
						} else {
							String newip = strs[0] + "." + strs[1] + "."
									+ strs[2] + "." + nip;

							String url = "http://" + newip + "/sys";
							System.out.println("扫描 ip==" + newip);
							log = log + "扫描 ip==" + newip + "\n";
							try {
								String str = HttpServer.getGetTestRequest(url);
								log = log + newip + ":" + str + "\n";
								JSONObject jObj = new JSONObject(str);
								if (jObj != null && jObj.has("uuid")
										&& jObj.has("name")) {
									searchNum++;
									String ipAndId = searchNum + "#" + newip
											+ "#" + jObj.getString("name")
											+ "#" + jObj.getString("uuid");
									wifiipList.add(newip);

									Message msg = new Message();
									msg.what = 0;
									msg.obj = ipAndId;
									handlerForAdd.sendMessage(msg);
								}
							} catch (Exception ex) {

							}
						}
					}
					b11 = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 0;
				msg.obj = true;
				handlerInitView.sendMessage(msg);
			}
		};
		th11.start();
	}

	private Handler handlerForAdd = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null) {
					String[] arr = msg.obj.toString().split("#");
					addItem("美的热水器" + arr[0] + "号", arr[1], arr[2], arr[3],
							true);
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	private Handler handlerInitView = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (isHasTh11) {
					if (b1 && b2 && b3 && b4 && b5 && b6 && b7 && b8 && b9
							&& b10 && b11) {
						autoLoadLL.setVisibility(View.GONE);

						System.out.println("扫描到 wifiIP个数==" + listItem.size());
						if (listItem.size() > 0 && wifiipList.size() > 0) {
							saveServerList();
						} else {
							if (!isOpenNoFound) {
								isOpenNoFound = true;
								MyTool.closeProgressDialog();
								if (listItem.size() > 0) {
									MyTool.openSureAD(SelLinkType.this, "提示",
											"没有找到可用设备\n" + getError());
								} else {
									MyTool.openSureAD(SelLinkType.this, "提示",
											"没有找到可用设备\n" + getError());
								}
							}
						}

//						log = log + "扫描到 wifiIP个数==" + listItem.size() + "\n";
//						log = log + "扫描到 wifiipList个数==" + wifiipList.size()
//								+ "\n";
//						for (int i = 0; i < wifiipList.size(); i++) {
//							log = log + "扫描到ip=" + wifiipList.get(i) + "\n";
//						}
//						System.out.println("log==" + log);
//						try {
//							LoadData.saveErr(log);
//
//							File file = new File("/sdcard/searchLog.txt");
//							FileOutputStream fos = new FileOutputStream(file);
//							fos.write(log.getBytes());
//							fos.flush();
//						} catch (JSONException e) {
//							e.printStackTrace();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
						break;
					}
				} else {
					if (b1 && b2 && b3 && b4 && b5 && b6 && b7 && b8 && b9
							&& b10) {
						autoLoadLL.setVisibility(View.GONE);

						System.out.println("扫描到 wifiIP个数==" + listItem.size());
						if (listItem.size() > 0 && wifiipList.size() > 0) {
							saveServerList();
						} else {
							if (!isOpenNoFound) {
								isOpenNoFound = true;
								MyTool.closeProgressDialog();
								if (listItem.size() > 0) {
									MyTool.openSureAD(SelLinkType.this, "提示",
											"没有找到可用设备\n" + getError());
								} else {
									MyTool.openSureAD(SelLinkType.this, "提示",
											"没有找到可用设备\n" + getError());
								}
							}
						}

//						log = log + "扫描到 wifiIP个数==" + listItem.size() + "\n";
//						log = log + "扫描到 wifiipList个数==" + wifiipList.size()
//								+ "\n";
//						for (int i = 0; i < wifiipList.size(); i++) {
//							log = log + "扫描到ip=" + wifiipList.get(i) + "\n";
//						}
//						System.out.println("log==" + log);
//						try {
//							LoadData.saveErr(log);
//
//							File file = new File("/sdcard/searchLog.txt");
//							FileOutputStream fos = new FileOutputStream(file);
//							fos.write(log.getBytes());
//							fos.flush();
//						} catch (JSONException e) {
//							e.printStackTrace();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
						break;
					}
				}
			default:
				super.handleMessage(msg);
			}
		}
	};

	// 检查搜不到设备原因
	private String getError() {
		String err = "";

		if (!myApp.getWifiAdmin().isWifiEnable()) {
			err = "\n手机Wifi没有开启！";
		} else {
			myApp.setWifiAdmin(new WifiAdmin(SelLinkType.this));
			err = "\n手机当前Wifi名称为：" + myApp.getWifiAdmin().getSSID()
					+ "，请检查是否和热水器在相同Wifi下!";
		}

		return err;
	}

}
