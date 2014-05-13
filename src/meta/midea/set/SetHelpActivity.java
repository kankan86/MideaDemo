package meta.midea.set;

import meta.midea.MyApplication;
import meta.midea.R;
import meta.midea.main.DemoActivity;
import meta.midea.main.MainActivity;
import meta.midea.main.NoNetworkActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SetHelpActivity extends Activity {
	private MyApplication myApp;

	private ImageView backImg;
	private ImageView toHelpInfoImg;
	private ImageView toHelpOpeImg;
	
	private static RelativeLayout noNetworkRL;
	private Button noNetworkBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_help);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		backImg = (ImageView) findViewById(R.id.setHelpBackBtn);
		backImg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				if (myApp.isDemoMode()) {
					intent.setClass(SetHelpActivity.this, DemoActivity.class);
				} else {
					intent.setClass(SetHelpActivity.this, MainActivity.class);
				}
				startActivity(intent);
				SetHelpActivity.this.finish();
			}
		});

		toHelpInfoImg = (ImageView) findViewById(R.id.sethelp_help1Btn);
		toHelpInfoImg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SetHelpActivity.this, SetHelpInfoActivity.class);
				startActivity(intent);
			}
		});

		toHelpOpeImg = (ImageView) findViewById(R.id.sethelp_help2Btn);
		toHelpOpeImg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SetHelpActivity.this, SetHelpOpeActivity.class);
				startActivity(intent);
			}
		});

		// 设置字体加粗
		TextView tv1 = (TextView) findViewById(R.id.textView2);
		TextPaint paint1 = tv1.getPaint();
		paint1.setFakeBoldText(true);
		TextView tv2 = (TextView) findViewById(R.id.textView4);
		TextPaint paint2 = tv2.getPaint();
		paint2.setFakeBoldText(true);
		TextView tv3 = (TextView) findViewById(R.id.textView6);
		TextPaint paint3 = tv3.getPaint();
		paint3.setFakeBoldText(true);
		TextView tv4 = (TextView) findViewById(R.id.textView8);
		TextPaint paint4 = tv4.getPaint();
		paint4.setFakeBoldText(true);
		TextView tv5 = (TextView) findViewById(R.id.textView10);
		TextPaint paint5 = tv5.getPaint();
		paint5.setFakeBoldText(true);
		TextView tv6 = (TextView) findViewById(R.id.textView12);
		TextPaint paint6 = tv6.getPaint();
		paint6.setFakeBoldText(true);
		TextView tv7 = (TextView) findViewById(R.id.textView14);
		TextPaint paint7 = tv7.getPaint();
		paint7.setFakeBoldText(true);
		TextView tv8 = (TextView) findViewById(R.id.textView16);
		TextPaint paint8 = tv8.getPaint();
		paint8.setFakeBoldText(true);
		TextView tv9 = (TextView) findViewById(R.id.textView18);
		TextPaint paint9 = tv9.getPaint();
		paint9.setFakeBoldText(true);
		TextView tv10 = (TextView) findViewById(R.id.textView20);
		TextPaint paint10 = tv10.getPaint();
		paint10.setFakeBoldText(true);
		TextView tv11 = (TextView) findViewById(R.id.textView22);
		TextPaint paint11 = tv11.getPaint();
		paint11.setFakeBoldText(true);
		TextView tv12 = (TextView) findViewById(R.id.textView24);
		TextPaint paint12 = tv12.getPaint();
		paint12.setFakeBoldText(true);
		TextView tv13 = (TextView) findViewById(R.id.textView26);
		TextPaint paint13 = tv13.getPaint();
		paint13.setFakeBoldText(true);
		TextView tv14 = (TextView) findViewById(R.id.textView28);
		TextPaint paint14 = tv14.getPaint();
		paint14.setFakeBoldText(true);

		setFaceLevel();
		initNoNetwork();
	}
	
	private void initNoNetwork() {
		noNetworkRL = (RelativeLayout) findViewById(R.id.sethelp_noNetwork);
		noNetworkBtn = (Button) findViewById(R.id.sethelp_noNetworkBtn);
		setNoNetwork(myApp.isNoNetwork());

		noNetworkBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SetHelpActivity.this, NoNetworkActivity.class);
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
		LinearLayout pageLL = (LinearLayout) findViewById(R.id.setHelpPage);
		pageLL.getBackground().setLevel(myApp.getFaceLevel());

		RelativeLayout pageHead = (RelativeLayout) findViewById(R.id.setHelpHead);
		pageHead.getBackground().setLevel(myApp.getFaceLevel());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			if (myApp.isDemoMode()) {
				intent.setClass(SetHelpActivity.this, DemoActivity.class);
			} else {
				intent.setClass(SetHelpActivity.this, MainActivity.class);
			}
			startActivity(intent);
			SetHelpActivity.this.finish();
			return false;
		}
		return false;
	}
}
