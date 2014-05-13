package meta.midea.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import meta.midea.MyApplication;
import meta.midea.data.LoadData;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

public class MyExceptionHandler implements UncaughtExceptionHandler {

	private static MyExceptionHandler mHandler;
	private static Context mContext;

	private MyExceptionHandler() {
	}

	public synchronized static MyExceptionHandler getInstance(Context context) {
		if (mHandler == null) {
			mHandler = new MyExceptionHandler();
			mContext = context;
		}
		return mHandler;
	}

	public void uncaughtException(Thread arg0, Throwable ex) {
		try {
			SIMCardInfo siminfo = new SIMCardInfo(mContext);
			String errStr = "time:" + MyTool.getCurTime() + "\n";
			if(siminfo!=null){
				errStr = errStr + "tel:" + siminfo.getNativePhoneNumber() + "\n";
				errStr = errStr + "server:" + siminfo.getProvidersName() + "\n";
			}

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			System.out.println("错误信息" + sw.toString());

			File file = new File("/sdcard/err.txt");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(errStr.getBytes());
			fos.flush();

			fos.write(sw.toString().getBytes());
			fos.flush();

			errStr = errStr + sw.toString() + "\n";

			// 获取手机的版本信息
			Field[] fields = Build.class.getFields();
			for (Field field : fields) {
				field.setAccessible(true); // 暴力反射
				String key = field.getName();
				String value = field.get(null).toString();

				errStr = errStr + key + "=" + value + "\n";
				fos.write((key + "=" + value + "\n").getBytes());
				fos.flush();
			}

			saveErr(errStr);

//			new Thread() {
//				public void run() {
//					Looper.prepare();
//					Toast.makeText(mContext, "应用出错了", 0).show();
//					Looper.loop();
//
//				};
//			}.start();
			new Thread() {
				public void run() {
					try {
						Thread.sleep(12000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					MyApplication.getInstance().exit(true);
					//android.os.Process.killProcess(android.os.Process.myPid());
				};
			}.start();

			// fos.close();
			System.out.println("程序发生了异常,但是被哥捕获了");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveErr(final String errStr) {
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;

				try {
					System.out.println("********关机********");
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
				if (msg.obj != null && (Boolean) msg.obj) {

				} else {
					//Toast.makeText(mContext, "应用出错了", 0).show();
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

}
