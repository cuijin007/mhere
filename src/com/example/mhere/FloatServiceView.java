package com.example.mhere;


import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class FloatServiceView extends View {

	Bitmap bitmap;
	int[] buffer;
	int[] bufferOut;
	int Width=400;
	int Height=640;
	Timer timer=new Timer();
	Paint paint;
	Thread thread;
	boolean runMark=true;
	public static int DrawPlus;
	public static boolean RenewBitmap; 
	public static long timeMark=0;
	public static void SetDrawPlus(int drawPlus)
	{
		DrawPlus=drawPlus;
		timeMark=System.currentTimeMillis();
	}
	public FloatServiceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.Init();
		this.ChangeAlpha(-255);
	}

	public FloatServiceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.Init();
		this.ChangeAlpha(-255);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		//canvas.drawBitmap(this.bitmap, 0,0, this.paint);
		Rect rectF = new Rect(0, 0, canvas.getWidth(), canvas.getHeight()); 
		canvas.drawBitmap(bitmap,null,rectF,null);
	}
	
	public void Init()
	{
		this.buffer=new int[this.Width*this.Height];
		this.bufferOut=new int[this.Width*this.Height];
		bitmap=Bitmap.createBitmap(this.Width, this.Height, Config.ARGB_8888);
		this.bitmap.getPixels(this.buffer, 0, this.Width, 0, 0, this.Width, this.Height);
		
		this.paint = new Paint();
		this.paint.setStyle(Style.FILL);
		this.paint.setColor(Color.GRAY);
		//this.paint.setAlpha(100);
		
		this.SetBitmapIntBuffer();
	}
	public void SetBitmapIntBuffer()
	{
		int red=100;
		int blue=100;
		int green=100;
		int alpha=0;
		for(int i=0;i<this.Height;i++)
		{
			for(int j=0;j<this.Width;j++)
			{
				//alpha=(int) (100*(Math.sin((i-this.Height/2)/200.0)*Math.sin((j-this.Width/2)/200.0)+1));
				alpha=(int) (300*Math.sin(Math.cos((j)/300.0)*((i)/300.0))*Math.cos((j)/300.0));
				if(alpha>255)
				{
					alpha=0xfe;
				}
				if(alpha<0)
				{
					alpha=0;
				}
				this.buffer[i*this.Width+j]=0xff|0xff<<8|0xff<<16|alpha<<24;
			}
		}
		this.bitmap.setPixels(this.buffer, 0, this.Width, 0, 0, this.Width, this.Height);
	}
	public void StartTimer()
	{		
		this.timer.schedule(new TimerTask() {
			 int count=-260;
			 int min=-3;
			 boolean mark=true;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//FunctionView3.this.ChangeAlpha(1);
				if(count+FloatServiceView.DrawPlus>-255)
				{
					count+=FloatServiceView.DrawPlus;
					if(count+FloatServiceView.DrawPlus>150)
					{
						count=150;
					}
					
				}
				if(count+min>-260)
				{
					count=count+min;
				}
				if(count>-255)
				{
					FloatServiceView.this.ChangeAlpha(count);
					FloatServiceView.this.postInvalidate();
				}
				if((count<-255)&&FloatServiceView.this.RenewBitmap)
				{
					FloatServiceView.this.SetBitmapIntBuffer();
					FloatServiceView.RenewBitmap=false;
				}
				if(System.currentTimeMillis()-timeMark>500)
				{
					FloatServiceView.DrawPlus=0;
				}
			}
		}, 0,30);
	}
	public void ChangeAlpha(int alphaPlus)
	{
		int red=100;
		int blue=100;
		int green=100;
		int alpha=100;
		for(int i=0;i<this.Height;i++)
		{
			for(int j=0;j<this.Width;j++)
			{

			int alphaGet=(this.buffer[i*this.Width+j])>>>24;
			if(alphaGet>0)
			{
				int kk=0;
				kk++;
			}
				int alphaResult=0;
				if(alphaPlus+alphaGet<0)
				{
					alphaResult=0x00;
				}
				else if(alphaPlus+alphaGet>255)
				{
					alphaResult=0xff;
				}
				else
				{
					alphaResult=alphaPlus+alphaGet;
				}
				this.bufferOut[i*this.Width+j]=(this.buffer[i*this.Width+j]&0x00ffffff)|(alphaResult<<24);
			}
		}
		this.bitmap.setPixels(this.bufferOut, 0, this.Width, 0, 0, this.Width, this.Height);
	}

}
