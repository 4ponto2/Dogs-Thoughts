package com.example.dogs_thoughts.git;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CameraActivity extends Activity implements OnTouchListener {


	    static Camera mCamera;
	    private Camera_preview mPreview;
	    private FrameLayout preview;
	    private Intent ShareActivity;
	    
	    private String local_foto;
	    
	    public static int orientation;
		public static final int HORIZONTAL=0;
		public static final int VERTICAL=1;
		public int w,h;
		boolean executou = false;
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
		    getWindow().setFormat(PixelFormat.TRANSLUCENT);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        
	        setContentView(R.layout.activity_camera);
	        w=h=0;
	        
	        ShareActivity = new Intent(getApplicationContext(), ShareActivity.class);
	        
	        // Create an instance of Camera
	        mCamera = getCameraInstance();
	      
	        // Create our Preview view and set it as the content of our activity.
	        mPreview = new Camera_preview(this, mCamera);
	        preview = (FrameLayout) findViewById(R.id.camera_preview);
	        preview.addView(mPreview);
	       
	        orientation=1;
	        
	        ImageView imageView = (ImageView)findViewById(R.id.id_camera_preview_front);
	        imageView.setOnTouchListener(this);
	        imageView.setVisibility(View.VISIBLE);
	        
	        ImageButton captureButton = (ImageButton) findViewById(R.id.photoButton);
	        captureButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                TirarFoto();
	            }
	        });	        
	        
	    }
		

		private float preX = 0;
		private float preY = 0;		

		private String TAG = "CameraActivity";
		
		@SuppressLint("ClickableViewAccessibility")
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

				if(newX < mPreview.getX())
					newX = mPreview.getX();
				if(newY < mPreview.getY())
					newY = mPreview.getY();
				if(newX > mPreview.getX() + mPreview.getWidth() - v.getWidth())
					newX = mPreview.getX() + mPreview.getWidth() - v.getWidth();
				if(newY > mPreview.getY() + mPreview.getHeight() - v.getHeight())
					newY = mPreview.getY() + mPreview.getHeight() - v.getHeight();
				
				v.setX(newX);
				v.setY(newY);
				preX = event.getRawX();
				preY = event.getRawY();
			}
			return true;
		}		

		protected void onResume() {
			super.onResume();
		}
	    
	    private void TirarFoto(){
//	    	mCamera.takePicture(null, null, mPicture);
	        mCamera.takePicture(shutterCallback, null, mPicture);        
	    }
	    
	    ShutterCallback shutterCallback = new ShutterCallback() {
	    	public void onShutter() {
	    		Log.d("FOTO", "onShutter'd");
	    	}
		};
		
		PictureCallback mPicture = new PictureCallback(){		
			public void onPictureTaken(byte[] data, Camera camera) {
				try{
					Log.d("FOTO", "onPictureTaken");
					salvafoto(data,camera);
					releaseCamera();
					chamaShare();
					
				}catch(Error e){
					Log.d("FOTO ERRO", "ERRO");
					releaseCamera();
					
				}
			}
		};		
		
	    private void releaseCamera() {
			 if (mCamera != null){
				 mCamera.release();
				 mCamera = null;
		        }
		} 
	    
	    public void onBackPressed()
	    {

	    }
	    
	    public static Camera getCameraInstance(){
	        Camera c = null;
	        try {
	            c = Camera.open(); // attempt to get a Camera instance
	        }
	        catch (Exception e){
	        	
	        }
	        return c;
	    }
	    
	    public void salvafoto(byte[] data, Camera camera) {
	          File pictureFile = getOutputMediaFile();
	          Log.i("LOCAL", "MEDIAFILE: " + pictureFile);
	          	if (pictureFile == null) {
	                return;
	            }
	            try {
	                FileOutputStream fos = new FileOutputStream(pictureFile);
	                fos.write(data);
	                fos.close();
	                releaseCamera();
	                local_foto = pictureFile.toString();
	                Log.i("LOCAL_FOTO", "TO STRING: " + local_foto);
	            } catch (FileNotFoundException e) {

	            } catch (IOException e) {
	            }
	    }	    
	    
	    private static File getOutputMediaFile() {
	        File mediaStorageDir = new File(
	                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
	                "Dog's Toughts");
	        if (!mediaStorageDir.exists()) {
	            if (!mediaStorageDir.mkdirs()) {
	                Log.d("MyCameraApp", "failed to create directory");
	                return null;
	            }
	        }
	        // Create a media file name
	        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
	        File mediaFile;
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator
	                + "IMG_" + timeStamp + ".jpg");

	        return mediaFile;
	    }	
	    
	    public void chamaShare(){
	    	ShareActivity.putExtra("Foto_Local", local_foto);
	        startActivity(ShareActivity);	        
	    }	    

	}
