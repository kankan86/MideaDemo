package meta.midea.set;

import meta.midea.MyApplication;
import meta.midea.R;
import meta.midea.main.DemoActivity;
import meta.midea.main.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SetStartActivity extends Activity {
	private MyApplication myApp;

	private ImageView backImg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_setstart);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		backImg = (ImageView) findViewById(R.id.setStartBackBtn);
		backImg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				if (myApp.isDemoMode()) {
					intent.setClass(SetStartActivity.this, DemoActivity.class);
				} else {
					intent.setClass(SetStartActivity.this, MainActivity.class);
				}
				startActivity(intent);
				SetStartActivity.this.finish();
			}
		});
		setFaceLevel();
	}

	private void setFaceLevel() {
		LinearLayout pageLL = (LinearLayout) findViewById(R.id.setStartPage);
		pageLL.getBackground().setLevel(myApp.getFaceLevel());
	
		RelativeLayout pageHead = (RelativeLayout) findViewById(R.id.setStartHead);
		pageHead.getBackground().setLevel(myApp.getFaceLevel());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			if (myApp.isDemoMode()) {
				intent.setClass(SetStartActivity.this, DemoActivity.class);
			} else {
				intent.setClass(SetStartActivity.this, MainActivity.class);
			}
			startActivity(intent);
			SetStartActivity.this.finish();
			return false;
		}
		return false;
	}

}
