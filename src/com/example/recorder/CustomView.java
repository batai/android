package com.example.recorder;

import android.graphics.Bitmap;

public class CustomView {
	private String name;
	private Bitmap image;
	
	public CustomView() {
		super();
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	public CustomView(String name,Bitmap image) {
		super();
		this.name = name;
		this.image = image;
	}

	public Bitmap getImage() {
		return image;
	}

	public String getName() {
		return name;
	}
	
	public void setImage(Bitmap image) {
		this.image = image;
	}

	public void setName(String name) {
		this.name = name;
	}
}
