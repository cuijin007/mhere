package Save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.SharedPreferences;

public class SaveReadWrite {

	private static String fileName="mnt/sdcard/confFile";
	private static Configure configure;
	public static boolean Init()
	{
		File file=new File(fileName);
		if(!file.exists())
		{
			try {
				file.createNewFile();
				GetConfigure();
				WriteConfigure();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			finally
			{
				
			}
		}
		return true;
	}
	public static  boolean WriteConfigure()
	{
		FileOutputStream fp=null;
		ObjectOutputStream op = null;
		try
		{
			fp=new FileOutputStream(fileName);
			op=new ObjectOutputStream(fp);
			op.writeObject(GetConfigure());
		}
		catch(Exception ee)
		{
			return false;
		}
		finally
		{
			if(op!=null)
			{
				try {
					op.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fp!=null)
			{
				try {
					fp.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	public static Configure GetConfigure()
	{
		if(configure==null)
		{
			configure=new Configure();
		}
		return configure;
	}
	public static boolean ReadConfigure()
	{
		FileInputStream fs = null;
		ObjectInputStream or = null;
		try
		{
			
			fs=new FileInputStream(fileName);
			or=new ObjectInputStream(fs);
			configure=(Configure) or.readObject();
		}
		catch(Exception ee)
		{
			return false;
		}
		finally
		{
			if(or!=null)
			{
				try {
					or.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fs!=null)
			{
				try {
					fs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}

		}
		return true;
	}
	
}
