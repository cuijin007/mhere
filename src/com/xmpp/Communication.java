package com.xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class Communication {

	private static String HOST = "talk.google.com";
	private static String SERV = "gmail.com";
	public static String ActionStart="<ACTION>";
	private static int PORT = 5222;
	private static XMPPConnection connection;
	public static XMPPConnection GetConnection()
	{
		if(connection==null||(connection!=null&&!connection.isConnected()))
		{
			ConnectionConfiguration conf = new ConnectionConfiguration(HOST,
					PORT, SERV);
			{ 
				try{
				   Class.forName("org.jivesoftware.smack.ReconnectionManager");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			conf.setReconnectionAllowed(true);
			connection = new XMPPConnection(conf);
			try {
				connection.connect();
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				connection=null;
			}
		}
		return connection;
	}
	
	
}
