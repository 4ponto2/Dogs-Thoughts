package com.example.dogs_thoughts.git;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Camera_preview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    CameraActivity camAct;
    boolean executou = false;

	public Camera_preview(Context context, Camera camera) {
        super(context);
        
        camAct = (CameraActivity) context;
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
//        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
        	
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            
        } catch (IOException e) {
            Log.d("surface created", "Error setting camera preview: " + e.getMessage());
        }
    }
   
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    	Log.d("SURFACE DESTROYED", "SURFACE DESTROYED ");
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
    	
    	Log.i("Camera_Preview", "Surface changed");
    	
    	if(executou==false){
//    		executou = true;
    		
    		if (mHolder.getSurface() == null){
    	          // preview surface does not exist

            	  Log.i("Camera_Preview", "Surface changed NULL");
                  return;    			
    		}
    		
    		// stop preview before making changes
    		try {
    			Log.i("Camera_Preview", "Surface changed TRY 1");
    			mCamera.stopPreview();
    		} catch (Exception e){
    		// ignore: tried to stop a non-existent preview
    		}
       
    	    Log.i("Camera_Preview", "Arrumando parametros");
            Camera.Parameters param=mCamera.getParameters();
        
            if(w<h){
            	Log.i("Camera_Preview", "se W < H");
            	mCamera.setDisplayOrientation(90);
    	    	CameraActivity.orientation=CameraActivity.VERTICAL;
            }else {
            	Log.i("Camera_Preview", "se W <> H");
          	CameraActivity.orientation=CameraActivity.VERTICAL;
          	mCamera.setDisplayOrientation(0);
//          	CameraActivity.orientation=CameraActivity.HORIZONTAL;
            }
            mHolder.setSizeFromLayout();
            Log.i("Camera_Preview", "adicionou os parametros");
            mCamera.setParameters(param);
        // 	set preview size and make any resize, rotate or
        // 	reformatting changes here

        // 	start preview with new settings
            try {
            	Log.i("Camera_Preview", "TRY 2");
            	mCamera.setPreviewDisplay(mHolder);
            	mCamera.startPreview();

            } catch (Exception e){
            	Log.d("surface created changed", "Error starting camera preview: " + e.getMessage());
            }
    	}
    }
}
