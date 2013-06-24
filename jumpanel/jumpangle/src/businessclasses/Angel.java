package businessclasses;

import java.io.Serializable;

public class Angel implements Serializable{

	public static int selectedangelindex=-1;
	
	String user_id=null;
	String lng=null;
	String lat=null;
	String l_name=null;
	String f_name=null;
	String distance=null;
	String duration=null;
	Vehicle_Info Vehicle=null;
	public String rating=null;
	public String OfferAmount=null;
	String ContactNumber=null;
	
	public String getuser_id(){return user_id;}
	public String getlng(){return lng;}
	public String getlat(){return lat;}
	public String getl_name(){return l_name;}
	public String getf_name(){return f_name;}
	public String getdistance(){return distance;}
	public Vehicle_Info getVehicle(){return Vehicle;}
	public String getduration(){return duration;}
	public String getContactNumber(){return ContactNumber;}
	
	public void setuser_id(String val){ user_id=val;}
	public void setlng(String val){ lng=val;}
	public void setlat(String val){ lat=val;}
	public void setl_name(String val){ l_name=val;}
	public void setf_name(String val){ f_name=val;}
	public void setdistance(String val){ distance=val;}
	public void setVehicle(Vehicle_Info val){ Vehicle=val;}
	public void setduration(String val){duration=val;}
	public void setContactNumber(String val){ContactNumber=val;}
}
