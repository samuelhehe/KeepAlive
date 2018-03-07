package cn.samuelnotes.keepalive.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.coolerfall.daemon.Daemon;


/**
 * 这个类的主要作用
 * 1.启动保活另一个保活进程来设定间隔一段60分钟唤醒该service
 * 2.保证第三方推送存活
 */
public class DaemonService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("Daemon", "onCreate in Daemon service");
//		Daemon.run(this, DaemonService.class, Daemon.INTERVAL_ONE_MINUTE * 2);
		/**
		 *  https://github.com/Coolerfall/Android-AppDaemon
		 */
		Daemon.run(this, DaemonService.class, Daemon.INTERVAL_ONE_MINUTE);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}



	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		/* do something here */

        Log.d("daemon", "onStartCommand of service time : "+ System.currentTimeMillis()/1000L);
//  	  这里可以添加第三方推送的receiver 的相互唤醒模块
//		  这里可以检查pushservice是否挂掉

//        PushManager pushManager = new PushManager(getApplicationContext());
//        pushManager.checkPushService();

        Log.d("daemon", "onStartCommand of service init pushservice  end ");



		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("daemon", "onDestroy of service");
	}


}
