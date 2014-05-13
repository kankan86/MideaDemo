package meta.midea;

import java.io.FileInputStream;

import meta.midea.data.LoadData;
import meta.midea.login.IntLoginActivity;
import meta.midea.login.MyServerListActivity;
import meta.midea.main.DemoActivity;
import meta.midea.main.MainActivity;
import meta.midea.main.NoNetworkActivity;
import meta.midea.tool.MyTool;
import meta.midea.tool.WifiAdmin;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SelModeActivity extends Activity {

	private Button localBtn;
	private Button cloudBtn;
	private Button demoBtn;
	private Button testBtn;

	private static RelativeLayout noNetworkRL;
	private Button noNetworkBtn;

	private MyApplication myApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.selmode);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);
		myApp.setWifiAdmin(new WifiAdmin(SelModeActivity.this));

		LinearLayout page = (LinearLayout) findViewById(R.id.selmode_page);
		page.getBackground().setLevel(myApp.getFaceLevel());

		localBtn = (Button) findViewById(R.id.localBtn);
		cloudBtn = (Button) findViewById(R.id.cloudBtn);
		demoBtn = (Button) findViewById(R.id.demoBtn);
		testBtn = (Button) findViewById(R.id.testBtn);

		localBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!isWifiOpen())
					return;

				myApp.setDemoMode(false);

				if (!isFirstLogin().equals("1")) {
					AlertDialog.Builder dialogClose = new AlertDialog.Builder(
							SelModeActivity.this);
					dialogClose
							.setTitle("提示")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setMessage("您是首次登陆系统，需要先登陆云账号！")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent();
											intent.setClass(
													SelModeActivity.this,
													IntLoginActivity.class);
											startActivity(intent);
										}
									}).create().show();
				} else {
					LoadData.isWifi = true;
					Intent intent = new Intent();
					intent.setClass(SelModeActivity.this, SelLinkType.class);
					startActivity(intent);
				}

				// 192.168.43.108
				// LoadData.isWifi = true;
				// myApp.setCurWifiIP("192.168.43.108");
				// myApp.setCurServerAuthKey("midea;111117");
				// LoadData.BASE_URLForWIFI = "http://192.168.43.108/";
				// Intent intent = new Intent();
				// intent.setClass(SelModeActivity.this, MainActivity.class);
				// startActivity(intent);
			}
		});

		cloudBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				myApp.setDemoMode(false);
				LoadData.isWifi = false;
				MyTool.showProgressDialog("请稍等，正在为您的软件创建同云平台的连接...",
						SelModeActivity.this, SelModeActivity.this);
				getCloudContent();
			}
		});

		demoBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// boolean tag0 = LoadData.setMode(true);
				myApp.setDemoMode(true);

				Intent intent = new Intent();
				intent.setClass(SelModeActivity.this, DemoActivity.class);
				startActivity(intent);
			}
		});

		testBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				myApp.setTestMode(true);
				myApp.setDemoMode(false);
				LoadData.isAddAuthKey = false;

				Intent intent = new Intent();
				intent.setClass(SelModeActivity.this, SelLinkType.class);
				startActivity(intent);
			}
		});

		initNoNetwork();
	}

	private void initNoNetwork() {
		noNetworkRL = (RelativeLayout) findViewById(R.id.selmode_noNetwork);
		noNetworkBtn = (Button) findViewById(R.id.selmode_noNetworkBtn);
		setNoNetwork(myApp.isNoNetwork());

		noNetworkBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SelModeActivity.this, NoNetworkActivity.class);
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

	private String isFirstLogin() {
		String isFirstLogin = "";
		try {
			FileInputStream inputStream = openFileInput("first");
			if (inputStream != null) {
				byte[] b = new byte[inputStream.available()];
				inputStream.read(b);
				isFirstLogin = new String(b);
			}
		} catch (Exception e) {
			return isFirstLogin;
		}

		return isFirstLogin;
	}

	private boolean isWifiOpen() {
		if (myApp.getWifiAdmin().isWifiEnable()) {
			return true;
		} else {
			AlertDialog.Builder dialogClose = new AlertDialog.Builder(
					SelModeActivity.this);
			dialogClose
					.setTitle("提示")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage("请打开WIFI连接，开启成功重新登陆系统。")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									startActivityForResult(
											new Intent(
													android.provider.Settings.ACTION_WIFI_SETTINGS),
											0);
									myApp.exit(false);
								}
							}).create().show();

			return false;
		}
	}

	// 获取同云端的连接
	public void getCloudContent() {
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

	private Handler handlerForGetCloudContent = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				MyTool.closeProgressDialog();
				if (msg.obj != null && (Boolean) msg.obj) {
					myApp.setIntLogToGetServers(true);
					if (myApp.getSessionID() == null
							|| myApp.getSessionID().equals("")) {
						Intent intent = new Intent();
						intent.setClass(SelModeActivity.this,
								IntLoginActivity.class);
						startActivity(intent);
					} else {
						Intent intent = new Intent();
						intent.setClass(SelModeActivity.this,
								MyServerListActivity.class);
						startActivity(intent);
					}
				} else {
					System.out.println("同云平台连接失败，请检查网络连接是否正常！");
					MyTool.openSureAD(SelModeActivity.this, "提示",
							"同云平台连接失败，请检查网络连接是否正常！");
					return;
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

			MyTool.openAlertDialog(SelModeActivity.this, "系统提示", "确定要退出吗",
					"确定", "取消", listener, listener, false);
			return false;
		}
		return false;
	}
}
