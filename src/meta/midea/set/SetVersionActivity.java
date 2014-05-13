package meta.midea.set;

import meta.midea.MyApplication;
import meta.midea.R;
import meta.midea.data.LoadData;
import meta.midea.main.DemoActivity;
import meta.midea.main.MainActivity;
import meta.midea.main.NoNetworkActivity;
import meta.midea.tool.MyTool;
import meta.midea.tool.UpdateManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SetVersionActivity extends Activity {
	private MyApplication myApp;

	private ImageView backImg;
	private TextView curVersionValue;
	private TextView newVersionValue;
	private Button updateBtn;

	private static RelativeLayout noNetworkRL;
	private Button noNetworkBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_version);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		backImg = (ImageView) findViewById(R.id.setVersionBackBtn);
		backImg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				if (myApp.isDemoMode()) {
					intent.setClass(SetVersionActivity.this, DemoActivity.class);
				} else {
					intent.setClass(SetVersionActivity.this, MainActivity.class);
				}
				startActivity(intent);
				SetVersionActivity.this.finish();
			}
		});
		curVersionValue = (TextView) findViewById(R.id.textView_version_curValue);
		newVersionValue = (TextView) findViewById(R.id.textView_version_newValue);
		updateBtn = (Button) findViewById(R.id.set_version_updateBtn);
		updateBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				update();
			}
		});

		setFaceLevel();
		checkVersion();
		initNoNetwork();
	}

	private void initNoNetwork() {
		noNetworkRL = (RelativeLayout) findViewById(R.id.setversion_noNetwork);
		noNetworkBtn = (Button) findViewById(R.id.setversion_noNetworkBtn);
		setNoNetwork(myApp.isNoNetwork());

		noNetworkBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SetVersionActivity.this,
						NoNetworkActivity.class);
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

	private void update() {
		UpdateManager mUpdateManager = new UpdateManager(
				SetVersionActivity.this);
		mUpdateManager.apkUrl = myApp.getNewVersionUrl();
		mUpdateManager.showDownloadDialog();
	}

	private void checkVersion() {
		MyTool.showProgressDialog("请稍等，正在检查是否有新版本。。。", SetVersionActivity.this,
				SetVersionActivity.this);
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					String curVersion = MyTool
							.getVersion(SetVersionActivity.this);
					curVersionValue.setText("V" + curVersion);
					msg.obj = LoadData.checkVersion(curVersion);
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForCheckVersion.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForCheckVersion = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				MyTool.closeProgressDialog();
				if (msg.obj != null && (Boolean) msg.obj) {
					String newVersionUrl = myApp.getNewVersionUrl();
					if (newVersionUrl.equals("")) {
						newVersionValue.setText("V"
								+ curVersionValue.getText().toString()
										.replace("V", ""));
						updateBtn.setVisibility(View.GONE);
					} else {
						// MideaAndroid1219V1.2.apk
						String names[] = newVersionUrl.split("V");
						newVersionValue.setText("V"
								+ names[1].replace(".apk", ""));
						updateBtn.setVisibility(View.VISIBLE);
					}
				} else {
					MyTool.openSureAD(SetVersionActivity.this, "提示",
							"检查版本失败，请检查网络是否工作正常！");
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	private void setFaceLevel() {
		LinearLayout pageLL = (LinearLayout) findViewById(R.id.setVersionPage);
		pageLL.getBackground().setLevel(myApp.getFaceLevel());

		RelativeLayout pageHead = (RelativeLayout) findViewById(R.id.setVersionHead);
		pageHead.getBackground().setLevel(myApp.getFaceLevel());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			if (myApp.isDemoMode()) {
				intent.setClass(SetVersionActivity.this, DemoActivity.class);
			} else {
				intent.setClass(SetVersionActivity.this, MainActivity.class);
			}
			startActivity(intent);
			SetVersionActivity.this.finish();
			return false;
		}
		return false;
	}

}
