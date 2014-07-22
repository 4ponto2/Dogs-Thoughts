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
import android.widget.ImageView;
import android.widget.ShareActionProvider;

public class ShareActivity extends Activity {

	private Intent camera;
	private boolean wasExecuted;
	private String localFoto;
	private File imgFile;
	private Uri uri;
	
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

	    	ImageView myImage = (ImageView) findViewById(R.id.ImageFoto);
	        myImage.setImageBitmap(myBitmap);

	    }
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