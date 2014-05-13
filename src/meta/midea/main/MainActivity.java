package meta.midea.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import meta.midea.MyApplication;
import meta.midea.SelLinkType;
import meta.midea.data.LoadData;
import meta.midea.R;
import meta.midea.login.MyServerListActivity;
import meta.midea.service.MyReceiver;
import meta.midea.set.SetErrActivity;
import meta.midea.set.SetFaceActivity;
import meta.midea.set.SetHelpActivity;
import meta.midea.set.SetLinkActivity;
import meta.midea.set.SetRefreshActivity;
import meta.midea.set.SetStartActivity;
import meta.midea.set.SetUserActivity;
import meta.midea.set.SetVersionActivity;
import meta.midea.tool.MyTool;
import meta.midea.util.slidelayout.SlideLayout;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private MyApplication myApp;

	private SlideLayout slideLayout;// 滑动Layout
	private ImageButton rightButton;
	private Button openBtn;

	private LinearLayout cloudLL;
	private RelativeLayout heatingTimeLL;
	private LinearLayout slip2LL;
	private RelativeLayout cloudInfoLL;
	private LinearLayout reserveLL;

	private TextView trueTempTitle1;
	private TextView trueTempTitle2;
	private TextView trueTemp;
	private TextView trueTemptag;

	private LinearLayout heatingTagLL;
	private ImageView heatingImg;
	private TextView heatingTV;
	private TextView heatTV;

	private LinearLayout stateTag;
	private TextView stateTitleTV;
	private TextView stateTempValueTV;
	private TextView stateTempTagTV;
	private TextView hotwarterTitleTV;
	private TextView hotwarterValueTV;
	private TextView hotwarterNoTV;
	private ProgressBar hotwarterProbar;

	private TextView heatingTimeTitle;
	private TextView heatingTimeHourTV;
	private TextView heatingTimeMinuteTV;
	private TextView heatingTimeHourTagTV;
	private TextView heatingTimeMinuteTagTV;

	private TextView reserveTitle;
	private TextView noReserveInfoTV;
	private TextView reserveInfoTV1;
	private TextView reserveInfoTV2;
	private TextView reserveInfoTV3;

	private ImageView cloudTag;
	private ProgressBar cloudLoadProbar;
	private TextView cloudCityTitleTV;
	private TextView cloudCityTempTV;
	private TextView cloudTempTitleTV;
	private TextView cloudTempTV;

	private Button cloudBtn;
	private Button jsxBtn;
	private Button yyxBtn;

	private LinearLayout jsxLL;
	private LinearLayout yyxLL;
	private LinearLayout jsxListLL;

	private Button jsxSetTempUpImg;
	private Button jsxSetTempDownImg;
	private TextView jsxSetTempSaveBtn;
	private TextView jsxSetTempValueTV;
	private EditText jsxSetTempValueET;

	private TextView reserve1TV;
	private TextView reserve2TV;
	private TextView reserve3TV;
	private ImageView reserve1Img;
	private ImageView reserve2Img;
	private ImageView reserve3Img;
	private RelativeLayout reserve1RL;
	private RelativeLayout reserve2RL;
	private RelativeLayout reserve3RL;
	private LinearLayout reserve1ImgLL;
	private LinearLayout reserve2ImgLL;
	private LinearLayout reserve3ImgLL;

	private ImageView setFaceImg;
	private ImageView setLinkImg;
	private ImageView setVersionImg;
	private ImageView setStartImg;
	private ImageView setUserImg;
	private ImageView setRefreshImg;
	private ImageView setHelpImg;
	private ImageView setErrImg;
	private TextView setFaceTV;
	private TextView setLinkTV;
	private TextView setVersionTV;
	private TextView setStartTV;
	private TextView setUserTV;
	private TextView setRefreshTV;
	private TextView setHelpTV;
	private TextView setErrTV;
	private LinearLayout setFaceLL;
	private LinearLayout setLinkLL;
	private LinearLayout setVersionLL;
	private LinearLayout setStartLL;
	private LinearLayout setUserLL;
	private LinearLayout setRefreshLL;
	private LinearLayout setHelpLL;
	private LinearLayout setErrLL;
	private ImageView setFaceTag;
	private ImageView setLinkTag;
	private ImageView setVersionTag;
	private ImageView setStartTag;
	private ImageView setUserTag;
	private ImageView setRefreshTag;
	private ImageView setHelpTag;
	private ImageView setErrTag;

	private boolean isJSXLLOpen;
	private boolean isYYXLLOpen;

	private boolean isClose = true;
	private boolean isJSXBtnOpen = false;
	private boolean isYYXBtnOpen = false;
	private boolean isCloudBtnOpen = false;
	private boolean isHeating = false;// 正在加热
	private boolean isHeat = false;// 正在保温
	private boolean isUse = false;// 正在使用
	private boolean isLoading = false;// 正在匹配
	private boolean isErr = false;// 是否有故障
	private String errStr = "4";// 故障代码

	private Handler handlerForTempUp;
	private Runnable runnableForTempUp;
	private Handler handlerForTempDown;
	private Runnable runnableForTempDown;
	private Handler handlerForGetWeather;
	private Runnable runnableForGetWeather;

	private long downTime;
	private long upTime;

	private int curX;
	private int curY;
	private int curW;
	private int curH;

	private Timer timerForTVFlash;
	private int clo = 0;// 文字闪烁记录标记
	private String curSetModeCName;// 记录当前设置模式的名称
	private Timer timerForRefresh;
	// private boolean isTimerStart;
	// private Handler handlerForDemoTemp;
	// private Runnable runnableForDemoTemp;
	public String setReserveName = "";// 记录设置预约名称
	public String socketBackName; // 记录socket返回调用的方法
	private boolean isLoadData = false;// 正在交互

	private String callSetTempName;// 设置温度由谁调用("cloud"云智能,"jsxMode"即时洗模式,"jsxSet"即时洗自设)

	private Intent intent;
	private MyReceiver receiver;

	private final int closeColor = 0xff808080;// 关闭状态下文本颜色
	private final int openColor = 0xffffffff;// 开启状体下文本颜色
	private final int heatingColor = 0xffFFFF22;// 加热状态下文本颜色
	private final int heatTagOpenColor = 0xff000000;// 开启状态下加热提示文本颜色
	// private final int shadowColor = 0xff4d4d4d;// 文本阴影颜色

	private static RelativeLayout noNetworkRL;
	private Button noNetworkBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		initPanelPage();
		initSetPage();
		setFaceLevel();
		setPanelClose();
		initNoNetwork();
	}

	private void initNoNetwork() {
		noNetworkRL = (RelativeLayout) findViewById(R.id.panel_noNetwork);
		noNetworkBtn = (Button) findViewById(R.id.panel_noNetworkBtn);
		setNoNetwork(myApp.isNoNetwork());

		noNetworkBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, NoNetworkActivity.class);
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

	private void initDataOpe() {
		// // 启动service
		// Intent i = new Intent();
		// i.setClass(MainActivity.this, MyService.class);
		// startService(i);

		// 初始化预约匹配数组
		if (myApp.getReserveList() == null) {
			myApp.setReserveList(new ArrayList<Map<String, Object>>());
			myApp.getReserveList().add(null);
			myApp.getReserveList().add(null);
			myApp.getReserveList().add(null);
		}

		// 开机刷新状态以及预约
		// getServerType();
		getCity();

		// 注册广播
		regReceiver();

		// 更新authKey
		if (!myApp.isTestMode())
			updateAuthKey();
	}

	protected void onResume() {
		super.onResume();
		setFaceLevel();

		jsxLL.setVisibility(View.GONE);
		yyxLL.setVisibility(View.GONE);

		// 不是第一次加载主页面，则不去做获取机型操作
		if (!myApp.isFirstLoadMain()) {
			setJSXList();
			if (myApp.isReserveToMain() && !myApp.isReserveToMainAndClose()) {
				loadRefresh();
			} else {
				setMainState(true);
			}
			return;
		}

		initDataOpe();
	}

	private void setFaceLevel() {
		int level = myApp.getFaceLevel();

		RelativeLayout pageLL = (RelativeLayout) findViewById(R.id.appPage);
		pageLL.getBackground().setLevel(level);

		RelativeLayout pageHead = (RelativeLayout) findViewById(R.id.panel_head);
		pageHead.getBackground().setLevel(level);

		if (!isClose) {
			openBtn.setBackgroundResource(R.drawable.bg_skin_openbtn);
			openBtn.getBackground().setLevel(level);
		}

		rightButton.getBackground().setLevel(level);

		if (isCloudBtnOpen)
			cloudBtn.getBackground().setLevel(level);
		if (isJSXBtnOpen)
			jsxBtn.getBackground().setLevel(level);
		if (isYYXBtnOpen)
			yyxBtn.getBackground().setLevel(level);
	}

	// 初始化panel面板
	private void initPanelPage() {
		slideLayout = (SlideLayout) findViewById(R.id.slidelayout);
		openBtn = (Button) findViewById(R.id.openButton);
		rightButton = (ImageButton) findViewById(R.id.rightButton);

		trueTempTitle1 = (TextView) findViewById(R.id.trueTempTitle1);
		trueTempTitle2 = (TextView) findViewById(R.id.trueTempTitle2);
		trueTemp = (TextView) findViewById(R.id.trueTempValue);
		trueTemptag = (TextView) findViewById(R.id.trueTempTag);

		heatingTagLL = (LinearLayout) findViewById(R.id.heatingTagLL);
		heatingImg = (ImageView) findViewById(R.id.heatingTag);
		heatingTV = (TextView) findViewById(R.id.heatingTitle);
		heatTV = (TextView) findViewById(R.id.heatTitle);

		stateTag = (LinearLayout) findViewById(R.id.stateTag);
		stateTitleTV = (TextView) findViewById(R.id.stateTitle);
		stateTempValueTV = (TextView) findViewById(R.id.stateTempValue);
		stateTempTagTV = (TextView) findViewById(R.id.stateTempTag);
		hotwarterTitleTV = (TextView) findViewById(R.id.hotwarterTitle);
		hotwarterValueTV = (TextView) findViewById(R.id.hotwarterValue);
		hotwarterNoTV = (TextView) findViewById(R.id.hotwarterNoTitle);
		hotwarterProbar = (ProgressBar) findViewById(R.id.hotwarterBar);

		heatingTimeTitle = (TextView) findViewById(R.id.heatingTimeTitle);
		TextPaint paint1 = heatingTimeTitle.getPaint();
		paint1.setFakeBoldText(true);
		heatingTimeHourTV = (TextView) findViewById(R.id.heatingTimeHour);
		heatingTimeMinuteTV = (TextView) findViewById(R.id.heatingTimeMinute);
		heatingTimeHourTagTV = (TextView) findViewById(R.id.heatingTimeHourTag);
		heatingTimeMinuteTagTV = (TextView) findViewById(R.id.heatingTimeMinuteTag);

		reserveTitle = (TextView) findViewById(R.id.reserveTitle);
		TextPaint paint2 = reserveTitle.getPaint();
		paint2.setFakeBoldText(true);
		noReserveInfoTV = (TextView) findViewById(R.id.noReserveInfo);
		reserveInfoTV1 = (TextView) findViewById(R.id.reserve1Info);
		reserveInfoTV2 = (TextView) findViewById(R.id.reserve2Info);
		reserveInfoTV3 = (TextView) findViewById(R.id.reserve3Info);

		cloudTag = (ImageView) findViewById(R.id.cloudTag);
		cloudLoadProbar = (ProgressBar) findViewById(R.id.cloudProBar);
		cloudCityTempTV = (TextView) findViewById(R.id.cityTemp);
		cloudTempTV = (TextView) findViewById(R.id.cloudTemp);
		cloudCityTitleTV = (TextView) findViewById(R.id.cityTitle);
		TextPaint paint3 = cloudCityTitleTV.getPaint();
		paint3.setFakeBoldText(true);
		cloudTempTitleTV = (TextView) findViewById(R.id.cloudTempTitle);
		TextPaint paint4 = cloudTempTitleTV.getPaint();
		paint4.setFakeBoldText(true);

		cloudLL = (LinearLayout) findViewById(R.id.cloudLL);
		heatingTimeLL = (RelativeLayout) findViewById(R.id.heatingTimeLL);
		slip2LL = (LinearLayout) findViewById(R.id.slipLL2);
		cloudInfoLL = (RelativeLayout) findViewById(R.id.cloudInfoLL);
		reserveLL = (LinearLayout) findViewById(R.id.reserveLL);

		jsxLL = (LinearLayout) findViewById(R.id.panel_jsxLL);
		yyxLL = (LinearLayout) findViewById(R.id.panel_yyxLL);

		jsxSetTempUpImg = (Button) findViewById(R.id.jsxtemp_upBtn);
		jsxSetTempUpImg.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				jsxSetTempValueET.setVisibility(View.GONE);
				jsxSetTempValueTV.setVisibility(View.VISIBLE);
				jsxSetTempValueTV.setText(jsxSetTempValueET.getText());

				final int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					jsxSetTempUpImg.setBackgroundResource(R.drawable.up_act);
					downTime = System.currentTimeMillis();
					initTempUp();
					break;
				case MotionEvent.ACTION_UP:
					jsxSetTempUpImg.setBackgroundResource(R.drawable.up);
					upTime = System.currentTimeMillis();
					if (upTime - downTime < 500) {
						handlerForTempUp.removeCallbacks(runnableForTempUp);
						addTemp();
					} else {
						handlerForTempUp.removeCallbacks(runnableForTempUp);
					}
					break;
				}

				jsxSetTempValueET.setVisibility(View.GONE);
				jsxSetTempValueTV.setVisibility(View.VISIBLE);
				return true;
			}
		});

		jsxSetTempDownImg = (Button) findViewById(R.id.jsxtemp_downBtn);
		jsxSetTempDownImg.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				jsxSetTempValueET.setVisibility(View.GONE);
				jsxSetTempValueTV.setVisibility(View.VISIBLE);
				jsxSetTempValueTV.setText(jsxSetTempValueET.getText());

				final int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					jsxSetTempDownImg
							.setBackgroundResource(R.drawable.down_act);
					downTime = System.currentTimeMillis();
					initTempDown();
					break;
				case MotionEvent.ACTION_UP:
					jsxSetTempDownImg.setBackgroundResource(R.drawable.down);
					upTime = System.currentTimeMillis();

					if (upTime - downTime < 500) {
						handlerForTempDown.removeCallbacks(runnableForTempDown);
						subTemp();
					} else {
						handlerForTempDown.removeCallbacks(runnableForTempDown);
					}
					break;
				}

				jsxSetTempValueET.setVisibility(View.GONE);
				jsxSetTempValueTV.setVisibility(View.VISIBLE);
				return true;
			}
		});

		jsxSetTempSaveBtn = (TextView) findViewById(R.id.jsxtemp_savebtn);
		jsxSetTempSaveBtn.setOnClickListener(this);
		jsxSetTempValueTV = (TextView) findViewById(R.id.jsxtemp_tempvalue_TV);
		jsxSetTempValueTV.setOnClickListener(this);
		jsxSetTempValueET = (EditText) findViewById(R.id.jsxtemp_tempvalue_ET);
		// jsxSetTempValueET.setOnTouchListener(new OnTouchListener() {
		//
		// public boolean onTouch(View v, MotionEvent event) {
		// if (v.getId() == R.id.jsxtemp_tempvalue_ET) {
		// jsxSetTempValueET.selectAll();
		// InputMethodManager imm = (InputMethodManager) MainActivity.this
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.showSoftInput(v, 0);
		// return true;
		// }
		// return false;
		// }
		// });

		reserve1TV = (TextView) findViewById(R.id.yyxlist_TV1);
		reserve1TV.setOnClickListener(this);
		reserve2TV = (TextView) findViewById(R.id.yyxlist_TV2);
		reserve2TV.setOnClickListener(this);
		reserve3TV = (TextView) findViewById(R.id.yyxlist_TV3);
		reserve3TV.setOnClickListener(this);
		reserve1Img = (ImageView) findViewById(R.id.yyxlist_Img1);
		reserve1Img.setOnClickListener(this);
		reserve2Img = (ImageView) findViewById(R.id.yyxlist_Img2);
		reserve2Img.setOnClickListener(this);
		reserve3Img = (ImageView) findViewById(R.id.yyxlist_Img3);
		reserve3Img.setOnClickListener(this);
		reserve1ImgLL = (LinearLayout) findViewById(R.id.yyxlist_Img1LL);
		reserve1ImgLL.setOnClickListener(this);
		reserve2ImgLL = (LinearLayout) findViewById(R.id.yyxlist_Img2LL);
		reserve2ImgLL.setOnClickListener(this);
		reserve3ImgLL = (LinearLayout) findViewById(R.id.yyxlist_Img3LL);
		reserve3ImgLL.setOnClickListener(this);
		reserve1RL = (RelativeLayout) findViewById(R.id.yyxlist_RL1);
		reserve1RL.setOnClickListener(this);
		reserve2RL = (RelativeLayout) findViewById(R.id.yyxlist_RL2);
		reserve2RL.setOnClickListener(this);
		reserve3RL = (RelativeLayout) findViewById(R.id.yyxlist_RL3);
		reserve3RL.setOnClickListener(this);

		cloudBtn = (Button) findViewById(R.id.cloudBtn);
		jsxBtn = (Button) findViewById(R.id.jsxBtn);
		yyxBtn = (Button) findViewById(R.id.yyxBtn);
	}

	// 初始化设置页面
	private void initSetPage() {
		setFaceImg = (ImageView) findViewById(R.id.setFaceBtn);
		setLinkImg = (ImageView) findViewById(R.id.setLinkBtn);
		setVersionImg = (ImageView) findViewById(R.id.setVersionBtn);
		setStartImg = (ImageView) findViewById(R.id.setStartBtn);
		setUserImg = (ImageView) findViewById(R.id.setUserBtn);
		setRefreshImg = (ImageView) findViewById(R.id.setRefreshBtn);
		setHelpImg = (ImageView) findViewById(R.id.setHelpBtn);
		setErrImg = (ImageView) findViewById(R.id.setErrBtn);

		setFaceTV = (TextView) findViewById(R.id.setFaceTitle);
		setLinkTV = (TextView) findViewById(R.id.setLinkTitle);
		setVersionTV = (TextView) findViewById(R.id.setVersionTitle);
		setStartTV = (TextView) findViewById(R.id.setStartTitle);
		setUserTV = (TextView) findViewById(R.id.setUserTitle);
		setRefreshTV = (TextView) findViewById(R.id.setRefreshTitle);
		setHelpTV = (TextView) findViewById(R.id.setHelpTitle);
		setErrTV = (TextView) findViewById(R.id.setErrTitle);

		setFaceLL = (LinearLayout) findViewById(R.id.setFaceLL);
		setLinkLL = (LinearLayout) findViewById(R.id.setLinkLL);
		setVersionLL = (LinearLayout) findViewById(R.id.setVersionLL);
		setStartLL = (LinearLayout) findViewById(R.id.setStartLL);
		setUserLL = (LinearLayout) findViewById(R.id.setUserLL);
		setRefreshLL = (LinearLayout) findViewById(R.id.setRefreshLL);
		setHelpLL = (LinearLayout) findViewById(R.id.setHelpLL);
		setErrLL = (LinearLayout) findViewById(R.id.setErrLL);

		setFaceTag = (ImageView) findViewById(R.id.setFaceImg);
		setLinkTag = (ImageView) findViewById(R.id.setLinkImg);
		setVersionTag = (ImageView) findViewById(R.id.setVersionImg);
		setStartTag = (ImageView) findViewById(R.id.setStartImg);
		setUserTag = (ImageView) findViewById(R.id.setUserImg);
		setRefreshTag = (ImageView) findViewById(R.id.setRefreshImg);
		setHelpTag = (ImageView) findViewById(R.id.setHelpImg);
		setErrTag = (ImageView) findViewById(R.id.setErrImg);

		setFaceImg.setOnClickListener(this);
		setLinkImg.setOnClickListener(this);
		setVersionImg.setOnClickListener(this);
		setStartImg.setOnClickListener(this);
		setUserImg.setOnClickListener(this);
		setRefreshImg.setOnClickListener(this);
		setHelpImg.setOnClickListener(this);
		setErrImg.setOnClickListener(this);

		setFaceTV.setOnClickListener(this);
		setLinkTV.setOnClickListener(this);
		setVersionTV.setOnClickListener(this);
		setStartTV.setOnClickListener(this);
		setUserTV.setOnClickListener(this);
		setRefreshTV.setOnClickListener(this);
		setHelpTV.setOnClickListener(this);
		setErrTV.setOnClickListener(this);

		setFaceLL.setOnClickListener(this);
		setLinkLL.setOnClickListener(this);
		setVersionLL.setOnClickListener(this);
		setStartLL.setOnClickListener(this);
		setUserLL.setOnClickListener(this);
		setRefreshLL.setOnClickListener(this);
		setHelpLL.setOnClickListener(this);
		setErrLL.setOnClickListener(this);

		setFaceTag.setOnClickListener(this);
		setLinkTag.setOnClickListener(this);
		setVersionTag.setOnClickListener(this);
		setStartTag.setOnClickListener(this);
		setUserTag.setOnClickListener(this);
		setRefreshTag.setOnClickListener(this);
		setHelpTag.setOnClickListener(this);
		setErrTag.setOnClickListener(this);

		if (LoadData.isWifi) {
			setVersionImg.setOnClickListener(null);
			setVersionLL.setOnClickListener(null);
			setVersionTag.setOnClickListener(null);
			setVersionTV.setOnClickListener(null);
			setVersionTV.setTextColor(closeColor);
			setVersionImg.setVisibility(View.INVISIBLE);
			setUserImg.setOnClickListener(null);
			setUserLL.setOnClickListener(null);
			setUserTag.setOnClickListener(null);
			setUserTV.setOnClickListener(null);
			setUserTV.setTextColor(closeColor);
			setUserImg.setVisibility(View.INVISIBLE);
		}
	}

	// 初始化预约洗值
	public void initReserveInfo() {
		String reserveStr1 = null;
		String reserveStr2 = null;
		String reserveStr3 = null;

		String reserve1 = myApp.getReserve1Mark();
		String open1 = reserve1.substring(0, 2);
		String temp1 = reserve1.substring(2, 4);
		String hour1 = reserve1.substring(4, 6);
		String minute1 = reserve1.substring(6, 8);
		String week16 = reserve1.substring(9, 10);
		String week15 = reserve1.substring(10, 11);
		String week14 = reserve1.substring(11, 12);
		String week13 = reserve1.substring(12, 13);
		String week12 = reserve1.substring(13, 14);
		String week11 = reserve1.substring(14, 15);
		String week17 = reserve1.substring(15, 16);
		String week1 = "周" + (week17.equals("1") ? "日" : "")
				+ (week11.equals("1") ? "一" : "")
				+ (week12.equals("1") ? "二" : "")
				+ (week13.equals("1") ? "三" : "")
				+ (week14.equals("1") ? "四" : "")
				+ (week15.equals("1") ? "五" : "")
				+ (week16.equals("1") ? "六" : "");
		if (week1.equals("周日一二三四五六")) {
			week1 = "每天";
		} else if (week1.equals("周")) {
			week1 = "";
		}
		if (open1.toLowerCase().equals("ff")) {
			reserveStr1 = "预约一    " + temp1 + "°C  " + hour1 + ":" + minute1
					+ "  " + week1;
			reserve1Img.setImageResource(R.drawable.radbtn_sel);

			// 保存比对数据
			String[] weekStr1 = new String[7];
			if (week17.equals("1")) {
				weekStr1[6] = "日";
			}
			if (week16.equals("1")) {
				weekStr1[5] = "六";
			}
			if (week15.equals("1")) {
				weekStr1[4] = "五";
			}
			if (week14.equals("1")) {
				weekStr1[3] = "四";
			}
			if (week13.equals("1")) {
				weekStr1[2] = "三";
			}
			if (week12.equals("1")) {
				weekStr1[1] = "二";
			}
			if (week11.equals("1")) {
				weekStr1[0] = "一";
			}
			final Map<String, Object> addMap1 = new HashMap<String, Object>();
			addMap1.put("tempStr", temp1);
			addMap1.put("hourStr", hour1);
			addMap1.put("minuteStr", minute1);
			addMap1.put("weekStr", weekStr1);
			myApp.getReserveList().set(0, addMap1);
		}

		String reserve2 = myApp.getReserve2Mark();
		String open2 = reserve2.substring(0, 2);
		String temp2 = reserve2.substring(2, 4);
		String hour2 = reserve2.substring(4, 6);
		String minute2 = reserve2.substring(6, 8);
		String week26 = reserve2.substring(9, 10);
		String week25 = reserve2.substring(10, 11);
		String week24 = reserve2.substring(11, 12);
		String week23 = reserve2.substring(12, 13);
		String week22 = reserve2.substring(13, 14);
		String week21 = reserve2.substring(14, 15);
		String week27 = reserve2.substring(15, 16);
		String week2 = "周" + (week27.equals("1") ? "日" : "")
				+ (week21.equals("1") ? "一" : "")
				+ (week22.equals("1") ? "二" : "")
				+ (week23.equals("1") ? "三" : "")
				+ (week24.equals("1") ? "四" : "")
				+ (week25.equals("1") ? "五" : "")
				+ (week26.equals("1") ? "六" : "");
		if (week2.equals("周日一二三四五六")) {
			week2 = "每天";
		} else if (week2.equals("周")) {
			week2 = "";
		}
		if (open2.toLowerCase().equals("ff")) {
			reserveStr2 = "预约二    " + temp2 + "°C  " + hour2 + ":" + minute2
					+ "  " + week2;
			reserve2Img.setImageResource(R.drawable.radbtn_sel);

			// 保存比对数据
			String[] weekStr2 = new String[7];
			if (week27.equals("1")) {
				weekStr2[6] = "日";
			}
			if (week26.equals("1")) {
				weekStr2[5] = "六";
			}
			if (week25.equals("1")) {
				weekStr2[4] = "五";
			}
			if (week24.equals("1")) {
				weekStr2[3] = "四";
			}
			if (week23.equals("1")) {
				weekStr2[2] = "三";
			}
			if (week22.equals("1")) {
				weekStr2[1] = "二";
			}
			if (week21.equals("1")) {
				weekStr2[0] = "一";
			}
			final Map<String, Object> addMap2 = new HashMap<String, Object>();
			addMap2.put("tempStr", temp2);
			addMap2.put("hourStr", hour2);
			addMap2.put("minuteStr", minute2);
			addMap2.put("weekStr", weekStr2);
			myApp.getReserveList().set(1, addMap2);
		}

		String reserve3 = myApp.getReserve3Mark();
		String open3 = reserve3.substring(0, 2);
		String temp3 = reserve3.substring(2, 4);
		String hour3 = reserve3.substring(4, 6);
		String minute3 = reserve3.substring(6, 8);
		String week36 = reserve3.substring(9, 10);
		String week35 = reserve3.substring(10, 11);
		String week34 = reserve3.substring(11, 12);
		String week33 = reserve3.substring(12, 13);
		String week32 = reserve3.substring(13, 14);
		String week31 = reserve3.substring(14, 15);
		String week37 = reserve3.substring(15, 16);
		String week3 = "周" + (week37.equals("1") ? "日" : "")
				+ (week31.equals("1") ? "一" : "")
				+ (week32.equals("1") ? "二" : "")
				+ (week33.equals("1") ? "三" : "")
				+ (week34.equals("1") ? "四" : "")
				+ (week35.equals("1") ? "五" : "")
				+ (week36.equals("1") ? "六" : "");
		if (week3.equals("周日一二三四五六")) {
			week3 = "每天";
		} else if (week3.equals("周")) {
			week3 = "";
		}
		if (open3.toLowerCase().equals("ff")) {
			reserveStr3 = "预约三    " + temp3 + "°C  " + hour3 + ":" + minute3
					+ "  " + week3;
			reserve3Img.setImageResource(R.drawable.radbtn_sel);

			// 保存比对数据
			String[] weekStr3 = new String[7];
			if (week37.equals("1")) {
				weekStr3[6] = "日";
			}
			if (week36.equals("1")) {
				weekStr3[5] = "六";
			}
			if (week35.equals("1")) {
				weekStr3[4] = "五";
			}
			if (week34.equals("1")) {
				weekStr3[3] = "四";
			}
			if (week33.equals("1")) {
				weekStr3[2] = "三";
			}
			if (week32.equals("1")) {
				weekStr3[1] = "二";
			}
			if (week31.equals("1")) {
				weekStr3[0] = "一";
			}
			final Map<String, Object> addMap3 = new HashMap<String, Object>();
			addMap3.put("tempStr", temp3);
			addMap3.put("hourStr", hour3);
			addMap3.put("minuteStr", minute3);
			addMap3.put("weekStr", weekStr3);
			myApp.getReserveList().set(2, addMap3);
		}

		updateReserveInfo(reserveStr1, reserveStr2, reserveStr3);
	}

	// 设置panel关闭开启状态
	private void setPanelClose() {
		updateOpenBtn();
		updateOpenSetPageBtn();
		updateTrueTemp(true);
		updateHeatingTag();
		updateStateInfo();
		updateHotWarter();
		updateHotTime(false);
		updateReserveInfo(null, null, null);
		updateOpeBtn();
	}

	// 初始化即时洗温度up设置
	private void initTempUp() {
		handlerForTempUp = new Handler();
		runnableForTempUp = new Runnable() {
			public void run() {
				addTemp();
				handlerForTempUp.postDelayed(this, 200);
			}
		};
		handlerForTempUp.postDelayed(runnableForTempUp, 500);
	}

	// 初始化即时洗温度down设置
	private void initTempDown() {
		handlerForTempDown = new Handler();
		runnableForTempDown = new Runnable() {
			public void run() {
				subTemp();
				handlerForTempDown.postDelayed(this, 200);
			}
		};
		handlerForTempDown.postDelayed(runnableForTempDown, 500);
	}

	// 增加即时洗温度操作
	private void addTemp() {
		String curTempValue1 = jsxSetTempValueTV.getText().toString();
		int newTempValue1 = Integer.parseInt(curTempValue1) + 5;
		if (newTempValue1 > 75) {
			newTempValue1 = 30;
		}

		jsxSetTempValueTV.setText(newTempValue1 + "");
		jsxSetTempValueET.setText(newTempValue1 + "");
	}

	// 降低即时洗温度操作
	private void subTemp() {
		String curTempValue2 = jsxSetTempValueTV.getText().toString();
		int newTempValue2 = Integer.parseInt(curTempValue2) - 5;
		if (newTempValue2 < 30) {
			newTempValue2 = 75;
		}

		jsxSetTempValueTV.setText(newTempValue2 + "");
		jsxSetTempValueET.setText(newTempValue2 + "");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case (R.id.openButton):
			if (cloudLL.getVisibility() == View.VISIBLE || isLoadData) {
				return;
			}

			if (isErr) {
				MyTool.makeText(MainActivity.this, "热水器故障，暂无法开机");
				return;
			}

			if (isClose) {
				setOpen();
			} else {
				setClose();
			}
			break;
		case (R.id.rightButton):
			setSetPageShow();

			if (isJSXLLOpen) {
				jsxLL.setVisibility(View.INVISIBLE);
				isJSXLLOpen = false;
			} else if (isYYXLLOpen) {
				yyxLL.setVisibility(View.INVISIBLE);
				isYYXLLOpen = false;
			}
			updateOpeBtn();
			break;
		case (R.id.cloudBtn):
			if (myApp.getCurServerCity() == null
					|| myApp.getCurServerCity().equals("")) {
				MyTool.openSureAD(MainActivity.this, "提示",
						"云智能不可用！该功能需要热水器绑定云平台，并且需要手机可访问互联网，请检查...");
				return;
			}
			if (myApp.getCurServerCloudTemp() == null
					|| myApp.getCurServerCloudTemp().equals("")) {
				MyTool.openSureAD(MainActivity.this, "提示",
						"云智能不可用！获取当前天气失败，请检查网络是否可用...");
				return;
			}
			if (cloudLL.getVisibility() == View.VISIBLE || isLoadData) {
				return;
			}

			updateOpeBtn();

			reserveLL.setVisibility(View.GONE);
			heatingTimeLL.setVisibility(View.GONE);
			slip2LL.setVisibility(View.GONE);
			cloudInfoLL.setVisibility(View.GONE);
			cloudLL.setVisibility(View.VISIBLE);
			jsxLL.setVisibility(View.INVISIBLE);
			yyxLL.setVisibility(View.INVISIBLE);
			cloudTag.setVisibility(View.VISIBLE);
			setPanelState("cloudLoad");

			isJSXLLOpen = false;
			isYYXLLOpen = false;

			openCloudLoadTimer();
			openCloudLoadProbar();
			break;
		case (R.id.jsxBtn):
			if (cloudLL.getVisibility() == View.VISIBLE || isLoadData) {
				return;
			}

			updateOpeBtn();

			jsxLL.setVisibility(View.VISIBLE);
			jsxLL.getChildAt(1).setVisibility(View.GONE);
			ScrollView popJSXList = (ScrollView) jsxLL.getChildAt(0);
			popJSXList.getBackground().setLevel(myApp.getFaceLevel());
			RelativeLayout popJSXSettempHead = (RelativeLayout) ((LinearLayout) jsxLL
					.getChildAt(1)).getChildAt(0);
			popJSXSettempHead.getBackground().setLevel(myApp.getFaceLevel());
			yyxLL.setVisibility(View.INVISIBLE);
			jsxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
			jsxBtn.getBackground().setLevel(myApp.getFaceLevel());

			isJSXLLOpen = true;
			isYYXLLOpen = false;
			break;
		case (R.id.yyxBtn):
			if (cloudLL.getVisibility() == View.VISIBLE || isLoadData) {
				return;
			}

			updateOpeBtn();

			jsxLL.setVisibility(View.INVISIBLE);
			yyxLL.setVisibility(View.VISIBLE);
			LinearLayout popYYXList = (LinearLayout) yyxLL.getChildAt(0);
			popYYXList.getBackground().setLevel(myApp.getFaceLevel());
			yyxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
			yyxBtn.getBackground().setLevel(myApp.getFaceLevel());

			isJSXLLOpen = false;
			isYYXLLOpen = true;
			break;
		case (R.id.yyxlist_TV1):
		case (R.id.yyxlist_RL1):
			myApp.setCurReserveSet("1");
			openReserveActivity();
			break;
		case (R.id.yyxlist_TV2):
		case (R.id.yyxlist_RL2):
			myApp.setCurReserveSet("2");
			openReserveActivity();
			break;
		case (R.id.yyxlist_TV3):
		case (R.id.yyxlist_RL3):
			myApp.setCurReserveSet("3");
			openReserveActivity();
			break;
		case (R.id.yyxlist_Img1):
		case (R.id.yyxlist_Img1LL):
			String reserve1 = myApp.getReserve1Mark();
			String open1 = reserve1.substring(0, 2);

			if (open1.toLowerCase().equals("ff")) {
				setReserve("1", "0075140000000000");
			} else {
				myApp.setCurReserveSet("1");
				openReserveActivity();
			}
			break;
		case (R.id.yyxlist_Img2):
		case (R.id.yyxlist_Img2LL):
			String reserve2 = myApp.getReserve2Mark();
			String open2 = reserve2.substring(0, 2);

			if (open2.toLowerCase().equals("ff")) {
				setReserve("2", "0075080000000000");
			} else {
				myApp.setCurReserveSet("2");
				openReserveActivity();
			}
			break;
		case (R.id.yyxlist_Img3):
		case (R.id.yyxlist_Img3LL):
			String reserve3 = myApp.getReserve3Mark();
			String open3 = reserve3.substring(0, 2);

			if (open3.toLowerCase().equals("ff")) {
				setReserve("3", "0075140000000000");
			} else {
				myApp.setCurReserveSet("3");
				openReserveActivity();
			}
			break;
		// case (R.id.jsxtemp_tempvalue_TV):
		// jsxSetTempValueET.setVisibility(View.VISIBLE);
		// jsxSetTempValueTV.setVisibility(View.GONE);
		// break;
		case (R.id.jsxtemp_savebtn):
			myApp.setTempMark(jsxSetTempValueTV.getText().toString());
			myApp.setCurModeName("自设温度");

			callSetTempName = "jsxSet";
			setMode("自设温度");

			break;
		case (R.id.setFaceBtn):
		case (R.id.setFaceImg):
		case (R.id.setFaceTitle):
		case (R.id.setFaceLL):
			intent = new Intent();
			intent.setClass(MainActivity.this, SetFaceActivity.class);
			startActivity(intent);

			break;
		case (R.id.setLinkBtn):
		case (R.id.setLinkImg):
		case (R.id.setLinkTitle):
		case (R.id.setLinkLL):
			intent = new Intent();
			intent.setClass(MainActivity.this, SetLinkActivity.class);
			startActivity(intent);

			break;
		case (R.id.setVersionBtn):
		case (R.id.setVersionImg):
		case (R.id.setVersionTitle):
		case (R.id.setVersionLL):
			intent = new Intent();
			intent.setClass(MainActivity.this, SetVersionActivity.class);
			startActivity(intent);

			break;
		case (R.id.setStartBtn):
		case (R.id.setStartImg):
		case (R.id.setStartTitle):
		case (R.id.setStartLL):
			intent = new Intent();
			intent.setClass(MainActivity.this, SetStartActivity.class);
			startActivity(intent);

			break;
		case (R.id.setRefreshBtn):
		case (R.id.setRefreshImg):
		case (R.id.setRefreshTitle):
		case (R.id.setRefreshLL):
			intent = new Intent();
			intent.setClass(MainActivity.this, SetRefreshActivity.class);
			startActivity(intent);

			break;
		case (R.id.setHelpBtn):
		case (R.id.setHelpImg):
		case (R.id.setHelpTitle):
		case (R.id.setHelpLL):
			intent = new Intent();
			intent.setClass(MainActivity.this, SetHelpActivity.class);
			startActivity(intent);

			break;
		case (R.id.setErrBtn):
		case (R.id.setErrImg):
		case (R.id.setErrTitle):
		case (R.id.setErrLL):
		case (R.id.trueTempValue):
			intent = new Intent();
			intent.setClass(MainActivity.this, SetErrActivity.class);
			startActivity(intent);

			break;
		case (R.id.setUserBtn):
		case (R.id.setUserImg):
		case (R.id.setUserTitle):
		case (R.id.setUserLL):
			intent = new Intent();
			intent.setClass(MainActivity.this, SetUserActivity.class);
			startActivity(intent);

			break;
		}
	}

	// 打开预约洗设置页面
	private void openReserveActivity() {
		if (isJSXLLOpen) {
			jsxLL.setVisibility(View.INVISIBLE);
			isJSXLLOpen = false;

			if (isJSXBtnOpen) {
				jsxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
				jsxBtn.getBackground().setLevel(myApp.getFaceLevel());
			} else {
				jsxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			}
		} else if (isYYXLLOpen) {
			yyxLL.setVisibility(View.INVISIBLE);
			isYYXLLOpen = false;

			if (isYYXBtnOpen) {
				yyxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
				yyxBtn.getBackground().setLevel(myApp.getFaceLevel());
			} else {
				yyxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			}
		}

		intent = new Intent();
		intent.setClass(MainActivity.this, ReserveSetActivity.class);
		startActivity(intent);
	}

	// 封装即时洗列表
	public void setJSXList() {
		jsxListLL = (LinearLayout) ((ScrollView) jsxLL.getChildAt(0))
				.getChildAt(0);
		jsxListLL.removeAllViews();
		String curMode = myApp.getCurModeName();
		boolean hasTop = false;

		char[] mode1CArr = myApp.getMode1Mark().toCharArray();
		char[] mode2CArr = myApp.getMode2Mark().toCharArray();
		char[] mode3CArr = myApp.getMode3Mark().toCharArray();
		char[] mode4CArr = myApp.getMode4Mark().toCharArray();
		if (mode3CArr[7] == '1') {
			jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
					"1人洗", curMode.equals("1人洗") ? true : false));
			if (!hasTop) {
				hasTop = true;
			}
		}
		if (mode3CArr[6] == '1') {
			jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
					"2人洗", curMode.equals("2人洗") ? true : false));
			if (!hasTop) {
				hasTop = true;
			}
		}
		if (mode3CArr[5] == '1') {
			jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
					"3人洗", curMode.equals("3人洗") ? true : false));
			if (!hasTop) {
				hasTop = true;
			}
		}
		if (mode1CArr[6] == '1') {
			jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
					"生活用水", curMode.equals("生活用水") ? true : false));
			if (!hasTop) {
				hasTop = true;
			}
		}
		if (mode1CArr[7] == '1') {
			jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
					"E+增容", curMode.equals("E+增容") ? true : false));
			if (!hasTop) {
				hasTop = true;
			}
		}
		if (mode2CArr[7] == '1') {
			jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
					"节能模式", curMode.equals("节能模式") ? true : false));
			if (!hasTop) {
				hasTop = true;
			}
		}
		if (mode2CArr[6] == '1') {
			jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
					"峰谷夜电", curMode.equals("峰谷夜电") ? true : false));
			if (!hasTop) {
				hasTop = true;
			}
		}
		if (mode1CArr[2] == '1') {
			jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
					"冬季模式", curMode.equals("冬季模式") ? true : false));
			if (!hasTop) {
				hasTop = true;
			}
		}
		if (mode1CArr[3] == '1') {
			jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
					"夏季模式", curMode.equals("夏季模式") ? true : false));
			if (!hasTop) {
				hasTop = true;
			}
		}
		// if (mode1CArr[4] == '1') {
		// jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
		// "整胆速热", curMode.equals("整胆速热") ? true : false));
		// if (!hasTop) {
		// hasTop = true;
		// }
		// }
		// if (mode1CArr[5] == '1') {
		// jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
		// "半胆速热", curMode.equals("半胆速热") ? true : false));
		// if (!hasTop) {
		// hasTop = true;
		// }
		// }
		if (mode3CArr[2] == '1') {
			jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
					"成人洗", curMode.equals("成人洗") ? true : false));
			if (!hasTop) {
				hasTop = true;
			}
		}
		if (mode3CArr[3] == '1') {
			jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
					"儿童洗", curMode.equals("儿童洗") ? true : false));
			if (!hasTop) {
				hasTop = true;
			}
		}
		if (mode3CArr[4] == '1') {
			jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
					"老人洗", curMode.equals("老人洗") ? true : false));
			if (!hasTop) {
				hasTop = true;
			}
		}
		if (mode4CArr[5] == '1') {
			jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
					"智能记忆", curMode.equals("智能记忆") ? true : false));
			if (!hasTop) {
				hasTop = true;
			}
		}
		// if (mode4CArr[6] == '1') {
		// jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
		// "智洁排水", curMode.equals("智洁排水") ? true : false));
		// if (!hasTop) {
		// hasTop = true;
		// }
		// }
		// if (mode4CArr[7] == '1') {
		// jsxListLL.addView(setJSXTabItem(hasTop ? "middle" : "top", "sel",
		// "暖风", curMode.equals("暖风") ? true : false));
		// if (!hasTop) {
		// hasTop = true;
		// }
		// }
		if (hasTop) {
			jsxListLL.addView(setJSXTabItem("bottom", "open", "自设温度", false));
		} else {
			jsxListLL.addView(setJSXTabItem("middle", "open", "自设温度", false));
		}
	}

	// 封装即时洗列表表格
	private RelativeLayout setJSXTabItem(String type, String btnType,
			String title, boolean isSel) {
		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layout.setGravity(Gravity.CENTER_VERTICAL);
		layout.setLayoutParams(params);
		if (type.equals("top")) {
			layout.setBackgroundResource(R.drawable.shape_table_top);
			layout.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					ImageView imgV = (ImageView) ((RelativeLayout) v)
							.getChildAt(1);
					imgV.setImageResource(R.drawable.radbtn_sel);

					TextView tv = (TextView) ((RelativeLayout) v).getChildAt(0);
					String curMode = tv.getText().toString();

					LinearLayout totalLL = (LinearLayout) v.getParent();
					if (totalLL == null)
						return;
					int c = totalLL.getChildCount() - 1;
					for (int i = 0; i < c; i++) {
						RelativeLayout rV = (RelativeLayout) totalLL
								.getChildAt(i);
						if (rV == v) {
							continue;
						} else {
							ImageView imgView = (ImageView) ((RelativeLayout) totalLL
									.getChildAt(i)).getChildAt(1);
							imgView.setImageResource(R.drawable.radbtn_nosel);
						}
					}

					jsxModeToSetTemp(curMode);
				}
			});
		} else if (type.equals("middle")) {
			layout.setBackgroundResource(R.drawable.shape_table_middle);
			layout.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					ImageView imgV = (ImageView) ((RelativeLayout) v)
							.getChildAt(1);
					imgV.setImageResource(R.drawable.radbtn_sel);

					TextView tv = (TextView) ((RelativeLayout) v).getChildAt(0);
					String curMode = tv.getText().toString();

					LinearLayout totalLL = (LinearLayout) v.getParent();
					if (totalLL == null)
						return;
					int c = totalLL.getChildCount() - 1;
					for (int i = 0; i < c; i++) {
						RelativeLayout rV = (RelativeLayout) totalLL
								.getChildAt(i);
						if (rV == v) {
							continue;
						} else {
							ImageView imgView = (ImageView) ((RelativeLayout) totalLL
									.getChildAt(i)).getChildAt(1);
							imgView.setImageResource(R.drawable.radbtn_nosel);
						}
					}

					jsxModeToSetTemp(curMode);
				}
			});
		} else if (type.equals("bottom")) {
			layout.setBackgroundResource(R.drawable.shape_table_bottom);
			layout.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					jsxLL.getChildAt(1).setVisibility(View.VISIBLE);
					jsxSetTempValueTV.setText(myApp.getTempMark());
					jsxSetTempValueET.setText(myApp.getTempMark());
				}
			});
		}

		TextView textView = new TextView(this);
		// textView.setShadowLayer(3, 2, 3, shadowColor);
		textView.setText(title);
		textView.setTextColor(heatTagOpenColor);
		textView.setTextSize(20);
		if (title.equals("自设温度")) {
			textView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					jsxLL.getChildAt(1).setVisibility(View.VISIBLE);
					jsxSetTempValueTV.setText(myApp.getTempMark());
					jsxSetTempValueET.setText(myApp.getTempMark());
				}
			});
		} else {
			textView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					ImageView imgV = (ImageView) ((RelativeLayout) v
							.getParent()).getChildAt(1);
					imgV.setImageResource(R.drawable.radbtn_sel);

					String curMode = ((TextView) v).getText().toString();

					LinearLayout totalLL = (LinearLayout) v.getParent()
							.getParent();
					if (totalLL == null)
						return;
					int c = totalLL.getChildCount() - 1;
					for (int i = 0; i < c; i++) {
						TextView textV = (TextView) ((RelativeLayout) totalLL
								.getChildAt(i)).getChildAt(0);
						if (textV == v) {
							continue;
						} else {
							ImageView imgView = (ImageView) ((RelativeLayout) totalLL
									.getChildAt(i)).getChildAt(1);
							imgView.setImageResource(R.drawable.radbtn_nosel);
						}
					}

					jsxModeToSetTemp(curMode);
				}
			});
		}

		ImageView imgView = new ImageView(this);
		imgView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		if (btnType.equals("sel")) {
			if (isSel) {
				imgView.setImageResource(R.drawable.radbtn_sel);
			} else {
				imgView.setImageResource(R.drawable.radbtn_nosel);
			}
			imgView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					((ImageView) v).setImageResource(R.drawable.radbtn_sel);

					TextView tv = (TextView) ((RelativeLayout) v.getParent())
							.getChildAt(0);
					String curMode = tv.getText().toString();

					LinearLayout totalLL = (LinearLayout) v.getParent()
							.getParent();
					if (totalLL == null)
						return;
					int c = totalLL.getChildCount() - 1;
					for (int i = 0; i < c; i++) {
						ImageView imgView = (ImageView) ((RelativeLayout) totalLL
								.getChildAt(i)).getChildAt(1);
						if (imgView == v) {
							continue;
						} else {
							imgView.setImageResource(R.drawable.radbtn_nosel);
						}
					}

					jsxModeToSetTemp(curMode);
				}
			});
		} else if (btnType.equals("open")) {
			imgView.setImageResource(R.drawable.v5_3_0_profile_arrow_back);
			imgView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					jsxLL.getChildAt(1).setVisibility(View.VISIBLE);
				}
			});
		}

		layout.addView(textView);
		layout.addView(imgView);

		android.widget.RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) textView
				.getLayoutParams();
		textParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		textParams.addRule(RelativeLayout.CENTER_VERTICAL);
		textView.setLayoutParams(textParams);

		android.widget.RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) imgView
				.getLayoutParams();
		imgParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		imgParams.addRule(RelativeLayout.CENTER_VERTICAL);
		imgView.setLayoutParams(imgParams);

		return layout;
	}

	// 点击即时洗模式，设置热水器温度
	private void jsxModeToSetTemp(String curMode) {
		callSetTempName = "jsxMode";
		setMode(curMode);
	}

	private void setMode(String name) {
		boolean isSrSetBtn1Sel = false;// E+增容
		boolean isSrSetBtn2Sel = false;// 生活用水
		boolean isSrSetBtn3Sel = false;// 半胆速热
		boolean isSrSetBtn4Sel = false;// 整胆速热
		boolean isSrSetBtn5Sel = false;// 夏季模式
		boolean isSrSetBtn6Sel = false;// 冬季模式
		boolean isFrSetBtn1Sel = false;// 1人洗
		boolean isFrSetBtn2Sel = false;// 2人洗
		boolean isFrSetBtn3Sel = false;// 3人洗
		boolean isFrSetBtn4Sel = false;// 老人洗
		boolean isFrSetBtn5Sel = false;// 成人洗
		boolean isFrSetBtn6Sel = false;// 儿童洗
		boolean isJnSetBtn1Sel = false;// 节能模式
		boolean isJnSetBtn2Sel = false;// 峰谷夜电
		// boolean isJkSetBtn1Sel = false;// 暖风
		// boolean isJkSetBtn2Sel = false;// 智洁排水
		// boolean isJkSetBtn3Sel = false;// 智能记忆
		boolean isCloudeSel = false;// 云智能
		boolean isSetTempSel = false;// 自设温度

		curSetModeCName = name;
		if (curSetModeCName.equals("云智能")) {
			isCloudeSel = true;
		} else if (curSetModeCName.equals("自设温度")) {
			isSetTempSel = true;
		} else {
			if (curSetModeCName.equals("峰谷夜电")) {
				isJnSetBtn2Sel = true;
			} else if (curSetModeCName.equals("节能")) {
				isJnSetBtn1Sel = true;
			} else if (curSetModeCName.equals("E+增容")) {
				isSrSetBtn1Sel = true;
			} else if (curSetModeCName.equals("生活用水")) {
				isSrSetBtn2Sel = true;
			} else if (curSetModeCName.equals("半胆速热")) {
				isSrSetBtn3Sel = true;
			} else if (curSetModeCName.equals("整胆速热")) {
				isSrSetBtn4Sel = true;
			} else if (curSetModeCName.equals("夏季模式")) {
				isSrSetBtn5Sel = true;
			} else if (curSetModeCName.equals("冬季模式")) {
				isSrSetBtn6Sel = true;
			} else if (curSetModeCName.equals("1人洗")) {
				isFrSetBtn1Sel = true;
			} else if (curSetModeCName.equals("2人洗")) {
				isFrSetBtn2Sel = true;
			} else if (curSetModeCName.equals("3人洗")) {
				isFrSetBtn3Sel = true;
			} else if (curSetModeCName.equals("老人洗")) {
				isFrSetBtn4Sel = true;
			} else if (curSetModeCName.equals("儿童洗")) {
				isFrSetBtn5Sel = true;
			} else if (curSetModeCName.equals("成人洗")) {
				isFrSetBtn6Sel = true;
			}
			curSetModeCName = curSetModeCName + "洗浴模式";
		}

		String cmd1Str = "00";// 预留00
		cmd1Str = cmd1Str + (isJnSetBtn2Sel ? "1" : "0");
		cmd1Str = cmd1Str + (isJnSetBtn1Sel ? "1" : "0");
		cmd1Str = cmd1Str + (isSrSetBtn6Sel ? "1" : "0");
		cmd1Str = cmd1Str + (isSrSetBtn5Sel ? "1" : "0");
		cmd1Str = cmd1Str + (isSrSetBtn2Sel ? "1" : "0");
		cmd1Str = cmd1Str + (isSrSetBtn1Sel ? "1" : "0");

		String cmd2Str = isSetTempSel ? "1" : "0";
		cmd2Str = cmd2Str + (isCloudeSel ? "1" : "0");
		cmd2Str = cmd2Str + (isFrSetBtn6Sel ? "1" : "0");
		cmd2Str = cmd2Str + (isFrSetBtn5Sel ? "1" : "0");
		cmd2Str = cmd2Str + (isFrSetBtn4Sel ? "1" : "0");
		cmd2Str = cmd2Str + (isFrSetBtn3Sel ? "1" : "0");
		cmd2Str = cmd2Str + (isFrSetBtn2Sel ? "1" : "0");
		cmd2Str = cmd2Str + (isFrSetBtn1Sel ? "1" : "0");

		String cmd3Str = "000000";// 预留00
		cmd3Str = cmd3Str + (isSrSetBtn4Sel ? "1" : "0");
		cmd3Str = cmd3Str + (isSrSetBtn3Sel ? "1" : "0");

		String tempStr = null;
		if (isCloudeSel || isSetTempSel) {
			tempStr = myApp.getTempMark();
		}

		myApp.setMsModeMark(cmd1Str + cmd2Str + cmd3Str);

		final String cmdMode = cmd1Str + cmd2Str + cmd3Str;
		final String cmdTemp = tempStr;

		setModeCmd(cmdMode, cmdTemp, curSetModeCName);
	}

	// 打开云智能加载定时器
	private void openCloudLoadTimer() {
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			public void run() {
				handler.removeCallbacks(this);

				// 要做的事情
				myApp.setTempMark(myApp.getCurServerCloudTemp());
				cloudTempTV.setText(myApp.getTempMark());
				callSetTempName = "cloud";
				setMode("云智能");
			}
		};
		handler.postDelayed(runnable, 2000);
	}

	// 打开云智能加载进度条
	private void openCloudLoadProbar() {
		final int TIME = 100;
		final int PRO = 5;

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			int cTime = 0;
			int cPro = 0;

			@Override
			public void run() {
				cTime = cTime + TIME;
				cPro = cPro + PRO;

				// 要做的事情
				cloudLoadProbar.setProgress(cPro);

				if (cTime > 2000) {
					cancel();
				}
			}
		}, 0, TIME);
	}

	// 打开模拟加热定时器
	// private void openTempChangeTimer() {
	// if (handlerForDemoTemp != null) {
	// handlerForDemoTemp.removeCallbacks(runnableForDemoTemp);
	// handlerForDemoTemp = null;
	// if (runnableForDemoTemp != null)
	// runnableForDemoTemp = null;
	// }
	//
	// handlerForDemoTemp = new Handler();
	// runnableForDemoTemp = new Runnable() {
	// public void run() {
	// int curTemp = Integer.parseInt(trueTemp.getText().toString());
	// int setTemp = Integer.parseInt(stateTempValueTV.getText()
	// .toString());
	//
	// // 要做的事情
	// if (curTemp < setTemp) {
	// trueTemp.setText(String.valueOf(curTemp + 1));
	//
	// int time = setTemp - curTemp--;
	//
	// if (time < 60) {
	// heatingTimeHourTV.setText("00");
	// heatingTimeMinuteTV.setText((time < 10) ? "0"
	// + String.valueOf(time) : String.valueOf(time));
	// } else {
	// heatingTimeHourTV.setText("01");
	// heatingTimeMinuteTV.setText((time < 10) ? "0"
	// + String.valueOf(time - 60) : String
	// .valueOf(time - 60));
	// }
	//
	// handlerForDemoTemp.postDelayed(this, 1000);
	// } else if (curTemp > setTemp) {
	// trueTemp.setText(String.valueOf(curTemp - 1));
	//
	// int time = curTemp++ - setTemp;
	//
	// if (time < 60) {
	// heatingTimeHourTV.setText("00");
	// heatingTimeMinuteTV.setText((time < 10) ? "0"
	// + String.valueOf(time) : String.valueOf(time));
	// } else {
	// heatingTimeHourTV.setText("01");
	// heatingTimeMinuteTV.setText((time < 10) ? "0"
	// + String.valueOf(time - 60) : String
	// .valueOf(time - 60));
	// }
	//
	// handlerForDemoTemp.postDelayed(this, 1000);
	// } else {
	// handlerForDemoTemp.removeCallbacks(this);
	//
	// heatingTV.setTextColor(closeColor);
	// heatTV.setTextColor(heatingColor);
	// heatingImg.setImageResource(R.drawable.sun);
	// heatingTagLL.getChildAt(0).setVisibility(View.VISIBLE);
	// heatingTagLL.getChildAt(1).setVisibility(View.GONE);
	// stateTitleTV.setText(R.string.panel_heatstate0);
	// stateTempValueTV.setVisibility(View.GONE);
	// stateTempTagTV.setVisibility(View.GONE);
	// heatingTimeHourTV.setText("00");
	// heatingTimeMinuteTV.setText("00");
	// heatingTimeHourTV.setTextColor(openColor);
	// heatingTimeMinuteTV.setTextColor(openColor);
	// }
	// }
	// };
	// handlerForDemoTemp.postDelayed(runnableForDemoTemp, 1000);
	// }

	// 热水量不足文字闪烁
	private void setTVFlash() {
		if (timerForTVFlash != null) {
			timerForTVFlash.cancel();
			timerForTVFlash = null;
		}

		timerForTVFlash = new Timer();
		TimerTask taskcc = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (clo == 0) {
							clo = 1;
							hotwarterNoTV.setTextColor(Color.TRANSPARENT);
						} else {
							if (clo == 1) {

								clo = 0;
								hotwarterNoTV.setTextColor(Color.YELLOW);
							}
						}
					}
				});
			}
		};
		timerForTVFlash.schedule(taskcc, 1, 1000);
	}

	public void setMainState(boolean loadReserve) {
		// 解析刷新返回的指令码，展示不同的view
		// String stateStr = "";
		char[] charArr = (myApp.getRefStateMark()).toCharArray();
		char c0 = charArr[7];// 0关机；1开机
		char c1 = charArr[6];// 0即热关；1即热开
		char c2 = charArr[5];// 0加热关；1加热开
		char c3 = charArr[4];// 0保温关；1保温开
		// char c4 = charArr[3];// 0无水流；1有水流
		// char c5 = charArr[2];// 0清新负离子关；1清新负离子开
		// char c6 = charArr[1];// 0抑菌杀菌关；1抑菌杀菌开

		if (c1 == '1') {
			isUse = true;
		} else {
			isUse = false;
		}

		if (c2 == '1') {
			isHeating = true;
		} else {
			isHeating = false;
		}

		if (c3 == '1') {
			isHeat = true;
		} else {
			isHeat = false;
		}

		if (c0 == '0') {
			isClose = true;
		} else if (c0 == '1') {
			isClose = false;
		}

		// 模式开启信息
		char[] modeCArr = myApp.getRefModeMark().toCharArray();
		// 0 0 0 0 0 0 0 0 000 00 000
		// 峰谷 节能 冬季 夏季 整胆 半胆 极速 E+ 01智洁排水 001 1人洗
		// 10一键排水 010 2人洗
		// 11暖风 011 3人洗
		// 100 老人洗
		// 101 成人洗
		// 110 儿童洗
		char modec7 = modeCArr[7]; // E+增容
		char modec6 = modeCArr[6]; // 生活用水
		// char modec5 = modeCArr[5]; // 半胆速热
		// char modec4 = modeCArr[4]; // 整胆速热
		char modec3 = modeCArr[3];// 夏季模式
		char modec2 = modeCArr[2];// 冬季模式
		char modec1 = modeCArr[1];// 节能模式
		char modec0 = modeCArr[0];// 峰谷夜电
		// 001 1人洗；010 2人洗；011 3人洗；100 老人洗；101 成人洗；110 儿童洗
		String modeFr = String.valueOf(modeCArr[13])
				+ String.valueOf(modeCArr[14]) + String.valueOf(modeCArr[15]);
		// 01智洁排水；10一键排水；11暖风
		String modeJk = String.valueOf(modeCArr[11])
				+ String.valueOf(modeCArr[12]);
		boolean isCloudMode = (modeCArr[10] == '1') ? true : false;
		boolean isReserveMode = (modeCArr[9] == '1') ? true : false;
		boolean isJSXMode = (modeCArr[8] == '1') ? true : false;
		myApp.setCurModeName("");
		if (modec7 == '1') {
			myApp.setCurModeName("E+增容");
		} else if (modec6 == '1') {
			myApp.setCurModeName("生活用水");
		}
		// else if (modec5 == '1') {
		// myApp.setCurModeName("半胆速热");
		// } else if (modec4 == '1') {
		// myApp.setCurModeName("整胆速热");
		// }
		else if (modec3 == '1') {
			myApp.setCurModeName("夏季模式");
		} else if (modec2 == '1') {
			myApp.setCurModeName("冬季模式");
		} else if (modec1 == '1') {
			myApp.setCurModeName("节能模式");
		} else if (modec0 == '1') {
			myApp.setCurModeName("峰谷夜电");
		} else if (modeFr.equals("001")) {
			myApp.setCurModeName("1人洗");
		} else if (modeFr.equals("010")) {
			myApp.setCurModeName("2人洗");
		} else if (modeFr.equals("011")) {
			myApp.setCurModeName("3人洗");
		} else if (modeFr.equals("100")) {
			myApp.setCurModeName("老人洗");
		} else if (modeFr.equals("101")) {
			myApp.setCurModeName("成人洗");
		} else if (modeFr.equals("110")) {
			myApp.setCurModeName("儿童洗");
		} else if (modeJk.equals("01")) {
			myApp.setCurModeName("智洁排水");
		} else if (modeJk.equals("10")) {
			myApp.setCurModeName("一键排水");
		} else if (modeJk.equals("11")) {
			myApp.setCurModeName("暖风");
		}

		isCloudBtnOpen = false;
		isYYXBtnOpen = false;
		isJSXBtnOpen = false;
		if (isCloudMode) {
			isCloudBtnOpen = true;
			setPanelState("cloud");
		}
		if (isReserveMode) {
			isYYXBtnOpen = true;
			setPanelState("yyx");
		}
		if (isJSXMode) {
			isJSXBtnOpen = true;
			setPanelState("jsx");
		}

		// 故障信息
		isErr = false;
		errStr = "4";
		char[] errCArr = myApp.getRefErrMark().toCharArray();
		System.out.println("故障代码=="+myApp.getRefErrMark());
		if (errCArr[4] == '1') {
			isErr = true;

			if (!errStr.equals("") && Integer.parseInt(errStr) > 4) {
				errStr = "4";
			}
		}
		if (errCArr[5] == '1') {
			isErr = true;

			if (!errStr.equals("") && Integer.parseInt(errStr) > 3) {
				errStr = "3";
			}
		}
		if (errCArr[6] == '1') {
			isErr = true;
			if (!errStr.equals("") && Integer.parseInt(errStr) > 2) {
				errStr = "2";
			}
		}
		if (errCArr[7] == '1') {
			isErr = true;
			if (!errStr.equals("") && Integer.parseInt(errStr) > 1) {
				errStr = "1";
			}
		}

		if (isErr) {
			// myApp.setHasErr(true);
			isClose = true;
		} else {
			myApp.setHasErr(false);
		}

		System.out.println("刷新页面，关机：" + isClose);
		updateOpenBtn();
		updateOpenSetPageBtn();
		updateTrueTemp(true);
		updateHeatingTag();
		updateStateInfo();
		updateHotWarter();
		updateHotTime(isReserveMode);
		updateOpeBtn();
		setJSXList();

		// 1、预约开启2、设置预约/取消预约---不用刷新预约状态；第一次加载页面，预约开启，则去刷新预约状态，否则不去刷新预约状态
		if (!isReserveMode) {
			myApp.setReserve1Mark("0075200011111111");
			myApp.setReserve2Mark("0075080010111110");
			myApp.setReserve3Mark("0075140010111110");
			myApp.setReserveList(new ArrayList<Map<String, Object>>());
			myApp.getReserveList().add(null);
			myApp.getReserveList().add(null);
			myApp.getReserveList().add(null);
			updateReserveInfo(null, null, null);
		} else {
			if (myApp.isReserveToMain()) {
				myApp.setReserveToMain(false);
				if (myApp.isReserveToMainAndClose()) {
					myApp.setReserveToMainAndClose(false);
					setReserveName = myApp.getReserveToMainCloseName();
					setCloseReserve("");
				} else {
					initReserveInfo();
				}
			} else {
				if (loadReserve && myApp.isFirstLoadMain()) {
					loadReserve1();
				} else
					initReserveInfo();
			}
		}

		if (myApp.isFirstLoadMain()) {
			// 第一次加载完毕，设置状态标志
			myApp.setFirstLoadMain(false);
		}
	}

	// 设置面板的温度设置状态
	private void setPanelState(String tempType) {
		if (tempType.equals("jsx")) {
			heatingTV.setTextColor(heatingColor);
			heatingTagLL.getChildAt(0).setVisibility(View.GONE);
			heatingTagLL.getChildAt(1).setVisibility(View.VISIBLE);
			stateTitleTV.setText(R.string.panel_heatstate2);
			stateTempValueTV.setVisibility(View.VISIBLE);
			stateTempValueTV.setText(myApp.getTempMark());
			stateTempTagTV.setVisibility(View.VISIBLE);
			heatingTimeHourTV.setTextColor(heatingColor);
			heatingTimeMinuteTV.setTextColor(heatingColor);

			cloudBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			jsxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
			jsxBtn.getBackground().setLevel(myApp.getFaceLevel());
			yyxBtn.setBackgroundResource(R.drawable.bg_btn_normal);

			cloudTag.setVisibility(View.GONE);
			cloudLL.setVisibility(View.GONE);
			cloudInfoLL.setVisibility(View.GONE);
			reserveLL.setVisibility(View.VISIBLE);
		} else if (tempType.equals("yyx")) {
			heatingTV.setTextColor(heatingColor);
			heatingTagLL.getChildAt(0).setVisibility(View.GONE);
			heatingTagLL.getChildAt(1).setVisibility(View.VISIBLE);
			stateTitleTV.setText(R.string.panel_heatstate5);
			stateTempValueTV.setVisibility(View.VISIBLE);
			stateTempValueTV.setText(myApp.getTempMark());
			stateTempTagTV.setVisibility(View.VISIBLE);
			heatingTimeHourTV.setTextColor(heatingColor);
			heatingTimeMinuteTV.setTextColor(heatingColor);

			cloudBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			jsxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			yyxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
			yyxBtn.getBackground().setLevel(myApp.getFaceLevel());
		} else if (tempType.equals("cloudLoad")) {
			stateTitleTV.setText(R.string.panel_heatstate1);
			stateTempValueTV.setVisibility(View.GONE);
			stateTempTagTV.setVisibility(View.GONE);
			heatingTimeHourTV.setTextColor(openColor);
			heatingTimeMinuteTV.setTextColor(openColor);

			return;
		} else if (tempType.equals("cloud")) {
			heatingTV.setTextColor(heatingColor);
			heatingTagLL.getChildAt(0).setVisibility(View.GONE);
			heatingTagLL.getChildAt(1).setVisibility(View.VISIBLE);
			stateTitleTV.setText(R.string.panel_heatstate2);
			stateTempValueTV.setVisibility(View.VISIBLE);
			stateTempValueTV.setText(myApp.getTempMark());
			stateTempTagTV.setVisibility(View.VISIBLE);
			heatingTimeHourTV.setTextColor(heatingColor);
			heatingTimeMinuteTV.setTextColor(heatingColor);

			cloudBtn.setBackgroundResource(R.drawable.bg_skin_btn);
			cloudBtn.getBackground().setLevel(myApp.getFaceLevel());
			jsxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			yyxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
		}

	}

	// 打开设置页面
	public void setSetPageShow() {
		slideLayout.snapToScreen(1, false);
	}

	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		int[] location = new int[2];

		if (isJSXLLOpen) {
			jsxLL.getLocationOnScreen(location);
			curX = location[0];
			curY = location[1];
			curW = jsxLL.getWidth();
			curH = jsxLL.getHeight();

			if (x >= curX && x <= (curX + curW) && y >= curY
					&& y <= (curY + curH)) {
				if (jsxSetTempValueET.getVisibility() == View.VISIBLE) {
					jsxSetTempValueET.getLocationOnScreen(location);
					curX = location[0];
					curY = location[1];
					curW = jsxSetTempValueET.getWidth();
					curH = jsxSetTempValueET.getHeight();

					if (x >= curX && x <= (curX + curW) && y >= curY
							&& y <= (curY + curH)) {
					} else {
						jsxSetTempValueET.setVisibility(View.GONE);
						jsxSetTempValueTV.setVisibility(View.VISIBLE);
						jsxSetTempValueTV.setText(jsxSetTempValueET.getText());
					}
				}
			} else {
				jsxLL.setVisibility(View.INVISIBLE);
				isJSXLLOpen = false;

				if (isJSXBtnOpen) {
					jsxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
					jsxBtn.getBackground().setLevel(myApp.getFaceLevel());
				} else {
					jsxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
				}
				if (isYYXBtnOpen) {
					yyxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
					yyxBtn.getBackground().setLevel(myApp.getFaceLevel());
				} else {
					yyxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
				}
				if (isCloudBtnOpen) {
					cloudBtn.setBackgroundResource(R.drawable.bg_skin_btn);
					cloudBtn.getBackground().setLevel(myApp.getFaceLevel());
				} else {
					cloudBtn.setBackgroundResource(R.drawable.bg_btn_normal);
				}
			}
		} else if (isYYXLLOpen) {
			yyxLL.getLocationOnScreen(location);
			curX = location[0];
			curY = location[1];
			curW = yyxLL.getWidth();
			curH = yyxLL.getHeight();

			if (x >= curX && x <= (curX + curW) && y >= curY
					&& y <= (curY + curH)) {
			} else {
				yyxLL.setVisibility(View.INVISIBLE);
				isYYXLLOpen = false;

				if (isJSXBtnOpen) {
					jsxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
					jsxBtn.getBackground().setLevel(myApp.getFaceLevel());
				} else {
					jsxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
				}
				if (isYYXBtnOpen) {
					yyxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
					yyxBtn.getBackground().setLevel(myApp.getFaceLevel());
				} else {
					yyxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
				}
				if (isCloudBtnOpen) {
					cloudBtn.setBackgroundResource(R.drawable.bg_skin_btn);
					cloudBtn.getBackground().setLevel(myApp.getFaceLevel());
				} else {
					cloudBtn.setBackgroundResource(R.drawable.bg_btn_normal);
				}
			}
		}

		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (isJSXLLOpen) {
				jsxLL.setVisibility(View.INVISIBLE);
				isJSXLLOpen = false;

				if (isJSXBtnOpen) {
					jsxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
					jsxBtn.getBackground().setLevel(myApp.getFaceLevel());
				} else {
					jsxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
				}
			} else if (isYYXLLOpen) {
				yyxLL.setVisibility(View.INVISIBLE);
				isYYXLLOpen = false;

				if (isYYXBtnOpen) {
					yyxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
					yyxBtn.getBackground().setLevel(myApp.getFaceLevel());
				} else {
					yyxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
				}
			} else {
				DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case AlertDialog.BUTTON1:// "确认"按钮退出程序
							MyTool.closeSureAD();
							myApp.exit(true);
							break;
						case AlertDialog.BUTTON2:// "取消"第二个按钮取消对话框
							MyTool.closeSureAD();
							dialog.cancel();
							break;
						default:
							break;
						}
					}
				};

				MyTool.openAlertDialog(MainActivity.this, "系统提示", "确定要退出吗",
						"确定", "取消", listener, listener, false);
			}
			return false;
		}
		return false;
	}

	/****************** 控件更新 *****************/
	private void updateOpenBtn() {
		if (isClose) {
			openBtn.setText("开启热水器");
			openBtn.setBackgroundResource(R.drawable.bg_btn_open);
			openBtn.setOnClickListener(this);
		} else {
			openBtn.setText("关闭热水器");
			openBtn.setBackgroundResource(R.drawable.bg_skin_openbtn);
			openBtn.getBackground().setLevel(myApp.getFaceLevel());
			openBtn.setOnClickListener(this);
		}
	}

	private void updateOpenSetPageBtn() {
		if (isClose) {
			rightButton.setOnClickListener(this);
		} else {
			rightButton.setOnClickListener(this);
		}
	}

	private void updateTrueTemp(boolean isChangeStyle) {
		trueTemp.setText(myApp.getRefSJWDMark());
		trueTemp.setOnClickListener(null);

		if (!isChangeStyle)
			return;

		if (isClose) {
			if (isErr) {
				trueTempTitle1.setTextColor(openColor);
				trueTempTitle2.setTextColor(openColor);
				trueTemp.setTextColor(openColor);
				trueTemptag.setVisibility(View.GONE);

				trueTempTitle1.setText(R.string.panel_tempTitle3);
				trueTempTitle2.setText(R.string.panel_tempTitle4);
				trueTemp.setText("E" + errStr);
				trueTemp.setOnClickListener(this);
			} else {
				trueTempTitle1.setTextColor(closeColor);
				trueTempTitle2.setTextColor(closeColor);
				trueTemp.setTextColor(closeColor);
				trueTemptag.setVisibility(View.VISIBLE);
				trueTemptag.setTextColor(closeColor);

				trueTempTitle1.setText(R.string.panel_tempTitle1);
				trueTempTitle2.setText(R.string.panel_tempTitle2);
			}
		} else {
			trueTempTitle1.setTextColor(openColor);
			trueTempTitle2.setTextColor(openColor);
			trueTemp.setTextColor(openColor);
			trueTemptag.setVisibility(View.VISIBLE);
			trueTemptag.setTextColor(openColor);

			trueTempTitle1.setText(R.string.panel_tempTitle1);
			trueTempTitle2.setText(R.string.panel_tempTitle2);
		}
	}

	private void updateHeatingTag() {
		if (isClose) {
			heatingTagLL.getChildAt(0).setVisibility(View.VISIBLE);
			heatingTagLL.getChildAt(1).setVisibility(View.GONE);
			heatingImg.setImageResource(R.drawable.sun_noact);
			heatingTV.setTextColor(closeColor);
			heatTV.setTextColor(closeColor);
		} else {
			if (isHeating) {
				heatingTagLL.getChildAt(0).setVisibility(View.GONE);
				heatingTagLL.getChildAt(1).setVisibility(View.VISIBLE);
				heatingTV.setTextColor(heatingColor);
				heatTV.setTextColor(closeColor);

				heatingTimeLL.setVisibility(View.VISIBLE);
				slip2LL.setVisibility(View.VISIBLE);
			} else if (isHeat) {
				heatingTagLL.getChildAt(0).setVisibility(View.VISIBLE);
				heatingTagLL.getChildAt(1).setVisibility(View.GONE);
				heatingImg.setImageResource(R.drawable.sun);
				heatingTV.setTextColor(closeColor);
				heatTV.setTextColor(heatingColor);

				heatingTimeLL.setVisibility(View.GONE);
				slip2LL.setVisibility(View.GONE);
			} else {
				heatingTagLL.getChildAt(0).setVisibility(View.VISIBLE);
				heatingTagLL.getChildAt(1).setVisibility(View.GONE);
				heatingImg.setImageResource(R.drawable.sun_noact);
				heatingTV.setTextColor(closeColor);
				heatTV.setTextColor(closeColor);

				heatingTimeLL.setVisibility(View.VISIBLE);
				slip2LL.setVisibility(View.VISIBLE);
			}
		}
	}

	private void updateStateInfo() {
		if (isClose) {
			if (isErr) {
				stateTag.setBackgroundResource(R.drawable.bg_heatstate);
				stateTitleTV.setTextColor(heatTagOpenColor);
				stateTitleTV.setText(R.string.panel_heatstate10);
				stateTempValueTV.setVisibility(View.GONE);
				stateTempTagTV.setVisibility(View.GONE);
			} else {
				stateTag.setBackgroundResource(R.drawable.bg_heatstate_noact);
				stateTitleTV.setTextColor(closeColor);
				stateTitleTV.setText(R.string.panel_heatstate7);
				stateTempValueTV.setVisibility(View.GONE);
				stateTempTagTV.setVisibility(View.GONE);
			}
		} else {
			stateTag.setBackgroundResource(R.drawable.bg_heatstate);
			stateTitleTV.setTextColor(heatTagOpenColor);
			if (isLoading) {// 匹配中
				stateTitleTV.setText(R.string.panel_heatstate1);
				stateTempValueTV.setVisibility(View.GONE);
				stateTempTagTV.setVisibility(View.GONE);
			}
			if (isHeating) {// 正在加热
				stateTitleTV.setText(R.string.panel_heatstate2);
				stateTempValueTV.setVisibility(View.VISIBLE);
				stateTempValueTV.setText(myApp.getTempMark());
				stateTempTagTV.setVisibility(View.VISIBLE);
			} else if (isHeat) {// 保温中
				stateTitleTV.setText(R.string.panel_heatstate8);
				stateTempValueTV.setVisibility(View.GONE);
				stateTempTagTV.setVisibility(View.GONE);
			} else {
				if (isUse) {// 使用中
					stateTitleTV.setText(R.string.panel_heatstate6);
					stateTempValueTV.setVisibility(View.GONE);
					stateTempTagTV.setVisibility(View.GONE);
				} else {
					stateTitleTV.setText(R.string.panel_heatstate9);
					stateTempValueTV.setVisibility(View.GONE);
					stateTempTagTV.setVisibility(View.GONE);
				}
			}
		}
	}

	private void updateHotWarter() {
		if (isClose) {
			if (timerForTVFlash != null) {
				timerForTVFlash.cancel();
				timerForTVFlash = null;
			}

			hotwarterTitleTV.setTextColor(closeColor);
			hotwarterValueTV.setTextColor(closeColor);
			if (hotwarterNoTV.getVisibility() == View.VISIBLE) {
				hotwarterNoTV.setTextColor(closeColor);
			}

			Drawable d = getResources()
					.getDrawable(R.drawable.progressbg_close);
			d.setBounds(hotwarterProbar.getProgressDrawable().getBounds());
			hotwarterProbar.setProgressDrawable(d);
			hotwarterProbar.setProgress(0);

			hotwarterProbar
					.setProgress(Integer.parseInt(myApp.getRefRSLMark()));
		} else {
			Drawable d = getResources().getDrawable(R.drawable.progressbg);
			d.setBounds(hotwarterProbar.getProgressDrawable().getBounds());
			hotwarterProbar.setProgressDrawable(d);
			hotwarterProbar.setProgress(0);

			if (myApp.getRefRSLMark().equals("0")) {
				hotwarterNoTV.setVisibility(View.VISIBLE);
				hotwarterTitleTV.setVisibility(View.GONE);
				hotwarterValueTV.setVisibility(View.GONE);
				hotwarterProbar.setProgress(10);
				setTVFlash();
			} else {
				hotwarterTitleTV.setTextColor(openColor);
				hotwarterValueTV.setTextColor(openColor);

				hotwarterValueTV
						.setText((myApp.getRefRSLMark().equals("100") ? "99"
								: myApp.getRefRSLMark()) + "%");
				hotwarterProbar.setProgress(Integer.parseInt(myApp
						.getRefRSLMark()));

				hotwarterNoTV.setVisibility(View.GONE);
				hotwarterTitleTV.setVisibility(View.VISIBLE);
				hotwarterValueTV.setVisibility(View.VISIBLE);
			}
		}
	}

	private void updateHotTime(boolean isReserveOpen) {
		if (isClose) {
			heatingTimeTitle.setTextColor(closeColor);
			heatingTimeHourTV.setTextColor(closeColor);
			heatingTimeMinuteTV.setTextColor(closeColor);
			heatingTimeHourTagTV.setTextColor(closeColor);
			heatingTimeMinuteTagTV.setTextColor(closeColor);
		} else {
			heatingTimeTitle.setTextColor(openColor);
			heatingTimeHourTV.setTextColor(openColor);
			heatingTimeMinuteTV.setTextColor(openColor);
			heatingTimeHourTagTV.setTextColor(openColor);
			heatingTimeMinuteTagTV.setTextColor(openColor);

			if (!isHeat && !isHeating && isReserveOpen) {
				heatingTimeTitle.setText(R.string.panel_heatingTimeForReserve);
			} else {
				heatingTimeTitle.setText(R.string.panel_heatingTimeForHeating);
			}

			if (myApp.getRefHeatingTimeHMark().equals("0")) {
				heatingTimeHourTV.setVisibility(View.INVISIBLE);
				heatingTimeHourTagTV.setVisibility(View.INVISIBLE);
			} else {
				heatingTimeHourTV.setText(myApp.getRefHeatingTimeHMark());
				heatingTimeHourTV.setVisibility(View.VISIBLE);
				heatingTimeHourTagTV.setVisibility(View.VISIBLE);
			}
			heatingTimeMinuteTV.setText(myApp.getRefHeatingTimeMMark());
		}
	}

	private void updateReserveInfo(String reserve1Info, String reserve2Info,
			String reserve3Info) {
		if (isClose) {
			reserveTitle.setTextColor(closeColor);
			noReserveInfoTV.setTextColor(closeColor);
			reserveInfoTV1.setTextColor(closeColor);
			reserveInfoTV2.setTextColor(closeColor);
			reserveInfoTV3.setTextColor(closeColor);
		} else {
			reserveTitle.setTextColor(openColor);
			noReserveInfoTV.setTextColor(openColor);
			reserveInfoTV1.setTextColor(openColor);
			reserveInfoTV2.setTextColor(openColor);
			reserveInfoTV3.setTextColor(openColor);

			if (reserve1Info == null && reserve2Info == null
					&& reserve3Info == null) {
				noReserveInfoTV.setVisibility(View.VISIBLE);
				reserveInfoTV1.setVisibility(View.GONE);
				reserveInfoTV2.setVisibility(View.GONE);
				reserveInfoTV3.setVisibility(View.GONE);
				reserve1Img
						.setImageResource(R.drawable.v5_3_0_profile_arrow_back);
				reserve2Img
						.setImageResource(R.drawable.v5_3_0_profile_arrow_back);
				reserve3Img
						.setImageResource(R.drawable.v5_3_0_profile_arrow_back);
			} else {
				noReserveInfoTV.setVisibility(View.GONE);
				if (reserve1Info != null) {
					reserveInfoTV1.setVisibility(View.VISIBLE);
					reserveInfoTV1.setText(reserve1Info);
				} else {
					reserveInfoTV1.setVisibility(View.GONE);
					reserve1Img
							.setImageResource(R.drawable.v5_3_0_profile_arrow_back);
				}
				if (reserve2Info != null) {
					reserveInfoTV2.setVisibility(View.VISIBLE);
					reserveInfoTV2.setText(reserve2Info);
				} else {
					reserveInfoTV2.setVisibility(View.GONE);
					reserve2Img
							.setImageResource(R.drawable.v5_3_0_profile_arrow_back);
				}
				if (reserve3Info != null) {
					reserveInfoTV3.setVisibility(View.VISIBLE);
					reserveInfoTV3.setText(reserve3Info);
				} else {
					reserveInfoTV3.setVisibility(View.GONE);
					reserve3Img
							.setImageResource(R.drawable.v5_3_0_profile_arrow_back);
				}
			}
		}
	}

	private void updateOpeBtn() {
		if (isClose) {
			cloudBtn.setOnClickListener(null);
			cloudBtn.setTextColor(closeColor);
			cloudBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			jsxBtn.setOnClickListener(null);
			jsxBtn.setTextColor(closeColor);
			jsxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			yyxBtn.setOnClickListener(null);
			yyxBtn.setTextColor(closeColor);
			yyxBtn.setBackgroundResource(R.drawable.bg_btn_normal);

			cloudLL.setVisibility(View.GONE);
			cloudInfoLL.setVisibility(View.GONE);
			reserveLL.setVisibility(View.VISIBLE);
		} else {
			cloudBtn.setOnClickListener(this);
			cloudBtn.setTextColor(openColor);
			jsxBtn.setOnClickListener(this);
			jsxBtn.setTextColor(openColor);
			yyxBtn.setOnClickListener(this);
			yyxBtn.setTextColor(openColor);
			if (isYYXBtnOpen) {
				yyxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
				yyxBtn.getBackground().setLevel(myApp.getFaceLevel());

				cloudInfoLL.setVisibility(View.GONE);
				cloudTag.setVisibility(View.GONE);
				reserveLL.setVisibility(View.VISIBLE);
			} else {
				yyxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			}
			if (isCloudBtnOpen) {
				cloudBtn.setBackgroundResource(R.drawable.bg_skin_btn);
				cloudBtn.getBackground().setLevel(myApp.getFaceLevel());

				cloudInfoLL.setVisibility(View.VISIBLE);
				cloudTag.setVisibility(View.VISIBLE);
				reserveLL.setVisibility(View.GONE);
			} else {
				cloudBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			}
			if (isJSXBtnOpen) {
				jsxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
				jsxBtn.getBackground().setLevel(myApp.getFaceLevel());
			} else {
				jsxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			}
		}
	}

	/***************** 数据加载 *****************/
	// 获取城市天气
	private void getCity() {
		MyTool.showProgressDialog("请稍等，正在加载设备信息。。。", MainActivity.this,
				MainActivity.this);

		// 获取城市代码更新天气 1、不为空，说明获取机型时拿到了城市信息，保存到本地即可(室外通过获取机型返回信息拿城市信息)
		// 2、为空，说明没有拿到城市信息，则去访问服务器获取城市信息(室内通过调云端接口拿城市信息)
		if (myApp.getCurServerCity() == null) {
			getServerCity();
		} else {
			getCityCode(myApp.getCurServerCity());
		}
	}

	// 获取机型
	public void getServerType() {
		// MyTool.showProgressDialog("请稍等，正在获取热水器机型。。。", MainActivity.this,
		// MainActivity.this);
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					System.out.println("********获取机型********");
					if (LoadData.isWifi) {
						msg.obj = LoadData.getServerTypeForWifi();
					} else {
						socketBackName = "getType";
						openTimeForSocket();
						msg.obj = LoadData.getServerTypeFor3G();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForGetServertype.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForGetServertype = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null && (Boolean) msg.obj) {
					if (LoadData.isWifi)
						getTypeBack(true);
				} else {
					getTypeBack(false);
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void getTypeBack(boolean result) {
		MyTool.closeProgressDialog();
		closeTimeForSocket();
		if (result) {
			if (!LoadData.isWifi)
				MyTool.showProgressDialog("请稍等，正在查询热水器状态。。。",
						MainActivity.this, MainActivity.this);
			else
				isLoadData = true;
			loadRefresh();
		} else {
			MyTool.openAlertDialog(MainActivity.this, "提示",
					"获取热水器机型失败，请检查热水器工作是否正常!", "返回列表", "重新获取",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();// 取消弹出框
							MyTool.closeSureAD();
							Intent intent = new Intent();
							System.out
									.println("*************LoadData.isWifi==="
											+ LoadData.isWifi);
							if (LoadData.isWifi)
								intent.setClass(MainActivity.this,
										SelLinkType.class);
							else
								intent.setClass(MainActivity.this,
										MyServerListActivity.class);
							startActivity(intent);
							MainActivity.this.finish();
						}
					}, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							MyTool.closeSureAD();
							dialog.cancel();
							MyTool.showProgressDialog("请稍等，正在获取热水器机型。。。",
									MainActivity.this, MainActivity.this);
							getServerType();
						}
					}, false);
		}
	}

	// 刷新状态
	public void loadRefresh() {
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					System.out.println("********刷新状态********");
					if (LoadData.isWifi) {
						msg.obj = LoadData.refreshStateForWifi();
					} else {
						socketBackName = "refresh";
						openTimeForSocket();
						msg.obj = LoadData.refreshStateFor3G();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForRefresh.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForRefresh = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null && (Boolean) msg.obj) {
					if (LoadData.isWifi)
						refreshBack(true);
				} else {
					refreshBack(false);
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void refreshBack(boolean result) {
		MyTool.closeProgressDialog();
		isLoadData = false;
		closeTimeForSocket();
		if (result) {
			setMainState(true);
		} else {
			MyTool.openAlertDialog(MainActivity.this, "提示",
					"查询热水器状态失败，请检查热水器工作是否正常!", "确定", "重新查询",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							MyTool.closeSureAD();
							dialog.cancel();// 取消弹出框
						}
					}, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							MyTool.closeSureAD();
							dialog.cancel();
							MyTool.showProgressDialog("请稍等，正在查询热水器状态。。。",
									MainActivity.this, MainActivity.this);
							loadRefresh();
						}
					}, false);
		}
	}

	// 开机
	public void setOpen() {
		if (MyTool.mypDialog != null || isLoadData)
			return;
		if (!LoadData.isWifi)
			MyTool.showProgressDialog("请稍等，正在启动热水器。。。", MainActivity.this,
					MainActivity.this);
		else
			isLoadData = true;
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					System.out.println("********开机********");
					if (LoadData.isWifi) {
						msg.obj = LoadData.openForWifi();
					} else {
						socketBackName = "open";
						openTimeForSocket();
						msg.obj = LoadData.openFor3G();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForOpen.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForOpen = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null && (Boolean) msg.obj) {
					if (LoadData.isWifi)
						openBack(true);
				} else {
					openBack(false);
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void openBack(boolean result) {
		MyTool.closeProgressDialog();
		isLoadData = false;
		closeTimeForSocket();

		if (result) {
			setMainState(false);
		} else {
			MyTool.openSureAD(MainActivity.this, "提示", "开机失败，请检查热水器工作是否正常!");
		}
	}

	// 关机
	public void setClose() {
		if (MyTool.mypDialog != null || isLoadData)
			return;
		if (!LoadData.isWifi)
			MyTool.showProgressDialog("请稍等，正在关闭热水器。。。", MainActivity.this,
					MainActivity.this);
		else
			isLoadData = true;
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					System.out.println("********关机********");
					if (LoadData.isWifi) {
						msg.obj = LoadData.closeForWifi();
					} else {
						socketBackName = "close";
						openTimeForSocket();
						msg.obj = LoadData.closeFor3G();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForClose.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForClose = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null && (Boolean) msg.obj) {
					if (LoadData.isWifi)
						closeBack(true);
				} else {
					closeBack(false);
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void closeBack(boolean result) {
		MyTool.closeProgressDialog();
		isLoadData = false;
		closeTimeForSocket();
		if (result) {
			if (isJSXLLOpen) {
				jsxLL.setVisibility(View.INVISIBLE);
				isJSXLLOpen = false;

				if (isJSXBtnOpen) {
					jsxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
					jsxBtn.getBackground().setLevel(myApp.getFaceLevel());
				} else {
					jsxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
				}
			} else if (isYYXLLOpen) {
				yyxLL.setVisibility(View.INVISIBLE);
				isYYXLLOpen = false;

				if (isYYXBtnOpen) {
					yyxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
					yyxBtn.getBackground().setLevel(myApp.getFaceLevel());
				} else {
					yyxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
				}
			}
			setMainState(false);
		} else {
			MyTool.openSureAD(MainActivity.this, "提示", "关机失败，请检查热水器工作是否正常!");
		}
	}

	// 设置模式
	public void setModeCmd(final String cmdMode, final String setTemp,
			final String cmdCName) {
		if (MyTool.mypDialog != null || isLoadData)
			return;
		if (!LoadData.isWifi)
			MyTool.showProgressDialog("请稍等，正在设置热水器" + curSetModeCName,
					MainActivity.this, MainActivity.this);
		else
			isLoadData = true;
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					System.out.println("********设置模式********");
					String tempStr;
					if (setTemp != null) {
						tempStr = Integer.toString(
								Integer.parseInt(setTemp, 10), 16)
								+ "0000";
					} else {
						tempStr = "000000";
					}

					if (LoadData.isWifi) {
						msg.obj = LoadData.openModeForWifi("mode", cmdMode,
								tempStr, cmdCName);
					} else {
						socketBackName = "setMode";
						openTimeForSocket();
						msg.obj = LoadData.openModeFor3G("mode", cmdMode,
								tempStr, cmdCName);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForSetMode.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForSetMode = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null && (Boolean) msg.obj) {
					if (LoadData.isWifi)
						setModeBack(true);
				} else {
					setModeBack(false);
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void setModeBack(boolean result) {
		MyTool.closeProgressDialog();
		isLoadData = false;
		closeTimeForSocket();

		if (result) {
			if (curSetModeCName != null)
				MyTool.makeText(MainActivity.this, "设置" + curSetModeCName
						+ "成功");
			if (callSetTempName == null)
				return;

			if (callSetTempName.equals("jsxSet")) {
				jsxLL.getChildAt(1).setVisibility(View.GONE);
				jsxLL.setVisibility(View.INVISIBLE);
				cloudInfoLL.setVisibility(View.GONE);
				cloudTag.setVisibility(View.GONE);
				reserveLL.setVisibility(View.VISIBLE);

				isJSXLLOpen = false;
				isJSXBtnOpen = true;

				callSetTempName = null;
				setPanelState("jsx");
			} else if (callSetTempName.equals("jsxMode")) {
				jsxLL.setVisibility(View.INVISIBLE);
				isJSXBtnOpen = true;

				callSetTempName = null;
				setPanelState("jsx");
			} else if (callSetTempName.equals("cloud")) {
				cloudLL.setVisibility(View.GONE);
				heatingTimeLL.setVisibility(View.VISIBLE);
				slip2LL.setVisibility(View.VISIBLE);
				cloudInfoLL.setVisibility(View.VISIBLE);
				isCloudBtnOpen = true;

				callSetTempName = null;
				setPanelState("cloud");
			}

			setMainState(false);
			isJSXLLOpen = false;
		} else {
			if (curSetModeCName != null)
				MyTool.openSureAD(MainActivity.this, "提示",
						"设置模式失败，请检查热水器工作是否正常!");
			if (curSetModeCName.equals("云智能")) {
				reserveLL.setVisibility(View.VISIBLE);
				heatingTimeLL.setVisibility(View.VISIBLE);
				slip2LL.setVisibility(View.VISIBLE);
				cloudInfoLL.setVisibility(View.GONE);
				cloudLL.setVisibility(View.GONE);
				cloudTag.setVisibility(View.INVISIBLE);
				updateOpeBtn();
			}
		}
	}

	// 刷新预约一
	public void loadReserve1() {
		if (!LoadData.isWifi)
			MyTool.showProgressDialog("请稍等，正在查询热水器预约状态。。。", MainActivity.this,
					MainActivity.this);
		else
			isLoadData = true;
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					System.out.println("********刷新预约一********");
					if (LoadData.isWifi) {
						msg.obj = LoadData.refreshReserveForWifi("1");
					} else {
						socketBackName = "getReserve";
						openTimeForSocket();
						msg.obj = LoadData.refreshReserveFor3G("1");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForReserve1.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForReserve1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null && (Boolean) msg.obj) {
					if (LoadData.isWifi)
						getReserveBack(true);
				} else {
					getReserveBack(false);
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void getReserveBack(boolean result) {
		MyTool.closeProgressDialog();
		isLoadData = false;
		closeTimeForSocket();
		if (result) {
			initReserveInfo();
		} else {
			MyTool.openSureAD(MainActivity.this, "提示",
					"查询热水器预约状态失败，请检查热水器工作是否正常!");
		}
	}

	// 设置预约
	public void setReserve(final String reserveName, final String reserveMark) {
		if (MyTool.mypDialog != null || isLoadData)
			return;
		if (!LoadData.isWifi)
			MyTool.showProgressDialog("请稍等，正在取消预约" + reserveName + "。。。",
					MainActivity.this, MainActivity.this);
		else
			isLoadData = true;
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					System.out.println("********设置预约********");
					setReserveName = reserveName;
					if (LoadData.isWifi) {
						msg.obj = LoadData.sendReserveForWifi(reserveName,
								reserveMark, false);
					} else {
						socketBackName = "setReserve";
						openTimeForSocket();
						msg.obj = LoadData.sendReserveFor3G(reserveName,
								reserveMark, false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForSetReserve.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForSetReserve = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null && (Boolean) msg.obj) {
					if (LoadData.isWifi)
						setReserveBack(true, setReserveName);
				} else {
					setReserveBack(false, "");
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void setReserveBack(boolean result, String name) {
		MyTool.closeProgressDialog();
		isLoadData = false;
		closeTimeForSocket();
		if (result) {
			MyTool.makeText(MainActivity.this, "取消预约成功");
			setCloseReserve(name);
		} else {
			MyTool.openSureAD(MainActivity.this, "提示", "设置预约失败，请检查热水器工作是否正常!");
		}
	}

	private void setCloseReserve(String name) {
		System.out.println("取消预约成功：" + setReserveName);
		if (setReserveName.equals("")) {
			setReserveName = name;
		}

		if (setReserveName.equals("1")) {
			reserve1Img.setImageResource(R.drawable.v5_3_0_profile_arrow_back);
			myApp.setReserve1Mark("0075200011111111");
			myApp.getReserveList().set(0, null);

			yyxLL.setVisibility(View.GONE);
			System.out.println("取消预约一，预约列表是否打开：" + yyxLL.getVisibility());
		} else if (setReserveName.equals("2")) {
			reserve2Img.setImageResource(R.drawable.v5_3_0_profile_arrow_back);
			myApp.setReserve2Mark("0075080010111110");
			myApp.getReserveList().set(1, null);

			yyxLL.setVisibility(View.GONE);
		} else if (setReserveName.equals("3")) {
			reserve3Img.setImageResource(R.drawable.v5_3_0_profile_arrow_back);
			myApp.setReserve3Mark("0075140010111110");
			myApp.getReserveList().set(2, null);

			yyxLL.setVisibility(View.GONE);
		}
		setReserveName = "";
		loadRefresh();
		isYYXLLOpen = false;
	}

	// 获取热水器所在地
	public void getServerCity() {
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					msg.obj = LoadData.getServerCity();
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForGetServerCity.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForGetServerCity = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null && (Boolean) msg.obj) {
					if (myApp.getCurServerCity() == null
							|| myApp.getCurServerCity().equals("")) {
						MyTool.makeText(MainActivity.this,
								"云智能不可用！该功能需要云平台支持，请检查热水器是否绑定云平台...");
						getServerType();
					} else {
						getCityCode(myApp.getCurServerCity());
					}
				} else {
					MyTool.makeText(MainActivity.this,
							"云智能不可用！该功能需要互联网支持，请检查网络是否工作正常...");
					getServerType();
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void refreshForTime3G() {
		timerForRefresh = new Timer();

		timerForRefresh.schedule(new TimerTask() {
			@Override
			public void run() {
				loadRefresh();
			}
		}, 0, myApp.getRefreshTimer() * 1000);
		// isTimerStart = true;
	}

	public void stopTime3G() {
		if (timerForRefresh != null) {
			timerForRefresh.cancel();
			timerForRefresh = null;
			// isTimerStart = false;
		}

	}

	// 注册广播接收
	private void regReceiver() {
		receiver = new MyReceiver(MainActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction("meta.midea.MainActivity");
		registerReceiver(receiver, filter);
	}

	// 更新authkey
	private void updateAuthKey() {
		String curAuthKey = myApp.getCurServerAuthKey();
		if (curAuthKey == null)
			return;
		String[] curAuthKeyArr = curAuthKey.split(";");
		// "authkey":"test4;F50J;111112"
		String newCode = String.valueOf((int) (Math.random() * 10))
				+ String.valueOf((int) (Math.random() * 10))
				+ String.valueOf((int) (Math.random() * 10))
				+ String.valueOf((int) (Math.random() * 10))
				+ String.valueOf((int) (Math.random() * 10))
				+ String.valueOf((int) (Math.random() * 10));
		System.out.println("6位随机数：" + newCode);
		if (curAuthKeyArr != null && curAuthKeyArr.length == 3) {
			String newAuthKey = curAuthKeyArr[0] + ";" + curAuthKeyArr[1] + ";"
					+ curAuthKeyArr[2] + ";" + newCode;
			System.out.println("newAuthKey=" + newAuthKey);

			// myApp.setCurServerAuthKey(newAuthKey);
		}
	}

	/************* socket超时线程 **************/
	public Timer timer;
	private TimerTask task;
	Handler handlerForTimer = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null && (Boolean) msg.obj) {
					if (socketBackName.equals("getType")) {
						getTypeBack(false);
					}
					if (socketBackName.equals("refresh")) {
						getReserveBack(false);
					}
					if (socketBackName.equals("open")) {
						openBack(false);
					}
					if (socketBackName.equals("close")) {
						closeBack(false);
					}
					if (socketBackName.equals("setMode")) {
						setModeBack(false);
					}
					if (socketBackName.equals("setReserve")) {
						setReserveBack(false, "");
					}
					if (socketBackName.equals("getReserve")) {
						getReserveBack(false);
					}
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void openTimeForSocket() {
		task = new TimerTask() {
			public void run() {
				Message message = new Message();
				message.what = 0;
				message.obj = true;
				handlerForTimer.sendMessage(message);
			}
		};
		System.out.println("开启超时");
		timer = new Timer(true);
		timer.schedule(task, 15000, 999999); // 延时15000ms后执行，999999ms执行一次
	}

	public void closeTimeForSocket() {
		System.out.println("关闭超时");
		if (timer != null) {
			System.out.println("设置预约socket超时定时器关闭了");
			timer.cancel(); // 退出计时器
			timer = null;
			if (task != null) {
				task.cancel();
			}
		}
	}

	/***************** 刷新天气 ********************/
	private void getCloudWeather() {
		if (handlerForGetWeather != null) {
			handlerForGetWeather.removeCallbacks(runnableForGetWeather);
			handlerForGetWeather = null;
		}

		// 城市码为空，说明是新的城市还没有匹配到城市码
		final String cityCode = myApp.getCurServerCityCode();
		// 间隔一小时去刷天气
		handlerForGetWeather = new Handler();
		runnableForGetWeather = new Runnable() {
			public void run() {
				searchWeatherSK(cityCode);
				handlerForGetWeather
						.postDelayed(runnableForGetWeather, 3600000);

				getSetTemp();

				// 更新云智能显示信息
				cloudCityTitleTV.setText(myApp.getCurServerCity() + "当地气温");
				cloudCityTempTV.setText(myApp.getCurServerWeather());
				cloudTempTV.setText(myApp.getCurServerCloudTemp());

				// 第一次获取到天气之后，发送获取机型指令
				if (myApp.isFirstLoadMain()) {
					myApp.setTempMark(myApp.getCurServerCloudTemp());
					getServerType();
				}

				// 保存到云端
				try {
					LoadData.saveCloudTemp();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		handlerForGetWeather.postDelayed(runnableForGetWeather, 0);
	}

	// 获取城市代码
	private String getCityCode(String cityName) {
		String cityID = null;
		String cityStr = myApp.getTextCity();

		// 去除省、市、区、县，匹配
		// cityName = cityName.replace("省", "").replace("市", "");

		if (cityName != null && !cityName.equals("")) {
			try {
				JSONObject jsonObj = new JSONObject(cityStr);
				// cityID = jsonObj.getString(cityName);

				Iterator<?> it = jsonObj.keys();
				while (it.hasNext()) {// 遍历JSONObject
					String name = it.next().toString();
					if (cityName.indexOf(name) > -1) {
						cityID = jsonObj.getString(name);
						break;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		if (cityID == null) {
			cityID = "101020100";// 无匹配默认上海天气
		}

		myApp.setCurServerCityCode(cityID);
		// 启动定时器，一小时间隔刷新当前天气情况
		getCloudWeather();

		return cityID;
	}

	// 获取当天天气信息
	private void searchWeatherSK(String cityID) {
		/* 声明网址字符串 */
		String uriAPI = "http://www.weather.com.cn/data/sk/" + cityID + ".html";
		/* 建立HTTP Get联机 */
		HttpGet httpRequest = new HttpGet(uriAPI);
		try {
			/* 发出HTTP request */
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 取出响应字符串 */
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				System.out.println("天气信息==" + strResult);
				JSONObject jsonObj = new JSONObject(strResult);
				JSONObject infoObj = new JSONObject(
						jsonObj.getString("weatherinfo"));

				// {"weatherinfo":{"city":"北京","cityid":"101010100","temp":"-9",
				// "WD":"东北风","WS":"2级","SD":"21%","WSE":"2","time":"10:30",
				// "isRadar":"1","Radar":"JC_RADAR_AZ9010_JB"}}
				String wTempStr = infoObj.getString("temp");
				int wTempInt = Integer.parseInt(wTempStr.replace("℃", "")
						.replace("°C", ""));
				myApp.setCurServerWeather(String.valueOf(wTempInt));
			} else {
				System.out.println("获取天气信息失败");
			}
		} catch (ClientProtocolException e) {
			System.out.println("获取天气信息失败");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("获取天气信息失败");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("获取天气信息失败");
			e.printStackTrace();
		}
	}

	private void getSetTemp() {
		String wTempStr = myApp.getCurServerWeather();
		int wTempInt = Integer.parseInt(wTempStr);

		// 云端温度按以下表根据当地气温获取T1≤0℃ 75℃
		// 0℃＜T1≤10℃ 75℃
		// 10℃＜T1≤15℃ 70℃
		// 15℃＜T1≤20℃ 65℃
		// 20℃＜T1≤25℃ 60℃
		// 25℃＜T1≤30℃ 55℃
		// 30℃＜T1 50℃

		if (wTempInt <= 10) {
			myApp.setCurServerCloudTemp("75");
		} else if (wTempInt > 10 && wTempInt <= 15) {
			myApp.setCurServerCloudTemp("70");
		} else if (wTempInt > 15 && wTempInt <= 20) {
			myApp.setCurServerCloudTemp("65");
		} else if (wTempInt > 20 && wTempInt <= 25) {
			myApp.setCurServerCloudTemp("60");
		} else if (wTempInt > 25 && wTempInt <= 30) {
			myApp.setCurServerCloudTemp("55");
		} else if (wTempInt > 30) {
			myApp.setCurServerCloudTemp("50");
		}
	}
}
