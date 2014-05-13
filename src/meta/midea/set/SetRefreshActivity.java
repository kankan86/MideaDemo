package meta.midea.set;

import meta.midea.MyApplication;
import meta.midea.R;
import meta.midea.data.LoadData;
import meta.midea.main.DemoActivity;
import meta.midea.main.MainActivity;
import android.app.Activity;
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
import android.widget.SeekBar;

public class SetRefreshActivity extends Activity {
	private MyApplication myApp;

	private ImageView backImg;
	private SeekBar seekBar;
	private Button saveBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_setrefresh);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		backImg = (ImageView) findViewById(R.id.setRefreshBackBtn);
		backImg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				if (myApp.isDemoMode()) {
					intent.setClass(SetRefreshActivity.this, DemoActivity.class);
				} else {
					intent.setClass(SetRefreshActivity.this, MainActivity.class);
				}
				startActivity(intent);
				SetRefreshActivity.this.finish();
			}
		});

		seekBar = (SeekBar) findViewById(R.id.set_refresh_seekBar);
		seekBar.setProgress(LoadData.TIME);
		saveBtn = (Button) findViewById(R.id.set_refresh_saveBtn);
		saveBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("刷新速率：" + seekBar.getProgress());
				//LoadData.TIME = seekBar.getProgress();
			}
		});

		setFaceLevel();
	}

	private void setFaceLevel() {
		LinearLayout pageLL = (LinearLayout) findViewById(R.id.setRefreshPage);
		pageLL.getBackground().setLevel(myApp.getFaceLevel());

		RelativeLayout pageHead = (RelativeLayout) findViewById(R.id.setRefreshHead);
		pageHead.getBackground().setLevel(myApp.getFaceLevel());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			if (myApp.isDemoMode()) {
				intent.setClass(SetRefreshActivity.this, DemoActivity.class);
			} else {
				intent.setClass(SetRefreshActivity.this, MainActivity.class);
			}
			startActivity(intent);
			SetRefreshActivity.this.finish();
			return false;
		}
		return false;
	}

}
