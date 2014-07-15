package com.assignment.android;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.assignment.android.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ClockActivity extends Activity {

	private final class TimerRunnable implements Runnable {
		@Override
		public void run() {
			long current = new Date().getTime();
			currentNetworkTime = timeGetNetworkServer + (current - timeGetNetworkServer);
			if(current - timeGetNetworkServer >= TIME_TO_SYN_SERVER_TIME){
				requestServerTime();				
			}
			showCurrentNetworkTime();
			runTimer();
		}
	}
	private static final int TIME_TO_SYN_SERVER_TIME = 10*60*1000;
	long networkTime = 0;
	long currentNetworkTime = 0;
	long timeGetNetworkServer =0;
	Handler timerHandler = new Handler();
	TimerRunnable timerRunnable = new TimerRunnable();
	private boolean isReqestingTime = false;
	
	
	private void showCurrentNetworkTime() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		 ((TextView)findViewById(R.id.txtNormalClock)).setText(df.format(currentNetworkTime));
		 Log.v("now", df.format(currentNetworkTime));
		 
		 Date currentNetworkDate = new Date(currentNetworkTime);
		 ((ClockView)findViewById(R.id.analogClock)).setTime(currentNetworkDate.getHours(),
				 currentNetworkDate.getMinutes(), currentNetworkDate.getSeconds());
		 findViewById(R.id.analogClock).invalidate();
		 long remainTimeSync = TIME_TO_SYN_SERVER_TIME - (new Date().getTime() - timeGetNetworkServer);
		  
		 ((TextView)findViewById(R.id.txtCountDownSyn)).setText(df.format(remainTimeSync));
		 Log.v("remainTimeSync", df.format(remainTimeSync));
	}
	
	private void runTimer() {
		timerHandler.postDelayed(timerRunnable, 1000);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		requestServerTime();
		((Button)findViewById(R.id.btnSync)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestServerTime();
			}
		});
	}

	private void requestServerTime() {
		if(isReqestingTime){
			return;
		}
		isReqestingTime  = true;
		AsyncTask<Void, Void, Long> requestTimeTask = new AsyncTask<Void, Void, Long>() {
			@Override
			protected Long doInBackground(Void... params) {
				timeGetNetworkServer = new Date().getTime();
				Log.v("request server", new Date().toString());
				SntpClient client = new SntpClient();				 
				 if (client.requestTime("0.ubuntu.pool.ntp.org", 30000)) {
					 networkTime = client.getNtpTime() + SystemClock.elapsedRealtime() - client.getNtpTimeReference();
				  }
				 currentNetworkTime = networkTime;
				 timeGetNetworkServer = new Date().getTime();
				 
				 return networkTime;
			}
			
			@Override
			protected void onPostExecute(Long now) {			
				super.onPostExecute(now);
				showCurrentNetworkTime();
				runTimer();
				isReqestingTime = false;
			}			
		};
		requestTimeTask.execute();
	}
}