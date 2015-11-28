package cn.edu.zjut.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.zjut.studentFitManager.R;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;

public class MainActivity extends Activity {

	TextView text;
	Handler mHandler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);

		text = (TextView) findViewById(R.id.test);

		final Context context = this;

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					text.setText("Connected success");
					break;
				case 0:
					text.setText("Connected failed");
					break;
				}
			}
		};

		new Thread() {
			public void run() {
				try {
					Message message = new Message();
					BandInfo[] pairedBands = BandClientManager.getInstance().getPairedBands();
					BandClient bandClient = BandClientManager.getInstance().create(context, pairedBands[0]);
					BandPendingResult<ConnectionState> pendingResult = bandClient.connect();
					ConnectionState state = pendingResult.await();
					if(state == ConnectionState.CONNECTED) {
						message.what = 1;
					} else {
						message.what = 0;
					}
					mHandler.sendMessage(message);
				} catch (InterruptedException ex) {
					Toast.makeText(context, "InterruptedException", Toast.LENGTH_LONG).show();
				} catch (BandException ex) {
					Toast.makeText(context, "BandException", Toast.LENGTH_LONG).show();
				}
			}
		}.start();
	}

}
