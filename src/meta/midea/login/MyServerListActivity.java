package meta.midea.login;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import meta.midea.MyApplication;
import meta.midea.SelModeActivity;
import meta.midea.data.LoadData;
import meta.midea.R;
import meta.midea.main.MainActivity;
import meta.midea.main.NoNetworkActivity;
import meta.midea.tool.MyTool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyServerListActivity extends Activity {

	private MyApplication myApp;

	private LinearLayout serverListTabLL;
	private LinearLayout addserverTabLL;
	private TextView serverListTV;
	private TextView addserverTV;
	private LinearLayout serverListSlip;
	private LinearLayout addserverSlip;

	private ListView serverList;

	private LinearLayout addServerLL;
	private EditText serverNameET;
	private EditText serverActCodeET;
	@SuppressWarnings("unused")
	private Spinner serverCitySpinner;
	@SuppressWarnings("unused")
	private ImageView serverCitySpinnerMoreImg;
	private Button saveBtn;

	@SuppressWarnings("unused")
	private ArrayList<String> cityNameList = new ArrayList<String>();
	@SuppressWarnings("unused")
	private ArrayAdapter<String> adapterspCity;
	
	private static RelativeLayout noNetworkRL;
	private Button noNetworkBtn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_serverlist);
		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		LinearLayout page = (LinearLayout) findViewById(R.id.logServerList_page);
		page.getBackground().setLevel(myApp.getFaceLevel());
		LinearLayout head = (LinearLayout) findViewById(R.id.serverlist_headTabLL);
		head.getBackground().setLevel(myApp.getFaceLevel());

		serverListTabLL = (LinearLayout) findViewById(R.id.serverlist_listLL);
		addserverTabLL = (LinearLayout) findViewById(R.id.serverlist_addLL);
		serverListTV = (TextView) findViewById(R.id.serverlist_listTV);
		addserverTV = (TextView) findViewById(R.id.serverlist_addTV);
		serverListSlip = (LinearLayout) findViewById(R.id.serverlist_listSlip);
		addserverSlip = (LinearLayout) findViewById(R.id.serverlist_addSlip);
		serverList = (ListView) findViewById(R.id.serverlist_listView);
		addServerLL = (LinearLayout) findViewById(R.id.serverlist_addserverLL);
		serverNameET = (EditText) findViewById(R.id.serverlist_servernameET);
		serverActCodeET = (EditText) findViewById(R.id.serverlist_serveractcodeET);
		serverCitySpinner = (Spinner) findViewById(R.id.serverCitySpinner);
		serverCitySpinnerMoreImg = (ImageView) findViewById(R.id.serverCity_moreImgForSpinner);
		saveBtn = (Button) findViewById(R.id.saveBtn);

		getServers();

		serverListTV.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				serverListSlip.setVisibility(View.VISIBLE);
				addserverSlip.setVisibility(View.INVISIBLE);

				serverList.setVisibility(View.VISIBLE);
				addServerLL.setVisibility(View.GONE);
			}
		});

		addserverTV.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				serverListSlip.setVisibility(View.INVISIBLE);
				addserverSlip.setVisibility(View.VISIBLE);

				serverList.setVisibility(View.GONE);
				addServerLL.setVisibility(View.VISIBLE);
			}
		});

		saveBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String name = serverNameET.getText().toString();
				String code = serverActCodeET.getText().toString();
				if (name == null || name.equals("")) {
					MyTool.openSureAD(MyServerListActivity.this, "提示",
							"设备名是您热水器的标识，不可以为空哈！");
					return;
				}
				if (code == null || code.equals("")) {
					MyTool.openSureAD(MyServerListActivity.this, "提示",
							"激活码不可以为空！");
					return;
				}

				saveServer();
			}
		});

		// setCityData();
		initNoNetwork();
	}
	
	private void initNoNetwork() {
		noNetworkRL = (RelativeLayout) findViewById(R.id.serverlist_noNetwork);
		noNetworkBtn = (Button) findViewById(R.id.serverlist_noNetworkBtn);
		setNoNetwork(myApp.isNoNetwork());

		noNetworkBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MyServerListActivity.this, NoNetworkActivity.class);
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

