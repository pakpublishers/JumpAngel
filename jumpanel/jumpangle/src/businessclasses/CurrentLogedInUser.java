package businessclasses;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;




public class CurrentLogedInUser implements Serializable{

	String UserCatagory=null;//motorist,provider
	Object UserObject=null;
	String user_id=null;
	String vehicle_Id=null;
	Vehicle_Info VehicleComplete=null;
	String token=null;
	
	String RequestAmount=null;
	String RequestService=null;
	
	String Lat=null;
	String Long=null;
	String fb_id=null;
	String master_id=null;
	RequestDetails requestDetails=null;
	
	
	static int CurrentLogedInUserAttributeCounts=4;
	
	static int TotalAttributes=6;
	
	public CurrentLogedInUser(){}
	public void setUserDetails(String userCatagory,Object userObject)
	{
		UserCatagory=userCatagory;
		UserObject=userObject;
	}
	public void setuser_id(String val){user_id=val;}
	public void setvehicle_id(String val){vehicle_Id=val;}
	public void settoken(String val){token=val;}
	public void setVehicleComplete(Vehicle_Info val){VehicleComplete=val;}
	public void setLat(String val){Lat=val;}
	public void setLong(String val){Long=val;}
	public void setfb_id(String val){fb_id=val;}
	public void setmaster_id(String val){master_id=val;}
	
	public String getUserCatagory(){return UserCatagory;}
	public Object getUserObject(){return UserObject;}
	public String getuser_id(){return user_id;}
	public String getvehicle_id(){return vehicle_Id;}
	public String gettoken(){return token;}
	public Vehicle_Info getVehicleComplete(){return VehicleComplete;}
	public String getLat(){return Lat;}
	public String getLong(){return Long;}
	public String getfb_id(){return fb_id;}
	public String getmaster_id(){return master_id;}
	
	
	
	public String getRequestAmount(){return RequestAmount;}
	public String getRequestService(){return RequestService;}
	
	public void setRequestAmount(String val){RequestAmount=val;}
	public void setRequestService(String val){RequestService=val;}
	
	public void setrequestDetails(RequestDetails val){requestDetails=val;}
	public RequestDetails getrequestDetails(){return requestDetails;}
	public String getJSONString()
	{
		JSONObject obj=new JSONObject();
		try {obj.put("UserCatagory", UserCatagory);} catch (JSONException e) {}
		
		if(UserCatagory.toLowerCase().contains("provider"))
		{
			Provider p=(Provider)UserObject;
			try {obj.put("UserObject",p.getJsonString() );} catch (JSONException e) {}
		}
		else
		{
			Motorist m=(Motorist)UserObject;
			try {obj.put("UserObject",m.getJSON() );} catch (JSONException e) {}
		}
		
		
		try {obj.put("user_id",user_id );} catch (JSONException e) {}
		try {obj.put("vehicle_Id",vehicle_Id );} catch (JSONException e) {}
		if(VehicleComplete==null)VehicleComplete=new Vehicle_Info();
		try {obj.put("VehicleComplete",VehicleComplete.getJSONString() );} catch (JSONException e) {}
		try {obj.put("token",token );} catch (JSONException e) {}
		try {obj.put("Lat",Lat );} catch (JSONException e) {}
		try {obj.put("Long",Long );} catch (JSONException e) {}
		try {obj.put("fb_id",fb_id );} catch (JSONException e) {}
		try {obj.put("master_id", master_id);} catch (JSONException e) {}
		if(requestDetails==null)requestDetails=new RequestDetails();
		try {obj.put("requestDetails", requestDetails.getJsonString());} catch (JSONException e) {}
		try {obj.put("CurrentLogedInUserAttributeCounts", CurrentLogedInUserAttributeCounts);} catch (JSONException e) {}
		try {obj.put("TotalAttributes", TotalAttributes);} catch (JSONException e) {}
		
		try {obj.put("RequestAmount", RequestAmount);} catch (JSONException e) {}
		try {obj.put("RequestService", RequestService);} catch (JSONException e) {}
		
		return obj.toString();
		
	}
	public void parseJSON(JSONObject obj)
	{
		try{UserCatagory=obj.getString("UserCatagory");}catch(Exception ki){}
		
		
		if(UserCatagory.toLowerCase().contains("provider"))
		{
			Provider p=new Provider();
			try {
				p.parseJSON(new JSONObject(obj.getString("UserObject")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UserObject=(Object)p;
		}
		else
		{
			Motorist m=new Motorist();
			try {
				m.parsJSONOBject(new JSONObject(obj.getString("UserObject")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UserObject=(Object)m;
		}
		
		try{user_id=obj.getString("user_id");}catch(Exception ki){}
		try{vehicle_Id=obj.getString("vehicle_Id");}catch(Exception ki){}
		
		if(VehicleComplete==null)VehicleComplete=new Vehicle_Info();
		try{VehicleComplete.parseObjectFromJSON(new JSONObject(obj.getString("VehicleComplete")));}catch(Exception ki){}
		
		try{RequestAmount=obj.getString("RequestAmount");}catch(Exception ki){}
		try{RequestService=obj.getString("RequestService");}catch(Exception ki){}
		
		try{token=obj.getString("token");}catch(Exception ki){}
		try{Lat=obj.getString("Lat");}catch(Exception ki){}
		try{Long=obj.getString("Long");}catch(Exception ki){}
		try{fb_id=obj.getString("fb_id");}catch(Exception ki){}
		try{master_id=obj.getString("master_id");}catch(Exception ki){}
		try{
			
			String d=obj.getString("requestDetails");
			if(requestDetails==null)requestDetails=new RequestDetails(); 
			requestDetails.parseJSON(new JSONObject(d));
			
		}catch(Exception ki){}
		try{CurrentLogedInUserAttributeCounts=Integer.parseInt(obj.getString("CurrentLogedInUserAttributeCounts"));}catch(Exception ki){}
		try{TotalAttributes=Integer.parseInt(obj.getString("TotalAttributes"));}catch(Exception ki){}
		
	}
	


}