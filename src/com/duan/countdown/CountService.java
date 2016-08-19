package com.duan.countdown;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CountService extends Service {

	private int count = 30;
	private boolean mFlag = false;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		count = intent.getIntExtra("countContro", count);
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (!mFlag) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count--;
					if (count > -1) {
						
						Intent intent = new Intent();
						intent.putExtra("count", count);
						intent.setAction("com.duan.countdown.CountService");
						sendBroadcast(intent);
					} else {
						stopSelf();
					}
				}
			}
		}).start();

		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		count = 0;
		mFlag = true;
	}

}

/**
 * private int count = 10; private boolean mFlag = false;
 * 
 * @Override public void onCreate() { super.onCreate(); new Thread(new
 *           Runnable() {
 * @Override public void run() { while (!mFlag) { try { Thread.sleep(1000); }
 *           catch (InterruptedException e) { e.printStackTrace(); } count++;
 * 
 *           Log.i("CountService", "  " + count); Intent intent = new Intent();
 *           intent.putExtra("count", count);
 *           intent.setAction("com.example.zzztest1.service");
 *           sendBroadcast(intent); } } }).start(); }
 * @Override public IBinder onBind(Intent intent) {
 * 
 *           return null; }
 * @Override public void onDestroy() { super.onDestroy(); count = 0; mFlag =
 *           true; Log.i("CountService", " onDestroy "); }
 * 
 */
