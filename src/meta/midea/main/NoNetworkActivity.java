package meta.midea.main;

import meta.midea.MyApplication;
import meta.midea.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class NoNetworkActivity extends Activity {
	private MyApplication myApp;

	private ImageView backImg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.nonetwork);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		backImg = (ImageView) findViewById(R.id.noNetworkBackBtn);
		backImg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(SetHelpActivity.this, MainActivity.class);
//				startActivity(intent);
//				NoNetworkActivity.this.finish();
			}
		});

		setFaceLevel();
	}

	private void setFaceLevel() {
		LinearLayout pageLL = (LinearLayout) findViewById(R.id.noNetworkPage);
		pageLL.getBackground().setLevel(myApp.getFaceLevel());

		RelativeLayout pageHead = (RelativeLayout) findViewById(R.id.noNetworkHead);
		pageHead.getBackground().setLevel(myApp.getFaceLevel());
	}
}
