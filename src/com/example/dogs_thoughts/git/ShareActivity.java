package com.example.dogs_thoughts.git;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.R.drawable;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;

public class ShareActivity extends Activity implements OnTouchListener{

	private Intent camera;
	private boolean wasExecuted;
	private static String localFoto;
	private File imgFile;
	private Uri uri;
	private ImageView myImage,imageView;
	private Bitmap imageFinal;
	
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

	    	FrameLayout frame=(FrameLayout) findViewById(R.id.frame_talk);
	    	myImage = (ImageView) findViewById(R.id.ImageFoto);
	    	myImage.setVisibility(View.INVISIBLE);
//	    	myImage2 = (ImageView) findViewById(R.id.ImageFoto2);
	    	
	    	
	    	
//	    	frame.setBack;
	    	Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.toString());
//	        Bitmap theBitmap = BitmapFactory.decodeFile(theFileImage.toString());
	        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(myBitmap.getWidth(), myBitmap.getHeight());
	        Log.i("LAYOUT SIZE", "widht: " + lp.width + ", Height: " + lp.height);
	        Log.i("LAYOUT SIZE", "widht: " + myBitmap.getWidth() + ", Height: " + myBitmap.getHeight());
	        
	        Bitmap bit = getRotateBitmap(myBitmap, 1000, 1135);
	        Drawable draw = new BitmapDrawable(getResources(), bit);
	    	frame.setBackground(draw);
	        lp.setMargins(0, 90, 0, 90);
	        
	        frame.setLayoutParams(lp);
	        
	        myImage.setImageBitmap(myBitmap);
	        myImage.setRotation(90);
	        
//	    	Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

//	    	myImage = (ImageView) findViewById(R.id.ImageFoto);
//	        myImage.setImageBitmap(myBitmap);
//	        myImage.setRotation(90);
	        	        
	    }
	    
	    imageView = (ImageView)findViewById(R.id.dog_talk);
        imageView.setOnTouchListener(this);
        imageView.bringToFront();
        imageView.setVisibility(View.VISIBLE);	    
	}
	
	private static float preX = 0;
	private static float preY = 0;		
	
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
	

///////////////////////////////////////////////////////////////////////
//	      
//					MISTURAR IMAGENS
//	
///////////////////////////////////////////////////////////////////////
	
	public void Misturar(View view){ 
		Log.i("teste", "teste");
		
//		private Bitmap myHeart;
//		myHeart = montagem.montaCoracao(viewCor, viewSent);
		
		imageFinal = montaPensamento(myImage, imageView);
		imgFile = SalvaBit(imageFinal);
		
	}
	
    public Bitmap montaPensamento(ImageView viewFoto, ImageView viewPens){
    	
    	BitmapDrawable drawable = (BitmapDrawable) viewFoto.getDrawable();
    	BitmapDrawable drawable2 = (BitmapDrawable) viewPens.getDrawable();
    	Bitmap piecePensFoto = drawable.getBitmap(); 
    	Bitmap piecePensFoto2 = drawable2.getBitmap();
    	return overlay(piecePensFoto, piecePensFoto2);

    }
 
    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {

        int sizeWidth =  1135;
        int sizeHeight = 1000;
        int ballonSize = 520;
    	
//    	junta duas imagens
        
//    	cria bitmap com tamanho da imagem 1
//        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Bitmap bmOverlay = Bitmap.createBitmap(sizeHeight, sizeWidth, Config.ARGB_8888);
        Log.i("VALORES", "Valores primeira camada: width: " + bmOverlay.getWidth() + " - height: " + bmOverlay.getHeight());
//        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(),bmp1.getConfig());
        
//      define valor da posição do balao
        float x_adjust, y_adjust;
        x_adjust = preX;
        y_adjust = preY;
        
        Canvas canvas = new Canvas(bmOverlay);
        
        Matrix matrix = new Matrix();

        matrix.postRotate(90);

//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp1, bmp1.getHeight(), bmp1.getWidth(), true);
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp1, sizeHeight, sizeWidth, false);
//        Bitmap scaledBitmap = getResizedBitmap(bmp1, sizeHeight, sizeWidth);
        Bitmap scaledBitmap = getRotateBitmap(bmp1, sizeHeight, sizeWidth);
        
        Bitmap scaledBitmap2 = getResizedBitmap(bmp2, ballonSize, ballonSize);
//        Bitmap scaledBitmap2 = Bitmap.createScaledBitmap(bmp2, ballonSize, ballonSize, false);
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp1, sizeWidth, sizeHeight, true);
        
//        Bitmap rotatedBitmap = Bitmap.createBitmap(bmp1, 0, 0, sizeWidth, sizeHeight, matrix, true);
        
        Log.i("VALORES", "Valores camada Giro: width: " + scaledBitmap.getHeight() + " - height: " + scaledBitmap.getWidth());
        
        //Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);        
        
        canvas.drawBitmap(scaledBitmap, 0, 0, null);
//        canvas.drawBitmap(scaledBitmap, new Matrix(), null);
//        canvas.drawBitmap(scaledBitmap, new Matrix(), null);
        
//        canvas.drawBitmap(scaledBitmap2, x_adjust, y_adjust, null);
        canvas.drawBitmap(bmp2, x_adjust, y_adjust, null);
        return bmOverlay;
    }    
  
    protected static File SalvaBit(final Bitmap result) 
    { 
    	File file = null;
//    	String PATH = Environment.getExternalStorageDirectory().toString();
    	if(result!=null){
    		// TODO Auto-generated method stub
			OutputStream outStream = null;
//			File file = new File(PATH, "sampleimage4.jpg");
			file = getOutputMediaFile();
			Log.i("LOCAL", "LOCAL: " + file);
			try{
				outStream = new FileOutputStream(file);
				result.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				outStream.flush();
				outStream.close();
				
			}catch (FileNotFoundException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}catch (IOException e){
//				TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return file;
    }
    
	private static File getOutputMediaFile(){

	      // Create a media file name
	      File mediaFile = new File(localFoto);
	      return mediaFile;
	  }    

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		
		Bitmap scaledBitmap = Bitmap.createBitmap(newHeight, newWidth, Config.ARGB_8888 );
		 
		float ratioX = newWidth / (float) bm.getWidth();
		float ratioY = newHeight / (float) bm.getHeight();
		float middleX = newWidth / 2.0f;
		float middleY = newHeight / 2.0f;
		
		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
		
		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bm, middleX - bm.getWidth() / 2, middleY - bm.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
		
		// RECREATE THE NEW BITMAP
		 
		return scaledBitmap;
	}
	
	public static Bitmap getRotateBitmap(Bitmap bm, int newHeight, int newWidth) {
		
		Bitmap scaledBitmap = Bitmap.createBitmap(newHeight, newWidth, Config.ARGB_8888 );
		 
		float ratioX = newWidth / (float) bm.getWidth();
		float ratioY = newHeight / (float) bm.getHeight();
		float middleX = newWidth / 2.0f;
		float middleY = newHeight / 2.0f;
		
		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
		
		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.rotate(90, 480, 480);
		canvas.drawBitmap(bm, middleX - bm.getWidth() / 2, middleY - bm.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
		String teste = localFoto;
		localFoto = localFoto + "1";
		File imgFile2 = SalvaBit(scaledBitmap);
		localFoto = teste;
		
		// RECREATE THE NEW BITMAP
		 
		return scaledBitmap;
	}

}