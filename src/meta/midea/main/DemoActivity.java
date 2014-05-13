package meta.midea.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import meta.midea.MyApplication;
import meta.midea.R;
import meta.midea.SelModeActivity;
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
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class DemoActivity extends Activity implements OnClickListener {
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
	private TextView trueTempYitle2;
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
	private boolean isReserve = false;// 预约等待

	private Handler handlerForTempUp;
	private Runnable runnableForTempUp;
	private Handler handlerForTempDown;
	private Runnable runnableForTempDown;

	private long downTime;
	private long upTime;

	private int curX;
	private int curY;
	private int curW;
	private int curH;

	private Timer timerForTVFlash;
	private int clo = 0;// 文字闪烁记录标记
	private Handler handlerForDemoTemp;
	private Runnable runnableForDemoTemp;

	// private String callSetTempName;//
	// 设置温度由谁调用("cloud"云智能,"jsxMode"即时洗模式,"jsxSet"即时洗自设)

	private Intent intent;
	private String isEvent;

	private final int closeColor = 0xff808080;// 关闭状态下文本颜色
	private final int openColor = 0xffffffff;// 开启状体下文本颜色
	private final int heatingColor = 0xffFFFF22;// 加热状态下文本颜色
	private final int heatTagOpenColor = 0xff000000;// 开启状态下加热提示文本颜色

	// private final int shadowColor = 0xff4d4d4d;// 文本阴影颜色

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		isEvent = "";
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		if (data != null) {
			isEvent = data.getString("isEvent");
			System.out.println("isEvent==" + isEvent);
		}

		// 初始化预约匹配数组
		if (myApp.getReserveList() == null) {
			myApp.setReserveList(new ArrayList<Map<String, Object>>());
			myApp.getReserveList().add(null);
			myApp.getReserveList().add(null);
			myApp.getReserveList().add(null);
		}

		myApp.setTempMark("70");

		initPanelPage();
		initSetPage();
		setFaceLevel();
		setPanelClose();

		setJSXList();
		setMainState(true);
	}

	protected void onResume() {
		super.onResume();
		setFaceLevel();

		jsxLL.setVisibility(View.GONE);
		yyxLL.setVisibility(View.GONE);

		setMainState(true);
	}

	private void setFaceLevel() {
		RelativeLayout pageLL = (RelativeLayout) findViewById(R.id.appPage);
		pageLL.getBackground().setLevel(myApp.getFaceLevel());

		RelativeLayout pageHead = (RelativeLayout) findViewById(R.id.panel_head);
		pageHead.getBackground().setLevel(myApp.getFaceLevel());

		if (!isClose) {
			openBtn.setBackgroundResource(R.drawable.bg_skin_openbtn);
			openBtn.getBackground().setLevel(myApp.getFaceLevel());
		}

		rightButton.getBackground().setLevel(myApp.getFaceLevel());

		if (isCloudBtnOpen)
			cloudBtn.getBackground().setLevel(myApp.getFaceLevel());
		if (isJSXBtnOpen)
			jsxBtn.getBackground().setLevel(myApp.getFaceLevel());
		if (isYYXBtnOpen)
			yyxBtn.getBackground().setLevel(myApp.getFaceLevel());
	}

	// 初始化panel面板
	private void initPanelPage() {
		slideLayout = (SlideLayout) findViewById(R.id.slidelayout);
		openBtn = (Button) findViewById(R.id.openButton);
		rightButton = (ImageButton) findViewById(R.id.rightButton);

		trueTempTitle1 = (TextView) findViewById(R.id.trueTempTitle1);
		trueTempYitle2 = (TextView) findViewById(R.id.trueTempTitle2);
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
		cloudTempTV = (TextView) findViewById(R.id.cloudTemp);
		cloudTempTV.setText("65");
		cloudCityTitleTV = (TextView) findViewById(R.id.cityTitle);
		cloudCityTitleTV.setText("本地气温");
		TextPaint paint3 = cloudCityTitleTV.getPaint();
		paint3.setFakeBoldText(true);
		cloudCityTempTV = (TextView) findViewById(R.id.cityTemp);
		cloudCityTempTV.setText("19");
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
					if (upTime - downTime < 300) {
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

					if (upTime - downTime < 300) {
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

	// 初始化预约洗值
	public void initReserveInfo() {
		reserve1Img.setImageResource(R.drawable.radbtn_nosel);
		reserve2Img.setImageResource(R.drawable.radbtn_nosel);
		reserve3Img.setImageResource(R.drawable.radbtn_nosel);
		noReserveInfoTV.setVisibility(View.VISIBLE);
		reserveInfoTV1.setVisibility(View.GONE);
		reserveInfoTV2.setVisibility(View.GONE);
		reserveInfoTV3.setVisibility(View.GONE);

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
			isYYXBtnOpen = true;

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
			isYYXBtnOpen = true;

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
			isYYXBtnOpen = true;

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
		updateHotTime();
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
			if (isClose) {
				String oldMark = myApp.getRefStateMark();
				myApp.setRefStateMark(oldMark.substring(0, 7) + "1");
				setMainState(true);
				return;
			} else {
				String oldMark = myApp.getRefStateMark();
				myApp.setRefStateMark(oldMark.substring(0, 7) + "0");
				setMainState(true);
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
			updateOpeBtn();

			jsxLL.setVisibility(View.VISIBLE);
			jsxLL.getChildAt(1).setVisibility(View.GONE);
			yyxLL.setVisibility(View.INVISIBLE);
			jsxBtn.setBackgroundResource(R.drawable.bg_skin_btn);
			jsxBtn.getBackground().setLevel(myApp.getFaceLevel());

			isJSXLLOpen = true;
			isYYXLLOpen = false;
			break;
		case (R.id.yyxBtn):
			updateOpeBtn();

			jsxLL.setVisibility(View.INVISIBLE);
			yyxLL.setVisibility(View.VISIBLE);
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

			if (open1.equals("ff")) {
				closeReserve1();
			} else {
				myApp.setCurReserveSet("1");
				openReserveActivity();
			}
			break;
		case (R.id.yyxlist_Img2):
		case (R.id.yyxlist_Img2LL):
			String reserve2 = myApp.getReserve2Mark();
			String open2 = reserve2.substring(0, 2);

			if (open2.equals("ff")) {
				closeReserve2();
			} else {
				myApp.setCurReserveSet("2");
				openReserveActivity();
			}
			break;
		case (R.id.yyxlist_Img3):
		case (R.id.yyxlist_Img3LL):
			String reserve3 = myApp.getReserve3Mark();
			String open3 = reserve3.substring(0, 2);

			if (open3.equals("ff")) {
				closeReserve3();
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

			jsxLL.getChildAt(1).setVisibility(View.GONE);
			jsxLL.setVisibility(View.INVISIBLE);
			cloudInfoLL.setVisibility(View.GONE);
			cloudTag.setVisibility(View.GONE);
			reserveLL.setVisibility(View.VISIBLE);

			// callSetTempName = null;
			setPanelState("jsx");

			int curTemp = Integer.parseInt(trueTemp.getText().toString());
			int setTemp = Integer.parseInt(myApp.getTempMark());
			if (curTemp > setTemp) {
				String oldMark1 = myApp.getRefStateMark();
				myApp.setRefStateMark(oldMark1.substring(0, 4) + "10"
						+ oldMark1.substring(6, 8));
			} else {
				String oldMark2 = myApp.getRefStateMark();
				myApp.setRefStateMark(oldMark2.substring(0, 4) + "01"
						+ oldMark2.substring(6, 8));
			}

			String oldMark = myApp.getRefModeMark();
			myApp.setRefModeMark(oldMark.substring(0, 8) + "1"
					+ oldMark.substring(9, 10) + "0"
					+ oldMark.substring(11, 16));
			setMainState(true);
			break;
		case (R.id.setFaceBtn):
		case (R.id.setFaceImg):
		case (R.id.setFaceTitle):
		case (R.id.setFaceLL):
			intent = new Intent();
			intent.setClass(DemoActivity.this, SetFaceActivity.class);
			startActivity(intent);

			break;
		case (R.id.setLinkBtn):
		case (R.id.setLinkImg):
		case (R.id.setLinkTitle):
		case (R.id.setLinkLL):
			intent = new Intent();
			intent.setClass(DemoActivity.this, SetLinkActivity.class);
			startActivity(intent);

			break;
		case (R.id.setVersionBtn):
		case (R.id.setVersionImg):
		case (R.id.setVersionTitle):
		case (R.id.setVersionLL):
			intent = new Intent();
			intent.setClass(DemoActivity.this, SetVersionActivity.class);
			startActivity(intent);

			break;
		case (R.id.setStartBtn):
		case (R.id.setStartImg):
		case (R.id.setStartTitle):
		case (R.id.setStartLL):
			intent = new Intent();
			intent.setClass(DemoActivity.this, SetStartActivity.class);
			startActivity(intent);

			break;
		case (R.id.setRefreshBtn):
		case (R.id.setRefreshImg):
		case (R.id.setRefreshTitle):
		case (R.id.setRefreshLL):
			intent = new Intent();
			intent.setClass(DemoActivity.this, SetRefreshActivity.class);
			startActivity(intent);

			break;
		case (R.id.setHelpBtn):
		case (R.id.setHelpImg):
		case (R.id.setHelpTitle):
		case (R.id.setHelpLL):
			intent = new Intent();
			intent.setClass(DemoActivity.this, SetHelpActivity.class);
			startActivity(intent);

			break;
		case (R.id.setErrBtn):
		case (R.id.setErrImg):
		case (R.id.setErrTitle):
		case (R.id.setErrLL):
			intent = new Intent();
			intent.setClass(DemoActivity.this, SetErrActivity.class);
			startActivity(intent);

			break;
		case (R.id.setUserBtn):
		case (R.id.setUserImg):
		case (R.id.setUserTitle):
		case (R.id.setUserLL):
			intent = new Intent();
			intent.setClass(DemoActivity.this, SetUserActivity.class);
			startActivity(intent);

			break;
		}
	}

	private void closeReserve1() {
		String reserve1 = myApp.getReserve1Mark();
		String vStr1 = reserve1.substring(2);
		reserve1Img.setImageResource(R.drawable.v5_3_0_profile_arrow_back);
		myApp.setReserve1Mark("00" + vStr1);
		myApp.getReserveList().set(0, null);
		String oldMark = myApp.getRefModeMark();
		myApp.setRefModeMark(oldMark.substring(0, 9) + "0"
				+ oldMark.substring(10, 16));

		reserveInfoTV1.setVisibility(View.GONE);
		yyxLL.setVisibility(View.GONE);

		if (reserveInfoTV1.getVisibility() == View.GONE
				&& reserveInfoTV2.getVisibility() == View.GONE
				&& reserveInfoTV3.getVisibility() == View.GONE) {
			noReserveInfoTV.setVisibility(View.VISIBLE);
			yyxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			isYYXBtnOpen = false;
			isHeat = true;
			isHeating = false;
			isReserve = false;
			updateHeatingTag();
			updateStateInfo();
		}
	}

	private void closeReserve2() {
		String reserve2 = myApp.getReserve2Mark();
		String vStr2 = reserve2.substring(2);

		reserve2Img.setImageResource(R.drawable.v5_3_0_profile_arrow_back);
		myApp.setReserve2Mark("00" + vStr2);
		myApp.getReserveList().set(1, null);
		String oldMark = myApp.getRefModeMark();
		myApp.setRefModeMark(oldMark.substring(0, 9) + "0"
				+ oldMark.substring(10, 16));

		reserveInfoTV2.setVisibility(View.GONE);
		yyxLL.setVisibility(View.GONE);

		if (reserveInfoTV1.getVisibility() == View.GONE
				&& reserveInfoTV2.getVisibility() == View.GONE
				&& reserveInfoTV3.getVisibility() == View.GONE) {
			noReserveInfoTV.setVisibility(View.VISIBLE);
			yyxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			isYYXBtnOpen = false;
			isHeat = true;
			isHeating = false;
			isReserve = false;
			updateHeatingTag();
			updateStateInfo();
		}
	}

	private void closeReserve3() {
		String reserve3 = myApp.getReserve3Mark();
		String vStr3 = reserve3.substring(2);

		reserve3Img.setImageResource(R.drawable.v5_3_0_profile_arrow_back);
		myApp.setReserve3Mark("00" + vStr3);
		myApp.getReserveList().set(2, null);
		String oldMark = myApp.getRefModeMark();
		myApp.setRefModeMark(oldMark.substring(0, 9) + "0"
				+ oldMark.substring(10, 16));

		reserveInfoTV3.setVisibility(View.GONE);
		yyxLL.setVisibility(View.GONE);

		if (reserveInfoTV1.getVisibility() == View.GONE
				&& reserveInfoTV2.getVisibility() == View.GONE
				&& reserveInfoTV3.getVisibility() == View.GONE) {
			noReserveInfoTV.setVisibility(View.VISIBLE);
			yyxBtn.setBackgroundResource(R.drawable.bg_btn_normal);
			isYYXBtnOpen = false;
			isHeat = true;
			isHeating = false;
			isReserve = false;
			updateHeatingTag();
			updateStateInfo();
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
		intent.setClass(DemoActivity.this, ReserveSetActivity.class);
		startActivity(intent);
	}

	// 封装即时洗列表
	public void setJSXList() {
		jsxListLL = (LinearLayout) ((ScrollView) jsxLL.getChildAt(0))
				.getChildAt(0);
		jsxListLL.removeAllViews();
		String curMode = myApp.getCurModeName();

		jsxListLL.addView(setJSXTabItem("top", "sel", "单人洗",
				curMode.equals("单人洗") ? true : false));
		jsxListLL.addView(setJSXTabItem("middle", "sel", "多人洗",
				curMode.equals("多人洗") ? true : false));
		jsxListLL.addView(setJSXTabItem("middle", "sel", "极速洗",
				curMode.equals("极速洗") ? true : false));
		jsxListLL.addView(setJSXTabItem("bottom", "open", "自设温度", false));
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
		if (curMode.equals("单人洗")) {
			myApp.setTempMark("65");
		} else if (curMode.equals("多人洗")) {
			myApp.setTempMark("75");
		} else if (curMode.equals("极速洗")) {
			myApp.setTempMark("50");
		}
		myApp.setCurModeName(curMode);

		jsxLL.setVisibility(View.INVISIBLE);
		isJSXBtnOpen = true;

		// callSetTempName = null;
		setPanelState("jsx");

		int curTemp = Integer.parseInt(trueTemp.getText().toString());
		int setTemp = Integer.parseInt(myApp.getTempMark());
		if (curTemp > setTemp) {
			String oldMark1 = myApp.getRefStateMark();
			myApp.setRefStateMark(oldMark1.substring(0, 4) + "10"
					+ oldMark1.substring(6, 8));
		} else {
			String oldMark2 = myApp.getRefStateMark();
			myApp.setRefStateMark(oldMark2.substring(0, 4) + "01"
					+ oldMark2.substring(6, 8));
		}

		String oldMark = myApp.getRefModeMark();
		myApp.setRefModeMark(oldMark.substring(0, 8) + "1"
				+ oldMark.substring(9, 10) + "0" + oldMark.substring(11, 16));
		setMainState(true);
	}

	// 打开云智能加载定时器
	private void openCloudLoadTimer() {
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			public void run() {
				handler.removeCallbacks(this);

				// 要做的事情
				cloudLL.setVisibility(View.GONE);
				heatingTimeLL.setVisibility(View.VISIBLE);
				slip2LL.setVisibility(View.VISIBLE);
				cloudInfoLL.setVisibility(View.VISIBLE);
				myApp.setTempMark(cloudTempTV.getText().toString());
				// myApp.setRefModeMark("0000000010100000");

				int curTemp = Integer.parseInt(myApp.getRefSJWDMark());
				int setTemp = Integer.parseInt(myApp.getTempMark());
				if (curTemp > setTemp) {
					String oldMark1 = myApp.getRefStateMark();
					myApp.setRefStateMark(oldMark1.substring(0, 4) + "10"
							+ oldMark1.substring(6, 8));
				} else {
					String oldMark2 = myApp.getRefStateMark();
					myApp.setRefStateMark(oldMark2.substring(0, 4) + "01"
							+ oldMark2.substring(6, 8));
				}

				String oldMark = myApp.getRefModeMark();
				myApp.setRefModeMark(oldMark.substring(0, 8) + "0"
						+ oldMark.substring(9, 10) + "1"
						+ oldMark.substring(11, 16));

				myApp.setCurModeName("云智能");

				// callSetTempName = null;
				// isJSXBtnOpen = false;
				// isCloudBtnOpen = true;
				// setPanelState("cloud");
				setMainState(true);

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
	private void openTempChangeTimer() {
		if (handlerForDemoTemp != null) {
			handlerForDemoTemp.removeCallbacks(runnableForDemoTemp);
			handlerForDemoTemp = null;
			if (runnableForDemoTemp != null)
				runnableForDemoTemp = null;
		}

		handlerForDemoTemp = new Handler();
		runnableForDemoTemp = new Runnable() {
			public void run() {
				int curTemp = Integer.parseInt(myApp.getRefSJWDMark());
				int setTemp = Integer.parseInt(myApp.getTempMark());

				// 要做的事情
				if (curTemp < setTemp) {
					myApp.setRefSJWDMark(String.valueOf(curTemp + 1));
					updateTrueTemp(false);

					int time = setTemp - curTemp--;

					if (time < 60) {
						myApp.setRefHeatingTimeHMark("00");
						myApp.setRefHeatingTimeMMark((time < 10) ? ("0" + String
								.valueOf(time)) : String.valueOf(time));

					} else {
						myApp.setRefHeatingTimeHMark("01");
						myApp.setRefHeatingTimeMMark((time < 10) ? "0"
								+ String.valueOf(time - 60) : String
								.valueOf(time - 60));
					}

					updateHotTime();

					handlerForDemoTemp.postDelayed(this, 1000);
				} else if (curTemp > setTemp) {
					myApp.setRefSJWDMark(String.valueOf(curTemp - 1));
					updateTrueTemp(false);

					int time = curTemp++ - setTemp;

					if (time < 60) {
						myApp.setRefHeatingTimeHMark("00");
						myApp.setRefHeatingTimeMMark((time < 10) ? ("0" + String
								.valueOf(time)) : String.valueOf(time));

					} else {
						myApp.setRefHeatingTimeHMark("01");
						myApp.setRefHeatingTimeMMark((time < 10) ? "0"
								+ String.valueOf(time - 60) : String
								.valueOf(time - 60));
					}

					updateHotTime();

					handlerForDemoTemp.postDelayed(this, 1000);
				} else {
					handlerForDemoTemp.removeCallbacks(this);

					isHeat = true;
					isHeating = false;
					updateHeatingTag();
					updateStateInfo();
					myApp.setRefHeatingTimeHMark("00");
					myApp.setRefHeatingTimeMMark("00");
					updateHotTime();
				}
			}
		};
		handlerForDemoTemp.postDelayed(runnableForDemoTemp, 1000);
	}

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

	public void setMainState(boolean hasOpen) {
		if (hasOpen) {
			isReserve = false;

			// 解析刷新返回的指令码，展示不同的view
			// /String stateStr = "";
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
			char modec6 = modeCArr[6]; // 极速洗
			char modec5 = modeCArr[5]; // 半胆速热
			char modec4 = modeCArr[4]; // 整胆速热
			char modec3 = modeCArr[3];// 夏季模式
			char modec2 = modeCArr[2];// 冬季模式
			char modec1 = modeCArr[1];// 节能模式
			char modec0 = modeCArr[0];// 峰谷夜电
			// 001 1人洗；010 2人洗；011 3人洗；100 老人洗；101 成人洗；110 儿童洗
			String modeFr = String.valueOf(modeCArr[13])
					+ String.valueOf(modeCArr[14])
					+ String.valueOf(modeCArr[15]);
			// 01智洁排水；10一键排水；11暖风
			String modeJk = String.valueOf(modeCArr[11])
					+ String.valueOf(modeCArr[12]);
			boolean isCloudMode = (modeCArr[10] == '1') ? true : false;
			boolean isReserveMode = (modeCArr[9] == '1') ? true : false;
			boolean isJSXMode = (modeCArr[8] == '1') ? true : false;
			if (modec7 == '1') {
				myApp.setCurModeName("E+增容");
			} else if (modec6 == '1') {
				myApp.setCurModeName("极速洗");
			} else if (modec5 == '1') {
				myApp.setCurModeName("半胆速热");
			} else if (modec4 == '1') {
				myApp.setCurModeName("整胆速热");
			} else if (modec3 == '1') {
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
			}
			if (isJSXMode) {
				isJSXBtnOpen = true;
			}
			if (isReserveMode) {
				isYYXBtnOpen = true;

				if (!isCloudMode && !isJSXMode) {
					isHeat = false;
					isHeating = false;
					isReserve = true;
				}
			}

			if (c0 == '0') {
				isClose = true;
			} else if (c0 == '1') {
				isClose = false;
			}

			initReserveInfo();
			updateOpenBtn();
			updateOpenSetPageBtn();
			updateTrueTemp(true);
			updateHeatingTag();
			updateStateInfo();
			updateHotWarter();
			updateHotTime();
			updateOpeBtn();

			if (!isClose && !isReserve)
				openTempChangeTimer();

			setJSXList();
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

			cloudBtn.setBackgroundResource(R.drawable.bg_skin_btn);
			cloudBtn.getBackground().setLevel(myApp.getFaceLevel());

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

		int curTemp = Integer.parseInt(trueTemp.getText().toString());
		int setTemp = Integer.parseInt(stateTempValueTV.getText().toString());
		if (curTemp < setTemp) {
			isHeating = true;
			isHeat = false;
		} else if (curTemp > setTemp) {
			isHeating = false;
			isHeat = true;
		}

		updateHeatingTag();
		updateStateInfo();
		updateHotTime();

		openTempChangeTimer();
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
				// DialogInterface.OnClickListener listener = new
				// DialogInterface.OnClickListener() {
				// public void onClick(DialogInterface dialog, int which) {
				// switch (which) {
				// case AlertDialog.BUTTON1:// "确认"按钮退出程序
				// MyTool.closeSureAD();
				// myApp.exit(false);
				// break;
				// case AlertDialog.BUTTON2:// "取消"第二个按钮取消对话框
				// MyTool.closeSureAD();
				// dialog.cancel();
				// break;
				// default:
				// break;
				// }
				// }
				// };
				//
				// MyTool.openAlertDialog(DemoActivity.this, "系统提示", "确定要退出吗",
				// "确定", "取消", listener, listener, false);

				MyTool.closeSureAD();
				intent = new Intent();
				intent.setClass(DemoActivity.this, SelModeActivity.class);
				startActivity(intent);
				DemoActivity.this.finish();
			}
			return false;
		}
		return false;
	}

	/****************** 控件更新 *****************/
	private void updateOpenBtn() {
		if (isClose) {
			if (handlerForDemoTemp != null) {
				handlerForDemoTemp.removeCallbacks(runnableForDemoTemp);
				handlerForDemoTemp = null;
			}

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

		if (!isChangeStyle)
			return;

		if (isClose) {
			trueTempTitle1.setTextColor(closeColor);
			trueTempYitle2.setTextColor(closeColor);
			trueTemp.setTextColor(closeColor);
			trueTemptag.setTextColor(closeColor);
		} else {
			trueTempTitle1.setTextColor(openColor);
			trueTempYitle2.setTextColor(openColor);
			trueTemp.setTextColor(openColor);
			trueTemptag.setTextColor(openColor);
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
			stateTag.setBackgroundResource(R.drawable.bg_heatstate_noact);
			stateTitleTV.setTextColor(closeColor);
			stateTitleTV.setText(R.string.panel_heatstate7);
			stateTempValueTV.setVisibility(View.GONE);
			stateTempTagTV.setVisibility(View.GONE);
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

			if (isReserve) {
				stateTitleTV.setText("预约等待中...");

				if (handlerForDemoTemp != null) {
					handlerForDemoTemp.removeCallbacks(runnableForDemoTemp);
					handlerForDemoTemp = null;
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
				hotwarterProbar.setProgress(Integer.parseInt(myApp
						.getRefRSLMark()));
				setTVFlash();
			} else {
				hotwarterTitleTV.setTextColor(openColor);
				hotwarterValueTV.setTextColor(openColor);

				hotwarterValueTV.setText(myApp.getRefRSLMark() + "%");
				hotwarterProbar.setProgress(Integer.parseInt(myApp
						.getRefRSLMark()));

				hotwarterNoTV.setVisibility(View.GONE);
				hotwarterTitleTV.setVisibility(View.VISIBLE);
				hotwarterValueTV.setVisibility(View.VISIBLE);
			}
		}
	}

	private void updateHotTime() {
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

			if (myApp.getRefHeatingTimeHMark().equals("00")) {
				heatingTimeHourTV.setVisibility(View.INVISIBLE);
				heatingTimeHourTagTV.setVisibility(View.INVISIBLE);
			} else {
				heatingTimeHourTV.setText(myApp.getRefHeatingTimeHMark());
				heatingTimeHourTV.setVisibility(View.VISIBLE);
				heatingTimeHourTagTV.setVisibility(View.VISIBLE);
			}
			heatingTimeMinuteTV.setText(myApp.getRefHeatingTimeMMark());

			if (!isHeat && !isHeating && isReserve) {
				heatingTimeTitle.setText(R.string.panel_heatingTimeForReserve);
				heatingTimeHourTV.setVisibility(View.VISIBLE);
				heatingTimeHourTagTV.setVisibility(View.VISIBLE);
				heatingTimeHourTV.setText("6");
				heatingTimeMinuteTV.setText("30");
			} else {
				heatingTimeTitle.setText(R.string.panel_heatingTimeForHeating);
			}
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
			} else {
				noReserveInfoTV.setVisibility(View.GONE);
				if (reserve1Info != null) {
					reserveInfoTV1.setVisibility(View.VISIBLE);
					reserveInfoTV1.setText(reserve1Info);
				}
				if (reserve2Info != null) {
					reserveInfoTV2.setVisibility(View.VISIBLE);
					reserveInfoTV2.setText(reserve2Info);
				}
				if (reserve3Info != null) {
					reserveInfoTV3.setVisibility(View.VISIBLE);
					reserveInfoTV3.setText(reserve3Info);
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
}
