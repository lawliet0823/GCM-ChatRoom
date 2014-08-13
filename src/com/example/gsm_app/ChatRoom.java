package com.example.gsm_app;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.gcm_app.R;
import com.google.android.gcm.GCMRegistrar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatRoom extends Activity {

	public final static String Sender_ID = "678505194911";
	public final static int Message = 1;
	public EditText edtMessag;
	public Button btnSend;
	public TextView txtShow;
	public String IP;
	public String Port;
	public String ID;
	public String dataString;
	private String registerID;
	public String BROATCAST_STRING = "Servive";
	public Socket socket;
	protected static final int ABC = 0;
	public Handler handler;
	private Calendar mCalendar;
	private SimpleDateFormat df;
	private String time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);

		initControl();
		btnSend.setOnClickListener(btnMessageClick);

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		final String regID = GCMRegistrar.getRegistrationId(this);

		if (regID.equals("")) {
			GCMRegistrar.register(this, Sender_ID);
		} else {
			Log.i("Chatroom_regID", regID);
		}
		Log.i("Sender", Sender_ID);
		registerID = regID;

		IntentFilter mFilter01 = new IntentFilter(BROATCAST_STRING);
		registerReceiver(mBroadcast, mFilter01);

		Thread serverThread = new Thread(new serverConnect());
		serverThread.start();

		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ABC: {
					mCalendar = Calendar.getInstance();
					df = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
					time = df.format(mCalendar.getTime());
					txtShow.setText(time + " " + txtShow.getText().toString()
							+ "\n" + dataString);
					break;
				}
				}
				super.handleMessage(msg);
			}
		};

		registerReceiver(mBroadcast, new IntentFilter("Service"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_room, menu);
		return true;
	}

	public void initControl() {
		edtMessag = (EditText) findViewById(R.id.edtMessage);
		btnSend = (Button) findViewById(R.id.btnID);
		txtShow = (TextView) findViewById(R.id.txtShow);

		Bundle bundleChat = this.getIntent().getExtras();
		IP = bundleChat.getString("IP");
		ID = bundleChat.getString("ID");
		Port = bundleChat.getString("Port");
	}

	class serverConnect implements Runnable {
		public void run() {
			try {
				socket = new Socket(InetAddress.getByName(IP),
						Integer.parseInt(Port));
				DataOutputStream writer = new DataOutputStream(
						socket.getOutputStream());
				writer.writeUTF(registerID);

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	Button.OnClickListener btnMessageClick = new Button.OnClickListener() {
		public void onClick(View v) {
			try {
				Log.i("Button Click", "Button");
				DataOutputStream messageWriter = new DataOutputStream(
						socket.getOutputStream());
				messageWriter.writeUTF(ID + " "
						+ edtMessag.getText().toString());
			} catch (IOException e) { // TODO Auto-generated catch block
										// e.printStackTrace();
			}
		}
	};

	private BroadcastReceiver mBroadcast = new BroadcastReceiver() {

		String broadcastString = "Service";

		public void onReceive(Context mcontext, Intent mIntent) {
			if (broadcastString.equals(mIntent.getAction())) {
				Bundle messageBundle = mIntent.getExtras();
				dataString = messageBundle.getString("Message");
				Log.i("dataString", dataString);
				Message message = new Message();
				message.what = ABC;
				ChatRoom.this.handler.sendMessage(message);
			}
		}
	};

}
