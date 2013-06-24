package businessclasses;

import java.io.Serializable;

import org.json.JSONObject;

import android.os.Parcel;


public class BattryJump implements Serializable{

	Boolean LightDuty=false;
	Boolean MediumDuty=false;
	Boolean HeavyDuty=false;
	
	Boolean isActive=false;
	
	Boolean ApplyVehicleOnly=false;
	
	static int TotalAttributesCount=5;
	
	public String getBattryJumpasParameter()
	{
		String data="";
		if(LightDuty)data+="Light Duty";
		if(MediumDuty)data +=",Medium Duty";
		if(HeavyDuty)data +=",Heavy Duty";
		if(data.charAt(0)==',')
		{
			data=data.substring(1);
		}
		return data;
	}
	
	
	public BattryJump(){}
	
	public BattryJump(Parcel in)
	{
		boolean[] arr=new boolean[TotalAttributesCount];
		in.readBooleanArray(arr);
		fetchDatafromArray(arr);
	}
	public void fetchDatafromArray(boolean[] arr)
	{
		LightDuty=arr[0];
		MediumDuty=arr[1];
		HeavyDuty=arr[2];
		isActive=arr[3];
		ApplyVehicleOnly=arr[4];
		
	}
	public void parseObjectFromJSON(JSONObject json)
	{
		try{LightDuty=Boolean.parseBoolean(json.getString("LightDuty"));}catch(Exception e){}
		try{MediumDuty=Boolean.parseBoolean(json.getString("LightDuty"));}catch(Exception e){}
		try{HeavyDuty=Boolean.parseBoolean(json.getString("LightDuty"));}catch(Exception e){}
		try{isActive=Boolean.parseBoolean(json.getString("LightDuty"));}catch(Exception e){}
		try{ApplyVehicleOnly=Boolean.parseBoolean(json.getString("LightDuty"));}catch(Exception e){}
		
	}
	
	public JSONObject getJSONString()
	{
		JSONObject obj=new JSONObject();
		try{obj.put("LightDuty", LightDuty);}catch(Exception k){}
		try{obj.put("MediumDuty", MediumDuty);}catch(Exception k){}
		try{obj.put("HeavyDuty", HeavyDuty);}catch(Exception k){}
		try{obj.put("isActive", isActive);}catch(Exception k){}
		try{obj.put("ApplyVehicleOnly", ApplyVehicleOnly);}catch(Exception k){}
		
//		String data="{"+
//					"\"LightDuty\":"+"\""+LightDuty+"\","+
//					"\"MediumDuty\":"+"\""+MediumDuty+"\","+
//					"\"HeavyDuty\":"+"\""+HeavyDuty+"\","+
//					"\"isActive\":"+"\""+isActive+"\","+
//					"\"ApplyVehicleOnly\":"+"\""+ApplyVehicleOnly+"\""
//				+"}";
		return obj;
	}
	
	public Boolean getLightDuty(){return LightDuty;}
	public Boolean getMediumDuty(){return MediumDuty;}
	public Boolean getHeavyDuty(){return HeavyDuty;}
	public Boolean getApplyVehicleOnly(){return ApplyVehicleOnly;}
	
	public Boolean isActive(){return isActive;}
	
	
	public void setLightDuty(Boolean val){ LightDuty=val;}
	public void setMediumDuty(Boolean val){ MediumDuty=val;}
	public void setHeavyDuty(Boolean val){ HeavyDuty=val;}
	public void setisActive(Boolean val){isActive=val;}
	public void setApplyVehicleOnly(Boolean val){ApplyVehicleOnly=val;}
	
	public boolean[] getAttributeArray()
	{
		return new boolean[]{LightDuty,MediumDuty,HeavyDuty,isActive,ApplyVehicleOnly};
	}
	
}
