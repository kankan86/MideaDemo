package meta.midea.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import meta.midea.MyApplication;
import meta.midea.data.LoadData;
import meta.midea.R;
import meta.midea.main.NoNetworkActivity;
import meta.midea.tool.MyTool;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegisterActivity extends Activity {
	private Intent intent;

	private EditText usernameET;
	private EditText passwordET;
	private EditText rePassWordET;
	private EditText cNameET;
	private EditText eNameET;
	private EditText tel1ET;
	private EditText tel2ET;
	private EditText emailET;
	private EditText homeAddressET;
	private EditText officeAddressET;

	private TextView moreTV;
	private LinearLayout moreLL;

	private Button registerBtn;
	private Button cancelBtn;

	private MyApplication myApp;

	private static RelativeLayout noNetworkRL;
	private Button noNetworkBtn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_register);
		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		LinearLayout page = (LinearLayout) findViewById(R.id.wifiRegisterPage);
		page.getBackground().setLevel(myApp.getFaceLevel());

		usernameET = (EditText) findViewById(R.id.wifireg_ET_name);
		passwordET = (EditText) findViewById(R.id.wifireg_ET_password);
		rePassWordET = (EditText) findViewById(R.id.wifireg_ET_msPass);
		cNameET = (EditText) findViewById(R.id.wifireg_ET_cName);
		eNameET = (EditText) findViewById(R.id.wifireg_ET_eName);
		tel1ET = (EditText) findViewById(R.id.wifireg_ET_tel1);
		tel2ET = (EditText) findViewById(R.id.wifireg_ET_tel2);
		emailET = (EditText) findViewById(R.id.wifireg_ET_email);
		homeAddressET = (EditText) findViewById(R.id.wifireg_ET_homeAddress);
		officeAddressET = (EditText) findViewById(R.id.wifireg_ET_officeAddress);

		moreTV = (TextView) findViewById(R.id.moreTV);
		moreLL = (LinearLayout) findViewById(R.id.moreLL);

		registerBtn = (Button) findViewById(R.id.wifireg_Btn_submit);
		cancelBtn = (Button) findViewById(R.id.wifireg_Btn_cancel);

		usernameET.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					check();
				}
			}
		});

		moreTV.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				int moreLLV = moreLL.getVisibility();
				if (moreLLV == View.VISIBLE) {
					Drawable img_off = getResources().getDrawable(
							R.drawable.v5_3_0_profile_arrow_back);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					img_off.setBounds(0, 0, img_off.getMinimumWidth(),
							img_off.getMinimumHeight());
					moreTV.setCompoundDrawables(null, null, img_off, null); // 设置右图标

					moreLL.setVisibility(View.GONE);
				} else if (moreLLV == View.GONE) {
					Drawable img_off = getResources().getDrawable(
							R.drawable.v5_3_0_profile_arrow_back_b);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					img_off.setBounds(0, 0, img_off.getMinimumWidth(),
							img_off.getMinimumHeight());
					moreTV.setCompoundDrawables(null, null, img_off, null); // 设置右图标

					moreLL.setVisibility(View.VISIBLE);
				}
			}
		});

		registerBtn.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				if (usernameET.getText().toString().equals("")) {
					MyTool.openSureAD(RegisterActivity.this, "提示", "用户名不得为空...");
					return;
				}
				if (!isUserName(usernameET.getText().toString())) {
					MyTool.openSureAD(RegisterActivity.this, "提示",
							"用户名只能为英文字母、数字、下划线，长度在3~15个字符，请检查...");
					return;
				}
				if (passwordET.getText().toString().equals("")) {
					MyTool.openSureAD(RegisterActivity.this, "提示",
							"请为您的账号设置安全性密码...");
					return;
				}
				if (!passwordET.getText().toString()
						.equals(rePassWordET.getText().toString())) {
					MyTool.openSureAD(RegisterActivity.this, "提示",
							"您两次输入的密码不一致，请仔细检查...");
					return;
				}
				if (emailET.getText().toString().equals("")) {
					MyTool.openSureAD(RegisterActivity.this, "提示", "邮箱信息要填写的哈");
					return;
				}
				if (!isEmail(emailET.getText().toString())) {
					MyTool.openSureAD(RegisterActivity.this, "提示",
							"请正确填写您的邮箱，方便您信息的找回。");
					return;
				}

				register();
			}
		});

		cancelBtn.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(RegisterActivity.this, IntLoginActivity.class);
				startActivity(intent);
				RegisterActivity.this.finish();
			}

		});

		initNoNetwork();
	}

	private void initNoNetwork() {
		noNetworkRL = (RelativeLayout) findViewById(R.id.wifireg_noNetwork);
		noNetworkBtn = (Button) findViewById(R.id.wifireg_noNetworkBtn);
		setNoNetwork(myApp.isNoNetwork());

		noNetworkBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, NoNetworkActivity.class);
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
	
	public static boolean isUserName(String userName) {
		String strPattern = "^[A-Za-z0-9_]{3,15}+$";

		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(userName);
		return m.matches();
	}

	public static boolean isEmail(String strEmail) {
		String strPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	public void check() {
		String name = usernameET.getText().toString();
		if (name == null || name.equals("")) {
			return;
		}

		if (MyTool.mypDialog != null)
			return;
		MyTool.showProgressDialog("请稍等，检查用户名是否可用...", RegisterActivity.this,
				RegisterActivity.this);
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					msg.obj = LoadData.checkLoginName(usernameET.getText()
							.toString().trim());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				handlerForCheck.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForCheck = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				MyTool.closeProgressDialog();

				if (msg.obj != null && (Boolean) msg.obj) {

				} else {
					MyTool.openSureAD(RegisterActivity.this, "提示", "用户名已注册");
					usernameET.setText("");
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void register() {
		if (MyTool.mypDialog != null)
			return;
		MyTool.showProgressDialog("请稍等，正在注册云账号...", RegisterActivity.this,
				RegisterActivity.this);
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					msg.obj = LoadData.register(usernameET.getText().toString()
							.trim(), passwordET.getText().toString().trim(),
							cNameET.getText().toString().trim(), eNameET
									.getText().toString().trim(), tel1ET
									.getText().toString().trim(), tel2ET
									.getText().toString().trim(), emailET
									.getText().toString().trim(), homeAddressET
									.getText().toString().trim(),
							officeAddressET.getText().toString().trim());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				handlerForRegister.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForRegister = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				MyTool.closeProgressDialog();

				if (msg.obj != null && (Boolean) msg.obj) {
					MyTool.makeText(RegisterActivity.this, "注册成功");
					myApp.setUsername(usernameET.getText().toString().trim());

					intent = new Intent();
					intent.setClass(RegisterActivity.this,
							IntLoginActivity.class);
					startActivity(intent);
					RegisterActivity.this.finish();
				} else {
					MyTool.openSureAD(RegisterActivity.this, "提示", "注册失败");
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
			// MyTool.openAlertDialog(RegisterActivity.this, "系统提示", "确定要退出吗",
			// "确定", "取消", listener, listener, false);

			MyTool.closeSureAD();
			intent = new Intent();
			intent.setClass(RegisterActivity.this, IntLoginActivity.class);
			startActivity(intent);
			RegisterActivity.this.finish();
			return false;
		}
		return false;
	}
}
