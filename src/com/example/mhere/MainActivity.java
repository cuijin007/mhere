package com.example.mhere;

import org.jivesoftware.smack.XMPPConnection;

import com.xmpp.Communication;

import Save.SaveReadWrite;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	String password;
	String name;
	EditText passwordEdit,nameEdit;
	Button loginButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//full screen
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		this.InitControl();
	}

	private void InitControl()
	{
		this.passwordEdit=(EditText)this.findViewById(R.id.login_password);
		this.nameEdit=(EditText)this.findViewById(R.id.login_name);
		this.loginButton=(Button)this.findViewById(R.id.login_login);
		this.loginButton.setOnClickListener(new LoginOnClickListener());
		SaveReadWrite.Init();
		SaveReadWrite.ReadConfigure();
		
		if(!SaveReadWrite.GetConfigure().Name.equals(""))
		{
			this.nameEdit.setText(SaveReadWrite.GetConfigure().Name);
		}
		if(!SaveReadWrite.GetConfigure().Password.equals(""))
		{
			this.passwordEdit.setText(SaveReadWrite.GetConfigure().Password);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class LoginOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0.getId()==R.id.login_login)
			{
				AsyncTask<String, Integer, String> task = new AsyncTask<String, Integer, String>() {
					String result;
					boolean startMark=false;
					@Override
					protected String doInBackground(String... params) {
						// TODO Auto-generated method stub
						XMPPConnection Connection=Communication.GetConnection();
						if(Connection!=null)
						{
							try
							{
								Connection.login(params[0], params[1]);
								result="Login Ok";
								this.startMark=true;
								SaveReadWrite.ReadConfigure();
								SaveReadWrite.GetConfigure().Name=params[0];
								SaveReadWrite.GetConfigure().Password=params[1];
								SaveReadWrite.WriteConfigure();
							}
							catch(Exception ee)
							{
								result=ee.getMessage().toString();
							}
						}
						else
						{
							result="Check the Internet :D";
						}
						return null;
					}
				
					@Override
					protected void onPostExecute(String result) {
						// TODO Auto-generated method stub
						super.onPostExecute(result);
						if(!startMark)
						{
							Toast.makeText(MainActivity.this, this.result, Toast.LENGTH_SHORT).show();
						}
						else
						{
							Intent intent=new Intent(MainActivity.this,ListActivity.class);
							MainActivity.this.startActivity(intent);
						}
					}

				};
				
				task.execute(nameEdit.getText().toString(), passwordEdit.getText().toString());

			}
		}
		
	}

}
