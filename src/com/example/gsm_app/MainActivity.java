package com.example.gsm_app;

import java.io.IOException;

import com.example.gcm_app.R;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gcm.server.Sender;
import com.google.android.gcm.server.Message;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public EditText editIP;
	public EditText editPort;
	public EditText editID;
	public Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initControl();

		button.setOnClickListener(btnLoginOnClick);
		
	}

	public void initControl() {
		editIP = (EditText)findViewById(R.id.edtIP);
		editPort = (EditText)findViewById(R.id.edtPort);
		editID = (EditText)findViewById(R.id.edtUserID);
		button = (Button)findViewById(R.id.btnLogin);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Button.OnClickListener btnLoginOnClick = new Button.OnClickListener()
	{
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ChatRoom.class);
			
			Bundle bundle = new Bundle();
			String IP = editIP.getText().toString();
			String Port = editPort.getText().toString();
			String ID = editID.getText().toString();
			bundle.putString("IP", IP);
			bundle.putString("Port", Port);
			bundle.putString("ID", ID);
			
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};
}
