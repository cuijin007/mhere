package com.example.mhere;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import com.xmpp.Communication;

import Save.Configure;
import Save.CoupleStateEnum;
import Save.SaveReadWrite;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListActivity extends Activity{

	//
	public static String KEY = "roster";

	private ListView rosterListView;
	private List<String> data=new ArrayList();
	private List<RosterEntry> entry=new ArrayList();
	private Button buttonRefresh,buttonStartIntent;
	private Configure configure;
	private Chat chat;
	
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>(); 
	AsyncTask<Void, Integer, Roster> getListTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.acti_list);
	
		this.InitControl();
		this.InitList();
		this.InitChat();
		
		this.InitLINSHI();
	}

    public void InitLINSHI()
    {
    	this.buttonStartIntent=(Button)this.findViewById(R.id.button1);
    	this.buttonStartIntent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent intent=new Intent(getApplicationContext(),BlowActivity.class);
				startActivity(intent);
			}
		});
    }
	private Handler handler=new Handler()
	{

		@Override
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub		
			super.handleMessage(msg);
			final String name=(String)msg.obj;
			switch(msg.what)
			{
			case 0:
				{
					AlertDialog.Builder builder = new Builder(ListActivity.this);
					builder.setMessage("Connect!");
					builder.setPositiveButton("OK", null); 
					builder.setNegativeButton("Cancel", null);
					builder.show();
					ClearListView();
					InitList();
					break;
				}
			case 1:
				{
					AlertDialog.Builder builder = new Builder(ListActivity.this);
					builder.setMessage("IF you want to Connect "+name);
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							Chat chat1=Communication.GetConnection().getChatManager().createChat(SaveReadWrite.GetConfigure().coupleName, null);
							try {
								chat1.sendMessage(Communication.ActionStart+"<ADD>"+SaveReadWrite.GetConfigure().Name);
							} catch (XMPPException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							SaveReadWrite.GetConfigure().coupleName=name;
							SaveReadWrite.GetConfigure().coupleState=CoupleStateEnum.CONNECT;
							SaveReadWrite.WriteConfigure();
						}
						
					});
					builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							SaveReadWrite.GetConfigure().coupleName=name;
							SaveReadWrite.GetConfigure().coupleState=CoupleStateEnum.BEINVITE;
							SaveReadWrite.WriteConfigure();
							ClearListView();
							InitList();
						}
						
					});
					
					builder.show();
					break;
				}
			case 2:
				{
					AlertDialog.Builder builder = new Builder(ListActivity.this);
					builder.setMessage("Receive an invitation?");
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							SaveReadWrite.GetConfigure().coupleState=CoupleStateEnum.CONNECT;
							SaveReadWrite.WriteConfigure();
							Chat chat1=Communication.GetConnection().getChatManager().createChat(SaveReadWrite.GetConfigure().coupleName, null);
							
							try {

								chat1.sendMessage(Communication.ActionStart+"<ADD>"+SaveReadWrite.GetConfigure().Name);
							} catch (XMPPException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ClearListView();
							InitList();
						}
					}); 
					builder.setNegativeButton("Cancel", null);
					builder.show();
					break;
				}
			case 3:
				{
					AlertDialog.Builder builder2 = new Builder(ListActivity.this);
					builder2.setMessage("DisConnect\resend the invite?");						
					builder2.setNeutralButton("Cancel", null);
					
					
					builder2.setPositiveButton("DisConnect", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							SaveReadWrite.GetConfigure().coupleState=CoupleStateEnum.NOACTION;
							SaveReadWrite.GetConfigure().coupleName="";
							SaveReadWrite.WriteConfigure();
							ClearListView();
							InitList();
							
						}
					}); 
					builder2.setNegativeButton("Resend", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							Chat chat1=Communication.GetConnection().getChatManager().createChat(SaveReadWrite.GetConfigure().coupleName, null);
							try {
								chat1.sendMessage(Communication.ActionStart+"<ADD>"+SaveReadWrite.GetConfigure().Name);
							} catch (XMPPException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ClearListView();
							InitList();
						}
					});
					
					builder2.show();
					break;
				}
			case 4:
				{
					AlertDialog.Builder builder2 = new Builder(ListActivity.this);
					builder2.setMessage("Send Invite ?");
					builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							SaveReadWrite.GetConfigure().coupleName=name;
							SaveReadWrite.WriteConfigure();
							Chat chat1=Communication.GetConnection().getChatManager().createChat(SaveReadWrite.GetConfigure().coupleName, null);
							try {
								chat1.sendMessage(Communication.ActionStart+"<ADD>"+SaveReadWrite.GetConfigure().Name);
							} catch (XMPPException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ClearListView();
							InitList();
						}
					}); 
					builder2.setNegativeButton("Cancel", null);
					builder2.show();
					break;
				}
			case 5:
				{
					AlertDialog.Builder builder2 = new Builder(ListActivity.this);
					builder2.setMessage("Send Invite ?");
					builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							SaveReadWrite.WriteConfigure();
							Chat chat1=Communication.GetConnection().getChatManager().createChat(name, null);
							try {
								chat1.sendMessage(Communication.ActionStart+"<ADD>"+SaveReadWrite.GetConfigure().Name);
								SaveReadWrite.GetConfigure().coupleName=name;
								SaveReadWrite.GetConfigure().coupleState=CoupleStateEnum.INVITE;
								SaveReadWrite.WriteConfigure();
							} catch (XMPPException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ClearListView();
							InitList();
						}
					}); 
					builder2.setNegativeButton("Cancel", null);
					builder2.show();
					break;
				}
			}
		}
		
	};
	
	private MessageListener messageListener=new MessageListener() {
		
		@Override
		public void processMessage(Chat arg0, Message arg1) {
			// TODO Auto-generated method stub
			if(arg1.getBody()!=null&&arg1.getBody().startsWith(Communication.ActionStart)&&arg1.getBody().contains("<ADD>"))
			{
				final String name=arg1.getBody().replace(Communication.ActionStart+"<ADD>", "");
				
				if(!name.equals(SaveReadWrite.GetConfigure().Name))
				{
				String str=SaveReadWrite.GetConfigure().coupleName;
				if(name.equals(str))
				{
					if(SaveReadWrite.GetConfigure().coupleState==CoupleStateEnum.INVITE)
					{
						
						SaveReadWrite.GetConfigure().coupleState=CoupleStateEnum.CONNECT;
						Chat chat1=Communication.GetConnection().getChatManager().createChat(SaveReadWrite.GetConfigure().coupleName, null);
						
						try {

							chat1.sendMessage(Communication.ActionStart+"<ADD>"+SaveReadWrite.GetConfigure().Name);
						} catch (XMPPException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						SaveReadWrite.WriteConfigure();						
						handler.obtainMessage(0, name).sendToTarget();						
					}

				}
				else
				{
					if(SaveReadWrite.GetConfigure().coupleState!=CoupleStateEnum.CONNECT)
					{
						handler.obtainMessage(1, name).sendToTarget();
						
					}
				}
				}
			}
		}
	};
	
	
	public void InitControl()
	{
		this.buttonRefresh=(Button)this.findViewById(R.id.acti_refresh);
		this.buttonRefresh.setOnClickListener(new ListOnClickListener());
		this.rosterListView=(ListView)findViewById(R.id.acti_listshow);
		this.rosterListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub				
				final RosterEntry entryNode=entry.get((int)id);
				if(entryNode.getUser().equals(SaveReadWrite.GetConfigure().coupleName))
				{
					if(SaveReadWrite.GetConfigure().coupleState==CoupleStateEnum.BEINVITE)
					{
						/*
						AlertDialog.Builder builder = new Builder(ListActivity.this);
						builder.setMessage("Receive an invitation?");
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								SaveReadWrite.GetConfigure().coupleState=CoupleStateEnum.CONNECT;
								SaveReadWrite.WriteConfigure();
								InitList();
							}
						}); 
						builder.setNegativeButton("Cancel", null);
						builder.show();
						*/
						handler.obtainMessage(2, null).sendToTarget();
					}
					if(SaveReadWrite.GetConfigure().coupleState==CoupleStateEnum.CONNECT)
					{
						/*
						AlertDialog.Builder builder2 = new Builder(ListActivity.this);
						builder2.setMessage("DisConnect the one?");
						builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								SaveReadWrite.GetConfigure().coupleState=CoupleStateEnum.NOACTION;
								SaveReadWrite.GetConfigure().coupleName="";
								SaveReadWrite.WriteConfigure();
								InitList();
							}
						}); 
						builder2.setNegativeButton("Cancel", null);
						builder2.show();
						*/
						/*
						AlertDialog.Builder builder2 = new Builder(ListActivity.this);
						builder2.setMessage("DisConnect\resend the invite?");						
						builder2.setNeutralButton("Cancel", null);
						
						
						builder2.setPositiveButton("DisConnect", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								SaveReadWrite.GetConfigure().coupleState=CoupleStateEnum.NOACTION;
								SaveReadWrite.GetConfigure().coupleName="";
								SaveReadWrite.WriteConfigure();
								ClearListView();
								InitList();
								
							}
						}); 
						builder2.setNegativeButton("Resend", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Chat chat1=Communication.GetConnection().getChatManager().createChat(SaveReadWrite.GetConfigure().coupleName, null);
								try {
									chat1.sendMessage(Communication.ActionStart+"<ADD>"+SaveReadWrite.GetConfigure().Name);
								} catch (XMPPException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						});
						
						builder2.show();
						*/
						handler.obtainMessage(3, null).sendToTarget();
						
					}
					if(SaveReadWrite.GetConfigure().coupleState==CoupleStateEnum.INVITE)
					{
						/*
						AlertDialog.Builder builder2 = new Builder(ListActivity.this);
						builder2.setMessage("Send Invite ?");
						builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								SaveReadWrite.GetConfigure().coupleName=entryNode.getUser();
								SaveReadWrite.WriteConfigure();
								Chat chat1=Communication.GetConnection().getChatManager().createChat(SaveReadWrite.GetConfigure().coupleName, null);
								try {
									chat1.sendMessage(Communication.ActionStart+"<ADD>"+SaveReadWrite.GetConfigure().Name);
								} catch (XMPPException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								InitList();
							}
						}); 
						builder2.setNegativeButton("Cancel", null);
						builder2.show();
						*/
						handler.obtainMessage(4, entryNode.getUser()).sendToTarget();
					}
					
				}
				else 
				{
					/*
					AlertDialog.Builder builder2 = new Builder(ListActivity.this);
					builder2.setMessage("Send Invite ?");
					builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							SaveReadWrite.WriteConfigure();
							Chat chat1=Communication.GetConnection().getChatManager().createChat(SaveReadWrite.GetConfigure().coupleName, null);
							try {
								chat1.sendMessage(Communication.ActionStart+"<ADD>"+SaveReadWrite.GetConfigure().Name);
								SaveReadWrite.GetConfigure().coupleName=entryNode.getUser();
								SaveReadWrite.GetConfigure().coupleState=CoupleStateEnum.INVITE;
								SaveReadWrite.WriteConfigure();
							} catch (XMPPException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							InitList();
						}
					}); 
					builder2.setNegativeButton("Cancel", null);
					builder2.show();
					*/
					handler.obtainMessage(5, entryNode.getUser()).sendToTarget();
				}
			}
			
		});
	}
	
	public void InitChat()
	{
		Communication.GetConnection().getChatManager().addChatListener(new ChatManagerListener() {
			
			@Override
			public void chatCreated(Chat arg0, boolean arg1) {
				// TODO Auto-generated method stub
				arg0.addMessageListener(messageListener);
			}
		});		
	}
	
	public void InitList()
	{
		this.getListTask=new AsyncTask<Void, Integer, Roster>()
		{

			@Override
			protected void onPostExecute(Roster result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				Collection<RosterEntry> entries = result.getEntries();

				for (RosterEntry r : entries) {
					try
					{
						//data.add(r.getUser()+r.getName());
						HashMap<String, Object> map = new HashMap<String, Object>();  
						map.put("listview_item_name", r.getName());
						map.put("listview_item_address", r.getUser());
						map.put("listview_item_state", "");
						listItem.add(map);
						entry.add(r);
					}
					catch(Exception ee)
					{
						int kk=0;
						kk++;
					}
					
				}
				
				SimpleAdapter listItemAdapter = new SimpleAdapter(
						ListActivity.this,
						listItem,
						R.layout.listview_item,
						new String[]{"listview_item_name","listview_item_address","listview_item_state"},
						new int[]{R.id.listview_item_name,R.id.listview_item_address,R.id.listview_item_state});
				
				rosterListView.setAdapter(listItemAdapter);
				SetListViewState();
				rosterListView.invalidate();
			}

			@Override
			protected Roster doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				return Communication.GetConnection().getRoster();
				
			}
			
		};
		this.getListTask.execute();
	}
	
	public class ListOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0.getId()==R.id.acti_refresh)
			{
				ClearListView();
				InitList();
			}
		}
		
	}
	
	public void ClearListView()
	{
		listItem.clear();
		SimpleAdapter listItemAdapter = new SimpleAdapter(
				ListActivity.this,
				listItem,
				R.layout.listview_item,
				new String[]{"listview_item_name","listview_item_address","listview_item_state"},
				new int[]{R.id.listview_item_name,R.id.listview_item_address,R.id.listview_item_state});
		
		rosterListView.setAdapter(listItemAdapter);
	}
	
	
	public void SetListViewState()
	{		
		ListAdapter adapter=this.rosterListView.getAdapter();
		for(int i=0;i<adapter.getCount();i++)
		{
			HashMap<String,Object> map=(HashMap<String,Object>)adapter.getItem(i);
			if(map.get("listview_item_address").equals(SaveReadWrite.GetConfigure().coupleName))
			{
				if(SaveReadWrite.GetConfigure().coupleState==CoupleStateEnum.BEINVITE)
				{
					map.put("listview_item_state", "be Invited");
				}
				if(SaveReadWrite.GetConfigure().coupleState==CoupleStateEnum.INVITE)
				{
					map.put("listview_item_state", "Invite");
				}
				if(SaveReadWrite.GetConfigure().coupleState==CoupleStateEnum.NOACTION)
				{
					map.put("listview_item_state", "");
				}
				if(SaveReadWrite.GetConfigure().coupleState==CoupleStateEnum.CONNECT)
				{
					map.put("listview_item_state", "connect");
				}
			}
		}
		((SimpleAdapter)adapter).notifyDataSetChanged();
		 
	}

	
	
	
	class ListViewAdapter extends SimpleAdapter
	{
		public String user=null;
		public CoupleStateEnum state;
		public ListViewAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			//convertView.setBackground(background)
			
			LinearLayout item=(LinearLayout) super.getView(position, convertView, parent);
			TextView text=(TextView)item.findViewById(R.id.listview_item_address);
			if(user!=null&&user.equals(text.getText()))
			{
				text.setText("123");
				item.setBackgroundColor(Color.YELLOW);
			}
			else
			{
				item.setBackgroundColor(Color.WHITE);
			}
			
			return super.getView(position, convertView, parent);
		}
		
		
		
	}

	
}


