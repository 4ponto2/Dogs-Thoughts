package com.example.dogs_thoughts.git;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ShareActionProvider;

public class ShareActivity extends Activity implements OnTouchListener{

	private Intent camera;
	private boolean wasExecuted;
	private String localFoto;
	private File imgFile;
	private Uri uri;
	private ImageView myImage,imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		
    	camera = new Intent(getApplicationContext(), CameraActivity.class);
        wasExecuted = false;
        
		Intent iin = getIntent();
	    Bundle extras = iin.getExtras();
	    localFoto = (String) extras.getString("Foto_Local");
	    
	    Log.d("LOCAL FOTO", "LOCAL FOTO: " + localFoto);
	    
	    imgFile = new File(localFoto);
	    if(imgFile.exists()){

	    	Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

	    	myImage = (ImageView) findViewById(R.id.ImageFoto);
	        myImage.setImageBitmap(myBitmap);
	        myImage.setRotation(90);

	    }
	    
	    imageView = (ImageView)findViewById(R.id.dog_talk);
        imageView.setOnTouchListener(this);
        imageView.bringToFront();
        imageView.setVisibility(View.VISIBLE);	    
	}
	
	private float preX = 0;
	private float preY = 0;		
	
	private String TAG = "CameraActivity";
	
	public boolean onTouch(View v, MotionEvent event) {
		Log.d(TAG, TAG + "X, Y is : " + event.getRawX() + ", " + event.getRawY());
		Log.d(TAG, TAG + "vX, vY is : " + v.getX() + ", " + v.getY());
		Log.d(TAG, TAG + ".......................");
		
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			preX = event.getRawX();
			preY = event.getRawY();
			return true;
		}
		if(event.getAction() == MotionEvent.ACTION_UP) {
			float newX = v.getX() + (event.getRawX() - preX);
			float newY = v.getY() + (event.getRawY() - preY);
			v.setX(newX);
			v.setY(newY);
			preX = event.getRawX();
			preY = event.getRawY();
			
		}
		if(event.getAction() == MotionEvent.ACTION_MOVE) {
			float newX = v.getX() + (event.getRawX() - preX);
			float newY = v.getY() + (event.getRawY() - preY);

			if(newX < myImage.getX())
				newX = myImage.getX();
			if(newY < myImage.getY())
				newY = myImage.getY();
			if(newX > myImage.getX() + myImage.getWidth() - v.getWidth())
				newX = myImage.getX() + myImage.getWidth() - v.getWidth();
			if(newY > myImage.getY() + myImage.getHeight() - v.getHeight())
				newY = myImage.getY() + myImage.getHeight() - v.getHeight();
			
			v.setX(newX);
			v.setY(newY);
			preX = event.getRawX();
			preY = event.getRawY();
		}
		return true;
	}		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.share, menu);

		MenuItem shareItem = (MenuItem) menu.findItem(R.id.action_share);
		ShareActionProvider mShare = (ShareActionProvider)shareItem.getActionProvider();

		shareImage();
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

		mShare.setShareIntent(shareIntent);

		return true;
	}
	
    public void shareImage() {
  	
  		uri = Uri.fromFile(imgFile);
    }	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}