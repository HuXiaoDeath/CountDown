package com.duan.countdown;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private TextView num_down;
	private Button button_again;
	private EditText num_syst_again;
	private Intent intent;
	private CountBroadcase receiver;
	private ExecutorService executorService; // 线程池
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				int numCount = (Integer) msg.obj;
				if(numCount <1){
					numCount = 0;
				}
					num_down.setText(numCount + "");
				
				break;

			default:
				break;
			}
		}
	};
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		intent = new Intent(MainActivity.this, CountService.class);

		serviceBrodact();

	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		num_down = (TextView) findViewById(R.id.num_down);
		button_again = (Button) findViewById(R.id.button_again);
		num_syst_again = (EditText) findViewById(R.id.num_syst_again);
		button_again.setOnClickListener(this);
	}

	/**
	 * 发送服务并监听广播
	 */
	private void serviceBrodact() {
		startService(intent);
		receiver = new CountBroadcase();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.duan.countdown.CountService");
		MainActivity.this.registerReceiver(receiver, filter);
		executorService = Executors.newCachedThreadPool();
	}

	
	private class CountBroadcase extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			final int count = bundle.getInt("count");

			executorService.submit(new Thread(new Runnable() {

				@Override
				public void run() {
					Message msg = Message.obtain();
					msg.obj = count;
					msg.what = 1;
					mHandler.sendMessage(msg);

				}
			}));
			
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_again:
			String numString = num_syst_again.getText().toString().trim();
			if (TextUtils.isEmpty(numString)) {
				Toast.makeText(MainActivity.this, "数字不能为空", 0).show();
				return;
			}
			stopService(intent);
			intent.putExtra("countContro", Integer.valueOf(numString));
			startService(intent);
			num_down.setText(numString + "");
			num_syst_again.setText("");
			break;

		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(intent);
	}

}
