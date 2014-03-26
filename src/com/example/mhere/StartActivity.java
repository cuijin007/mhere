package com.example.mhere;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class StartActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        //隐去状态栏部分(电池等图标和一切修饰部分)
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    this.setContentView(R.layout.activity_begin);   
		AsyncTask<Void, Integer, Void> task=new AsyncTask<Void, Integer, Void>()
				{
					@Override
					protected Void doInBackground(Void... arg0) {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(2500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						this.publishProgress(0);
						return null;
					}

					@Override
					protected void onProgressUpdate(Integer... values) {
						// TODO Auto-generated method stub
						super.onProgressUpdate(values);
					
						Intent intent=new Intent(StartActivity.this, MainActivity.class);
						startActivity(intent);
						StartActivity.this.finish();
					}
					
			
				};
			task.execute();
				
	
	}

	
	
}
