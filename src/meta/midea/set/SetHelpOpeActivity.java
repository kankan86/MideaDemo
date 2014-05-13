package meta.midea.set;

import meta.midea.MyApplication;
import meta.midea.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SetHelpOpeActivity extends Activity {
	private MyApplication myApp;

	private ImageView backImg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_help_ope);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		backImg = (ImageView) findViewById(R.id.setHelpBackBtn);
		backImg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SetHelpOpeActivity.this, SetHelpActivity.class);
				startActivity(intent);
				SetHelpOpeActivity.this.finish();
			}
		});
		setFaceLevel();
	}

	private void setFaceLevel() {
		LinearLayout pageLL = (LinearLayout) findViewById(R.id.setHelpOpePage);
		pageLL.getBackground().setLevel(myApp.getFaceLevel());
	
		RelativeLayout pageHead = (RelativeLayout) findViewById(R.id.setHelpOpeHead);
		pageHead.getBackground().setLevel(myApp.getFaceLevel());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			intent.setClass(SetHelpOpeActivity.this, SetHelpActivity.class);
			startActivity(intent);
			SetHelpOpeActivity.this.finish();
			return false;
		}
		return false;
	}
}
