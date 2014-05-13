package meta.midea.main;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import meta.midea.MyApplication;
import meta.midea.R;
import meta.midea.data.LoadData;
import meta.midea.service.MyReceiver;
import meta.midea.tool.MyTool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReserveSetActivity extends Activity implements OnClickListener {
	private MyApplication myApp;

	private Intent intent;

	private ImageView yyxSetbackBtn;
	private TextView yyxSetCancleBtn;
	private TextView yyxSetSaveBtn;
	private TextView yyxSetTempTitleTV;
	private TextView yyxSetTimeTitleTV;
	private Button yyxSetTempUpBtn;
	private Button yyxSetTempDownBtn;
	private TextView yyxSetTempValueTV;
	private EditText yyxSetTempValueET;
	private Button yyxSetHourUpBtn;
	private Button yyxSetHourDownBtn;
	private TextView yyxSetHourValueTV;
	private EditText yyxSetHourValueET;
	private Button yyxSetMinuteUpBtn;
	private Button yyxSetMinuteDownBtn;
	private TextView yyxSetMinuteValueTV;
	private EditText yyxSetMinuteValueET;
	private TextView yyxSetWeek1Btn;
	private TextView yyxSetWeek2Btn;
	private TextView yyxSetWeek3Btn;
	private TextView yyxSetWeek4Btn;
	private TextView yyxSetWeek5Btn;
	private TextView yyxSetWeek6Btn;
	private TextView yyxSetWeek7Btn;
	private ImageView yyxSetWeekLoop;
	private ImageView yyxSetSingleLoop;
	private TextView yyxSetWeekLoopTV;
	private TextView yyxSetSingleLoopTV;
	private RelativeLayout yyxSetWeekLoopRL;
	private RelativeLayout yyxSetSingleLoopRL;

	private boolean isWeek1Sel;
	private boolean isWeek2Sel;
	private boolean isWeek3Sel;
	private boolean isWeek4Sel;
	private boolean isWeek5Sel;
	private boolean isWeek6Sel;
	private boolean isWeek7Sel;

	private boolean isLoopWeekSel;
	private boolean isLoopSingleSel;

	private Handler handlerForTempUp;
	private Runnable runnableForTempUp;
	private Handler handlerForTempDown;
	private Runnable runnableForTempDown;

	private Handler handlerForHourUp;
	private Runnable runnableForHourUp;
	private Handler handlerForHourDown;
	private Runnable runnableForHourDown;

	private Handler handlerForMinuteUp;
	private Runnable runnableForMinuteUp;
	private Handler handlerForMinuteDown;
	private Runnable runnableForMinuteDown;

	private long downTime;
	private long upTime;

	private int curX;
	private int curY;
	private int curW;
	private int curH;

	private int curDayIndex; // 当天周几
	private MyReceiver receiver;
	private boolean isSaving = false;// 记录是否点击存储按钮
	private boolean isBack = false;// 记录是否点击了返回按钮
	private String reserveName = "";// 记录操作的预约

	private static final int Dialog_Load = 1;
	private static final int Dialog_Sure = 2;
	private static final int Dialog_Close = 3;

	private static RelativeLayout noNetworkRL;
	private Button noNetworkBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.panel_yyxset);

		myApp = MyApplication.getInstance();
		myApp.addActivity(this);

		Calendar calendar = Calendar.getInstance();
		curDayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		System.out.println("当天 curDayIndex==" + curDayIndex);

		yyxSetbackBtn = (ImageView) findViewById(R.id.yyxset_backBtn);
		yyxSetCancleBtn = (TextView) findViewById(R.id.yyxset_canclebtn);
		yyxSetSaveBtn = (TextView) findViewById(R.id.yyxset_savebtn);
		yyxSetTempTitleTV = (TextView) findViewById(R.id.yyxset_tempTitle);
		TextPaint paint1 = yyxSetTempTitleTV.getPaint();
		paint1.setFakeBoldText(true);
		yyxSetTimeTitleTV = (TextView) findViewById(R.id.yyxset_timeTitle);
		TextPaint paint2 = yyxSetTimeTitleTV.getPaint();
		paint2.setFakeBoldText(true);
		yyxSetTempUpBtn = (Button) findViewById(R.id.yyxset_tempUpBtn);
		yyxSetTempDownBtn = (Button) findViewById(R.id.yyxset_tempDownBtn);
		yyxSetTempValueTV = (TextView) findViewById(R.id.yyxset_tempValue_TV);
		yyxSetTempValueET = (EditText) findViewById(R.id.yyxset_tempValue_ET);
		yyxSetHourUpBtn = (Button) findViewById(R.id.yyxset_hourUpBtn);
		yyxSetHourDownBtn = (Button) findViewById(R.id.yyxset_hourDownBtn);
		yyxSetHourValueTV = (TextView) findViewById(R.id.yyxset_hourValue_TV);
		yyxSetHourValueET = (EditText) findViewById(R.id.yyxset_hourValue_ET);
		yyxSetMinuteUpBtn = (Button) findViewById(R.id.yyxset_minuteUpBtn);
		yyxSetMinuteDownBtn = (Button) findViewById(R.id.yyxset_minuteDownBtn);
		yyxSetMinuteValueTV = (TextView) findViewById(R.id.yyxset_minuteValue_TV);
		yyxSetMinuteValueET = (EditText) findViewById(R.id.yyxset_minuteValue_ET);
		yyxSetWeek1Btn = (TextView) findViewById(R.id.yyxset_week1);
		yyxSetWeek2Btn = (TextView) findViewById(R.id.yyxset_week2);
		yyxSetWeek3Btn = (TextView) findViewById(R.id.yyxset_week3);
		yyxSetWeek4Btn = (TextView) findViewById(R.id.yyxset_week4);
		yyxSetWeek5Btn = (TextView) findViewById(R.id.yyxset_week5);
		yyxSetWeek6Btn = (TextView) findViewById(R.id.yyxset_week6);
		yyxSetWeek7Btn = (TextView) findViewById(R.id.yyxset_week7);
		yyxSetWeekLoop = (ImageView) findViewById(R.id.yyxset_loopweekBtn);
		yyxSetSingleLoop = (ImageView) findViewById(R.id.yyxset_loopsingleBtn);
		yyxSetWeekLoopTV = (TextView) findViewById(R.id.yyxset_loopweekTitle);
		yyxSetWeekLoopRL = (RelativeLayout) findViewById(R.id.yyxset_loopweekLL);
		yyxSetSingleLoopTV = (TextView) findViewById(R.id.yyxset_loopsingleTitle);
		yyxSetSingleLoopRL = (RelativeLayout) findViewById(R.id.yyxset_loopsingleLL);

		yyxSetbackBtn.setOnClickListener(this);
		yyxSetCancleBtn.setOnClickListener(this);
		yyxSetSaveBtn.setOnClickListener(this);
		yyxSetTempUpBtn.setOnClickListener(this);
		yyxSetTempDownBtn.setOnClickListener(this);
		yyxSetTempValueTV.setOnClickListener(this);
		yyxSetHourUpBtn.setOnClickListener(this);
		yyxSetHourDownBtn.setOnClickListener(this);
		yyxSetHourValueTV.setOnClickListener(this);
		yyxSetMinuteUpBtn.setOnClickListener(this);
		yyxSetMinuteDownBtn.setOnClickListener(this);
		yyxSetMinuteValueTV.setOnClickListener(this);
		yyxSetWeek1Btn.setOnClickListener(this);
		yyxSetWeek2Btn.setOnClickListener(this);
		yyxSetWeek3Btn.setOnClickListener(this);
		yyxSetWeek4Btn.setOnClickListener(this);
		yyxSetWeek5Btn.setOnClickListener(this);
		yyxSetWeek6Btn.setOnClickListener(this);
		yyxSetWeek7Btn.setOnClickListener(this);
		yyxSetWeekLoop.setOnClickListener(this);
		yyxSetSingleLoop.setOnClickListener(this);
		yyxSetWeekLoopTV.setOnClickListener(this);
		yyxSetSingleLoopTV.setOnClickListener(this);
		yyxSetWeekLoopRL.setOnClickListener(this);
		yyxSetSingleLoopRL.setOnClickListener(this);

		yyxSetTempUpBtn.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				yyxSetTempValueET.setVisibility(View.GONE);
				yyxSetTempValueTV.setVisibility(View.VISIBLE);
				yyxSetTempValueTV.setText(yyxSetTempValueET.getText());

				final int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					yyxSetTempUpBtn.setBackgroundResource(R.drawable.up_act);
					downTime = System.currentTimeMillis();
					initTempUp();
					break;
				case MotionEvent.ACTION_UP:
					yyxSetTempUpBtn.setBackgroundResource(R.drawable.up);
					upTime = System.currentTimeMillis();
					if (upTime - downTime < 500) {
						handlerForTempUp.removeCallbacks(runnableForTempUp);
						addTemp();
					} else {
						handlerForTempUp.removeCallbacks(runnableForTempUp);
					}
					break;
				}

				return false;
			}
		});

		yyxSetTempDownBtn.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				yyxSetTempValueET.setVisibility(View.GONE);
				yyxSetTempValueTV.setVisibility(View.VISIBLE);
				yyxSetTempValueTV.setText(yyxSetTempValueET.getText());

				final int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					yyxSetTempDownBtn
							.setBackgroundResource(R.drawable.down_act);
					downTime = System.currentTimeMillis();
					initTempDown();
					break;
				case MotionEvent.ACTION_UP:
					yyxSetTempDownBtn.setBackgroundResource(R.drawable.down);
					upTime = System.currentTimeMillis();

					if (upTime - downTime < 500) {
						handlerForTempDown.removeCallbacks(runnableForTempDown);
						subTemp();
					} else {
						handlerForTempDown.removeCallbacks(runnableForTempDown);
					}
					break;
				}

				return false;
			}
		});

		yyxSetHourUpBtn.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				yyxSetHourValueET.setVisibility(View.GONE);
				yyxSetHourValueTV.setVisibility(View.VISIBLE);
				yyxSetHourValueTV.setText(yyxSetHourValueET.getText());

				final int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					yyxSetHourUpBtn.setBackgroundResource(R.drawable.up_act);
					downTime = System.currentTimeMillis();
					initHourUp();
					break;
				case MotionEvent.ACTION_UP:
					yyxSetHourUpBtn.setBackgroundResource(R.drawable.up);
					upTime = System.currentTimeMillis();
					if (upTime - downTime < 500) {
						handlerForHourUp.removeCallbacks(runnableForHourUp);
						addHour();
					} else {
						handlerForHourUp.removeCallbacks(runnableForHourUp);
					}
					break;
				}

				return false;
			}
		});

		yyxSetHourDownBtn.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				yyxSetHourValueET.setVisibility(View.GONE);
				yyxSetHourValueTV.setVisibility(View.VISIBLE);
				yyxSetHourValueTV.setText(yyxSetHourValueET.getText());

				final int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					yyxSetHourDownBtn
							.setBackgroundResource(R.drawable.down_act);
					downTime = System.currentTimeMillis();
					initHourDown();
					break;
				case MotionEvent.ACTION_UP:
					yyxSetHourDownBtn.setBackgroundResource(R.drawable.down);
					upTime = System.currentTimeMillis();

					if (upTime - downTime < 500) {
						handlerForHourDown.removeCallbacks(runnableForHourDown);
						subHour();
					} else {
						handlerForHourDown.removeCallbacks(runnableForHourDown);
					}
					break;
				}

				return false;
			}
		});

		yyxSetMinuteUpBtn.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				yyxSetMinuteValueET.setVisibility(View.GONE);
				yyxSetMinuteValueTV.setVisibility(View.VISIBLE);
				yyxSetMinuteValueTV.setText(yyxSetMinuteValueET.getText());

				final int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					yyxSetMinuteUpBtn.setBackgroundResource(R.drawable.up_act);
					downTime = System.currentTimeMillis();
					initMinuteUp();
					break;
				case MotionEvent.ACTION_UP:
					yyxSetMinuteUpBtn.setBackgroundResource(R.drawable.up);
					upTime = System.currentTimeMillis();
					if (upTime - downTime < 500) {
						handlerForMinuteUp.removeCallbacks(runnableForMinuteUp);
						addMinute();
					} else {
						handlerForMinuteUp.removeCallbacks(runnableForMinuteUp);
					}
					break;
				}

				return false;
			}
		});

		yyxSetMinuteDownBtn.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				yyxSetMinuteValueET.setVisibility(View.GONE);
				yyxSetMinuteValueTV.setVisibility(View.VISIBLE);
				yyxSetMinuteValueTV.setText(yyxSetMinuteValueET.getText());

				final int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					yyxSetMinuteDownBtn
							.setBackgroundResource(R.drawable.down_act);
					downTime = System.currentTimeMillis();
					initMinuteDown();
					break;
				case MotionEvent.ACTION_UP:
					yyxSetMinuteDownBtn.setBackgroundResource(R.drawable.down);
					upTime = System.currentTimeMillis();

					if (upTime - downTime < 500) {
						handlerForMinuteDown
								.removeCallbacks(runnableForMinuteDown);
						subMinute();
					} else {
						handlerForMinuteDown
								.removeCallbacks(runnableForMinuteDown);
					}
					break;
				}

				return false;
			}
		});

		yyxSetTempValueET.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				// if (hasFocus)
				yyxSetTempValueET.selectAll();
			}
		});

		yyxSetHourValueET.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				// if (hasFocus)
				yyxSetHourValueET.selectAll();
			}
		});

		yyxSetMinuteValueET
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					public void onFocusChange(View v, boolean hasFocus) {
						// if (hasFocus)
						yyxSetMinuteValueET.selectAll();
					}
				});

		setDataForReserve();
		regReceiver();
		setFaceLevel();
		initNoNetwork();
	}

	private void initNoNetwork() {
		noNetworkRL = (RelativeLayout) findViewById(R.id.yyxset_noNetwork);
		noNetworkBtn = (Button) findViewById(R.id.yyxset_noNetworkBtn);
		setNoNetwork(myApp.isNoNetwork());

		noNetworkBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ReserveSetActivity.this,
						NoNetworkActivity.class);
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

	protected void onResume() {
		super.onResume();
		setFaceLevel();
	}

	private void setFaceLevel() {
		RelativeLayout pageHead = (RelativeLayout) findViewById(R.id.yyxset_head);
		pageHead.getBackground().setLevel(myApp.getFaceLevel());
	}

	private void initTempUp() {
		handlerForTempUp = new Handler();
		runnableForTempUp = new Runnable() {
			public void run() {
				String curTempValue = yyxSetTempValueTV.getText().toString();
				if (curTempValue.equals("75")) {
					curTempValue = "25";
				}
				int newTempValue = Integer.parseInt(curTempValue) + 5;

				yyxSetTempValueTV.setText(newTempValue + "");
				yyxSetTempValueET.setText(newTempValue + "");

				handlerForTempUp.postDelayed(this, 200);
			}
		};
		handlerForTempUp.postDelayed(runnableForTempUp, 500);
	}

	private void initTempDown() {
		handlerForTempDown = new Handler();
		runnableForTempDown = new Runnable() {
			public void run() {
				String curTempValue = yyxSetTempValueTV.getText().toString();
				if (curTempValue.equals("30")) {
					curTempValue = "80";
				}
				int newTempValue = Integer.parseInt(curTempValue) - 5;

				yyxSetTempValueTV.setText(newTempValue + "");
				yyxSetTempValueET.setText(newTempValue + "");

				handlerForTempDown.postDelayed(this, 200);
			}
		};
		handlerForTempDown.postDelayed(runnableForTempDown, 500);
	}

	private void addTemp() {
		String curTempValue1 = yyxSetTempValueTV.getText().toString();
		int newTempValue1 = Integer.parseInt(curTempValue1) + 5;
		if (newTempValue1 > 75) {
			newTempValue1 = 30;
		}

		yyxSetTempValueTV.setText(newTempValue1 + "");
		yyxSetTempValueET.setText(newTempValue1 + "");
	}

	private void subTemp() {
		String curTempValue2 = yyxSetTempValueTV.getText().toString();
		int newTempValue2 = Integer.parseInt(curTempValue2) - 5;
		if (newTempValue2 < 30) {
			newTempValue2 = 75;
		}

		yyxSetTempValueTV.setText(newTempValue2 + "");
		yyxSetTempValueET.setText(newTempValue2 + "");
	}

	private void initHourUp() {
		handlerForHourUp = new Handler();
		runnableForHourUp = new Runnable() {
			public void run() {
				String curHourValue = yyxSetHourValueTV.getText().toString();
				if (Integer.parseInt(curHourValue) >= 23) {
					curHourValue = "-1";
				}
				int newHourValue = Integer.parseInt(curHourValue) + 1;

				yyxSetHourValueTV.setText((newHourValue < 10 ? "0" : "")
						+ newHourValue + "");
				yyxSetHourValueET.setText((newHourValue < 10 ? "0" : "")
						+ newHourValue + "");

				handlerForHourUp.postDelayed(this, 200);
			}
		};
		handlerForHourUp.postDelayed(runnableForHourUp, 500);
	}

	private void initHourDown() {
		handlerForHourDown = new Handler();
		runnableForHourDown = new Runnable() {
			public void run() {
				String curHourValue = yyxSetHourValueTV.getText().toString();
				if (Integer.parseInt(curHourValue) <= 0) {
					curHourValue = "24";
				}
				int newHourValue = Integer.parseInt(curHourValue) - 1;

				yyxSetHourValueTV.setText((newHourValue < 10 ? "0" : "")
						+ newHourValue + "");
				yyxSetHourValueET.setText((newHourValue < 10 ? "0" : "")
						+ newHourValue + "");

				handlerForHourDown.postDelayed(this, 200);
			}
		};
		handlerForHourDown.postDelayed(runnableForHourDown, 500);
	}

	private void addHour() {
		String curHourValue1 = yyxSetHourValueTV.getText().toString();
		if (Integer.parseInt(curHourValue1) >= 23) {
			curHourValue1 = "-1";
		}

		int newHourValue1 = Integer.parseInt(curHourValue1) + 1;

		yyxSetHourValueTV.setText((newHourValue1 < 10 ? "0" : "")
				+ newHourValue1 + "");
		yyxSetHourValueET.setText((newHourValue1 < 10 ? "0" : "")
				+ newHourValue1 + "");
	}

	private void subHour() {
		String curHourValue2 = yyxSetHourValueTV.getText().toString();
		if (Integer.parseInt(curHourValue2) <= 0) {
			curHourValue2 = "24";
		}

		int newHourValue2 = Integer.parseInt(curHourValue2) - 1;

		yyxSetHourValueTV.setText((newHourValue2 < 10 ? "0" : "")
				+ newHourValue2 + "");
		yyxSetHourValueET.setText((newHourValue2 < 10 ? "0" : "")
				+ newHourValue2 + "");
	}

	private void initMinuteUp() {
		handlerForMinuteUp = new Handler();
		runnableForMinuteUp = new Runnable() {
			public void run() {
				String curMinuteValue = yyxSetMinuteValueTV.getText()
						.toString();
				if (Integer.parseInt(curMinuteValue) >= 59) {
					curMinuteValue = "-1";
				}
				int newMinuteValue = Integer.parseInt(curMinuteValue) + 1;

				yyxSetMinuteValueTV.setText((newMinuteValue < 10 ? "0" : "")
						+ newMinuteValue + "");
				yyxSetMinuteValueET.setText((newMinuteValue < 10 ? "0" : "")
						+ newMinuteValue + "");

				handlerForMinuteUp.postDelayed(this, 200);
			}
		};
		handlerForMinuteUp.postDelayed(runnableForMinuteUp, 500);
	}

	private void initMinuteDown() {
		handlerForMinuteDown = new Handler();
		runnableForMinuteDown = new Runnable() {
			public void run() {
				String curMinuteValue = yyxSetMinuteValueTV.getText()
						.toString();
				if (Integer.parseInt(curMinuteValue) <= 0) {
					curMinuteValue = "60";
				}
				int newMinuteValue = Integer.parseInt(curMinuteValue) - 1;

				yyxSetMinuteValueTV.setText((newMinuteValue < 10 ? "0" : "")
						+ newMinuteValue + "");
				yyxSetMinuteValueET.setText((newMinuteValue < 10 ? "0" : "")
						+ newMinuteValue + "");

				handlerForMinuteDown.postDelayed(this, 200);
			}
		};
		handlerForMinuteDown.postDelayed(runnableForMinuteDown, 500);
	}

	private void addMinute() {
		String curMinuteValue1 = yyxSetMinuteValueTV.getText().toString();
		if (Integer.parseInt(curMinuteValue1) >= 59) {
			curMinuteValue1 = "-1";
		}

		int newMinuteValue1 = Integer.parseInt(curMinuteValue1) + 1;

		yyxSetMinuteValueTV.setText((newMinuteValue1 < 10 ? "0" : "")
				+ newMinuteValue1 + "");
		yyxSetMinuteValueET.setText((newMinuteValue1 < 10 ? "0" : "")
				+ newMinuteValue1 + "");
	}

	private void subMinute() {
		String curMinuteValue2 = yyxSetMinuteValueTV.getText().toString();
		if (Integer.parseInt(curMinuteValue2) <= 0) {
			curMinuteValue2 = "60";
		}

		int newMinuteValue2 = Integer.parseInt(curMinuteValue2) - 1;

		yyxSetMinuteValueTV.setText((newMinuteValue2 < 10 ? "0" : "")
				+ newMinuteValue2 + "");
		yyxSetMinuteValueET.setText((newMinuteValue2 < 10 ? "0" : "")
				+ newMinuteValue2 + "");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case (R.id.yyxset_backBtn):
			if (isBack)
				return;

			isBack = true;
			intent = new Intent();
			if (myApp.isDemoMode()) {
				intent.setClass(ReserveSetActivity.this, DemoActivity.class);
			} else {
				intent.setClass(ReserveSetActivity.this, MainActivity.class);
			}
			startActivity(intent);
			unregisterReceiver(receiver);
			ReserveSetActivity.this.finish();
			break;
		case (R.id.yyxset_canclebtn):
			if (isBack)
				return;

			// 如果当前预约已保存，点击“取消”则关闭该预约
			// 如果当前预约未保存，点击“取消”则返回主页面
			String curReserveSet = myApp.getCurReserveSet();
			String curReserveMark;
			if (curReserveSet.equals("1")) {
				curReserveMark = myApp.getReserve1Mark().substring(0, 2);
				if (curReserveMark.toLowerCase().equals("ff")) {
					if (myApp.isDemoMode()) {
						myApp.setReserve1Mark("0075140000000000");
						isBack = true;
						intent = new Intent();
						intent.setClass(ReserveSetActivity.this,
								DemoActivity.class);
						startActivity(intent);
						unregisterReceiver(receiver);
						ReserveSetActivity.this.finish();
					} else {
						setReserveClose("1", "0075140000000000");
					}
				} else {
					isBack = true;
					intent = new Intent();
					if (myApp.isDemoMode()) {
						intent.setClass(ReserveSetActivity.this,
								DemoActivity.class);
					} else {
						intent.setClass(ReserveSetActivity.this,
								MainActivity.class);
					}
					startActivity(intent);
					unregisterReceiver(receiver);
					ReserveSetActivity.this.finish();
				}
			} else if (curReserveSet.equals("2")) {
				curReserveMark = myApp.getReserve2Mark().substring(0, 2);
				if (curReserveMark.toLowerCase().equals("ff")) {
					if (myApp.isDemoMode()) {
						myApp.setReserve2Mark("0075080000000000");
						isBack = true;
						intent = new Intent();
						intent.setClass(ReserveSetActivity.this,
								DemoActivity.class);
						startActivity(intent);
						unregisterReceiver(receiver);
						ReserveSetActivity.this.finish();
					} else {
						setReserveClose("2", "0075080000000000");
					}
				} else {
					isBack = true;
					intent = new Intent();
					if (myApp.isDemoMode()) {
						intent.setClass(ReserveSetActivity.this,
								DemoActivity.class);
					} else {
						intent.setClass(ReserveSetActivity.this,
								MainActivity.class);
					}
					startActivity(intent);
					unregisterReceiver(receiver);
					ReserveSetActivity.this.finish();
				}
			} else if (curReserveSet.equals("3")) {
				curReserveMark = myApp.getReserve3Mark().substring(0, 2);
				if (curReserveMark.toLowerCase().equals("ff")) {
					if (myApp.isDemoMode()) {
						myApp.setReserve3Mark("0075140000000000");

						isBack = true;
						intent = new Intent();
						intent.setClass(ReserveSetActivity.this,
								DemoActivity.class);
						startActivity(intent);
						unregisterReceiver(receiver);
						ReserveSetActivity.this.finish();
					} else {
						setReserveClose("3", "0075140000000000");
					}
				} else {
					isBack = true;
					intent = new Intent();
					if (myApp.isDemoMode()) {
						intent.setClass(ReserveSetActivity.this,
								DemoActivity.class);
					} else {
						intent.setClass(ReserveSetActivity.this,
								MainActivity.class);
					}
					startActivity(intent);
					unregisterReceiver(receiver);
					ReserveSetActivity.this.finish();
				}
			}

			break;
		case (R.id.yyxset_savebtn):
			if (!isSaving)
				saveReserveInfo();
			break;
		// case (R.id.yyxset_tempValue_TV):
		// yyxSetTempValueET.setVisibility(View.VISIBLE);
		// yyxSetTempValueTV.setVisibility(View.GONE);
		// // yyxSetTempValueET.setFocusable(true);
		// // yyxSetTempValueET.selectAll();
		// break;
		// case (R.id.yyxset_hourValue_TV):
		// yyxSetHourValueTV.setVisibility(View.GONE);
		// yyxSetHourValueET.setVisibility(View.VISIBLE);
		// // yyxSetHourValueET.setFocusable(true);
		// break;
		// case (R.id.yyxset_minuteValue_TV):
		// yyxSetMinuteValueTV.setVisibility(View.GONE);
		// yyxSetMinuteValueET.setVisibility(View.VISIBLE);
		// // yyxSetMinuteValueET.setFocusable(true);
		// break;
		case (R.id.yyxset_week1):
			if (isWeek1Sel) {
				((LinearLayout) yyxSetWeek1Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle);
				isWeek1Sel = false;
			} else {
				((LinearLayout) yyxSetWeek1Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
				isWeek1Sel = true;
			}
			break;
		case (R.id.yyxset_week2):
			if (isWeek2Sel) {
				((LinearLayout) yyxSetWeek2Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle);
				isWeek2Sel = false;
			} else {
				((LinearLayout) yyxSetWeek2Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
				isWeek2Sel = true;
			}
			break;
		case (R.id.yyxset_week3):
			if (isWeek3Sel) {
				((LinearLayout) yyxSetWeek3Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle);
				isWeek3Sel = false;
			} else {
				((LinearLayout) yyxSetWeek3Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
				isWeek3Sel = true;
			}
			break;
		case (R.id.yyxset_week4):
			if (isWeek4Sel) {
				((LinearLayout) yyxSetWeek4Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle);
				isWeek4Sel = false;
			} else {
				((LinearLayout) yyxSetWeek4Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
				isWeek4Sel = true;
			}
			break;
		case (R.id.yyxset_week5):
			if (isWeek5Sel) {
				((LinearLayout) yyxSetWeek5Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle);
				isWeek5Sel = false;
			} else {
				((LinearLayout) yyxSetWeek5Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
				isWeek5Sel = true;
			}
			break;
		case (R.id.yyxset_week6):
			if (isWeek6Sel) {
				((LinearLayout) yyxSetWeek6Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_right);
				isWeek6Sel = false;
			} else {
				((LinearLayout) yyxSetWeek6Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_right_sel);
				isWeek6Sel = true;
			}
			break;
		case (R.id.yyxset_week7):
			if (isWeek7Sel) {
				((LinearLayout) yyxSetWeek7Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_left);
				isWeek7Sel = false;
			} else {
				((LinearLayout) yyxSetWeek7Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_left_sel);
				isWeek7Sel = true;
			}
			break;
		case (R.id.yyxset_loopweekBtn):
		case (R.id.yyxset_loopweekTitle):
		case (R.id.yyxset_loopweekLL):
			if (isLoopWeekSel) {
			} else {
				yyxSetWeekLoop.setImageResource(R.drawable.checkbox_pressed);
				isLoopWeekSel = true;

				yyxSetSingleLoop
						.setImageResource(R.drawable.checkbox_normal_black);
				isLoopSingleSel = false;
			}
			break;
		case (R.id.yyxset_loopsingleBtn):
		case (R.id.yyxset_loopsingleTitle):
		case (R.id.yyxset_loopsingleLL):
			if (isLoopSingleSel) {
			} else {
				yyxSetSingleLoop.setImageResource(R.drawable.checkbox_pressed);
				isLoopSingleSel = true;

				yyxSetWeekLoop
						.setImageResource(R.drawable.checkbox_normal_black);
				isLoopWeekSel = false;
			}
			break;
		}

		if (v.getId() != R.id.yyxset_tempValue_TV) {
			yyxSetTempValueET.setVisibility(View.GONE);
			yyxSetTempValueTV.setVisibility(View.VISIBLE);
			yyxSetTempValueTV.setText(yyxSetTempValueET.getText());
		}
		if (v.getId() != R.id.yyxset_hourValue_TV) {
			yyxSetHourValueET.setVisibility(View.GONE);
			yyxSetHourValueTV.setVisibility(View.VISIBLE);
			yyxSetHourValueTV.setText(yyxSetHourValueET.getText());
		}
		if (v.getId() != R.id.yyxset_minuteValue_TV) {
			yyxSetMinuteValueET.setVisibility(View.GONE);
			yyxSetMinuteValueTV.setVisibility(View.VISIBLE);
			yyxSetMinuteValueTV.setText(yyxSetMinuteValueET.getText());
		}
	}

	private void setDataForReserve() {
		String curReserveSet = myApp.getCurReserveSet();
		String reserve = "007502000000000001";
		String temp;
		String hour;
		String minute;
		String isLoop;
		String week7;
		String week6;
		String week5;
		String week4;
		String week3;
		String week2;
		String week1;

		if (curReserveSet.equals("1")) {
			reserve = myApp.getReserve1Mark();
		} else if (curReserveSet.equals("2")) {
			reserve = myApp.getReserve2Mark();
		} else if (curReserveSet.equals("3")) {
			reserve = myApp.getReserve3Mark();
		}

		temp = reserve.substring(2, 4);
		hour = reserve.substring(4, 6);
		minute = reserve.substring(6, 8);
		isLoop = reserve.substring(8, 9);
		week6 = reserve.substring(9, 10);
		week5 = reserve.substring(10, 11);
		week4 = reserve.substring(11, 12);
		week3 = reserve.substring(12, 13);
		week2 = reserve.substring(13, 14);
		week1 = reserve.substring(14, 15);
		week7 = reserve.substring(15, 16);

		yyxSetTempValueTV.setText(temp);
		yyxSetTempValueET.setText(temp);
		yyxSetHourValueTV.setText(hour);
		yyxSetHourValueET.setText(hour);
		yyxSetMinuteValueTV.setText(minute);
		yyxSetMinuteValueET.setText(minute);
		if (week1.equals("1")) {
			((LinearLayout) yyxSetWeek1Btn.getParent())
					.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
			isWeek1Sel = true;
		}
		if (week2.equals("1")) {
			((LinearLayout) yyxSetWeek2Btn.getParent())
					.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
			isWeek2Sel = true;
		}
		if (week3.equals("1")) {
			((LinearLayout) yyxSetWeek3Btn.getParent())
					.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
			isWeek3Sel = true;
		}
		if (week4.equals("1")) {
			((LinearLayout) yyxSetWeek4Btn.getParent())
					.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
			isWeek4Sel = true;
		}
		if (week5.equals("1")) {
			((LinearLayout) yyxSetWeek5Btn.getParent())
					.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
			isWeek5Sel = true;
		}
		if (week6.equals("1")) {
			((LinearLayout) yyxSetWeek6Btn.getParent())
					.setBackgroundResource(R.drawable.bg_yyxweek_right_sel);
			isWeek6Sel = true;
		}
		if (week7.equals("1")) {
			((LinearLayout) yyxSetWeek7Btn.getParent())
					.setBackgroundResource(R.drawable.bg_yyxweek_left_sel);
			isWeek7Sel = true;
		}

		if (!isWeek1Sel && !isWeek2Sel && !isWeek3Sel && !isWeek4Sel
				&& !isWeek5Sel && !isWeek6Sel && !isWeek7Sel) {
			if (curDayIndex == 2) {
				((LinearLayout) yyxSetWeek1Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
				isWeek1Sel = true;
			}
			if (curDayIndex == 3) {
				((LinearLayout) yyxSetWeek2Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
				isWeek2Sel = true;
			}
			if (curDayIndex == 4) {
				((LinearLayout) yyxSetWeek3Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
				isWeek3Sel = true;
			}
			if (curDayIndex == 5) {
				((LinearLayout) yyxSetWeek4Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
				isWeek4Sel = true;
			}
			if (curDayIndex == 6) {
				((LinearLayout) yyxSetWeek5Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_middle_sel);
				isWeek5Sel = true;
			}
			if (curDayIndex == 7) {
				((LinearLayout) yyxSetWeek6Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_right_sel);
				isWeek6Sel = true;
			}
			if (curDayIndex == 1) {
				((LinearLayout) yyxSetWeek7Btn.getParent())
						.setBackgroundResource(R.drawable.bg_yyxweek_left_sel);
				isWeek7Sel = true;
			}
		}

		if (isLoop.equals("1")) {
			yyxSetWeekLoop.setImageResource(R.drawable.checkbox_pressed);
			isLoopWeekSel = true;

			yyxSetSingleLoop.setImageResource(R.drawable.checkbox_normal_black);
			isLoopSingleSel = false;
		} else if (isLoop.equals("0")) {
			yyxSetSingleLoop.setImageResource(R.drawable.checkbox_pressed);
			isLoopSingleSel = true;

			yyxSetWeekLoop.setImageResource(R.drawable.checkbox_normal_black);
			isLoopWeekSel = false;
		}
	}

	private void saveReserveInfo() {
		if (!isWeek1Sel && !isWeek2Sel && !isWeek3Sel && !isWeek4Sel
				&& !isWeek5Sel && !isWeek6Sel && !isWeek7Sel) {
			MyTool.openSureAD(ReserveSetActivity.this, "提示",
					"您还没有设置预约周期，请选择预约周期...");
			return;
		}

		isSaving = true;
		String curReserveSet = myApp.getCurReserveSet();

		// 封装基本信息
		String temp = yyxSetTempValueTV.getText().toString();
		String hour = yyxSetHourValueTV.getText().toString();
		String minute = yyxSetMinuteValueTV.getText().toString();
		String week = (isLoopWeekSel ? "1" : "0") + (isWeek6Sel ? "1" : "0")
				+ (isWeek5Sel ? "1" : "0") + (isWeek4Sel ? "1" : "0")
				+ (isWeek3Sel ? "1" : "0") + (isWeek2Sel ? "1" : "0")
				+ (isWeek1Sel ? "1" : "0") + (isWeek7Sel ? "1" : "0");
		String[] weekStr = new String[7];
		if (isWeek7Sel) {
			weekStr[6] = "日";
		}
		if (isWeek6Sel) {
			weekStr[5] = "六";
		}
		if (isWeek5Sel) {
			weekStr[4] = "五";
		}
		if (isWeek4Sel) {
			weekStr[3] = "四";
		}
		if (isWeek3Sel) {
			weekStr[2] = "三";
		}
		if (isWeek2Sel) {
			weekStr[1] = "二";
		}
		if (isWeek1Sel) {
			weekStr[0] = "一";
		}

		String reserveMark = temp + hour + minute + week;

		// 封装基本信息列表，用来比对，小于40分钟给予提示
		final Map<String, Object> addMap = new HashMap<String, Object>();
		addMap.put("tempStr", temp);
		addMap.put("hourStr", hour);
		addMap.put("minuteStr", minute);
		addMap.put("weekStr", weekStr);

		// 判断设置时间是否有冲突
		String returnStr = isReserveVal(temp, hour, minute, weekStr,
				Integer.parseInt(curReserveSet) - 1);
		if (returnStr == null) {
			if (curReserveSet.equals("1")) {
				myApp.setReserve1Change(true);
				myApp.getReserveList().set(0, addMap);

				myApp.setReserve1Mark("ff" + reserveMark);

				if (myApp.isDemoMode()) {
					String oldMark = myApp.getRefModeMark();
					myApp.setRefModeMark(oldMark.substring(0, 8) + "010"
							+ oldMark.substring(11, 16));

					intent = new Intent();
					intent.setClass(ReserveSetActivity.this, DemoActivity.class);
					startActivity(intent);
					unregisterReceiver(receiver);
					ReserveSetActivity.this.finish();
				} else {
					setReserve("1", myApp.getReserve1Mark());
				}
			} else if (curReserveSet.equals("2")) {
				myApp.setReserve2Change(true);
				myApp.getReserveList().set(1, addMap);

				myApp.setReserve2Mark("ff" + reserveMark);

				if (myApp.isDemoMode()) {
					String oldMark = myApp.getRefModeMark();
					myApp.setRefModeMark(oldMark.substring(0, 8) + "010"
							+ oldMark.substring(11, 16));

					intent = new Intent();
					intent.setClass(ReserveSetActivity.this, DemoActivity.class);
					startActivity(intent);
					unregisterReceiver(receiver);
					ReserveSetActivity.this.finish();
				} else {
					setReserve("2", myApp.getReserve2Mark());
				}
			} else if (curReserveSet.equals("3")) {
				myApp.setReserve3Change(true);
				myApp.getReserveList().set(2, addMap);

				myApp.setReserve3Mark("ff" + reserveMark);

				if (myApp.isDemoMode()) {
					String oldMark = myApp.getRefModeMark();
					myApp.setRefModeMark(oldMark.substring(0, 8) + "010"
							+ oldMark.substring(11, 16));

					intent = new Intent();
					intent.setClass(ReserveSetActivity.this, DemoActivity.class);
					startActivity(intent);
					unregisterReceiver(receiver);
					ReserveSetActivity.this.finish();
				} else {
					setReserve("3", myApp.getReserve3Mark());
				}
			}

		} else {
			if (curReserveSet.equals("1")) {
				myApp.setReserve1Change(false);
			} else if (curReserveSet.equals("2")) {
				myApp.setReserve2Change(false);
			} else if (curReserveSet.equals("3")) {
				myApp.setReserve3Change(false);
			}

			// 弹出提示框
			isSaving = false;
			AlertDialog.Builder dialogClose = new AlertDialog.Builder(
					ReserveSetActivity.this);
			dialogClose
					.setTitle("提示")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage(returnStr)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();// 取消弹出框
								}
							}).create().show();
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		int[] location = new int[2];

		if (yyxSetTempValueET.getVisibility() == View.VISIBLE) {
			yyxSetTempValueET.getLocationOnScreen(location);
			curX = location[0];
			curY = location[1];
			curW = yyxSetTempValueET.getWidth();
			curH = yyxSetTempValueET.getHeight();

			if (x >= curX && x <= (curX + curW) && y >= curY
					&& y <= (curY + curH)) {
			} else {
				yyxSetTempValueET.setVisibility(View.GONE);
				yyxSetTempValueTV.setVisibility(View.VISIBLE);
				yyxSetTempValueTV.setText(yyxSetTempValueET.getText());
			}
		}
		if (yyxSetHourValueET.getVisibility() == View.VISIBLE) {
			yyxSetHourValueET.getLocationOnScreen(location);
			curX = location[0];
			curY = location[1];
			curW = yyxSetHourValueET.getWidth();
			curH = yyxSetHourValueET.getHeight();

			if (x >= curX && x <= (curX + curW) && y >= curY
					&& y <= (curY + curH)) {
			} else {
				yyxSetHourValueET.setVisibility(View.GONE);
				yyxSetHourValueTV.setVisibility(View.VISIBLE);
				yyxSetHourValueTV.setText(yyxSetHourValueET.getText());
			}
		}
		if (yyxSetMinuteValueET.getVisibility() == View.VISIBLE) {
			yyxSetMinuteValueET.getLocationOnScreen(location);
			curX = location[0];
			curY = location[1];
			curW = yyxSetMinuteValueET.getWidth();
			curH = yyxSetMinuteValueET.getHeight();

			if (x >= curX && x <= (curX + curW) && y >= curY
					&& y <= (curY + curH)) {
			} else {
				yyxSetMinuteValueET.setVisibility(View.GONE);
				yyxSetMinuteValueTV.setVisibility(View.VISIBLE);
				yyxSetMinuteValueTV.setText(yyxSetMinuteValueET.getText());
			}
		}

		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			intent = new Intent();
			if (myApp.isDemoMode()) {
				intent.setClass(ReserveSetActivity.this, DemoActivity.class);
			} else {
				intent.setClass(ReserveSetActivity.this, MainActivity.class);
			}
			startActivity(intent);
			unregisterReceiver(receiver);
			ReserveSetActivity.this.finish();
			return false;
		}
		return false;
	}

	/********** 数据处理函数 ***********/
	// 判断预约模式间隔，增加提示内容
	private String isReserveVal(String tempStr, String hourStr,
			String minuteStr, String[] weekStr, int reserveIndex) {
		String returnStr = null;
		if (myApp.getReserveList() == null)
			return null;

		System.out.println("myApp.reserveList.size()=="
				+ myApp.getReserveList().size());
		String oldTemp = null;
		String oldHour = null;
		String oldMinute = null;
		String equelWeek = null;
		for (int i = 0; i < myApp.getReserveList().size(); i++) {
			System.out.println("|||||" + i + "|||" + reserveIndex);
			if (i == reserveIndex)
				continue;

			System.out.println("*******" + i);
			Map<String, Object> tempMap = (Map<String, Object>) myApp
					.getReserveList().get(i);
			if (tempMap == null)
				continue;
			oldTemp = (String) tempMap.get("tempStr");
			oldHour = (String) tempMap.get("hourStr");
			oldMinute = (String) tempMap.get("minuteStr");
			int tempHour = Integer.parseInt(oldHour);
			int tempMinute = Integer.parseInt(oldMinute);
			String[] tempWeekStr = (String[]) tempMap.get("weekStr");

			for (int j = 0; j < 7; j++) {
				System.out.println("tempWeekStr[j]" + tempWeekStr[j]);
				System.out.println("weekStr[j]" + weekStr[j]);
				if (tempWeekStr[j] != null && weekStr[j] != null
						&& tempWeekStr[j] == weekStr[j]) {
					equelWeek = weekStr[j];
					break;
				} else {
					continue;
				}
			}

			if (equelWeek == null)
				continue;

			int addTime = Integer.parseInt(hourStr) * 60
					+ Integer.parseInt(minuteStr);
			if (Math.abs((tempHour * 60 + tempMinute) - addTime) <= 40) {
				returnStr = "热水器已预约\n";
				returnStr = returnStr + "周" + equelWeek;
				returnStr = returnStr + " " + oldHour + ":" + oldMinute + ","
						+ oldTemp + "°C\n";
				returnStr = returnStr + "不可以再新增预约\n";
				returnStr = returnStr + "周" + equelWeek;
				returnStr = returnStr + " " + hourStr + ":" + minuteStr + ","
						+ tempStr + "°C\n";
				returnStr = returnStr + "温馨提示：\n";
				returnStr = returnStr + "    预约时间间隔不得小于40分钟!";

				return returnStr;
			}
		}

		return null;
	}

	// 设置预约
	public void setReserve(final String name, final String reserveMark) {
		reserveName = name;
		if (!LoadData.isWifi)
			showDialog(Dialog_Load);

		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					System.out.println("********设置预约********");
					if (LoadData.isWifi) {
						msg.obj = LoadData.sendReserveForWifi(reserveName,
								reserveMark, true);
					} else {
						openTimeForSocket();
						msg.obj = LoadData.sendReserveFor3G(reserveName,
								reserveMark, true);
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
						setReserveBack(true);
				} else {
					setReserveBack(false);
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void setReserveBack(boolean result) {
		isSaving = false;
		if (!LoadData.isWifi)
			dismissDialog(Dialog_Load);
		closeTimeForSocket();

		if (result) {
			MyTool.makeText(ReserveSetActivity.this, "设置预约成功");

			myApp.setReserveToMain(true);

			intent = new Intent();
			intent.setClass(ReserveSetActivity.this, MainActivity.class);
			startActivity(intent);
			try {
				if (receiver != null)
					unregisterReceiver(receiver);
			} catch (Exception e) {
				System.out.println(e);
			}
			ReserveSetActivity.this.finish();
		} else {
			showDialog(Dialog_Sure);
		}
	}

	// 取消预约
	public void setReserveClose(final String name, final String reserveMark) {
		reserveName = name;
		if (!LoadData.isWifi)
			showDialog(Dialog_Close);

		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					System.out.println("********取消预约********");
					if (LoadData.isWifi) {
						msg.obj = LoadData.sendReserveForWifi(reserveName,
								reserveMark, false);
					} else {
						openTimeForSocket();
						msg.obj = LoadData.sendReserveFor3G(reserveName,
								reserveMark, false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				handlerForSetReserveClose.sendMessage(msg);
			}
		}.start();
	}

	private Handler handlerForSetReserveClose = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null && (Boolean) msg.obj) {
					if (LoadData.isWifi)
						setReserveCloseBack(true);
				} else {
					setReserveBack(false);
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void setReserveCloseBack(boolean result) {
		isSaving = false;
		if (!LoadData.isWifi)
			dismissDialog(Dialog_Close);
		closeTimeForSocket();

		if (result) {
			MyTool.makeText(ReserveSetActivity.this, "取消预约成功");

			myApp.setReserveToMain(true);
			myApp.setReserveToMainAndClose(true);
			myApp.setReserveToMainCloseName(reserveName);

			intent = new Intent();
			intent.setClass(ReserveSetActivity.this, MainActivity.class);
			startActivity(intent);
			try {
				if (receiver != null)
					unregisterReceiver(receiver);
			} catch (Exception e) {
				System.out.println(e);
			}
			ReserveSetActivity.this.finish();
		} else {
			showDialog(Dialog_Sure);
		}
	}

	private void regReceiver() {
		receiver = new MyReceiver(ReserveSetActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction("meta.midea.ReserveSetActivity");
		registerReceiver(receiver, filter);
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
					setReserveBack(false);
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
		timer = new Timer(true);
		timer.schedule(task, 15000, 999999); // 延时15000ms后执行，999999ms执行一次
	}

	public void closeTimeForSocket() {
		if (timer != null) {
			System.out.println("设置预约socket超时定时器关闭了");
			timer.cancel(); // 退出计时器
			timer = null;
			if (task != null) {
				task.cancel();
			}
		}
	}

	/**************** 对话框 **************/
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case Dialog_Load: {
			ProgressDialog checkUpdateIng = new ProgressDialog(
					ReserveSetActivity.this);
			checkUpdateIng.setMessage("请稍等，正在设置预约" + reserveName + "。。。");
			checkUpdateIng.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			checkUpdateIng.setButton("取消",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dismissDialog(Dialog_Load);
						}
					});
			return checkUpdateIng;
		}
		case Dialog_Close: {
			ProgressDialog checkUpdateIng = new ProgressDialog(
					ReserveSetActivity.this);
			checkUpdateIng.setMessage("请稍等，正在取消预约" + reserveName + "。。。");
			checkUpdateIng.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			checkUpdateIng.setButton("取消",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dismissDialog(Dialog_Close);
						}
					});
			return checkUpdateIng;
		}
		case Dialog_Sure: {
			Dialog dialog = new AlertDialog.Builder(ReserveSetActivity.this)
					.setTitle("提示")
					.setMessage("设置预约失败，请检查热水器工作是否正常!")
					// 设置内容
					.setPositiveButton("确定", // 设置确定按钮
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}

							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.cancel();
								}
							}).create();// 创建
			return dialog;
		}
		default: {
			return null;
		}
		}
	}
}
