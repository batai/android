package com.example.recorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	private Button movie;
	private Button setting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		movie = (Button)findViewById(R.id.Movie);
        movie.setOnClickListener(this);
        
        setting = (Button)findViewById(R.id.Setting);
        setting.setOnClickListener(this);
        
        
	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		switch (v.getId()) {
		case R.id.Movie:
			Intent intent = new Intent(this,SampleView.class);
			startActivity(intent);
			break;
			
		case R.id.Setting:
			
			break;
		}
	}
	
}