package meta.midea.login;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import meta.midea.MyApplication;
import meta.midea.SelLinkType;
import meta.midea.SelModeActivity;
import meta.midea.data.LoadData;
import meta.midea.R;
import meta.midea.main.NoNetworkActivity;
import meta.midea.tool.MyTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.RelativeLayout;

public class IntLoginActivity extends Activity {
	private Intent intent;

	public EditText usernameET;
	public EditText passwordET;
	private ImageView moreImg;
	private CheckBox reNameAndPass;

	private ArrayList<String> mList = new ArrayList<String>();
	private boolean mInitPopup;
	private ArrayAdapter<String> mAdapter;
	private ListView mListView;
	private PopupWindow mPopup;
	private boolean mShowing;

	public Button linkBtn;
	public Button regBtn;

	private MyApplication myApp;

	private String userList;

	private static RelativeLayout noNetworkRL;
	private Button noNetworkBtn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_3glogin);
		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		RelativeLayout page = (RelativeLayout) findViewById(R.id.intLoginPage);
		page.getBackground().setLevel(myApp.getFaceLevel());

		// 获取本地保存的账号信息
		// String userList = "{\"user\":"
		// + "["
		// + "	{	\"username\":\"123\","
		// + "		\"password\":\"1\""
		// + "	},"
		// + "	{	\"username\":\"123\","
		// + "		\"password\":\"0\""
		// + "	}" + "]}";
		// userList = myApp.getSp().getString("user", null);
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

		// 初始化登录页面
		usernameET = (EditText) findViewById(R.id.intlogin_ET_name);
		passwordET = (EditText) findViewById(R.id.intlogin_ET_password);
		moreImg = (ImageView) findViewById(R.id.intlogin_moreImg);
		moreImg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mList != null && mList.size() > 0 && !mInitPopup) {
					mInitPopup = true;
					initPopup();
				}
				if (mPopup != null) {
					if (!mShowing) {
						mPopup.showAsDropDown(usernameET, 0, -5);
						mShowing = true;
					} else {
						mPopup.dismiss();
					}
				}
			}
		});

		if (myApp.getUserArr() != null && myApp.getUserArr().length() > 0) {
			if (myApp.getUsername() != null && !myApp.getUsername().equals("")) {
				usernameET.setText(myApp.getUsername());
			} else {
				try {
					for (int i = 0; i < myApp.getUserArr().length(); i++) {
						JSONObject jObj = (JSONObject) myApp.getUserArr()
								.get(i);
						if (jObj.has("last")
								&& jObj.getString("last").equals("1")) {
							usernameET.setText(jObj.getString("username"));
							passwordET.setText(jObj.getString("password"));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
				moreImg.setVisibility(View.VISIBLE);
			}
		}

		reNameAndPass = (CheckBox) findViewById(R.id.intlogin_CheBox_remNamePass);
		reNameAndPass.setChecked(true);

		linkBtn = (Button) findViewById(R.id.intlogin_Btn_link);
		regBtn = (Button) findViewById(R.id.intlogin_Btn_reg);

		linkBtn.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				String usernameStr = usernameET.getText().toString().trim();
				String passStr = passwordET.getText().toString().trim();
				if (usernameStr.equals("") || passStr.equals("")) {
					MyTool.openSureAD(IntLoginActivity.this, "提示",
							"用户名密码都不能为空！");
					return;
				}

				login();
			}
		});

		regBtn.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				myApp.setBindToReg(false);
				intent = new Intent();
				intent.setClass(IntLoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				IntLoginActivity.this.finish();
			}
		});

		initNoNetwork();
	}

	private void initNoNetwork() {
		noNetworkRL = (RelativeLayout) findViewById(R.id.intlogin_noNetwork);
		noNetworkBtn = (Button) findViewById(R.id.intlogin_noNetworkBtn);
		setNoNetwork(myApp.isNoNetwork());

		noNetworkBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(IntLoginActivity.this, NoNetworkActivity.class);
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

	public void login() {
		if (MyTool.mypDialog != null)
			return;
		MyTool.showProgressDialog("请稍等，正在登录云端", IntLoginActivity.this,
				IntLoginActivity.this);
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					msg.obj = LoadData.login(usernameET.getText().toString()
							.trim(), passwordET.getText().toString().trim());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				handlerForLogin.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForLogin = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				MyTool.closeProgressDialog();

				if (msg.obj != null && (Boolean) msg.obj) {
					String newUser;
					String userStr = "{\"user\":[";
					if (reNameAndPass.isChecked()) {
						// String userList = "{\"user\":"
						// + "["
						// + "	{	\"username\":\"123\","
						// + "		\"password\":\"1\""
						// + "	},"
						// + "	{	\"username\":\"123\","
						// + "		\"password\":\"0\""
						// + "	}" + "]}";
						boolean isHas = false;
						if (myApp.getUserArr() != null
								&& myApp.getUserArr().length() > 0) {
							int len = myApp.getUserArr().length();
							for (int i = 0; i < len; i++) {
								try {
									JSONObject jObj = (JSONObject) myApp
											.getUserArr().get(i);

									if (jObj.getString("username").equals(
											myApp.getUsername())) {
										isHas = true;
										newUser = "{\"username\":\""
												+ myApp.getUsername()
												+ "\", \"password\":\""
												+ myApp.getUserpass()
												+ "\", \"last\":\"1\"}";
									} else {
										newUser = "{\"username\":\""
												+ jObj.getString("username")
												+ "\", \"password\":\""
												+ jObj.getString("password")
												+ "\", \"last\":\"0\"}";
									}
									userStr = userStr + newUser
											+ (((i + 1) == len) ? "" : ",");
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							if (!isHas) {
								newUser = ",{\"username\":\""
										+ myApp.getUsername()
										+ "\", \"password\":\""
										+ myApp.getUserpass()
										+ "\", \"last\":\"1\"}";
								userStr = userStr + newUser;
							}
							userStr = userStr + "]}";
						} else {
							newUser = "{\"username\":\"" + myApp.getUsername()
									+ "\", \"password\":\""
									+ myApp.getUserpass()
									+ "\", \"last\":\"1\"}";
							userStr = "{\"user\":[" + newUser + "]}";
						}
					} else {
						if (myApp.getUserArr() != null
								&& myApp.getUserArr().length() > 0) {
							int len = myApp.getUserArr().length();
							for (int i = 0; i < len; i++) {
								try {
									JSONObject jObj = (JSONObject) myApp
											.getUserArr().get(i);

									if (jObj.getString("username").equals(
											myApp.getUsername())) {
										continue;
									} else {
										newUser = "{\"username\":\""
												+ jObj.getString("username")
												+ "\", \"password\":\""
												+ jObj.getString("password")
												+ "\", \"last\":\"0\"}";
									}
									userStr = userStr + newUser
											+ (((i + 1) == len) ? "" : ",");
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							userStr = userStr + "]}";
						} else {
							userStr = "";
						}
					}

					System.out.println("userStr==" + userStr);
					try {
						FileOutputStream fos = openFileOutput("user",
								Context.MODE_PRIVATE);
						fos.write(userStr.getBytes());
						fos.close();
					} catch (Exception e) {
					}

					String isFirstLogin = "";
					try {
						FileInputStream inputStream = openFileInput("first");
						if (inputStream != null) {
							byte[] b = new byte[inputStream.available()];
							inputStream.read(b);
							isFirstLogin = new String(b);
						}
					} catch (Exception e) {
					}

					if (!isFirstLogin.equals("1")) {
						try {
							String v = "1";
							FileOutputStream fos = openFileOutput("first",
									Context.MODE_PRIVATE);
							fos.write(v.getBytes());
							fos.close();
						} catch (Exception e) {
						}
					}

					if (myApp.isIntLogToGetServers()) {
						intent = new Intent();
						intent.setClass(IntLoginActivity.this,
								MyServerListActivity.class);
						startActivity(intent);
					} else {
						LoadData.isWifi = true;
						intent = new Intent();
						intent.setClass(IntLoginActivity.this,
								SelLinkType.class);
						startActivity(intent);
					}

				} else {
					String err = myApp.getLoadErr();
					if (err != null && !err.equals("")) {
						MyTool.openSureAD(IntLoginActivity.this, "提示", err);
						myApp.setLoadErr(null);
					} else {
						MyTool.openSureAD(IntLoginActivity.this, "提示",
								"登录失败，请检查网络是否正常");
					}
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	private void initPopup() {
		mListView = new ListView(this);
		mListView.setCacheColorHint(0x00000000);
		mListView.setDivider(new ColorDrawable(0xffa2a2a2));
		mListView.setDividerHeight(1);
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					JSONObject jObj = (JSONObject) myApp.getUserArr().get(arg2);
					usernameET.setText(jObj.getString("username"));
					passwordET.setText(jObj.getString("password"));
				} catch (JSONException e) {
					e.printStackTrace();
				}

				mPopup.dismiss();
			}
		});
		// 添加长按点击
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// list.remove(arg2);

				MyTool.openAlertDialog(IntLoginActivity.this, "提示",
						"是否删除该账号信息", "确定", "取消",
						new DialogInterface.OnClickListener() {

							@SuppressWarnings("unchecked")
							public void onClick(DialogInterface dialog,
									int which) {
								MyTool.closeSureAD();
								String delName = mList.get(arg2);

								// 按照这种方式做删除操作
								mList.remove(arg2);
								mAdapter = (ArrayAdapter<String>) mListView
										.getAdapter();
								if (!mAdapter.isEmpty()) {
									mListView.setVisibility(View.GONE);
									mAdapter.notifyDataSetChanged(); // 实现数据的实时刷新
									mListView.setVisibility(View.VISIBLE);
								} else {
									mAdapter.notifyDataSetChanged();
								}

								// 判断是否只剩一个用户，只有一个则不显示下拉框
								if (mList.size() == 1) {
									moreImg.setVisibility(View.GONE);
									mPopup.dismiss();
									usernameET.setText(mList.get(0));

									for (int i = 0; i < myApp.getUserArr()
											.length(); i++) {
										try {
											JSONObject jObj = (JSONObject) myApp
													.getUserArr().get(i);
											if (jObj.getString("username")
													.equals(mList.get(0))) {
												passwordET.setText(jObj
														.getString("password"));
											}
										} catch (Exception e) {

										}

									}
								}

								String userList = "";
								JSONArray jsonArr = myApp.getUserArr();
								JSONArray newJsonArr = new JSONArray();
								try {
									int len = jsonArr.length();
									for (int i = 0; i < len; i++) {
										// 判断是否已有此设备信息，有则删除
										JSONObject tempJsonObj = (JSONObject) jsonArr
												.get(i);
										if (tempJsonObj.getString("username")
												.equals(delName)) {
											continue;
										}
										newJsonArr.put(tempJsonObj);
									}

									int newlen = newJsonArr.length();
									userList = "{\"user\":[";
									for (int i = 0; i < newlen; i++) {
										userList = userList
												+ newJsonArr.get(i).toString();
										if (i == (newlen - 1))
											break;
										else
											userList = userList + ",";
									}
									userList = userList + "]}";
								} catch (JSONException e) {
									e.printStackTrace();
								}

								myApp.setUserArr(newJsonArr);
								try {
									FileOutputStream fos = openFileOutput(
											"user", Context.MODE_PRIVATE);
									fos.write(userList.getBytes());
									fos.close();
								} catch (Exception e) {
								}
							}
						}, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								MyTool.closeSureAD();
								dialog.cancel();// 取消弹出框
							}
						}, false);
				return true;
			}
		});

		int height = ViewGroup.LayoutParams.WRAP_CONTENT;
		int width = usernameET.getWidth();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// DialogInterface.OnClickListener listener = new
			// DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog, int which) {
			// switch (which) {
			// case AlertDialog.BUTTON1:// "确认"按钮退出程序
			// MyTool.closeSureAD();
			// myApp.exit(false);
			// break;
			// case AlertDialog.BUTTON2:// "取消"第二个按钮取消对话框
			// MyTool.closeSureAD();
			// dialog.cancel();
			// break;
			// default:
			// break;
			// }
			// }
			// };
			//
			// MyTool.openAlertDialog(IntLoginActivity.this, "系统提示", "确定要退出吗",
			// "确定", "取消", listener, listener, false);

			MyTool.closeSureAD();
			Intent intent = new Intent();
			intent.setClass(IntLoginActivity.this, SelModeActivity.class);
			startActivity(intent);
			IntLoginActivity.this.finish();
			return false;
		}
		return false;
	}

}
