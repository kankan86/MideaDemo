package meta.midea.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;

import meta.midea.MyApplication;
import meta.midea.SelLinkType;
import meta.midea.SelModeActivity;
import meta.midea.SelWifi;
import meta.midea.data.LoadData;
import meta.midea.data.MySocketClient;
import meta.midea.login.IntLoginActivity;
import meta.midea.login.MyServerListActivity;
import meta.midea.login.RegisterActivity;
import meta.midea.main.MainActivity;
import meta.midea.main.ReserveSetActivity;
import meta.midea.set.SetErrActivity;
import meta.midea.set.SetFaceActivity;
import meta.midea.set.SetHelpActivity;
import meta.midea.set.SetUserActivity;
import meta.midea.set.SetVersionActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
	// 定义个一个Tag标签
	private static final String TAG = "MyService";
	// 这里定义吧一个Binder类，用在onBind()有方法里，这样Activity那边可以获取到
	private MyBinder mBinder = new MyBinder();

	private MulticastSocket ms;
	private MyApplication myApp;
	private NotificationManager notificationManager;

	// private ChannelFuture lastWriteFuture = null;
	// private Channel channel;
	// private ClientBootstrap bootstrap;

	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	// 网络状态改变的广播
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Log.d("mark", "网络状态已经改变");
				connectivityManager = (ConnectivityManager)

				getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					String name = info.getTypeName();
					Log.d("mark", "当前网络名称：" + name);
					sendNoNetwork(true);

					if (!LoadData.isWifi) {
						openSocket();
						// try {
						// run(myApp.getSockHostForWebSocket(),
						// myApp.getSocketPortForWebSocket());
						// } catch (IOException e) {
						// e.printStackTrace();
						// }
					}
				} else {
					Log.d("mark", "没有可用网络");
					sendNoNetwork(false);
				}
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		Log.e(TAG, "start IBinder~~~");
		return mBinder;
	}

	@Override
	public void onCreate() {
		Log.e(TAG, "start onCreate~~~");
		super.onCreate();

		myApp = MyApplication.getInstance();
		myApp.setMyService(this);

		// 注册网络状态改变广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, filter);

		if (!LoadData.isWifi) {
			openSocket();
			// try {
			// run(myApp.getSockHostForWebSocket(),
			// myApp.getSocketPortForWebSocket());
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
	}

	// public void run(String host, int port) throws IOException {
	// // Configure the client.
	// bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
	// Executors.newCachedThreadPool(),
	// Executors.newCachedThreadPool()));
	//
	// // Configure the pipeline factory.
	// bootstrap.setPipelineFactory(new SecureChatClientPipelineFactory(this));
	// bootstrap.setOption("tcpNoDelay", true);
	// bootstrap.setOption("keepAlive", true);
	//
	// // Start the connection attempt.
	// ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
	// port));
	// future.awaitUninterruptibly();
	// // Wait until the connection attempt succeeds or fails.
	// channel = future.awaitUninterruptibly().getChannel();
	// if (!future.isSuccess()) {
	// future.getCause().printStackTrace();
	// bootstrap.releaseExternalResources();
	// return;
	// }
	//
	// // 发送登陆消息
	// myApp.setChannel(channel);
	// channel.write("[\"login\":\"" + myApp.getUsername() + "\"]" + "\r\n");
	// }

	@Override
	public void onStart(Intent intent, int startId) {
		Log.e(TAG, "start onStart~~~");
		super.onStart(intent, startId);

		myApp = MyApplication.getInstance();

		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					setClientMS(myApp.getGroup(), myApp.getPort());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "start onDestroy~~~");
		super.onDestroy();

		unregisterReceiver(mReceiver);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.e(TAG, "start onUnbind~~~");
		return super.onUnbind(intent);
	}

	public void stop() {
		stopSelf();
	}

	public class MyBinder extends Binder {
		public MyService getService() {
			return MyService.this;
		}
	}

	// 监听组播数据
	private void setClientMS(String groupIP, int serverPort) {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		MulticastLock multicastLock = wifiManager
				.createMulticastLock("multicast.test");
		multicastLock.acquire();

		try {
			ms = new MulticastSocket(serverPort);
			ms.setLoopbackMode(true);
			ms.joinGroup(InetAddress.getByName(groupIP));
			ms.setSoTimeout(0); // set timeout

			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			while (true) { // 一直监听
				// 将UDP收到的消息存放在dp当中
				ms.receive(dp);
				// 打印获取到的消息
				System.out.println("UDP接收到的测试信息"
						+ new String(dp.getData()).trim());
				LoadData.setMSData(new String(dp.getData()).trim());
			}
		} catch (Exception e) {
		}
	}

	public void openNotification(String tickerT, String isErr, Class<?> act) {
		notificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(); // 定义消息
		notification.icon = getResources().getIdentifier("icon", "drawable",
				getApplicationInfo().packageName); // 获取应用程序下的图标资源文件作为任务栏图标
		notification.tickerText = tickerT; // 显示的消息
		// notification.sound = Uri.parse("android.resource://"
		// + getPackageName()
		// + "/"
		// + getResources().getIdentifier("msg", "raw",
		// getApplicationInfo().packageName)); // 设置语音为/res/raw下的MP3文件
		notification.defaults = Notification.DEFAULT_SOUND; // 设置为系统默认提示音
		notification.flags = Notification.FLAG_AUTO_CANCEL;

		Intent intent = new Intent(MyService.this, act); // 设置任务栏拉下点击的链接

		Bundle bundle = new Bundle();
		bundle.putString("isErr", isErr);
		bundle.putString("eventInfo", tickerT);

		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent contentIntent = PendingIntent.getActivity(MyService.this,
				0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(MyService.this, "点击查看", tickerT,
				contentIntent);
		notificationManager.notify(0, notification);
	}

	public void closeNotification() {
		if (notificationManager == null)
			return;
		notificationManager.cancel(0);
	}

	private void sendNoNetwork(boolean isHas) {
		myApp.setNoNetwork(isHas);

		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
		RunningTaskInfo cinfo = runningTasks.get(0);
		ComponentName component = cinfo.topActivity;
		Log.e("当前活动的Activity", component.getClassName());

		String actName = component.getClassName();
		if (actName.indexOf("SelModeActivity") > -1) {
			SelModeActivity.setNoNetwork(isHas);
		} else if (actName.indexOf("SelLinkType") > -1) {
			SelLinkType.setNoNetwork(isHas);
		} else if (actName.indexOf("SelWifi") > -1) {
			SelWifi.setNoNetwork(isHas);
		} else if (actName.indexOf("IntLoginActivity") > -1) {
			IntLoginActivity.setNoNetwork(isHas);
		} else if (actName.indexOf("RegisterActivity") > -1) {
			RegisterActivity.setNoNetwork(isHas);
		} else if (actName.indexOf("MyServerListActivity") > -1) {
			MyServerListActivity.setNoNetwork(isHas);
		} else if (actName.indexOf("MainActivity") > -1) {
			MainActivity.setNoNetwork(isHas);
		} else if (actName.indexOf("ReserveSetActivity") > -1) {
			ReserveSetActivity.setNoNetwork(isHas);
		} else if (actName.indexOf("SetFaceActivity") > -1) {
			SetFaceActivity.setNoNetwork(isHas);
		} else if (actName.indexOf("SetHelpActivity") > -1) {
			SetHelpActivity.setNoNetwork(isHas);
		} else if (actName.indexOf("SetErrActivity") > -1) {
			SetErrActivity.setNoNetwork(isHas);
		} else if (actName.indexOf("SetVersionActivity") > -1) {
			SetVersionActivity.setNoNetwork(isHas);
		} else if (actName.indexOf("SetUserActivity") > -1) {
			SetUserActivity.setNoNetwork(isHas);
		}

	}

	/************** socket监听 **************/
	private Thread mySocketThread;
	public MySocketClient mySocket;

	public boolean openSocket() {
		if (mySocket != null && mySocket.isConnect)
			return true;

		mySocket = new MySocketClient(myApp.getSockHost(),
				myApp.getSocketPort(), myApp);

		if (mySocket == null || !mySocket.isConnect) {
			return false;
		} else {
			mySocketThread = new Thread(mySocket);
			mySocketThread.start();
			return true;
		}
	}

	public void closeSocket() {
		if (mySocket != null) {
			try {
				mySocket.socket.shutdownInput();
				mySocket.socket.shutdownOutput();

				mySocket.in.close();
				mySocket.out.close();

				mySocket.socket.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}

			mySocket = null;
		}

		if (mySocketThread != null) {
			mySocketThread.interrupt();
			mySocketThread = null;
		}
	}
}