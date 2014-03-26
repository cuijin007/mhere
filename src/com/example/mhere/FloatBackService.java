package com.example.mhere;


import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;

import com.xmpp.Communication;

import Save.SaveReadWrite;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;

public class FloatBackService extends Service{

	private WindowManager windowManager;  
	private WindowManager.LayoutParams layoutParams;  
	public static Chat chat;
	private FloatServiceView floatServiceView;
	int width=800;
	int height=1280;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	private MessageListener messageListener=new MessageListener()
	{

		@Override
		public void processMessage(Chat arg0,
				org.jivesoftware.smack.packet.Message arg1) {
			// TODO Auto-generated method stub
			//FloatServiceView.DrawPlus=300;
			String messageStr=arg1.getBody();
			String[] spiltNode={"<",">"};
			if(messageStr!=null&&messageStr.startsWith("<Action>"))
			{
				messageStr=messageStr.replace("<Action>", "");
				String[] afterSpilt=messageStr.split("<|>");
				if(afterSpilt.length>1)
				{
					if(afterSpilt[1].equals("name"))
					{
						if(afterSpilt.length>2&&afterSpilt[2].equals(SaveReadWrite.GetConfigure().coupleName))
						{
							if(afterSpilt.length>3)
							{
								if(afterSpilt[3].equals("BlowNum"))
								{
									int result=Integer.parseInt(afterSpilt[4]);
									FloatServiceView.SetDrawPlus(result);
								}
								if(afterSpilt[3].equals("OverBlow"))
								{
									floatServiceView.RenewBitmap=true;
								}
							}
						}
					}
				}
			}
		}
		
	};
	
	
	private Handler mHandler=new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
		
	};
	

	//private FunctionViewCocos functionViewCoco;

	
	
	private void InitChat()
	{
		
		AsyncTask<String, Integer, Chat> task = new AsyncTask<String, Integer, Chat>() {

			@Override
			protected Chat doInBackground(String... params) {
				// TODO Auto-generated method stub
				//chatManager=Connection.getChat(params[0]);
				//return Connection.getChat(params[0]);
				ChatManager chatManager= Communication.GetConnection().getChatManager();
				chat=chatManager.createChat(params[0], FloatBackService.this.messageListener);
				//chatOut=chat;
				return chat;
			}

			@Override
			protected void onPostExecute(Chat result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				chat = result;
			}

		};

		task.execute(SaveReadWrite.GetConfigure().coupleName);

		try {
			task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(chat.getParticipant());
	}


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		 this.floatServiceView=new FloatServiceView(this);
			
			
			layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,  
	                LayoutParams.FILL_PARENT, LayoutParams.TYPE_SYSTEM_ERROR,  
	                LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);  
		
			layoutParams.flags=WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
					| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
					| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
			
			this.windowManager=(WindowManager)this.getSystemService(WINDOW_SERVICE);
			
			
			 this.floatServiceView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					//onBackPressed();
					return true;
				}  
		            
		            }  
		        );
		        
			
			this.floatServiceView.setFocusable(false);
			this.floatServiceView.setFocusable(false);
			this.floatServiceView.setClickable(false);
			this.floatServiceView.setKeepScreenOn(false);
			this.floatServiceView.setLongClickable(false);
			this.floatServiceView.setFocusableInTouchMode(false);
			this.floatServiceView.StartTimer();
			
			this.windowManager.addView(this.floatServiceView, this.layoutParams);
			
			this.InitChat();
	}

}
