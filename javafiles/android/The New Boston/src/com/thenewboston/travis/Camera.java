package com.thenewboston.travis;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Camera extends Activity implements View.OnClickListener{
	//uses Permission android.permission.SET_WALLPAPER
	ImageButton ib;
	Button b;
	ImageView iv;	
	Intent i;
	final static int cameraData = 0;
	Bitmap bmp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo);
		initialize();
		InputStream is = getResources().openRawResource(R.drawable.icon);
		bmp = BitmapFactory.decodeStream(is);
	}
	private void initialize() {
		// TODO Auto-generated method stub
		iv = (ImageView) findViewById (R.id.ivReturnedPic);
		ib = (ImageButton) findViewById (R.id.ibTakePic);
		b = (Button) findViewById(R.id.bSetWall);
		b.setOnClickListener(this);
		ib.setOnClickListener(this);
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.bSetWall:
			try {
				getApplicationContext().setWallpaper(bmp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.ibTakePic:
			i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);//get picture from camera
			startActivityForResult(i, cameraData);//camreaData-variable that collects datas
		break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {//gets data
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK){
			Bundle extras = data.getExtras();//get results back
			bmp = (Bitmap) extras.get("data");//"data" default key
			iv.setImageBitmap(bmp);
		}
	}
}
