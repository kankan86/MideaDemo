package meta.midea;

import java.io.FileInputStream;

import meta.midea.data.LoadData;
import meta.midea.R;
import meta.midea.service.MyService;
import meta.midea.tool.MyTool;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;

public class StartPageActivity extends Activity {

	private MyApplication myApp;
	//private UpdateManager mUpdateManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.startpage);

		// 初始化
		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		initFace();
		// initUpdate();
		initConfig();
		startPage();
	}

	private void initFace() {
		// 初始化皮肤
		try {
			FileInputStream inputStream = openFileInput("face");
			if (inputStream != null) {
				byte[] b = new byte[inputStream.available()];
				inputStream.read(b);
				String face = new String(b);
				myApp.setFaceLevel(Integer.parseInt(face));
			}
		} catch (Exception e) {
		}

		// 设置皮肤
		FrameLayout page = (FrameLayout) findViewById(R.id.startImg);
		page.getBackground().setLevel(myApp.getFaceLevel());
	}

//	private void initUpdate() {
//		// 检测版本是否需要更新
//		mUpdateManager = new UpdateManager(this);
//		// mUpdateManager.checkUpdateInfo();
//		AlertDialog.Builder builder = new Builder(StartPageActivity.this);
//		builder.setTitle("软件版本更新");
//		builder.setMessage("有最新的软件包哦，亲快下载吧~");
//		builder.setPositiveButton("下载", new OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				mUpdateManager.showDownloadDialog();
//			}
//		});
//		builder.setNegativeButton("以后再说", new OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				startPage();
//			}
//		});
//		Dialog noticeDialog = builder.create();
//		noticeDialog.show();
//	}

	private void initConfig() {
		// 读取本地文件
		try {
			String text = MyTool
					.readText(StartPageActivity.this, "config.json");
			if (text != null) {
				JSONObject jsonObj = new JSONObject(text);
				if (jsonObj != null) {
					myApp.setGroup(jsonObj.getString("ip"));
					myApp.setPort(Integer.parseInt(jsonObj.getString("port")));
					myApp.setServerType(jsonObj.getString("serverType"));
					myApp.setServerName(jsonObj.getString("serverName"));
					myApp.setServerIP(jsonObj.getString("serverIP"));
					myApp.setWifiName(jsonObj.getString("wifiName"));
					myApp.setIsSearchAll(jsonObj.getString("isserchAll"));
					myApp.setCloudUrl(jsonObj.getString("cloudUrl"));
					LoadData.BASE_URLFor3G = myApp.getCloudUrl();
					myApp.setSockHost(jsonObj.getString("socketHost"));
					myApp.setSocketPort(Integer.parseInt(jsonObj
							.getString("socketPort")));
					myApp.setCloudUrlForWebSocket(jsonObj
							.getString("cloudUrlForWebSocket"));
					myApp.setSockHostForWebSocket(jsonObj
							.getString("socketHostForWebSocket"));
					myApp.setSocketPortForWebSocket(Integer.parseInt(jsonObj
							.getString("socketPortForWebSocket")));
				}
			}

			String text2 = MyTool.readText(StartPageActivity.this, "city.json");
			myApp.setTextCity(text2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void startPage() {
		// 定义splash 动画
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
		aa.setDuration(500);

		this.findViewById(R.id.logoImg).startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			public void onAnimationEnd(Animation arg0) {
				// 启动service
				Intent i = new Intent();
				i.setClass(StartPageActivity.this, MyService.class);
				startService(i);

				Intent intent = new Intent();
				intent.setClass(StartPageActivity.this, SelModeActivity.class);
				startActivity(intent);
			}

			public void onAnimationRepeat(Animation arg0) {
			}

			public void onAnimationStart(Animation arg0) {
			}

		});
	}

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

			MyTool.openAlertDialog(StartPageActivity.this, "系统提示", "确定要退出吗",
					"确定", "取消", listener, listener, false);
			return false;
		}
		return false;
	}
}
