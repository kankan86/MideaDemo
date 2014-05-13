package meta.midea.set;

import meta.midea.MyApplication;
import meta.midea.R;
import meta.midea.main.DemoActivity;
import meta.midea.main.MainActivity;
import meta.midea.main.NoNetworkActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SetErrActivity extends Activity {
	private MyApplication myApp;

	private ImageView backImg;
	
	private static RelativeLayout noNetworkRL;
	private Button noNetworkBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_err);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		backImg = (ImageView) findViewById(R.id.setErrBackBtn);
		backImg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				if (myApp.isDemoMode()) {
					intent.setClass(SetErrActivity.this, DemoActivity.class);
				} else {
					intent.setClass(SetErrActivity.this, MainActivity.class);
				}
				startActivity(intent);
				SetErrActivity.this.finish();
			}
		});

		setFaceLevel();
		initNoNetwork();
	}
	
	private void initNoNetwork() {
		noNetworkRL = (RelativeLayout) findViewById(R.id.seterr_noNetwork);
		noNetworkBtn = (Button) findViewById(R.id.seterr_noNetworkBtn);
		setNoNetwork(myApp.isNoNetwork());

		noNetworkBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SetErrActivity.this, NoNetworkActivity.class);
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
		LinearLayout pageLL = (LinearLayout) findViewById(R.id.setErrPage);
		pageLL.getBackground().setLevel(myApp.getFaceLevel());

		RelativeLayout pageHead = (RelativeLayout) findViewById(R.id.setErrHead);
		pageHead.getBackground().setLevel(myApp.getFaceLevel());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			if (myApp.isDemoMode()) {
				intent.setClass(SetErrActivity.this, DemoActivity.class);
			} else {
				intent.setClass(SetErrActivity.this, MainActivity.class);
			}
			startActivity(intent);
			SetErrActivity.this.finish();
			return false;
		}
		return false;
	}

}
