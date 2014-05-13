package meta.midea.event;

import meta.midea.MyApplication;
import meta.midea.R;
import meta.midea.set.SetHelpOpeActivity;
import meta.midea.tool.MyTool;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class EventActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(this);

		setContentView(R.layout.err);
		
		ImageView errIV = (ImageView) findViewById(R.id.errIV);
		
		final FrameLayout errLL = (FrameLayout) findViewById(R.id.errFL);
		errIV.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				errLL.setVisibility(View.VISIBLE);
			}
		});

		Button errBtn = (Button) findViewById(R.id.errBtn);
		TextView errInfoTV = (TextView) findViewById(R.id.errInfo);
		
		String errStr = MyTool.HexStringToBinary(MyApplication.getInstance().getRefErrMark()).toString();
		char[] errCharArr = errStr.toCharArray();
		String errText = "";
		if(errCharArr[7]=='1'){
			errText = "漏电保护(E1)";
		}
		if(errCharArr[6]=='1'){
			errText = "干烧保护(E2)";
		}
		if(errCharArr[5]=='1'){
			errText = "超温保护(E3)";
		}
		if(errCharArr[4]=='1'){
			errText = "传感器故障保护(E4)";
		}
		errInfoTV.setText(errText);

		errBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("isEvent", "1");
				intent.putExtras(bundle);
				intent.setClass(EventActivity.this, SetHelpOpeActivity.class);
				startActivity(intent);
				EventActivity.this.finish();
			}
		});

		errLL.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("isEvent", "1");
				intent.putExtras(bundle);
				intent.setClass(EventActivity.this, SetHelpOpeActivity.class);
				startActivity(intent);
				EventActivity.this.finish();
			}
		});

	}
}