//	private void setCityData() {
//		String cityStr = myApp.getTextCity();
//		String[] selCitys = new String[0];
//		JSONObject result = null;
//		try {
//			if (cityStr != null)
//				result = new JSONObject(cityStr);
//		} catch (JSONException e1) {
//			System.out.println(e1.getMessage());
//			e1.printStackTrace();
//		}
//		if (result != null) {
//			selCitys = new String[result.length()];
//			Iterator<?> it = result.keys();
//			int i = 0;
//			while (it.hasNext()) {// 遍历JSONObject
//				selCitys[i] = (String) (it.next().toString());
//				i++;
//			}
//		}
//
//		for (int j = 0; j < selCitys.length; j++) {
//			cityNameList.add((String) selCitys[j]);
//		}
//
//		if (adapterspCity != null)
//			adapterspCity.clear();
//
//		adapterspCity = new ArrayAdapter<String>(MyServerListActivity.this,
//				android.R.layout.simple_spinner_item, cityNameList);
//		adapterspCity
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		adapterspCity.notifyDataSetChanged();
//
//		serverCitySpinner.setAdapter(adapterspCity);
//		serverCitySpinner.setVisibility(View.VISIBLE);
//		serverCitySpinnerMoreImg.setVisibility(View.VISIBLE);
//	}

	private void addList(ArrayList<HashMap<String, Object>> listArr) {
		int len = listArr.size();

		// 生成动态数组，加入数据
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < len; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemTitle", listArr.get(i).get("deviceName"));
			map.put("ItemText", "设备别名：" + listArr.get(i).get("otherName"));
			listItem.add(map);
		}
		// 生成适配器的Item和动态数组对应的元素
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
				R.layout.list_items,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { "ItemTitle", "ItemText" },
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.ItemTitle, R.id.ItemText });

		// 添加并且显示
		serverList.setAdapter(listItemAdapter);
		// 添加点击
		serverList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					if (myApp.getCloudServersList() == null)
						return;
					JSONObject obj = (JSONObject) myApp.getCloudServersList()
							.get(arg2);
					if (obj == null)
						return;
					myApp.setCurProType(obj.getString("productModel"));
					myApp.setCurServerAuthKey(obj.getString("authKey"));
					myApp.setCurServerActCode(myApp.getCurServerAuthKey()
							.split(";")[1]);

					LoadData.isWifi = false;
					// if (!LoadData.openSocket()) {
					// MyTool.openAlertDialog(MyServerListActivity.this, "提示",
					// "连接云服务器socket失败，请检查网络是否正常", "确定", null,
					// new DialogInterface.OnClickListener() {
					//
					// public void onClick(DialogInterface dialog,
					// int which) {
					// dialog.cancel();
					// }
					// }, null);
					// return;
					// }

					Intent intent = new Intent();
					intent.setClass(MyServerListActivity.this,
							MainActivity.class);
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		// 添加长按点击
		serverList
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						System.out.println("长按选中的列"
								+ serverList.getCheckedItemPosition());
					}
				});
	}

	public void getServers() {
		MyTool.showProgressDialog("请稍等，正在获取设备列表...", MyServerListActivity.this,
				MyServerListActivity.this);
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					msg.obj = LoadData.getServers(myApp.getUsername(),
							myApp.getSessionID());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				handlerForGetServers.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForGetServers = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				MyTool.closeProgressDialog();

				if (msg.obj != null && (Boolean) msg.obj) {

					if (myApp.getCloudServersList() != null) {
						int len = myApp.getCloudServersList().length();

						if (len == 0) {
							MyTool.openSureAD(MyServerListActivity.this, "提示",
									"该账号无可用设备，请新增设备");
							serverListSlip.setVisibility(View.INVISIBLE);
							addserverSlip.setVisibility(View.VISIBLE);

							serverList.setVisibility(View.GONE);
							addServerLL.setVisibility(View.VISIBLE);

							return;
						}

						ArrayList<HashMap<String, Object>> nameList = new ArrayList<HashMap<String, Object>>();
						HashMap<String, Object> map = new HashMap<String, Object>();
						for (int i = 0; i < len; i++) {
							try {
								JSONObject jobj = (JSONObject) myApp
										.getCloudServersList().get(i);

								if (jobj.has("deviceName")) {
									map = new HashMap<String, Object>();
									map.put("deviceName",
											jobj.getString("deviceName"));
									map.put("otherName",
											jobj.has("otherName") ? jobj
													.getString("otherName")
													: "");
									nameList.add(map);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						serverList.setVisibility(View.VISIBLE);
						addServerLL.setVisibility(View.GONE);
						addList(nameList);
					}
				} else {
					MyTool.openSureAD(MyServerListActivity.this, "提示",
							"获取设备列表失败");
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void saveServer() {
		if (MyTool.mypDialog != null)
			return;
		MyTool.showProgressDialog("请稍等，正在保存设备...", MyServerListActivity.this,
				MyServerListActivity.this);
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					msg.obj = LoadData.saveServers(serverNameET.getText()
							.toString().trim(), serverActCodeET.getText()
							.toString().trim(), myApp.getUsername(),
							myApp.getSessionID());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				handlerForSaveServer.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForSaveServer = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				MyTool.closeProgressDialog();

				if (msg.obj != null && (Boolean) msg.obj) {

					if (myApp.getCloudServersList() != null) {
						int len = myApp.getCloudServersList().length();

						if (len == 0) {
							MyTool.openSureAD(MyServerListActivity.this, "提示",
									"该账号无可用设备，请新增设备");
							serverListTabLL.setBackgroundColor(0xff226DDD);
							addserverTabLL.setBackgroundColor(0xffffffff);

							serverList.setVisibility(View.GONE);
							addServerLL.setVisibility(View.VISIBLE);

							return;
						}

						ArrayList<HashMap<String, Object>> nameList = new ArrayList<HashMap<String, Object>>();
						HashMap<String, Object> map = new HashMap<String, Object>();
						for (int i = 0; i < len; i++) {
							try {
								JSONObject jobj = (JSONObject) myApp
										.getCloudServersList().get(i);
								// {"ipAddr":"0:0:0:0:0:0:0:1",
								// "deviceName":"五楼大厅",
								// "authKey":"midea;111114",
								// "productModel":"F50J",
								// "city":"0"}]}

								if (jobj.has("deviceName")) {
									map = new HashMap<String, Object>();
									map.put("deviceName",
											jobj.getString("deviceName"));
									map.put("otherName",
											jobj.has("otherName") ? jobj
													.getString("otherName")
													: "");
									nameList.add(map);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						serverListTabLL.setBackgroundColor(0xffffffff);
						addserverTabLL.setBackgroundColor(0xff226DDD);

						serverList.setVisibility(View.VISIBLE);
						addServerLL.setVisibility(View.GONE);

						addList(nameList);
					} else {
						MyTool.openSureAD(MyServerListActivity.this, "提示",
								myApp.getCloudAddServerStr());
					}
				} else {
					MyTool.openSureAD(MyServerListActivity.this, "提示",
							"保存设备失败 ，请检网络连接是否正常");
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	@Override
	protected void onDestroy() {
		MyTool.closeProgressDialog();
		super.onDestroy();
		System.out.println("MyServerListActivity onDestroy~~~");

		// MyTool.closeProgressDialog();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					switch (which) {
//					case AlertDialog.BUTTON1:// "确认"按钮退出程序
//						MyTool.closeSureAD();
//						myApp.exit(false);
//						break;
//					case AlertDialog.BUTTON2:// "取消"第二个按钮取消对话框
//						MyTool.closeSureAD();
//						dialog.cancel();
//						break;
//					default:
//						break;
//					}
//				}
//			};
//
//			MyTool.openAlertDialog(MyServerListActivity.this, "系统提示", "确定要退出吗",
//					"确定", "取消", listener, listener, false);
			
			MyTool.closeSureAD();
			Intent intent = new Intent();
			intent.setClass(MyServerListActivity.this, SelModeActivity.class);
			startActivity(intent);
			MyServerListActivity.this.finish();
			return false;
		}
		return false;
	}
}
