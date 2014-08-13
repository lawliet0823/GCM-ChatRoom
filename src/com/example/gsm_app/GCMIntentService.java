package com.example.gsm_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	
	Context context;
	Intent intent;
	Handler handler = new Handler();

	public GCMIntentService() {
		super(ChatRoom.Sender_ID);
	}

	protected void onRegistered(Context context, String registrationId) {
	}

	protected void onUnregistered(Context context, String registrationId) {
	}

	protected void onMessage(Context context, Intent intent) {
		this.context = context;
		this.intent = intent;
		handler.post(new Runnable() {
			@Override
			public void run() {
				String broadcastString = "Service";
				Intent intentService = new Intent(broadcastString);
				Bundle message = GCMIntentService.this.intent.getExtras();
				intentService.putExtras(message);
				intentService.setAction("Service");
				sendBroadcast(intentService);
			}
		});
	}

	public void onError(Context context, String errorID) {
	}
}
