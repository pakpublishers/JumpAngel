package businessclasses;

import java.io.Serializable;

import org.json.JSONObject;


import android.os.Parcel;
import android.os.Parcelable;

public class TowService implements Serializable{

	BattryJump Boom;//=false;
	BattryJump Flat_Bed;//=false;
	BattryJump Sling;//=false;
	
	BattryJump WheelLift;//=false;
	BattryJump Winch;//=false;
	
	Boolean ApplyVehicleOnly=false;
	
	static int TotalAttributesCount=5;
	
	
	
	public TowService(){}
	
	public TowService(Parcel in)
	{
		Parcelable[] arr=new Parcelable[TotalAttributesCount];
		arr= in.readParcelableArray(BattryJump.class.getClassLoader());
		fetchDatafromArray(arr);
	}
	public void fetchDatafromArray(Parcelable[] arr)
	{
		Boom=(BattryJump)arr[0];
		Flat_Bed=(BattryJump)arr[1];
		Sling=(BattryJump)arr[2];
		WheelLift=(BattryJump)arr[3];
		Winch=(BattryJump)arr[4];
		//ApplyVehicleOnly=arr[5];
		
	}
	public JSONObject getJSON()
	{
		JSONObject obj=new JSONObject();
		try{obj.put("TowServiceBoom", Boom.getJSONString());}catch(Exception k){}
		try{obj.put("TowServiceFlat_Bed", Flat_Bed.getJSONString());}catch(Exception k){}
		try{obj.put("TowServiceSling", Sling.getJSONString());}catch(Exception k){}
		try{obj.put("TowServiceWheelLift", WheelLift.getJSONString());}catch(Exception k){}
		try{obj.put("TowServiceWinch", Winch.getJSONString());}catch(Exception k){}
		try{obj.put("TowServiceApplyVehicleOnly", ApplyVehicleOnly);}catch(Exception k){}
		
//		String data="{"+
//				"\"TowServiceBoom\":"+""+((Boom!=null)?Boom.getJSONString():"\"null\"")+","+
//				"\"TowServiceFlat_Bed\":"+""+((Flat_Bed!=null)?Flat_Bed.getJSONString():"\"null\"")+","+
//				"\"TowServiceSling\":"+""+((Sling!=null)?Sling.getJSONString():"\"null\"")+","+
//				"\"TowServiceWheelLift\":"+""+((WheelLift!=null)?WheelLift.getJSONString():"\"null\"")+","+
//				"\"TowServiceWinch\":"+""+((Winch!=null)?Winch.getJSONString():"\"null\"")+","+
//				"\"TowServiceApplyVehicleOnly\":"+"\""+ApplyVehicleOnly+"\""
//				+"}";
		return obj;
	}
	public void parseJSON(JSONObject json)
	{
		try{Boom.parseObjectFromJSON(json.getJSONObject("TowServiceBoom"));}catch(Exception k){}
		try{Flat_Bed.parseObjectFromJSON(json.getJSONObject("TowServiceFlat_Bed"));}catch(Exception k){}
		try{Sling.parseObjectFromJSON(json.getJSONObject("TowServiceSling"));}catch(Exception k){}
		try{WheelLift.parseObjectFromJSON(json.getJSONObject("TowServiceWheelLift"));}catch(Exception k){}
		try{Winch.parseObjectFromJSON(json.getJSONObject("TowServiceWinch"));}catch(Exception k){}
		try{ApplyVehicleOnly=Boolean.parseBoolean(json.getString("TowServiceApplyVehicleOnly"));}catch(Exception k){}
		
		
	}
	
	public BattryJump getBoom(){return Boom;}
	public BattryJump getFlat_Bed(){return Flat_Bed;}
	public BattryJump getSling(){return Sling;}
	public Boolean getApplyVehicleOnly(){return ApplyVehicleOnly;}
	public BattryJump getWheelLift(){return WheelLift;}
	public BattryJump getWinch(){return Winch;}
	
	public void setBoom(BattryJump val){ Boom=val;}
	public void setFlat_Bed(BattryJump val){ Flat_Bed=val;}
	public void setSling(BattryJump val){ Sling=val;}
	public void setApplyVehicleOnly(Boolean val){ApplyVehicleOnly=val;}
	public void setWheelLift(BattryJump val){WheelLift=val;}
	public void setWinch(BattryJump val){Winch=val;}
	
	public BattryJump[] getAttributeArray()
	{
		return new BattryJump[]{Boom,Flat_Bed,Sling,WheelLift,Winch};
		
	}
	 
}
