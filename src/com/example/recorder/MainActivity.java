package com.example.recorder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	private Button movie;
	private Button setting;
	private Button folder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		movie = (Button)findViewById(R.id.Movie);
        movie.setOnClickListener(this);
        
        setting = (Button)findViewById(R.id.Setting);
        setting.setOnClickListener(this);
        
        folder = (Button)findViewById(R.id.Folder);
        folder.setOnClickListener(this);
        
      
	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		switch (v.getId()) {
		case R.id.Movie:
			/*
			Intent intent = new Intent(this,SampleView.class);
			startActivity(intent);
			Log.e("click", "click");
			*/
			RecTimer mRecTimer=new RecTimer(getApplicationContext());
			mRecTimer.setTimer(SampleView.class,10*1000,0);
			
			
			break;
		    
		case R.id.Setting:
			
			break;
			
		case R.id.Folder:
			Intent intent2 = new Intent(this,Thumbnail.class);
			startActivity(intent2);
			Log.e("click", "click");
		}
	}
}
