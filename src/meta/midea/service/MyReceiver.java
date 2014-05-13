package meta.midea.service;

import meta.midea.MyApplication;
import meta.midea.data.LoadData;
import meta.midea.main.MainActivity;
import meta.midea.main.ReserveSetActivity;
import meta.midea.tool.MyTool;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MyReceiver extends BroadcastReceiver {
	Activity activty;

	public MyReceiver(Activity activty) {
		this.activty = activty;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		String tagStr = bundle.getString("tag");
		// boolean errStr = bundle.getBoolean("error");
		boolean result = bundle.getBoolean("result");

		// if (errStr) {
		// NotificationManager mgr = (NotificationManager) context
		// .getSystemService(Context.NOTIFICATION_SERVICE);
		// Notification nt = new Notification();
		// nt.defaults = Notification.DEFAULT_SOUND;
		// int soundId = new Random(System.currentTimeMillis())
		// .nextInt(Integer.MAX_VALUE);
		// mgr.notify(soundId, nt);
		// }

		if (tagStr.equals("refreshEvent")) {
			String actName = bundle.getString("activity");
			if (actName.equals("main")
					&& !MyApplication.getInstance().isFirstLoadMain()) {
				((MainActivity) activty).setMainState(false);
			}
		}

		if (tagStr.equals("refreshReserveEvent")) {
			String actName = bundle.getString("activity");
			if (actName.equals("main")) {
				((MainActivity) activty).initReserveInfo();
			}
		}

		if (tagStr.equals("socketError")) {
			String actName = bundle.getString("activity");
			if (actName.equals("main")) {
				if (((MainActivity) activty).timer == null) {
					return;
				}

				String bakcName = ((MainActivity) activty).socketBackName;
				if (bakcName.equals("getType")) {
					((MainActivity) activty).getTypeBack(false);
				}
				if (bakcName.equals("refresh")) {
					((MainActivity) activty).refreshBack(false);
				}
				if (bakcName.equals("open")) {
					((MainActivity) activty).openBack(false);
				}
				if (bakcName.equals("close")) {
					((MainActivity) activty).closeBack(false);
				}
				if (bakcName.equals("setMode")) {
					((MainActivity) activty).setModeBack(false);
				}
				if (bakcName.equals("setReserve")) {
					((MainActivity) activty).setReserveBack(false, "");
				}
				if (bakcName.equals("getReserve")) {
					((MainActivity) activty).getReserveBack(false);
				}

			}
			if (actName.equals("setReserve")) {
				if (((ReserveSetActivity) activty).timer == null) {
					return;
				}

				((ReserveSetActivity) activty).setReserveBack(false);
			}
		}

		if (tagStr.equals("getType")) {
			String actName = bundle.getString("activity");
			if (actName.equals("main")) {
				if (((MainActivity) activty).timer == null) {
					return;
				}
				((MainActivity) activty).getTypeBack(result);
			}
		}

		if (tagStr.equals("refresh")) {
			String actName = bundle.getString("activity");
			if (actName.equals("main")) {
				if (((MainActivity) activty).timer == null) {
					return;
				}
				((MainActivity) activty).refreshBack(result);
			}
		}

		if (tagStr.equals("refreshReserve1")
				|| tagStr.equals("refreshReserve2")
				|| tagStr.equals("refreshReserve3")) {
			String actName = bundle.getString("activity");
			if (actName.equals("main")) {
				System.out.println("((MainActivity) activty).timer=="
						+ ((MainActivity) activty).timer);
				if (((MainActivity) activty).timer == null) {
					return;
				}
				((MainActivity) activty).getReserveBack(result);
			}
		}

		if (tagStr.equals("setReserve1") || tagStr.equals("setReserve2")
				|| tagStr.equals("setReserve3")) {
			String actName = bundle.getString("activity");
			// if (actName.equals("main")) {
			// if (((MainActivity) activty).timer == null) {
			// return;
			// }
			//
			// ((MainActivity) activty).setReserveBack(result,
			// bundle.getString("name"));
			// }
			if (actName.equals("setReserve")) {
				if (((ReserveSetActivity) activty).timer == null) {
					return;
				}
				((ReserveSetActivity) activty).setReserveBack(result);
			}
		}

		if (tagStr.equals("closeReserve1") || tagStr.equals("closeReserve2")
				|| tagStr.equals("closeReserve3")) {
			String actName = bundle.getString("activity");
			if (actName.equals("main")) {
				if (((MainActivity) activty).timer == null) {
					return;
				}

				((MainActivity) activty).setReserveBack(result,
						bundle.getString("name"));
			}
			if (actName.equals("setReserve")) {
				if (((ReserveSetActivity) activty).timer == null) {
					return;
				}
				((ReserveSetActivity) activty).setReserveCloseBack(result);
			}
		}

		if (tagStr.equals("setMode")) {
			String actName = bundle.getString("activity");

			if (actName.equals("main")) {
				if (((MainActivity) activty).timer == null) {
					return;
				}
				((MainActivity) activty).setModeBack(result);
			}
		}

		if (tagStr.equals("open")) {
			String actName = bundle.getString("activity");

			if (actName.equals("main")) {
				if (((MainActivity) activty).timer == null) {
					return;
				}
				((MainActivity) activty).openBack(result);
			}
		}

		if (tagStr.equals("close")) {
			String actName = bundle.getString("activity");

			if (actName.equals("main")) {
				if (((MainActivity) activty).timer == null) {
					return;
				}
				((MainActivity) activty).closeBack(result);
			}
		}

	}

	public void saveErr(final String errStr) {
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					LoadData.isWifi = false;
					msg.obj = LoadData.saveErr(errStr);
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
				MyTool.closeProgressDialog();

				if (msg.obj != null && (Boolean) msg.obj) {
					LoadData.isWifi = true;
				} else {
					// Toast.makeText(mContext, "应用出错了", 0).show();
					LoadData.isWifi = true;
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

}
