package meta.midea.set;

import meta.midea.MyApplication;
import meta.midea.R;
import meta.midea.data.LoadData;
import meta.midea.main.DemoActivity;
import meta.midea.main.MainActivity;
import meta.midea.main.NoNetworkActivity;
import meta.midea.tool.MyTool;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SetUserActivity extends Activity {
	private MyApplication myApp;

	private ImageView backImg;

	private EditText userNameET;
	private EditText oldPassET;
	private EditText newPassET;
	private EditText msNewPassET;
	private Button saveBtn;

	private static RelativeLayout noNetworkRL;
	private Button noNetworkBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_setuser);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		backImg = (ImageView) findViewById(R.id.setUserBackBtn);
		backImg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				if (myApp.isDemoMode()) {
					intent.setClass(SetUserActivity.this, DemoActivity.class);
				} else {
					intent.setClass(SetUserActivity.this, MainActivity.class);
				}
				startActivity(intent);
				SetUserActivity.this.finish();
			}
		});

		userNameET = (EditText) findViewById(R.id.setuser_usernameET);
		userNameET.setText("用户名："
				+ (myApp.getUsername() == null ? "" : myApp.getUsername()));
		userNameET.setKeyListener(null);
		oldPassET = (EditText) findViewById(R.id.setuser_oldpassET);
		newPassET = (EditText) findViewById(R.id.setuser_newpassET);
		msNewPassET = (EditText) findViewById(R.id.setuser_mspassET);
		saveBtn = (Button) findViewById(R.id.setuser_saveBtn);
		saveBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (userNameET.getText().toString().equals("")) {
					MyTool.openSureAD(SetUserActivity.this, "提示", "用户名不得为空");
					return;
				}

				if (oldPassET.getText().toString().equals("")) {
					MyTool.openSureAD(SetUserActivity.this, "提示", "旧密码不得为空");
					return;
				}

				if (!msNewPassET.getText().toString()
						.equals(newPassET.getText().toString())) {
					MyTool.openSureAD(SetUserActivity.this, "提示", "新密码两次输入不统一");
					return;
				}

				update();
			}
		});

		setFaceLevel();
		initNoNetwork();
	}

	private void initNoNetwork() {
		noNetworkRL = (RelativeLayout) findViewById(R.id.setuser_noNetwork);
		noNetworkBtn = (Button) findViewById(R.id.setuser_noNetworkBtn);
		setNoNetwork(myApp.isNoNetwork());

		noNetworkBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SetUserActivity.this, NoNetworkActivity.class);
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

	private void setFaceLevel() {
		LinearLayout pageLL = (LinearLayout) findViewById(R.id.setUserPage);
		pageLL.getBackground().setLevel(myApp.getFaceLevel());

		RelativeLayout pageHead = (RelativeLayout) findViewById(R.id.setUserHead);
		pageHead.getBackground().setLevel(myApp.getFaceLevel());
	}

	public void update() {
		if (MyTool.mypDialog != null)
			return;
		MyTool.showProgressDialog("请稍等，正在保存设置...", SetUserActivity.this,
				SetUserActivity.this);
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					msg.obj = LoadData.update(myApp.getUsername(), msNewPassET
							.getText().toString().trim(), myApp.getSessionID());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				handlerForUpdate.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForUpdate = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				MyTool.closeProgressDialog();

				if (msg.obj != null && (Boolean) msg.obj) {
					MyTool.makeText(SetUserActivity.this, "更新成功");
					userNameET.setText("");
					oldPassET.setText("");
					newPassET.setText("");
					msNewPassET.setText("");
				} else {
					MyTool.openSureAD(SetUserActivity.this, "提示",
							"更新失败，请检查网络是否可用");
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
			Intent intent = new Intent();
			if (myApp.isDemoMode()) {
				intent.setClass(SetUserActivity.this, DemoActivity.class);
			} else {
				intent.setClass(SetUserActivity.this, MainActivity.class);
			}
			startActivity(intent);
			SetUserActivity.this.finish();
			return false;
		}
		return false;
	}

}
