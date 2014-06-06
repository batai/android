package com.example.recorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
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
	//private MediaRecorder movieMediaRecorder;
	private RelativeLayout relativeLayout;
	private Button startButton;
	private Button stopButton;
	private SView sampleView;
	private String fileName;
	private SurfaceHolder holder;
	private MediaRecorder mRecorder;

	private static final int START = 0;
	private static final int STOP = 1;

	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	// 録画のプレビューにSurfaceが必要なのでSurfaceViewのサブクラスを定義する
	class SView extends SurfaceView implements SurfaceHolder.Callback {

		SView(Context context) {
			super(context);
			holder = getHolder();
			holder.addCallback(this);
			// プレビューに使うサーフェスはプッシュバッファでなくてはならない
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		// サーフェスが作成されると呼ばれる
		public void surfaceCreated(SurfaceHolder holder) {
			boolean flag=false;
			while(true){
				// MediaRecorderのインスタンスを作成
				mRecorder = new MediaRecorder();

				// ビデオ入力ソースをカメラに設定
				mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				/*
			// 記録フォーマットをMPEG-4に設定
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			// ビデオコーデックをMPEG-4 SPに設定
			mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
				 */
				// プレビューに使用するサーフェスを設定
				mRecorder.setPreviewDisplay(holder.getSurface());
				CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
				mRecorder.setProfile(profile);
				
				long freeByteSize = 0;
				String state = Environment.getExternalStorageState();

				//外部保存領域が使用可能かチェック
				if(Environment.MEDIA_MOUNTED.equals(state) || 
				    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
				{
				  //サイズ取得
				  freeByteSize = Environment.getExternalStorageDirectory().getFreeSpace();
				  Log.e("freesize:",Long.toString(freeByteSize));
				}
				
				//空き容量が1GByte以下なら削除処理
				if(freeByteSize<=1073741824){
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
				//Log.e("getsize", String.valueOf(getsize()));
				// 録画を開始

				mRecorder.start();
				try {
					//
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				mRecorder.stop();
				mRecorder.reset();
				flag=true;
			}
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 画面をフルスクリーンに設定
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(new SView(this));
		/*
		movieMediaRecorder = new MediaRecorder();
		sampleView = new SampleView(this,movieMediaRecorder);

		relativeLayout = new RelativeLayout(this);
        setContentView(relativeLayout);

        relativeLayout.addView(sampleView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //サンプル表示用のボタンを作成
        startButton = new Button(this);
        startButton.setId(START);
        startButton.setText("Start");
        startButton.setOnClickListener(this);
        relativeLayout.addView(startButton, new ViewGroup.LayoutParams( WC, WC ));

        stopButton = new Button(this);
        stopButton.setId(STOP);
        stopButton.setText("Start");
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(WC, WC);
        param.addRule(RelativeLayout.ALIGN_RIGHT, 1);
        stopButton.setOnClickListener(this);
        relativeLayout.addView(stopButton,param);
		 */

	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		/*
		switch (v.getId()) {
		case START:
			movieMediaRecorder.start();
			break;

		case STOP:
			movieMediaRecorder.stop();
			break;
		}
		 */
	}

	public int getsize(){
		StatFs sf = new StatFs("/sdcard/");
		int gb = sf.getAvailableBlocks() * sf.getBlockSize();
		//Log.e("getsize1", String.valueOf(gb));
		return gb;
	}
}