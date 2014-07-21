package com.example.dogs_thoughts.git;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;

public class InitialActivity extends Activity {

	private boolean wasExecuted = false;
    private Intent mainActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_initial);
	}

    public void init()
    {
//    	mainActivity = new Intent(getApplicationContext(), CameraActivity.class);
        wasExecuted = false;
    }

    public void proxima(View view){
        
    	if(!wasExecuted){
    	    wasExecuted = true;
            startActivity(mainActivity);
        }
    }

	
    public void onBackPressed(){
	 	   Log.i("HA", "Finishing");
	 	   Intent intent = new Intent(Intent.ACTION_MAIN);
	 	   intent.addCategory(Intent.CATEGORY_HOME);
	 	   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 	   startActivity(intent);
	 }
}