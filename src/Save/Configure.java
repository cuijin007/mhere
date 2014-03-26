package Save;

import java.io.Serializable;

public class Configure implements Serializable{

	public String Password="";
	public String Name="";
	public String coupleName="";
	public CoupleStateEnum coupleState=CoupleStateEnum.NOACTION;
}
