package meta.midea;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import meta.midea.data.LoadData;
import meta.midea.R;
import meta.midea.main.NoNetworkActivity;
import meta.midea.tool.MySlipSwitch;
import meta.midea.tool.WifiAdmin;
import meta.midea.tool.MySlipSwitch.OnSwitchListener;
import meta.midea.tool.MyTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.DhcpInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Spinner;

public class SelWifi extends Activity {
	private ArrayList<String> wifiNameList = new ArrayList<String>();
	private ArrayAdapter<String> adapterspWifi;
	private JSONArray list1;
	private MyApplication myApp;
	private String cloudEnable = "1";
	private String authKeyEnable = "1";

	private String ssidStr;
	private String securityStr;
	private String passphraseStr;

	// 操作选择
	private TextView setIPTV;
	private LinearLayout setIPLL;
	private TextView setCloudTV;
	private LinearLayout setCloudLL;
	private LinearLayout setIPSlip;
	private LinearLayout setCloudSlip;

	// 设置ip
	private LinearLayout setWifiIP;
	private Button selHttpWifi;
	private Spinner wifiHttpSpinner;
	private ImageView wifiHttpSpinnerMoreImg;
	private EditText wifiHttpName;
	private EditText wifiHttpPass;
	private RadioButton staticIp;
	private TextView staticIPTV;
	private LinearLayout staticIpLL;
	private EditText staticIP1;
	private EditText staticIP2;
	private EditText staticIP3;
	private EditText staticIP4;
	private TextView staticIPMaskTV;
	private LinearLayout staticIpMaskLL;
	private EditText staticIPMask1;
	private EditText staticIPMask2;
	private EditText staticIPMask3;
	private EditText staticIPMask4;
	private TextView staticIPGwTV;
	private LinearLayout staticIpGwLL;
	private EditText staticIPGw1;
	private EditText staticIPGw2;
	private EditText staticIPGw3;
	private EditText staticIPGw4;
	private TextView staticIPDns1TV;
	private LinearLayout staticIpDns1LL;
	private EditText staticIPDns11;
	private EditText staticIPDns12;
	private EditText staticIPDns13;
	private EditText staticIPDns14;
	private TextView staticIPDns2TV;
	private LinearLayout staticIpDns2LL;
	private EditText staticIPDns21;
	private EditText staticIPDns22;
	private EditText staticIPDns23;
	private EditText staticIPDns24;
	private Button sendWifi;

	// 设置云端
	private LinearLayout cloudLL;
	private MySlipSwitch slipswitchCloud;
	private EditText authKeyUserName;
	private ImageView authKeyMoreImg;
	private EditText authKeyActCode;
	private EditText interval;
	private Button sendCloud;

	private ArrayList<String> mList = new ArrayList<String>();
	private boolean mInitPopup;
	private ArrayAdapter<String> mAdapter;
	private ListView mListView;
	private PopupWindow mPopup;
	private boolean mShowing;
	private String userList;

	private static final int Dialog_Load_SetCloud = 1;
	private static final int Dialog_Load_Reset = 2;
	private static final int Dialog_Load_SearchWifi = 3;
	private static final int Dialog_Load_LoadInfo = 4;
	private static final int Dialog_Sure_ResetFail = 21;
	private static final int Dialog_Sure_SetModeFail = 22;
	private static final int Dialog_Sure_SetCloudFail = 23;
	private static final int Dialog_Sure_WifiNameNull = 24;
	private static final int Dialog_Sure_NetLinkErr = 25;

