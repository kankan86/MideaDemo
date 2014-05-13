package meta.midea.set;

import java.io.FileOutputStream;

import meta.midea.MyApplication;
import meta.midea.R;
import meta.midea.main.DemoActivity;
import meta.midea.main.MainActivity;
import meta.midea.main.NoNetworkActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SetFaceActivity extends Activity implements OnClickListener {
	private MyApplication myApp;

	private ImageView backImg;

	private ImageView faceImg0;
	private ImageView faceImg1;
	private ImageView faceImg2;

	private static RelativeLayout noNetworkRL;
	private Button noNetworkBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_setface);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		backImg = (ImageView) findViewById(R.id.setFaceBackBtn);
		backImg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				if (myApp.isDemoMode()) {
					intent.setClass(SetFaceActivity.this, DemoActivity.class);
				} else {
					intent.setClass(SetFaceActivity.this, MainActivity.class);
				}
				startActivity(intent);
				SetFaceActivity.this.finish();
			}
		});

		faceImg0 = (ImageView) findViewById(R.id.faceImg0);
		faceImg1 = (ImageView) findViewById(R.id.faceImg1);
		faceImg2 = (ImageView) findViewById(R.id.faceImg2);
		faceImg0.setOnClickListener(this);
		faceImg1.setOnClickListener(this);
		faceImg2.setOnClickListener(this);

		setFaceLevel();
		initNoNetwork();
	}

	private void initNoNetwork() {
		noNetworkRL = (RelativeLayout) findViewById(R.id.setface_noNetwork);
		noNetworkBtn = (Button) findViewById(R.id.setface_noNetworkBtn);
		setNoNetwork(myApp.isNoNetwork());

		noNetworkBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SetFaceActivity.this, NoNetworkActivity.class);
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
		LinearLayout pageLL = (LinearLayout) findViewById(R.id.setFacePage);
		pageLL.getBackground().setLevel(myApp.getFaceLevel());

		RelativeLayout pageHead = (RelativeLayout) findViewById(R.id.setFaceHead);
		pageHead.getBackground().setLevel(myApp.getFaceLevel());
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case (R.id.faceImg0):
			myApp.setFaceLevel(0);
			saveFaceLevel("0");
			setFaceLevel();
			break;
		case (R.id.faceImg1):
			myApp.setFaceLevel(1);
			saveFaceLevel("1");
			setFaceLevel();
			break;
		case (R.id.faceImg2):
			myApp.setFaceLevel(2);
			saveFaceLevel("2");
			setFaceLevel();
			break;
		}
	}

	private void saveFaceLevel(String level) {
		try {
			FileOutputStream fos = openFileOutput("face", Context.MODE_PRIVATE);
			fos.write(level.getBytes());
			fos.close();
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			if (myApp.isDemoMode()) {
				intent.setClass(SetFaceActivity.this, DemoActivity.class);
			} else {
				intent.setClass(SetFaceActivity.this, MainActivity.class);
			}
			startActivity(intent);
			SetFaceActivity.this.finish();
			return false;
		}
		return false;
	}

}
