package com.example.recorder;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class RecTimer{
	private Context context;
	
	public RecTimer(Context context){
		this.context=context;
	}
	
	
	public <T>void setTimer(Class<T> Activity,int looptime,int serviceID){
		Intent intent =new Intent(context,Activity);
		PendingIntent pendingIntent = PendingIntent.getService(context,serviceID,intent,PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Calendar cal=Calendar.getInstance();
		am.setRepeating(AlarmManager.RTC,cal.getTimeInMillis(),looptime,pendingIntent);
		Log.e("AlarmManager","set");
	}
	
	
	
	
	
	
	
	
	
	
}
