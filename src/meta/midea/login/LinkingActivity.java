package meta.midea.login;

import meta.midea.MyApplication;
import meta.midea.R;
import meta.midea.main.MainActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class LinkingActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(this);

		setContentView(R.layout.linking);
		
//		LinearLayout linkingLL = (LinearLayout) findViewById(R.id.linkingLL);
//		View linkingView = new com.android.midea.tool.CustomGifView(this, R.drawable.linking);
//		linkingLL.addView(linkingView);
		
		AlphaAnimation aa = new AlphaAnimation(1.0f, 1.0f);
		aa.setDuration(1000);

		this.findViewById(R.id.linking).startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			public void onAnimationEnd(Animation arg0) {
				Intent intent = new Intent();
				intent.setClass(LinkingActivity.this, MainActivity.class);
				startActivity(intent);
				LinkingActivity.this.finish();
			}

			public void onAnimationRepeat(Animation arg0) {
			}

			public void onAnimationStart(Animation arg0) {
			}

		});
	}

}
