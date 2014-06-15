package com.example.recorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SampleView extends Activity implements OnClickListener{
	private String fileName;
	private SView sview;
	private SurfaceHolder holder;
	private MediaRecorder mRecorder;
	private boolean isRecording;

	
	class SView extends SurfaceView implements SurfaceHolder.Callback {
		private final Runnable func=new Runnable(){
			@Override
			public void run(){
				//sview.surfaceDestroyed(holder);
				// 録画を停止
				mRecorder.stop();
				mRecorder.reset();
				Log.e("Sview","stop");
			}
			
		};
		
		SView(Context context) {
			super(context);
			holder = getHolder();
			holder.addCallback(this);
			// プレビューに使うサーフェスはプッシュバッファでなくてはならない
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		
		

		// サーフェスが作成されると呼ばれる
		public void surfaceCreated(SurfaceHolder holder) {
			Log.e("Serface","Created");
			// MediaRecorderのインスタンスを作成
			mRecorder = new MediaRecorder();

			// ビデオ入力ソースをカメラに設定
			mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// プレビューに使用するサーフェスを設定
			mRecorder.setPreviewDisplay(holder.getSurface());
			CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
			mRecorder.setProfile(profile);

			//空き容量が1GByte以下なら削除処理
			if(false){
				deletefiles();
			}

			//スタートボタンを押す直前の時刻による名前付け
			// 現在の時刻を取得
			Date date = new Date();
			// 表示形式を設定
			SimpleDateFormat sdf = new SimpleDateFormat("MMdd_kkmm");
			fileName = "/sdcard/download/" + sdf.format(date) + ".mp4";

			// 出力ファイルのパスを指定
			mRecorder.setOutputFile(fileName);
			//mRecorder.setMaxDuration(10);

			try {
				// レコーダーを準備
				mRecorder.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 録画を開始
			mRecorder.start();
			try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			mRecorder.stop();
			mRecorder.reset();
			mRecorder.release();
			Log.e("Sview","stop");
			//new Handler().postDelayed(func,10*1000);
		}

		// サーフェスが破棄されると呼ばれる
		public void surfaceDestroyed(SurfaceHolder holder) {
			// 録画を停止
			mRecorder.stop();
			mRecorder.reset();
			// 使わなくなった時点でレコーダーリソースを解放する
			mRecorder.release();
		}

		// サーフェスの状態が変化したら呼ばれる
		public void surfaceChanged(SurfaceHolder holder, int format,
				int w, int h) {
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		sview = new SView(this);
		setContentView(sview);
	}

	public int getsize(){
		StatFs sf = new StatFs("/sdcard/");
		int gb = sf.getAvailableBlocks() * sf.getBlockSize();
		//Log.e("getsize1", String.valueOf(gb));
		return gb;
	}
	
	public void deletefiles(){
		//ファイルの作成日時を見て最も古いファイルを削除する
		//(宿題:容量の半分分だけループして削除するようにする)

		String path = Environment.getExternalStorageDirectory().getPath()+"/download";
		//ファイル名一覧の取得
		File dir = new File(path);	
		final File[] files = dir.listFiles();
		//最も古いファイルの検索
		String filename,filedate;
		int filetimelist[]=new int[files.length];
		//日時の文字列を数値列にしてfiletimelistに入れる
		for(int i=0;i<files.length;i++){
			filename=files[i].toString();
			//ファイル名のパース(見直し必要)
			String[] strAry1 = filename.split("/",0);
			String[] strAry2 = strAry1[strAry1.length-1].split("_",0);
			String[] strAry3 = strAry2[1].split("\\.");
			filedate=strAry2[0]+strAry3[0];
			filetimelist[i]=Integer.valueOf(filedate);
		}
		int oldfile=filetimelist[0];
		StringBuilder oldfilename=new StringBuilder();
		//最も古いファイル名(数値)を抽出
		for(int i=0;i<filetimelist.length;i++)
			oldfile=Math.min(oldfile,filetimelist[i]);
		//最も古いファイル名を再構成
		String deletefilename;
		//10月未満の場合の処理
		if(oldfile<10000000){
			oldfilename.append(Integer.toString(oldfile));
			oldfilename.insert(3,"_");
			deletefilename="0"+new String(oldfilename);
		}
		else{
			oldfilename.append(Integer.toString(oldfile));
			oldfilename.insert(4,"_");
			deletefilename=new String(oldfilename);
		}
		//Log.e("oldfile:",Environment.getExternalStorageDirectory().getPath()+"/download/"+deletefilename+".mp4");
		//削除処理
		File delfile=new File(Environment.getExternalStorageDirectory().getPath()+"/download/"+deletefilename+".mp4");
		delfile.delete();
	}

	@Override
	public void onClick(View arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
