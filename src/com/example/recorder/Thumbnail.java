package com.example.recorder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class Thumbnail extends Activity{
	// 要素をArrayListで設定
    private ArrayList<CustomView> imgList = new ArrayList<CustomView>();
    private WindowManager wm;
	private TextView nameText;
	private ImageView imageView;
	private Display disp;
	private Point size;
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thumbnail);
		
		// ウィンドウマネージャのインスタンス取得
		wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		// ディスプレイのインスタンス生成
		disp = wm.getDefaultDisplay();
		size = new Point();
		//disp.getSize(size);
		
		getImagePath();
		// GridViewのインスタンスを生成
        GridView gridview = (GridView) findViewById(R.id.gridView1);
        
        CustomAdapter adapter = new CustomAdapter(this, 0, imgList);
        // gridViewにadapterをセット
        gridview.setAdapter(adapter);
        
	}
	
	private void getImagePath() {
		final File[] filelist;
		//ディレクトリ /mnt/sdcard を指定
		File dir = new File("/sdcard/download/");
		//指定されたディレクトリのファイル名（ディレクトリ名）を取得
		filelist = dir.listFiles();
		
		CustomView customView = null;

		for(int i=0; i< filelist.length ; i++){
			customView = new CustomView();
			customView.setImage(CustomThum(filelist[i]));
			customView.setName(filelist[i].getName());
			//Log.v("filename", filelist[i].getName());
			imgList.add(customView);
			customView = null;
		}
	}
	
	private void setLayout(int position,View convertView){
		nameText = (TextView)convertView.findViewById(R.id.name);
		imageView = (ImageView)convertView.findViewById(R.id.imageview);
	}
	
	public Bitmap CustomThum(File path){
		   Bitmap thumbnail = null;
		   MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		   retriever.setDataSource(path.getPath());
		 
		   //秒単位で指定
		   thumbnail = retriever.getFrameAtTime(1000 * 1000 * 2);
		   
		   return thumbnail;
		}
	
	public Bitmap reSize(Bitmap imageBitmap){
		int s = size.x/4;
		Bitmap resizeBitmap = Bitmap.createScaledBitmap(imageBitmap, s, s, false);
		return resizeBitmap;
	}
	
	class CustomAdapter extends ArrayAdapter<CustomView>{
		public CustomAdapter(Context context, int resource, List<CustomView> objects) {
			super(context, resource, objects);
			// TODO 自動生成されたコンストラクター・スタブ
		}
		
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
			// 特定の行(position)のデータを得る
			final CustomView item = (CustomView)getItem(position);
			
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.grid_items, parent, false);
			}
			Bitmap reImage = reSize(item.getImage());
			setLayout(position,convertView);
			nameText.setText(item.getName());
			imageView.setImageBitmap(reImage);
			return convertView;
		}
	}
}
