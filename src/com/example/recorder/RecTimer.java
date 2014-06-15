package com.example.recorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class RecTimer extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		intent.setClass(context,SampleView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Log.e("Intent","SampleView");
		context.startActivity(intent);
	}

}