	private static RelativeLayout noNetworkRL;
	private Button noNetworkBtn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wifiset);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		// 设置皮肤
		ScrollView scroll = (ScrollView) findViewById(R.id.wifiset_scroll);
		scroll.getBackground().setLevel(myApp.getFaceLevel());
		LinearLayout head = (LinearLayout) findViewById(R.id.wifiset_headTabLL);
		head.getBackground().setLevel(myApp.getFaceLevel());

		// 获取本地保存的用户列表
		try {
			FileInputStream inputStream = openFileInput("user");
			if (inputStream != null) {
				byte[] b = new byte[inputStream.available()];
				inputStream.read(b);
				userList = new String(b);
			}
		} catch (Exception e) {
		}
		if (userList != null) {
			try {
				JSONObject jsonObj = new JSONObject(userList);
				if (jsonObj != null) {
					JSONArray jsonArr = jsonObj.getJSONArray("user");
					myApp.setUserArr(jsonArr);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		setIPTV = (TextView) findViewById(R.id.setIPTV);
		setIPLL = (LinearLayout) findViewById(R.id.setIPLL);
		setCloudTV = (TextView) findViewById(R.id.setCloudTV);
		setCloudLL = (LinearLayout) findViewById(R.id.setCloudLL);
		setIPSlip = (LinearLayout) findViewById(R.id.setIPSlip);
		setCloudSlip = (LinearLayout) findViewById(R.id.setCloudSlip);

		setWifiIP = (LinearLayout) findViewById(R.id.searchHttpWifiLL);
		selHttpWifi = (Button) findViewById(R.id.selHttpWifi);
		wifiHttpSpinner = (Spinner) findViewById(R.id.wifiHttpSpinner);
		wifiHttpSpinnerMoreImg = (ImageView) findViewById(R.id.wifiset_moreImgForSpinner);
		wifiHttpName = (EditText) findViewById(R.id.wifiHttpName);
		wifiHttpPass = (EditText) findViewById(R.id.wifiHttpPass);
		staticIp = (RadioButton) findViewById(R.id.staticIP);

		staticIPTV = (TextView) findViewById(R.id.staticIPTV);
		staticIpLL = (LinearLayout) findViewById(R.id.staticIpLL);
		staticIP1 = (EditText) findViewById(R.id.staticIPET1);
		staticIP2 = (EditText) findViewById(R.id.staticIPET2);
		staticIP3 = (EditText) findViewById(R.id.staticIPET3);
		staticIP4 = (EditText) findViewById(R.id.staticIPET4);

		staticIPMaskTV = (TextView) findViewById(R.id.staticIPMaskTV);
		staticIpMaskLL = (LinearLayout) findViewById(R.id.staticIpMaskLL);
		staticIPMask1 = (EditText) findViewById(R.id.staticIPMaskET1);
		staticIPMask2 = (EditText) findViewById(R.id.staticIPMaskET2);
		staticIPMask3 = (EditText) findViewById(R.id.staticIPMaskET3);
		staticIPMask4 = (EditText) findViewById(R.id.staticIPMaskET4);

		staticIPGwTV = (TextView) findViewById(R.id.staticIPGwTV);
		staticIpGwLL = (LinearLayout) findViewById(R.id.staticIpGwLL);
		staticIPGw1 = (EditText) findViewById(R.id.staticIPGwET1);
		staticIPGw2 = (EditText) findViewById(R.id.staticIPGwET2);
		staticIPGw3 = (EditText) findViewById(R.id.staticIPGwET3);
		staticIPGw4 = (EditText) findViewById(R.id.staticIPGwET4);

		staticIPDns1TV = (TextView) findViewById(R.id.staticIPDns1TV);
		staticIpDns1LL = (LinearLayout) findViewById(R.id.staticIpDns1LL);
		staticIPDns11 = (EditText) findViewById(R.id.staticIPDns1ET1);
		staticIPDns12 = (EditText) findViewById(R.id.staticIPDns1ET2);
		staticIPDns13 = (EditText) findViewById(R.id.staticIPDns1ET3);
		staticIPDns14 = (EditText) findViewById(R.id.staticIPDns1ET4);

		staticIPDns2TV = (TextView) findViewById(R.id.staticIPDns2TV);
		staticIpDns2LL = (LinearLayout) findViewById(R.id.staticIpDns2LL);
		staticIPDns21 = (EditText) findViewById(R.id.staticIPDns2ET1);
		staticIPDns22 = (EditText) findViewById(R.id.staticIPDns2ET2);
		staticIPDns23 = (EditText) findViewById(R.id.staticIPDns2ET3);
		staticIPDns24 = (EditText) findViewById(R.id.staticIPDns2ET4);

		sendWifi = (Button) findViewById(R.id.sendWifi);

		cloudLL = (LinearLayout) findViewById(R.id.cloudLL);
		authKeyUserName = (EditText) findViewById(R.id.authKey_userName);
		authKeyMoreImg = (ImageView) findViewById(R.id.wifiset_moreImg);
		authKeyActCode = (EditText) findViewById(R.id.authKey_serverActCode);
		interval = (EditText) findViewById(R.id.interval);
		sendCloud = (Button) findViewById(R.id.sendCloud);

		slipswitchCloud = (MySlipSwitch) findViewById(R.id.selwifi_cloudSwitch);
		slipswitchCloud.setImageResource(R.drawable.bkg_switch,
				R.drawable.bkg_switch, R.drawable.btn_slip);
		slipswitchCloud.setSwitchState(true);
		slipswitchCloud.setOnSwitchListener(new OnSwitchListener() {

			public void onSwitched(boolean isSwitchOn) {
				if (isSwitchOn) {
					cloudEnable = "1";
					authKeyEnable = "1";
				} else {
					cloudEnable = "0";
					authKeyEnable = "0";
				}
			}
		});

		setIPTV.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setIPSlip.setVisibility(View.VISIBLE);
				setCloudSlip.setVisibility(View.INVISIBLE);

				setWifiIP.setVisibility(View.VISIBLE);
				cloudLL.setVisibility(View.GONE);
			}
		});

		setIPLL.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setIPSlip.setVisibility(View.VISIBLE);
				setCloudSlip.setVisibility(View.INVISIBLE);

				setWifiIP.setVisibility(View.VISIBLE);
				cloudLL.setVisibility(View.GONE);
			}
		});

		setCloudTV.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setIPSlip.setVisibility(View.INVISIBLE);
				setCloudSlip.setVisibility(View.VISIBLE);

				setWifiIP.setVisibility(View.GONE);
				cloudLL.setVisibility(View.VISIBLE);
			}
		});

		setCloudLL.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setIPSlip.setVisibility(View.INVISIBLE);
				setCloudSlip.setVisibility(View.VISIBLE);

				setWifiIP.setVisibility(View.GONE);
				cloudLL.setVisibility(View.VISIBLE);

			}
		});

		authKeyMoreImg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mList != null && mList.size() > 0 && !mInitPopup) {
					mInitPopup = true;
					initPopup();
				}
				if (mPopup != null) {
					if (!mShowing) {
						mPopup.showAsDropDown(authKeyUserName, 0, -5);
						mShowing = true;
					} else {
						mPopup.dismiss();
					}
				}
			}
		});

		if (myApp.getUserArr() != null && myApp.getUserArr().length() > 0) {
			try {
				JSONObject jObj = (JSONObject) myApp.getUserArr().get(0);
				authKeyUserName.setText(jObj.getString("username"));
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (myApp.getUserArr().length() > 1) {
				for (int i = 0; i < myApp.getUserArr().length(); i++) {
					JSONObject jObj;
					try {
						jObj = (JSONObject) myApp.getUserArr().get(i);
						mList.add(jObj.getString("username"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				authKeyMoreImg.setVisibility(View.VISIBLE);
			}
		}

		sendCloud.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (MyTool.mypDialog != null)
					return;
				showDialog(Dialog_Load_SetCloud);
				String userName = authKeyUserName.getText().toString();
				String actCode = authKeyActCode.getText().toString();
				boolean tag = LoadData.sendCloud(cloudEnable, "2",
						myApp.getCloudUrl(), authKeyEnable, userName + ";"
								+ actCode, "");

				if (tag) {
					boolean tag0 = LoadData.setMode(true);
					if (tag0) {
						boolean tag1 = LoadData.resetServer();

						if (tag1) {
							dismissDialog(Dialog_Load_SetCloud);
							if (MyTool.mypDialog != null)
								return;
							showDialog(Dialog_Load_Reset);

							Timer timer = new Timer();
							timer.schedule(new TimerTask() {
								@Override
								public void run() {
									this.cancel();
									dismissDialog(Dialog_Load_Reset);
									Intent intent = new Intent();
									intent.setClass(SelWifi.this,
											SelLinkType.class);
									startActivity(intent);
									SelWifi.this.finish();
								}
							}, 15000, 1000);
						} else {
							showDialog(Dialog_Sure_ResetFail);
						}
					} else {
						showDialog(Dialog_Sure_SetModeFail);
					}
				} else {
					showDialog(Dialog_Sure_SetCloudFail);
				}
			}
		});

		wifiHttpSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				wifiHttpName.setText(wifiNameList.get(arg2));

				if (wifiNameList.get(arg2).equals("")) {
					showDialog(Dialog_Sure_WifiNameNull);
					return;
				}

				if (list1 == null)
					return;

				JSONArray jsonArr;
				try {
					jsonArr = (JSONArray) list1.get(arg2);
					ssidStr = (String) jsonArr.get(0);
					securityStr = (String) jsonArr.get(2);
					passphraseStr = wifiHttpPass.getText().toString();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		selHttpWifi.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				myApp.setWifiAdmin(new WifiAdmin(SelWifi.this));
				boolean tag = true;
				int index = -1;
				if (myApp.getWifiAdmin().getSSID() == null) {
					tag = false;
				} else {
					index = myApp.getWifiAdmin().getSSID()
							.indexOf(myApp.getWifiName());
				}
				if (index <= -1) {
					tag = false;
				}
				if (!tag) {
					AlertDialog.Builder dialogClose = new AlertDialog.Builder(
							SelWifi.this);
					dialogClose
							.setTitle("提示")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setMessage("当前WIFI连接不是可配置热水器，请检查")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											startActivityForResult(
													new Intent(
															android.provider.Settings.ACTION_WIFI_SETTINGS),
													0);
										}
									}).create().show();
				} else {
					LoadData.isWifi = true;
					LoadData.BASE_URLForWIFI = "http://192.168.1.1/";

					if (MyTool.mypDialog != null)
						return;
					showDialog(Dialog_Load_SearchWifi);

					loadWifi();
				}
			}
		});

		sendWifi.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				myApp.setWifiAdmin(new WifiAdmin(SelWifi.this));
				boolean tag = true;
				int index = -1;
				if (myApp.isApMode()) {// AP模式下判断是否是可配置热水器
					if (myApp.getWifiAdmin().getSSID() == null) {
						tag = false;
					} else {
						index = myApp.getWifiAdmin().getSSID()
								.indexOf(myApp.getWifiName());
					}
					if (index <= -1) {
						tag = false;
					}
				}

				if (!tag) {
					AlertDialog.Builder dialogClose = new AlertDialog.Builder(
							SelWifi.this);
					dialogClose
							.setTitle("提示")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setMessage("当前WIFI连接不是可配置热水器，请检查")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											startActivityForResult(
													new Intent(
															android.provider.Settings.ACTION_WIFI_SETTINGS),
													0);
										}
									}).create().show();
				} else {
					ssidStr = wifiHttpName.getText().toString();
					if (ssidStr == null || ssidStr.equals("")) {
						showDialog(Dialog_Sure_WifiNameNull);
						return;
					}
					passphraseStr = wifiHttpPass.getText().toString();

					// if (!isWifiEnable(ssidStr, passphraseStr, securityStr)) {
					// MyTool.openSureAD(SelWifi.this, "提示",
					// "网络连接失败，请确认密码是否正确");
					// return;
					// }

					String staticIpStr = "";
					String staticIpMaskStr = "";
					String staticIpGwStr = "";
					String staticIpDns1Str = "";
					String staticIpDns2Str = "";
					if (staticIp.isChecked()) {
						staticIpStr = staticIP1.getText() + "."
								+ staticIP2.getText() + "."
								+ staticIP3.getText() + "."
								+ staticIP4.getText();
						staticIpMaskStr = staticIPMask1.getText() + "."
								+ staticIPMask2.getText() + "."
								+ staticIPMask3.getText() + "."
								+ staticIPMask4.getText();
						staticIpGwStr = staticIPGw1.getText() + "."
								+ staticIPGw2.getText() + "."
								+ staticIPGw3.getText() + "."
								+ staticIPGw4.getText();
						staticIpDns1Str = staticIPDns11.getText() + "."
								+ staticIPDns12.getText() + "."
								+ staticIPDns13.getText() + "."
								+ staticIPDns14.getText();
						staticIpDns2Str = staticIPDns21.getText() + "."
								+ staticIPDns22.getText() + "."
								+ staticIPDns23.getText() + "."
								+ staticIPDns24.getText();

						myApp.setIpMask(staticIpMaskStr);
						myApp.setIpGW(staticIpGwStr);
					}
					boolean tag1 = LoadData.sendWifi(ssidStr, securityStr,
							passphraseStr, staticIpStr, myApp.getIpMask(),
							myApp.getIpGW(), staticIpDns1Str, staticIpDns2Str);

					if (tag1) {
						MyTool.openAlertDialog(SelWifi.this, "提示", "是否配置云端？",
								"配置", "取消",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										MyTool.closeSureAD();
										setIPSlip.setVisibility(View.INVISIBLE);
										setCloudSlip
												.setVisibility(View.VISIBLE);

										setWifiIP.setVisibility(View.GONE);
										cloudLL.setVisibility(View.VISIBLE);
									}
								}, new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										MyTool.closeSureAD();
										boolean tag0 = false;
										if (myApp.isApMode()) {
											tag0 = LoadData.setMode(true);
										} else {
											tag0 = true;
										}
										if (tag0) {
											boolean tag = LoadData
													.resetServer();

											if (tag) {
												if (MyTool.mypDialog != null)
													return;
												showDialog(Dialog_Load_Reset);

												Timer timer = new Timer();
												timer.schedule(new TimerTask() {
													@Override
													public void run() {
														this.cancel();
														dismissDialog(Dialog_Load_Reset);
														Intent intent = new Intent();
														intent.setClass(
																SelWifi.this,
																SelLinkType.class);
														startActivity(intent);
														SelWifi.this.finish();
													}
												}, 15000, 1000);
											} else {
												showDialog(Dialog_Sure_ResetFail);
											}
										} else {
											showDialog(Dialog_Sure_SetModeFail);
										}
									}
								}, true);
					} else {
						showDialog(Dialog_Sure_NetLinkErr);
					}
				}
			}
		});

		initData();
		initNoNetwork();
	}

	private void initNoNetwork() {
		noNetworkRL = (RelativeLayout) findViewById(R.id.wifiset_noNetwork);
		noNetworkBtn = (Button) findViewById(R.id.wifiset_noNetworkBtn);
		setNoNetwork(myApp.isNoNetwork());

		noNetworkBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SelWifi.this, NoNetworkActivity.class);
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

	// 验证wifi密码是否正确
	// private boolean isWifiEnable(String ssid, String pass, String type) {
	// // Security type can be open, wep, wpa, wpaaes, wpa2,
	// // wap2aes, wpa2tkip, or unknown.
	//
	// int passType = 0;
	// if (type.indexOf("wpa") > -1) {
	// passType = myApp.getWifiAdmin().WIFICIPHER_WPA;
	// } else if (type.indexOf("wep") > -1) {
	// passType = myApp.getWifiAdmin().WIFICIPHER_WEP;
	// }
	// WifiConfiguration wc = myApp.getWifiAdmin().createWifiInfo(ssid, pass,
	// passType);
	// int i = myApp.getWifiAdmin().addNetwork(wc);
	// // boolean b = myApp.getWifiAdmin().linkNetwork(wc);
	// // if (i == -1 || !b) {
	// // return false;
	// // }
	// System.out.println("验证wifi密码：i==" + i);
	// if (i == -1) {
	// return false;
	// }
	//
	// return false;
	// }

	private void initData() {
		if (myApp.isApMode()) {
			myApp.setWifiAdmin(new WifiAdmin(SelWifi.this));
			// 如果是AP的配置，则提示要切换到Midea-WH开头的wifi
			boolean tag = true;
			int index = -1;
			if (myApp.getWifiAdmin().getSSID() == null) {
				tag = false;
			} else {
				index = myApp.getWifiAdmin().getSSID()
						.indexOf(myApp.getWifiName());
			}
			if (index <= -1) {
				tag = false;
			}

			if (!tag) {
				AlertDialog.Builder dialogClose = new AlertDialog.Builder(
						SelWifi.this);
				dialogClose
						.setTitle("提示")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setMessage("请切换为以'Midea-WH'开头的WIFI网络")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										startActivityForResult(
												new Intent(
														android.provider.Settings.ACTION_WIFI_SETTINGS),
												0);
									}
								}).create().show();
			}
		} else {
			myApp.setWifiAdmin(new WifiAdmin(this));
			DhcpInfo info = myApp.getWifiAdmin().getDhcpInfo();
			System.out.println("dhcpInfo==" + info);
			System.out.println("info.dns1=="
					+ Formatter.formatIpAddress(info.dns1));
			System.out.println("info.dns2=="
					+ Formatter.formatIpAddress(info.dns2));
			System.out.println("info.gateway=="
					+ Formatter.formatIpAddress(info.gateway));
			System.out.println("info.netmask=="
					+ Formatter.formatIpAddress(info.netmask));
			myApp.setIpDns1(Formatter.formatIpAddress(info.dns1));
			myApp.setIpDns2(Formatter.formatIpAddress(info.dns2));
			myApp.setIpGW(Formatter.formatIpAddress(info.gateway));
			myApp.setIpMask(Formatter.formatIpAddress(info.netmask));

			LoadData.BASE_URLForWIFI = "http://" + myApp.getStaticIp() + "/";
			// 如果是修改wifi的网络配置，则需要读取基本信息
			staticIp.setVisibility(View.VISIBLE);
			staticIPTV.setVisibility(View.VISIBLE);
			staticIpLL.setVisibility(View.VISIBLE);
			staticIPMaskTV.setVisibility(View.VISIBLE);
			staticIpMaskLL.setVisibility(View.VISIBLE);
			staticIPGwTV.setVisibility(View.VISIBLE);
			staticIpGwLL.setVisibility(View.VISIBLE);
			staticIPDns1TV.setVisibility(View.VISIBLE);
			staticIpDns1LL.setVisibility(View.VISIBLE);
			staticIPDns2TV.setVisibility(View.VISIBLE);
			staticIpDns2LL.setVisibility(View.VISIBLE);
			if (MyTool.mypDialog != null)
				return;
			showDialog(Dialog_Load_LoadInfo);
			loadNetwork();
		}
	}

	private void initPopup() {
		mListView = new ListView(this);
		mListView.setCacheColorHint(0x00000000);
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					JSONObject jObj = (JSONObject) myApp.getUserArr().get(arg2);
					authKeyUserName.setText(jObj.getString("username"));
				} catch (JSONException e) {
					e.printStackTrace();
				}

				mPopup.dismiss();
			}
		});
		int height = ViewGroup.LayoutParams.WRAP_CONTENT;
		int width = authKeyUserName.getWidth();
		mPopup = new PopupWindow(mListView, width, height, true);
		mPopup.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.popwindowshape));
		mPopup.setFocusable(true);
		mPopup.update();
		mPopup.setOnDismissListener(new android.widget.PopupWindow.OnDismissListener() {
			public void onDismiss() {
				mShowing = false;
			}
		});
	}

	public void loadWifi() {
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					list1 = LoadData.getWifi();
					if (list1 != null) {
						for (int j = 0; j < list1.length(); j++) {
							JSONArray jsonArr = (JSONArray) list1.get(j);
							wifiNameList.add((String) jsonArr.get(0));
						}
					}
					msg.obj = wifiNameList;
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForWifi.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForWifi = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dismissDialog(Dialog_Load_SearchWifi);

				// 没有搜到时，判断是否wifi是否连接的热水器wifi
				if (list1 == null || list1.length() == 0) {
					myApp.setWifiAdmin(new WifiAdmin(SelWifi.this));
					boolean tag = true;
					int index = -1;
					if (myApp.getWifiAdmin().getSSID() == null) {
						tag = false;
					} else {
						index = myApp.getWifiAdmin().getSSID()
								.indexOf(myApp.getWifiName());
					}
					if (index <= -1) {
						tag = false;
					}
					if (!tag) {
						AlertDialog.Builder dialogClose = new AlertDialog.Builder(
								SelWifi.this);
						dialogClose
								.setTitle("提示")
								.setIcon(android.R.drawable.ic_dialog_info)
								.setMessage("当前WIFI连接不是可配置热水器，请检查")
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												startActivityForResult(
														new Intent(
																android.provider.Settings.ACTION_WIFI_SETTINGS),
														0);
											}
										}).create().show();
					} else {
						MyTool.makeText(SelWifi.this,
								"获取网络列表失败，请检查热水器wifi工作是否正常");
					}
					return;
				}

				if (adapterspWifi != null)
					adapterspWifi.clear();
				wifiNameList = new ArrayList<String>();

				try {
					for (int j = 0; j < list1.length(); j++) {
						JSONArray jsonArr = (JSONArray) list1.get(j);
						wifiNameList.add((String) jsonArr.get(0));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				adapterspWifi = new ArrayAdapter<String>(SelWifi.this,
						android.R.layout.simple_spinner_item, wifiNameList);
				adapterspWifi
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				adapterspWifi.notifyDataSetChanged();

				wifiHttpSpinner.setAdapter(adapterspWifi);
				wifiHttpSpinner.setVisibility(View.VISIBLE);
				wifiHttpSpinnerMoreImg.setVisibility(View.VISIBLE);
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void loadNetwork() {
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					msg.obj = LoadData.getNetWork();
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForLoadNetWork.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForLoadNetWork = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null) {
					// {"apchannel":11,"ipaddr":"192.168.254.250",
					// "ipdns1":"202.96.209.133","ipmask":"255.255.255.0",
					// "passphrase":"meta@817!","apsecurity":"open",
					// "apssid":"Midea-WH-E7A8F1","appassphrase":"",
					// "ipgw":"192.168.254.1","dhcp":0,
					// "ssid":"Meta-link","ipdns2":"61.151.246.246","security":"wpa2aes"}
					try {
						JSONObject jObj = (JSONObject) msg.obj;
						wifiHttpName.setText(jObj.getString("ssid"));
						wifiHttpPass.setText(jObj.getString("passphrase"));
						securityStr = jObj.getString("security");
						if (jObj.getString("dhcp").equals("0")) {
							staticIp.setChecked(true);
							myApp.setStaticIp(jObj.getString("ipaddr"));
							myApp.setIpMask(jObj.getString("ipmask"));
							myApp.setIpGW(jObj.getString("ipgw"));
							myApp.setIpDns1(jObj.getString("ipdns1"));
							myApp.setIpDns2(jObj.getString("ipdns2"));
						} else {
							staticIp.setChecked(false);
						}

						String[] staticIP = myApp.getStaticIp().split("\\.");
						if (staticIP.length == 4) {
							staticIP1.setText(staticIP[0]);
							staticIP2.setText(staticIP[1]);
							staticIP3.setText(staticIP[2]);
							staticIP4.setText(staticIP[3]);
						}

						String[] staticIPMask = myApp.getIpMask().split("\\.");
						if (staticIPMask.length == 4) {
							staticIPMask1.setText(staticIPMask[0]);
							staticIPMask2.setText(staticIPMask[1]);
							staticIPMask3.setText(staticIPMask[2]);
							staticIPMask4.setText(staticIPMask[3]);
						} else {
							staticIPMask1.setText("255");
							staticIPMask2.setText("255");
							staticIPMask3.setText("255");
							staticIPMask4.setText("0");
						}

						String[] staticIPGw = myApp.getIpGW().split("\\.");
						if (staticIPGw.length == 4) {
							staticIPGw1.setText(staticIPGw[0]);
							staticIPGw2.setText(staticIPGw[1]);
							staticIPGw3.setText(staticIPGw[2]);
							staticIPGw4.setText(staticIPGw[3]);
						} else {
							staticIPGw1.setText(staticIP[0]);
							staticIPGw2.setText(staticIP[1]);
							staticIPGw3.setText(staticIP[2]);
							staticIPGw4.setText("1");
						}

						String[] staticIPDns1 = myApp.getIpDns1().split("\\.");
						if (staticIPDns1.length == 4) {
							staticIPDns11.setText(staticIPDns1[0]);
							staticIPDns12.setText(staticIPDns1[1]);
							staticIPDns13.setText(staticIPDns1[2]);
							staticIPDns14.setText(staticIPDns1[3]);
						} else {
							staticIPDns11.setText(staticIP[0]);
							staticIPDns12.setText(staticIP[1]);
							staticIPDns13.setText(staticIP[2]);
							staticIPDns14.setText("1");
						}

						String[] staticIPDns2 = myApp.getIpDns2().split("\\.");
						if (staticIPDns2.length == 4) {
							staticIPDns21.setText(staticIPDns2[0]);
							staticIPDns22.setText(staticIPDns2[1]);
							staticIPDns23.setText(staticIPDns2[2]);
							staticIPDns24.setText(staticIPDns2[3]);
						} else {
							staticIPDns11.setText(staticIP[0]);
							staticIPDns12.setText(staticIP[1]);
							staticIPDns13.setText(staticIP[2]);
							staticIPDns14.setText("1");
						}

						loadNetCloud();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void loadNetCloud() {
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					msg.obj = LoadData.getCloud();
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForLoadNetCloud.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForLoadNetCloud = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dismissDialog(Dialog_Load_LoadInfo);
				if (msg.obj != null) {
					// {"enable":1,"interval":2,
					// "url":"http://119.147.213.171/customize/control/getServicesOfCloudMothod",
					// "isauthkey":1,"authkey":"midea;jbh101"}
					try {
						JSONObject jObj = (JSONObject) msg.obj;
						if (jObj.getString("enable").equals("1")) {
							slipswitchCloud.setSwitchState(true);
							cloudEnable = "1";
							authKeyEnable = "1";
							String[] authKey = jObj.getString("authkey").split(
									"\\;");
							authKeyUserName.setText(authKey[0]);
							authKeyActCode.setText(authKey[1]);
							interval.setText(jObj.getString("interval"));
						} else {
							slipswitchCloud.setSwitchState(false);
							cloudEnable = "0";
							authKeyEnable = "0";
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case AlertDialog.BUTTON1:// "确认"按钮退出程序
						MyTool.closeSureAD();
						myApp.exit(false);
						break;
					case AlertDialog.BUTTON2:// "取消"第二个按钮取消对话框
						MyTool.closeSureAD();
						dialog.cancel();
						break;
					default:
						break;
					}
				}
			};

			MyTool.openAlertDialog(SelWifi.this, "系统提示", "确定要退出吗", "确定", "取消",
					listener, listener, false);
		}
		return false;
	}

	/**************** 对话框 **************/
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case Dialog_Load_SetCloud: {
			ProgressDialog checkUpdateIng = new ProgressDialog(SelWifi.this);
			checkUpdateIng.setMessage("正在配置云服务，请稍等。。。");
			checkUpdateIng.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return checkUpdateIng;
		}
		case Dialog_Load_Reset: {
			ProgressDialog checkUpdateIng = new ProgressDialog(SelWifi.this);
			checkUpdateIng.setMessage("配置成功，等待设备启动。。。");
			checkUpdateIng.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return checkUpdateIng;
		}
		case Dialog_Load_SearchWifi: {
			ProgressDialog checkUpdateIng = new ProgressDialog(SelWifi.this);
			checkUpdateIng.setMessage("请稍等，正在搜索Wifi。。。");
			checkUpdateIng.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return checkUpdateIng;
		}
		case Dialog_Load_LoadInfo: {
			ProgressDialog checkUpdateIng = new ProgressDialog(SelWifi.this);
			checkUpdateIng.setMessage("请稍等，正在加载信息...");
			checkUpdateIng.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return checkUpdateIng;
		}
		case Dialog_Sure_ResetFail: {
			Dialog dialog = new AlertDialog.Builder(SelWifi.this)
					.setTitle("提示")
					.setMessage("重启设备失败")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}

							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.cancel();
								}
							}).create();
			return dialog;
		}
		case Dialog_Sure_SetModeFail: {
			Dialog dialog = new AlertDialog.Builder(SelWifi.this)
					.setTitle("提示")
					.setMessage("设置模式失败")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}

							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.cancel();
								}
							}).create();
			return dialog;
		}
		case Dialog_Sure_SetCloudFail: {
			Dialog dialog = new AlertDialog.Builder(SelWifi.this)
					.setTitle("提示")
					.setMessage("设置云服务器失败")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}

							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.cancel();
								}
							}).create();
			return dialog;
		}
		case Dialog_Sure_WifiNameNull: {
			Dialog dialog = new AlertDialog.Builder(SelWifi.this)
					.setTitle("提示")
					.setMessage("wifi名称不得为空")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}

							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.cancel();
								}
							}).create();
			return dialog;
		}
		case Dialog_Sure_NetLinkErr: {
			Dialog dialog = new AlertDialog.Builder(SelWifi.this)
					.setTitle("提示")
					.setMessage("设置网络连接失败")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}

							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.cancel();
								}
							}).create();
			return dialog;
		}
		default: {
			return null;
		}
		}
	}
}
