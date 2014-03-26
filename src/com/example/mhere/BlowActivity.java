package com.example.mhere;


import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;

import ca.uol.aig.fftpack.RealDoubleFFT;
import Save.SaveReadWrite;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BlowActivity extends Activity{

	protected Chat chat;
	public static Chat chatOut;
	Button buttonSend;
	TextView textView;
	protected MessageListener messageListener;
	
	private Button button;
    private ImageView imageView;
    private int frequency = 8000;
    private int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    private RealDoubleFFT transformer;
    private int blockSize = 256;
    private boolean started = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blow);
		this.Init();
	}

	private void Init()
	{
		this.transformer= new RealDoubleFFT(blockSize);
		this.buttonSend=(Button)findViewById(R.id.blow_blowbutton);
		Intent intent = getIntent();
		
		Intent intentStart=new Intent(getApplicationContext(), FloatBackService.class);
		this.startService(intentStart);
	    this.buttonSend.setOnTouchListener(new ButtonListener());
	    
	}
	private class ButtonListener implements OnTouchListener
	{

		RecordAudio recordAudio;
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			
			if(arg1.getAction()==MotionEvent.ACTION_DOWN)
			{
				recordAudio=new RecordAudio();
				recordAudio.RunMark=true;
				recordAudio.execute();
			}
			if(arg1.getAction()==MotionEvent.ACTION_UP)
			{
				recordAudio.RunMark=false;
				//recordAudio.cancel(false);
				try {
					FloatBackService.chat.sendMessage("<Action><name>"+SaveReadWrite.GetConfigure().Name+"<OverBlow>0");
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}
		
	}
	
	private class RecordAudio extends AsyncTask<Void, double[], Void>
	{
		AudioRecord audioRecord;
		int bufferSize;
		public long timeMark;
		
		public RecordAudio()
		{
			this.timeMark=System.currentTimeMillis();
		}
		
		public boolean RunMark=false;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			int bufferSize = AudioRecord.getMinBufferSize(frequency,
                    channelConfiguration, audioEncoding);
			 AudioRecord audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC, frequency,
                    channelConfiguration, audioEncoding, bufferSize);
			 short[] buffer = new short[blockSize];
			 double[] toTransform = new double[blockSize];
			 audioRecord.startRecording();
			 while (RunMark) {
				 //将record的数据 读到buffer中，但是我认为叫做write可能会比较合适些。
				 int bufferResult = audioRecord.read(buffer, 0, blockSize);

				 for (int i = 0; i < bufferResult; i++) {
                     	toTransform[i] = (double) buffer[i] / Short.MAX_VALUE;
				 }
				 try
                 {
                 	transformer.ft(toTransform);
                 	publishProgress(toTransform);
                 }
                 catch(Exception ee)
                 {
                 	int kk=0;
                 	kk++;
                 }
			 }
			 audioRecord.stop();
			 audioRecord.release();
			 return null;
		}

		@Override
		protected void onProgressUpdate(double[]... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			
			 int all=0;
             for(int i=0;i<values[0].length/2;i++)
             {
             	int downy=(int)values[0][i]*10;
             	if(downy>0)
             	{
             		all+=downy;
             	}
             	else
             	{
             		all-=downy;
             	}
             }
             all=all/(values[0].length/2);
			FloatServiceView.SetDrawPlus(all);
             try {
				if(System.currentTimeMillis()-this.timeMark>1000)
				{
					FloatBackService.chat.sendMessage("<Action><name>"+SaveReadWrite.GetConfigure().Name+"<BlowNum>"+all);
					this.timeMark=System.currentTimeMillis();					
					//FloatServiceView.SetDrawPlus(all);
				}
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}				
     }
	
}
